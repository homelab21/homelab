import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IHomelabUser } from 'app/shared/model/homelab-user.model';
import { HomelabUserService } from './homelab-user.service';

@Component({
  templateUrl: './homelab-user-delete-dialog.component.html',
})
export class HomelabUserDeleteDialogComponent {
  homelabUser?: IHomelabUser;

  constructor(
    protected homelabUserService: HomelabUserService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.homelabUserService.delete(id).subscribe(() => {
      this.eventManager.broadcast('homelabUserListModification');
      this.activeModal.close();
    });
  }
}
