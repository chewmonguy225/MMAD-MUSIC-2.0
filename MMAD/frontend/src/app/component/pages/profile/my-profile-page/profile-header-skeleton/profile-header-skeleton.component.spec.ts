import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileHeaderSkeletonComponent } from './profile-header-skeleton.component';

describe('ProfileHeaderSkeletonComponent', () => {
  let component: ProfileHeaderSkeletonComponent;
  let fixture: ComponentFixture<ProfileHeaderSkeletonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfileHeaderSkeletonComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfileHeaderSkeletonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
