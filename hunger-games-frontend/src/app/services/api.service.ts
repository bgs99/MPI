export class ApiService {
    static get host(): string {
        return 'localhost:42322'
    }
    static get baseURL(): string {
        return `http://${ApiService.host}/api`
    }
}
