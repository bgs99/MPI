import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Tribute } from 'src/app/models/tribute';
import { TributesService } from 'src/app/services/tributes.service';

@Component({
    selector: 'app-tributes',
    templateUrl: './tributes.component.html',
    styleUrls: ['./tributes.component.css']
})
export class TributesComponent implements OnInit {
    @Output() tributeSelected = new EventEmitter<Tribute>();
    tributesColumns: string[] = ['name', 'district', 'select'];
    tributes = new MatTableDataSource<Tribute>([]);

    constructor(private tributesService: TributesService) { }

    select(tribute: Tribute): void {
        this.tributeSelected.emit(tribute);
    }

    async ngOnInit(): Promise<void> {
        try {
            this.tributes.data = await this.tributesService.getTributes();
        }
        catch (err: any) {
            console.error(err)
        }
    }
}
