import { HgRxStompConfig } from '../configs/stomp.config';
import { RxStompService } from './rxstomp.service';

export function rxStompServiceFactory() {
    const rxStomp = new RxStompService();
    rxStomp.configure(HgRxStompConfig);
    rxStomp.activate();
    return rxStomp;
}
