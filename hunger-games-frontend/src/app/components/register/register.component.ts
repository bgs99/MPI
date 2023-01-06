import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

const validatePasswords: ValidatorFn = (registerForm: AbstractControl): ValidationErrors | null => {
    const password = registerForm.get('password');
    const confirmPassword = registerForm.get('password2');

    return password && confirmPassword && password.value !== confirmPassword.value ? { passwordsNotEqual: true } : null;
}

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.css'],
})
export class RegisterComponent {
    registerForm = new FormGroup({
        name: new FormControl<string>('', [Validators.required, Validators.minLength(3), Validators.maxLength(250)]),
        login: new FormControl<string>('', [Validators.required, Validators.minLength(3), Validators.maxLength(250)]),
        password: new FormControl<string>('', [Validators.required, Validators.minLength(3), Validators.maxLength(255)]),
        password2: new FormControl<string>('', [Validators.required]),
    }, { validators: validatePasswords });

    registerError: string | null = null;

    constructor(private router: Router, private authService: AuthService) { }

    async register(): Promise<void> {
        const formValue = this.registerForm.value;
        try {
            await this.authService.register(formValue.login!, formValue.name!, formValue.password!);
            await this.router.navigateByUrl('/login');
        }
        catch (e: any) {
            if (e instanceof HttpErrorResponse && e.status === 409) {
                this.registerError = 'Пользователь с таким логином уже существует';
                return;
            }
            console.error(e);
            this.registerError = 'Неожиданная ошибка.\nПопробуйте повторить позже';
        }
    }
}
