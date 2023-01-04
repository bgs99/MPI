import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Event, EventId, EventType, eventTypes, eventTypeName } from 'src/app/models/event';

@Component({
    selector: 'app-event-editor',
    templateUrl: './event-editor.component.html',
})
export class EventEditorComponent implements OnInit {
    public dateTime: Date | null = null;
    public place: string = '';
    public eventType: EventType | null = null;
    public eventTypes: EventType[] = eventTypes();

    eventTypeName(eventType: EventType): string {
        return eventTypeName(eventType);
    }

    @Input() sourceEvent: Event | undefined;
    @Output() eventChanged = new EventEmitter<Event>();
    @Output() cancelled = new EventEmitter<void>();

    ngOnInit(): void {
        if (this.sourceEvent === undefined) {
            return;
        }
        this.dateTime = this.sourceEvent.dateTime;
        this.place = this.sourceEvent.place;
        this.eventType = this.sourceEvent.type;
    }

    static padDate(number: number): string {
        if (number < 10) {
            return '0' + number.toString()
        }
        return number.toString()
    }

    static dateToString(date: Date): string {
        return `${date.getUTCFullYear()}-${EventEditorComponent.padDate(date.getUTCMonth() + 1)}-${EventEditorComponent.padDate(date.getUTCDate())} ${date.getUTCHours()}:${date.getUTCMinutes()}:${date.getUTCSeconds()}`;
    }

    submitEvent() {
        if (this.dateTime === null || this.place === '' || this.eventType === null) {
            return;
        }
        const event = new Event('' as EventId, this.dateTime, this.place, this.eventType);
        this.eventChanged.emit(event);
    }

    cancel() {
        this.cancelled.emit();
    }
}
