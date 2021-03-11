import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, convertToParamMap } from '@angular/router';

import { HomelabTestModule } from '../../../test.module';
import { HomelabUserComponent } from 'app/entities/homelab-user/homelab-user.component';
import { HomelabUserService } from 'app/entities/homelab-user/homelab-user.service';
import { HomelabUser } from 'app/shared/model/homelab-user.model';

describe('Component Tests', () => {
  describe('HomelabUser Management Component', () => {
    let comp: HomelabUserComponent;
    let fixture: ComponentFixture<HomelabUserComponent>;
    let service: HomelabUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HomelabTestModule],
        declarations: [HomelabUserComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: of({
                defaultSort: 'id,asc',
              }),
              queryParamMap: of(
                convertToParamMap({
                  page: '1',
                  size: '1',
                  sort: 'id,desc',
                })
              ),
            },
          },
        ],
      })
        .overrideTemplate(HomelabUserComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HomelabUserComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HomelabUserService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new HomelabUser(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.homelabUsers && comp.homelabUsers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new HomelabUser(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.homelabUsers && comp.homelabUsers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      comp.ngOnInit();
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,desc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // INIT
      comp.ngOnInit();

      // GIVEN
      comp.predicate = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,desc', 'id']);
    });
  });
});
