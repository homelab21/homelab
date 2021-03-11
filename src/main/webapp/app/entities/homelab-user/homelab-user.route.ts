import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IHomelabUser, HomelabUser } from 'app/shared/model/homelab-user.model';
import { HomelabUserService } from './homelab-user.service';
import { HomelabUserComponent } from './homelab-user.component';
import { HomelabUserDetailComponent } from './homelab-user-detail.component';
import { HomelabUserUpdateComponent } from './homelab-user-update.component';

@Injectable({ providedIn: 'root' })
export class HomelabUserResolve implements Resolve<IHomelabUser> {
  constructor(private service: HomelabUserService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHomelabUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((homelabUser: HttpResponse<HomelabUser>) => {
          if (homelabUser.body) {
            return of(homelabUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new HomelabUser());
  }
}

export const homelabUserRoute: Routes = [
  {
    path: '',
    component: HomelabUserComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'homelabApp.homelabUser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HomelabUserDetailComponent,
    resolve: {
      homelabUser: HomelabUserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'homelabApp.homelabUser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HomelabUserUpdateComponent,
    resolve: {
      homelabUser: HomelabUserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'homelabApp.homelabUser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HomelabUserUpdateComponent,
    resolve: {
      homelabUser: HomelabUserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'homelabApp.homelabUser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
