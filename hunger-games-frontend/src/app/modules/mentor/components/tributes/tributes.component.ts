import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { MentorTribute } from 'src/app/models/tribute';
import { MentorsService } from 'src/app/services/mentors.service';

@Component({
    templateUrl: './tributes.component.html',
    styleUrls: ['./tributes.component.css']
})
export class TributesComponent implements OnInit {
    tributesColumns: string[] = ['name', 'select'];
    tributes = new MatTableDataSource<MentorTribute>([]);

    constructor(private router: Router, private mentorService: MentorsService) { }

    select(tribute: MentorTribute): void {
        this.router.navigate(['/mentor', 'tribute', tribute.id, 'resources']);
    }

    async ngOnInit(): Promise<void> {
        try {
            this.tributes.data = await this.mentorService.tributes();
        }
        catch (err: any) {
            console.error(err)
        }
    }
}
