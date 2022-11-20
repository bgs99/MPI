import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ApprovalComponent } from './components/approval/approval.component';
import { MenuComponent } from './components/menu/menu.component';
import { ResourcesComponent } from './components/resources/resources.component';

const routes: Routes = [
    { path: '', component: MenuComponent },
    { path: 'approval', component: ApprovalComponent },
    { path: 'resources', component: ResourcesComponent },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class MentorRoutingModule { }
