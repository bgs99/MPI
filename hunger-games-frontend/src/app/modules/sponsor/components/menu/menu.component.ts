import { Component } from '@angular/core';
import { MenuItem } from 'src/app/modules/shared/components/menu/menu.component';

@Component({
    templateUrl: './menu.component.html',
})
export class MenuComponent {
    menuItems: MenuItem[] = [
        new MenuItem('chats', 'Чаты', 'message'),
        new MenuItem('tributes', 'Трибуты', 'people'),
        new MenuItem('news', 'Новости', 'email'),
        new MenuItem('settings', 'настройки', 'message'),
    ];
}
