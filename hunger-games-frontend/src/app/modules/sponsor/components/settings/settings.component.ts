import { Component, OnInit } from '@angular/core';
import { pay } from 'src/app/services/mock/payment.service';
import { SponsorsService } from 'src/app/services/sponsors.service';
import { UserService } from 'src/app/services/user.service';

@Component({
    templateUrl: './settings.component.html',
    styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {
    oldEmail: string | null = null;
    email: string = '';
    subscriptionPrice: number | null = null;
    emailError: string | null = null;

    constructor(
        private userService: UserService,
        private sponsorService: SponsorsService,
    ) { }

    async ngOnInit(): Promise<void> {
        this.oldEmail = await this.userService.getEmail();
        if (!await this.sponsorService.isSubscribed()) {
            this.subscriptionPrice = await this.sponsorService.getSubscriptionPrice();
        }
        console.info(`${this.oldEmail} ${this.subscriptionPrice}`);
    }

    async changeEmail() {
        this.emailError = null;
        try {
            await this.userService.setEmail(this.email);
        } catch (err: any) {
            this.emailError = "Неправильный адрес электронной почты";
        }
        this.oldEmail = await this.userService.getEmail();
    }

    async subscribe() {
        const order = await this.sponsorService.subscribe(this.oldEmail!);
        pay(order.orderId);
        this.subscriptionPrice = null;
    }
}
