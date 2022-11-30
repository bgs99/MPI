import { Component, OnInit } from '@angular/core';
import { Chat } from 'src/app/models/chat';
import { UserRole } from 'src/app/models/person';
import { AuthService } from 'src/app/services/auth.service';
import { ChatService } from 'src/app/services/chat.service';

@Component({
    selector: 'app-chats',
    templateUrl: './chats.component.html'
})
export class ChatsComponent implements OnInit {
    chats: Chat[] = []

    constructor(private chatService: ChatService, private authService: AuthService) { }

    async ngOnInit(): Promise<void> {
        this.chats = await this.chatService.getChats();
    }

}
