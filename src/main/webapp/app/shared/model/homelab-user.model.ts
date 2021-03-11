import { Moment } from 'moment';

export interface IHomelabUser {
  id?: number;
  numCNI?: string;
  dateDeNaissance?: Moment;
  addressLine1?: string;
  addressLine2?: string;
  city?: string;
  pays?: string;
  phone?: string;
  userLogin?: string;
  userId?: number;
}

export class HomelabUser implements IHomelabUser {
  constructor(
    public id?: number,
    public numCNI?: string,
    public dateDeNaissance?: Moment,
    public addressLine1?: string,
    public addressLine2?: string,
    public city?: string,
    public pays?: string,
    public phone?: string,
    public userLogin?: string,
    public userId?: number
  ) {}
}
