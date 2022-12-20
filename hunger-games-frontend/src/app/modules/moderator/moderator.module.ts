import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

import { ModeratorRoutingModule } from './moderator-routing.module';
import { SharedModule } from '../shared/shared.module';
import { LoginComponent } from './components/login/login.component';
import { MenuComponent } from './components/menu/menu.component';
import { PostComponent } from './components/post/post.component';
import { AngularEditorModule } from '@kolkov/angular-editor';


@NgModule({
    declarations: [MenuComponent, LoginComponent, PostComponent],
    imports: [
        CommonModule,
        FormsModule,

        ModeratorRoutingModule,

        SharedModule,

        MatButtonModule,
        MatCardModule,
        MatFormFieldModule,
        MatInputModule,
        AngularEditorModule,
    ]
})
export class ModeratorModule { }
