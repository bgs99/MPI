import { Person } from "./person";

export class Tribute extends Person {
    constructor(username: string, name: string, public district: number, id: string) {
        super(username, name, id)
    }
}

export class MentorTribute {
    constructor(name: string, public id: string) { }
}