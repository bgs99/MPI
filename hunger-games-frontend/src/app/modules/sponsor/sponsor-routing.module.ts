import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FailureComponent } from './components/failure/failure.component';
import { ResourcesComponent } from './components/resources/resources.component';
import { SuccessComponent } from './components/success/success.component';
import { TributesComponent } from './components/tributes/tributes.component';

const routes: Routes = [
    { path: 'tributes', component: TributesComponent },
    { path: 'resources', component: ResourcesComponent },
    { path: 'failure', component: FailureComponent },
    { path: 'success', component: SuccessComponent },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class SponsorRoutingModule { }
