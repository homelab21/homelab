import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IHomelabUser, HomelabUser } from 'app/shared/model/homelab-user.model';
import { HomelabUserService } from './homelab-user.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-homelab-user-update',
  templateUrl: './homelab-user-update.component.html',
})
export class HomelabUserUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  dateDeNaissanceDp: any;

  editForm = this.fb.group({
    id: [],
    numCNI: [null, [Validators.required, Validators.minLength(13), Validators.maxLength(13), Validators.pattern('[0-9]')]],
    dateDeNaissance: [null, [Validators.required]],
    addressLine1: [null, [Validators.required]],
    addressLine2: [],
    city: [null, [Validators.required]],
    pays: [null, [Validators.required]],
    phone: [null, [Validators.required, Validators.minLength(9), Validators.maxLength(13)]],
    userId: [null, Validators.required],
  });

  constructor(
    protected homelabUserService: HomelabUserService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ homelabUser }) => {
      this.updateForm(homelabUser);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(homelabUser: IHomelabUser): void {
    this.editForm.patchValue({
      id: homelabUser.id,
      numCNI: homelabUser.numCNI,
      dateDeNaissance: homelabUser.dateDeNaissance,
      addressLine1: homelabUser.addressLine1,
      addressLine2: homelabUser.addressLine2,
      city: homelabUser.city,
      pays: homelabUser.pays,
      phone: homelabUser.phone,
      userId: homelabUser.userId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const homelabUser = this.createFromForm();
    if (homelabUser.id !== undefined) {
      this.subscribeToSaveResponse(this.homelabUserService.update(homelabUser));
    } else {
      this.subscribeToSaveResponse(this.homelabUserService.create(homelabUser));
    }
  }

  private createFromForm(): IHomelabUser {
    return {
      ...new HomelabUser(),
      id: this.editForm.get(['id'])!.value,
      numCNI: this.editForm.get(['numCNI'])!.value,
      dateDeNaissance: this.editForm.get(['dateDeNaissance'])!.value,
      addressLine1: this.editForm.get(['addressLine1'])!.value,
      addressLine2: this.editForm.get(['addressLine2'])!.value,
      city: this.editForm.get(['city'])!.value,
      pays: this.editForm.get(['pays'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      userId: this.editForm.get(['userId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHomelabUser>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
