import { Component, OnInit, EventEmitter, Output, Input, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subject, Subscription } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

import { Item } from '../../core/model/item/item.type';
import { SearchResult } from '../../core/dto/search/search-result.model';

import { SearchService } from '../../service/search/search.service';
import { UserDTO, UserService } from '../../service/user/user.service';
import { ItemService } from '../../service/item/item/item.service';

import { ItemCardComponent } from '../item/item.component';
import { UserCardComponent } from '../user-card/user-card.component';

import { Router } from '@angular/router';

@Component({
  selector: 'app-search-bar',
  standalone: true,
  imports: [
    CommonModule,
    ItemCardComponent,
    UserCardComponent
  ],
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.css']
})
export class SearchBarComponent implements OnInit, OnDestroy {

  @Input() navigateOnItemSelect = true;
  @Input() includeUsers = true;
  @Output() itemSelected = new EventEmitter<Item>();

  private searchSubject = new Subject<string>();
  private searchSub?: Subscription;

  searchQuery = '';
  isSearching = false;

  results: SearchResult[] = [];

  selectedTypes: string[] = [];

  userCache = new Map<string, UserDTO>();

  constructor(
    private searchService: SearchService,
    private userService: UserService,
    private itemService: ItemService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.searchSub = this.searchSubject
      .pipe(
        debounceTime(300),
        distinctUntilChanged()
      )
      .subscribe((query: string) => {
        this.searchQuery = query;
        this.performSearch();
      });
  }
  ngOnDestroy(): void {
    this.searchSub?.unsubscribe();
  }

  onSearchInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.searchSubject.next(input.value);

    if (!input.value.trim()) {
      this.results = [];
    }
  }

  toggleType(type: string): void {
    if (this.selectedTypes.includes(type)) {
      this.selectedTypes = this.selectedTypes.filter(t => t !== type);
    } else {
      this.selectedTypes.push(type);
    }

    this.performSearch();
  }

  isTypeSelected(type: string): boolean {
    return this.selectedTypes.includes(type);
  }

  performSearch(): void {
    const query = this.searchQuery.trim();

    if (!query) {
      this.results = [];
      this.isSearching = false;
      return;
    }

    this.isSearching = true;

    let types: string[] | undefined;

    if (this.selectedTypes.length > 0) {
      types = [...this.selectedTypes];
    }

    if (!this.includeUsers) {
      types = types
        ? types.filter(type => type !== 'user')
        : ['artist', 'album', 'song'];
    }

    this.searchService.search(query, types)
      .subscribe({
        next: (response) => {

          const items = response.items ?? [];

          this.results = items;
          this.isSearching = false;

          items.forEach(item => {

            if (
              item.type === 'user' &&
              !this.userCache.has(item.name)
            ) {

              this.userService
                .getUserByUsername(item.name)
                .subscribe(user => {
                  this.userCache.set(item.name, user);
                });
            }
          });
        },
        error: (err) => {
          console.error(err);
          this.isSearching = false;
        }
      });
  }

  selectItem(item: SearchResult): void {
    if (item.type === 'user') {
      this.goToUserProfile(item.name);
      return;
    }

    const request: Item = {
      id: null,
      sourceId: item.sourceId,
      name: item.name,
      imageURL: item.imageURL,
      type: item.type,
      provider: item.provider
    };

    this.itemService.addItem(request)
      .subscribe({
        next: (saved: Item) => {
          if (this.navigateOnItemSelect) {
            this.router.navigate([
              '/item',
              saved.id
            ]);
          } else {
            this.itemSelected.emit(saved);
          }
        },
        error: (err) => {
          console.error(err);
        }
      });
  }

  goToUserProfile(username: string): void {
    this.router.navigate([
      '/profile',
      username
    ]);
  }

  clearSearch(): void {
    this.searchQuery = '';
    this.results = [];
  }
}