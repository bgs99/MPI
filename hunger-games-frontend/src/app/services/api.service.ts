export class ApiService {
    static get host(): string {
        return 'http://localhost:42322'
    }
    static get baseURL(): string {
        return `${ApiService.host}/api`
    }
}
