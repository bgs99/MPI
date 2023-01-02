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

export function eventTypeToString(eventType: EventType): string {
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


export class Event {
    constructor(public id: EventId, public dateTime: string, public place: string, private eventType: string) { }

    get eventT(): EventType {
        return eventTypeFromString(this.eventType);
    }

    set eventT(value: EventType) {
        this.eventType = eventTypeToString(value);
    }
}
