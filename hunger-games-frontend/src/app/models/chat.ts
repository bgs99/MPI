import { TributeId } from "./tribute";
import { UUID } from "./uuid";

export class ChatMessage {
    constructor(
        public id: string,
        public role: string,
        public dateTime: Date,
        public message: string,
        public senderName: string,
        public senderAvatarUri: string | null,
    ) { }

    toSerDe(): ChatMessageSerDe {
        return new ChatMessageSerDe(
            this.id,
            this.role,
            this.dateTime.toISOString(),
            this.message,
            this.senderName,
            this.senderAvatarUri);
    }

    static fromSerDe(serde: ChatMessageSerDe): ChatMessage {
        return new ChatMessage(
            serde.id,
            serde.role,
            new Date(serde.dateTime),
            serde.message,
            serde.senderName,
            serde.senderAvatarUri)
    }
}

export class ChatMessageSerDe {
    constructor(
        public id: string,
        public role: string,
        public dateTime: string,
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
        public lastMessage: ChatMessage | null
    ) { }

    toSerDe(): ChatSerDe {
        return new ChatSerDe(
            this.chatId,
            this.mentorName,
            this.tributeName,
            this.sponsorName,
            this.tributeId,
            this.lastMessage? this.lastMessage.toSerDe() : null);
    }

    static fromSerDe(serde: ChatSerDe): Chat {
        return new Chat(
            serde.chatId,
            serde.mentorName,
            serde.tributeName,
            serde.sponsorName,
            serde.tributeId,
            serde.lastMessage ? ChatMessage.fromSerDe(serde.lastMessage) : null)
    }
}

export class ChatSerDe {
    constructor(
        public chatId: ChatId,
        public mentorName: string,
        public tributeName: string,
        public sponsorName: string,
        public tributeId: TributeId,
        public lastMessage: ChatMessageSerDe | null
    ) { }
}