import { RxStompConfig } from '@stomp/rx-stomp';
import { ApiService } from '../services/api.service';

export const HgRxStompConfig: RxStompConfig = {
    brokerURL: `ws://${ApiService.host}/ws`,

    heartbeatIncoming: 0,
    heartbeatOutgoing: 20000,

    reconnectDelay: 200,

    debug: (msg: string): void => {
        console.log(new Date(), msg);
    },
};
