import { Resource } from "./resource";

export class Order {
    constructor(
        public orderId: string,
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

export class OrderDetail {
    resourceId: string;
    size: number;

    constructor(resource: Resource) {
        this.resourceId = resource.id;
        this.size = resource.amount;
    }
}

export class ResourceOrderRequest {
    orderDetails: OrderDetail[];

    constructor(public tributeId: string, resources: Resource[]) {
        this.tributeId = tributeId;
        this.orderDetails = resources
            .filter((resource: Resource) => resource.amount > 0)
            .map((resource: Resource) => new OrderDetail(resource))
    }
}