package com.mxi.am.stepdefn.persona.purchasingagent;

import javax.inject.Inject;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import com.mxi.am.driver.web.AmWebConditions;
import com.mxi.am.driver.web.AssetManagement;
import com.mxi.am.driver.web.AuthenticationRequiredPageDriver;
import com.mxi.am.driver.web.LoginPageDriver;
import com.mxi.am.driver.web.LogoutPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.po.IssueOrderPageDriver;
import com.mxi.am.driver.web.po.OrderDetailsPageDriver;
import com.mxi.am.driver.web.po.OrderSearchPageDriver;
import com.mxi.am.driver.web.po.RequestAuthorizationPageDriver;
import com.mxi.am.driver.web.po.UnAuthorizeOrderPageDriver;
import com.mxi.driver.standard.Wait;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class IssuePurchaseOrderStepDefinitions {

   @Inject
   private LoginPageDriver iLoginDriver;

   @Inject
   private OrderDetailsPageDriver iOrderDetailsPageDriver;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private RequestAuthorizationPageDriver iRequestAuthorizationPageDriver;

   @Inject
   private AuthenticationRequiredPageDriver iAuthenticationRequiredPageDriver;

   @Inject
   private OrderSearchPageDriver iOrderSearchPageDriver;

   @Inject
   private IssueOrderPageDriver iIssueOrderPageDriver;

   @Inject
   private UnAuthorizeOrderPageDriver iUnauthorizeOrderPageDriver;

   @Inject
   private LogoutPageDriver iLogoutPageDriver;

   @Inject
   @AssetManagement
   private WebDriver iWebDriver;

   private final String PAGE_NAME = "PO Details";
   private final String PASSWORD = "password";
   private final String ORDER_NUMBER = "PO2_Issue";


   @Given( "^that I am a purchase agent$" )
   public void iAmPurchaseAgent() throws Throwable {

      prepareDataByPurchaseManager();

      // Login as Purchase Agent
      iLoginDriver.setUserName( "purcha1" ).setPassword( PASSWORD ).login();
   }


   /**
    * We need purchase manager's help to unauthorize purchase order because the data loading can
    * only load issued purchase order.
    *
    */
   private void prepareDataByPurchaseManager() {

      // a purchase agent login
      iLoginDriver.setUserName( "user1" ).setPassword( PASSWORD ).login();

      // he goes to order search page and look for the order
      iNavigationDriver.navigate( "Purchasing Manager", "Order Search" );
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


   @And( "^I have a purchase order to be issued$" )
   public void iHaveAnOrder() throws Throwable {

      iNavigationDriver.navigate( "Purchasing Agent", "Order Search" );
      iOrderSearchPageDriver.clickClearAll();
      iOrderSearchPageDriver.setOrderNumber( ORDER_NUMBER );
      iOrderSearchPageDriver.clickSearch();

   }


   @When( "^I issue the purchase order$" )
   public void iIssuePO() throws Throwable {

      // wait until the page is loaded completely
      Wait.until( AmWebConditions.isOnPage( iWebDriver, PAGE_NAME ), 20000 );

      iOrderDetailsPageDriver.clickIssuePurchaseOrder();
      iIssueOrderPageDriver.clickOk();

   }


   @Then( "^the purchase order is issued$" )
   public void thePOisIssued() throws Throwable {

      // wait until the page is loaded completely
      Wait.until( AmWebConditions.isOnPage( iWebDriver, PAGE_NAME ), 20000 );

      Assert.assertEquals( "ISSUED (The order has been issued.)",
            iOrderDetailsPageDriver.getStatus() );
   }

}
