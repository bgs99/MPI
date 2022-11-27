import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar'
import { MatIconModule } from '@angular/material/icon';

import { HeaderComponent } from './components/header/header.component';
import { TributesComponent } from './components/tributes/tributes.component';
import { ResourcesComponent } from './components/resources/resources.component';
import { FormsModule } from '@angular/forms';


@NgModule({
    declarations: [
        HeaderComponent,
        TributesComponent,
        ResourcesComponent,
    ],
    imports: [
        CommonModule,
        FormsModule,

        MatFormFieldModule,
        MatInputModule,
        MatTableModule,
        MatToolbarModule,
        MatIconModule,
    ],
    exports: [
        HeaderComponent,
        TributesComponent,
        ResourcesComponent,
    ]
})
export class SharedModule { }
