import { Component} from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string = '';
  password: string = '';

  async login(): Promise<void> {
    try {
      await this.authService.login(this.username, this.password);
      this.router.navigateByUrl('/sponsor/tributes');
    } catch (e) {
      console.error(e);
    }
  }

  constructor(
    public router: Router,
    private authService: AuthService) { }
}
