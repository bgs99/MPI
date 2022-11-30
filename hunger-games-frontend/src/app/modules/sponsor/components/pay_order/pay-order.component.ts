import { Component, OnInit, ViewChild } from '@angular/core';
import { MatStepper } from '@angular/material/stepper';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute } from '@angular/router';
import { Order, OrderResource } from 'src/app/models/order';
import { PaymentResult } from 'src/app/models/payment';
import { Tribute } from 'src/app/models/tribute';
import { ResourcesService } from 'src/app/services/resources.service';
import { SponsorsService } from 'src/app/services/sponsors.service';
import { TributesService } from 'src/app/services/tributes.service';

@Component({
    templateUrl: './pay-order.component.html',
})
export class PayOrderComponent implements OnInit {
    @ViewChild('stepper') stepper!: MatStepper;

    constructor(private route: ActivatedRoute, private tributesService: TributesService, private sponsorsService: SponsorsService, private resourcesService: ResourcesService) { }

    orders = new MatTableDataSource<Order>([]);

    readonly ordersColumns: string[] = ['resources', 'total', 'action'];

    ordersMap: Map<string, Order[]> = new Map();
    priceMap: Map<string, number> = new Map();

    tribute: Tribute | undefined;

    paymentEnabled: boolean = true;
    paymentSucceded: boolean | undefined = undefined;
    order: Order | null = null;

    total(resources: OrderResource[]): number {
        let result = 0;
        resources.forEach(resource => {
            const price = this.priceMap.get(resource.name);
            if (price !== undefined) {
                result += price * resource.size;
            }
        });
        return result;
    }

    async pay(order: Order): Promise<void> {
        if (this.tribute === null) {
            return;
        }
        try {
            this.paymentEnabled = false;
            this.order = order;
            window.addEventListener('message', (event: MessageEvent) => {
                if (event.origin !== window.origin) { // Capitol origin
                    return;
                }
                const data: PaymentResult = event.data;
                if (data.orderId != order.orderId) {
                    return;
                }
                this.paymentSucceded = data.success;
                this.paymentEnabled = true;
            });
            window.open(`/capitol/payment?id=${order.orderId}`);
            this.stepper.next();
        }
        catch (err: any) {
            console.error(err);
            this.paymentEnabled = true;
        }
    }

    async ngOnInit(): Promise<void> {
        try {
            this.ordersMap = await this.sponsorsService.resourcesToPayFor();
            const resources = await this.resourcesService.getResources();
            resources.forEach(resource => {
                this.priceMap.set(resource.name, resource.price);
            });
            const tributeId = this.route.snapshot.paramMap.get('tribute')!;
            this.tribute = (await this.tributesService.getTribute(tributeId))!;
        } catch (err: any) {
            console.error(err);
        }
    }

}
