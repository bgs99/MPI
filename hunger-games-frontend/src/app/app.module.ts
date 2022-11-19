import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TributesComponent } from './components/sponsoring/tributes/tributes.component';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
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

import { AuthService } from './services/auth.service';
import { TributesService } from './services/tributes.service';
import { HeaderComponent } from './components/header/header.component';
import { ResourcesComponent } from './components/sponsoring/resources/resources.component';
import { PaymentComponent } from './components/capitol/payment/payment.component';
import { SuccessComponent } from './components/sponsoring/success/success.component';
import { FailureComponent } from './components/sponsoring/failure/failure.component';
import { ResourcesService } from './services/resources.service';
import { PaymentService } from './services/mock/payment.service';
import { LoginComponent } from './components/login/login.component';
import { SponsorsService } from './services/sponsors.service';
import { MentorsService } from './services/mentors.service';
import { ApprovalComponent } from './components/mentor/approval/approval.component';
import { PostingComponent } from './components/tribute/posting/posting.component';
import { SuccessComponent as PostingSuccessComponent } from './components/tribute/posting/success/success.component';
import { FailureComponent as PostingFailureComponent } from './components/tribute/posting/failure/failure.component';
import { RegisterComponent } from './components/register/register.component';
import { AuthComponent } from './components/capitol/auth/auth.component';

@NgModule({
  declarations: [
    AppComponent,
    TributesComponent,
    HeaderComponent,
    ResourcesComponent,
    PaymentComponent,
    SuccessComponent,
    FailureComponent,
    LoginComponent,
    ApprovalComponent,
    PostingComponent,
    PostingSuccessComponent,
    PostingFailureComponent,
    RegisterComponent,
    AuthComponent,
  ],
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
  ],
  providers: [
    AuthService,
    TributesService,
    ResourcesService,
    PaymentService,
    SponsorsService,
    MentorsService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
