import { OrderId } from "./order";

export class PaymentData {
    constructor(public orderId: OrderId, public sum: number) {}
}
