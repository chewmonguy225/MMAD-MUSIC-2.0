import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemPageSkeletonComponent } from './item-page-skeleton.component';

describe('ItemPageSkeletonComponent', () => {
  let component: ItemPageSkeletonComponent;
  let fixture: ComponentFixture<ItemPageSkeletonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ItemPageSkeletonComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ItemPageSkeletonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
