import { StepperSelectionEvent } from '@angular/cdk/stepper';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
    selector: 'app-failure',
    templateUrl: './failure.component.html',
    styleUrls: ['./failure.component.css']
})
export class FailureComponent implements OnInit {
    stepperChanged(event: StepperSelectionEvent): void {
        if (event.selectedIndex == 0) {
            this.router.navigateByUrl("/tribute/posting");
        }
    }

    retry(): void {
        const orderItem: string | null = localStorage.getItem('order');
        if (orderItem === null) {
            return;
        }
        this.router.navigate(["/capitol/payment"], { queryParams: { id: orderItem, path: '/tribute/posting' } });
    }

    cancel(): void {
        localStorage.removeItem('order');
        this.router.navigateByUrl("/tribute/posting");
    }

    constructor(private router: Router) { }

    ngOnInit(): void {
    }

}
