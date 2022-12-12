import { Person } from "./person";

export class Sponsor extends Person {
    constructor(username: string, name: string, id: string, avatarUri: string | null) {
        super(username, name, id, avatarUri)
    }
}
