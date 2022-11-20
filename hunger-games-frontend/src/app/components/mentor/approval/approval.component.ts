import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Order } from 'src/app/models/order';
import { MentorsService } from 'src/app/services/mentors.service';

@Component({
    selector: 'app-approval',
    templateUrl: './approval.component.html',
    styleUrls: ['./approval.component.css']
})
export class ApprovalComponent implements OnInit {
    ordersColumns: string[] = ['tributeName', 'sponsorName', 'resources', 'action'];
    orders = new MatTableDataSource<Order>([]);

    constructor(
        private mentorsService: MentorsService) { }

    async approve(order: Order): Promise<void> {
        try {
            await this.mentorsService.approve(order.orderId, true);
            this.orders.data = this.orders.data.filter((elem) => elem.orderId != order.orderId);
        }
        catch (err) {
            console.log(err);
        }
    }

    async deny(order: Order): Promise<void> {
        try {
            await this.mentorsService.approve(order.orderId, false);
            this.orders.data = this.orders.data.filter((elem) => elem.orderId != order.orderId);
        }
        catch (err) {
            console.log(err);
        }
    }

    async ngOnInit(): Promise<void> {
        try {
            this.orders.data = await this.mentorsService.getOrders();
        }
        catch (err: any) {
            console.log(err)
        }
    }

}
