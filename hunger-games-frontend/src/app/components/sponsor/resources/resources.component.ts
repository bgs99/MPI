import { StepperSelectionEvent } from '@angular/cdk/stepper';
import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs';
import { Resource } from 'src/app/models/resource';
import { Tribute } from 'src/app/models/tribute';
import { ResourcesService } from 'src/app/services/resources.service';
import { SponsorsService } from 'src/app/services/sponsors.service';

@Component({
    templateUrl: './resources.component.html',
    styleUrls: ['./resources.component.css']
})
export class ResourcesComponent implements OnInit {
    resourcesColumns: string[] = ['name', 'amount', 'price', 'total'];
    resources = new MatTableDataSource<Resource>([]);
    paymentEnabled: boolean = true;

    tribute: Tribute | null = null;

    constructor(
        private router: Router,
        private sponsorsService: SponsorsService,
        private resourcesService: ResourcesService) {
        router.events.pipe(filter(e => e instanceof NavigationEnd)).subscribe(e => {
            const state = router.getCurrentNavigation()?.extras.state;
            if (state !== undefined) {
                this.tribute = state['tribute']
            }
        });
    }

    stepperChanged(event: StepperSelectionEvent): void {
        if (event.selectedIndex == 0) {
            this.router.navigateByUrl("/sponsor/tributes");
        }
    }

    total(): number {
        return this.resources.data.reduce((sum: number, resource: Resource) => sum + resource.amount * resource.price, 0)
    }

    async pay(): Promise<void> {
        if (this.tribute === null) {
            return;
        }
        this.paymentEnabled = false;
        try {
            const paymentData = await this.sponsorsService.orderResources(this.tribute.id, this.resources.data);
            localStorage.setItem('tribute', JSON.stringify(this.tribute));
            localStorage.setItem('resources', JSON.stringify(this.resources.data));
            localStorage.setItem('order', JSON.stringify(paymentData.orderId));
            this.router.navigate(["/mock/payment"], { queryParams: { id: paymentData.orderId, path: '/sponsor' } });
        }
        catch (err: any) {
            console.log(err);
            this.paymentEnabled = true;
        }
    }

    async ngOnInit(): Promise<void> {
        try {
            const resources = await this.resourcesService.getResources();

            const resourceItem: string | null = localStorage.getItem('resources');

            const savedData: Resource[] = resourceItem === null ? [] : JSON.parse(resourceItem);

            resources.forEach(resource => {
                const savedAmount: number | undefined = savedData.find((res) => res.id === resource.id)?.amount;
                resource.amount = savedAmount === undefined ? 0 : savedAmount;
            });
            this.resources.data = resources;
        }
        catch (err: any) {
            console.log(err)
        }
    }
}
