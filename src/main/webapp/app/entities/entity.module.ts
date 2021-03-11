import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'homelab-user',
        loadChildren: () => import('./homelab-user/homelab-user.module').then(m => m.HomelabHomelabUserModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class HomelabEntityModule {}
