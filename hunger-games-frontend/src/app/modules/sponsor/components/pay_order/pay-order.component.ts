import { Component, OnInit, ViewChild } from '@angular/core';
import { MatStepper } from '@angular/material/stepper';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute } from '@angular/router';
import { Order, OrderResource } from 'src/app/models/order';
import { PaymentResult } from 'src/app/models/payment';
import { Tribute, TributeId } from 'src/app/models/tribute';
import { pay } from 'src/app/services/mock/payment.service';
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
        this.paymentEnabled = false;
        this.order = order;
        this.stepper.next();
        try {

            this.paymentSucceded = await pay(order.orderId);
            this.paymentEnabled = true;
        }
        catch (err: any) {
            console.error(err);
            this.paymentEnabled = true;
        }
    }

    async ngOnInit(): Promise<void> {
        try {
            const tributeId = this.route.snapshot.paramMap.get('tribute')! as TributeId;
            this.tribute = (await this.tributesService.getTribute(tributeId))!;

            const ordersMap = await this.sponsorsService.resourcesToPayFor();

            this.orders.data = ordersMap.get(this.tribute.name)!;
            const resources = await this.resourcesService.getResources();
            resources.forEach(resource => {
                this.priceMap.set(resource.name, resource.price);
            });
        } catch (err: any) {
            console.error(err);
        }
    }

}
