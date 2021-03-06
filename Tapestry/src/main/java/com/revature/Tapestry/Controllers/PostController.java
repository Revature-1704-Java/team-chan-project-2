package com.revature.Tapestry.Controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.revature.Tapestry.DatabaseAccessors.BoardDAO;
import com.revature.Tapestry.DatabaseAccessors.CommentDAO;
import com.revature.Tapestry.DatabaseAccessors.PostDAO;
import com.revature.Tapestry.DatabaseAccessors.UserDAO;
import com.revature.Tapestry.beans.Board;
import com.revature.Tapestry.beans.Comment;
import com.revature.Tapestry.beans.Post;
import com.revature.Tapestry.beans.User;

@RestController
@CrossOrigin
public class PostController {

	private PostDAO postDao;
	private BoardDAO boardDao;
	private UserDAO userDao;
	private CommentDAO commentDao;
	
	public PostController(PostDAO postDao, BoardDAO boardDao, UserDAO userDao, CommentDAO commentDao) {
		this.postDao = postDao;
		this.boardDao = boardDao;
		this.userDao = userDao;
		this.commentDao = commentDao;
	}
	
	//Get a thread. Post and all replies
	@PostMapping(value="/getPost", produces=MediaType.APPLICATION_JSON_VALUE)
	public Post getPosts(@RequestParam("postId") int postId) {
		return postDao.findOne(postId);
	}
	
	//Get all the posts in a board
	@PostMapping(value="/getPosts", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Post> getPost(@RequestParam("boardName") String boardName) {
		List<Post> postsReturned = new ArrayList<>();
		List<Post> list = postDao.findAll();
		for (Post p : list) {
			List<Board> boards = p.getBoardsPosted();
			if (boards == null) {
				System.out.println("no boards found");
			}
			for(Board b : boards) {
				if (boardName.equals(b.getBoardName())) {
					postsReturned.add(p);
				}
			}	
		}
		return postsReturned;
		
		/* //get s3client
		String bucketName = "bucketOfPhotos";
		AWSCredentialsProvider credentials = new AWSStaticCredentialsProvider 
				(new BasicAWSCredentials(System.getenv("ACCESSKEY"), System.getenv("SECRETKEY")));
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(credentials)
                .build();
                
         //get image from s3
         //key should be the image path field of the post/comment object
		String key = null;
        S3Object imageToReturn = s3Client.getObject(bucketName, key);
        InputStream in = imageToReturn.getObjectContent();
        byte[] buf = new byte[1024];
        OutputStream out = null;							//this is where the object goes, what type of output stream is needed to send to response
        int count;
		try {
			while( (count = in.read(buf)) != -1)
			{
			   if( Thread.interrupted() )
			   {
			       throw new InterruptedException();
			   }
			   out.write(buf, 0, count);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
															//image is in out, do your stuff here, closing streams next
		
        try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        try {
			imageToReturn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        */		
	}	
	
	@PostMapping(value="/createThread", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public void submitPost(@RequestParam(value="type", required=true) String type, @RequestParam(value="userId", required=true) String userId,
			@RequestParam(value="title", required = false) String title, @RequestParam(value="body", required=true) String textContent, 
			@RequestParam(value="file", required=false) MultipartFile inputImage, @RequestParam(value="board", required=false) String board,
			@RequestParam(value="postID", required=false) String postID)
	{
		String bucketName = "moirai";
		//AWSCredentialsProvider credentials = new AWSStaticCredentialsProvider 
		//		(new BasicAWSCredentials(System.getenv("ACCESSKEY"), System.getenv("SECRETKEY")));
		AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
		
		Integer uploaderId = Integer.parseInt(userId);
		User uploader = userDao.findOne(uploaderId);
		String key = null;
		if(inputImage != null && !inputImage.isEmpty())//only post image if one is sent
		{
			String alteredEmail = uploader.getEmail().replace('@', '.');
			//code to insert an image to s3
			key = "" + alteredEmail + "/" + inputImage.getOriginalFilename();
			InputStream image;
			try {
				image = inputImage.getInputStream();
				PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, image, new ObjectMetadata());
				s3Client.putObject(putObjectRequest);
			} catch (IOException e) {
			}
			
			if(type.equals("post"))
			{
				if(board != null)
				{
					Board boardPosted = boardDao.findByBoardName(board);
					if (boardPosted != null) 
					{
						List<Board> boardsPosted = new ArrayList<Board>();
						boardsPosted.add(boardPosted);
						Post postToSubmit = new Post(uploader, key, textContent, new Date(), title, boardsPosted);
						postDao.save(postToSubmit);
						List<Post> postsOnBoard = boardPosted.getThreads();
						postsOnBoard.add(postToSubmit);
						boardPosted.setThreads(postsOnBoard);
						boardDao.save(boardPosted);
					}
				}
			}
			else if (type.equals("comment"))
			{
				Comment commentToSubmit = new Comment(uploader, key, textContent, new Date());
				Post parentPost = postDao.findOne(Integer.parseInt(postID));
				List<Comment> parentPostReplies = parentPost.getReplies();
				parentPostReplies.add(commentToSubmit);
				parentPost.setReplies(parentPostReplies);
				commentDao.save(commentToSubmit);
				postDao.save(parentPost);
			}
		}
		//file null post comment
		else {
			Comment commentToSubmit = new Comment(uploader, key, textContent, new Date());
			Post parentPost = postDao.findOne(Integer.parseInt(postID));
			List<Comment> parentPostReplies = parentPost.getReplies();
			parentPostReplies.add(commentToSubmit);
			parentPost.setReplies(parentPostReplies);
			commentDao.save(commentToSubmit);
			postDao.save(parentPost);
		}
	}
}
