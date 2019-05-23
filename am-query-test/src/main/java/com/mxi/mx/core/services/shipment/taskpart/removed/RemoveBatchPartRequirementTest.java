
package com.mxi.mx.core.services.shipment.taskpart.removed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.key.TimeZoneKey;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefRemoveReasonKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.key.TaskRmvdPartKey;
import com.mxi.mx.core.services.stask.taskpart.removed.RemovedPartService;
import com.mxi.mx.core.table.sched.SchedRmvdPartTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;


/**
 * Test the creation of a RO work package (repair order) for a removed repairable BATCH part
 *
 * @author gpichetto
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class RemoveBatchPartRequirementTest {

   /** Global Logic Parm stub */
   private String USERNAME_TESTUSER = "12345";
   private int USERID_TESTUSER = 12345;

   @Rule
   public OperateAsUserRule iOperateAsUserRule =
         new OperateAsUserRule( USERID_TESTUSER, USERNAME_TESTUSER );
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      GlobalParameters.getInstance( ParmTypeEnum.LOGIC ).setString( "BLANK_RO_SIGNATURE", null );
   }


   /**
    * This test ensure that no repair work order is created for a removed BATCH part that was in a
    * part requirement and is flagged as NOT repairable
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testRemoveNonRepairableBatchPartRequest() throws MxException, TriggerException {

      // create an inventory owner
      OwnerKey lOwner = new OwnerDomainBuilder().build();

      // build an inventory
      InventoryKey lMainInventoryKey = new InventoryBuilder().withOwner( lOwner ).build();

      // build a scheduled task
      final TaskKey lTask = new TaskBuilder().withName( "Test_Task" )
            .withLabour( RefLabourSkillKey.LBR, 0 ).onInventory( lMainInventoryKey ).build();

      // build a part group
      PartGroupKey lPartGroupKey = new PartGroupDomainBuilder( "TESTGROUP" )
            .withInventoryClass( RefInvClassKey.BATCH ).build();

      // build a NON repairable BATCH part
      final PartNoKey lPartNoKey = new PartNoBuilder().withUnitType( RefQtyUnitKey.EA )
            .withOemPartNo( "BATCH_PART_NOT_REPAIRABLE" )
            .withShortDescription( "Batch Part for testing" ).withTotalValue( new BigDecimal( 0 ) )
            .withInventoryClass( RefInvClassKey.BATCH )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withStatus( RefPartStatusKey.ACTV )
            .withTotalQuantity( new BigDecimal( 0 ) )
            .withAverageUnitPrice( new BigDecimal( 10.50 ) ).withRepairBool( false )
            .isAlternateIn( lPartGroupKey ).build();

      // build HR
      HumanResourceKey lHr = new HumanResourceDomainBuilder().withUserId( USERID_TESTUSER )
            .withUsername( USERNAME_TESTUSER ).build();

      // build a part requirement
      TaskPartKey lTaskPartKey = new PartRequirementDomainBuilder( lTask ).forPart( lPartNoKey )
            .withRemovalQuantity( 1 ).withRemovalReason( RefRemoveReasonKey.IMSCHD )
            .forPartGroup( lPartGroupKey ).forPart( lPartNoKey ).build();

      // generate task removed part key
      TaskRmvdPartKey lTaskRmvdPartKey = new TaskRmvdPartKey( lTaskPartKey, 1 );

      // instantiate service class and remove the part
      RemovedPartService.removeParts( lTaskPartKey, null, lHr, true, true );

      // assert inventory and serial number are filled in sched removed part record
      SchedRmvdPartTable lSchedRmvdPartTable =
            SchedRmvdPartTable.findByPrimaryKey( lTaskRmvdPartKey );
      assertNotNull( lSchedRmvdPartTable.getInventory() );
      assertNotNull( lSchedRmvdPartTable.getSerialNoOem() );

      // assert no RO task was created for the removed part
      List<TaskKey> lList =
            findComponentWorkPackagesForInventory( lSchedRmvdPartTable.getInventory() );
      assertTrue( lList.isEmpty() );
   }


   /**
    * This test ensure that a repair work order is created for a removed BATCH part that was in a
    * part requirement The part is repairable
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testRemoveRepairableBatchPartRequest() throws MxException, TriggerException {

      // create an inventory owner
      OwnerKey lOwner = new OwnerDomainBuilder().build();

      // build an inventory
      InventoryKey lMainInventoryKey = new InventoryBuilder().withOwner( lOwner ).build();

      // create schedule location
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "default_bool", true );

      QuerySet lQuerySet = QuerySetFactory.getInstance().executeQueryTable( "utl_timezone", lArgs );
      lQuerySet.first();

      TimeZoneKey lTimeZone = new TimeZoneKey( lQuerySet.getString( "timezone_cd" ) );

      LocationKey lHubLocation = new LocationDomainBuilder().isSupplyLocation().build();

      LocationKey lSupplyLocation =
            new LocationDomainBuilder().isSupplyLocation().withHubLocation( lHubLocation ).build();

      LocationKey lCheckLocation = new LocationDomainBuilder().withSupplyLocation( lSupplyLocation )
            .withTimeZone( lTimeZone ).build();

      // create issue account
      FncAccountKey lIssueAccount = new AccountBuilder().withType( RefAccountTypeKey.EXPENSE )
            .withCode( "TESTACCOUNT" ).isDefault().build();

      // create component work package
      TaskKey lCompCheck = new TaskBuilder().onInventory( lMainInventoryKey )
            .atLocation( lCheckLocation ).withTaskClass( RefTaskClassKey.CHECK )
            .withScheduledStart( new Date() ).withScheduledEnd( DateUtils.addDays( new Date(), 5 ) )
            .withIssueAccount( lIssueAccount ).build();

      // create a adhoc task under the component work package
      final TaskKey lTask =
            new TaskBuilder().withName( "Test_Task" ).withLabour( RefLabourSkillKey.LBR, 0 )
                  .onInventory( lMainInventoryKey ).withParentTask( lCompCheck ).build();

      // build a part group
      PartGroupKey lPartGroupKey = new PartGroupDomainBuilder( "TESTGROUP" )
            .withInventoryClass( RefInvClassKey.BATCH ).build();

      // build a repairable BATCH part
      final PartNoKey lPartNoKey = new PartNoBuilder().withUnitType( RefQtyUnitKey.EA )
            .withOemPartNo( "BATCH_PART_REPAIRABLE" )
            .withShortDescription( "Batch Part for testing" ).withTotalValue( new BigDecimal( 0 ) )
            .withInventoryClass( RefInvClassKey.BATCH )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withStatus( RefPartStatusKey.ACTV )
            .withTotalQuantity( new BigDecimal( 0 ) )
            .withAverageUnitPrice( new BigDecimal( 10.50 ) ).withRepairBool( true )
            .isAlternateIn( lPartGroupKey ).build();

      // build HR
      HumanResourceKey lHr = new HumanResourceDomainBuilder().withUserId( USERID_TESTUSER )
            .withUsername( USERNAME_TESTUSER ).build();

      // build a part requirement
      TaskPartKey lTaskPartKey = new PartRequirementDomainBuilder( lTask ).forPart( lPartNoKey )
            .withRemovalQuantity( 1 ).withRemovalReason( RefRemoveReasonKey.IMSCHD )
            .forPartGroup( lPartGroupKey ).forPart( lPartNoKey ).build();

      // generate task removed part key
      TaskRmvdPartKey lTaskRmvdPartKey = new TaskRmvdPartKey( lTaskPartKey, 1 );

      // instantiate service class and remove the part
      RemovedPartService.removeParts( lTaskPartKey, null, lHr, true, true );

      // assert inventory and serial number are filled in sched removed part record
      SchedRmvdPartTable lSchedRmvdPartTable =
            SchedRmvdPartTable.findByPrimaryKey( lTaskRmvdPartKey );
      assertNotNull( lSchedRmvdPartTable.getInventory() );
      assertNotNull( lSchedRmvdPartTable.getSerialNoOem() );

      // assert a RO task was created for the removed part
      List<TaskKey> lList =
            findComponentWorkPackagesForInventory( lSchedRmvdPartTable.getInventory() );
      assertTrue( !lList.isEmpty() );

      SchedStaskTable lSchedStaskTable = SchedStaskTable.findByPrimaryKey( lList.get( 0 ) );
      assertEquals( RefTaskClassKey.RO, lSchedStaskTable.getTaskClass() );
      assertEquals( new Double( 1 ), lSchedStaskTable.getRepairQt() );

      // assert the issue account cascaded from the component's work package
      assertEquals( lIssueAccount, lSchedStaskTable.getIssueAccount() );
   }


   /**
    * Returns a list of work packages (task class of RO) for the given inventory.
    *
    * @param aInventory
    *           inventory key
    *
    * @return list of work packages
    */
   private List<TaskKey> findComponentWorkPackagesForInventory( InventoryKey aInventory ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventory, "main_inv_no_db_id", "main_inv_no_id" );
      lArgs.add( RefTaskClassKey.RO, "task_class_db_id", "task_class_cd" );

      DataSet lDs = MxDataAccess.getInstance().executeQueryTable( TaskKey.TABLE_NAME, lArgs );

      // create list of work package TaskKeys
      List<TaskKey> lWPs = new ArrayList<TaskKey>();

      while ( lDs.next() ) {
         lWPs.add( lDs.getKey( TaskKey.class, "sched_db_id", "sched_id" ) );
      }

      return lWPs;
   }

}
