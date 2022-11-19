import { Person } from "./person";

export class Tribute extends Person {
    constructor(username: string, name: string, public district: number, id: number) {
        super(username, name, id)
    }
}
