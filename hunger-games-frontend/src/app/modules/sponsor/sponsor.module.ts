import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { MatStepperModule } from '@angular/material/stepper';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

import { SponsorRoutingModule } from './sponsor-routing.module';
import { FailureComponent } from './components/failure/failure.component';
import { ResourcesComponent } from './components/resources/resources.component';
import { SuccessComponent } from './components/success/success.component';
import { TributesComponent } from './components/tributes/tributes.component';
import { SharedModule } from '../shared/shared.module';
import { MatGridListModule } from '@angular/material/grid-list';


@NgModule({
    declarations: [
        FailureComponent,
        ResourcesComponent,
        SuccessComponent,
        TributesComponent,
    ],
    imports: [
        CommonModule,
        FormsModule,

        SponsorRoutingModule,

        SharedModule,

        MatStepperModule,
        MatTableModule,
        MatFormFieldModule,
        MatInputModule,
        MatGridListModule,
    ]
})
export class SponsorModule { }
