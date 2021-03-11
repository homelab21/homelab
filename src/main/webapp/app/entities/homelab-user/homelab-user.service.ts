import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IHomelabUser } from 'app/shared/model/homelab-user.model';

type EntityResponseType = HttpResponse<IHomelabUser>;
type EntityArrayResponseType = HttpResponse<IHomelabUser[]>;

@Injectable({ providedIn: 'root' })
export class HomelabUserService {
  public resourceUrl = SERVER_API_URL + 'api/homelab-users';

  constructor(protected http: HttpClient) {}

  create(homelabUser: IHomelabUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(homelabUser);
    return this.http
      .post<IHomelabUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(homelabUser: IHomelabUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(homelabUser);
    return this.http
      .put<IHomelabUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IHomelabUser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IHomelabUser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(homelabUser: IHomelabUser): IHomelabUser {
    const copy: IHomelabUser = Object.assign({}, homelabUser, {
      dateDeNaissance:
        homelabUser.dateDeNaissance && homelabUser.dateDeNaissance.isValid() ? homelabUser.dateDeNaissance.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateDeNaissance = res.body.dateDeNaissance ? moment(res.body.dateDeNaissance) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((homelabUser: IHomelabUser) => {
        homelabUser.dateDeNaissance = homelabUser.dateDeNaissance ? moment(homelabUser.dateDeNaissance) : undefined;
      });
    }
    return res;
  }
}
