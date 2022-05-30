import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Resource } from 'src/app/models/resource';

@Component({
    selector: 'app-resources',
    templateUrl: './resources.component.html',
    styleUrls: ['./resources.component.css']
})
export class ResourcesComponent implements OnInit {
    resourcesColumns: string[] = ['name', 'amount', 'price', 'total'];
    tributes = new MatTableDataSource<Resource>([]);

    tribute: number = -1;

    constructor(private route: ActivatedRoute) { }

    ngOnInit(): void {
        this.route.queryParamMap.subscribe({
            next: (params: ParamMap) => {
                const tribute_param: string | null = params.get("tribute");
                this.tribute = tribute_param === null ? -1 : parseInt(tribute_param);
            }
        })
    }
}
