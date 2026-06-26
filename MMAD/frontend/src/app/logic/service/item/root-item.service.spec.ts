import { TestBed } from '@angular/core/testing';

import { RootItemService } from './root-item.service';

describe('RootItemService', () => {
  let service: RootItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RootItemService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
