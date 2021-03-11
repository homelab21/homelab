import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HomelabSharedModule } from 'app/shared/shared.module';
import { HomelabUserComponent } from './homelab-user.component';
import { HomelabUserDetailComponent } from './homelab-user-detail.component';
import { HomelabUserUpdateComponent } from './homelab-user-update.component';
import { HomelabUserDeleteDialogComponent } from './homelab-user-delete-dialog.component';
import { homelabUserRoute } from './homelab-user.route';

@NgModule({
  imports: [HomelabSharedModule, RouterModule.forChild(homelabUserRoute)],
  declarations: [HomelabUserComponent, HomelabUserDetailComponent, HomelabUserUpdateComponent, HomelabUserDeleteDialogComponent],
  entryComponents: [HomelabUserDeleteDialogComponent],
})
export class HomelabHomelabUserModule {}
