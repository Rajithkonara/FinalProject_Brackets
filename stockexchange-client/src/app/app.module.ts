import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {RouterModule,Routes} from '@angular/router';

import {HttpModule} from '@angular/http';

import { AppComponent } from './app.component';
import { TopNavComponent } from './top-nav/top-nav.component';
import { LeftNavComponent } from './left-nav/left-nav.component';
import { MainContentComponent } from './main-content/main-content.component';
import { CentercomponentComponent } from './centercomponent/centercomponent.component';
import { RightcomponentComponent } from './rightcomponent/rightcomponent.component';
import { LogincomponentComponent } from './logincomponent/logincomponent.component';
import { FullpageComponent } from './fullpage/fullpage.component';
import { UserService } from './user.service';
import { AuthguardGuard } from './authguard.guard';


const appRoutes:Routes = [
  {
    path:'',
    component:LogincomponentComponent
  },
  {
    path:'logincomponent',
    component:LogincomponentComponent
  },
  {
    path: 'fullpage',
    // canActivate:[AuthguardGuard],
    component:FullpageComponent

  }
];


@NgModule({
  declarations: [
    AppComponent,
    TopNavComponent,
    LeftNavComponent,
    MainContentComponent,
    CentercomponentComponent,
    RightcomponentComponent,
    LogincomponentComponent,
    FullpageComponent
  ],
  imports: [
    RouterModule.forRoot(appRoutes),
    BrowserModule,
    HttpModule
  ],
  providers: [UserService,AuthguardGuard],
  bootstrap: [AppComponent]
})
export class AppModule { }
