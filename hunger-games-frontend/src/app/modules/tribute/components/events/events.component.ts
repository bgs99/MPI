import { Component } from '@angular/core';
import { Event } from 'src/app/models/event';
import { TributesService } from 'src/app/services/tributes.service';

@Component({
    templateUrl: './events.component.html',
})
export class EventsComponent {
    constructor(private tributesService: TributesService) { }

    async addEvent(event: Event): Promise<void> {
        await this.tributesService.addEvent(event);
    }
}
