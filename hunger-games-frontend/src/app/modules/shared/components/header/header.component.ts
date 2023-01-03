import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
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
}
