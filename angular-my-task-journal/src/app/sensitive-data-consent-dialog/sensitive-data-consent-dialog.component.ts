import { Component } from '@angular/core';

@Component({
  selector: 'app-sensitive-data-consent-dialog',
  template: `
    <h2 mat-dialog-title>Sensitive Data Consent</h2>
    <mat-dialog-content>
      <p>Registration requires some sensitive personal data. Do you consent to provide this data?</p>
    </mat-dialog-content>
    <mat-dialog-actions class="justify-content-center">
      <button mat-button [mat-dialog-close]="true">Accept</button>
      <button mat-button [mat-dialog-close]="false">Reject</button>
    </mat-dialog-actions>
  `,
})
export class SensitiveDataConsentDialogComponent {

}
