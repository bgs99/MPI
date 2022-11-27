import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PaymentResult } from 'src/app/models/payment';
import { PaymentService } from 'src/app/services/mock/payment.service';

@Component({
    selector: 'app-mock-payment',
    templateUrl: './payment.component.html',
    styleUrls: ['./payment.component.css']
})
export class PaymentComponent implements OnInit {
    orderId: string | null = null;
    readonly opener: Window = window.opener;

    constructor(private route: ActivatedRoute, private paymentService: PaymentService) { }

    async approve(): Promise<void> {
        if (this.orderId === null) {
            return;
        }

        try {
            await this.paymentService.approve(this.orderId);
            opener.postMessage(new PaymentResult(this.orderId, true), '*');
            window.close();
        }
        catch (err: any) {
            console.log(err);
        }
    }

    async deny(): Promise<void> {
        if (this.orderId === null) {
            return;
        }
        opener.postMessage(new PaymentResult(this.orderId, false), '*');
        window.close();
    }

    async ngOnInit(): Promise<void> {
        try {
            const params = this.route.snapshot.queryParams;

            this.orderId = params['id'];;
        }
        catch (err: any) {
            console.error(err);
        }
    }
}
