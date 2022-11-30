import { Component } from '@angular/core';

enum Mode {
    Existing,
    New,        
}

@Component({
    templateUrl: './chats.component.html',
})
export class ChatsComponent {
    Mode = Mode;

    mode: Mode = Mode.Existing;

    constructor() { }

}
