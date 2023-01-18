import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserRole } from 'src/app/models/person';
import { AuthService } from 'src/app/services/auth.service';

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

    constructor(private router: Router, private authService: AuthService) { }

    async ngOnInit(): Promise<void> {
        if (!this.authService.authenticated) {
            await this.router.navigate(['/login']);
        }
    }

    async goHome(): Promise<void> {
        switch (this.authService.role) {
            case UserRole.Mentor:
                await this.router.navigate(['/mentor']);
                break;
            case UserRole.Tribute:
                await this.router.navigate(['/tribute']);
                break;
            case UserRole.Sponsor:
                await this.router.navigate(['/sponsor']);
                break;
            case UserRole.Moderator:
                await this.router.navigate(['/moderator']);
                break;
        }
    }

    async logout(): Promise<void> {
        switch (this.authService.role) {
            case UserRole.Moderator:
                await this.router.navigate(['/moderator/login']);
                break;
            default:
                await this.router.navigate(['/login'])
        }
    }
}
