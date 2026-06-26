import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TestItemComponent } from './test-item.component';

describe('TestItemComponent (derived from ItemComponent)', () => {
  let component: TestItemComponent;
  let fixture: ComponentFixture<TestItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TestItemComponent] // Use concrete subclass, not ItemComponent
    }).compileComponents();

    fixture = TestBed.createComponent(TestItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should toggle showReviewButton on image click', () => {
    expect(component.showReviewButton).toBeFalse();
    component.onImageClick();
    expect(component.showReviewButton).toBeTrue();
  });
});
