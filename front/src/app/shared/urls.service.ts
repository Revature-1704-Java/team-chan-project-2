import { Injectable } from '@angular/core';

@Injectable()
export class UrlsService {
  imageBasePath = 'http://s3.amazonaws.com/moirai/';
  serverBasePath = 'http://34.234.202.22:8181';
  placeholderPostImage = 'http://placehold.it/150x150';
  placeholderCommentImage = 'http://placehold.it/100x100';
}
