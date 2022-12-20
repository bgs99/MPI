import { Component } from '@angular/core';
import { ModeratorService } from 'src/app/services/moderator.service';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html'
})
export class PostComponent {
    title: string = '';
    htmlContent: string = '';

    constructor(
        private moderatorService: ModeratorService,
    ) { }

    async post() {
        await this.moderatorService.post(this.title, this.htmlContent);
        this.htmlContent = '';
        this.title = '';
    }
}
