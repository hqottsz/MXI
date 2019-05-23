
package com.mxi.mx.core.query.print;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.core.key.InvLocPrinterKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefJobTypeKey;


/**
 * This tests the getDefaultPrinterByLocationAndJobType.qrx query
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetDefaultPrinterByLocationAndJobTypeTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetDefaultPrinterByLocationAndJobTypeTest.class );
   }


   private static final LocationKey REGIONAL_AIRPORT_LOCATION = new LocationKey( 4650, 5 );
   private static final LocationKey AIRPORT_LOCATION = new LocationKey( 4650, 1 );
   private static final LocationKey SUB_AIRPORT_LOCATION = new LocationKey( 4650, 2 );
   private static final LocationKey SUB_AIRPORT_LOCATION_WITH_PRINTER = new LocationKey( 4650, 3 );

   private static final InvLocPrinterKey AIRPORT_PRINTER = new InvLocPrinterKey( 4650, 1, 1 );
   private static final InvLocPrinterKey SUB_AIRPORT_PRINTER = new InvLocPrinterKey( 4650, 3, 1 );


   /**
    * Ensure that we can use the current location's printer
    */
   @Test
   public void testPrinterAtCurrentLocation_ReturnsCurrentLocationPrinter() {
      QuerySet lQs = execute( AIRPORT_LOCATION );
      assertTrue( lQs.first() );

      InvLocPrinterKey lLocPrinter = lQs.getKey( InvLocPrinterKey.class, "printer_key" );
      assertEquals( AIRPORT_PRINTER, lLocPrinter );
   }


   /**
    * Ensure that the parent pritner is used if there is no current printer
    */
   @Test
   public void testPrinterAtParentLocation_ReturnsParentLocationPrinter() {
      QuerySet lQs = execute( SUB_AIRPORT_LOCATION );
      assertTrue( lQs.first() );

      InvLocPrinterKey lLocPrinter = lQs.getKey( InvLocPrinterKey.class, "printer_key" );
      assertEquals( AIRPORT_PRINTER, lLocPrinter );
   }


   /**
    * Ensures that the current printer is used instead of the parent printers
    */
   @Test
   public void testPrinterAtParentLocationAndCurrentLocation_ReturnsCurrentLocationPrinter() {
      QuerySet lQs = execute( SUB_AIRPORT_LOCATION_WITH_PRINTER );
      assertTrue( lQs.first() );

      InvLocPrinterKey lLocPrinter = lQs.getKey( InvLocPrinterKey.class, "printer_key" );
      assertEquals( SUB_AIRPORT_PRINTER, lLocPrinter );
   }


   /**
    * Ensures that lookup doesn't go above airport level
    */
   @Test
   public void testPrinterAtRegionalLevel_ReturnsNoPrinter() {
      QuerySet lQs = execute( REGIONAL_AIRPORT_LOCATION );
      assertFalse( lQs.first() );
   }


   /**
    * Executes the query at a given location
    *
    * @param aLocation
    *           the location
    *
    * @return the {@link QuerySet}
    */
   private QuerySet execute( LocationKey aLocation ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aLocation, "aLocDbId", "aLocId" );
      lArgs.add( RefJobTypeKey.ISSUE_TX, "aJobTypeDbId", "aJobTypeCd" );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.print.getDefaultPrinterByLocationAndJobType", lArgs );
   }
}
