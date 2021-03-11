import { element, by, ElementFinder } from 'protractor';

export class HomelabUserComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-homelab-user div table .btn-danger'));
  title = element.all(by.css('jhi-homelab-user div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class HomelabUserUpdatePage {
  pageTitle = element(by.id('jhi-homelab-user-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  numCNIInput = element(by.id('field_numCNI'));
  dateDeNaissanceInput = element(by.id('field_dateDeNaissance'));
  addressLine1Input = element(by.id('field_addressLine1'));
  addressLine2Input = element(by.id('field_addressLine2'));
  cityInput = element(by.id('field_city'));
  paysInput = element(by.id('field_pays'));
  phoneInput = element(by.id('field_phone'));

  userSelect = element(by.id('field_user'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setNumCNIInput(numCNI: string): Promise<void> {
    await this.numCNIInput.sendKeys(numCNI);
  }

  async getNumCNIInput(): Promise<string> {
    return await this.numCNIInput.getAttribute('value');
  }

  async setDateDeNaissanceInput(dateDeNaissance: string): Promise<void> {
    await this.dateDeNaissanceInput.sendKeys(dateDeNaissance);
  }

  async getDateDeNaissanceInput(): Promise<string> {
    return await this.dateDeNaissanceInput.getAttribute('value');
  }

  async setAddressLine1Input(addressLine1: string): Promise<void> {
    await this.addressLine1Input.sendKeys(addressLine1);
  }

  async getAddressLine1Input(): Promise<string> {
    return await this.addressLine1Input.getAttribute('value');
  }

  async setAddressLine2Input(addressLine2: string): Promise<void> {
    await this.addressLine2Input.sendKeys(addressLine2);
  }

  async getAddressLine2Input(): Promise<string> {
    return await this.addressLine2Input.getAttribute('value');
  }

  async setCityInput(city: string): Promise<void> {
    await this.cityInput.sendKeys(city);
  }

  async getCityInput(): Promise<string> {
    return await this.cityInput.getAttribute('value');
  }

  async setPaysInput(pays: string): Promise<void> {
    await this.paysInput.sendKeys(pays);
  }

  async getPaysInput(): Promise<string> {
    return await this.paysInput.getAttribute('value');
  }

  async setPhoneInput(phone: string): Promise<void> {
    await this.phoneInput.sendKeys(phone);
  }

  async getPhoneInput(): Promise<string> {
    return await this.phoneInput.getAttribute('value');
  }

  async userSelectLastOption(): Promise<void> {
    await this.userSelect.all(by.tagName('option')).last().click();
  }

  async userSelectOption(option: string): Promise<void> {
    await this.userSelect.sendKeys(option);
  }

  getUserSelect(): ElementFinder {
    return this.userSelect;
  }

  async getUserSelectedOption(): Promise<string> {
    return await this.userSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class HomelabUserDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-homelabUser-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-homelabUser'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
