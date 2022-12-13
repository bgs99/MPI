import { StepperSelectionEvent } from '@angular/cdk/stepper';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-success',
  templateUrl: './success.component.html',
  styleUrls: ['./success.component.css']
})
export class SuccessComponent {
  stepperChanged(event: StepperSelectionEvent): void {
    if (event.selectedIndex == 0) {
      this.router.navigateByUrl("/tribute/posting");
    }
  }

  constructor(private router: Router) { }
}
