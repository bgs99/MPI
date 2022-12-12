import { Mentor } from "./mentor";
import { Person } from "./person";

export class Tribute extends Person {
    constructor(username: string, name: string, id: string, avatarUri: string | null, public mentor: Mentor) {
        super(username, name, id, avatarUri)
    }
}

export class MentorTribute {
    constructor(name: string, public id: string) { }
}