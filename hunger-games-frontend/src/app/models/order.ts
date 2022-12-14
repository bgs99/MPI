import { Resource, ResourceId } from "./resource";
import { TributeId } from "./tribute";
import { UUID } from "./uuid";

export type OrderId = UUID<Order>;

export class Order {
    constructor(
        public orderId: OrderId,
        public tributeName: string,
        public sponsorName: string,
        public orderDetailResponses: OrderResource[],
        public paid: boolean,
        public approved: boolean | null,
        public sum: number,
    ) { }
}

export class OrderResource {
    constructor(
        public name: string,
        public size: number
    ) { }
}

export class OrderDetail {
    resourceId: ResourceId;
    size: number;
    name: string;

    constructor(resource: Resource) {
        this.resourceId = resource.id;
        this.size = resource.amount;
        this.name = resource.name;
    }
}

export class ResourceOrderRequest {
    orderDetails: OrderDetail[];

    constructor(public tributeId: TributeId, resources: Resource[]) {
        this.tributeId = tributeId;
        this.orderDetails = resources
            .filter((resource: Resource) => resource.amount > 0)
            .map((resource: Resource) => new OrderDetail(resource))
    }
}