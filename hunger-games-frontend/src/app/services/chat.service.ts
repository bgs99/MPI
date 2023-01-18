import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { lastValueFrom, Observable, map } from 'rxjs';
import { ApiService } from './api.service';
import { Chat, ChatId, ChatMessage, ChatMessageSerDe, ChatSerDe } from '../models/chat';
import { RxStompService } from './rxstomp.service';
import { RxStompServiceFactory } from './rxstomp.factory';

export class ConnectedChatServiceInstance {
    constructor(private chat: Chat, private stompService: RxStompService) { }
    send(message: string): void {
        this.stompService.publish({
            destination: `/app/send/${this.chat.chatId}`,
            body: JSON.stringify({ chatId: this.chat.chatId, message })
        })
    }
    get messages(): Observable<ChatMessage> {
        return this.stompService.watch(`/chat/receive/${this.chat.chatId}`).pipe(
            map((message) => {
                return ChatMessage.fromSerDe(JSON.parse(message.body) as ChatMessageSerDe)
            })
        )
    }
    async disconnect(): Promise<void> {
        await this.stompService.deactivate();
    }
}

export class ChatServiceInstance {
    constructor(private chat: Chat, private http: HttpClient, private stompServiceFactory: RxStompServiceFactory, private pendingMessages: string[]) { }
    connect(): ConnectedChatServiceInstance {
        const connectedInstance = new ConnectedChatServiceInstance(this.chat, this.stompServiceFactory.make());
        this.pendingMessages.forEach(message => connectedInstance.send(message));
        return connectedInstance;
    }
    async getMessagesSnapshot(): Promise<ChatMessage[]> {
        const messages =  await lastValueFrom(this.http.get<ChatMessageSerDe[]>(
            `${ApiService.baseURL}/chat/message/${this.chat.chatId}`
        ));
        return messages.map(ChatMessage.fromSerDe)
    }
}

@Injectable({
    providedIn: 'root'
})
export class ChatService {
    private pendingMessages: Map<ChatId, string[]> = new Map();
    private static BASE_URL: string = `${ApiService.baseURL}/chat`;
    constructor(private http: HttpClient, private stompServiceFactory: RxStompServiceFactory) { }
    async createChat(tributeId: string): Promise<Chat> {
        const chat = await lastValueFrom(this.http.post<ChatSerDe>(
            `${ChatService.BASE_URL}`,
            { tributeId }
        ));
        return Chat.fromSerDe(chat);
    }
    async getChats(): Promise<Chat[]> {
        const chats = await lastValueFrom(this.http.get<ChatSerDe[]>(
            `${ChatService.BASE_URL}`,
        ));
        return chats.map(Chat.fromSerDe)
    }
    async getChat(id: ChatId): Promise<Chat> {
        const chats = await this.getChats();
        return chats.find(chat => chat.chatId === id)!;
    }

    instance(chat: Chat): ChatServiceInstance {
        const pendingMessages = this.pendingMessages.get(chat.chatId) || [];
        this.pendingMessages.delete(chat.chatId);
        return new ChatServiceInstance(chat, this.http, this.stompServiceFactory, pendingMessages);
    }

    addPendingMessage(chatId: ChatId, message: string) {
        if (this.pendingMessages.has(chatId)) {
            this.pendingMessages.get(chatId)!.push(message);
        } else {
            this.pendingMessages.set(chatId, [message]);
        }
    }
}
