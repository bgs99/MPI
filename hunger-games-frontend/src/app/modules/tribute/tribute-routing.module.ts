import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FailureComponent } from './components/posting/failure/failure.component';
import { PostingComponent } from './components/posting/posting.component';
import { SuccessComponent } from './components/posting/success/success.component';

const routes: Routes = [
    { path: 'posting', component: PostingComponent },
    { path: 'posting/failure', component: FailureComponent },
    { path: 'posting/success', component: SuccessComponent },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class TributeRoutingModule { }
