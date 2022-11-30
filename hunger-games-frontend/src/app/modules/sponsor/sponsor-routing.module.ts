import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MenuComponent } from './components/menu/menu.component';
import { CreateOrderComponent } from './components/create_order/create-order.component';
import { PayOrderComponent } from './components/pay_order/pay-order.component';
import { TributesComponent } from './components/tributes/tributes.component';
import { TributeComponent } from './components/tribute/tribute.component';
import { ChatsComponent } from './components/chats/chats.component';
import { ChatComponent } from './components/chat/chat.component';

const routes: Routes = [
    { path: '', component: MenuComponent },
    { path: 'tributes', component: TributesComponent },
    { path: 'tribute/:id', component: TributeComponent },
    { path: 'tribute/:tribute/createorder', component: CreateOrderComponent },
    { path: 'tribute/:tribute/payorder', component: PayOrderComponent },
    { path: 'chats', component: ChatsComponent },
    { path: 'chat/:id', component: ChatComponent },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class SponsorRoutingModule { }
