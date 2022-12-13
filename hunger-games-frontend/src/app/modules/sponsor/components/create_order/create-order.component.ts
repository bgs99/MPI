import { Component, OnInit, ViewChild } from '@angular/core';
import { Tribute, TributeId } from 'src/app/models/tribute';
import { MatStepper } from '@angular/material/stepper';
import { Resource } from 'src/app/models/resource';
import { SponsorsService } from 'src/app/services/sponsors.service';
import { PaymentResult } from 'src/app/models/payment';
import { ActivatedRoute, Router } from '@angular/router';
import { TributesService } from 'src/app/services/tributes.service';
import { OrderId } from 'src/app/models/order';
import { ChatId } from 'src/app/models/chat';
import { ChatService } from 'src/app/services/chat.service';
import { pay } from 'src/app/services/mock/payment.service';

@Component({
    templateUrl: './create-order.component.html',
})
export class CreateOrderComponent implements OnInit {
    @ViewChild('stepper') stepper!: MatStepper;

    tribute: Tribute | undefined;

    paymentEnabled: boolean = true;
    paymentSucceded: boolean | undefined = undefined;
    chatId: ChatId | null = null;

    total: number = 0;

    constructor(
        private route: ActivatedRoute,
        private sponsorsService: SponsorsService,
        private tributesService: TributesService,
        private router: Router,
        private chatService: ChatService,
    ) { }

    async ngOnInit(): Promise<void> {
        const tributeId = this.route.snapshot.paramMap.get('tribute')! as TributeId;
        this.tribute = (await this.tributesService.getTribute(tributeId))!;

        this.chatId = this.route.snapshot.paramMap.get('chatId') as ChatId | null;
    }

    async pay(resources: Resource[]): Promise<void> {
        if (this.tribute === undefined) {
            return;
        }
        try {
            this.paymentEnabled = false;
            const paymentData = await this.sponsorsService.orderResources(this.tribute.id, resources);
            this.paymentSucceded = await pay(paymentData.orderId);
            this.paymentEnabled = true;
            if (this.paymentSucceded && this.chatId !== null) {
                this.chatService.addPendingMessage(this.chatId, `/${paymentData.orderId}`);
                await this.router.navigate(['sponsor', 'chat', this.chatId]);
            } else {
                this.stepper.next();
            }
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
