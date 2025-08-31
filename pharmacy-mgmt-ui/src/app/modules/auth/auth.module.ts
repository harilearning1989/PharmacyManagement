import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AuthRoutingModule } from './auth-routing.module';
import {LoginComponent} from "../../components/login/login.component";
import {ReactiveFormsModule} from "@angular/forms";
import {RegisterComponent} from "../../components/register/register.component";
import {ForgotPasswordComponent} from "../../components/forgot-password/forgot-password.component";
import {SharedModule} from "../shared/shared.module";


@NgModule({
  declarations: [
    LoginComponent,
    RegisterComponent,
    ForgotPasswordComponent
  ],
  imports: [
    CommonModule,
    AuthRoutingModule,
    ReactiveFormsModule,
    SharedModule
  ]
})
export class AuthModule { }
