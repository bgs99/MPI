import { Component, Input } from '@angular/core';
import { Event } from 'src/app/models/event';

@Component({
    selector: 'app-event',
    templateUrl: './event.component.html',
})
export class EventComponent {
    @Input() event!: Event;
}
