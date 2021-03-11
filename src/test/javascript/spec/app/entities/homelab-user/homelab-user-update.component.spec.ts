import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { HomelabTestModule } from '../../../test.module';
import { HomelabUserUpdateComponent } from 'app/entities/homelab-user/homelab-user-update.component';
import { HomelabUserService } from 'app/entities/homelab-user/homelab-user.service';
import { HomelabUser } from 'app/shared/model/homelab-user.model';

describe('Component Tests', () => {
  describe('HomelabUser Management Update Component', () => {
    let comp: HomelabUserUpdateComponent;
    let fixture: ComponentFixture<HomelabUserUpdateComponent>;
    let service: HomelabUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HomelabTestModule],
        declarations: [HomelabUserUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(HomelabUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HomelabUserUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HomelabUserService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new HomelabUser(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new HomelabUser();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
