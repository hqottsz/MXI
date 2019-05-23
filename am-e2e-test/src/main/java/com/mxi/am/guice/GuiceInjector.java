
package com.mxi.am.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;

import cucumber.api.guice.CucumberModules;
import cucumber.runtime.java.guice.InjectorSource;


/**
 * The custom guice injector
 */
public class GuiceInjector implements InjectorSource {

   /**
    * {@inheritDoc}
    */
   @Override
   public Injector getInjector() {
      return Guice.createInjector( Stage.PRODUCTION, CucumberModules.SCENARIO,
            new AssetManagementModule() );
   }

}
