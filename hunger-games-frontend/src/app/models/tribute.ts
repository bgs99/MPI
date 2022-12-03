import { Mentor } from "./mentor";
import { Person } from "./person";

export class Tribute extends Person {
    constructor(username: string, name: string, id: string, public mentor: Mentor) {
        super(username, name, id)
    }
}

export class MentorTribute {
    constructor(name: string, public id: string) { }
}