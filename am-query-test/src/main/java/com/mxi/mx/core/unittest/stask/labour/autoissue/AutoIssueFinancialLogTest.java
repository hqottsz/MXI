package com.mxi.mx.core.unittest.stask.labour.autoissue;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefAbcClassKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefReqTypeKey;
import com.mxi.mx.core.key.TaskInstPartKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.services.stask.workcapture.autoissue.BatchAutoIssueInventoryHandler;
import com.mxi.mx.core.table.inv.InvAcReg;
import com.mxi.mx.core.table.sched.SchedStaskTable;


public class AutoIssueFinancialLogTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /*
    * Test that a consumable batch part, when auto-issued, creates an ISSUE financial transaction
    */
   @Test
   public void testAutoIssueCreatesIssueTransaction() throws MxException, TriggerException {

      // Set up a work package and task
      AutoIssueFinancialLogTestData lTestData = setUpWPAndTaskWithPartInstallationRequirement();

      // Install the component during the completion of the task
      completePartRequestDuringTask( lTestData );

      // Verify that the Detailed Financial Log Report returns at least one row, providing the WP
      // information for the issuance
      QuerySet lQs = executeDetailedFinancialLogReportQuery();

      Assert.assertTrue( "No DetailedFinancialLogReport rows", lQs.next() );
      Assert.assertEquals( "Work package number not populated",
            AutoIssueFinancialLogTestData.WP_NUMBER, lQs.getString( "wo_ref_sdesc" ) );
      Assert.assertEquals( "Aircraft registration code not populated",
            AutoIssueFinancialLogTestData.AC_REG_CODE, lQs.getString( "ac_reg_cd" ) );
      Assert.assertEquals( "Maintenance location not populated",
            AutoIssueFinancialLogTestData.MAINT_LOCATION_CODE, lQs.getString( "loc_cd" ) );
      Assert.assertEquals( "Supply location not populated",
            AutoIssueFinancialLogTestData.SUPPLY_LOCATION_CODE, lQs.getString( "supply_loc_cd" ) );

   }


   // Set up a task with a part installation requirement with TableBuilders and Domain Builders
   private AutoIssueFinancialLogTestData setUpWPAndTaskWithPartInstallationRequirement() {

      final AutoIssueFinancialLogTestData lTestData = new AutoIssueFinancialLogTestData();

      lTestData.setHr( new HumanResourceDomainBuilder().build() );

      LocationKey iSupplyLocation = new LocationDomainBuilder()
            .withCode( AutoIssueFinancialLogTestData.SUPPLY_LOCATION_CODE ).build();
      lTestData.setMaintenanceLocation( new LocationDomainBuilder().withSupplyLocation( iSupplyLocation )
            .withCode( AutoIssueFinancialLogTestData.MAINT_LOCATION_CODE )
            .withType( RefLocTypeKey.LINE ).build() );

      // create an aircraft with issue to account which is mandatory
      final InventoryKey lAcft =
            new InventoryBuilder().withRegistrationCode( AutoIssueFinancialLogTestData.AC_REG_CODE )
                  .withClass( RefInvClassKey.ACFT ).build();

      final FncAccountKey lAcftAccount = new AccountBuilder().withCode( "ACFT_ACCOUNT" )
            .withType( RefAccountTypeKey.EXPENSE ).build();

      AircraftKey lAcftKey = new AircraftKey( lAcft );
      InvAcReg lInvAcReg = InvAcReg.findByPrimaryKey( lAcftKey );
      lInvAcReg.setIssueToAccount( lAcftAccount );

      lTestData.setTask( Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aBuilder ) {
            aBuilder.setIssueToAccount( lAcftAccount );

         }
      } ) );

      lTestData.setWPTask( Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aBuilder ) {
            aBuilder.setLocation( lTestData.getMaintenanceLocation() );
            aBuilder.setAircraft( lAcft );
            aBuilder.setName( AutoIssueFinancialLogTestData.WP_NAME );
            aBuilder.addTask( lTestData.getTask() );
         }
      } ) );

      // Simulate the work package being in scheduled state: it is given a WP number
      SchedStaskTable lSchedStaskTable = SchedStaskTable.findByPrimaryKey( lTestData.getWPTask() );
      lSchedStaskTable.setWoRefSdesc( AutoIssueFinancialLogTestData.WP_NUMBER );
      lSchedStaskTable.update();

      OwnerKey lOwnerKey = new OwnerDomainBuilder().build();

      // Set up batch parts and inventory
      lTestData.setBATCHPart( new PartNoBuilder().withInventoryClass( RefInvClassKey.BATCH )
            .withShortDescription( "BATCHA" ).withAbcClass( RefAbcClassKey.A )
            .withFinancialType( RefFinanceTypeKey.CONSUM )
            .withAverageUnitPrice( new BigDecimal( 1.0 ) )
            .withTotalQuantity( new BigDecimal( 1.0 ) ).withTotalValue( new BigDecimal( 1.0 ) )
            .build() );

      lTestData.setBATCHInventory( new InventoryBuilder().withPartNo( lTestData.getBATCHPart() )
            .withSerialNo( "batchA" ).withClass( RefInvClassKey.BATCH )
            .atLocation( lTestData.getMaintenanceLocation() ).withOwner( lOwnerKey )
            .withBinQt( 10.0 ).withReserveQt( 0.0 ).withCondition( RefInvCondKey.RFI ).build() );

      // create a part requirement with install of inventory A
      TaskPartKey lTaskPartKey = new PartRequirementDomainBuilder( lTestData.getTask() )
            .withInstallPart( lTestData.getBATCHPart() ).withInstallQuantity( 1.0 )
            .withInstallInventory( lTestData.getBATCHInventory() )
            .withInstallSerialNumber( "batchA" ).build();

      TaskInstPartKey iTaskInstPartKey = new TaskInstPartKey( lTaskPartKey, 1 );

      // create a part request with reserved inventory A (locally)
      lTestData.setPartRequestKey( new PartRequestBuilder().forPartRequirement( iTaskInstPartKey )
            .withType( RefReqTypeKey.TASK ).withReservedInventory( lTestData.getBATCHInventory() )
            .withRequestedQuantity( 1.0 ).withStatus( RefEventStatusKey.PRAVAIL )
            .isNeededAt( lTestData.getMaintenanceLocation() ).build() );

      return lTestData;
   }


   // Install the component during the completion of the task
   // This step emulates certain steps of CompleteService.completeNonRootTask( );
   private void completePartRequestDuringTask( AutoIssueFinancialLogTestData aTestData )
         throws TriggerException, MxException {

      // Process the auto-issue of the inventory (during labour finish)
      new BatchAutoIssueInventoryHandler().process( aTestData.getPartRequestKey(),
            aTestData.getHr(), aTestData.getTask() );

      // Copy the work and repair order properties from the root task to the
      // current task (during CompleteService.recordWorkOrder)
      SchedStaskTable lSchedStaskCurrent = SchedStaskTable.findByPrimaryKey( aTestData.getTask() );
      SchedStaskTable lSchedStaskRoot = SchedStaskTable.findByPrimaryKey( aTestData.getWPTask() );
      lSchedStaskCurrent.setRoRefSdesc( lSchedStaskRoot.getRoRefSdesc() );
      lSchedStaskCurrent.setRoVendor( lSchedStaskRoot.getRoVendor() );
      lSchedStaskCurrent.setWoRefSdesc( lSchedStaskRoot.getWoRefSdesc() );
      lSchedStaskCurrent.setWoTaskCompletedOn( aTestData.getWPTask() );
      lSchedStaskCurrent.update();
   }


   // Execute the report function
   private QuerySet executeDetailedFinancialLogReportQuery() {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aidt_from_date", DateUtils.addDays( new Date(), -10 ) );
      lArgs.add( "aidt_to_date", new Date() );
      lArgs.add( "aiv_account_code", "*ALL" );
      lArgs.add( "ait_account_type_code", "INVASSET, EXPENSE" );

      return QuerySetFactory.getInstance().executeQuery(
            "com.mxi.mx.report.query.inventory.DetailedFinancialLogReportQuery", lArgs );
   }


   // Encapsulate the test setup
   private class AutoIssueFinancialLogTestData {

      private static final String SUPPLY_LOCATION_CODE = "SUPPLY";
      private static final String MAINT_LOCATION_CODE = "MAINT";
      private static final String AC_REG_CODE = "AC_001";
      private static final String WP_NAME = "DTM_WP";
      private static final String WP_NUMBER = "WO 0001";
      private PartRequestKey iPartRequestKey;
      private TaskKey iTask;
      private TaskKey iWPTask;
      private PartNoKey iBATCHPart;
      private InventoryKey iBATCHInventory;
      private HumanResourceKey iHr;
      private LocationKey iMaintenanceLocation;


      public PartRequestKey getPartRequestKey() {
         return iPartRequestKey;
      }


      public void setPartRequestKey( PartRequestKey aPartRequestKey ) {
         iPartRequestKey = aPartRequestKey;
      }


      public TaskKey getTask() {
         return iTask;
      }


      public void setTask( TaskKey aTask ) {
         iTask = aTask;
      }


      public TaskKey getWPTask() {
         return iWPTask;
      }


      public void setWPTask( TaskKey aWPTask ) {
         iWPTask = aWPTask;
      }


      public PartNoKey getBATCHPart() {
         return iBATCHPart;
      }


      public void setBATCHPart( PartNoKey aBATCHPart ) {
         iBATCHPart = aBATCHPart;
      }


      public InventoryKey getBATCHInventory() {
         return iBATCHInventory;
      }


      public void setBATCHInventory( InventoryKey aBATCHInventory ) {
         iBATCHInventory = aBATCHInventory;
      }


      public HumanResourceKey getHr() {
         return iHr;
      }


      public void setHr( HumanResourceKey aHr ) {
         iHr = aHr;
      }


      public LocationKey getMaintenanceLocation() {
         return iMaintenanceLocation;
      }


      public void setMaintenanceLocation( LocationKey aMaintenanceLocation ) {
         iMaintenanceLocation = aMaintenanceLocation;
      }

   };

}
