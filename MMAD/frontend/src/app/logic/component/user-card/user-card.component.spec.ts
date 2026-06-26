import { ComponentFixture, TestBed } from '@angular/core/testing';
import { UserCardComponent } from './user-card.component';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { By } from '@angular/platform-browser';

describe('UserCardComponent', () => {
  let component: UserCardComponent;
  let fixture: ComponentFixture<UserCardComponent>;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([]), // mock router
        UserCardComponent
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    fixture = TestBed.createComponent(UserCardComponent);
    component = fixture.componentInstance;

    // Provide sample input data
    component.user = { username: 'testuser' };

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to user profile on username click', () => {
    const navigateSpy = spyOn(router, 'navigate');

    // Simulate click on the element that triggers navigation
    const usernameEl = fixture.debugElement.query(By.css('.username'));
    usernameEl.triggerEventHandler('click', null);

    expect(navigateSpy).toHaveBeenCalledWith(['/profile', 'testuser']);
  });
});
