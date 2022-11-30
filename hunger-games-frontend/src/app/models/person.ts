export class Person {
    constructor(public username: string, public name: string, public id: string) { }
}

export enum UserRole {
    Sponsor,
    Tribute,
    Mentor,
    Moderator,
}