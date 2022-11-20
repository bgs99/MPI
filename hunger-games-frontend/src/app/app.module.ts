import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatStepperModule } from '@angular/material/stepper';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatSelectModule } from '@angular/material/select';
import { MatListModule } from '@angular/material/list';

import { AngularEditorModule } from '@kolkov/angular-editor';

import { HeaderComponent } from './components/header/header.component';
import { TributesComponent } from './components/sponsor/tributes/tributes.component';
import { ResourcesComponent as MentorResourcesComponent } from './components/mentor/resources/resources.component';
import { ResourcesComponent as SponsorResourcesComponent } from './components/sponsor/resources/resources.component';
import { PaymentComponent } from './components/capitol/payment/payment.component';
import { SuccessComponent } from './components/sponsor/success/success.component';
import { FailureComponent } from './components/sponsor/failure/failure.component';
import { LoginComponent } from './components/login/login.component';
import { ApprovalComponent } from './components/mentor/approval/approval.component';
import { PostingComponent } from './components/tribute/posting/posting.component';
import { SuccessComponent as PostingSuccessComponent } from './components/tribute/posting/success/success.component';
import { FailureComponent as PostingFailureComponent } from './components/tribute/posting/failure/failure.component';
import { RegisterComponent } from './components/register/register.component';

import { AuthService } from './services/auth.service';
import { TributesService } from './services/tributes.service';
import { ResourcesService } from './services/resources.service';
import { SponsorsService } from './services/sponsors.service';
import { MentorsService } from './services/mentors.service';

import { AuthInterceptor } from './services/auth-interceptor.service';

import { AuthComponent as CapitolAuthComponent } from './components/capitol/auth/auth.component';
import { HeaderComponent as CapitolHeaderComponent } from "./components/capitol/header/header.component";

import { TributesService as MockTributesService } from './services/mock/tributes.service';
import { MentorsService as MockMentorsService } from './services/mock/mentors.service';
import { PaymentService as MockPaymentService } from './services/mock/payment.service';

@NgModule({
    declarations: [
        AppComponent,
        TributesComponent,
        HeaderComponent,
        MentorResourcesComponent,
        SponsorResourcesComponent,
        PaymentComponent,
        SuccessComponent,
        FailureComponent,
        LoginComponent,
        ApprovalComponent,
        PostingComponent,
        PostingSuccessComponent,
        PostingFailureComponent,
        RegisterComponent,
        CapitolAuthComponent,
        CapitolHeaderComponent,
    ],
    providers: [
        AuthService,
        TributesService,
        ResourcesService,
        SponsorsService,
        MentorsService,
        MockPaymentService,
        MockTributesService,
        MockMentorsService,
        { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
    ],
    bootstrap: [AppComponent],
    imports: [
        BrowserModule,
        FormsModule,
        HttpClientModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        MatCardModule,
        MatTableModule,
        MatButtonModule,
        MatStepperModule,
        MatToolbarModule,
        MatIconModule,
        MatInputModule,
        MatFormFieldModule,
        MatGridListModule,
        MatSelectModule,
        MatListModule,
        AngularEditorModule,
    ]
})
export class AppModule { }
