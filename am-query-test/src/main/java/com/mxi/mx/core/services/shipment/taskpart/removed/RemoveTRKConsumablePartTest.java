
package com.mxi.mx.core.services.shipment.taskpart.removed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.AssemblyBuilder;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.ConfigSlotPositionBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefAbcClassKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefRemoveReasonKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.key.TaskRmvdPartKey;
import com.mxi.mx.core.services.stask.taskpart.removed.RemovedPartService;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.sched.SchedRmvdPartTable;


/**
 * Test the remove part service and check the final value of the issued_bool of the inventory under
 * removal considering the AUTO_ISSUE_INVENTORY parameter value. The inventory is TRK Consumable
 * part based
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class RemoveTRKConsumablePartTest {

   private static final int USER_ID = 1;

   private static final Date NOW = new Date();

   private static final String SERIAL_NO = "SN002";

   private HumanResourceKey iHrKey;

   private TaskPartKey iTaskPartKey;

   private TaskRmvdPartKey iTaskRmvdPartKey;

   private InventoryKey iTrkInventory;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Sets up the test case.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Before
   public void loadData() {
      GlobalParameters.getInstance( ParmTypeEnum.LOGIC ).setString( "BLANK_RO_SIGNATURE", null );

      // build HR
      iHrKey = new HumanResourceDomainBuilder().withUserId( USER_ID ).build();

      // build an aircraft
      AssemblyKey lAcftAssembly = new AssemblyBuilder( "AAC01" ).build();

      ConfigSlotKey lAcftConfigSlot = new ConfigSlotBuilder( "ACS01" )
            .withClass( RefBOMClassKey.ROOT ).withRootAssembly( lAcftAssembly ).build();

      ConfigSlotPositionKey lAcftConfigSlotPos = new ConfigSlotPositionKey( lAcftConfigSlot, 1 );

      InventoryKey lAircraft = new InventoryBuilder().withClass( RefInvClassKey.ACFT )
            .withDescription( "Aircraft Inventory" ).withConfigSlotPosition( lAcftConfigSlotPos )
            .withOriginalAssembly( lAcftAssembly ).withSerialNo( "SN001" ).build();

      // build a config slot on aircraft to attach a inventory
      ConfigSlotKey lTrkConfigSlot = new ConfigSlotBuilder( "TRKCSCD" )
            .withClass( RefBOMClassKey.TRK ).withRootAssembly( lAcftAssembly ).build();

      ConfigSlotPositionKey lTrkConfigSlotPos =
            new ConfigSlotPositionBuilder().withConfigSlot( lTrkConfigSlot ).build();

      // build a part and assign it to part group
      PartNoKey lPartNo = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withShortDescription( "Part A" )
            .withAbcClass( RefAbcClassKey.A ).withStatus( RefPartStatusKey.ACTV )
            .withTotalQuantity( new BigDecimal( 0 ) )
            .withAverageUnitPrice( new BigDecimal( 10.50 ) ).withRepairBool( false ).build();

      // build a part group
      PartGroupKey lTrkPartGroup =
            new PartGroupDomainBuilder( "PARTGC" ).withConfigSlot( lTrkConfigSlot )
                  .withInventoryClass( RefInvClassKey.TRK ).withPartNo( lPartNo ).build();

      // build a location
      LocationKey lLocation = new LocationDomainBuilder().isSupplyLocation().build();

      // create inventory based on the given part, and attach it to aircraft.
      iTrkInventory = new InventoryBuilder().withClass( RefInvClassKey.TRK )
            .withConfigSlotPosition( lTrkConfigSlotPos ).withPartGroup( lTrkPartGroup )
            .withPartNo( lPartNo ).withOriginalAssembly( lAcftAssembly )
            .withAssemblyInventory( lAircraft ).withParentInventory( lAircraft )
            .atLocation( lLocation ).withSerialNo( "SN002" ).isIssued().build();

      // build a scheduled task
      final TaskKey lTask =
            new TaskBuilder().withName( "Removal Task" ).withTaskClass( RefTaskClassKey.ADHOC )
                  .withLabour( RefLabourSkillKey.LBR, 0 ).onInventory( lAircraft )
                  .withCalendarDeadline( DateUtils.getEndOfPrevDay( NOW ) ).build();

      // build a part requirement
      iTaskPartKey =
            new PartRequirementDomainBuilder( lTask ).forPart( lPartNo ).withRemovalQuantity( 1 )
                  .withRemovalReason( RefRemoveReasonKey.IMSCHD ).forPartGroup( lTrkPartGroup )
                  .forPosition( lTrkConfigSlotPos ).withNextHighestPosition( lAcftConfigSlotPos )
                  .withRemovalInventory( iTrkInventory ).withRemovalSerialNo( "SN002" ).build();

      // generate task removed part key
      iTaskRmvdPartKey = new TaskRmvdPartKey( iTaskPartKey, 1 );

   }


   /**
    * This test the removal of an inventory based on a TRK consumable part. The revomal is performed
    * via part requirement. The inventory is attached to a parent inventory. The
    * AUTO_ISSUE_INVENTORY is set to false then, after detaching the inventory the issued_bool value
    * remain as is
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testRemoveTRKConsumableWithAutoIssueParamOffAndIssuedInventory()
         throws MxException, TriggerException {

      withAutoIssueInventoryParameterOff();

      withIssuedInventory();

      // instantiate service class and remove the inventory
      RemovedPartService.removeParts( iTaskPartKey, null, iHrKey, true, true );

      // assert removed inventory details
      SchedRmvdPartTable lSchedRmvdPartTable =
            SchedRmvdPartTable.findByPrimaryKey( iTaskRmvdPartKey );
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( lSchedRmvdPartTable.getInventory() );

      // inventory was removed
      assertNull( lInvInv.getNhInvNo() );

      // since AUTO_ISSUE_INVENTORY is turn off, the issued_bool remain as is
      assertTrue( lInvInv.isIssuedBool() );
      assertEquals( lInvInv.getSerialNoOem(), SERIAL_NO );
   }


   /**
    * This test the removal of an inventory based on a TRK consumable part. The revomal is performed
    * via part requirement. The inventory is attached to a parent inventory. The
    * AUTO_ISSUE_INVENTORY is set to false then, after detaching the inventory the issued_bool value
    * remain as is
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testRemoveTRKConsumableWithAutoIssueParamOffAndNotIssueInventory()
         throws MxException, TriggerException {

      withAutoIssueInventoryParameterOff();

      withNotIssuedInventory();

      // instantiate service class and remove the inventory
      RemovedPartService.removeParts( iTaskPartKey, null, iHrKey, true, true );

      // assert removed inventory details
      SchedRmvdPartTable lSchedRmvdPartTable =
            SchedRmvdPartTable.findByPrimaryKey( iTaskRmvdPartKey );
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( lSchedRmvdPartTable.getInventory() );

      // inventory was removed
      assertNull( lInvInv.getNhInvNo() );

      // since AUTO_ISSUE_INVENTORY is turn off, the issued_bool remain as is
      assertFalse( lInvInv.isIssuedBool() );
      assertEquals( lInvInv.getSerialNoOem(), SERIAL_NO );
   }


   /**
    * This test the removal of an inventory based on a TRK consumable part. The revomal is performed
    * via part requirement. The inventory is attached to a parent inventory. The
    * AUTO_ISSUE_INVENTORY is set to true then, after detaching the inventory the issued_bool is set
    * to true.
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testRemoveTRKConsumableWithAutoIssueParamOnAndIssueInventory()
         throws MxException, TriggerException {

      withAutoIssueInventoryParameterOn();

      withIssuedInventory();

      // instantiate service class and remove the inventory
      RemovedPartService.removeParts( iTaskPartKey, null, iHrKey, true, true );

      // assert removed inventory details
      SchedRmvdPartTable lSchedRmvdPartTable =
            SchedRmvdPartTable.findByPrimaryKey( iTaskRmvdPartKey );
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( lSchedRmvdPartTable.getInventory() );

      // inventory was removed
      assertNull( lInvInv.getNhInvNo() );

      // since AUTO_ISSUE_INVENTORY is turn on, the issued_bool is set to true
      assertTrue( lInvInv.isIssuedBool() );
      assertEquals( lInvInv.getSerialNoOem(), SERIAL_NO );
   }


   /**
    * This test the removal of an inventory based on a TRK consumable part. The revomal is performed
    * via part requirement. The inventory is attached to a parent inventory. The
    * AUTO_ISSUE_INVENTORY is set to true then, after detaching the inventory the issued_bool is set
    * to true.
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testRemoveTRKConsumableWithAutoIssueParamOnAndNotIssueInventory()
         throws MxException, TriggerException {

      withAutoIssueInventoryParameterOn();

      withNotIssuedInventory();

      // instantiate service class and remove the inventory
      RemovedPartService.removeParts( iTaskPartKey, null, iHrKey, true, true );

      // assert removed inventory details
      SchedRmvdPartTable lSchedRmvdPartTable =
            SchedRmvdPartTable.findByPrimaryKey( iTaskRmvdPartKey );
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( lSchedRmvdPartTable.getInventory() );

      // inventory was removed
      assertNull( lInvInv.getNhInvNo() );

      // since AUTO_ISSUE_INVENTORY is turn on, the issued_bool is set to true
      assertTrue( lInvInv.isIssuedBool() );
      assertEquals( lInvInv.getSerialNoOem(), SERIAL_NO );
   }


   /**
    * Set the AUTO_ISSUE_INVENTORY parameter to false
    */
   private void withAutoIssueInventoryParameterOff() {
      GlobalParameters.getInstance( ParmTypeEnum.LOGIC ).setBoolean( "AUTO_ISSUE_INVENTORY",
            false );
   }


   /**
    * Set the AUTO_ISSUE_INVENTORY parameter to true
    */
   private void withAutoIssueInventoryParameterOn() {
      GlobalParameters.getInstance( ParmTypeEnum.LOGIC ).setBoolean( "AUTO_ISSUE_INVENTORY", true );
   }


   /**
    * Update issued_bool = true for the removed inventory
    */
   private void withIssuedInventory() {
      InvInvTable lInvInvTable = InvInvTable.findByPrimaryKey( iTrkInventory );
      lInvInvTable.setIssuedBool( true );
      lInvInvTable.update();
   }


   /**
    * Update issued_bool = false for the removed inventory
    */
   private void withNotIssuedInventory() {
      InvInvTable lInvInvTable = InvInvTable.findByPrimaryKey( iTrkInventory );
      lInvInvTable.setIssuedBool( false );
      lInvInvTable.update();
   }
}
