import { AfterViewChecked, Component, ElementRef, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Chat, ChatMessage } from 'src/app/models/chat';
import { AuthService } from 'src/app/services/auth.service';
import { ChatService, ChatServiceInstance, ConnectedChatServiceInstance } from 'src/app/services/chat.service';

@Component({
    selector: 'app-chat',
    templateUrl: './chat.component.html',
    styleUrls: ['./chat.component.css'],
})
export class ChatComponent implements OnInit, OnDestroy, AfterViewChecked {
    @ViewChild("chatContainer") chatContainer!: ElementRef;
    @Input() chat!: Chat

    messages: ChatMessage[] = []
    self: string = ''
    private chatServiceInstance!: ChatServiceInstance
    private connectedChatServiceInstance!: ConnectedChatServiceInstance

    pendingMessage: string = '';

    constructor(private chatService: ChatService, private authService: AuthService) { }

    async ngOnInit(): Promise<void> {
        this.self = this.authService.id;
        this.chatServiceInstance = this.chatService.instance(this.chat);
        this.messages = await this.chatServiceInstance.getMessagesSnapshot();
        this.connectedChatServiceInstance = this.chatServiceInstance.connect();
        this.connectedChatServiceInstance.messages.subscribe({
            next: (message: ChatMessage) => {
                this.messages.push(message);
            },
            error: (err: any) => {
                console.error(err);
            }
        });
    }

    async ngOnDestroy(): Promise<void> {
        await this.connectedChatServiceInstance.disconnect();
    }

    ngAfterViewChecked(): void {
        this.chatContainer.nativeElement.scrollTop = this.chatContainer.nativeElement.scrollHeight;
    }

    send(): void {
        this.connectedChatServiceInstance.send(this.pendingMessage);
        this.pendingMessage = '';
    }
}
