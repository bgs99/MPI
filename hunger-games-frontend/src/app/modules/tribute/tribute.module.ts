import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatListModule } from '@angular/material/list';
import { MatRadioModule } from '@angular/material/radio';
import { MatStepperModule } from '@angular/material/stepper';
import { NgxMatDatetimePickerModule, NgxMatNativeDateModule } from '@angular-material-components/datetime-picker'
import { AngularEditorModule } from '@kolkov/angular-editor';

import { SharedModule } from '../shared/shared.module';

import { TributeRoutingModule } from './tribute-routing.module';
import { PostingComponent } from './components/posting/posting.component';
import { FailureComponent } from './components/posting/failure/failure.component';
import { SuccessComponent } from './components/posting/success/success.component';
import { MenuComponent } from './components/menu/menu.component';
import { ChatComponent } from './components/chat/chat.component';
import { ChatsComponent } from './components/chats/chats.component';
import { EventsComponent } from './components/events/events.component';
import { EventEditorComponent } from './components/event-editor/event-editor.component';
import { TributeComponent } from './components/tribute/tribute.component';

@NgModule({
    declarations: [
        PostingComponent,
        FailureComponent,
        SuccessComponent,
        MenuComponent,
        ChatComponent,
        ChatsComponent,
        EventsComponent,
        EventEditorComponent,
        TributeComponent,
    ],
    imports: [
        CommonModule,
        FormsModule,

        TributeRoutingModule,

        SharedModule,

        MatButtonModule,
        MatCardModule,
        MatDatepickerModule,
        MatInputModule,
        MatFormFieldModule,
        MatListModule,
        MatRadioModule,
        MatStepperModule,
        NgxMatDatetimePickerModule,
        NgxMatNativeDateModule,
        AngularEditorModule,
    ]
})
export class TributeModule { }
