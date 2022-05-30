import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TributesComponent } from './components/sponsoring/tributes/tributes.component';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatStepperModule } from '@angular/material/stepper';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';

import { AuthService } from './services/auth.service';
import { TributesService } from './services/tributes.service';
import { HttpClientModule } from '@angular/common/http';
import { HeaderComponent } from './components/header/header.component';
import { ResourcesComponent } from './components/sponsoring/resources/resources.component';

@NgModule({
  declarations: [
    AppComponent,
    TributesComponent,
    HeaderComponent,
    ResourcesComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatTableModule,
    MatButtonModule,
    MatStepperModule,
    MatToolbarModule,
    MatIconModule,
  ],
  providers: [AuthService, TributesService],
  bootstrap: [AppComponent]
})
export class AppModule { }
