import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { TributesService } from 'src/app/services/tributes.service';

@Component({
    selector: 'app-posting',
    templateUrl: './posting.component.html',
    styleUrls: ['./posting.component.css']
})
export class PostingComponent {
    htmlContent: string = "";

    constructor(private router: Router, private tributesService: TributesService) { }

    async order(): Promise<void> {
        const text: string = this.htmlContent;
        this.htmlContent = '';
        try {
            const paymentData = await this.tributesService.orderAd(text)
            console.log("Got payment data: " + JSON.stringify(paymentData));
            localStorage.setItem('htmlContent', this.htmlContent);
            localStorage.setItem('order', JSON.stringify(paymentData.orderId));
            this.router.navigate(["/capitol/payment"], { queryParams: { id: paymentData.orderId, path: '/tribute/posting' } });
        }
        catch (err) {
            console.log(err);
        }
    }
}
