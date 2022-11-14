import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Mentor } from 'src/app/models/mentor';
import { Person } from 'src/app/models/person';
import { Sponsor } from 'src/app/models/sponsor';
import { Tribute } from 'src/app/models/tribute';
import { AuthService } from 'src/app/services/auth.service';
import { MentorsService } from 'src/app/services/mentors.service';
import { SponsorsService } from 'src/app/services/sponsors.service';
import { TributesService } from 'src/app/services/tributes.service';

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
      const result = await this.authService.login(this.username, this.password);
      localStorage.setItem("username", result.username);
      localStorage.setItem("id", result.id.toString());
      localStorage.setItem("token", result.token);
      if (result.roles[0] == 'SPONSOR') {
        this.router.navigateByUrl('/sponsoring/tributes');
      }
    } catch (e) {
      console.error(e);
    }
  }

  constructor(
    private router: Router,
    private authService: AuthService) { }
}
