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
    if (event.selectedIndex === 0) {
      this.router.navigateByUrl("/sponsoring/tributes");
    } else if (event.selectedIndex === 1) {
      this.router.navigateByUrl("/sponsoring/resources");
    }
  }

  retry(): void {
    const orderItem: string | null = localStorage.getItem('order');
    if (orderItem === null) {
      return;
    }
    this.router.navigateByUrl("/mock/payment/" + orderItem);
  }

  rechoose(): void {
    this.router.navigateByUrl("/sponsoring/resources");
  }

  cancel(): void {
    localStorage.removeItem('tribute');
    localStorage.removeItem('resources');
    this.router.navigateByUrl("/sponsoring/tributes");
  }


  constructor(private router: Router) { }

  ngOnInit(): void { }
}