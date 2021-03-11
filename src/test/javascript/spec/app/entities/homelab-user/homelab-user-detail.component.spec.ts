import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HomelabTestModule } from '../../../test.module';
import { HomelabUserDetailComponent } from 'app/entities/homelab-user/homelab-user-detail.component';
import { HomelabUser } from 'app/shared/model/homelab-user.model';

describe('Component Tests', () => {
  describe('HomelabUser Management Detail Component', () => {
    let comp: HomelabUserDetailComponent;
    let fixture: ComponentFixture<HomelabUserDetailComponent>;
    const route = ({ data: of({ homelabUser: new HomelabUser(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HomelabTestModule],
        declarations: [HomelabUserDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(HomelabUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(HomelabUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load homelabUser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.homelabUser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
