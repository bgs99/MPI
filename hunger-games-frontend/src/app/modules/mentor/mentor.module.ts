import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { MatInputModule } from '@angular/material/input';
import { MatTableModule } from '@angular/material/table';
import { MatListModule } from '@angular/material/list';
import { MatStepperModule } from '@angular/material/stepper'
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatGridListModule } from '@angular/material/grid-list';

import { MentorRoutingModule } from './mentor-routing.module';
import { SharedModule } from '../shared/shared.module';

import { ApprovalComponent } from './components/approval/approval.component';
import { MenuComponent } from './components/menu/menu.component';
import { ResourcesComponent } from './components/resources/resources.component';
import { TributesComponent } from './components/tributes/tributes.component';
import { ChatComponent } from './components/chat/chat.component';
import { ChatsComponent } from './components/chats/chats.component';
import { MatButtonModule } from '@angular/material/button';


@NgModule({
    declarations: [ApprovalComponent, MenuComponent, ResourcesComponent, TributesComponent, ChatComponent, ChatsComponent],
    imports: [
        CommonModule,
        FormsModule,

        MentorRoutingModule,

        SharedModule,

        MatButtonModule,
        MatInputModule,
        MatTableModule,
        MatListModule,
        MatStepperModule,
        MatFormFieldModule,
        MatGridListModule,
    ]
})
export class MentorModule { }
