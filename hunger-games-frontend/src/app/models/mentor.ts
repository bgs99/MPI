import { Person } from "./person";

export class Mentor extends Person {
    constructor(username: string, name: string, id: string) {
        super(username, name, id)
    }
}
