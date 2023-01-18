import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Event } from 'src/app/models/event';
import { Tribute, TributeId } from 'src/app/models/tribute';
import { AuthService } from 'src/app/services/auth.service';
import { TributesService } from 'src/app/services/tributes.service';

@Component({
    templateUrl: './tribute.component.html',
    styleUrls: ['./tribute.component.css'],
})
export class TributeComponent implements OnInit {
    self: Tribute | undefined
    ads: string[] = []
    events: Event[] = []

    constructor(
        private authService: AuthService,
        private tributesService: TributesService,
        private router: Router) { }


    async ngOnInit(): Promise<void> {
        const id = this.authService.id as TributeId;
        this.self = await this.tributesService.getTribute(id)!;
        this.events = await this.tributesService.getEvents(id);
        this.ads = (await this.tributesService.getAds(id)).reverse();
    }

    async addPost(): Promise<void> {
        await this.router.navigate(['/tribute/posting']);
    }
}
