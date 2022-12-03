import { Injectable } from '@angular/core';
import { makeRxStompConfig } from '../configs/stomp.config';
import { AuthService } from './auth.service';
import { RxStompService } from './rxstomp.service';

@Injectable({
    providedIn: 'root',
})
export class RxStompServiceFactory
{
    constructor(private authService: AuthService) {

    }
    make() : RxStompService {
        const rxStomp = new RxStompService();
        rxStomp.configure(makeRxStompConfig(this.authService));
        rxStomp.activate();
        return rxStomp;
    }
}
