import { Person, PersonId } from "./person";
import { UUID } from "./uuid";

export type SponsorId = UUID<Sponsor>

export class Sponsor extends Person {
    constructor(
        username: string,
        name: string,
        public override id: SponsorId,
        avatarUri: string | null,
    ) {
        super(username, name, id, avatarUri)
    }
}
