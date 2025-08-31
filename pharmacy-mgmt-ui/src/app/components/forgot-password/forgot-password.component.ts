import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent implements OnInit {

  loading = false;
  done = false;
  form = this.fb.group({email: ['', [Validators.required, Validators.email]]});

  constructor(private fb: FormBuilder, private auth: AuthService) {
  }

  ngOnInit(): void {
  }

  async submit() {
    if (this.form.invalid || this.loading) return;
    this.loading = true;
    try {
      await this.auth.requestPasswordReset(this.form.value.email!).toPromise();
      this.done = true;
    } finally {
      this.loading = false;
    }
  }

}
