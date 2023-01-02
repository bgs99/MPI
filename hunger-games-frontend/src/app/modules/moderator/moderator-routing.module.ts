import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ApproveComponent } from './components/review/review.component';
import { LoginComponent } from './components/login/login.component';
import { MenuComponent } from './components/menu/menu.component';
import { PostComponent } from './components/post/post.component';


const routes: Routes = [
    { path: '', component: MenuComponent },
    { path: 'login', component: LoginComponent },
    { path: 'post', component: PostComponent },
    { path: 'review', component: ApproveComponent },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ModeratorRoutingModule { }
