// src/app/auth/auth.service.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { UserDTO } from '../user.service';

export interface User {
  id: number;
  username: string;
  // add other user fields as needed
}

export interface LoginResponse {
  token: string;
  user: User;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  // Add this BehaviorSubject to hold the current user state
  private currentUserSubject = new BehaviorSubject<UserDTO | null>(this.getCurrentUser());
  public currentUser$: Observable<UserDTO | null> = this.currentUserSubject.asObservable();

  setSession(token: string, user: UserDTO): void {
    localStorage.setItem('token', token);
    localStorage.setItem('user', JSON.stringify(user));
    this.currentUserSubject.next(user);  // Notify subscribers about new user
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.currentUserSubject.next(null);  // Notify subscribers user logged out
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getCurrentUser(): UserDTO | null {
    const userJson = localStorage.getItem('user');
    return userJson ? JSON.parse(userJson) : null;
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  // Method to update the user data (e.g. after follow/unfollow)
  updateUser(user: UserDTO): void {
    localStorage.setItem('user', JSON.stringify(user));
    this.currentUserSubject.next(user);
  }
}