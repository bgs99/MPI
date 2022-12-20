import { Component } from '@angular/core';
import { MenuItem } from 'src/app/modules/shared/components/menu/menu.component';

@Component({
    templateUrl: './menu.component.html'
})
export class MenuComponent {
    menuItems: MenuItem[] = [
        new MenuItem('approval', 'Рассмотреть предложения спонсоров', 'redeem'),
        new MenuItem('tributes', 'Запросить ресурсы', 'shopping_cart'),
        new MenuItem('chats', 'Чаты', 'message'),
    ];
}
