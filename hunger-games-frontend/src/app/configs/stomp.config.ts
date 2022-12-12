import { RxStompConfig } from '@stomp/rx-stomp';
import { ApiService } from '../services/api.service';
import { AuthService } from '../services/auth.service';


export function makeRxStompConfig(authService: AuthService): RxStompConfig {
    return {
        brokerURL: `ws://${ApiService.host}/ws`,

        heartbeatIncoming: 0,
        heartbeatOutgoing: 20000,

        reconnectDelay: 200,

        connectHeaders: {
            passcode: `Bearer ${authService.token}`
        },
    }
}
