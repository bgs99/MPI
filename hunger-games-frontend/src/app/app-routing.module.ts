import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { ApprovalComponent } from './components/mentor/approval/approval.component';
import { PaymentComponent as MockPaymentComponent } from './components/capitol/payment/payment.component';
import { FailureComponent } from './components/sponsor/failure/failure.component';
import { ResourcesComponent } from './components/sponsor/resources/resources.component';
import { SuccessComponent } from './components/sponsor/success/success.component';
import { TributesComponent } from './components/sponsor/tributes/tributes.component';
import { PostingComponent } from './components/tribute/posting/posting.component';
import { SuccessComponent as PostingSuccessComponent } from './components/tribute/posting/success/success.component';
import { FailureComponent as PostingFailureComponent } from './components/tribute/posting/failure/failure.component';
import { RegisterComponent } from './components/register/register.component';
import { AuthComponent as MockAuthComponent } from './components/capitol/auth/auth.component';

const routes: Routes = [
    { path: 'mock/auth', component: MockAuthComponent },
    { path: 'mock/payment', component: MockPaymentComponent },
    { path: 'sponsor/success', component: SuccessComponent },
    { path: 'sponsor/failure', component: FailureComponent },
    { path: 'sponsor/tributes', component: TributesComponent },
    { path: 'sponsor/resources', component: ResourcesComponent },
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
