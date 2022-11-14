import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { ApprovalComponent } from './components/mentor/approval/approval.component';
import { PaymentComponent } from './components/payment/payment.component';
import { FailureComponent } from './components/sponsoring/failure/failure.component';
import { ResourcesComponent } from './components/sponsoring/resources/resources.component';
import { SuccessComponent } from './components/sponsoring/success/success.component';
import { TributesComponent } from './components/sponsoring/tributes/tributes.component';
import { PostingComponent } from './components/tribute/posting/posting.component';
import { SuccessComponent as PostingSuccessComponent } from './components/tribute/posting/success/success.component';
import { FailureComponent as PostingFailureComponent } from './components/tribute/posting/failure/failure.component';
import { RegisterComponent } from './components/register/register.component';

const routes: Routes = [
  { path: 'mock/payment', component: PaymentComponent },
  { path: 'sponsoring/success', component: SuccessComponent },
  { path: 'sponsoring/failure', component: FailureComponent },
  { path: 'sponsoring/tributes', component: TributesComponent },
  { path: 'sponsoring/resources', component: ResourcesComponent },
  { path: 'mentor/approval', component: ApprovalComponent },
  { path: 'tribute/posting', component: PostingComponent },
  { path: 'tribute/posting/success', component: PostingSuccessComponent },
  { path: 'tribute/posting/failure', component: PostingFailureComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: false })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
