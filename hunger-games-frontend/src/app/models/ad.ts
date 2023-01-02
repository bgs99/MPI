import { OrderId } from "./order";

export class Ad {
    constructor(public orderId: OrderId, public text: string) { }
}