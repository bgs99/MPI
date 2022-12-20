import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html'
})
export class LoginComponent {
    username: string = '';
    password: string = '';

    constructor(
        private authService: AuthService,
        private router: Router,
    ) { }

    async login() {
        await this.authService.login(this.username, this.password);
        this.router.navigateByUrl('/moderator');
    }
}
