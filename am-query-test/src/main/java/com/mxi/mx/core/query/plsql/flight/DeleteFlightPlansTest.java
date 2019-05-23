
package com.mxi.mx.core.query.plsql.flight;

import static com.mxi.mx.testing.matchers.MxMatchers.allElements;
import static com.mxi.mx.testing.matchers.MxMatchers.noElements;
import static org.hamcrest.Matchers.isIn;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

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
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.tuple.Pair;
import com.mxi.mx.core.key.InventoryKey;


/**
 * Ensures that DeleteFlightPlans works
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class DeleteFlightPlansTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), DeleteFlightPlansTest.class );
   }


   private static final InventoryKey SPECIFIED_AIRCRAFT = new InventoryKey( 4650, 1 );
   private static final InventoryKey OTHER_AIRCRAFT = new InventoryKey( 4650, 2 );

   @SuppressWarnings( "unchecked" )
   private static final Set<Pair<InventoryKey, Integer>> SPECIFIED_FLIGHT_PLANS =
         new LinkedHashSet<Pair<InventoryKey, Integer>>(
               Arrays.asList( new Pair<InventoryKey, Integer>( SPECIFIED_AIRCRAFT, 1 ),
                     new Pair<InventoryKey, Integer>( SPECIFIED_AIRCRAFT, 2 ),
                     new Pair<InventoryKey, Integer>( SPECIFIED_AIRCRAFT, 3 ) ) );

   @SuppressWarnings( "unchecked" )
   private static final Set<Pair<InventoryKey, Integer>> OTHER_FLIGHT_PLANS =
         new LinkedHashSet<Pair<InventoryKey, Integer>>(
               Arrays.asList( new Pair<InventoryKey, Integer>( OTHER_AIRCRAFT, 1 ),
                     new Pair<InventoryKey, Integer>( OTHER_AIRCRAFT, 2 ) ) );


   /**
    * Ensures that aircraft flight plans are deleted.
    *
    * @throws Exception
    */
   @Test
   public void testDeletes_SpecifiedAircraft() throws Exception {
      deleteFlightPlans();

      assertThat( remainingFlightPlans(), noElements( isIn( SPECIFIED_FLIGHT_PLANS ) ) );
   }


   /**
    * Ensures that other aircraft flight plans are not deleted.
    *
    * @throws Exception
    */
   @Test
   public void testDoesNotDelete_NonSpecifiedAircraft() throws Exception {
      deleteFlightPlans();

      assertThat( OTHER_FLIGHT_PLANS, allElements( isIn( remainingFlightPlans() ) ) );
   }


   /**
    * Deletes the SPECIFIED_AIRCRAFT's flight plan
    *
    * @throws Exception
    */
   private void deleteFlightPlans() throws Exception {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( SPECIFIED_AIRCRAFT, "aAircraftDbId", "aAircraftId" );

      QueryExecutor.executePlsql( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Returns a list of remaining flight plans
    *
    * @return returns the list of flight plans
    */
   private Set<Pair<InventoryKey, Integer>> remainingFlightPlans() {
      Set<Pair<InventoryKey, Integer>> lFlightPlans =
            new LinkedHashSet<Pair<InventoryKey, Integer>>();

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "inv_ac_flight_plan", null,
            "inv_no_db_id", "inv_no_id", "flight_plan_ord" );
      while ( lQs.next() ) {
         lFlightPlans.add( new Pair<InventoryKey, Integer>(
               lQs.getKey( InventoryKey.class, "inv_no_db_id", "inv_no_id" ),
               lQs.getInteger( "flight_plan_ord" ) ) );
      }

      return lFlightPlans;
   }
}
