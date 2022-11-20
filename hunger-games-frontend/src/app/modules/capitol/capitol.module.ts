import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { MatListModule } from '@angular/material/list'
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

import { CapitolRoutingModule } from './capitol-routing.module';

import { AuthComponent } from './components/auth/auth.component';
import { HeaderComponent } from './components/header/header.component';
import { PaymentComponent } from './components/payment/payment.component';

@NgModule({
    declarations: [
        AuthComponent,
        HeaderComponent,
        PaymentComponent,
    ],
    imports: [
        CommonModule,
        FormsModule,

        CapitolRoutingModule,

        MatListModule,
        MatFormFieldModule,
        MatSelectModule,
        MatInputModule,
        MatButtonModule,
    ]
})
export class CapitolModule { }
