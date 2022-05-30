import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Tribute } from 'src/app/models/tribute';
import { TributesService } from 'src/app/services/tributes.service';

@Component({
  selector: 'app-tributes',
  templateUrl: './tributes.component.html',
  styleUrls: ['./tributes.component.css']
})
export class TributesComponent implements OnInit {
  tributesColumns: string[] = ['name', 'district'];
  tributes = new MatTableDataSource<Tribute>([]);

  constructor(private tributesService: TributesService) { }

  ngOnInit(): void {
    this.tributesService.getTributes(0)
      .subscribe({
        next: (data: Tribute[]) => {
          console.log("Received data " + JSON.stringify(data));
          this.tributes.data = data;
        },
        error: (err: any) => {
          console.log(err)
        }
      });
  }

}
