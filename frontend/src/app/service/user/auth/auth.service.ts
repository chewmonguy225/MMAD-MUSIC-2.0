import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private tokenKey = 'token';
  private usernameKey = 'username';

  // -------------------------
  // SESSION
  // -------------------------
  setSession(token: string, username: string): void {
    localStorage.setItem(this.tokenKey, token);
    localStorage.setItem(this.usernameKey, username);
  }

  // -------------------------
  // GETTERS
  // -------------------------
  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  getUsername(): string |null {
    return localStorage.getItem(this.usernameKey);
  }

  // -------------------------
  // AUTH STATE
  // -------------------------
  isLoggedIn(): boolean {

    const token = this.getToken();

    if (!token) {
      return false;
    }

    try {
      const decoded: any = jwtDecode(token);

      // exp is in seconds
      const expired = decoded.exp * 1000 < Date.now();

      if (expired) {
        this.logout();
        return false;
      }

      return true;

    } catch {
      this.logout();
      return false;
    }
  }

  // -------------------------
  // LOGOUT
  // -------------------------
  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.usernameKey);
  }
}