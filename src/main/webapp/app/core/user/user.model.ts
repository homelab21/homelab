export interface IUser {
  id?: any;
  login?: string;
  firstName?: string;
  lastName?: string;
  email?: string;
  numCNI?: string;
  phone?: string;
  dateDeNaissance?: Date;
  addressLine1?: string;
  addressLine2?: string;
  city?: string;
  pays?: string;
  activated?: boolean;
  langKey?: string;
  authorities?: string[];
  createdBy?: string;
  createdDate?: Date;
  lastModifiedBy?: string;
  lastModifiedDate?: Date;
  password?: string;
}

export class User implements IUser {
  constructor(
    public id?: any,
    public login?: string,
    public firstName?: string,
    public lastName?: string,
    public email?: string,
    public numCNI?: string,
    public phone?: string,
    public dateDeNaissance?: Date,
    public addressLine1?: string,
    public addressLine2?: string,
    public city?: string,
    public pays?: string,
    public activated?: boolean,
    public langKey?: string,
    public authorities?: string[],
    public createdBy?: string,
    public createdDate?: Date,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Date,
    public password?: string
  ) {}
}
