import { Component, OnInit } from '@angular/core';
import { pay } from 'src/app/services/mock/payment.service';
import { SponsorsService } from 'src/app/services/sponsors.service';

@Component({
    selector: 'app-settings',
    templateUrl: './settings.component.html'
})
export class SettingsComponent implements OnInit {
    oldEmail: string = '';
    email: string = '';
    subscriptionPrice: number | null = null;

    constructor(
        private sponsorService: SponsorsService,
    ) { }

    async ngOnInit(): Promise<void> {
        this.oldEmail = await this.sponsorService.getEmail();
        if (!await this.sponsorService.isSubscribed()) {
            this.subscriptionPrice = await this.sponsorService.getSubscriptionPrice();
        }
    }

    async changeEmail() {
        await this.sponsorService.setEmail(this.email);
    }

    async subscribe() {
        const order = await this.sponsorService.subscribe(this.oldEmail);
        pay(order.orderId);
        this.subscriptionPrice = null;
    }
}
