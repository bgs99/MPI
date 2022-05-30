import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { NavigationExtras, Router } from '@angular/router';
import { Tribute } from 'src/app/models/tribute';
import { TributesService } from 'src/app/services/tributes.service';

@Component({
  selector: 'app-tributes',
  templateUrl: './tributes.component.html',
  styleUrls: ['./tributes.component.css']
})
export class TributesComponent implements OnInit {
  tributesColumns: string[] = ['name', 'district', 'select'];
  tributes = new MatTableDataSource<Tribute>([]);

  constructor(private router: Router, private tributesService: TributesService) { }

  select(tribute: Tribute): void {
    console.log('Passing tribute in state')
    const navigationExtras: NavigationExtras = { state: { tribute } };
    this.router.navigate(['/sponsoring/resources'], navigationExtras);
  }

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
