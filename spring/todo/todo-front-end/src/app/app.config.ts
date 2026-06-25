import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { loginAuthGuard } from './guards/login-auth-guard';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),

    //here I have included HTTPClient because we are using an interceptor
    //with route guards
    //provideHttpClient(withInterceptors([loginAuthGuard]))
  ]
};
