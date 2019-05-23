
package com.mxi.mx.core.worker.bsync.precondition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.WorkItemBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.services.work.precondition.PreconditionFailedException;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.worker.bsync.preconditions.ZipQueuePrecondition;


/**
 * This class tests the ZipQueuePrecondition class.
 *
 * @author dmacdonald
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class ZipQueuePreconditionTest {

   private InventoryKey iInventoryKey;

   private ZipQueuePrecondition iPrecondition;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Ensure that the precondition evaluation fails when there is an in-progress deadline update
    * work item
    */
   @Test
   public void testFailsWhenInProgressDeadlineUpdate() throws Exception {

      // Set up a work item representing an in-progress aircraft deadline update
      WorkItemBuilder lWorkItem = new WorkItemBuilder();
      lWorkItem.withType( "JOB_AIRCRAFT_DEADLINE_UPDATE" );
      lWorkItem.withKey( iInventoryKey.toString() );
      lWorkItem.withStartDate( DateUtils.getCurrentDateWithZeroTime() );
      lWorkItem.withEndDate( null );
      lWorkItem.build();

      try {
         iPrecondition.evaluate();

         fail( "Expected PreconditionFailedException" );
      } catch ( PreconditionFailedException e ) {
         assertEquals( i18n.get( "core.msg.THE_ZIPPING_OF_TASKS_MUST_WAIT", new Object[] { null } ),
               e.getMessage() );
      }
   }


   /**
    * Ensure that the precondition evaluation passes when there are planned deadline update work
    * items but no in-progress work items
    */
   @Test
   public void testPassesWhenPlannedDeadlineUpdate() throws Exception {

      // Set up a work item representing an planned aircraft deadline update
      WorkItemBuilder lWorkItem = new WorkItemBuilder();
      lWorkItem.withType( "JOB_AIRCRAFT_DEADLINE_UPDATE" );
      lWorkItem.withKey( iInventoryKey.toString() );
      lWorkItem.withStartDate( null );
      lWorkItem.withEndDate( null );
      lWorkItem.build();

      iPrecondition.evaluate();
   }


   /**
    * Ensure that the precondition evaluation passes when there are complete deadline update work
    * items but no in-progress work items
    */
   @Test
   public void testPassesWhenCompleteDeadlineUpdate() throws Exception {

      WorkItemBuilder lWorkItem = new WorkItemBuilder();
      lWorkItem.withType( "JOB_AIRCRAFT_DEADLINE_UPDATE" );
      lWorkItem.withKey( iInventoryKey.toString() );
      lWorkItem.withStartDate( DateUtils.getCurrentDateWithZeroTime() );
      lWorkItem.withEndDate( DateUtils.getCurrentDateWithZeroTime() );
      lWorkItem.build();

      iPrecondition.evaluate();
   }


   /**
    * Test against a false negative, Inventory:
    *
    * When there are two inventories A and B,and the key representation of A is a substring of the
    * key representation of B, the precondition for inventory A passes when there is an in-progress
    * deadline update work item for inventory B but no in-progress deadline update work item for
    * inventory A
    */
   @Test
   public void testPassesWhenSimilarInventoryDeadlineUpdate() throws Exception {

      // Simulate an inventory for whose key the iInventoryKey is a substring
      String lSimilarInventoryKey = iInventoryKey.toString().concat( "0" );
      String lSimilarWorkItemKey =
            lSimilarInventoryKey.concat( ":" ).concat( lSimilarInventoryKey );

      WorkItemBuilder lWorkItem = new WorkItemBuilder();
      lWorkItem.withType( "JOB_INVENTORY_DEADLINE_UPDATE" );
      lWorkItem.withKey( lSimilarWorkItemKey );
      lWorkItem.withStartDate( DateUtils.getCurrentDateWithZeroTime() );
      lWorkItem.withEndDate( null );
      lWorkItem.build();

      iPrecondition.evaluate();
   }


   /**
    * Test against a false negative, Aircraft:
    *
    * When there are two aircraft A and B,and the key representation of A is a substring of the key
    * representation of B, the precondition for aircraft A passes when there is an in-progress
    * deadline update work item for aircraft B but no in-progress deadline update work item for
    * aircraft A
    */
   @Test
   public void testPassesWhenSimilarAircraftDeadlineUpdate() throws Exception {

      // Simulate an inventory for whose key the iInventoryKey is a substring
      String lSimilarWorkItemKey = iInventoryKey.toString().concat( "0" );

      WorkItemBuilder lWorkItem = new WorkItemBuilder();
      lWorkItem.withType( "JOB_AIRCRAFT_DEADLINE_UPDATE" );
      lWorkItem.withKey( lSimilarWorkItemKey );
      lWorkItem.withStartDate( DateUtils.getCurrentDateWithZeroTime() );
      lWorkItem.withEndDate( null );
      lWorkItem.build();

      iPrecondition.evaluate();
   }


   @Before
   public void setup() {
      iInventoryKey = Domain.createAircraft();
      iPrecondition = new ZipQueuePrecondition.ZipQueuePreconditionBuilder()
            .hInventory( iInventoryKey ).build();
   }
}
