import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SensitiveDataConsentDialogComponent } from './sensitive-data-consent-dialog.component';

describe('SensitiveDataConsentDialogComponent', () => {
  let component: SensitiveDataConsentDialogComponent;
  let fixture: ComponentFixture<SensitiveDataConsentDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SensitiveDataConsentDialogComponent]
    });
    fixture = TestBed.createComponent(SensitiveDataConsentDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
