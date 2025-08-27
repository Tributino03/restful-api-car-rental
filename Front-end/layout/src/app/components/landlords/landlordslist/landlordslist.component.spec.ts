import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LandlordslistComponent } from './landlordslist.component';

describe('LandlordslistComponent', () => {
  let component: LandlordslistComponent;
  let fixture: ComponentFixture<LandlordslistComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LandlordslistComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LandlordslistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
