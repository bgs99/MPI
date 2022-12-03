import { Component, Input, OnInit } from '@angular/core';
import { send } from 'process';
import { Chat, ChatMessage } from 'src/app/models/chat';
import { AuthService } from 'src/app/services/auth.service';
import { ChatService, ChatServiceInstance } from 'src/app/services/chat.service';

@Component({
    selector: 'app-chat',
    templateUrl: './chat.component.html',
})
export class ChatComponent implements OnInit {
    @Input() chat!: Chat

    messages: ChatMessage[] = []
    self: string = ''
    private chatServiceInstance!: ChatServiceInstance

    pendingMessage: string = '';

    constructor(private chatService: ChatService, private authService: AuthService) { }

    async ngOnInit(): Promise<void> {
        this.self = this.authService.id;
        this.chatServiceInstance = this.chatService.instance(this.chat);
        this.messages = await this.chatServiceInstance.getMessagesSnapshot();
        this.chatServiceInstance.messages.subscribe({
            next: (message: ChatMessage) => {
                this.messages.push(message);
            },
            error: (err: any) => {
                console.error(err);
            }
        });
    }

    send(): void {
        this.chatServiceInstance.send(this.pendingMessage);
        this.pendingMessage = '';
    }
}
