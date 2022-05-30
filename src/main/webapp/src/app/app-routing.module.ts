import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TributesComponent } from './components/sponsoring/tributes/tributes.component';

const routes: Routes = [
  { path: 'sponsoring/tributes', component: TributesComponent },
  { path: '', redirectTo: '/sponsoring/tributes', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
