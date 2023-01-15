import { Component, OnInit } from '@angular/core';
import { Event } from 'src/app/models/event';
import { TributesService } from 'src/app/services/tributes.service';

@Component({
    templateUrl: './events.component.html',
})
export class EventsComponent implements OnInit {
    events: Event[] = [];
    addingEvent: boolean = false;
    editedEvent: number | null = null;

    constructor(private tributesService: TributesService) { }

    async ngOnInit(): Promise<void> {
        await this.loadEvents();
    }

    async addEvent(event: Event): Promise<void> {
        await this.tributesService.addEvent(event);
        this.addingEvent = false;
        await this.loadEvents();
    }

    async editEvent(index: number, event: Event): Promise<void> {
        event.id = this.events[index].id;
        await this.tributesService.editEvent(event);
        this.editedEvent = null;
        await this.loadEvents();
    }

    async loadEvents(): Promise<void> {
        this.events = await this.tributesService.getOwnEvents();
    }

    async deleteEvent(event: Event): Promise<void> {
        await this.tributesService.deleteEvent(event);
        await this.loadEvents();
    }
}
