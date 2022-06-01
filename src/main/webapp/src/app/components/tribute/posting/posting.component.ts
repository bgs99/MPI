import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PaymentData } from 'src/app/models/payment-data';
import { TributesService } from 'src/app/services/tributes.service';

@Component({
  selector: 'app-posting',
  templateUrl: './posting.component.html',
  styleUrls: ['./posting.component.css']
})
export class PostingComponent implements OnInit {
  htmlContent: string = "";
  selfId: number | null = null;

  order(): void {
    if (this.selfId === null) {
      return;
    }
    const text: string = this.htmlContent;
    this.htmlContent = '';
    this.tributesService.orderAd(this.selfId, text).subscribe({
      next: (paymentData: PaymentData) => {
        console.log("Got payment data: " + JSON.stringify(paymentData));
        localStorage.setItem('htmlContent', this.htmlContent);
        localStorage.setItem('order', JSON.stringify(paymentData.orderId));
        this.router.navigate(["/mock/payment"], { queryParams: { id: paymentData.orderId, path: '/tribute/posting' } });
      },
      error: (err) => {
        console.log(err);
      }
    })
  }

  constructor(private router: Router, private tributesService: TributesService) { }

  ngOnInit(): void {
    const selfId: string | null = localStorage.getItem("identity");
    if (selfId === null) {
      return;
    }
    this.selfId = parseInt(selfId);
  }

}
