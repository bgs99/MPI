import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ApprovalComponent } from './components/approval/approval.component';
import { ChatComponent } from './components/chat/chat.component';
import { ChatsComponent } from './components/chats/chats.component';
import { MenuComponent } from './components/menu/menu.component';
import { ResourcesComponent } from './components/resources/resources.component';
import { TributesComponent } from './components/tributes/tributes.component';

const routes: Routes = [
    { path: '', component: MenuComponent },
    { path: 'approval', component: ApprovalComponent },
    { path: 'tributes', component: TributesComponent },
    { path: 'resources', component: ResourcesComponent },
    { path: 'chats', component: ChatsComponent },
    { path: 'chat/:id', component: ChatComponent },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class MentorRoutingModule { }
