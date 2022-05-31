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

  constructor(private route: ActivatedRoute, private router: Router, private paymentService: PaymentService) { }

  approve(): void {
    if (this.orderId === null) {
      return;
    }
    this.paymentService.approve(this.orderId)
      .subscribe({
        next: () => {
          this.router.navigateByUrl('/sponsoring/success');
        },
        error: (err) => {
          console.log(err);
        }
      });
  }

  deny(): void {
    this.router.navigateByUrl('/sponsoring/failure');
  }

  ngOnInit(): void {
    this.route.params.subscribe(event => {
      this.orderId = parseInt(event['id']);
    });
  }

}
