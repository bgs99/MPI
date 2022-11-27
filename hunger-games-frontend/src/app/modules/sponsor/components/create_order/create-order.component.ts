import { Component, ViewChild } from '@angular/core';
import { Tribute } from 'src/app/models/tribute';
import { MatStepper } from '@angular/material/stepper';
import { Resource } from 'src/app/models/resource';
import { SponsorsService } from 'src/app/services/sponsors.service';
import { PaymentResult } from 'src/app/models/payment';

@Component({
    templateUrl: './create-order.component.html',
})
export class CreateOrderComponent {
    @ViewChild('stepper') stepper!: MatStepper;

    tribute: Tribute | null = null;

    paymentEnabled: boolean = true;
    paymentSucceded: boolean | undefined = undefined;
    orderId: string | null = null;

    constructor(private sponsorsService: SponsorsService) { }

    selectTribute(tribute: Tribute): void {
        this.tribute = tribute;
        this.stepper.next();
    }

    async pay(resources: Resource[]): Promise<void> {
        if (this.tribute === null) {
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
            console.log(err);
            this.paymentEnabled = true;
        }
    }

    editResources(): void {
        this.stepper.previous();
    }
}
