import { OrderId } from "./order";

export class PaymentResult {
    constructor(
        public orderId: OrderId,
        public success: boolean,
    ) { }
}