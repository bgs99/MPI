import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TributesComponent } from './tributes.component';

describe('TributesComponent', () => {
  let component: TributesComponent;
  let fixture: ComponentFixture<TributesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TributesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TributesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
