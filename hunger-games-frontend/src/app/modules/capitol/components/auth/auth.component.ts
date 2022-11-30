import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Mentor } from 'src/app/models/mentor';
import { Person, UserRole } from 'src/app/models/person';
import { Tribute } from 'src/app/models/tribute';
import { AuthService } from 'src/app/services/auth.service';
import { MentorsService } from 'src/app/services/mock/mentors.service';
import { TributesService } from 'src/app/services/mock/tributes.service';

@Component({
    selector: 'app-auth',
    templateUrl: './auth.component.html',
})
export class AuthComponent implements OnInit {
    public Role = UserRole;

    tributes: Tribute[] = []
    mentors: Mentor[] = []
    role: UserRole | null = null;
    identity: Person | null = null;

    peopleWithRole(): Person[] {
        switch (this.role) {
            case null:
                return [];
            case UserRole.Tribute:
                return this.tributes;
            case UserRole.Mentor:
                return this.mentors;
            default:
                return []
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
        if (this.identity === null || this.role === null) {
            return;
        }
        await this.authService.capitolAuth(this.identity.username, this.role);
        switch (this.role) {
            case UserRole.Tribute:
                this.router.navigateByUrl('tribute/posting');
                break;
            case UserRole.Mentor:
                this.router.navigateByUrl('mentor');
                break;
        }
    }

}
