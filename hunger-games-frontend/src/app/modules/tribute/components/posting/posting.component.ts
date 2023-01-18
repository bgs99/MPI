import { Component, ViewChild } from '@angular/core';
import { MatStepper } from '@angular/material/stepper';
import { OrderId } from 'src/app/models/order';
import { pay } from 'src/app/services/mock/payment.service';
import { TributesService } from 'src/app/services/tributes.service';

@Component({
    templateUrl: './posting.component.html',
    styleUrls: ['./posting.component.css']
})
export class PostingComponent {
    @ViewChild('stepper') stepper!: MatStepper;

    htmlContent: string = "";
    paymentSucceded: boolean | null = null;
    savedOrderId: OrderId | null = null;

    constructor(private tributesService: TributesService) { }

    async order(): Promise<void> {
        if (this.htmlContent === '') {
            return;
        }
        const text: string = this.htmlContent;
        this.htmlContent = '';

        const paymentData = await this.tributesService.orderAd(text);
        this.savedOrderId = paymentData.orderId;
        this.stepper.next();
        await this.retry();
    }

    async retry(): Promise<void> {
        if (this.savedOrderId === null) {
            return;
        }
        try {
            this.paymentSucceded = await pay(this.savedOrderId);
        }
        catch (err: any) {
            console.error(err);
        }
    }

    anotherPost(): void {
        this.paymentSucceded = null
        this.stepper.previous();
    }
}
