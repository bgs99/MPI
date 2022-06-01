export class Order {
    constructor(
        public orderId: number,
        public tributeName: string,
        public sponsorName: string,
        public resources: OrderResource[]
    ) { }
}

export class OrderResource {
    constructor(
        public name: string,
        public size: number
    ) { }
}