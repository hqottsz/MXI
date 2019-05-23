
package com.mxi.mx.core.query.capability;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.InventoryKey;


/**
 * This class tests the query
 * com.mxi.mx.core.query.capability.isAircraftAffectedByRemovalOfCapability.qrx
 *
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class IsAircraftAffectedByRemovalOfCapabilityTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            IsAircraftAffectedByRemovalOfCapabilityTest.class );
   }


   private static final InventoryKey AIRCRAFT__KEY = new InventoryKey( 4650, 300785 );

   private static final String ETOPS = "ETOPS";

   private static final String ALAND = "ALAND";

   private static final int CAP_DB_ID = 10;


   /**
    * Tests to see if the aircraft is affected when a set capability is removed from it.
    *
    * Preconditions:
    *
    * There is data setup needed in baseline and actuals. ETOPS capability needs to be set to the
    * aircraft and ALAND needs to exist but not be set to the aircraft.
    *
    * Action:
    *
    * Call the IsAircraftAffectedByRemovalOfCapability for ETOPS
    *
    * Expectation:
    *
    * A row should be returned for ETOPS.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testIsAircraftAffectedByRemovalOfUnsetCapability() throws Exception {

      // ARRANGE
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aInvNoDbId", AIRCRAFT__KEY.getDbId() );
      lArgs.add( "aInvNoId", AIRCRAFT__KEY.getId() );
      lArgs.add( "aCapDbId", CAP_DB_ID );
      lArgs.add( "aCapCd", ETOPS );

      // ACT
      DataSet lSetCapability = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // ASSERT
      assertTrue( !lSetCapability.isEmpty() );

   }


   /**
    * Tests to see if the aircraft is affected when an unset capability is removed from it.
    *
    * Preconditions:
    *
    * There is data setup needed in baseline and actuals. ETOPS capability needs to be set to the
    * aircraft and ALAND needs to exist but not be set to the aircraft.
    *
    * Action:
    *
    * Call the IsAircraftAffectedByRemovalOfCapability for ALAND
    *
    * Expectation:
    *
    * A row should not be returned for ALAND.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testIsAircraftAffectedByRemovalOfSetCapability() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aInvNoDbId", AIRCRAFT__KEY.getDbId() );
      lArgs.add( "aInvNoId", AIRCRAFT__KEY.getId() );
      lArgs.add( "aCapDbId", CAP_DB_ID );
      lArgs.add( "aCapCd", ALAND );

      // ACT

      DataSet lUnsetCapability =
            QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
                  QueryExecutor.getQueryName( getClass() ), lArgs );

      // ASSERT
      assertTrue( lUnsetCapability.isEmpty() );

   }

}
