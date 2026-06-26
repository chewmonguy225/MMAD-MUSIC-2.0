import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';

export interface UserRequest {
  username: string;
  password: string;
}

export interface UserDTO {
  username: string;
  followers?: string[]; // just usernames for now
  following?: string[]; // just usernames for now
  playlists?: number[];
}

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = 'http://localhost:8080/user';

  constructor(private http: HttpClient) {}

  getUserByUsername(username: string): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.apiUrl}/username/${username}`);
  }
  

  register(credentials: UserRequest): Observable<UserDTO> {
    return this.http.post<UserDTO>(`${this.apiUrl}/create`, credentials).pipe(
      catchError(this.handleError)
    );
  }

  login(credentials: UserRequest): Observable<UserDTO> {
    return this.http.post<UserDTO>(`${this.apiUrl}/login`, credentials).pipe(
      catchError(this.handleError)
    );
  }

  searchUsers(query: string): Observable<UserDTO[]> {
    const params = new HttpParams().set('query', query);
    return this.http.get<UserDTO[]>(`${this.apiUrl}/search`, { params }).pipe(
      catchError(this.handleError)
    );
  }

  followUser(username: string, followUsername: string): Observable<string> {
    const params = new HttpParams()
      .set('username', username)
      .set('followUsername', followUsername);
  
    return this.http.post(`${this.apiUrl}/follow`, null, {
      params,
      responseType: 'text' as const // 👈 this prevents JSON parsing errors
    }).pipe(
      catchError(this.handleError)
    );
  }

  unfollowUser(username: string, unfollowUsername: string): Observable<string> {
    const params = new HttpParams()
      .set('username', username)
      .set('unfollowUsername', unfollowUsername);
  
    return this.http.post(`${this.apiUrl}/unfollow`, null, {
      params,
      responseType: 'text' as const // to prevent JSON parsing errors
    }).pipe(
      catchError(this.handleError)
    );
  }
  

  getFollowers(userId: number): Observable<UserDTO[]> {
    return this.http.get<UserDTO[]>(`${this.apiUrl}/followers/${userId}`).pipe(
      catchError(this.handleError)
    );
  }

  getFollowing(userId: number): Observable<UserDTO[]> {
    return this.http.get<UserDTO[]>(`${this.apiUrl}/following/${userId}`).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred.';

    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side error
      switch (error.status) {
        case 400:
          errorMessage = 'Bad Request: Please check your input.';
          break;
        case 401:
          errorMessage = 'Unauthorized: Invalid username or password.';
          break;
        case 403:
          errorMessage = 'Forbidden: You don’t have permission to do this.';
          break;
        case 404:
          errorMessage = 'Not Found: The requested resource was not found.';
          break;
        case 500:
          errorMessage = 'Server Error: Please try again later.';
          break;
        default:
          errorMessage = `Error ${error.status}: ${error.message}`;
      }
    }

    console.error(`HTTP Error: ${errorMessage}`);
    return throwError(() => new Error(errorMessage));
  }
}
