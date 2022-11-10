import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Mentor } from 'src/app/models/mentor';
import { Person } from 'src/app/models/person';
import { Sponsor } from 'src/app/models/sponsor';
import { Tribute } from 'src/app/models/tribute';
import { MentorsService } from 'src/app/services/mentors.service';
import { SponsorsService } from 'src/app/services/sponsors.service';
import { TributesService } from 'src/app/services/tributes.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  role: string = 'sponsor'

  tributes: Tribute[] = [];
  sponsors: Sponsor[] = [];
  mentors: Mentor[] = [];

  identity: number = 0;

  persons(): Person[] {
    if (this.role === 'tribute') {
      return this.tributes;
    } else if (this.role === 'sponsor') {
      return this.sponsors;
    } else if (this.role === 'mentor') {
      return this.mentors;
    } else {
      return [];
    }
  }

  login(): void {
    localStorage.setItem("identity", this.identity.toString());

    if (this.role === 'tribute') {
      this.router.navigateByUrl('/tribute/posting');
    } else if (this.role === 'sponsor') {
      this.router.navigateByUrl('/sponsoring/tributes');
    } else if (this.role === 'mentor') {
      this.router.navigateByUrl('/mentor/approval');
    }
  }

  constructor(
    private router: Router,
    private tributesService: TributesService,
    private mentorsService: MentorsService,
    private sponsorsService: SponsorsService) { }

  ngOnInit(): void {
    this.tributesService.getTributes()
      .subscribe({
        next: (data: Tribute[]) => {
          console.log("Received data " + JSON.stringify(data));
          this.tributes = data;
        },
        error: (err: any) => {
          console.log(err)
        }
      });
    this.sponsorsService.getSponsors()
      .subscribe({
        next: (data: Sponsor[]) => {
          console.log("Received data " + JSON.stringify(data));
          this.sponsors = data;
        },
        error: (err: any) => {
          console.log(err)
        }
      });
    this.mentorsService.getMentors()
      .subscribe({
        next: (data: Mentor[]) => {
          console.log("Received data " + JSON.stringify(data));
          this.mentors = data;
        },
        error: (err: any) => {
          console.log(err)
        }
      });
  }

}
