import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AlbumSongsComponent } from './album-songs.component';

describe('AlbumSongsComponent', () => {
  let component: AlbumSongsComponent;
  let fixture: ComponentFixture<AlbumSongsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AlbumSongsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AlbumSongsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
