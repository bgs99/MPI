import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { pay } from 'src/app/services/mock/payment.service';
import { TributesService } from 'src/app/services/tributes.service';

@Component({
    selector: 'app-posting',
    templateUrl: './posting.component.html',
    styleUrls: ['./posting.component.css']
})
export class PostingComponent {
    htmlContent: string = "";

    constructor(private router: Router, private tributesService: TributesService) { }

    async order(): Promise<void> {
        const text: string = this.htmlContent;
        this.htmlContent = '';

        const paymentData = await this.tributesService.orderAd(text);

        const success = await pay(paymentData.orderId);
    }
}
