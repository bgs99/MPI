export class Person {
    constructor(public username: string, public name: string, public id: string, public avatarUri: string | null) { }
}

export enum UserRole {
    Sponsor,
    Tribute,
    Mentor,
    Moderator,
}