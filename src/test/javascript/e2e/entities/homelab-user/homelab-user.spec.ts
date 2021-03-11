import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  HomelabUserComponentsPage,
  /* HomelabUserDeleteDialog, */
  HomelabUserUpdatePage,
} from './homelab-user.page-object';

const expect = chai.expect;

describe('HomelabUser e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let homelabUserComponentsPage: HomelabUserComponentsPage;
  let homelabUserUpdatePage: HomelabUserUpdatePage;
  /* let homelabUserDeleteDialog: HomelabUserDeleteDialog; */

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load HomelabUsers', async () => {
    await navBarPage.goToEntity('homelab-user');
    homelabUserComponentsPage = new HomelabUserComponentsPage();
    await browser.wait(ec.visibilityOf(homelabUserComponentsPage.title), 5000);
    expect(await homelabUserComponentsPage.getTitle()).to.eq('homelabApp.homelabUser.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(homelabUserComponentsPage.entities), ec.visibilityOf(homelabUserComponentsPage.noResult)),
      1000
    );
  });

  it('should load create HomelabUser page', async () => {
    await homelabUserComponentsPage.clickOnCreateButton();
    homelabUserUpdatePage = new HomelabUserUpdatePage();
    expect(await homelabUserUpdatePage.getPageTitle()).to.eq('homelabApp.homelabUser.home.createOrEditLabel');
    await homelabUserUpdatePage.cancel();
  });

  /* it('should create and save HomelabUsers', async () => {
        const nbButtonsBeforeCreate = await homelabUserComponentsPage.countDeleteButtons();

        await homelabUserComponentsPage.clickOnCreateButton();

        await promise.all([
            homelabUserUpdatePage.setNumCNIInput('0'),
            homelabUserUpdatePage.setDateDeNaissanceInput('2000-12-31'),
            homelabUserUpdatePage.setAddressLine1Input('addressLine1'),
            homelabUserUpdatePage.setAddressLine2Input('addressLine2'),
            homelabUserUpdatePage.setCityInput('city'),
            homelabUserUpdatePage.setPaysInput('pays'),
            homelabUserUpdatePage.setPhoneInput('phone'),
            homelabUserUpdatePage.userSelectLastOption(),
        ]);

        expect(await homelabUserUpdatePage.getNumCNIInput()).to.eq('0', 'Expected NumCNI value to be equals to 0');
        expect(await homelabUserUpdatePage.getDateDeNaissanceInput()).to.eq('2000-12-31', 'Expected dateDeNaissance value to be equals to 2000-12-31');
        expect(await homelabUserUpdatePage.getAddressLine1Input()).to.eq('addressLine1', 'Expected AddressLine1 value to be equals to addressLine1');
        expect(await homelabUserUpdatePage.getAddressLine2Input()).to.eq('addressLine2', 'Expected AddressLine2 value to be equals to addressLine2');
        expect(await homelabUserUpdatePage.getCityInput()).to.eq('city', 'Expected City value to be equals to city');
        expect(await homelabUserUpdatePage.getPaysInput()).to.eq('pays', 'Expected Pays value to be equals to pays');
        expect(await homelabUserUpdatePage.getPhoneInput()).to.eq('phone', 'Expected Phone value to be equals to phone');

        await homelabUserUpdatePage.save();
        expect(await homelabUserUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await homelabUserComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last HomelabUser', async () => {
        const nbButtonsBeforeDelete = await homelabUserComponentsPage.countDeleteButtons();
        await homelabUserComponentsPage.clickOnLastDeleteButton();

        homelabUserDeleteDialog = new HomelabUserDeleteDialog();
        expect(await homelabUserDeleteDialog.getDialogTitle())
            .to.eq('homelabApp.homelabUser.delete.question');
        await homelabUserDeleteDialog.clickOnConfirmButton();

        expect(await homelabUserComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
