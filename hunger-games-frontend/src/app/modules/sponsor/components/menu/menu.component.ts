import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sponsor-menu',
  templateUrl: './menu.component.html'
})
export class MenuComponent {

  constructor(public router: Router) { }
}
