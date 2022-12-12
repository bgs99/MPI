import { Component, EventEmitter, OnInit, Output } from '@angular/core';
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

    @Output() totalChanged = new EventEmitter<number>();

    private get totalPrice(): number {
        return this.resources.reduce((sum: number, resource: Resource) => sum + resource.amount * resource.price, 0)
    }

    onAmountChanged() {
        this.totalChanged.emit(this.totalPrice);
    }

    async ngOnInit(): Promise<void> {
        try {
            this.resourcesDataSource.data = await this.resourcesService.getResources();
            this.resourcesDataSource.data.forEach(element => {
                element.amount = 0;
            });
        }
        catch (err: any) {
            console.error(err)
        }
    }
}
