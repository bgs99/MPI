import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { PaymentComponent } from './components/payment/payment.component';
import { FailureComponent } from './components/sponsoring/failure/failure.component';
import { ResourcesComponent } from './components/sponsoring/resources/resources.component';
import { SuccessComponent } from './components/sponsoring/success/success.component';
import { TributesComponent } from './components/sponsoring/tributes/tributes.component';

const routes: Routes = [
  { path: 'mock/payment/:id', component: PaymentComponent },
  { path: 'sponsoring/success', component: SuccessComponent },
  { path: 'sponsoring/failure', component: FailureComponent },
  { path: 'sponsoring/tributes', component: TributesComponent },
  { path: 'sponsoring/resources', component: ResourcesComponent },
  { path: 'login', component: LoginComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
