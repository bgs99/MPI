import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ChatComponent } from './components/chat/chat.component';
import { ChatsComponent } from './components/chats/chats.component';
import { MenuComponent } from './components/menu/menu.component';
import { FailureComponent } from './components/posting/failure/failure.component';
import { PostingComponent } from './components/posting/posting.component';
import { SuccessComponent } from './components/posting/success/success.component';

const routes: Routes = [
    { path: '', component: MenuComponent },
    { path: 'posting', component: PostingComponent },
    { path: 'posting/failure', component: FailureComponent },
    { path: 'posting/success', component: SuccessComponent },
    { path: 'chats', component: ChatsComponent },
    { path: 'chat/:id', component: ChatComponent },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class TributeRoutingModule { }
