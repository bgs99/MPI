import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Chat } from 'src/app/models/chat';

@Component({
    templateUrl: './chat.component.html'
})
export class ChatComponent implements OnInit {
    chat: Chat | undefined

    constructor(private route: ActivatedRoute) { }

    async ngOnInit(): Promise<void> {
        const id = this.route.snapshot.paramMap.get('id')!;
        this.chat = new Chat(id, '', '', '', undefined); // TODO: less dirty?
    }
}
