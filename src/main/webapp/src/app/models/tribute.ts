import { Person } from "./person";

export class Tribute extends Person {
    constructor(name: String, public district: number, id: number) {
        super(name, id)
    }
}
