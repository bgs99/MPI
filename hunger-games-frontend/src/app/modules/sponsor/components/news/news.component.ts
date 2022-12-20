import { Component, OnInit } from '@angular/core';
import { News } from 'src/app/models/news';
import { SponsorsService } from 'src/app/services/sponsors.service';

@Component({
    selector: 'app-news',
    templateUrl: './news.component.html'
})
export class NewsComponent implements OnInit {
    news: News[] = [];

    constructor(
        private sponsorService: SponsorsService,
    ) { }

    async ngOnInit(): Promise<void> {
        this.news = await this.sponsorService.getNews();
    }
}
