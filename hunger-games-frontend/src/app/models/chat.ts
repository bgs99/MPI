import { TributeId } from "./tribute";
import { UUID } from "./uuid";

export class ChatMessage {
    constructor(
        public id: string,
        public role: string,
        public dateTime: string, // TODO: proper type?
        public message: string,
        public senderName: string,
        public senderAvatarUri: string | null,
    ) { }
}

export type ChatId = UUID<Chat>;

export class Chat {
    constructor(
        public chatId: ChatId,
        public mentorName: string,
        public tributeName: string,
        public sponsorName: string,
        public tributeId: TributeId,
        public lastMessage: ChatMessage | undefined
    ) { }
}