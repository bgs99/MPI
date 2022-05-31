import { StepperSelectionEvent } from '@angular/cdk/stepper';
import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs';
import { Resource } from 'src/app/models/resource';
import { Tribute } from 'src/app/models/tribute';
import { ResourcesService } from 'src/app/services/resources.service';

@Component({
    selector: 'app-resources',
    templateUrl: './resources.component.html',
    styleUrls: ['./resources.component.css']
})
export class ResourcesComponent implements OnInit {
    resourcesColumns: string[] = ['name', 'amount', 'price', 'total'];
    resources = new MatTableDataSource<Resource>([]);

    tribute: Tribute | null = null;

    constructor(private router: Router, private resourcesService: ResourcesService) {
        router.events.pipe(filter(e => e instanceof NavigationEnd)).subscribe(e => {
            const state = router.getCurrentNavigation()?.extras.state;
            if (state !== undefined) {
                this.tribute = state['tribute']
            }
        });
    }

    stepperChanged(event: StepperSelectionEvent): void {
        if (event.selectedIndex == 0) {
            this.router.navigateByUrl("/sponsoring/tributes");
        }
    }

    total(): number {
        return this.resources.data.reduce((sum: number, resource: Resource) => sum + resource.amount * resource.price, 0)
    }

    pay(): void {
        this.resources.data = []
    }

    ngOnInit(): void {
        this.resourcesService.getResources()
            .subscribe({
                next: (data: Resource[]) => {
                    console.log("Received data " + JSON.stringify(data));
                    data.forEach(resource => {
                        resource.amount = 0;
                    });
                    this.resources.data = data;
                },
                error: (err: any) => {
                    console.log(err)
                }
            });
    }
}
