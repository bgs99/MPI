class UUIDFlavoring<FlavorT> {
    // tslint:disable-next-line: variable-name
    _type!: FlavorT;
}

export type UUID<FlavorT> = string & UUIDFlavoring<FlavorT>;
