import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MenuComponent } from './components/menu/menu.component';
import { CreateOrderComponent } from './components/create_order/create-order.component';
import { PayOrderComponent } from './components/pay_order/pay-order.component';

const routes: Routes = [
    { path: '', component: MenuComponent },
    { path: 'createorder', component: CreateOrderComponent },
    { path: 'payorder', component: PayOrderComponent },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class SponsorRoutingModule { }
