import { NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider'
import { MatCardModule } from '@angular/material/card'
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';

import { AuthService } from './services/auth.service';
import { TributesService } from './services/tributes.service';
import { ResourcesService } from './services/resources.service';
import { SponsorsService } from './services/sponsors.service';
import { MentorsService } from './services/mentors.service';

import { AuthInterceptor } from './services/auth-interceptor.service';

import { TributesService as MockTributesService } from './services/mock/tributes.service';
import { MentorsService as MockMentorsService } from './services/mock/mentors.service';
import { PaymentService as MockPaymentService } from './services/mock/payment.service';
import { RxStompServiceFactory } from './services/rxstomp.factory';
import { OrdersService } from './services/orders.service';
import { ModeratorService } from './services/moderator.service';

@NgModule({
    declarations: [
        AppComponent,
        LoginComponent,
        RegisterComponent,
    ],
    providers: [
        AuthService,
        TributesService,
        ResourcesService,
        SponsorsService,
        MentorsService,
        MockPaymentService,
        OrdersService,
        ModeratorService,
        MockTributesService,
        MockMentorsService,
        { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
        RxStompServiceFactory,
    ],
    bootstrap: [AppComponent],
    imports: [
        BrowserModule,
        FormsModule,
        HttpClientModule,
        BrowserAnimationsModule,
        ReactiveFormsModule,

        AppRoutingModule,

        MatButtonModule,
        MatDividerModule,
        MatCardModule,
        MatFormFieldModule,
        MatInputModule,
    ]
})
export class AppModule { }
