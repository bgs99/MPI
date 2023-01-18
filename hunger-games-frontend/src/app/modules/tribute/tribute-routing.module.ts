import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ChatComponent } from './components/chat/chat.component';
import { ChatsComponent } from './components/chats/chats.component';
import { EventsComponent } from './components/events/events.component';
import { MenuComponent } from './components/menu/menu.component';
import { PostingComponent } from './components/posting/posting.component';
import { TributeComponent } from './components/tribute/tribute.component';

const routes: Routes = [
    { path: '', component: MenuComponent },
    { path: 'posting', component: PostingComponent },
    { path: 'chats', component: ChatsComponent },
    { path: 'chat/:id', component: ChatComponent },
    { path: 'events', component: EventsComponent },
    { path: 'self', component: TributeComponent },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class TributeRoutingModule { }
