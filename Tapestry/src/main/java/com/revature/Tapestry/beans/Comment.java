package com.revature.Tapestry.beans;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ElementCollection;

@Entity
public class Comment {
	private Integer commentID;
	private Integer userID;
	//private String imagePath;
	//private String textContent;
	//private Date postTime;
	//List<Comment> replies;
	
	@Id
	@Column
	public Integer getCommentID() {
		return commentID;
	}
	public void setCommentID(Integer commentID) {
		this.commentID = commentID;
	}
	@Column
	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) {
		this.userID = userID;
	}
	/*@Column
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	@Column
	public String getTextContent() {
		return textContent;
	}
	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}
	@Column
	public Date getPostTime() {
		return postTime;
	}
	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}
	@ElementCollection
	public List<Comment> getReplies() {
		return replies;
	}
	public void setReplies(List<Comment> replies) {
		this.replies = replies;
	}*/
}
