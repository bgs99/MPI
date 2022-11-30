import { Component, OnInit, Output } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Resource } from 'src/app/models/resource';
import { ResourcesService } from 'src/app/services/resources.service';

@Component({
    selector: 'app-resources',
    templateUrl: './resources.component.html',
    styleUrls: ['./resources.component.css']
})
export class ResourcesComponent implements OnInit {
    resourcesColumns: string[] = ['name', 'amount', 'price', 'total'];
    resourcesDataSource = new MatTableDataSource<Resource>([]);

    get resources(): Resource[] {
        return this.resourcesDataSource.data;
    }

    constructor(
        private resourcesService: ResourcesService) {
    }

    get total(): number {
        return this.resources.reduce((sum: number, resource: Resource) => sum + resource.amount * resource.price, 0)
    }

    async ngOnInit(): Promise<void> {
        try {
            this.resourcesDataSource.data = await this.resourcesService.getResources();
        }
        catch (err: any) {
            console.error(err)
        }
    }
}
