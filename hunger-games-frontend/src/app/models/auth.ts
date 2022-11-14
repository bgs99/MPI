export class LoginResult {
    constructor(
        public id: number,
        public token: string,
        public type: string,
        public username: string,
        public roles: string[]) { }
}
