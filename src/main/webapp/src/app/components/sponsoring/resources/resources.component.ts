import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs';
import { Resource } from 'src/app/models/resource';
import { Tribute } from 'src/app/models/tribute';

@Component({
    selector: 'app-resources',
    templateUrl: './resources.component.html',
    styleUrls: ['./resources.component.css']
})
export class ResourcesComponent implements OnInit {
    resourcesColumns: string[] = ['name', 'amount', 'price', 'total'];
    resources = new MatTableDataSource<Resource>([]);

    tribute: Tribute | null = null;

    constructor(private router: Router) {
        router.events.pipe(filter(e => e instanceof NavigationEnd)).subscribe(e => {
            const state = router.getCurrentNavigation()?.extras.state;
            if (state !== undefined) {
                this.tribute = state['tribute']
            }
        });
    }

    ngOnInit(): void {

    }
}
