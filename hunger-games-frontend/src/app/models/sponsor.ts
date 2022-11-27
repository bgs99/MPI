import { Person } from "./person";

export class Sponsor extends Person {
    constructor(username: string, name: string, id: string) {
        super(username, name, id)
    }
}
