import { Component } from '@angular/core';
import { MenuItem } from 'src/app/modules/shared/components/menu/menu.component';

@Component({
    templateUrl: './menu.component.html'
})
export class MenuComponent {
    menuItems: MenuItem[] = [
        new MenuItem('post', 'Опубликовать новость', 'publish'),
        new MenuItem('review', 'Рассмотреть посты', 'rate_review'),
    ];
}
