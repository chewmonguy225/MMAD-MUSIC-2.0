import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemReviewsCardComponent } from './item-reviews-card.component';

describe('ItemReviewsCardComponent', () => {
  let component: ItemReviewsCardComponent;
  let fixture: ComponentFixture<ItemReviewsCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ItemReviewsCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ItemReviewsCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
