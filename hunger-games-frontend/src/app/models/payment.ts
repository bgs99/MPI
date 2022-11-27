export class PaymentResult {
    constructor(
        public orderId: string,
        public success: boolean,
    ) { }
}