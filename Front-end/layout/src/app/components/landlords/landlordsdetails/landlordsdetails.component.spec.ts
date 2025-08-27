import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LandlordsdetailsComponent } from './landlordsdetails.component';

describe('LandlordsdetailsComponent', () => {
  let component: LandlordsdetailsComponent;
  let fixture: ComponentFixture<LandlordsdetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LandlordsdetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LandlordsdetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
