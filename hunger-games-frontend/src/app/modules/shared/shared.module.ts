import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar'
import { MatIconModule } from '@angular/material/icon';

import { HeaderComponent } from './components/header/header.component';
import { TributesComponent } from './components/tributes/tributes.component';
import { ResourcesComponent } from './components/resources/resources.component';
import { ChatComponent } from './components/chat/chat.component';
import { ChatsComponent } from './components/chats/chats.component';


@NgModule({
    declarations: [
        HeaderComponent,
        TributesComponent,
        ResourcesComponent,
        ChatComponent,
        ChatsComponent,
    ],
    imports: [
        CommonModule,
        FormsModule,
        RouterModule,

        MatCardModule,
        MatFormFieldModule,
        MatInputModule,
        MatListModule,
        MatTableModule,
        MatToolbarModule,
        MatIconModule,
    ],
    exports: [
        HeaderComponent,
        TributesComponent,
        ResourcesComponent,
        ChatComponent,
        ChatsComponent,
    ]
})
export class SharedModule { }
