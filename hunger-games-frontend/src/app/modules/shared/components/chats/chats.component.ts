import { Component, Input, OnInit } from '@angular/core';
import { Chat } from 'src/app/models/chat';
import { AuthService } from 'src/app/services/auth.service';
import { ChatService } from 'src/app/services/chat.service';

@Component({
    selector: 'app-chats',
    templateUrl: './chats.component.html'
})
export class ChatsComponent implements OnInit {
    @Input() chatLink!: string

    chats: Chat[] = []

    constructor(private chatService: ChatService, private authService: AuthService) { }

    chatName(chat: Chat): string {
        let result = '';
        if (chat.tributeName !== this.authService.name) {
            result += chat.tributeName;
        }
        if (chat.mentorName !== this.authService.name) {
            if (result !== '') {
                result += ', ';
            }
            result += chat.mentorName;
        }
        if (chat.sponsorName !== this.authService.name) {
            if (result !== '') {
                result += ', ';
            }
            result += chat.sponsorName;
        }
        return result;
    }

    async ngOnInit(): Promise<void> {
        this.chats = await this.chatService.getChats();
    }

}
