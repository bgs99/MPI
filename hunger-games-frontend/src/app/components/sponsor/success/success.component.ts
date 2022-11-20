import { StepperSelectionEvent } from '@angular/cdk/stepper';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Tribute } from 'src/app/models/tribute';

@Component({
  selector: 'app-success',
  templateUrl: './success.component.html',
  styleUrls: ['./success.component.css']
})
export class SuccessComponent implements OnInit {
  tribute: Tribute | null = null;

  stepperChanged(event: StepperSelectionEvent): void {
    if (event.selectedIndex == 0) {
      this.router.navigateByUrl("/sponsor/tributes");
    }
  }

  constructor(private router: Router) { }

  ngOnInit(): void {
    const tributeItem: string | null = localStorage.getItem("tribute");
    this.tribute = tributeItem === null ? null : JSON.parse(tributeItem);

    localStorage.removeItem('resources');
  }
}
