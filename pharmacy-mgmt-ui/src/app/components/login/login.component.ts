import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  hide = true;
  loading = false;
  serverError = '';

  form = this.fb.group({
    username: ['', [Validators.required,
      Validators.minLength(4),
      Validators.maxLength(20)]],
    password: ['', [
      Validators.required,
      Validators.minLength(5),
      Validators.maxLength(20),
      // at least 1 letter + 1 digit
      //Validators.pattern(/^(?=.*[A-Za-z])(?=.*\d).{8,}$/),
    ]],
    rememberMe: [true],
  });

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
  }

  get f() {
    return this.form.controls;
  }

  async submit() {
    if (this.form.invalid || this.loading) return;
    this.serverError = '';
    this.loading = true;
    try {
      const {username, password, rememberMe} = this.form.getRawValue();
      await this.auth.login(username!, password!, !!rememberMe).toPromise();
      await this.router.navigateByUrl('/dashboard');
    } catch (e: any) {
      this.serverError =
        e?.error?.message || 'Invalid credentials or server unavailable.';
    } finally {
      this.loading = false;
    }
  }

}
