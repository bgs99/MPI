import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatStepperModule } from '@angular/material/stepper';
import { AngularEditorModule } from '@kolkov/angular-editor';

import { TributeRoutingModule } from './tribute-routing.module';
import { PostingComponent } from './components/posting/posting.component';
import { FailureComponent } from './components/posting/failure/failure.component';
import { SuccessComponent } from './components/posting/success/success.component';
import { SharedModule } from '../shared/shared.module';
import { FormsModule } from '@angular/forms';
import { MenuComponent } from './components/menu/menu.component';
import { ChatComponent } from './components/chat/chat.component';
import { ChatsComponent } from './components/chats/chats.component';

@NgModule({
    declarations: [
        PostingComponent,
        FailureComponent,
        SuccessComponent,
        MenuComponent,
        ChatComponent,
        ChatsComponent,
    ],
    imports: [
        CommonModule,
        FormsModule,

        TributeRoutingModule,

        SharedModule,

        MatStepperModule,
        AngularEditorModule,
    ]
})
export class TributeModule { }
