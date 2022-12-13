import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Chat, ChatId } from 'src/app/models/chat';
import { ChatService } from 'src/app/services/chat.service';

@Component({
    templateUrl: './chat.component.html'
})
export class ChatComponent implements OnInit {
    chat: Chat | undefined

    constructor(
        private route: ActivatedRoute,
        private chatService: ChatService,
    ) { }

    async ngOnInit(): Promise<void> {
        const id = this.route.snapshot.paramMap.get('id')! as ChatId;
        this.chat = await this.chatService.getChat(id);
    }
}
