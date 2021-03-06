/* Avoid: 'error TS2304: Cannot find name <type>' during compilation */
///<reference path="../../typings/index.d.ts"/>
System.register(["./app.component", "@angular/platform-browser-dynamic", "@angular/core", "@angular/router", '@angular/http'], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var app_component_1, platform_browser_dynamic_1, core_1, router_1, http_1;
    return {
        setters:[
            function (app_component_1_1) {
                app_component_1 = app_component_1_1;
            },
            function (platform_browser_dynamic_1_1) {
                platform_browser_dynamic_1 = platform_browser_dynamic_1_1;
            },
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (router_1_1) {
                router_1 = router_1_1;
            },
            function (http_1_1) {
                http_1 = http_1_1;
            }],
        execute: function() {
            //import {WindowService} from './services/window.service'
            //import {ConfigService} from './services/config.service' 
            //import {AppGlobals} from './services/appglobals.service' 
            //import {APP_ROUTER_PROVIDERS} from './app.router'
            //import {LocalStorageService, LocalStorageSubscriber} from 'angular2-localstorage/LocalStorageEmitter';
            //import { disableDeprecatedForms, provideForms } from '@angular/forms';
            platform_browser_dynamic_1.bootstrap(app_component_1.AppComponent, [
                router_1.ROUTER_PROVIDERS,
                http_1.HTTP_PROVIDERS,
                //provide(LocationStrategy, {useClass: HashLocationStrategy})
                core_1.provide(Window, { useValue: window })
            ]);
        }
    }
});
//bootstrap(AppComponent,[CookieService, AuthService, WindowService, ROUTER_PROVIDERS, HTTP_PROVIDERS, provide(Window, {useValue: window})]); 

//# sourceMappingURL=main.js.map
