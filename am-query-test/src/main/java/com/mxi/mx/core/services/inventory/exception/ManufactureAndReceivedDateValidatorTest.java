
package com.mxi.mx.core.services.inventory.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.validation.Messages;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefTaskSchedFromKey;
import com.mxi.mx.core.key.TaskPartMapKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.task.TaskPartMapTable;
import com.mxi.mx.core.table.task.TaskTaskTable;


/**
 * This class tests the ManufactureAndReceivedDateValidator class.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class ManufactureAndReceivedDateValidatorTest {

   private static final InventoryKey INVENTORY = new InventoryKey( 4650, 1002 );

   private static final PartNoKey INVENTORY_PART = new PartNoKey( 4650, 3002 );

   private static final InventoryKey AIRCRAFT_INVENTORY = new InventoryKey( 4650, 1005 );
   private static final InventoryKey AIRCRAFT_SUBSYS1_INVENTORY = new InventoryKey( 4650, 1006 );

   private static final InventoryKey ASSEMBLY_INVENTORY = new InventoryKey( 4650, 1008 );
   private static final InventoryKey ASSEMBLY_SUBSYS2_INVENTORY = new InventoryKey( 4650, 1010 );

   private static final TaskTaskKey TASK_TASK_1 = new TaskTaskKey( 4650, 2001 );
   private static final TaskTaskKey TASK_TASK_2 = new TaskTaskKey( 4650, 2002 );

   private static final AssemblyKey ACFT_ASSEMBLY = new AssemblyKey( 4650, "ACFT" );

   private static final ConfigSlotKey ACFT_ROOT_CONFIG_SLOT = new ConfigSlotKey( ACFT_ASSEMBLY, 0 );

   private static final ConfigSlotKey ACFT_SYS_CONFIG_SLOT = new ConfigSlotKey( ACFT_ASSEMBLY, 1 );

   private static final ConfigSlotKey ACFT_REPL_CONFIG_SLOT = new ConfigSlotKey( ACFT_ASSEMBLY, 2 );

   private static final AssemblyKey ASSY_ASSEMBLY = new AssemblyKey( 4650, "ASSY" );

   private static final ConfigSlotKey ASSY_ROOT_CONFIG_SLOT = new ConfigSlotKey( ASSY_ASSEMBLY, 0 );

   private static final ConfigSlotKey ASSY_SYS_CONFIG_SLOT = new ConfigSlotKey( ASSY_ASSEMBLY, 2 );

   private static final ConfigSlotKey ASSY_REPL_CONFIG_SLOT = new ConfigSlotKey( ASSY_ASSEMBLY, 3 );

   private static final ConfigSlotKey INV_CONFIG_SLOT = new ConfigSlotKey( ACFT_ASSEMBLY, 99 );

   private static final PartNoKey AIRCRAFT_PART = new PartNoKey( 4650, 3005 );
   private static final PartNoKey ASSEMBLY_PART = new PartNoKey( 4650, 3008 );

   private final Date iJan_1_2012;

   private final Date iJan_2_2012;

   private Messages iMessages;

   private ManufactureAndReceivedDateValidator iValidator;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Creates a new {@linkplain ManufactureAndReceivedDateValidatorTest} object.
    */
   public ManufactureAndReceivedDateValidatorTest() {
      Calendar lCal = Calendar.getInstance();
      lCal.set( Calendar.HOUR_OF_DAY, 0 );
      lCal.set( Calendar.MINUTE, 0 );
      lCal.set( Calendar.SECOND, 0 );
      lCal.set( Calendar.MILLISECOND, 0 );

      lCal.set( 2012, 0, 1 );
      iJan_1_2012 = lCal.getTime();

      lCal.set( 2012, 0, 2 );
      iJan_2_2012 = lCal.getTime();
   }


   @Before
   public void setUp() throws Exception {
      iValidator = new ManufactureAndReceivedDateValidator();

      iMessages = new Messages( Messages.SKIP_WARNINGS );
   }


   /**
    * Test that when both dates are missing it is considered to be invalid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testMissingBothDatesForAircraftIsInvalid() throws Exception {
      TaskTaskTable lTaskTaskFromManufact = TaskTaskTable.create( TASK_TASK_1 );
      lTaskTaskFromManufact.setBomItem( ACFT_ROOT_CONFIG_SLOT );
      lTaskTaskFromManufact.setScheduleFrom( RefTaskSchedFromKey.MANUFACT_DT );
      lTaskTaskFromManufact.setTaskDefStatus( RefTaskDefinitionStatusKey.ACTV );
      lTaskTaskFromManufact.insert();

      TaskTaskTable lTaskTaskFromReceived = TaskTaskTable.create( TASK_TASK_2 );
      lTaskTaskFromReceived.setBomItem( ACFT_ROOT_CONFIG_SLOT );
      lTaskTaskFromReceived.setScheduleFrom( RefTaskSchedFromKey.RECEIVED_DT );
      lTaskTaskFromReceived.setTaskDefStatus( RefTaskDefinitionStatusKey.ACTV );
      lTaskTaskFromReceived.insert();

      iValidator.checkThat( iMessages, AIRCRAFT_INVENTORY, null, null );

      assertTrue( "Has Errors", iMessages.hasErrors() );

      assertEquals( "core.err.31498", iMessages.getErrors().get( 0 ).getMsgFrameKey() );
   }


   /**
    * Test that when both dates are missing it is considered to be invalid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testMissingBothDatesForAircraftPartBasedIsInvalid() throws Exception {
      TaskTaskTable lTaskTaskFromManufact = TaskTaskTable.create( TASK_TASK_1 );
      lTaskTaskFromManufact.setScheduleFrom( RefTaskSchedFromKey.MANUFACT_DT );
      lTaskTaskFromManufact.setTaskDefStatus( RefTaskDefinitionStatusKey.ACTV );
      lTaskTaskFromManufact.insert();

      TaskPartMapTable lTaskPartMapFromManufact =
            TaskPartMapTable.create( new TaskPartMapKey( TASK_TASK_1, AIRCRAFT_PART ) );
      lTaskPartMapFromManufact.insert();

      TaskTaskTable lTaskTaskFromReceived = TaskTaskTable.create( TASK_TASK_2 );
      lTaskTaskFromReceived.setScheduleFrom( RefTaskSchedFromKey.RECEIVED_DT );
      lTaskTaskFromReceived.setTaskDefStatus( RefTaskDefinitionStatusKey.ACTV );
      lTaskTaskFromReceived.insert();

      TaskPartMapTable lTaskPartMapFromReceived =
            TaskPartMapTable.create( new TaskPartMapKey( TASK_TASK_2, AIRCRAFT_PART ) );
      lTaskPartMapFromReceived.insert();

      iValidator.checkThat( iMessages, AIRCRAFT_INVENTORY, null, null );

      assertTrue( "Has Errors", iMessages.hasErrors() );

      assertEquals( "core.err.31498", iMessages.getErrors().get( 0 ).getMsgFrameKey() );
   }


   /**
    * Test that when both dates are missing it is considered to be invalid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testMissingBothDatesForAircraftSystemIsInvalid() throws Exception {
      TaskTaskTable lTaskTaskFromManufact = TaskTaskTable.create( TASK_TASK_1 );
      lTaskTaskFromManufact.setBomItem( ACFT_SYS_CONFIG_SLOT );
      lTaskTaskFromManufact.setScheduleFrom( RefTaskSchedFromKey.MANUFACT_DT );
      lTaskTaskFromManufact.setTaskDefStatus( RefTaskDefinitionStatusKey.ACTV );
      lTaskTaskFromManufact.insert();

      TaskTaskTable lTaskTaskFromReceived = TaskTaskTable.create( TASK_TASK_2 );
      lTaskTaskFromReceived.setBomItem( ACFT_SYS_CONFIG_SLOT );
      lTaskTaskFromReceived.setScheduleFrom( RefTaskSchedFromKey.RECEIVED_DT );
      lTaskTaskFromReceived.setTaskDefStatus( RefTaskDefinitionStatusKey.ACTV );
      lTaskTaskFromReceived.insert();

      iValidator.checkThat( iMessages, AIRCRAFT_SUBSYS1_INVENTORY, null, null );

      assertTrue( "Has Errors", iMessages.hasErrors() );

      assertEquals( "core.err.31498", iMessages.getErrors().get( 0 ).getMsgFrameKey() );
   }


   /**
    * Test that when both dates are missing it is considered to be invalid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testMissingBothDatesForAssemblyIsInvalid() throws Exception {
      TaskTaskTable lTaskTaskFromManufact = TaskTaskTable.create( TASK_TASK_1 );
      lTaskTaskFromManufact.setBomItem( ASSY_ROOT_CONFIG_SLOT );
      lTaskTaskFromManufact.setScheduleFrom( RefTaskSchedFromKey.MANUFACT_DT );
      lTaskTaskFromManufact.setTaskDefStatus( RefTaskDefinitionStatusKey.ACTV );
      lTaskTaskFromManufact.insert();

      TaskTaskTable lTaskTaskFromReceived = TaskTaskTable.create( TASK_TASK_2 );
      lTaskTaskFromReceived.setBomItem( ASSY_ROOT_CONFIG_SLOT );
      lTaskTaskFromReceived.setScheduleFrom( RefTaskSchedFromKey.RECEIVED_DT );
      lTaskTaskFromReceived.setTaskDefStatus( RefTaskDefinitionStatusKey.ACTV );
      lTaskTaskFromReceived.insert();

      iValidator.checkThat( iMessages, ASSEMBLY_INVENTORY, null, null );

      assertTrue( "Has Errors", iMessages.hasErrors() );

      assertEquals( "core.err.31498", iMessages.getErrors().get( 0 ).getMsgFrameKey() );
   }


   /**
    * Test that when both dates are missing it is considered to be invalid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testMissingBothDatesForAssemblyPartBasedIsInvalid() throws Exception {
      TaskTaskTable lTaskTaskFromManufact = TaskTaskTable.create( TASK_TASK_1 );
      lTaskTaskFromManufact.setScheduleFrom( RefTaskSchedFromKey.MANUFACT_DT );
      lTaskTaskFromManufact.setTaskDefStatus( RefTaskDefinitionStatusKey.ACTV );
      lTaskTaskFromManufact.insert();

      TaskPartMapTable lTaskPartMapFromManufact =
            TaskPartMapTable.create( new TaskPartMapKey( TASK_TASK_1, ASSEMBLY_PART ) );
      lTaskPartMapFromManufact.insert();

      TaskTaskTable lTaskTaskFromReceived = TaskTaskTable.create( TASK_TASK_2 );
      lTaskTaskFromReceived.setScheduleFrom( RefTaskSchedFromKey.RECEIVED_DT );
      lTaskTaskFromReceived.setTaskDefStatus( RefTaskDefinitionStatusKey.ACTV );
      lTaskTaskFromReceived.insert();

      TaskPartMapTable lTaskPartMapFromReceived =
            TaskPartMapTable.create( new TaskPartMapKey( TASK_TASK_2, ASSEMBLY_PART ) );
      lTaskPartMapFromReceived.insert();

      iValidator.checkThat( iMessages, ASSEMBLY_INVENTORY, null, null );

      assertTrue( "Has Errors", iMessages.hasErrors() );

      assertEquals( "core.err.31498", iMessages.getErrors().get( 0 ).getMsgFrameKey() );
   }


   /**
    * Test that when both dates are missing it is considered to be invalid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testMissingBothDatesForAssemblySystemIsInvalid() throws Exception {
      TaskTaskTable lTaskTaskFromManufact = TaskTaskTable.create( TASK_TASK_1 );
      lTaskTaskFromManufact.setBomItem( ASSY_SYS_CONFIG_SLOT );
      lTaskTaskFromManufact.setScheduleFrom( RefTaskSchedFromKey.MANUFACT_DT );
      lTaskTaskFromManufact.setTaskDefStatus( RefTaskDefinitionStatusKey.ACTV );
      lTaskTaskFromManufact.insert();

      TaskTaskTable lTaskTaskFromReceived = TaskTaskTable.create( TASK_TASK_2 );
      lTaskTaskFromReceived.setBomItem( ASSY_SYS_CONFIG_SLOT );
      lTaskTaskFromReceived.setScheduleFrom( RefTaskSchedFromKey.RECEIVED_DT );
      lTaskTaskFromReceived.setTaskDefStatus( RefTaskDefinitionStatusKey.ACTV );
      lTaskTaskFromReceived.insert();

      iValidator.checkThat( iMessages, ASSEMBLY_SUBSYS2_INVENTORY, null, null );

      assertTrue( "Has Errors", iMessages.hasErrors() );

      assertEquals( "core.err.31498", iMessages.getErrors().get( 0 ).getMsgFrameKey() );
   }


   /**
    * Test that when both dates are missing it is considered to be invalid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testMissingBothDatesForInventoryIsInvalid() throws Exception {
      TaskTaskTable lTaskTaskFromManufact = TaskTaskTable.create( TASK_TASK_1 );
      lTaskTaskFromManufact.setBomItem( INV_CONFIG_SLOT );
      lTaskTaskFromManufact.setScheduleFrom( RefTaskSchedFromKey.MANUFACT_DT );
      lTaskTaskFromManufact.setTaskDefStatus( RefTaskDefinitionStatusKey.ACTV );
      lTaskTaskFromManufact.insert();

      TaskTaskTable lTaskTaskFromReceived = TaskTaskTable.create( TASK_TASK_2 );
      lTaskTaskFromReceived.setBomItem( INV_CONFIG_SLOT );
      lTaskTaskFromReceived.setScheduleFrom( RefTaskSchedFromKey.RECEIVED_DT );
      lTaskTaskFromReceived.setTaskDefStatus( RefTaskDefinitionStatusKey.ACTV );
      lTaskTaskFromReceived.insert();

      iValidator.checkThat( iMessages, INVENTORY, null, null );

      assertTrue( "Has Errors", iMessages.hasErrors() );

      assertEquals( "core.err.31498", iMessages.getErrors().get( 0 ).getMsgFrameKey() );
   }


   /**
    * Test that when both dates are missing it is considered to be invalid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testMissingBothDatesForInventoryPartBasedIsInvalid() throws Exception {
      TaskTaskTable lTaskTaskFromManufact = TaskTaskTable.create( TASK_TASK_1 );
      lTaskTaskFromManufact.setScheduleFrom( RefTaskSchedFromKey.MANUFACT_DT );
      lTaskTaskFromManufact.setTaskDefStatus( RefTaskDefinitionStatusKey.ACTV );
      lTaskTaskFromManufact.insert();

      TaskPartMapTable lTaskPartMapFromManufact =
            TaskPartMapTable.create( new TaskPartMapKey( TASK_TASK_1, INVENTORY_PART ) );
      lTaskPartMapFromManufact.insert();

      TaskTaskTable lTaskTaskFromReceived = TaskTaskTable.create( TASK_TASK_2 );
      lTaskTaskFromReceived.setScheduleFrom( RefTaskSchedFromKey.RECEIVED_DT );
      lTaskTaskFromReceived.setTaskDefStatus( RefTaskDefinitionStatusKey.ACTV );
      lTaskTaskFromReceived.insert();

      TaskPartMapTable lTaskPartMapFromReceived =
            TaskPartMapTable.create( new TaskPartMapKey( TASK_TASK_2, INVENTORY_PART ) );
      lTaskPartMapFromReceived.insert();

      iValidator.checkThat( iMessages, INVENTORY, null, null );

      assertTrue( "Has Errors", iMessages.hasErrors() );

      assertEquals( "core.err.31498", iMessages.getErrors().get( 0 ).getMsgFrameKey() );
   }


   /**
    * Tests that when we are missing the manufacture date it is considered to be invalid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testMissingManufactureDateForAircraftIsInvalid() throws Exception {
      TaskTaskTable lTaskTaskFromManufact = TaskTaskTable.create( TASK_TASK_1 );
      lTaskTaskFromManufact.setBomItem( ACFT_ROOT_CONFIG_SLOT );
      lTaskTaskFromManufact.setScheduleFrom( RefTaskSchedFromKey.MANUFACT_DT );
      lTaskTaskFromManufact.setTaskDefStatus( RefTaskDefinitionStatusKey.ACTV );
      lTaskTaskFromManufact.insert();

      iValidator.checkThat( iMessages, AIRCRAFT_INVENTORY, null, iJan_2_2012 );

      assertTrue( "Has Errors", iMessages.hasErrors() );

      assertEquals( "core.err.31497", iMessages.getErrors().get( 0 ).getMsgFrameKey() );
      assertEquals( i18n.get( "core.lbl.MANUFACTURED_DATE" ),
            iMessages.getErrors().get( 0 ).getParameter( 0 ).getValue() );
   }


   /**
    * Tests that when the manufacture date is missing but not required, it is considered to be
    * valid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testMissingManufactureDateForAircraftNotRequiredIsValid() throws Exception {

      // create a task definition from effective date
      TaskTaskTable lTaskTaskFromManufact = TaskTaskTable.create( TASK_TASK_1 );
      lTaskTaskFromManufact.setBomItem( ACFT_ROOT_CONFIG_SLOT );
      lTaskTaskFromManufact.setScheduleFrom( RefTaskSchedFromKey.EFFECTIVE_DT );
      lTaskTaskFromManufact.insert();

      iValidator.checkThat( iMessages, AIRCRAFT_INVENTORY, null, iJan_2_2012 );

      assertFalse( "Has Errors", iMessages.hasErrors() );
   }


   /**
    * Tests that when the manufacture date is missing but not required, it is considered to be
    * valid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testMissingManufactureDateForAircraftSystemNotRequiredIsValid() throws Exception {

      // create a task definition from effective date
      TaskTaskTable lTaskTaskFromManufact = TaskTaskTable.create( TASK_TASK_1 );
      lTaskTaskFromManufact.setBomItem( ACFT_SYS_CONFIG_SLOT );
      lTaskTaskFromManufact.setScheduleFrom( RefTaskSchedFromKey.EFFECTIVE_DT );
      lTaskTaskFromManufact.insert();

      iValidator.checkThat( iMessages, AIRCRAFT_INVENTORY, null, iJan_2_2012 );

      assertFalse( "Has Errors", iMessages.hasErrors() );
   }


   /**
    * Tests that when the manufacture date is missing but not required, it is considered to be
    * valid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testMissingManufactureDateForAircraftSystemReplIsValid() throws Exception {

      // create a task definition from effective date
      TaskTaskTable lTaskTaskFromManufact = TaskTaskTable.create( TASK_TASK_1 );
      lTaskTaskFromManufact.setBomItem( ACFT_SYS_CONFIG_SLOT );
      lTaskTaskFromManufact.setReplBomItem( ACFT_REPL_CONFIG_SLOT );
      lTaskTaskFromManufact.setScheduleFrom( RefTaskSchedFromKey.MANUFACT_DT );
      lTaskTaskFromManufact.insert();

      iValidator.checkThat( iMessages, AIRCRAFT_INVENTORY, null, iJan_2_2012 );

      assertFalse( "Has Errors", iMessages.hasErrors() );
   }


   /**
    * Tests that when we are missing the manufacture date it is considered to be invalid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testMissingManufactureDateForAssemblyIsInvalid() throws Exception {
      TaskTaskTable lTaskTaskFromManufact = TaskTaskTable.create( TASK_TASK_1 );
      lTaskTaskFromManufact.setBomItem( ASSY_ROOT_CONFIG_SLOT );
      lTaskTaskFromManufact.setScheduleFrom( RefTaskSchedFromKey.MANUFACT_DT );
      lTaskTaskFromManufact.setTaskDefStatus( RefTaskDefinitionStatusKey.ACTV );
      lTaskTaskFromManufact.insert();

      iValidator.checkThat( iMessages, ASSEMBLY_INVENTORY, null, iJan_2_2012 );

      assertTrue( "Has Errors", iMessages.hasErrors() );

      assertEquals( "core.err.31497", iMessages.getErrors().get( 0 ).getMsgFrameKey() );
      assertEquals( i18n.get( "core.lbl.MANUFACTURED_DATE" ),
            iMessages.getErrors().get( 0 ).getParameter( 0 ).getValue() );
   }


   /**
    * Tests that when the manufacture date is missing but not required, it is considered to be
    * valid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testMissingManufactureDateForAssemblyNotRequiredIsValid() throws Exception {

      // create a task definition from effective date
      TaskTaskTable lTaskTaskFromManufact = TaskTaskTable.create( TASK_TASK_1 );
      lTaskTaskFromManufact.setBomItem( ASSY_ROOT_CONFIG_SLOT );
      lTaskTaskFromManufact.setScheduleFrom( RefTaskSchedFromKey.EFFECTIVE_DT );
      lTaskTaskFromManufact.insert();

      iValidator.checkThat( iMessages, ASSEMBLY_INVENTORY, null, iJan_2_2012 );

      assertFalse( "Has Errors", iMessages.hasErrors() );
   }


   /**
    * Tests that when the manufacture date is missing but not required, it is considered to be
    * valid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testMissingManufactureDateForAssemblySystemNotRequiredIsValid() throws Exception {

      // create a task definition from effective date
      TaskTaskTable lTaskTaskFromManufact = TaskTaskTable.create( TASK_TASK_1 );
      lTaskTaskFromManufact.setBomItem( ASSY_SYS_CONFIG_SLOT );
      lTaskTaskFromManufact.setScheduleFrom( RefTaskSchedFromKey.EFFECTIVE_DT );
      lTaskTaskFromManufact.insert();

      iValidator.checkThat( iMessages, ASSEMBLY_INVENTORY, null, iJan_2_2012 );

      assertFalse( "Has Errors", iMessages.hasErrors() );
   }


   /**
    * Tests that when the manufacture date is missing but not required, it is considered to be
    * valid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testMissingManufactureDateForAssemblySystemReplIsValid() throws Exception {

      // create a task definition from effective date
      TaskTaskTable lTaskTaskFromManufact = TaskTaskTable.create( TASK_TASK_1 );
      lTaskTaskFromManufact.setBomItem( ASSY_SYS_CONFIG_SLOT );
      lTaskTaskFromManufact.setReplBomItem( ASSY_REPL_CONFIG_SLOT );
      lTaskTaskFromManufact.setScheduleFrom( RefTaskSchedFromKey.MANUFACT_DT );
      lTaskTaskFromManufact.insert();

      iValidator.checkThat( iMessages, ASSEMBLY_INVENTORY, null, iJan_2_2012 );

      assertFalse( "Has Errors", iMessages.hasErrors() );
   }


   /**
    * Tests that when we are missing the manufacture date it is considered to be invalid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testMissingManufactureDateForInventoryIsInvalid() throws Exception {
      TaskTaskTable lTaskTaskFromManufact = TaskTaskTable.create( TASK_TASK_1 );
      lTaskTaskFromManufact.setScheduleFrom( RefTaskSchedFromKey.MANUFACT_DT );
      lTaskTaskFromManufact.setTaskDefStatus( RefTaskDefinitionStatusKey.ACTV );
      lTaskTaskFromManufact.insert();

      TaskPartMapTable lTaskPartMapFromManufact =
            TaskPartMapTable.create( new TaskPartMapKey( TASK_TASK_1, INVENTORY_PART ) );
      lTaskPartMapFromManufact.insert();

      iValidator.checkThat( iMessages, INVENTORY, null, iJan_2_2012 );

      assertTrue( "Has Errors", iMessages.hasErrors() );

      assertEquals( "core.err.31497", iMessages.getErrors().get( 0 ).getMsgFrameKey() );
      assertEquals( i18n.get( "core.lbl.MANUFACTURED_DATE" ),
            iMessages.getErrors().get( 0 ).getParameter( 0 ).getValue() );
   }


   /**
    * Tests that when the manufacture date is missing but not required, it is considered to be
    * valid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testMissingManufactureDateForInventoryNotRequiredIsValid() throws Exception {

      // create a task definition from effective date
      TaskTaskTable lTaskTaskFromManufact = TaskTaskTable.create( TASK_TASK_1 );
      lTaskTaskFromManufact.setBomItem( INV_CONFIG_SLOT );
      lTaskTaskFromManufact.setScheduleFrom( RefTaskSchedFromKey.EFFECTIVE_DT );
      lTaskTaskFromManufact.insert();

      iValidator.checkThat( iMessages, INVENTORY, null, iJan_2_2012 );

      assertFalse( "Has Errors", iMessages.hasErrors() );
   }


   /**
    * Tests that when we are missing the received date it is considered to be invalid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testMissingReceivedDateForAircraftIsInvalid() throws Exception {
      TaskTaskTable lTaskTaskFromReceived = TaskTaskTable.create( TASK_TASK_1 );
      lTaskTaskFromReceived.setBomItem( ACFT_ROOT_CONFIG_SLOT );
      lTaskTaskFromReceived.setScheduleFrom( RefTaskSchedFromKey.RECEIVED_DT );
      lTaskTaskFromReceived.setTaskDefStatus( RefTaskDefinitionStatusKey.ACTV );
      lTaskTaskFromReceived.insert();

      iValidator.checkThat( iMessages, AIRCRAFT_INVENTORY, iJan_1_2012, null );

      assertTrue( "Has Errors", iMessages.hasErrors() );

      assertEquals( "core.err.31497", iMessages.getErrors().get( 0 ).getMsgFrameKey() );
      assertEquals( i18n.get( "core.lbl.RECEIVED_DATE" ),
            iMessages.getErrors().get( 0 ).getParameter( 0 ).getValue() );
   }


   /**
    * Tests that when the received date is missing but not required, it is considered to be valid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testMissingReceivedDateForAircraftNotRequiredIsValid() throws Exception {

      // create a task definition from effective date
      TaskTaskTable lTaskTaskFromManufact = TaskTaskTable.create( TASK_TASK_1 );
      lTaskTaskFromManufact.setBomItem( ACFT_ROOT_CONFIG_SLOT );
      lTaskTaskFromManufact.setScheduleFrom( RefTaskSchedFromKey.EFFECTIVE_DT );
      lTaskTaskFromManufact.insert();

      iValidator.checkThat( iMessages, AIRCRAFT_INVENTORY, iJan_1_2012, null );

      assertFalse( "Has Errors", iMessages.hasErrors() );
   }


   /**
    * Tests that when we are missing the received date it is considered to be invalid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testMissingReceivedDateForAssemblyIsInvalid() throws Exception {
      TaskTaskTable lTaskTaskFromReceived = TaskTaskTable.create( TASK_TASK_1 );
      lTaskTaskFromReceived.setBomItem( ASSY_ROOT_CONFIG_SLOT );
      lTaskTaskFromReceived.setScheduleFrom( RefTaskSchedFromKey.RECEIVED_DT );
      lTaskTaskFromReceived.setTaskDefStatus( RefTaskDefinitionStatusKey.ACTV );
      lTaskTaskFromReceived.insert();

      iValidator.checkThat( iMessages, ASSEMBLY_INVENTORY, iJan_1_2012, null );

      assertTrue( "Has Errors", iMessages.hasErrors() );

      assertEquals( "core.err.31497", iMessages.getErrors().get( 0 ).getMsgFrameKey() );
      assertEquals( i18n.get( "core.lbl.RECEIVED_DATE" ),
            iMessages.getErrors().get( 0 ).getParameter( 0 ).getValue() );
   }


   /**
    * Tests that when the received date is missing but not required, it is considered to be valid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testMissingReceivedDateForAssemblyNotRequiredIsValid() throws Exception {

      // create a task definition from effective date
      TaskTaskTable lTaskTaskFromManufact = TaskTaskTable.create( TASK_TASK_1 );
      lTaskTaskFromManufact.setBomItem( ASSY_ROOT_CONFIG_SLOT );
      lTaskTaskFromManufact.setScheduleFrom( RefTaskSchedFromKey.EFFECTIVE_DT );
      lTaskTaskFromManufact.insert();

      iValidator.checkThat( iMessages, ASSEMBLY_INVENTORY, iJan_1_2012, null );

      assertFalse( "Has Errors", iMessages.hasErrors() );
   }


   /**
    * Tests that when we are missing the received date it is considered to be invalid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testMissingReceivedDateForInventoryIsInvalid() throws Exception {
      TaskTaskTable lTaskTaskFromReceived = TaskTaskTable.create( TASK_TASK_1 );
      lTaskTaskFromReceived.setScheduleFrom( RefTaskSchedFromKey.RECEIVED_DT );
      lTaskTaskFromReceived.setTaskDefStatus( RefTaskDefinitionStatusKey.ACTV );
      lTaskTaskFromReceived.insert();

      TaskPartMapTable lTaskPartMapFromReceived =
            TaskPartMapTable.create( new TaskPartMapKey( TASK_TASK_1, INVENTORY_PART ) );
      lTaskPartMapFromReceived.insert();

      iValidator.checkThat( iMessages, INVENTORY, iJan_1_2012, null );

      assertTrue( "Has Errors", iMessages.hasErrors() );

      assertEquals( "core.err.31497", iMessages.getErrors().get( 0 ).getMsgFrameKey() );
      assertEquals( i18n.get( "core.lbl.RECEIVED_DATE" ),
            iMessages.getErrors().get( 0 ).getParameter( 0 ).getValue() );
   }


   /**
    * Tests that when the received date is missing but not required, it is considered to be valid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testMissingReceivedDateForInventoryNotRequiredIsValid() throws Exception {

      // create a task definition from effective date
      TaskTaskTable lTaskTaskFromManufact = TaskTaskTable.create( TASK_TASK_1 );
      lTaskTaskFromManufact.setBomItem( INV_CONFIG_SLOT );
      lTaskTaskFromManufact.setScheduleFrom( RefTaskSchedFromKey.EFFECTIVE_DT );
      lTaskTaskFromManufact.insert();

      iValidator.checkThat( iMessages, INVENTORY, iJan_1_2012, null );

      assertFalse( "Has Errors", iMessages.hasErrors() );
   }


   /**
    * Test that when both manufacture and received date are given, it is always valid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatNothingMissingIsValid() throws Exception {
      iValidator.checkThat( iMessages, INVENTORY, iJan_1_2012, iJan_2_2012 );

      assertFalse( "No Errors", iMessages.hasErrors() );
   }


   @Before
   public void loadData() throws Exception {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }
}
