import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TributesComponent } from './components/sponsoring/tributes/tributes.component';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatStepperModule } from '@angular/material/stepper';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatGridListModule } from '@angular/material/grid-list';

import { AuthService } from './services/auth.service';
import { TributesService } from './services/tributes.service';
import { HttpClientModule } from '@angular/common/http';
import { HeaderComponent } from './components/header/header.component';
import { ResourcesComponent } from './components/sponsoring/resources/resources.component';
import { PaymentComponent } from './components/payment/payment.component';
import { SuccessComponent } from './components/sponsoring/success/success.component';
import { FailureComponent } from './components/sponsoring/failure/failure.component';
import { ResourcesService } from './services/resources.service';
import { PaymentService } from './services/mock/payment.service';

@NgModule({
  declarations: [
    AppComponent,
    TributesComponent,
    HeaderComponent,
    ResourcesComponent,
    PaymentComponent,
    SuccessComponent,
    FailureComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatTableModule,
    MatButtonModule,
    MatStepperModule,
    MatToolbarModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatGridListModule,
  ],
  providers: [AuthService, TributesService, ResourcesService, PaymentService],
  bootstrap: [AppComponent]
})
export class AppModule { }
