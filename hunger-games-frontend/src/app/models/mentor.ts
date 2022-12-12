import { Person } from "./person";

export class Mentor extends Person {
    constructor(username: string, name: string, id: string, avatarUri: string | null, public district: number) {
        super(username, name, id, avatarUri)
    }
}
