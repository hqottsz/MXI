package com.mxi.mx.core.query.location;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import java.util.List;

import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QueryRow;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefLocTypeKey;


/**
 * Test case for GetPreferredLocationsBySupply.qrx
 *
 * @author dmacdonald
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetPreferredLocationsBySupplyOnColsTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   // A preferred serviceable store location
   private final static SimpleLocation iTwoPrefLine =
         new SimpleLocation( "L2", "Line with 2 preferences" );
   // A second preferred servicable store location
   private final static SimpleLocation iOnePrefLine =
         new SimpleLocation( "L1", "Line with 1 preferences" );
   // A serviceable store location that is not preferred
   private final static SimpleLocation iZeroPrefLine =
         new SimpleLocation( "L0", "Line with 0 preferences" );

   // A maintenance location that has two preferences
   private final static SimpleLocation iFirstPrefStore =
         new SimpleLocation( "PREF1", "FirstPreferred" );
   // A maintenance location that has one preference
   private final static SimpleLocation iSecondPrefStore =
         new SimpleLocation( "PREF2", "Second Preferred" );
   // A maintenance location that has no preference
   private final static SimpleLocation iNonPrefStore =
         new SimpleLocation( "NONPREF", "Non PREferred" );

   private LocationKey iSupplyLocation;
   private LocationKey iHubLocation;


   private void buildSupplyLocations() {
      iHubLocation =
            new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();
      iSupplyLocation = new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT ).isSupplyLocation()
            .withHubLocation( iHubLocation ).build();
   }


   private LocationKey buildStoreLocation( SimpleLocation aLocation ) {
      return aLocation.buildWith( new LocationDomainBuilder().withType( RefLocTypeKey.SRVSTORE )
            .withParent( iSupplyLocation ).withSupplyLocation( iSupplyLocation ) ).build();

   }


   private LocationKey buildLineLocation( SimpleLocation aLocation,
         LocationKey... aPreferredLocations ) {
      return aLocation.buildWith(
            new LocationDomainBuilder().withType( RefLocTypeKey.LINE ).withParent( iSupplyLocation )
                  .withSupplyLocation( iSupplyLocation ).withPrefLocations( aPreferredLocations ) )
            .build();
   }


   @Before
   public void setUpPreferredLocations() {

      // Two stores, one is the hub, one is the supply location for the following
      buildSupplyLocations();

      // Three stores
      LocationKey lFirstPrefStoreLocation = buildStoreLocation( iFirstPrefStore );
      LocationKey lSecondPrefStoreLocation = buildStoreLocation( iSecondPrefStore );
      buildStoreLocation( iNonPrefStore );

      // Three lines
      buildLineLocation( iTwoPrefLine, lFirstPrefStoreLocation, lSecondPrefStoreLocation );
      buildLineLocation( iOnePrefLine, lFirstPrefStoreLocation );
      buildLineLocation( iZeroPrefLine );

   }


   private QuerySet getPreferredLocations( LocationKey aSupplyLocation ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aLocDbId", aSupplyLocation.getDbId() );
      lArgs.add( "aLocId", aSupplyLocation.getId() );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    *
    * Test that a row with 2 preferences and a row with 1 preference are retrieved, and that a row
    * with no preferences is not retrieved
    *
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void testShowPreferredLocationsUnderSupply() {

      // execute the query with the supply location
      QuerySet iQuerySet = getPreferredLocations( iSupplyLocation );

      // assert two line locations with preferences are returned. One has two preferred locations,
      // one has one. One with none is not returned
      List<? extends QueryRow> lRows = iQuerySet.getRows();
      Assert.assertThat( "Unexpected rows returned", lRows,
            containsInAnyOrder(
                  PreferenceMatcher.hasPreference( iTwoPrefLine, iFirstPrefStore,
                        iSecondPrefStore ),
                  PreferenceMatcher.hasPreference( iOnePrefLine, iFirstPrefStore, null ) ) );

   }


   /**
    *
    * Test that only locations under the queried supply are returns
    *
    */
   @Test
   public void testDoesNotShowPreferredLocationsUnderDifferentSupply() {

      // execute the query with the supply location
      QuerySet iQuerySet = getPreferredLocations( iHubLocation );

      // assert zero preferences are returned.
      List<? extends QueryRow> lRows = iQuerySet.getRows();
      Assert.assertThat( "Unexpected rows returned", lRows.size(), equalTo( 0 ) );

   }


   public final static class SimpleLocation {

      private final String iCode;
      private final String iName;


      public SimpleLocation(String aCode, String aName) {
         iCode = aCode;
         iName = aName;
      }


      public String displayString() {
         return iCode.concat( " (" ).concat( iName ).concat( ")" );
      }


      public LocationDomainBuilder buildWith( LocationDomainBuilder aBuilder ) {
         return aBuilder.withCode( iCode ).withName( iName );
      }
   }

   static public class PreferenceMatcher extends TypeSafeMatcher<QueryRow> {

      private SimpleLocation lMaint;
      private SimpleLocation lPref1;
      private SimpleLocation lPref2;


      public PreferenceMatcher(SimpleLocation aMaint, SimpleLocation aPref1,
            SimpleLocation aPref2) {
         lMaint = aMaint;
         lPref1 = aPref1;
         lPref2 = aPref2;
      }


      @Override
      protected boolean matchesSafely( QueryRow aItem ) {
         String lMaintLocName = aItem.getString( "maint_loc_name" );
         String lFirstMaterialLocName = aItem.getString( "first_material_loc_name" );
         String lSecondMaterialLocName = aItem.getString( "second_material_loc_name" );

         return lMaintLocName.equals( lMaint.displayString() )
               && lFirstMaterialLocName.equals( lPref1.displayString() )
               && ( lSecondMaterialLocName == null
                     || lSecondMaterialLocName.equals( lPref2.displayString() ) );
      }


      @Override
      public void describeTo( org.hamcrest.Description aDescription ) {
         aDescription.appendText( "Didn't match " ).appendValue( lMaint.displayString() )
               .appendValue( lPref1.displayString() )
               .appendValue( lPref2 != null ? lPref2.displayString() : "" );
      }


      @Factory
      public static PreferenceMatcher hasPreference( SimpleLocation aMaint, SimpleLocation aPref1,
            SimpleLocation aPref2 ) {
         return new PreferenceMatcher( aMaint, aPref1, aPref2 );
      }
   }
}
