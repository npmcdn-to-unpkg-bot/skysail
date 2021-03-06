/* Avoid: 'error TS2304: Cannot find name <type>' during compilation */
///<reference path="../../typings/index.d.ts"/>

import {AppComponent} from "./app.component";
import {bootstrap} from "@angular/platform-browser-dynamic";
import {provide} from "@angular/core";
import {LocationStrategy, HashLocationStrategy} from "@angular/common";
import {ROUTER_PROVIDERS} from "@angular/router";
import {HTTP_PROVIDERS} from '@angular/http';

//import {WindowService} from './services/window.service'
//import {ConfigService} from './services/config.service' 

//import {AppGlobals} from './services/appglobals.service' 

//import {APP_ROUTER_PROVIDERS} from './app.router'

//import {LocalStorageService, LocalStorageSubscriber} from 'angular2-localstorage/LocalStorageEmitter';
//import { disableDeprecatedForms, provideForms } from '@angular/forms';


bootstrap(AppComponent, [
    ROUTER_PROVIDERS,
    HTTP_PROVIDERS,
    //provide(LocationStrategy, {useClass: HashLocationStrategy})
    provide(Window, {useValue: window})
]);

//bootstrap(AppComponent,[CookieService, AuthService, WindowService, ROUTER_PROVIDERS, HTTP_PROVIDERS, provide(Window, {useValue: window})]);