export class ChatMessage {
    constructor(
        public id: string,
        public role: string,
        public time: string, // TODO: proper type?
        public message: string
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