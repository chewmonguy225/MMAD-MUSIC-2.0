import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewCardSkeletonComponent } from './review-card-skeleton.component';

describe('ReviewCardSkeletonComponent', () => {
  let component: ReviewCardSkeletonComponent;
  let fixture: ComponentFixture<ReviewCardSkeletonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReviewCardSkeletonComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReviewCardSkeletonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
