import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  ngOnInit(): void {
  }

  loading = false;
  serverError = '';
  form = this.fb.group({
    fullName: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]],
  });

  constructor(private fb: FormBuilder, private auth: AuthService, private router: Router) {
  }

  async submit() {
    if (this.form.invalid || this.loading) return;
    this.loading = true;
    this.serverError = '';
    try {
      await this.auth.register(this.form.value as any).toPromise();
      this.router.navigateByUrl('/auth/login');
    } catch (e: any) {
      this.serverError = e?.error?.message || 'Registration failed.';
    } finally {
      this.loading = false;
    }
  }

}
