package com.mxi.mx.core.services.req;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.PartRequirement;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.PartRequirementBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefReqPriorityKey;
import com.mxi.mx.core.key.TaskInstPartKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.table.req.ReqPartTable;


/**
 * Test the validations are being done by PartRequestServiceWithValidation
 *
 */
public class PartRequestServiceWithValidationTest {

   private static final String SUPRESSING_TASK_BARCODE = "TRUMP_TASK";

   private static final String NORMAL_TASK_BARCODE = "OBAMA_TASK";

   /** If a history note needs to be added */
   private static final boolean HISTORY_NOTE_NEEDED = false;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Test
   public void testPartRequestWhenRequestAlreadyExistsForTask() {
      TaskKey lTaskKey = createTaskWithPartRequest();
      TaskInstPartKey lTaskInstPartKey =
            new TaskInstPartKey( lTaskKey.getDbId(), lTaskKey.getId(), 1, 1 );
      HumanResourceKey lHrKey = new HumanResourceKey( lTaskKey.getDbId(), 0 );
      try {

         PartRequestServiceWithValidation.createPartRequest( lTaskInstPartKey, null, lHrKey, "",
               true, true, HISTORY_NOTE_NEEDED );

         fail( "Expected PartRequestExistsException" );
      } catch ( PartRequestExistsException e ) {

         // Verify that the exception was thrown and contains the expected message.
         assertEquals( "core.err.30167", e.getMessageKey() );

      } catch ( MxException | TriggerException e ) {
         fail( "Unexpected exception" );
      }

   }


   @Test
   public void testPartRequestWithNonDefaultPriority() {
      TaskKey lTaskKey = new TaskBuilder().withBarcode( NORMAL_TASK_BARCODE ).build();
      // create a part group
      PartGroupKey lPartGroup = new PartGroupDomainBuilder( "MYGROUP" ).build();
      // build part
      PartNoKey lPartNoKey = new PartNoBuilder().withUnitType( RefQtyUnitKey.EA )
            .withOemPartNo( "123" ).withInventoryClass( RefInvClassKey.BATCH )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withStatus( RefPartStatusKey.ACTV )
            .withRepairBool( false ).isAlternateIn( lPartGroup ).build();
      // Create inventory to be installed
      InventoryKey lInventoryKey = new InventoryBuilder().withPartNo( lPartNoKey ).build();
      // Create a Part Requirement to associate task
      PartRequirement lPartRequirement = new PartRequirement();
      lPartRequirement.setPartGroup( lPartGroup );
      lPartRequirement.setTaskKey( lTaskKey );
      lPartRequirement.setQuantity( 1.0 );
      lPartRequirement.setPartInstallRequest( lPartRequirement.new PartInstallRequest()
            .withPartNo( lPartNoKey ).withInventory( lInventoryKey ) );
      PartRequirementBuilder.build( lPartRequirement );
      // Build a Task Install Part key based on the above data
      TaskInstPartKey lTaskInstPartKey =
            new TaskInstPartKey( lTaskKey.getDbId(), lTaskKey.getId(), lPartGroup.getId(), 1 );
      // Create a fake human
      HumanResourceKey lHrKey = new HumanResourceKey( lTaskKey.getDbId(), 0 );
      try {
         // Attempt to create the part requirement using the Task Update path used by
         // update_task_revision
         PartRequestKey lPartRequestKey =
               PartRequestServiceWithValidation.createPartRequest( lTaskInstPartKey, null, lHrKey,
                     "", true, true, HISTORY_NOTE_NEEDED, RefReqPriorityKey.AOG );

         assertNotNull( "Part Request was not created.", lPartRequestKey );

         ReqPartTable lPartRequestTable = ReqPartTable.findByPrimaryKey( lPartRequestKey );
         assertEquals( RefReqPriorityKey.AOG, lPartRequestTable.getReqPriority() );

      } catch ( Exception e ) {
         fail( "Unexpected exception" );
      }

   }


   @Test
   public void testPartRequestWhenRequestAlreadyExistsForTaskWithoutInstallation() {
      TaskKey lTaskKey = createTaskWithPartRequest();
      TaskPartKey lTaskPartKey = new TaskPartKey( lTaskKey.getDbId(), lTaskKey.getId(), 1 );
      HumanResourceKey lHrKey = new HumanResourceKey( lTaskKey.getDbId(), 0 );
      try {

         PartRequestServiceWithValidation.createPartRequestWithoutInstallation( lTaskPartKey, null,
               lHrKey, "", true, true );

         fail( "Expected PartRequestExistsException" );
      } catch ( PartRequestExistsException e ) {

         // Verify that the exception was thrown and contains the expected message.
         assertEquals( "core.err.30167", e.getMessageKey() );

      } catch ( MxException | TriggerException e ) {
         fail( "Unexpected exception" );
      }

   }


   @Test
   public void testPartRequestOnHistoricTask() {
      TaskKey lTaskKey = createHistoricTask();
      TaskInstPartKey lTaskInstPartKey =
            new TaskInstPartKey( lTaskKey.getDbId(), lTaskKey.getId(), 1, 1 );
      HumanResourceKey lHrKey = new HumanResourceKey( lTaskKey.getDbId(), 0 );
      try {

         PartRequestServiceWithValidation.createPartRequest( lTaskInstPartKey, null, lHrKey, "",
               true, true, HISTORY_NOTE_NEEDED );

         fail( "Expected HistoricTaskException" );
      } catch ( HistoricTaskException e ) {

         // Verify that the exception was thrown and contains the expected message.
         assertEquals( "core.err.30174", e.getMessageKey() );

      } catch ( MxException | TriggerException e ) {
         fail( "Unexpected exception" );
      }

   }


   @Test
   public void testPartRequestOnHistoricTaskWithoutInstallation() {
      TaskKey lTaskKey = createHistoricTask();
      TaskPartKey lTaskPartKey = new TaskPartKey( lTaskKey.getDbId(), lTaskKey.getId(), 1 );
      HumanResourceKey lHrKey = new HumanResourceKey( lTaskKey.getDbId(), 0 );
      try {

         PartRequestServiceWithValidation.createPartRequestWithoutInstallation( lTaskPartKey, null,
               lHrKey, "", true, true );

         fail( "Expected HistoricTaskException" );
      } catch ( HistoricTaskException e ) {

         // Verify that the exception was thrown and contains the expected message.
         assertEquals( "core.err.30174", e.getMessageKey() );

      } catch ( MxException | TriggerException e ) {
         fail( "Unexpected exception" );
      }

   }


   @Test
   public void testPartReqOnSuppressedTask() {
      TaskKey lTaskKey = createSuppressedTask();
      TaskInstPartKey lTaskInstPartKey =
            new TaskInstPartKey( lTaskKey.getDbId(), lTaskKey.getId(), 1, 1 );
      HumanResourceKey lHrKey = new HumanResourceKey( lTaskKey.getDbId(), 0 );
      try {

         PartRequestServiceWithValidation.createPartRequest( lTaskInstPartKey, null, lHrKey, "",
               true, true, HISTORY_NOTE_NEEDED );

         fail( "Expected PartRequestIsForSuppressedTaskException" );
      } catch ( PartRequestIsForSuppressedTaskException e ) {

         // Verify that the exception was thrown and contains the expected message.
         assertEquals( "core.err.31646", e.getMessageKey() );

      } catch ( MxException | TriggerException e ) {
         fail( "Unexpected exception" );
      }
   }


   @Test
   public void testPartReqWithoutInstallationOnSuppressedTask() {
      TaskKey lTaskKey = createSuppressedTask();
      TaskPartKey lTaskPartKey = new TaskPartKey( lTaskKey.getDbId(), lTaskKey.getId(), 1 );
      HumanResourceKey lHrKey = new HumanResourceKey( lTaskKey.getDbId(), 0 );
      try {

         PartRequestServiceWithValidation.createPartRequestWithoutInstallation( lTaskPartKey, null,
               lHrKey, "", true, true );

         fail( "Expected PartRequestIsForSuppressedTaskException" );
      } catch ( PartRequestIsForSuppressedTaskException e ) {

         // Verify that the exception was thrown and contains the expected message.
         assertEquals( "core.err.31646", e.getMessageKey() );

      } catch ( MxException | TriggerException e ) {
         fail( "Unexpected exception" );
      }
   }


   /**
    * Helper methods to set up data.
    *
    */
   private TaskKey createTaskWithPartRequest() {

      TaskKey lTaskKey =
            new TaskBuilder().asHistoric().withBarcode( SUPRESSING_TASK_BARCODE ).build();
      // create a part group
      PartGroupKey lPartGroup = new PartGroupDomainBuilder( "MYGROUP" ).build();

      // build part
      PartNoKey lPartNoKey = new PartNoBuilder().withUnitType( RefQtyUnitKey.EA )
            .withOemPartNo( "123" ).withInventoryClass( RefInvClassKey.BATCH )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withStatus( RefPartStatusKey.ACTV )
            .withRepairBool( false ).isAlternateIn( lPartGroup ).build();

      new PartRequestBuilder().forTask( lTaskKey ).forSpecifiedPart( lPartNoKey )
            .forPartGroup( lPartGroup ).build();

      return lTaskKey;

   }


   private TaskKey createHistoricTask() {

      TaskKey lTaskKey =
            new TaskBuilder().asHistoric().withBarcode( SUPRESSING_TASK_BARCODE ).build();

      return lTaskKey;

   }


   private TaskKey createSuppressedTask() {

      TaskKey lSupressingTask = new TaskBuilder().withBarcode( SUPRESSING_TASK_BARCODE ).build();
      return new TaskBuilder().withSuppressingTask( lSupressingTask ).build();

   }

}
