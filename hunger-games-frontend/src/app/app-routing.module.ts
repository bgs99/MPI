import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';

const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: '', redirectTo: '/login', pathMatch: 'full' },
    { path: 'capitol', loadChildren: () => import('./modules/capitol/capitol.module').then(m => m.CapitolModule) },
    { path: 'mentor', loadChildren: () => import('./modules/mentor/mentor.module').then(m => m.MentorModule) },
    { path: 'sponsor', loadChildren: () => import('./modules/sponsor/sponsor.module').then(m => m.SponsorModule) },
    { path: 'tribute', loadChildren: () => import('./modules/tribute/tribute.module').then(m => m.TributeModule) },
];

@NgModule({
    imports: [RouterModule.forRoot(routes, { useHash: false })],
    exports: [RouterModule]
})
export class AppRoutingModule { }
