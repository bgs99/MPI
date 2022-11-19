import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PaymentService } from 'src/app/services/mock/payment.service';

@Component({
    selector: 'app-payment',
    templateUrl: './payment.component.html',
    styleUrls: ['./payment.component.css']
})
export class PaymentComponent implements OnInit {
    orderId: number | null = null;
    path: string | null = null;

    constructor(private route: ActivatedRoute, private router: Router, private paymentService: PaymentService) { }

    async approve(): Promise<void> {
        if (this.orderId === null || this.path === null) {
            return;
        }
        try {
            await this.paymentService.approve(this.orderId);
            this.router.navigateByUrl(this.path + '/success');
        }
        catch (err: any) {
            console.log(err);
        }
    }

    deny(): void {
        if (this.orderId === null || this.path === null) {
            return;
        }
        this.router.navigateByUrl(this.path + '/failure');
    }

    ngOnInit(): void {
        this.route.queryParams.subscribe({
            next: (params) => {
                const idParam: string | null = params['id'];
                this.orderId = idParam === null ? null : parseInt(idParam);
                this.path = params['path'];
                console.log("Got route params " + this.orderId + ", " + this.path);
            },
            error: (err) => {
                console.log(err);
            }
        });
    }

}
