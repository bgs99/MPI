import { UUID } from "./uuid";

export type EventId = UUID<Event>;

export enum EventType {
    INTERVIEW,
    MEETING,
}

export function eventTypes(): EventType[] {
    return [EventType.INTERVIEW, EventType.MEETING];
}

function eventTypeFromString(eventType: string): EventType {
    switch (eventType) {
        case 'INTERVIEW':
            return EventType.INTERVIEW;
        case 'MEETING':
            return EventType.MEETING;
    }
    throw 'Unexpected event type';
}

function eventTypeToString(eventType: EventType): string {
    switch (eventType) {
        case EventType.INTERVIEW:
            return 'INTERVIEW';
        case EventType.MEETING:
            return 'MEETING';
    }
}

export function eventTypeName(eventType: EventType): string {
    switch (eventType) {
        case EventType.INTERVIEW:
            return 'Интервью';
        case EventType.MEETING:
            return 'Встреча';
    }
}

export class EventSerDe {
    constructor(public id: EventId, public dateTime: string, public eventPlace: string, public eventType: string) { }
}

export class Event {
    constructor(public id: EventId, public dateTime: Date, public place: string, private type: EventType) { }

    toSerDe(): EventSerDe {
        return new EventSerDe(this.id, this.dateTime.toISOString(), this.place, eventTypeToString(this.type));
    }

    static fromSerDe(event: EventSerDe): Event {
        return new Event(event.id, new Date(event.dateTime), event.eventPlace, eventTypeFromString(event.eventType));
    }

    get typeName(): string {
        return eventTypeName(this.type);
    }
}
