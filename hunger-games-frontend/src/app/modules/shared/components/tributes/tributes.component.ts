import { Component, Input, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Tribute } from 'src/app/models/tribute';
import { TributesService } from 'src/app/services/tributes.service';

@Component({
    selector: 'app-tributes',
    templateUrl: './tributes.component.html',
    styleUrls: ['./tributes.component.css']
})
export class TributesComponent implements OnInit {
    @Input() tributeLink!: string
    tributesColumns: string[] = ['name', 'district', 'select'];
    tributes = new MatTableDataSource<Tribute>([]);

    constructor(private tributesService: TributesService) { }

    async ngOnInit(): Promise<void> {
        try {
            this.tributes.data = await this.tributesService.getTributes();
        }
        catch (err: any) {
            console.error(err)
        }
    }
}
