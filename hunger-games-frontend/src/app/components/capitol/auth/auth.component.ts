import { Component, OnInit } from '@angular/core';
import { Mentor } from 'src/app/models/mentor';
import { Tribute } from 'src/app/models/tribute';
import { MentorsService } from 'src/app/services/mentors.service';
import { TributesService } from 'src/app/services/tributes.service';

@Component({
    selector: 'app-auth',
    templateUrl: './auth.component.html',
    styleUrls: ['./auth.component.css']
})
export class AuthComponent implements OnInit {
    tributes: Tribute[] = []
    mentors: Mentor[] = []

    constructor(
        private tributesService: TributesService,
        private mentorsService: MentorsService,) { }

    async ngOnInit(): Promise<void> {
        this.tributes = await this.tributesService.getTributes();
        this.mentors = await this.mentorsService.getMentors();
    }

}
