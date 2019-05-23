
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
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.InventoryKey;


/**
 * This tests the GetAircraftStatus test
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetAircraftStatusTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetAircraftStatusTest.class );
   }


   private static final AircraftKey AIRCRAFT = new AircraftKey( 4650, 1 );
   private static final AircraftKey AIRCRAFT_WITH_CAPACITY = new AircraftKey( 4650, 2 );


   /**
    * Executes the GetAircraftStatus query
    *
    * @param aAircraft
    *           the aircraft
    *
    * @return the query set
    */
   public QuerySet execute( AircraftKey aAircraft ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aAircraft, "aInvNoDbId", "aInvNoId" );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Tests that the capability value is returned properly
    */
   @Test
   public void testAircraftWithCapacity() {
      QuerySet lQs = execute( AIRCRAFT_WITH_CAPACITY );
      assertTrue( lQs.first() );
      assertEquals( AIRCRAFT_WITH_CAPACITY.getInventoryKey(),
            lQs.getKey( InventoryKey.class, "inv_no_db_id", "inv_no_id" ) );
      assertEquals( "TEST", lQs.getString( "capability" ) );
   }


   /**
    * Tests that the information can be returned
    */
   @Test
   public void testBasicAircraft() {
      QuerySet lQs = execute( AIRCRAFT );
      assertTrue( lQs.first() );
      assertEquals( AIRCRAFT.getInventoryKey(),
            lQs.getKey( InventoryKey.class, "inv_no_db_id", "inv_no_id" ) );
      assertEquals( "ACFT_LOC", lQs.getString( "airport_code" ) );
      assertEquals( "REPREQ", lQs.getString( "condition" ) );
      assertTrue( lQs.isNull( "capability" ) );
   }
}
