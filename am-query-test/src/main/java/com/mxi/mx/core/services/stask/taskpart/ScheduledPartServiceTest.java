
package com.mxi.mx.core.services.stask.taskpart;

import static com.mxi.mx.testing.matchers.MxMatchers.keyArgument;
import static com.mxi.mx.testing.matchers.MxMatchers.query;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QueryAccessObject;
import com.mxi.mx.common.unittest.dao.DataSetBuilder;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.ExternalReferenceItemKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.RefControlMethodKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefReqActionKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.maintenance.plan.externalreference.domain.ExternalReferenceItemRepository;
import com.mxi.mx.core.services.event.InvalidStatusException;
import com.mxi.mx.core.table.sched.SchedPartTable;
import com.mxi.mx.core.unittest.table.evt.EvtStage;


/**
 * This test class tests the {@link ScheduledPartService} class.
 *
 * @author hzheng
 */
public final class ScheduledPartServiceTest {

   private static final InventoryKey INVENTORYKEY = new InventoryKey( 4650, 1 );

   private static final boolean NO_AUTO_RESERVATION = false;
   private static final boolean PERFORM_VALIDATION = true;
   private static final boolean PERFORM_NO_VALIDATION = false;
   private static final boolean MANUALLY_REQUESTED = true;
   private static final boolean NOT_MANUALLY_REQUESTED = false;

   private static final String PART_GROUP_CODE = "PART_GROUP_CODE";

   private static final int USER_ID = 1;

   private Mockery iContext = new Mockery();

   private QueryAccessObject iQao;

   private ScheduledPartService iScheduledPartService;

   private ExternalReferenceItemRepository extRefItemRepository;

   @Rule
   public OperateAsUserRule iOperateAsUserRule = new OperateAsUserRule( USER_ID, "testuser" );

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() throws Exception {

      Domain.createHumanResource( humanResource -> {
         humanResource.setUser( Domain.createUser( user -> {
            user.setUserId( USER_ID );
         } ) );
      } );

      UserParameters.getInstance( USER_ID, "SECURED_RESOURCE" )
            .setBoolean( "ACTION_REMOVE_HISTORIC_PART_REQUIREMENT", true );

      iQao = iContext.mock( QueryAccessObject.class );
      iScheduledPartService = new ScheduledPartService( null, iQao );
      extRefItemRepository =
            InjectorContainer.get().getInstance( ExternalReferenceItemRepository.class );
   }


   /**
    *
    * Test that a part requirement can be created without a reference
    *
    * @throws Exception
    */
   @Test
   public void test_schedulePartRequirement_withNoReference() throws Exception {

      // ASSEMBLE
      InventoryKey inventoryKey = new InventoryBuilder().build();
      TaskKey taskKey = new TaskBuilder().onInventory( inventoryKey ).build();
      PartGroupKey partGroupKey = new PartGroupDomainBuilder( PART_GROUP_CODE )
            .withInventoryClass( RefInvClassKey.SER ).build();

      PartRequirementTO partRequirementTO = new PartRequirementTO();
      partRequirementTO.setBomPartKey( partGroupKey );
      partRequirementTO.setWarningConfirmed( false );
      partRequirementTO.setSchedQty( 1.0 );
      partRequirementTO.setRequestAction( RefReqActionKey.REQ );
      partRequirementTO.setReferenceItemName( "" );

      // ACT
      TaskPartKey taskRequirementKey = iScheduledPartService.schedulePartRequirement( taskKey,
            partRequirementTO, NO_AUTO_RESERVATION, PERFORM_VALIDATION, MANUALLY_REQUESTED );

      // ASSERT
      assertNotNull( taskRequirementKey );

      SchedPartTable table = SchedPartTable.findByPrimaryKey( taskRequirementKey );
      assertNull( table.getExternalReferenceItemKey() );

   }


   /**
    *
    * Tests that if a reference item does not already exist, it will be added to the external
    * reference item table and added to the part requirement.
    *
    * @throws Exception
    */

   @Test
   public void test_schedulePartRequirement_withNewReference_manualyCreated() throws Exception {

      // ASSEMBLE
      final String REFERENCE = "IPC:52-21-10";
      assertFalse( extRefItemRepository.getByReferenceItemName( REFERENCE ).isPresent() );

      InventoryKey inventoryKey = new InventoryBuilder().build();
      TaskKey taskKey = new TaskBuilder().onInventory( inventoryKey ).build();
      PartGroupKey partGroupKey = new PartGroupDomainBuilder( PART_GROUP_CODE )
            .withInventoryClass( RefInvClassKey.SER ).build();

      PartRequirementTO partRequirementTO = new PartRequirementTO();
      partRequirementTO.setBomPartKey( partGroupKey );
      partRequirementTO.setWarningConfirmed( false );
      partRequirementTO.setSchedQty( 1.0 );
      partRequirementTO.setRequestAction( RefReqActionKey.REQ );
      partRequirementTO.setReferenceItemName( REFERENCE );

      // ACT
      TaskPartKey taskRequirementKey = iScheduledPartService.schedulePartRequirement( taskKey,
            partRequirementTO, NO_AUTO_RESERVATION, PERFORM_VALIDATION, MANUALLY_REQUESTED );

      // ASSERT

      // reference item should now be in the table
      Optional<ExternalReferenceItemKey> extRefItemKeyOptional =
            extRefItemRepository.getByReferenceItemName( REFERENCE );
      assertTrue( extRefItemKeyOptional.isPresent() );

      assertNotNull( taskRequirementKey );
      SchedPartTable table = SchedPartTable.findByPrimaryKey( taskRequirementKey );
      assertTrue( table.getExternalReferenceItemKey().equals( extRefItemKeyOptional.get() ) );
      assertTrue( RefControlMethodKey.MANUAL.equals( table.getRefControlMethodKey() ) );
   }


   @Test
   public void test_schedulePartRequirement_withNewReference_automaticallyCreated()
         throws Exception {

      // ASSEMBLE
      final String REFERENCE = "IPC:52-21-10";
      assertFalse( extRefItemRepository.getByReferenceItemName( REFERENCE ).isPresent() );

      InventoryKey inventoryKey = new InventoryBuilder().build();
      TaskKey taskKey = new TaskBuilder().onInventory( inventoryKey ).build();
      PartGroupKey partGroupKey = new PartGroupDomainBuilder( PART_GROUP_CODE )
            .withInventoryClass( RefInvClassKey.SER ).build();

      PartRequirementTO partRequirementTO = new PartRequirementTO();
      partRequirementTO.setBomPartKey( partGroupKey );
      partRequirementTO.setWarningConfirmed( false );
      partRequirementTO.setSchedQty( 1.0 );
      partRequirementTO.setRequestAction( RefReqActionKey.REQ );
      partRequirementTO.setReferenceItemName( REFERENCE );

      // ACT
      TaskPartKey taskRequirementKey = iScheduledPartService.schedulePartRequirement( taskKey,
            partRequirementTO, NO_AUTO_RESERVATION, PERFORM_NO_VALIDATION, NOT_MANUALLY_REQUESTED );

      // ASSERT

      // reference item should now be in the table
      Optional<ExternalReferenceItemKey> extRefItemKeyOptional =
            extRefItemRepository.getByReferenceItemName( REFERENCE );
      assertTrue( extRefItemKeyOptional.isPresent() );

      assertNotNull( taskRequirementKey );
      SchedPartTable table = SchedPartTable.findByPrimaryKey( taskRequirementKey );
      assertTrue( table.getExternalReferenceItemKey().equals( extRefItemKeyOptional.get() ) );
      assertNull( table.getRefControlMethodKey() );
   }


   /**
    *
    * Tests that if a reference item already exist, it will be assigned to the part requirement.
    *
    * @throws Exception
    */

   @Test
   public void test_schedulePartRequirement_withExistingReference() throws Exception {

      // ASSEMBLE
      final String REFERENCE = "IPC:72-21-10";
      extRefItemRepository.save( REFERENCE );
      Optional<ExternalReferenceItemKey> extRefItemKeyOptional =
            extRefItemRepository.getByReferenceItemName( REFERENCE );
      assertTrue( extRefItemKeyOptional.isPresent() );

      InventoryKey inventoryKey = new InventoryBuilder().build();
      TaskKey taskKey = new TaskBuilder().onInventory( inventoryKey ).build();
      PartGroupKey partGroupKey = new PartGroupDomainBuilder( PART_GROUP_CODE )
            .withInventoryClass( RefInvClassKey.SER ).build();

      PartRequirementTO partRequirementTO = new PartRequirementTO();
      partRequirementTO.setBomPartKey( partGroupKey );
      partRequirementTO.setWarningConfirmed( false );
      partRequirementTO.setSchedQty( 1.0 );
      partRequirementTO.setRequestAction( RefReqActionKey.NOREQ );
      partRequirementTO.setReferenceItemName( REFERENCE );

      // ACT
      TaskPartKey taskRequirementKey = iScheduledPartService.schedulePartRequirement( taskKey,
            partRequirementTO, NO_AUTO_RESERVATION, PERFORM_VALIDATION, MANUALLY_REQUESTED );

      // ASSERT
      assertNotNull( taskRequirementKey );
      SchedPartTable table = SchedPartTable.findByPrimaryKey( taskRequirementKey );
      // existing reference item should be added to the part requirement
      assertTrue( table.getExternalReferenceItemKey().equals( extRefItemKeyOptional.get() ) );

   }


   /**
    * Test case for {@linkplain ScheduledPartService#schedulePartRequirement}.<br>
    * <br>
    * Verify that when the part group of the part requirement is not applicable with the main
    * inventory of the task AND the call is made manually, that the method throws a
    * InvalidBomPartException.
    *
    * @throws Exception
    */
   @Test
   public void test_schedulePartRequirement_whenPartGroupIsNotApplicableAndCallIsManual()
         throws Exception {

      // Set up an applicability code that is outside an applicability range.
      String lApplRange = "100-200";
      String lApplCodeOutsideOfRange = "999";

      // The task needs a main inventory with an applicability code that is not
      // within the applicability range of the part group.
      InventoryKey lTaskInv =
            new InventoryBuilder().withApplicabilityCode( lApplCodeOutsideOfRange ).build();
      TaskKey lTask = new TaskBuilder().onInventory( lTaskInv ).build();

      // The TO needs the following:
      // - a part group that is not for a kit and has a non-applicable range
      // - warning is not confirmed
      PartGroupKey lPartGroup = new PartGroupDomainBuilder( PART_GROUP_CODE )
            .withInventoryClass( RefInvClassKey.SER ).withApplicabilityRange( lApplRange ).build();
      PartRequirementTO lTO = new PartRequirementTO();
      lTO.setBomPartKey( lPartGroup );
      lTO.setWarningConfirmed( false );

      try {
         iScheduledPartService.schedulePartRequirement( lTask, lTO, NO_AUTO_RESERVATION,
               PERFORM_VALIDATION, MANUALLY_REQUESTED );
         fail( "Expected InvalidBomPartException to be thrown." );
      } catch ( InvalidBomPartException ex ) {
         // success
      }
   }


   /**
    * Test case for {@linkplain ScheduledPartService#schedulePartRequirement}.<br>
    * <br>
    * Verify that when the part group of the part requirement is not applicable with the main
    * inventory of the task AND the call is not made manually, that the method returns null.
    *
    * @throws Exception
    */
   @Test
   public void test_schedulePartRequirement_whenPartGroupIsNotApplicableAndCallIsNotManual()
         throws Exception {

      // Set up an applicability code that is outside an applicability range.
      String lApplRange = "100-200";
      String lApplCodeOutsideOfRange = "999";

      // The task needs a main inventory with an applicability code that is not
      // within the applicability range of the part group.
      InventoryKey lTaskInv =
            new InventoryBuilder().withApplicabilityCode( lApplCodeOutsideOfRange ).build();
      TaskKey lTask = new TaskBuilder().onInventory( lTaskInv ).build();

      // The TO needs the following:
      // - a part group that is not for a kit and has a non-applicable range
      // - warning is not confirmed
      PartGroupKey lPartGroup = new PartGroupDomainBuilder( PART_GROUP_CODE )
            .withInventoryClass( RefInvClassKey.SER ).withApplicabilityRange( lApplRange ).build();
      PartRequirementTO lTO = new PartRequirementTO();
      lTO.setBomPartKey( lPartGroup );
      lTO.setWarningConfirmed( false );

      TaskPartKey lTaskPart = iScheduledPartService.schedulePartRequirement( lTask, lTO,
            NO_AUTO_RESERVATION, PERFORM_VALIDATION, NOT_MANUALLY_REQUESTED );

      assertNull( lTaskPart );
   }


   @Test
   public void test_historyNoteOnAddingPartRequirement() throws Exception {
      // Set up an applicability code that is outside an applicability range.
      String lApplRange = "100-200";
      String lApplCodeInRange = "150";

      // The task needs a main inventory with an applicability code that is not
      // within the applicability range of the part group.
      InventoryKey lTaskInv =
            new InventoryBuilder().withApplicabilityCode( lApplCodeInRange ).build();
      TaskKey lTask = new TaskBuilder().onInventory( lTaskInv ).build();

      // The TO needs the following:
      // - a part group that is not for a kit and has a non-applicable range
      // - warning is not confirmed
      PartGroupKey lPartGroup = new PartGroupDomainBuilder( PART_GROUP_CODE )
            .withInventoryClass( RefInvClassKey.SER ).withApplicabilityRange( lApplRange ).build();
      PartRequirementTO lTO = new PartRequirementTO();
      lTO.setBomPartKey( lPartGroup );
      lTO.setWarningConfirmed( false );
      lTO.setSchedQty( 1.0 );

      try {
         iScheduledPartService.schedulePartRequirement( lTask, lTO, NO_AUTO_RESERVATION,
               PERFORM_VALIDATION, MANUALLY_REQUESTED );
      } catch ( InvalidBomPartException ex ) {
         // success
      }

      EvtStage lEvtStage = new EvtStage( lTask.getEventKey() );
      lEvtStage.assertStageNote(
            i18n.get( "core.msg.A_PART_REQUIREMENT_FOR_PART_GROUP_WAS_ADDED", PART_GROUP_CODE ) );
   }


   /**
    * Test case for {@linkplain ScheduledPartService#schedulePartRequirement}.<br>
    * <br>
    * Verify that when the part group of the part requirement is applicable with the main inventory
    * of a task BUT is not applicable to that main inventory's highest inventory, that the task is
    * generated but the install part request is not created.
    *
    * @throws Exception
    */
   @Test
   public void test_schedulePartRequirement_whenPartGroupIsNotApplicableToHighestInv()
         throws Exception {

      // Set up an applicability code that is outside an applicability range.
      String lApplRange = "100-200";
      String lApplCodeInsideOfRange = "150";
      String lApplCodeOutsideOfRange = "999";

      // The task needs a main inventory with an applicability code that IS within the applicability
      // range of the part group but whose highest inventory is NOT within the applicability range.
      InventoryKey lHighestInv =
            new InventoryBuilder().withApplicabilityCode( lApplCodeOutsideOfRange ).build();
      InventoryKey lTaskInv = new InventoryBuilder().withHighestInventory( lHighestInv )
            .withApplicabilityCode( lApplCodeInsideOfRange ).build();

      TaskKey lTask = new TaskBuilder().onInventory( lTaskInv ).build();

      // The TO needs the following:
      // - a part group that is not for a kit and has a non-applicable range
      // - warning is not confirmed
      PartGroupKey lPartGroup = new PartGroupDomainBuilder( PART_GROUP_CODE )
            .withInventoryClass( RefInvClassKey.SER ).withApplicabilityRange( lApplRange ).build();

      PartRequirementTO lTO = new PartRequirementTO();
      lTO.setBomPartKey( lPartGroup );
      lTO.setWarningConfirmed( false );
      lTO.setSchedQty( 1.0d );
      lTO.setIsInstall( true );
      lTO.setIsRemove( false );
      lTO.setRequestAction( RefReqActionKey.REQ );

      GlobalParameters.getInstance( "LOGIC" ).setBoolean( "SUPPRESS_DUPLICATE_TASKS", false );
      TaskPartKey lTaskPart = new ScheduledPartService( null, iQao ).schedulePartRequirement( lTask,
            lTO, NO_AUTO_RESERVATION, PERFORM_VALIDATION, NOT_MANUALLY_REQUESTED );

      // Verify the part requirement not created.
      assertFalse( lTaskPart.isValid() );
   }


   /**
    * There is no part requirement on open tasks against that inventory or its sub-inventory
    *
    * @throws MxException
    *            if an error occurs
    */
   @Test
   public void testNoPartRequirementOnInventoryOrSubInventory() throws MxException {
      final DataSetBuilder lTaskPartMappingDs =
            new DataSetBuilder( "bom_item_position_key", "sched_part_key" );

      iContext.checking( new Expectations() {

         {

            // Return task parts
            one( iQao ).executeQuery(
                  with( query(
                        "com.mxi.mx.core.query.stask.GetTaskPartsOnInventoryOrSubInventory" ) ),
                  with( keyArgument( INVENTORYKEY, "aInvNoDbId", "aInvNoId" ) ) );
            will( returnValue( lTaskPartMappingDs.getDs() ) );
         }
      } );

      Map<ConfigSlotPositionKey, List<TaskPartKey>> lTaskPartMap =
            iScheduledPartService.getTaskPartsOnInventoryOrSubInventory( INVENTORYKEY );

      assertEquals( 0, lTaskPartMap.size() );

      iContext.assertIsSatisfied();
   }


   /**
    * TEST CASE 2: There is one ACTV part requirement on open tasks against that inventory or its
    * sub-inventory
    *
    * @throws MxException
    *            if an error occurs
    */
   @Test
   public void testPartRequirementOnInventoryOrSubInventory() throws MxException {

      final DataSetBuilder lTaskPartMappingDs =
            new DataSetBuilder( "bom_item_position_key", "sched_part_key" );

      lTaskPartMappingDs.addRow( new ConfigSlotPositionKey( "5000000:A320:2696:1" ).toString(),
            new TaskPartKey( "4650:1:1" ).toString() );

      iContext.checking( new Expectations() {

         {

            // Return task parts
            one( iQao ).executeQuery(
                  with( query(
                        "com.mxi.mx.core.query.stask.GetTaskPartsOnInventoryOrSubInventory" ) ),
                  with( keyArgument( INVENTORYKEY, "aInvNoDbId", "aInvNoId" ) ) );
            will( returnValue( lTaskPartMappingDs.getDs() ) );
         }
      } );

      Map<ConfigSlotPositionKey, List<TaskPartKey>> lTaskPartMap =
            iScheduledPartService.getTaskPartsOnInventoryOrSubInventory( INVENTORYKEY );

      // there is only one config slot position
      assertEquals( 1, lTaskPartMap.size() );
      for ( List<TaskPartKey> lTaskPartKeys : lTaskPartMap.values() ) {

         // there are two tasks against the position's inventory
         assertEquals( 1, lTaskPartKeys.size() );
      }

      iContext.assertIsSatisfied();
   }


   /**
    * There is two ACTV part requirements with different positions on open tasks against that
    * inventory or its sub-inventory
    *
    * @throws MxException
    *            if an error occurs
    */
   @Test
   public void testPartRequirementsWithDifferentPositionsOnInventoryOrSubInventory()
         throws MxException {
      final DataSetBuilder lTaskPartMappingDs =
            new DataSetBuilder( "bom_item_position_key", "sched_part_key" );

      lTaskPartMappingDs.addRow( new ConfigSlotPositionKey( "5000000:A320:2696:1" ).toString(),
            new TaskPartKey( "4650:1:1" ).toString() );
      lTaskPartMappingDs.addRow( new ConfigSlotPositionKey( "5000000:A320:2697:1" ).toString(),
            new TaskPartKey( "4650:2:1" ).toString() );

      iContext.checking( new Expectations() {

         {

            // Return task parts
            one( iQao ).executeQuery(
                  with( query(
                        "com.mxi.mx.core.query.stask.GetTaskPartsOnInventoryOrSubInventory" ) ),
                  with( keyArgument( INVENTORYKEY, "aInvNoDbId", "aInvNoId" ) ) );
            will( returnValue( lTaskPartMappingDs.getDs() ) );
         }
      } );

      Map<ConfigSlotPositionKey, List<TaskPartKey>> lTaskPartMap =
            iScheduledPartService.getTaskPartsOnInventoryOrSubInventory( INVENTORYKEY );

      // there is only one config slot position
      assertEquals( 2, lTaskPartMap.size() );
      for ( List<TaskPartKey> lTaskPartKeys : lTaskPartMap.values() ) {

         // there are two tasks against the position's inventory
         assertEquals( 1, lTaskPartKeys.size() );
      }

      iContext.assertIsSatisfied();
   }


   /**
    * There are ACTV part requirements with same config slot position on open tasks against that
    * inventory or its sub-inventory
    *
    * @throws MxException
    *            if an error occurs
    */
   @Test
   public void testPartRequirementsWithSamePositionOnInventoryOrSubInventory() throws MxException {
      final DataSetBuilder lTaskPartMappingDs =
            new DataSetBuilder( "bom_item_position_key", "sched_part_key" );

      lTaskPartMappingDs.addRow( new ConfigSlotPositionKey( "5000000:A320:2696:1" ).toString(),
            new TaskPartKey( "4650:5:1" ).toString() );
      lTaskPartMappingDs.addRow( new ConfigSlotPositionKey( "5000000:A320:2696:1" ).toString(),
            new TaskPartKey( "4650:7:1" ).toString() );
      iContext.checking( new Expectations() {

         {

            // Return task parts
            one( iQao ).executeQuery(
                  with( query(
                        "com.mxi.mx.core.query.stask.GetTaskPartsOnInventoryOrSubInventory" ) ),
                  with( keyArgument( INVENTORYKEY, "aInvNoDbId", "aInvNoId" ) ) );
            will( returnValue( lTaskPartMappingDs.getDs() ) );
         }
      } );

      Map<ConfigSlotPositionKey, List<TaskPartKey>> lTaskPartMap =
            iScheduledPartService.getTaskPartsOnInventoryOrSubInventory( INVENTORYKEY );

      // there is only one config slot position
      assertEquals( 1, lTaskPartMap.size() );
      for ( List<TaskPartKey> lTaskPartKeys : lTaskPartMap.values() ) {

         // there are two tasks against the position's inventory
         assertEquals( 2, lTaskPartKeys.size() );
      }

      iContext.assertIsSatisfied();
   }


   /**
    * Tests that trying to cancel and already canceled part requirement throws an
    * {@link InvalidStatusException}.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testThatCancelingAlreadyCanceledPartRequirementThrowsInvalidStatusException()
         throws Exception {
      TaskKey lTask = new TaskBuilder().build();

      TaskPartKey lPartRequirement = new PartRequirementDomainBuilder( lTask ).build();

      try {
         ScheduledPartService.cancelPart( lPartRequirement );
      } catch ( InvalidStatusException e ) {
         assertEquals( "Message Key", "core.err.30214", e.getMessageKey() );
         assertEquals( "Event Status", "CFACTV", e.getMessageArgument( 0 ) );
         assertEquals( "Event Type", "core.lbl.EVENT", e.getMessageArgument( 1 ) );
      }
   }
}
