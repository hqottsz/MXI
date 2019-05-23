package com.mxi.am.stepdefn.persona.finance;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.web.RefreshDriver;
import com.mxi.am.driver.web.fnc.AccountDetailsPageWebDriver;
import com.mxi.am.driver.web.fnc.AccountSearchPageDriver;
import com.mxi.am.driver.web.fnc.CreateEditAccountPageDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;


/**
 * Step Definitions in order to test the GL Reopen Account inbound message.
 *
 */
public class OpenFinanceAccountStepDefinitions {

   @Inject
   public AccountSearchPageDriver iAccountSearchDriver;

   @Inject
   public CreateEditAccountPageDriver iCreateEditAccountDriver;

   @Inject
   public AccountDetailsPageWebDriver iAccountDetailsDriver;

   @Inject
   public RefreshDriver iRefreshDriver;

   private static final String NAME = "name";
   private static final String CODE = "code";
   private static final String ACCOUNT_TYPE_CD = "accountTypeCd";
   private static final String EXTERNAL_ID = "externalId";


   @Given( "^that I have a finance account in the closed state$" )
   public void thatIHaveAFinanceAccountInTheClosedState( List<Map<String, String>> aDataTable )
         throws Throwable {
      Map<String, String> aTableRow = aDataTable.get( 0 );
      // Create a new finance account
      iAccountSearchDriver.createAccount();
      iCreateEditAccountDriver.setAccountCode( aTableRow.get( CODE ) );
      iCreateEditAccountDriver.setAccountName( aTableRow.get( NAME ) );
      iCreateEditAccountDriver.setAccountType( aTableRow.get( ACCOUNT_TYPE_CD ) );
      iCreateEditAccountDriver.setAccountExternalRef( aTableRow.get( EXTERNAL_ID ) );
      iCreateEditAccountDriver.ok();

      // Set the finance account in the closed state
      Assert.assertTrue( iAccountDetailsDriver.isAccountOpen() );
      iAccountDetailsDriver.closeAccount();
      Assert.assertTrue( iAccountDetailsDriver.isAccountClosed() );
   }


   @Then( "^the finance account is opened$" )
   public void theFinanceAccountIsOpened() throws Throwable {
      iRefreshDriver.refreshCurrentPage();
      Assert.assertTrue( "Account is not open", iAccountDetailsDriver.isAccountOpen() );
   }
}
