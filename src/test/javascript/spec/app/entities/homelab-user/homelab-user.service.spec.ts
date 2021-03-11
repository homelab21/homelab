import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { HomelabUserService } from 'app/entities/homelab-user/homelab-user.service';
import { IHomelabUser, HomelabUser } from 'app/shared/model/homelab-user.model';

describe('Service Tests', () => {
  describe('HomelabUser Service', () => {
    let injector: TestBed;
    let service: HomelabUserService;
    let httpMock: HttpTestingController;
    let elemDefault: IHomelabUser;
    let expectedResult: IHomelabUser | IHomelabUser[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(HomelabUserService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new HomelabUser(0, 'AAAAAAA', currentDate, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateDeNaissance: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a HomelabUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateDeNaissance: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDeNaissance: currentDate,
          },
          returnedFromService
        );

        service.create(new HomelabUser()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a HomelabUser', () => {
        const returnedFromService = Object.assign(
          {
            numCNI: 'BBBBBB',
            dateDeNaissance: currentDate.format(DATE_FORMAT),
            addressLine1: 'BBBBBB',
            addressLine2: 'BBBBBB',
            city: 'BBBBBB',
            pays: 'BBBBBB',
            phone: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDeNaissance: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of HomelabUser', () => {
        const returnedFromService = Object.assign(
          {
            numCNI: 'BBBBBB',
            dateDeNaissance: currentDate.format(DATE_FORMAT),
            addressLine1: 'BBBBBB',
            addressLine2: 'BBBBBB',
            city: 'BBBBBB',
            pays: 'BBBBBB',
            phone: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDeNaissance: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a HomelabUser', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
