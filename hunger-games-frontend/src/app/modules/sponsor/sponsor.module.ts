import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { MatStepperModule } from '@angular/material/stepper';
import { MatTableModule } from '@angular/material/table';
import { MatListModule } from '@angular/material/list';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

import { SponsorRoutingModule } from './sponsor-routing.module';

import { CreateOrderComponent } from './components/create_order/create-order.component';
import { MenuComponent } from './components/menu/menu.component';

import { SharedModule } from '../shared/shared.module';
import { MatGridListModule } from '@angular/material/grid-list';
import { PayOrderComponent } from './components/pay_order/pay-order.component';
import { CapitolModule } from '../capitol/capitol.module';
import { ChatsComponent } from './components/chats/chats.component';
import { ChatComponent } from './components/chat/chat.component';
import { TributesComponent } from './components/tributes/tributes.component';
import { TributeComponent } from './components/tribute/tribute.component';
import { SettingsComponent } from './components/settings/settings.component';
import { NewsComponent } from './components/news/news.component';


@NgModule({
    declarations: [
        MenuComponent,
        CreateOrderComponent,
        PayOrderComponent,
        ChatsComponent,
        ChatComponent,
        TributesComponent,
        TributeComponent,
        SettingsComponent,
        NewsComponent,
    ],
    imports: [
        CommonModule,
        FormsModule,

        SponsorRoutingModule,

        SharedModule,
        CapitolModule,

        MatListModule,
        MatStepperModule,
        MatTableModule,
        MatFormFieldModule,
        MatInputModule,
        MatGridListModule,
    ]
})
export class SponsorModule { }
