package com.mxi.am;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.CucumberScenario;
import cucumber.runtime.model.CucumberTagStatement;
import gherkin.formatter.model.Tag;


public class E2EAnnotationsTest {

   private static final Logger sLOGGER = Logger.getLogger( E2EAnnotationsTest.class );
   List<String> iTagList = Arrays.asList( "@SmokeTest", "@JspSmokeTest", "@RefactorNonSmoke",
         "@E2ESmokeBuild1", "@UniqueConfigSmokeTest" );


   @Test
   public void testCheckAnnotationsALL() {

      List<String> lArgs = new ArrayList<String>();
      lArgs.add( "--tags" );
      lArgs.add( "~@Ignore" );
      lArgs.add( "src/main/resources/" );

      testAnnotations( lArgs );

   }


   /**
    * Execute the annotation checks for the params provided
    *
    * @param lArgs
    */
   private void testAnnotations( List<String> lArgs ) {
      RuntimeOptions lRuntimeOptions = new RuntimeOptions( lArgs );

      ResourceLoader lResourceLoader = new MultiLoader( this.getClass().getClassLoader() );

      final List<CucumberFeature> lCucumberFeatures =
            lRuntimeOptions.cucumberFeatures( lResourceLoader );

      // iterate features
      for ( CucumberFeature lCucumberFeature : lCucumberFeatures ) {
         validate( lCucumberFeature );
      }

   }


   /**
    * Checks to ensure either @SmokeTest, @JspSmokeTest or @RefactorNonSmoke are included with each
    * scenario
    *
    */
   private void validate( CucumberFeature aCucumberFeature ) {
      sLOGGER.debug( "FEATURE: " + aCucumberFeature.getGherkinFeature().getName() );

      // iterate scenarios
      List<CucumberTagStatement> lCucumberTagStatements = aCucumberFeature.getFeatureElements();
      for ( CucumberTagStatement lCucumberTagStatement : lCucumberTagStatements ) {
         if ( lCucumberTagStatement instanceof CucumberScenario ) {
            validate( aCucumberFeature, ( CucumberScenario ) lCucumberTagStatement );
         }
      }
   }


   /**
    * Checks to ensure either @SmokeTest, @JspSmokeTest or @RefactorNonSmoke are included on the
    * scenario
    *
    * @param aCucumberScenario
    */
   private void validate( CucumberFeature aCucumberFeature, CucumberScenario aCucumberScenario ) {
      sLOGGER.debug( "SCENARIO: " + aCucumberScenario.getVisualName() );
      List<Tag> lTags = aCucumberScenario.getGherkinModel().getTags();

      boolean found = false;
      for ( Tag lTag : lTags ) {
         String lNameTag = lTag.getName();

         sLOGGER.debug( "TAG: " + lNameTag );

         if ( iTagList.contains( lNameTag ) ) {
            found = true;
         }

      }

      String lMessage =
            "Either the @SmokeTest, @JspSmokeTest, @UniqueConfigSmokeTest or @RefactorNonSmoke annotation must be specified for each Scenario.\n"
                  + "Feature file: " + aCucumberFeature.getPath() + "\n Feature: "
                  + aCucumberFeature.getGherkinFeature().getName() + "\n\t "
                  + aCucumberScenario.getVisualName();

      Assert.assertTrue( lMessage, found );

   }

}
