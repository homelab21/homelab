import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHomelabUser } from 'app/shared/model/homelab-user.model';

@Component({
  selector: 'jhi-homelab-user-detail',
  templateUrl: './homelab-user-detail.component.html',
})
export class HomelabUserDetailComponent implements OnInit {
  homelabUser: IHomelabUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ homelabUser }) => (this.homelabUser = homelabUser));
  }

  previousState(): void {
    window.history.back();
  }
}
