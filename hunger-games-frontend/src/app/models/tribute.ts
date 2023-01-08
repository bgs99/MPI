import { Mentor } from "./mentor";
import { Person, PersonId } from "./person";
import { UUID } from "./uuid";

export type TributeId = UUID<Tribute>;

export class Tribute extends Person {
    constructor(
        username: string,
        name: string,
        public override id: TributeId,
        avatarUri: string | null,
        public district: number,
    ) {
        super(username, name, id, avatarUri)
    }
}

export class MentorTribute {
    constructor(name: string, public id: PersonId) { }
}
