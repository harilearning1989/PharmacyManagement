import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthLayoutComponent} from "../../components/auth-layout/auth-layout.component";
import {LoginComponent} from "../../components/login/login.component";
import {RegisterComponent} from "../../components/register/register.component";
import {ForgotPasswordComponent} from "../../components/forgot-password/forgot-password.component";

const routes: Routes = [
  {
    path: '',
    component: AuthLayoutComponent,
    children: [
      {path: 'login', component: LoginComponent},
      {path: 'register', component: RegisterComponent},
      {path: 'forgot-password', component: ForgotPasswordComponent},
      {path: '', pathMatch: 'full', redirectTo: 'login'},
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule {
}
