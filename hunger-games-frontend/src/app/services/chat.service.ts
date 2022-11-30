import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { lastValueFrom, Observable, map } from 'rxjs';
import { ApiService } from './api.service';
import { Chat, ChatMessage } from '../models/chat';
import { RxStompService } from './rxstomp.service';

export class ChatServiceInstance {
    constructor(private chat: Chat, private http: HttpClient, private stompService: RxStompService) { }
    send(message: string): void {
        this.stompService.publish({
            destination: `/app/send/${this.chat.chatId}`,
            body: JSON.stringify({ chatId: this.chat.chatId, message })
        })
    }
    get messages(): Observable<ChatMessage> {
        return this.stompService.watch(`/chat/receive/${this.chat.chatId}`).pipe(
            map((message) => {
                return JSON.parse(message.body) as ChatMessage
            })
        )
    }
    async getMessagesSnapshot(): Promise<ChatMessage[]> {
        return await lastValueFrom(this.http.get<ChatMessage[]>(
            `${ApiService.baseURL}/chat/message/${this.chat.chatId}`
        ));
    }
}

@Injectable({
    providedIn: 'root'
})
export class ChatService {
    private static BASE_URL: string = `${ApiService.baseURL}/chat`;
    constructor(private http: HttpClient, private stompService: RxStompService) { }
    async createChat(tributeId: string): Promise<Chat> {
        return await lastValueFrom(this.http.post<Chat>(
            `${ChatService.BASE_URL}`,
            { tributeId }
        ));
    }
    async getChats(): Promise<Chat[]> {
        return await lastValueFrom(this.http.get<Chat[]>(
            `${ChatService.BASE_URL}`,
        ));
    }
    instance(chat: Chat): ChatServiceInstance {
        return new ChatServiceInstance(chat, this.http, this.stompService);
    }
}
