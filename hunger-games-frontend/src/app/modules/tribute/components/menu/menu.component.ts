import { Component } from '@angular/core';
import { MenuItem } from 'src/app/modules/shared/components/menu/menu.component';

@Component({
    templateUrl: './menu.component.html'
})
export class MenuComponent {
    menuItems: MenuItem[] = [
        new MenuItem('self', 'Личная страница', 'person'),
        new MenuItem('posting', 'Добавить пост', 'font_download'),
        new MenuItem('chats', 'Чаты', 'message'),
        new MenuItem('events', 'События', 'event'),
    ];
}
