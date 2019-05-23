
package com.mxi.am.guice;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.UnknownHostException;

import javax.inject.Inject;
import javax.inject.Provider;

import org.openqa.selenium.WebDriver;

import com.mxi.am.driver.web.AssetManagement;
import com.mxi.driver.web.selenium.WebDriverFactory;
import com.thoughtworks.selenium.SeleniumException;

import cucumber.runtime.java.guice.ScenarioScoped;


/**
 * The provider for the web driver
 */
@ScenarioScoped
public class AssetManagementWebDriverProvider implements Provider<WebDriver> {

   private final WebDriver iWebDriver;


   /**
    * Creates a new {@linkplain AssetManagementWebDriverProvider} object.
    *
    * @throws MalformedURLException
    * @throws SeleniumException
    * @throws UnknownHostException
    *
    */
   @Inject
   public AssetManagementWebDriverProvider(@AssetManagement URI aAmUri)
         throws SeleniumException, MalformedURLException, UnknownHostException {
      iWebDriver = WebDriverFactory.getWebDriver();
      iWebDriver.get( aAmUri.resolve( "maintenix" ).toString() );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public WebDriver get() {
      return iWebDriver;
   }

}
