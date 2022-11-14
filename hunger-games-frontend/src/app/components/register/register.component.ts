import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  name: string = ''
  username: string = ''
  password: string = ''
  password2: string = ''

  constructor(private router: Router, private authService: AuthService) { }

  async register(): Promise<void> {
    if (this.password != this.password2) {
      return;
    }
    await this.authService.register(this.username, this.password)
    this.router.navigateByUrl('/login')
  }
}
