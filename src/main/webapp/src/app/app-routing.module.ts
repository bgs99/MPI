import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ResourcesComponent } from './components/sponsoring/resources/resources.component';
import { TributesComponent } from './components/sponsoring/tributes/tributes.component';

const routes: Routes = [
  { path: 'sponsoring/tributes', component: TributesComponent },
  { path: 'sponsoring/resources', component: ResourcesComponent },
  { path: '', redirectTo: '/sponsoring/tributes', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
