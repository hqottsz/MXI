
package com.mxi.mx.core.services.inventory.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.AssemblyBuilder;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.ConfigSlotPositionBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.FncXactionAccountKey;
import com.mxi.mx.core.key.FncXactionLogKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefXactionTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.inventory.InventoryServiceFactory;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.table.fnc.FncXactionAccount;
import com.mxi.mx.core.table.fnc.FncXactionLog;
import com.mxi.mx.core.table.inv.InvAcReg;
import com.mxi.mx.core.table.inv.InvInvTable;


/**
 * Test the AttachableInventoryService.attachTrackedInventory() for an inventory that is serviceable
 * and not issued. Depending on the AUTO_ISSUE_INVENTORY parameter value, an issue transaction will
 * be recorded or not, thus the part total quantity can change or not
 *
 * @author gpichetto
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class InstallTRKConsumablePartTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public OperateAsUserRule iOperateAsUserRule = new OperateAsUserRule( USER_ID, "user" );

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final int USER_ID = 999;

   // Issue To Account for Aircraft, Work Package and Task
   private static final String ACFT_ACCOUNT = "acft_account";
   private static final String WP_ACCOUNT = "wp_account";
   private static final String TASK_ACCOUNT = "task_account";

   private boolean iAutoIssueInventoryParameter;
   private InventoryKey iEngine;
   private PartNoKey iEnginePart;
   private PartGroupKey iEnginePartGroup;
   private ConfigSlotPositionKey iEngineSubAssyPosition;
   private InventoryKey iEngineSystem;
   private InventoryKey iAcft;
   private HumanResourceKey iHrKey;
   private LocationKey iLocation;
   private FncAccountKey iAcftAccount;
   private FncAccountKey iWPAccount;
   private FncAccountKey iTaskAccount;
   private TaskKey iWorkPackage;
   private TaskKey iTask;


   /**
    * <p>
    * This test the install of an inventory based on a TRK consumable part, and Work Package and
    * Task have no specific Issue To Account.
    * </p>
    *
    * <ol>
    * Pre-conditions:
    * <li>The AUTO_ISSUE_INVENTORY is set to true;</li>
    * <li>The inventory is not issued and serviceable;</li>
    * <li>The Work Package and Task have no specific Issue To Account.</li>
    *
    * Assert:
    * <li>The Inventory is installed and issued_bool set to true;</li>
    * <li>A corrective issue transaction with Aircraft Issue To Account as the Debit Account is
    * generated;</li>
    * <li>The total part quantity will decrement on one unit.</li>
    * </ol>
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testInstallTRKConsumWithAutoIssueOnAndNotIssuedInvWithWPAndTaskNoAccount()
         throws MxException, TriggerException {

      withAutoIssueInventoryParameterOn();

      withNotIssuedInventory();

      // assert pre-conditions
      // Auto Issue parameter is on
      assertAutoIssueIsOn();

      // create work package and task without specific Issue To Account
      iWorkPackage = new TaskBuilder().atLocation( iLocation ).onInventory( iAcft ).build();
      iTask = new TaskBuilder().withParentTask( iWorkPackage ).build();

      // install TRK Consumable inventory and assert:
      // inventory is installed and issued_bool set to true;
      // part total quantity has decremented on one unit after installing;
      // an ISSUE type financial transaction is created and the Debit Account is Aircraft Issue To
      // Account
      installTRKConsumInvAndAssert( iAcftAccount );
   }


   /**
    * <p>
    * This test the install of an inventory based on a TRK consumable part, and Work Package has
    * specific Issue To Account and Task has no specific Issue To Account.
    * </p>
    *
    * <ol>
    * Pre-conditions:
    * <li>The AUTO_ISSUE_INVENTORY is set to true;</li>
    * <li>The inventory is not issued and serviceable;</li>
    * <li>The Work Package has specific Issue To Account and Task has no specific Issue To
    * Account.</li>
    *
    * Assert:
    * <li>The Inventory is installed and issued_bool set to true;</li>
    * <li>A corrective issue transaction with Work Package Issue To Account as the Debit Account is
    * generated;</li>
    * <li>The total part quantity will decrement on one unit.</li>
    * </ol>
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testInstallTRKConsumWithAutoIssueOnAndNotIssuedInvWithWPHasAccountAndTaskNoAccount()
         throws MxException, TriggerException {

      withAutoIssueInventoryParameterOn();

      withNotIssuedInventory();

      // assert pre-conditions
      // Auto Issue parameter is on
      assertAutoIssueIsOn();

      // create work package with specific Issue To Account and task without specific Issue To
      // Account
      iWorkPackage = new TaskBuilder().atLocation( iLocation ).onInventory( iAcft )
            .withIssueAccount( iWPAccount ).build();
      iTask = new TaskBuilder().withParentTask( iWorkPackage ).build();

      // install TRK Consumable inventory and assert:
      // inventory is installed and issued_bool set to true;
      // part total quantity has decremented on one unit after installing;
      // an ISSUE type financial transaction is created and the Debit Account is Work Package Issue
      // To Account
      installTRKConsumInvAndAssert( iWPAccount );
   }


   /**
    * <p>
    * This test the install of an inventory based on a TRK consumable part, and Work Package and
    * Task both have specific Issue To Accounts.
    * </p>
    *
    * <ol>
    * Pre-conditions:
    * <li>The AUTO_ISSUE_INVENTORY is set to true;</li>
    * <li>The inventory is not issued and serviceable;</li>
    * <li>The Work Package and Task both have specific Issue To Accounts.</li>
    *
    * Assert:
    * <li>The Inventory is installed and issued_bool set to true;</li>
    * <li>A corrective issue transaction with Task Issue To Account as the Debit Account is
    * generated;</li>
    * <li>The total part quantity will decrement on one unit.</li>
    * </ol>
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testInstallTRKConsumWithAutoIssueOnAndNotIssuedInvWithWPAndTaskBothHaveAccount()
         throws MxException, TriggerException {

      withAutoIssueInventoryParameterOn();

      withNotIssuedInventory();

      // assert pre-conditions
      // Auto Issue parameter is on
      assertAutoIssueIsOn();

      // create work package and task with specific Issue To Accounts
      iWorkPackage = new TaskBuilder().atLocation( iLocation ).onInventory( iAcft )
            .withIssueAccount( iWPAccount ).build();
      iTask = new TaskBuilder().withParentTask( iWorkPackage ).withIssueAccount( iTaskAccount )
            .build();

      // install TRK Consumable inventory and assert:
      // inventory is installed and issued_bool set to true;
      // part total quantity has decremented on one unit after installing;
      // an ISSUE type financial transaction is created and the Debit Account is Task Issue To
      // Account
      installTRKConsumInvAndAssert( iTaskAccount );
   }


   /**
    * Install TRK Consumable inventory and assert: the inventory is installed and issued_bool set to
    * true; part total quantity has decremented on one unit after installing; an ISSUE type
    * financial transaction is created and the Debit Account is correct
    *
    * @param aFncAccountKey
    *           financial account key
    *
    * @throws MxException
    * @throws TriggerException
    */
   private void installTRKConsumInvAndAssert( FncAccountKey aFncAccountKey )
         throws MxException, TriggerException {

      EqpPartNoTable lEqpPartNoTable = EqpPartNoTable.findByPrimaryKey( iEnginePart );
      BigDecimal lPartQtyPriorInstall = lEqpPartNoTable.getTotalQt();

      // assert pre-conditions
      // total part quantity prior to perform the install
      assertEquals( lPartQtyPriorInstall, new BigDecimal( 2 ) );

      // assert inventory is now detached
      InvInvTable lInvInvTable = InventoryServiceFactory.getInstance()
            .getAttachableInventoryService( iEngine ).findInvInvByPrimaryKey();
      assertNull( lInvInvTable.getNhInvNo() );

      // attach the engine to the aircraft at the sub-assembly position
      InventoryServiceFactory.getInstance().getAttachableInventoryService( iEngine )
            .attachTrackedInventory( iEngineSystem, iEngineSubAssyPosition, iEnginePartGroup, iTask,
                  iHrKey, false, null, null, true, false, false, iAutoIssueInventoryParameter,
                  false );

      // assert inventory is installed and issued_bool set to true
      lInvInvTable = InventoryServiceFactory.getInstance().getAttachableInventoryService( iEngine )
            .findInvInvByPrimaryKey();
      assertNotNull( lInvInvTable.getNhInvNo() );
      assertTrue( lInvInvTable.isIssuedBool() );

      // assert part total quantity has decremented on one unit after installing
      lEqpPartNoTable = EqpPartNoTable.findByPrimaryKey( iEnginePart );

      BigDecimal lPartQtyAfterInstall = lEqpPartNoTable.getTotalQt();
      assertEquals( lPartQtyAfterInstall, lPartQtyPriorInstall.subtract( BigDecimal.ONE ) );

      // assert an ISSUE type financial transaction is created
      FncXactionLog lFncXactionLog =
            FncXactionLog.findByPrimaryKey( getTransactionHistoy( iEnginePart, 1 ) );
      assertEquals( RefXactionTypeKey.ISSUE, lFncXactionLog.getXactionType() );

      // assert the Debit Account in the financial transaction is correct (2 is the
      // xaction_account_id in table fnc_xaction_account for debiting the expense account)
      FncXactionAccountKey lFncXactionAccountKey =
            new FncXactionAccountKey( getTransactionHistoy( iEnginePart, 1 ), 2 );
      FncXactionAccount lFncXactionAccount =
            FncXactionAccount.findByPrimaryKey( lFncXactionAccountKey );
      assertEquals( new FncAccountKey( aFncAccountKey.getDbId(), aFncAccountKey.getId() ),
            lFncXactionAccount.getAccount() );
   }


   /**
    * Sets up the test case data.
    */
   @Before
   public void setUp() {

      iHrKey = new HumanResourceDomainBuilder().withUserId( USER_ID ).build();

      // create an inventory owner
      OwnerKey lOwner = new OwnerDomainBuilder().build();

      // create a location
      iLocation = new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT ).build();

      // create a part
      iEnginePart = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK )
            .withFinancialType( RefFinanceTypeKey.CONSUM )
            .withAverageUnitPrice( new BigDecimal( 11 ) ).withTotalQuantity( new BigDecimal( 2 ) )
            .withTotalValue( new BigDecimal( 22 ) ).build();

      // create the aircraft assembly
      AssemblyKey lAcftAssembly = new AssemblyBuilder( "ACFT" ).build();

      // create the aircraft root config slot and position
      ConfigSlotKey lAcftConfigSlot =
            new ConfigSlotBuilder( "ACFT" ).withRootAssembly( lAcftAssembly ).build();
      ConfigSlotPositionKey lAcftPosition =
            new ConfigSlotPositionBuilder().withConfigSlot( lAcftConfigSlot ).build();

      // create the engine system config slot and position
      ConfigSlotKey lEngineSystemSlot =
            new ConfigSlotBuilder( "ENGINE_SYS" ).withRootAssembly( lAcftAssembly )
                  .withParent( lAcftConfigSlot ).withClass( RefBOMClassKey.SYS ).build();
      ConfigSlotPositionKey lEngineSystemPosition = new ConfigSlotPositionBuilder()
            .withConfigSlot( lEngineSystemSlot ).withParentPosition( lAcftPosition ).build();

      // create the config slot for the engine subassembly
      ConfigSlotKey lEngineSubAssySlot =
            new ConfigSlotBuilder( "ENGINE_SUBASSY" ).withClass( RefBOMClassKey.SUBASSY )
                  .withParent( lEngineSystemSlot ).withRootAssembly( lAcftAssembly ).build();

      iEngineSubAssyPosition = new ConfigSlotPositionBuilder().withConfigSlot( lEngineSubAssySlot )
            .withParentPosition( lEngineSystemPosition ).build();

      // create a part group for the engine in the subassembly config slot
      iEnginePartGroup = new PartGroupDomainBuilder( "ENGINE" ).withConfigSlot( lEngineSubAssySlot )
            .withPartNo( iEnginePart ).withInventoryClass( RefInvClassKey.ASSY ).build();

      // create the enigne assembly
      AssemblyKey lEngineAssembly = new AssemblyBuilder( "ENGINE" ).build();

      // create the engine config slot and position
      ConfigSlotKey lEngineSlot =
            new ConfigSlotBuilder( "ENGINE" ).withRootAssembly( lEngineAssembly ).build();
      ConfigSlotPositionKey lEnginePosition =
            new ConfigSlotPositionBuilder().withConfigSlot( lEngineSlot ).build();

      // create the aircraft
      iAcft = new InventoryBuilder().withConfigSlotPosition( lAcftPosition )
            .withClass( RefInvClassKey.ACFT ).build();
      iAcftAccount = new AccountBuilder().withCode( ACFT_ACCOUNT )
            .withType( RefAccountTypeKey.EXPENSE ).build();

      AircraftKey lAcftKey = new AircraftKey( iAcft );
      InvAcReg lInvAcReg = InvAcReg.findByPrimaryKey( lAcftKey );
      lInvAcReg.setIssueToAccount( iAcftAccount );

      // create the engine system
      iEngineSystem = new InventoryBuilder().withClass( RefInvClassKey.SYS )
            .withParentInventory( iAcft ).withAssemblyInventory( iAcft )
            .withConfigSlotPosition( lEngineSystemPosition ).build();

      // create the engine inventory
      iEngine = new InventoryBuilder().withPartNo( iEnginePart )
            .withConfigSlotPosition( lEnginePosition ).withClass( RefInvClassKey.ASSY )
            .withCondition( RefInvCondKey.RFI ).withOwner( lOwner ).atLocation( iLocation ).build();

      // create an account for work package
      iWPAccount = new AccountBuilder().withCode( WP_ACCOUNT ).withType( RefAccountTypeKey.EXPENSE )
            .build();
      // create an account for task
      iTaskAccount = new AccountBuilder().withCode( TASK_ACCOUNT )
            .withType( RefAccountTypeKey.EXPENSE ).build();
   }


   /**
    * Get the financial transaction history for a given part no
    *
    * @param aPartNoKey
    *           part no key
    * @param aDays
    *           days count
    *
    * @return the financial transaction key
    */
   private FncXactionLogKey getTransactionHistoy( PartNoKey aPartNoKey, int aDays ) {
      FncXactionLogKey lKey = null;
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPartNoKey, "aPartNoDbId", "aPartNoId" );
      lArgs.add( "aDayCount", aDays );

      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.web.query.part.PartTransactionHistory", lArgs );

      if ( lQs.next() ) {
         lKey = lQs.getKey( FncXactionLogKey.class, "trans_key" );
      }

      return lKey;
   }


   /**
    * Set the AUTO_ISSUE_INVENTORY parameter to true
    */
   private void withAutoIssueInventoryParameterOn() {
      GlobalParameters.getInstance( ParmTypeEnum.LOGIC ).setBoolean( "AUTO_ISSUE_INVENTORY", true );
   }


   /**
    * Update issued_bool = false for the inventory
    */
   private void withNotIssuedInventory() {
      InvInvTable lInvInvTable = InvInvTable.findByPrimaryKey( iEngine );
      lInvInvTable.setIssuedBool( false );
      lInvInvTable.update();
   }


   /**
    * Assert AUTO_ISSUE_INVENTORY parameter is on
    */
   private void assertAutoIssueIsOn() {
      iAutoIssueInventoryParameter =
            GlobalParameters.getInstance( ParmTypeEnum.LOGIC ).getBoolean( "AUTO_ISSUE_INVENTORY" );
      assertTrue( iAutoIssueInventoryParameter );
   }
}
