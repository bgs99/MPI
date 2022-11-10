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

  selfId: number | null = null;

  constructor(private mentorsService: MentorsService) { }

  approve(order: Order): void {
    if (this.selfId === null) {
      return;
    }
    this.mentorsService.approve(this.selfId, order.orderId, true).subscribe({
      next: () => {
        this.orders.data = this.orders.data.filter((elem) => elem.orderId != order.orderId);
      },
      error: (err) => {
        console.log(err);
      }
    });
  }

  deny(order: Order): void {
    if (this.selfId === null) {
      return;
    }
    this.mentorsService.approve(this.selfId, order.orderId, false).subscribe({
      next: () => {
        this.orders.data = this.orders.data.filter((elem) => elem.orderId != order.orderId);
      },
      error: (err) => {
        console.log(err);
      }
    });
  }

  ngOnInit(): void {
    const selfId: string | null = localStorage.getItem("identity");
    if (selfId === null) {
      return;
    }
    this.selfId = parseInt(selfId);
    this.mentorsService.getOrders(this.selfId)
      .subscribe({
        next: (data: Order[]) => {
          console.log("Received data " + JSON.stringify(data));
          this.orders.data = data;
        },
        error: (err: any) => {
          console.log(err)
        }
      });
  }

}
