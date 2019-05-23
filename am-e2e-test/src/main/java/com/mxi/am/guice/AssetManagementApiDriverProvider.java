
package com.mxi.am.guice;

import java.net.URI;

import javax.inject.Inject;
import javax.inject.Provider;

import com.mxi.am.driver.web.AssetManagement;
import com.mxi.driver.api.ApiDriver;

import cucumber.runtime.java.guice.ScenarioScoped;


/**
 * The api driver provider for asset management
 */
@ScenarioScoped
public class AssetManagementApiDriverProvider implements Provider<ApiDriver> {

   private final ApiDriver iApiDriver;


   /**
    * Creates a new {@linkplain AssetManagementApiDriverProvider} object.
    */
   @Inject
   public AssetManagementApiDriverProvider(@AssetManagement URI aAmUri) {
      iApiDriver = new ApiDriver( aAmUri.resolve( "maintenix" ) );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public ApiDriver get() {
      return iApiDriver;
   }

}
