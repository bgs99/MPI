import { Component, OnInit, ViewChild } from '@angular/core';
import { Tribute, TributeId } from 'src/app/models/tribute';
import { MatStepper } from '@angular/material/stepper';
import { Resource } from 'src/app/models/resource';
import { SponsorsService } from 'src/app/services/sponsors.service';
import { PaymentResult } from 'src/app/models/payment';
import { ActivatedRoute } from '@angular/router';
import { TributesService } from 'src/app/services/tributes.service';
import { OrderId } from 'src/app/models/order';

@Component({
    templateUrl: './create-order.component.html',
})
export class CreateOrderComponent implements OnInit {
    @ViewChild('stepper') stepper!: MatStepper;

    tribute: Tribute | undefined;

    paymentEnabled: boolean = true;
    paymentSucceded: boolean | undefined = undefined;
    orderId: OrderId | null = null;

    constructor(private route: ActivatedRoute, private sponsorsService: SponsorsService, private tributesService: TributesService) { }

    async ngOnInit(): Promise<void> {
        const tributeId = this.route.snapshot.paramMap.get('tribute')! as TributeId;
        this.tribute = (await this.tributesService.getTribute(tributeId))!;
    }

    async pay(resources: Resource[]): Promise<void> {
        if (this.tribute === undefined) {
            return;
        }
        try {
            this.paymentEnabled = false;
            const paymentData = await this.sponsorsService.orderResources(this.tribute.id, resources);
            this.orderId = paymentData.orderId;
            window.addEventListener('message', (event: MessageEvent) => {
                if (event.origin !== window.origin) { // Capitol origin
                    return;
                }
                const data: PaymentResult = event.data;
                if (data.orderId != paymentData.orderId) {
                    return;
                }
                this.paymentSucceded = data.success;
                this.paymentEnabled = true;
            });
            window.open(`/capitol/payment?id=${paymentData.orderId}`);
            this.stepper.next();
        }
        catch (err: any) {
            console.error(err);
            this.paymentEnabled = true;
        }
    }

    editResources(): void {
        this.stepper.previous();
    }
}
