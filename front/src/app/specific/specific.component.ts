import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { share } from 'rxjs/operators';

import { CurrentViewService } from '../shared/current-view.service';
import { ThreadService } from '../shared/thread.service';
import { Thread } from '../shared/thread';

@Component({
  selector: 'app-specific',
  templateUrl: './specific.component.html',
  styleUrls: ['./specific.component.css']
})
export class SpecificComponent implements OnInit {
  public replies: Observable<Array<Thread>>;
  public post: Thread;

  constructor(private threadService: ThreadService, public curView: CurrentViewService) {
    this.replies = this.threadService.getReplies(this.curView.id).pipe(share());
  }

  ngOnInit(): void {
    //this.threadService.getReplies(this.curView.id);
  }

  viewCatalog(): void {
    this.curView.view = 'catalog';
  }

  postTrackByFn(index: number, thread: Thread): number {
    return thread.commentID;
  }
}
