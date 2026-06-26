import { Routes } from '@angular/router';
import { LoginComponent } from './logic/component/login/login.component';
import { BasePageComponent } from './pages/base-page/base-page.component';
import { authGuard } from './logic/guard/auth.guard'; // Ensure correct path to your guard
import { HomePageComponent } from './pages/home-page/home-page.component';
import { SearchBarComponent } from './logic/component/search-bar/search-bar.component';
import { ExplorePageComponent } from './pages/explore-page/explore-page.component';
import { ProfilePageComponent } from './pages/profile-page/profile-page.component';
export const routes: Routes = [
    {
        // This route will now try to go to the dashboard first.
        // If the user isn't logged in, the authGuard on '/dashboard'
        // will then redirect them to '/login'.
        path: '',
        redirectTo: '/login', // <-- CHANGE THIS TO YOUR PROTECTED HOME ROUTE
        pathMatch: 'full'
    },
    {
        path: 'login',
        component: LoginComponent,
        title: 'Login'
    },
    {
        path: 'home',
        component: HomePageComponent,
        title: 'Home'
    },
    {
        path: 'explore',
        component: ExplorePageComponent,
        title: 'Explore'
    },
    {
        path: 'myProfilePage',
        component: ProfilePageComponent,
        title: 'ProfilePage'
    },
    {
        path: 'profile/:username',
        component: ProfilePageComponent
      }
      

];