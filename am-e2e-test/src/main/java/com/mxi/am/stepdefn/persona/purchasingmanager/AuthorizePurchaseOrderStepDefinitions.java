package com.mxi.am.stepdefn.persona.purchasingmanager;

import javax.inject.Inject;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import com.mxi.am.driver.web.AmWebConditions;
import com.mxi.am.driver.web.AssetManagement;
import com.mxi.am.driver.web.AuthenticationRequiredPageDriver;
import com.mxi.am.driver.web.LoginPageDriver;
import com.mxi.am.driver.web.LogoutPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.po.AuthorizeOrderPageDriver;
import com.mxi.am.driver.web.po.OrderDetailsPageDriver;
import com.mxi.am.driver.web.po.OrderSearchPageDriver;
import com.mxi.am.driver.web.po.RequestAuthorizationPageDriver;
import com.mxi.am.driver.web.po.UnAuthorizeOrderPageDriver;
import com.mxi.driver.standard.Wait;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class AuthorizePurchaseOrderStepDefinitions {

   @Inject
   private LoginPageDriver iLoginDriver;

   @Inject
   private OrderDetailsPageDriver iOrderDetailsPageDriver;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private RequestAuthorizationPageDriver iRequestAuthorizationPageDriver;

   @Inject
   private OrderSearchPageDriver iOrderSearchPageDriver;

   @Inject
   private AuthorizeOrderPageDriver iAuthorizeOrderPageDriver;

   @Inject
   private UnAuthorizeOrderPageDriver iUnauthorizeOrderPageDriver;

   @Inject
   private LogoutPageDriver iLogoutPageDriver;

   @Inject
   private AuthenticationRequiredPageDriver iAuthenticationRequiredPageDriver;

   @Inject
   @AssetManagement
   private WebDriver iWebDriver;

   private final static String PAGE_NAME = "PO Details";
   private final static String PAGE_NAME1 = "Authorize PO";
   private final String PASSWORD = "password";
   private final String ORDER_NUMBER = "PO1_Author";


   @Given( "^A Purchasing Agent unauthorizes and requests authorization for a PO$" )
   public void purchaseAgentSetupDone() throws Throwable {

      // a purchase agent login
      iLoginDriver.setUserName( "purcha1" ).setPassword( PASSWORD ).login();

      // he goes to order search page and look for the order
      iNavigationDriver.navigate( "Purchasing Agent", "Order Search" );
      iOrderSearchPageDriver.clickClearAll();
      iOrderSearchPageDriver.setOrderNumber( ORDER_NUMBER );
      iOrderSearchPageDriver.clickSearch();

      // after clicked the order, he is directed to order details page, then unauthorize it
      iOrderDetailsPageDriver.clickUnauthorizeOrder();
      iUnauthorizeOrderPageDriver.clickOk();
      iAuthenticationRequiredPageDriver.setPasswordAndClickOK( PASSWORD );

      // after unauthorized, he requests authorization
      iOrderDetailsPageDriver.clickRequestAuthorization();
      iRequestAuthorizationPageDriver.clickOK();
      iAuthenticationRequiredPageDriver.setPasswordAndClickOK( PASSWORD );

      // wait until the page is loaded completely
      Wait.until( AmWebConditions.isOnPage( iWebDriver, PAGE_NAME ), 20000 );

      // log out after requests authorization
      iNavigationDriver.clickLogout();
      iLogoutPageDriver.clickOK();

   }


   @And( "^I have a purchase order to be authorized$" )
   public void iHaveAnOrder() throws Throwable {

      iNavigationDriver.navigate( "Purchasing Manager", "Order Search" );
      iOrderSearchPageDriver.clickClearAll();
      iOrderSearchPageDriver.setOrderNumber( ORDER_NUMBER );
      iOrderSearchPageDriver.clickSearch();

   }


   @When( "^I authorize the purchase order$" )
   public void iAuthorizePO() throws Throwable {

      // wait until the page is loaded completely
      Wait.until( AmWebConditions.isOnPage( iWebDriver, PAGE_NAME ), 20000 );

      iOrderDetailsPageDriver.clickAuthorizeOrder();

      // wait until the page is loaded completely
      Wait.until( AmWebConditions.isOnPage( iWebDriver, PAGE_NAME1 ), 20000 );

      iAuthorizeOrderPageDriver.clickOk();
      iAuthenticationRequiredPageDriver.setPasswordAndClickOK( PASSWORD );

   }


   @Then( "^the purchase order is authorized$" )
   public void thePOisAuthorizationRequest() throws Throwable {

      // wait until the page is loaded completely
      Wait.until( AmWebConditions.isOnPage( iWebDriver, PAGE_NAME ), 20000 );

      Assert.assertEquals( "AUTH (Order authorized)", iOrderDetailsPageDriver.getStatus() );
   }

}
