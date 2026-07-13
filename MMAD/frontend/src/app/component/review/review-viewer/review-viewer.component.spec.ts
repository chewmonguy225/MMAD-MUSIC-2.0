import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewViewerComponent } from './review-viewer.component';

describe('ReviewViewerComponent', () => {
  let component: ReviewViewerComponent;
  let fixture: ComponentFixture<ReviewViewerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReviewViewerComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReviewViewerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
