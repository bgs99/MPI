import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Mentor } from 'src/app/models/mentor';
import { Person } from 'src/app/models/person';
import { Tribute } from 'src/app/models/tribute';
import { AuthService } from 'src/app/services/auth.service';
import { MentorsService } from 'src/app/services/mock/mentors.service';
import { TributesService } from 'src/app/services/mock/tributes.service';

enum Role {
    Tribute,
    Mentor
}

@Component({
    selector: 'app-auth',
    templateUrl: './auth.component.html',
    styleUrls: ['./auth.component.css']
})
export class AuthComponent implements OnInit {
    public Role = Role;

    tributes: Tribute[] = []
    mentors: Mentor[] = []
    role: Role | null = null;
    identity: Person | null = null;

    peopleWithRole(): Person[] {
        switch (this.role) {
            case null:
                return [];
            case Role.Tribute:
                return this.tributes;
            case Role.Mentor:
                return this.mentors;
        }
    }

    constructor(
        private authService: AuthService,
        private tributesService: TributesService,
        private mentorsService: MentorsService,
        private router: Router) { }

    async ngOnInit(): Promise<void> {
        this.tributes = await this.tributesService.getAll();
        this.mentors = await this.mentorsService.getAll();
    }

    async login(): Promise<void> {
        if (this.identity === null) {
            return;
        }
        await this.authService.capitolAuth(this.identity.username);
        switch (this.role) {
            case Role.Tribute:
                this.router.navigateByUrl('tribute/posting');
                break;
            case Role.Mentor:
                this.router.navigateByUrl('mentor/approval');
                break;
        }
    }

}
