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

export class Chat {
    constructor(
        public chatId: string,
        public mentorName: string,
        public tributeName: string,
        public sponsorName: string,
        public lastMessage: ChatMessage | undefined
    ) { }
}