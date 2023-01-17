import { StepperSelectionEvent } from '@angular/cdk/stepper';
import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import { ChatId } from 'src/app/models/chat';
import { Resource } from 'src/app/models/resource';
import { Tribute, TributeId } from 'src/app/models/tribute';
import { ChatService } from 'src/app/services/chat.service';
import { MentorsService } from 'src/app/services/mentors.service';
import { ResourcesService } from 'src/app/services/resources.service';
import { TributesService } from 'src/app/services/tributes.service';

@Component({
    selector: 'app-mentor-resources',
    templateUrl: './resources.component.html',
    styleUrls: ['./resources.component.css']
})
export class ResourcesComponent implements OnInit {
    resourcesColumns: string[] = ['name', 'amount'];
    resources = new MatTableDataSource<Resource>([]);

    tribute: Tribute | null = null;
    chatId: ChatId | null = null;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private mentorService: MentorsService,
        private resourcesService: ResourcesService,
        private chatService: ChatService,
        private tributesService: TributesService,
    ) { }

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
            const order = await this.mentorService.requestResources(this.tribute.id, this.resources.data);
            if (this.chatId === null) {
                await this.router.navigate(['mentor', 'tributes']);
            } else {
                this.chatService.addPendingMessage(this.chatId, `/${order.orderId}`);
                this.router.navigate(['mentor', 'chat', this.chatId]);
            }
        }
        catch (err: any) {
            console.error(err);
        }
    }

    async ngOnInit(): Promise<void> {
        const tributeId = this.route.snapshot.paramMap.get('tributeId')! as TributeId;
        this.chatId = this.route.snapshot.paramMap.get('chatId') as ChatId | null;

        try {
            this.tribute = (await this.tributesService.getTribute(tributeId))!;
            const resources = await this.resourcesService.getResources();

            resources.forEach(resource => {
                resource.amount = 0
            });
            this.resources.data = resources;
        }
        catch (err: any) {
            console.error(err)
        }
    }
}
