import { Component, OnInit } from '@angular/core';
import { Event } from 'src/app/models/event';
import { TributesService } from 'src/app/services/tributes.service';

@Component({
    templateUrl: './events.component.html',
})
export class EventsComponent implements OnInit {
    events: Event[] = [];

    constructor(private tributesService: TributesService) { }

    async ngOnInit(): Promise<void> {
        await this.loadEvents();
    }

    async addEvent(event: Event): Promise<void> {
        await this.tributesService.addEvent(event);
        await this.loadEvents();
    }

    async loadEvents(): Promise<void> {
        this.events = await this.tributesService.getEvents();
    }
}
