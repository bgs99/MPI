import { Component, OnInit } from '@angular/core';
import { Ad } from 'src/app/models/ad';
import { ModeratorService } from 'src/app/services/moderator.service';

@Component({
    templateUrl: './review.component.html',
})
export class ApproveComponent implements OnInit {
    ad: Ad | null = null;

    constructor(private moderatorService: ModeratorService) { }

    async fetchNextAd(): Promise<void> {
        try {
            this.ad = await this.moderatorService.nextAd();
        } catch (err: any) {
            this.ad = null;
        }
    }

    async ngOnInit(): Promise<void> {
        await this.fetchNextAd();
    }

    async review(approved: boolean): Promise<void> {
        if (this.ad === null) {
            return;
        }
        await this.moderatorService.review(this.ad.orderId, approved);
        await this.fetchNextAd();
    }
}
