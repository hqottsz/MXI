
package com.mxi.mx.core.adapter.flight.query.flight;

import static org.junit.Assert.assertEquals;
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
import com.mxi.mx.core.key.InventoryKey;


/**
 * This tests the GetAircraftStatus test
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetAircraftCapabilitiesTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetAircraftCapabilitiesTest.class );
   }


   private static final InventoryKey AIRCRAFT1 = new InventoryKey( 4650, 1 );
   private static final InventoryKey AIRCRAFT2 = new InventoryKey( 4650, 2 );


   /**
    * Executes the GetAircraftStatus query
    *
    * @param aAircraft
    *           the aircraft
    *
    * @return the query set
    */
   public QuerySet execute( InventoryKey aAircraft ) {
      DataSetArgument lArgs = new DataSetArgument();
      if ( aAircraft != null ) {
         String[] lInventoryColumns = { "inv_inv.inv_no_db_id", "inv_inv.inv_no_id" };
         lArgs.addWhereEquals( lInventoryColumns, aAircraft );
      }
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Tests that the capability value is returned properly
    */
   @Test
   public void testSingleAircraft() {
      QuerySet lQs = execute( AIRCRAFT1 );
      assertTrue( lQs.first() );
      assertEquals( "4650:1", lQs.getString( "inv_key" ) );
      assertEquals( "ACFT1111", lQs.getString( "barcode_sdesc" ) );
      assertEquals( "ACFT1", lQs.getString( "ac_reg_cd" ) );
      assertEquals( "SER-01", lQs.getString( "serial_no_oem" ) );
      assertEquals( "A319/A320", lQs.getString( "part_no_oem" ) );
      assertEquals( "ABI", lQs.getString( "manufact_cd" ) );
      assertEquals( "Airbus Industrie", lQs.getString( "manufact_name" ) );

      assertEquals( "ALAND", lQs.getString( "cap_cd" ) );
      assertEquals( "Autoland", lQs.getString( "desc_sdesc" ) );
      assertEquals( "CATIII", lQs.getString( "config_level_cd" ) );
      assertEquals( "CATII", lQs.getString( "current_level_cd" ) );

      assertTrue( lQs.next() );
      assertEquals( "4650:1", lQs.getString( "inv_key" ) );
      assertEquals( "ACFT1111", lQs.getString( "barcode_sdesc" ) );
      assertEquals( "ACFT1", lQs.getString( "ac_reg_cd" ) );
      assertEquals( "SER-01", lQs.getString( "serial_no_oem" ) );
      assertEquals( "A319/A320", lQs.getString( "part_no_oem" ) );
      assertEquals( "ABI", lQs.getString( "manufact_cd" ) );
      assertEquals( "Airbus Industrie", lQs.getString( "manufact_name" ) );

      assertEquals( "SEATNUM", lQs.getString( "cap_cd" ) );
      assertEquals( "Seat Count / Available Seat Count", lQs.getString( "desc_sdesc" ) );
      assertEquals( "143", lQs.getString( "config_level_cd" ) );
      assertEquals( "121", lQs.getString( "current_level_cd" ) );
   }


   /**
    * Tests that the information can be returned
    */
   @Test
   public void testFleet() {
      QuerySet lQs = execute( null );
      // Assert aircraft1
      assertTrue( lQs.first() );
      assertEquals( "4650:1", lQs.getString( "inv_key" ) );
      assertEquals( "ACFT1111", lQs.getString( "barcode_sdesc" ) );
      assertEquals( "ACFT1", lQs.getString( "ac_reg_cd" ) );
      assertEquals( "SER-01", lQs.getString( "serial_no_oem" ) );
      assertEquals( "A319/A320", lQs.getString( "part_no_oem" ) );
      assertEquals( "ABI", lQs.getString( "manufact_cd" ) );
      assertEquals( "Airbus Industrie", lQs.getString( "manufact_name" ) );

      assertEquals( "ALAND", lQs.getString( "cap_cd" ) );
      assertEquals( "Autoland", lQs.getString( "desc_sdesc" ) );
      assertEquals( "CATIII", lQs.getString( "config_level_cd" ) );
      assertEquals( "CATII", lQs.getString( "current_level_cd" ) );

      assertTrue( lQs.next() );
      assertEquals( "4650:1", lQs.getString( "inv_key" ) );
      assertEquals( "ACFT1111", lQs.getString( "barcode_sdesc" ) );
      assertEquals( "ACFT1", lQs.getString( "ac_reg_cd" ) );
      assertEquals( "SER-01", lQs.getString( "serial_no_oem" ) );
      assertEquals( "A319/A320", lQs.getString( "part_no_oem" ) );
      assertEquals( "ABI", lQs.getString( "manufact_cd" ) );
      assertEquals( "Airbus Industrie", lQs.getString( "manufact_name" ) );

      assertEquals( "SEATNUM", lQs.getString( "cap_cd" ) );
      assertEquals( "Seat Count / Available Seat Count", lQs.getString( "desc_sdesc" ) );
      assertEquals( "143", lQs.getString( "config_level_cd" ) );
      assertEquals( "121", lQs.getString( "current_level_cd" ) );

      // Assert aircraft2
      assertTrue( lQs.next() );
      assertEquals( "4650:2", lQs.getString( "inv_key" ) );
      assertEquals( "ACFT2222", lQs.getString( "barcode_sdesc" ) );
      assertEquals( "ACFT2", lQs.getString( "ac_reg_cd" ) );
      assertEquals( "SER-02", lQs.getString( "serial_no_oem" ) );
      assertEquals( "A319/A320", lQs.getString( "part_no_oem" ) );
      assertEquals( "ABI", lQs.getString( "manufact_cd" ) );
      assertEquals( "Airbus Industrie", lQs.getString( "manufact_name" ) );

      assertEquals( "ETOPS", lQs.getString( "cap_cd" ) );
      assertEquals( "Extended Operations", lQs.getString( "desc_sdesc" ) );
      assertEquals( "ETOPS120", lQs.getString( "config_level_cd" ) );
      assertEquals( "ETOPS_90", lQs.getString( "current_level_cd" ) );

      assertTrue( lQs.next() );
      assertEquals( "4650:2", lQs.getString( "inv_key" ) );
      assertEquals( "ACFT2222", lQs.getString( "barcode_sdesc" ) );
      assertEquals( "ACFT2", lQs.getString( "ac_reg_cd" ) );
      assertEquals( "SER-02", lQs.getString( "serial_no_oem" ) );
      assertEquals( "A319/A320", lQs.getString( "part_no_oem" ) );
      assertEquals( "ABI", lQs.getString( "manufact_cd" ) );
      assertEquals( "Airbus Industrie", lQs.getString( "manufact_name" ) );

      assertEquals( "WINGLET", lQs.getString( "cap_cd" ) );
      assertEquals( "Winglets (regular or Split Scimitar) MEL", lQs.getString( "desc_sdesc" ) );
      assertEquals( "REG", lQs.getString( "config_level_cd" ) );
      assertEquals( "NONE", lQs.getString( "current_level_cd" ) );
   }
}
