import { UUID } from "./uuid";

export type ResourceId = UUID<Resource>

export class Resource {
    constructor(public name: string, public price: number, public id: ResourceId, public amount: number = 0) { }
}
