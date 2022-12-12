import { Person } from "./person";
import { UUID } from "./uuid";

export type MentorId = UUID<Mentor>;

export class Mentor extends Person {
    constructor(
        username: string,
        name: string,
        public override id: MentorId,
        avatarUri: string | null,
        public district: number,
    ) {
        super(username, name, id, avatarUri)
    }
}
