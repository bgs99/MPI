import { AfterViewChecked, Component, ElementRef, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { concatMap } from 'rxjs';
import { Chat, ChatMessage } from 'src/app/models/chat';
import { Order, OrderId } from 'src/app/models/order';
import { UserRole } from 'src/app/models/person';
import { AuthService } from 'src/app/services/auth.service';
import { ChatService, ChatServiceInstance, ConnectedChatServiceInstance } from 'src/app/services/chat.service';
import { MentorsService } from 'src/app/services/mentors.service';
import { pay } from 'src/app/services/mock/payment.service';
import { OrdersService } from 'src/app/services/orders.service';
import { TributesService } from 'src/app/services/tributes.service';

class ChatMaybeCommandMessage {
    constructor(public message: ChatMessage, public order?: Order) { }
}

@Component({
    selector: 'app-chat',
    templateUrl: './chat.component.html',
    styleUrls: ['./chat.component.css'],
})
export class ChatComponent implements OnInit, OnDestroy, AfterViewChecked {
    @ViewChild("chatContainer") chatContainer!: ElementRef;
    @Input() chat!: Chat

    messages: ChatMaybeCommandMessage[] = []
    self: string = ''
    role!: UserRole

    UserRole = UserRole;

    private chatServiceInstance!: ChatServiceInstance
    private connectedChatServiceInstance!: ConnectedChatServiceInstance

    pendingMessage: string = '';

    constructor(
        private chatService: ChatService,
        private authService: AuthService,
        private ordersService: OrdersService,
        private router: Router,
        private tributesService: TributesService,
        private mentorsService: MentorsService
    ) { }

    async resolveMessage(message: ChatMessage): Promise<ChatMaybeCommandMessage> {
        if (message.message.startsWith('/')) {
            const orderId = message.message.slice(1) as OrderId;
            const order = await this.ordersService.getOrder(orderId);
            return new ChatMaybeCommandMessage(message, order);
        } else {
            return new ChatMaybeCommandMessage(message);
        }
    }

    async ngOnInit(): Promise<void> {
        this.self = this.authService.id;
        this.role = this.authService.role;
        this.chatServiceInstance = this.chatService.instance(this.chat);

        this.messages = await Promise.all((await this.chatServiceInstance.getMessagesSnapshot()).map(async message => {
            if (message.message.startsWith('/')) {
                return await this.resolveMessage(message);
            } else {
                return new ChatMaybeCommandMessage(message);
            }
        }));

        this.connectedChatServiceInstance = this.chatServiceInstance.connect();
        this.connectedChatServiceInstance.messages.pipe(
            concatMap(message => this.resolveMessage(message))
        ).subscribe({
            next: (message) => {
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
        if (this.pendingMessage.startsWith('/')) {
            return;
        }
        this.connectedChatServiceInstance.send(this.pendingMessage);
        this.pendingMessage = '';
    }

    async requestResources() {
        this.router.navigate(['mentor', 'chat', this.chat.chatId, this.chat.tributeId, 'resources']);
    }

    async sendResources() {
        this.router.navigate(['sponsor', 'chat', this.chat.chatId, this.chat.tributeId, 'createorder']);
    }

    async considerOrder(order: Order, approve: boolean) {
        await this.mentorsService.approve(order.orderId, approve);
        order.approved = approve;
    }

    async payOrder(order: Order) {
        order.paid ||= await pay(order.orderId);
    }
}
