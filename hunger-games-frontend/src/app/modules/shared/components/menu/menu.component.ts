import { Component, Input } from '@angular/core';

export class MenuItem {
    constructor(
        public path: string,
        public label: string,
        public icon: string,
    ) { }
}

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent {
    @Input() menuItems!: MenuItem[]
}
