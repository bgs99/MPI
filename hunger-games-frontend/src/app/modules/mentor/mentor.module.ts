import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

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


@NgModule({
    declarations: [ApprovalComponent, MenuComponent, ResourcesComponent],
    imports: [
        CommonModule,
        FormsModule,

        MentorRoutingModule,

        SharedModule,

        MatTableModule,
        MatListModule,
        MatStepperModule,
        MatFormFieldModule,
        MatGridListModule,
    ]
})
export class MentorModule { }
