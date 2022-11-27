import { StepperSelectionEvent } from '@angular/cdk/stepper';
import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs';
import { Resource } from 'src/app/models/resource';
import { Tribute } from 'src/app/models/tribute';
import { MentorsService } from 'src/app/services/mentors.service';
import { ResourcesService } from 'src/app/services/resources.service';

@Component({
    selector: 'app-mentor-resources',
    templateUrl: './resources.component.html',
    styleUrls: ['./resources.component.css']
})
export class ResourcesComponent implements OnInit {
    resourcesColumns: string[] = ['name', 'amount'];
    resources = new MatTableDataSource<Resource>([]);

    tribute: Tribute | null = null;

    constructor(private router: Router, private mentorService: MentorsService, private resourcesService: ResourcesService) {
        router.events.pipe(filter(e => e instanceof NavigationEnd)).subscribe(e => {
            const state = router.getCurrentNavigation()?.extras.state;
            if (state !== undefined) {
                this.tribute = state['tribute'] as Tribute | null
            }
        });
    }

    stepperChanged(event: StepperSelectionEvent): void {
        if (event.selectedIndex == 0) {
            this.router.navigateByUrl("/mentor/tributes");
        }
    }

    any(): boolean {
        return this.resources.data.some((resource: Resource) => resource.amount > 0)
    }

    async request(): Promise<void> {
        if (this.tribute === null) {
            return;
        }
        try {
            await this.mentorService.requestResources(this.tribute.id, this.resources.data);
            this.router.navigateByUrl("/mentor/tributes");
        }
        catch (err: any) {
            console.log(err);
        }
    }

    async ngOnInit(): Promise<void> {
        try {
            const resources = await this.resourcesService.getResources();

            resources.forEach(resource => {
                resource.amount = 0
            });
            this.resources.data = resources;
        }
        catch (err: any) {
            console.log(err)
        }
    }
}
