import { UUID } from "./uuid";

export type PersonId = UUID<Person>;

export class Person {
    constructor(public username: string, public name: string, public id: PersonId, public avatarUri: string | null) { }
}

export enum UserRole {
    Sponsor,
    Tribute,
    Mentor,
    Moderator,
}