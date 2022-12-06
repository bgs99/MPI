import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Chat } from 'src/app/models/chat';
import { Tribute } from 'src/app/models/tribute';
import { ChatService } from 'src/app/services/chat.service';
import { TributesService } from 'src/app/services/tributes.service';

@Component({
    templateUrl: './tribute.component.html',
})
export class TributeComponent implements OnInit {
    self: Tribute | undefined
    chat: Chat | undefined

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private tributesService: TributesService,
        private chatService: ChatService,) { }

    async createChat(): Promise<void> {
        this.chat = await this.chatService.createChat(this.self!.id);
        await this.router.navigate(['../../chat', this.chat.chatId], {relativeTo: this.route});
    }

    async ngOnInit(): Promise<void> {
        const id = this.route.snapshot.paramMap.get('id')!;
        this.self = await this.tributesService.getTribute(id)!;
        const chats = await this.chatService.getChats();
        this.chat = chats.find(chat => chat.tributeName == this.self?.name);
    }

}