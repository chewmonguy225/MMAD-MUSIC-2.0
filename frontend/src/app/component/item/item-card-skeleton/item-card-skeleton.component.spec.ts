import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemCardSkeletonComponent } from './item-card-skeleton.component';

describe('ItemCardSkeletonComponent', () => {
  let component: ItemCardSkeletonComponent;
  let fixture: ComponentFixture<ItemCardSkeletonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ItemCardSkeletonComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ItemCardSkeletonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
