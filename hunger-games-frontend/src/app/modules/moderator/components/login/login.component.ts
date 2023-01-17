import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
    templateUrl: './login.component.html'
})
export class LoginComponent {
    loginForm = new FormGroup({
        username: new FormControl<string>('', [Validators.required]),
        password: new FormControl<string>('', [Validators.required]),
    });

    constructor(
        private authService: AuthService,
        private router: Router,
    ) { }


    loginError: string | null = null

    async login(): Promise<void> {
        const loginFormValue = this.loginForm.value;
        try {
            await this.authService.loginModerator(loginFormValue.username!, loginFormValue.password!);
            this.router.navigateByUrl('/moderator');
        } catch (e) {
            if (e instanceof HttpErrorResponse && e.status === 403) {
                this.loginError = 'Неправильный логин или пароль';
                return;
            }
            console.error(e);
            this.loginError = 'Неожиданная ошибка.\nПопробуйте повторить позже';
        }
    }
}
