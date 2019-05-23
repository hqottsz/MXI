package com.mxi.mx.core.services.stask.maintrelease;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.HumanResource;
import com.mxi.am.domain.RepairOrder;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.common.trigger.TriggerFactoryStub;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.plugin.releasenumber.RepairOrderReleaseNumberGenerator;
import com.mxi.mx.core.plugin.releasenumber.WorkOrderReleaseNumberGenerator;
import com.mxi.mx.core.services.inventory.ReleaseNotesTO;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.task.SchedWPTable;
import com.mxi.mx.core.trigger.MxCoreTriggerType;


public class MaintenanceReleaseServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   @Rule
   public OperateAsUserRule iOperateAsUserRule = new OperateAsUserRule( 12345, "jsmith" );

   private InventoryKey iAircraftKey;
   private TaskKey iRepairOrderKey;
   private TaskKey iCheckKey;

   private MaintenanceReleaseService iMaintenanceReleaseService;


   @Before
   public void setUp() {
      iMaintenanceReleaseService = new MaintenanceReleaseServiceImpl();

      Map<String, Object> lTriggerMap = new HashMap<String, Object>( 1 );
      lTriggerMap.put( MxCoreTriggerType.WO_REL_NUM_GEN.toString(),
            new WorkOrderReleaseNumberGenerator() );
      lTriggerMap.put( MxCoreTriggerType.RO_REL_NUM_GEN.toString(),
            new RepairOrderReleaseNumberGenerator() );

      TriggerFactory lTriggerFactoryStub = new TriggerFactoryStub( lTriggerMap );
      TriggerFactory.setInstance( lTriggerFactoryStub );

      // Create the dummy user to operate as
      Domain.createHumanResource( new DomainConfiguration<HumanResource>() {

         @Override
         public void configure( HumanResource aBuilder ) {
            aBuilder.setUser( new UserKey( 12345 ) );
         }

      } );

      iAircraftKey = Domain.createAircraft();
      iRepairOrderKey = Domain.createRepairOrder( new DomainConfiguration<RepairOrder>() {

         @Override
         public void configure( RepairOrder aBuilder ) {
            aBuilder.setMainInventory( iAircraftKey );
         }

      } );
      iCheckKey = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aBuilder ) {
            aBuilder.setAircraft( iAircraftKey );
         }

      } );

   }


   @Test( expected = NullPointerException.class )
   public void generateReleaseNumber_null() throws Throwable {
      iMaintenanceReleaseService.generateReleaseNumber( null );
   }


   @Test
   public void generateReleaseNumber_repairOrder() throws Throwable {
      WorkPackageDetailsTO lRepairOrder =
            new WorkPackageDetailsTO( iRepairOrderKey, RefTaskClassKey.RO, iAircraftKey );

      String lReleaseNumber = iMaintenanceReleaseService.generateReleaseNumber( lRepairOrder );

      String lExpectedReleaseNumber = "RO - " + iRepairOrderKey.getId();
      assertEquals( lExpectedReleaseNumber, lReleaseNumber );
   }


   @Test
   public void generateReleaseNumber_aircraftCheck() throws Throwable {
      WorkPackageDetailsTO lCheck =
            new WorkPackageDetailsTO( iCheckKey, RefTaskClassKey.CHECK, iAircraftKey );

      String lReleaseNumber = iMaintenanceReleaseService.generateReleaseNumber( lCheck );

      String lExpectedReleaseNumber = "WO - " + iCheckKey.getId();
      assertEquals( lExpectedReleaseNumber, lReleaseNumber );
   }


   @Test( expected = NullPointerException.class )
   public void updateReleaseNotes_nullArgs() throws Throwable {
      iMaintenanceReleaseService.updateReleaseNotes( null, null );
   }


   @Test( expected = NullPointerException.class )
   public void updateReleaseNotes_nullWorkPackageDetails() throws Throwable {
      iMaintenanceReleaseService.updateReleaseNotes( null, new ReleaseNotesTO() );
   }


   @Test( expected = NullPointerException.class )
   public void updateReleaseNotes_nullReleaseNotes() throws Throwable {
      WorkPackageDetailsTO lCheck =
            new WorkPackageDetailsTO( iCheckKey, RefTaskClassKey.CHECK, iAircraftKey );

      iMaintenanceReleaseService.updateReleaseNotes( lCheck, null );
   }


   @Test( expected = MxRuntimeException.class )
   public void updateReleaseNotes_nonExistingCheck() throws Throwable {
      TaskKey lNonExistingCheck = new TaskKey( 9999, 9999 );
      WorkPackageDetailsTO lCheck =
            new WorkPackageDetailsTO( lNonExistingCheck, RefTaskClassKey.CHECK, iAircraftKey );
      ReleaseNotesTO lReleaseNotes =
            new ReleaseNotesTO( new Date(), "WO - 1234", "Release remarks", true );

      iMaintenanceReleaseService.updateReleaseNotes( lCheck, lReleaseNotes );
   }


   @Test
   public void updateReleaseNotes_setsWorkPackageAndInventoryReleaseFields() throws Throwable {
      WorkPackageDetailsTO lCheck =
            new WorkPackageDetailsTO( iCheckKey, RefTaskClassKey.CHECK, iAircraftKey );

      Date lReleaseDate = new Date();
      String lReleaseNumber = "WO - 1234";
      String lReleaseRemarks = "Release remarks";
      boolean lReleaseToService = true;

      ReleaseNotesTO lReleaseNotes =
            new ReleaseNotesTO( lReleaseDate, lReleaseNumber, lReleaseRemarks, lReleaseToService );

      iMaintenanceReleaseService.updateReleaseNotes( lCheck, lReleaseNotes );

      // Ensure the wp was updated
      QuerySet lWorkPackageReleaseData = getWorkPackageReleaseData( iCheckKey );
      assertTrue( lWorkPackageReleaseData.next() );

      assertEquals( lReleaseNumber, lWorkPackageReleaseData
            .getString( SchedWPTable.ColumnName.RELEASE_NUMBER_SDESC.name() ) );
      assertEquals( lReleaseRemarks, lWorkPackageReleaseData
            .getString( SchedWPTable.ColumnName.RELEASE_REMARKS_LDESC.name() ) );
      assertEquals( lReleaseToService, lWorkPackageReleaseData
            .getBoolean( SchedWPTable.ColumnName.RELEASE_TO_SERVICE_BOOL.name() ) );

      // Ensure the inv was updated
      QuerySet lInventoryReleaseData = getInventoryReleaseData( iAircraftKey );
      assertTrue( lInventoryReleaseData.next() );

      assertEquals( DateUtils.truncate( lReleaseDate, Calendar.SECOND ),
            DateUtils.truncate(
                  lInventoryReleaseData.getDate( InvInvTable.ColumnName.RELEASE_DT.name() ),
                  Calendar.SECOND ) );
      assertEquals( lReleaseNumber,
            lInventoryReleaseData.getString( InvInvTable.ColumnName.RELEASE_NUMBER_SDESC.name() ) );
      assertEquals( lReleaseRemarks, lInventoryReleaseData
            .getString( InvInvTable.ColumnName.RELEASE_REMARKS_LDESC.name() ) );
   }


   private QuerySet getWorkPackageReleaseData( TaskKey aWorkPackage ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aWorkPackage, SchedWPTable.ColumnName.SCHED_DB_ID.name(),
            SchedWPTable.ColumnName.SCHED_ID.name() );
      return QuerySetFactory.getInstance().executeQueryTable( SchedWPTable.TABLE_NAME, lArgs );
   }


   private QuerySet getInventoryReleaseData( InventoryKey aInventory ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventory, InvInvTable.ColumnName.INV_NO_DB_ID.name(),
            InvInvTable.ColumnName.INV_NO_ID.name() );
      return QuerySetFactory.getInstance().executeQueryTable( InventoryKey.TABLE_NAME, lArgs );
   }
}
