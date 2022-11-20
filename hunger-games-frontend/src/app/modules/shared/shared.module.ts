import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatToolbarModule } from '@angular/material/toolbar'
import { MatIconModule } from '@angular/material/icon';

import { HeaderComponent } from './components/header/header.component';


@NgModule({
    declarations: [
        HeaderComponent
    ],
    imports: [
        CommonModule,

        MatToolbarModule,
        MatIconModule,
    ],
    exports: [
        HeaderComponent
    ]
})
export class SharedModule { }
