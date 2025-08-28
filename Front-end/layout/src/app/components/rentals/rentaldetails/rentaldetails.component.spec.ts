import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RentaldetailsComponent } from './rentaldetails.component';

describe('RentaldetailsComponent', () => {
  let component: RentaldetailsComponent;
  let fixture: ComponentFixture<RentaldetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RentaldetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RentaldetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
