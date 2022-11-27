import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { PaymentService } from 'src/app/services/mock/payment.service';

@Component({
    templateUrl: './payment.component.html',
    styleUrls: ['./payment.component.css']
})
export class PaymentComponent implements OnInit {
    orderId: string | null = null;
    path: string | null = null;

    constructor(private route: ActivatedRoute, private router: Router, private paymentService: PaymentService) { }

    async approve(): Promise<void> {
        if (this.orderId === null || this.path === null) {
            return;
        }

        try {
            await this.paymentService.approve(this.orderId);
            await this.router.navigate([this.path, 'success']);
        }
        catch (err: any) {
            console.log(err);
        }
    }

    async deny(): Promise<void> {
        if (this.orderId === null || this.path === null) {
            return;
        }
        await this.router.navigate([this.path, 'failure']);
    }

    async ngOnInit(): Promise<void> {
        try {
            const params = this.route.snapshot.queryParams;

            const idParam: string | null = params['id'];
            this.orderId = idParam;
            this.path = params['path'];
            console.log(`Got route params ${this.orderId}, ${this.path}`);
        }
        catch (err: any) {
            console.error(err);
        }
    }

}
