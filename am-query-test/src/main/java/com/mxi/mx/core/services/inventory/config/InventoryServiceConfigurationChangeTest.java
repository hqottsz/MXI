package com.mxi.mx.core.services.inventory.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.Part;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.System;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLabourRoleTypeKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefRemoveReasonKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.inventory.InvUtils;
import com.mxi.mx.core.services.inventory.InventoryService;
import com.mxi.mx.core.services.inventory.creation.InventoryCreationService;
import com.mxi.mx.core.services.partgroup.AlternatePartService;
import com.mxi.mx.core.services.stask.taskpart.ScheduledPartService;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.core.table.eqp.EqpAssmblPos;
import com.mxi.mx.core.table.eqp.EqpBomPart;
import com.mxi.mx.core.table.inv.InvOwnerTable;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.sched.SchedLabourRoleTable;
import com.mxi.mx.core.table.sched.SchedLabourTable;
import com.mxi.mx.core.usage.dao.JdbcUsageDataDao;
import com.mxi.mx.core.usage.dao.JdbcUsageRecordDao;
import com.mxi.mx.core.usage.dao.UsageDataDao;
import com.mxi.mx.core.usage.dao.UsageDataEntity;
import com.mxi.mx.core.usage.dao.UsageRecordEntity;
import com.mxi.mx.core.usage.model.UsageAdjustmentId;


/**
 * Test that InventoryService.recordConfigurationChange method properly populates inv_install
 *
 * Unable to use junit Theories to parameterize tests because I was unable to process @Rules
 * before @Datapoints
 */
public class InventoryServiceConfigurationChangeTest {

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private Date iCurrentDate;
   private Date iYesterday;
   private HumanResourceKey iHrKey;
   private int iUserId;

   /** The table columns for inventory that needs to be synchronized */
   private static final String[] INV_COLUMNS = new String[] { "inv_no_db_id", "inv_no_id" };

   /** The assembly inventory for the inventory */
   private static final String[] H_INV_COLUMNS = new String[] { "h_inv_no_db_id", "h_inv_no_id" };

   /** The assembly inventory for the inventory */
   private static final String[] ASSMBL_INV_COLUMNS =
         new String[] { "assmbl_inv_no_db_id", "assmbl_inv_no_id" };

   /** All columns to query for verification */
   private static final String[] INV_INSTALL_COLUMNS = { "inv_no_id", "inv_no_db_id",
         "assmbl_inv_no_id", "assmbl_inv_no_db_id", "h_inv_no_id", "h_inv_no_db_id", "event_dt" };


   /**
    * <pre>
    * Given an on-wing TRK inventory
    * When I attach a SER inventory to the TRK inventory via the completion of a part requirement
    * An entry for the SER inventory is added to the INV_INSTALL table
    * </pre>
    **/
   @Test
   public void itRecordsInstalledSerComponentToInvInstall() {

      // Arrange
      final InventoryKey lSerializedKey = Domain.createSerializedInventory();

      final InventoryKey lTrackedKey =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.addSerialized( lSerializedKey );
               }
            } );

      final InventoryKey lAircraftKey = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.addTracked( lTrackedKey );
         }
      } );

      // Act
      new InventoryService().recordConfigurationChange( lSerializedKey, createReqDoneYesterday(),
            RefEventStatusKey.FGINST, RefStageReasonKey.ACRTV, null, "", "", null );

      // Assert rows were properly added
      assertInvRowsAsExpected( lAircraftKey, lAircraftKey, iYesterday, lSerializedKey );
   }


   /**
    * <pre>
    * Given an aircraft
    * When I attach an engine which has two attached components via the Attach button
    * Entries for the engine and attached components are added to the INV_INSTALL table
    * </pre>
    **/
   @Test
   public void itRecordsInstalledTrkComponentToInvInstallNoTask() {
      itRecordsInstalledTrkComponentAndChildrenToInvInstall( null );
   }


   /**
    * <pre>
    * Given an aircraft
    * When I attach an engine which has two attached components via completion of a part requirement
    * Entries for the engine and attached components are added to the INV_INSTALL table
    * </pre>
    **/
   @Test
   public void itRecordsInstalledTrkComponentToInvInstallValidTask() {
      itRecordsInstalledTrkComponentAndChildrenToInvInstall( createReqDoneYesterday() );
   }


   private void itRecordsInstalledTrkComponentAndChildrenToInvInstall( TaskKey aTask ) {

      // Arrange
      final InventoryKey lSerKey = Domain.createSerializedInventory();

      final InventoryKey lTrkKey = Domain.createTrackedInventory();

      final InventoryKey lEngineKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.addSerialized( lSerKey );
            aBuilder.addTracked( lTrkKey );
         }
      } );

      final InventoryKey lAircraftKey = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.addEngine( lEngineKey );

         }
      } );

      // Act
      new InventoryService().recordConfigurationChange( lEngineKey, aTask, RefEventStatusKey.FGINST,
            RefStageReasonKey.ACRTV, null, "", "", null );

      // Assert rows were properly added. If there is an associated task, expect the event date of
      // that task, otherwise the current date
      assertInvRowsAsExpected( lEngineKey, lAircraftKey,
            ( aTask == null ? iCurrentDate : iYesterday ), lSerKey, lTrkKey, lEngineKey );
   }


   /**
    * <pre>
    * Given an off-wing engine with a system slot
    * When I attach a SER inventory to the system slot via the completion of a part requirement
    * An entry for the SER inventory is added to INV_INSTALL
    * </pre>
    */
   @Test
   public void itRecordsInstalledSerUnderSystemToInvInstall() {
      // Arrange
      final InventoryKey lSerializedKey = Domain.createSerializedInventory();

      final InventoryKey lEngineKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngineBuilder ) {
            aEngineBuilder.addSystem( new DomainConfiguration<System>() {

               @Override
               public void configure( System aSystemBuilder ) {
                  aSystemBuilder.addSerialized( lSerializedKey );
               }
            } );
         }
      } );

      // Act
      new InventoryService().recordConfigurationChange( lSerializedKey, createReqDoneYesterday(),
            RefEventStatusKey.FGINST, RefStageReasonKey.ACRTV, null, "", "", null );

      // Assert correct row was added
      assertInvRowsAsExpected( lEngineKey, lEngineKey, iYesterday, lSerializedKey );
   }


   /**
    * <pre>
    * Given an aircraft
    * When I attach an engine with a system slot via the Attach button
    * There is an entry for the system slot added to INV_INSTALL
    * </pre>
    */
   @Test
   public void itRecordsSystemsToInvInstallNoTask() {
      itRecordsSystemToInvInstall( null );
   }


   /**
    * <pre>
    * Given an aircraft
    * When I attach an engine with a system slot via the completion of a part requirement
    * There is an entry for the system slot added to INV_INSTALL
    * </pre>
    */
   @Test
   public void itRecordsSystemsToInvInstallValidTask() {
      itRecordsSystemToInvInstall( createReqDoneYesterday() );
   }


   /**
    * Tests if the configuration change was caused by a labour completion, use the labour completion
    * date to be the inv_install date
    */
   @Test
   public void recordConfigurationChange_withLabourCompletionDate() {
      // Set up an ended labour which caused this configuration change
      final Date lLabourCompletionDate = DateUtils.addDays( iCurrentDate, -30 );
      SchedLabourTable lSchedLabourTable = SchedLabourTable.create();
      lSchedLabourTable.insert();
      SchedLabourRoleTable lSchedLabourRoleTable = SchedLabourRoleTable.create();
      lSchedLabourRoleTable.setSchedLabour( lSchedLabourTable.getPk() );
      lSchedLabourRoleTable.setLabourRoleType( RefLabourRoleTypeKey.TECH );
      lSchedLabourRoleTable.setActualEndDate( lLabourCompletionDate );
      lSchedLabourRoleTable.insert();
      // Set up the installed inventory and the engine it was installed to
      InventoryKey lEngineInventory = Domain.createEngine();
      InventoryKey lInstalledInventory =
            Domain.createTrackedInventory( lInventory -> lInventory.setParent( lEngineInventory ) );

      new InventoryService().recordConfigurationChange( lInstalledInventory,
            Domain.createAdhocTask(), RefEventStatusKey.FGINST, RefStageReasonKey.ACRTV, null, "",
            "", lSchedLabourTable.getPk() );

      // Assert rows were properly added and the date of the inv_install is the labour completion
      // date
      assertInvRowsAsExpected( lEngineInventory, lEngineInventory, lLabourCompletionDate,
            lInstalledInventory );
   }


   private void itRecordsSystemToInvInstall( TaskKey aTask ) {

      // Arrange
      final InventoryKey iEngineKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngineBuilder ) {
            aEngineBuilder.addSystem( "Engine Subsystem" );
         }
      } );

      // Can't get the engine subsystem InventoryKey directly, so look it up here
      InventoryKey iSystemKey = InvUtils.getSystemByName( iEngineKey, "Engine Subsystem" );

      final InventoryKey iAircraftKey = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraftBuilder ) {
            aAircraftBuilder.addEngine( iEngineKey );
         }
      } );

      // Act
      new InventoryService().recordConfigurationChange( iEngineKey, aTask, RefEventStatusKey.FGINST,
            RefStageReasonKey.ACRTV, null, "", "", null );

      // Assert rows were properly added. If there is an associated task, expect the event date of
      // that task, otherwise the current date
      assertInvRowsAsExpected( iEngineKey, iAircraftKey,
            ( aTask == null ? iCurrentDate : iYesterday ), iEngineKey, iSystemKey );
   }


   /*
    * Test the create and attach button, Tracked inventory
    */
   @Test
   public void itRecordsConfigChangeForCreateAndAttachButton()
         throws MxException, TriggerException {

      // Set up an aircraft
      PartNoKey lPartNoKey = createTrkPart();
      LocationKey lLocationKey = createLocation();
      InventoryKey lAircraftInvKey =
            createAircraftWithSlotsForPartAtLocation( lPartNoKey, lLocationKey );

      // Get the assembly, config slot, and position for the tracked part
      PartGroupKey lTrkBomPartKey = AlternatePartService.getBomPartsFromPartNo( lPartNoKey )[0];
      ConfigSlotKey lTrkBomItemKey = EqpBomPart.findByPrimaryKey( lTrkBomPartKey ).getBomItem();
      ConfigSlotPositionKey lFirstTrkPositionKey = new ConfigSlotPositionKey( lTrkBomItemKey, 1 );

      // Act
      new InventoryCreationService().createAssyTrkSerWithRole( lLocationKey, lPartNoKey, "123",
            lAircraftInvKey, lFirstTrkPositionKey, lTrkBomPartKey, true, false,
            RefStageReasonKey.ACBROKEN, "", iHrKey, true, null, null,
            InvOwnerTable.getOwnerKey( "N/A" ), iCurrentDate, iCurrentDate, false, false,
            DateUtils.addDays( iCurrentDate, 1 ), true, false, false );

      // Assert a row was added
      assertEquals( "Incorrect number of rows were added", 1, QuerySetFactory.getInstance()
            .executeQueryTable( "inv_install", new DataSetArgument() ).getRowCount() );
   }


   /*
    * Test completing a part requirement, tracked, via complete Task or Finish Labour
    */
   @Test
   public void itRecordsConfigChangeForPerformPartRequirement()
         throws MxException, TriggerException {

      // Set up an aircraft
      final PartNoKey lPartNoKey = createTrkPart();
      final LocationKey lLocationKey = createLocation();
      final InventoryKey lAircraftInvKey =
            createAircraftWithSlotsForPartAtLocation( lPartNoKey, lLocationKey );

      // Get the assembly, config slot, and position for the tracked part
      final PartGroupKey lTrkBomPartKey =
            AlternatePartService.getBomPartsFromPartNo( lPartNoKey )[0];
      final ConfigSlotKey lTrkBomItemKey =
            EqpBomPart.findByPrimaryKey( lTrkBomPartKey ).getBomItem();
      final ConfigSlotPositionKey lFirstTrkPositionKey =
            new ConfigSlotPositionKey( lTrkBomItemKey, 1 );

      // Set up an inventory to be installed
      final String lTrkInventorySerialNumber = "123";

      InventoryKey lTrackedInventory =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.setPartNumber( lPartNoKey );
                  aBuilder.setSerialNumber( lTrkInventorySerialNumber );
                  aBuilder.setLocation( lLocationKey );
                  aBuilder.setCondition( RefInvCondKey.RFI );
                  aBuilder.setOwner( InvOwnerTable.getOwnerKey( "N/A" ) );

                  aBuilder.setLastKnownConfigSlot( lTrkBomItemKey.getCd(),
                        EqpAssmblBom.findByPrimaryKey( lTrkBomItemKey ).getAssemblBomCd(),
                        EqpAssmblPos.findByPrimaryKey( lFirstTrkPositionKey ).getEqpPosCode() );
               }
            } );

      // Set up a requirement for the installation
      TaskKey iTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aBuilder ) {
            aBuilder.setInventory( lAircraftInvKey );
         }
      } );

      // Set up a part requirement
      new PartRequirementDomainBuilder( iTask ).forPartGroup( lTrkBomPartKey )
            .forPosition( lFirstTrkPositionKey ).withInstallInventory( lTrackedInventory )
            .withInstallQuantity( 1.0 ).withInstallSerialNumber( lTrkInventorySerialNumber )
            .withInstallPart( lPartNoKey ).build();

      // Act
      new ScheduledPartService().completeTaskParts( iTask, iHrKey, true, null );

      // Assert a row was added
      assertEquals( "Incorrect number of rows were added", 1, QuerySetFactory.getInstance()
            .executeQueryTable( "inv_install", new DataSetArgument() ).getRowCount() );
   }


   /**
    * This test case is testing when remove/install part, a usage record is created for usage
    * correction
    *
    * <pre>
    *    Given an aircraft with a usage adjustment.
    *    And a tracked inventory on the aircraft.
    *    And a removal/install task for the tracked inventory before the usage adjustment.
    *    And WP with manual entered usage different than auto calculated ones.
    *    When complete the part requirement.
    *    Then verify usage records are created to correct usage on removed and installed inventory.
    * </pre>
    */
   @Test
   public void itCreatesUsageRecordForRemovalAndInstallTasks() throws Exception {
      GlobalParameters lGlobalParametersFake = new GlobalParametersFake( "LOGIC" );
      lGlobalParametersFake.setString( "BLANK_RO_SIGNATURE", "" );
      GlobalParameters.setInstance( "LOGIC", lGlobalParametersFake );
      Date now = new Date();
      Calendar calendar = Calendar.getInstance();
      calendar.setTime( now );
      calendar.add( Calendar.DAY_OF_YEAR, -2 );
      Date taskEndDate = calendar.getTime();
      Map<DataTypeKey, BigDecimal> usages = new HashMap<>();
      usages.put( DataTypeKey.HOURS, new BigDecimal( 300 ) );
      usages.put( DataTypeKey.CYCLES, new BigDecimal( 30 ) );
      calendar.setTime( taskEndDate );
      calendar.add( Calendar.MINUTE, -1 );
      calendar.setTime( now );
      calendar.add( Calendar.MINUTE, 2 );

      PartNoKey lPartNoKey = createTrkPart();
      LocationKey lLocationKey = createLocation();
      InventoryKey lAircraftInvKey =
            createAircraftWithSlotsForPartAtLocation( lPartNoKey, lLocationKey, usages );

      Domain.createUsageAdjustment( usageAdjustment -> {
         usageAdjustment.setMainInventory( lAircraftInvKey );
         usageAdjustment.setUsageDate( now );
         usageAdjustment.addUsage( lAircraftInvKey, DataTypeKey.HOURS, new BigDecimal( 300 ),
               new BigDecimal( 200 ) );
         usageAdjustment.addUsage( lAircraftInvKey, DataTypeKey.CYCLES, new BigDecimal( 30 ),
               new BigDecimal( 20 ) );
      } );

      PartGroupKey lTrkBomPartKey = AlternatePartService.getBomPartsFromPartNo( lPartNoKey )[0];
      ConfigSlotKey lTrkBomItemKey = EqpBomPart.findByPrimaryKey( lTrkBomPartKey ).getBomItem();
      ConfigSlotPositionKey lFirstTrkPositionKey = new ConfigSlotPositionKey( lTrkBomItemKey, 1 );

      final String lTrkInventorySerialNumberToBeInstalled = "123";
      final String lTrkInventorySerialNumberToBeRemoved = "321";

      InventoryKey lTrackedInventoryToBeInstalled =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.setPartNumber( lPartNoKey );
                  aBuilder.setSerialNumber( lTrkInventorySerialNumberToBeInstalled );
                  aBuilder.setLocation( lLocationKey );
                  aBuilder.setCondition( RefInvCondKey.RFI );
                  aBuilder.setOwner( InvOwnerTable.getOwnerKey( "N/A" ) );
               }
            } );

      InventoryKey lTrackedInventoryToBeRemoved =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.setPartNumber( lPartNoKey );
                  aBuilder.setSerialNumber( lTrkInventorySerialNumberToBeRemoved );
                  aBuilder.setLocation( lLocationKey );
                  aBuilder.setCondition( RefInvCondKey.INREP );
                  aBuilder.setOwner( InvOwnerTable.getOwnerKey( "N/A" ) );
                  aBuilder.setParent( lAircraftInvKey );
                  aBuilder.setPartGroup( lTrkBomPartKey );
                  aBuilder.setPosition( lFirstTrkPositionKey );
                  aBuilder.addUsage( DataTypeKey.CYCLES, new BigDecimal( 30 ) );
                  aBuilder.addUsage( DataTypeKey.HOURS, new BigDecimal( 300 ) );
               }
            } );

      TaskKey lTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aBuilder ) {
            aBuilder.setStatus( RefEventStatusKey.COMPLETE );
            aBuilder.setActualEndDate( taskEndDate );
            aBuilder.setInventory( lAircraftInvKey );
         }
      } );

      Domain.createWorkPackage( check -> {
         check.setAircraft( lAircraftInvKey );
         check.addTask( lTask );
         check.addUsageSnapshot( lAircraftInvKey, DataTypeKey.HOURS, new BigDecimal( 280 ) );
         check.addUsageSnapshot( lAircraftInvKey, DataTypeKey.CYCLES, new BigDecimal( 28 ) );
         check.setStatus( RefEventStatusKey.COMPLETE );
         check.setActualEndDate( taskEndDate );
      } );

      new PartRequirementDomainBuilder( lTask ).forPartGroup( lTrkBomPartKey )
            .forPosition( lFirstTrkPositionKey )
            .withInstallInventory( lTrackedInventoryToBeInstalled ).withInstallQuantity( 1.0 )
            .withInstallSerialNumber( lTrkInventorySerialNumberToBeInstalled )
            .withInstallPart( lPartNoKey ).withRemovalInventory( lTrackedInventoryToBeRemoved )
            .withRemovalSerialNo( lTrkInventorySerialNumberToBeRemoved ).withRemovalQuantity( 1 )
            .withRemovalReason( RefRemoveReasonKey.IMSCHD ).forPart( lPartNoKey ).build();

      // Act
      new ScheduledPartService().completeTaskParts( lTask, iHrKey, true, taskEndDate );

      JdbcUsageRecordDao usageRecordDao = new JdbcUsageRecordDao();
      List<UsageRecordEntity> removeUsageRecords =
            usageRecordDao.findAllByNaturalKey( lTrackedInventoryToBeRemoved );
      List<UsageRecordEntity> installUsageRecords =
            usageRecordDao.findAllByNaturalKey( lTrackedInventoryToBeInstalled );
      assertEquals( "Incorrect number of removal-usage-record records are created.",
            removeUsageRecords.size(), 1 );
      assertEquals( "Incorrect number of install-usage-record records are created.",
            installUsageRecords.size(), 1 );
      UsageAdjustmentId removalRecordId = removeUsageRecords.get( 0 ).getId();
      UsageAdjustmentId InstallRecordId = installUsageRecords.get( 0 ).getId();
      UsageDataDao usageDataDao = new JdbcUsageDataDao();
      UsageDataEntity removalUsageDataEntityOfCycle = usageDataDao
            .findByNaturalKey( removalRecordId, lTrackedInventoryToBeRemoved, DataTypeKey.CYCLES );
      UsageDataEntity removalUsageDataEntityOfHours = usageDataDao
            .findByNaturalKey( removalRecordId, lTrackedInventoryToBeRemoved, DataTypeKey.HOURS );
      UsageDataEntity installUsageDataEntityOfCycle = usageDataDao.findByNaturalKey(
            InstallRecordId, lTrackedInventoryToBeInstalled, DataTypeKey.CYCLES );
      UsageDataEntity installUsageDataEntityOfHours = usageDataDao
            .findByNaturalKey( InstallRecordId, lTrackedInventoryToBeInstalled, DataTypeKey.HOURS );

      assertEquals( "Removal usage data of cycle tsn is not correct.",
            removalUsageDataEntityOfCycle.getTsn(), new BigDecimal( 28 ) );
      assertEquals( "Removal usage data of cycle tsn delta is not correct.",
            removalUsageDataEntityOfCycle.getTsnDelta(), new BigDecimal( 18 ) );
      assertEquals( "Removal usage data of hours tsn is not correct.",
            removalUsageDataEntityOfHours.getTsn(), new BigDecimal( 280 ) );
      assertEquals( "Removal usage data of hours tsn delta is not correct.",
            removalUsageDataEntityOfHours.getTsnDelta(), new BigDecimal( 180 ) );
      assertEquals( "Install usage data of cycle tsn is not correct.",
            installUsageDataEntityOfCycle.getTsn(), new BigDecimal( 2 ) );
      assertEquals( "Install usage data of cycle tsn delta is not correct.",
            installUsageDataEntityOfCycle.getTsnDelta(), new BigDecimal( -18 ) );
      assertEquals( "Install usage data of hours tsn is not correct.",
            installUsageDataEntityOfHours.getTsn(), new BigDecimal( 20 ) );
      assertEquals( "Install usage data of hours tsn delta is not correct.",
            installUsageDataEntityOfHours.getTsnDelta(), new BigDecimal( -180 ) );
   }


   /*
    * Set up data used by all tests
    */
   @Before
   public void setup() {
      // Set up a user
      iHrKey = Domain.createHumanResource();
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iHrKey ) );

      // Set up a timeline
      iCurrentDate = new Date();
      iYesterday = DateUtils.addDays( iCurrentDate, -1 );

      // Set up config parms
      iUserId = OrgHr.findByPrimaryKey( iHrKey ).getUserId();
      UserParametersFake lUserParms = new UserParametersFake( iUserId, "LOGIC" );
      UserParameters.setInstance( iUserId, "LOGIC", lUserParms );

      UserParametersStub lUserParametersStub =
            new UserParametersStub( iUserId, "SECURED_RESOURCE" );
      lUserParametersStub.setBoolean( "ACTION_ALLOW_CREATE_EDIT_INV_WITH_FUTURE_MANUFACT_DATE",
            false );
      UserParameters.setInstance( iUserId, "SECURED_RESOURCE", lUserParametersStub );
   }


   @After
   public void teardown() {
      SecurityIdentificationUtils.setInstance( null );
      UserParameters.setInstance( iUserId, "LOGIC", null );
      UserParameters.setInstance( iUserId, "SECURED_RESOURCE", null );
   }


   /*
    * Set up a requirement done yesterday
    */
   private TaskKey createReqDoneYesterday() {

      return Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aBuilder ) {
            aBuilder.setActualEndDate( iYesterday );
         }
      } );
   }


   /*
    * Set up a location
    */
   private LocationKey createLocation() {
      return Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aBuilder ) {
            aBuilder.setType( RefLocTypeKey.OPS );
         }
      } );

   }


   // Set up a tracked part and group
   private PartNoKey createTrkPart() {
      //
      return Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aPartBuilder ) {
            aPartBuilder.setInventoryClass( RefInvClassKey.TRK );
            aPartBuilder.setPartStatus( RefPartStatusKey.ACTV );
            aPartBuilder.setShortDescription( "part number description" );
         }
      } );
   }


   /*
    * Set up an aircraft based on an assembly, with a slot for a TRK part
    */
   private InventoryKey createAircraftWithSlotsForPartAtLocation( final PartNoKey aPart,
         final LocationKey aLocation, Map<DataTypeKey, BigDecimal> usages ) {

      // Set up an aircraft assembly
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.setCode( "ACFTASSY" );
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {

                        aBuilder.setCode( "RootSlot" );
                        aBuilder
                              .addConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                                 @Override
                                 public void configure( ConfigurationSlot aBuilder ) {

                                    aBuilder.setCode( "72" );
                                    aBuilder.setName( "SlotName" );
                                    aBuilder.setConfigurationSlotClass( RefBOMClassKey.TRK );
                                    aBuilder.addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setCode( "PGCode" );
                                          aPartGroup.setInventoryClass( RefInvClassKey.TRK );
                                          aPartGroup.addPart( aPart );
                                       }
                                    } );
                                 }
                              } );
                     }
                  } );
               }
            } );

      return Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setLocation( aLocation );
            aAircraft.setPart( Domain.createPart() );
            for ( Entry<DataTypeKey, BigDecimal> entry : usages.entrySet() ) {
               aAircraft.addUsage( entry.getKey(), entry.getValue() );
            }
         }
      } );

   }


   private InventoryKey createAircraftWithSlotsForPartAtLocation( final PartNoKey aPart,
         final LocationKey aLocation ) {
      return createAircraftWithSlotsForPartAtLocation( aPart, aLocation,
            new HashMap<DataTypeKey, BigDecimal>() );
   }


   /**
    *
    * Check that a list of inventories were properly added to INV_INSTALL and fail the test if they
    * weren't. Also check that no additional rows were added.
    *
    * @param aAssemblyKey
    *           The assembly that was installed or the subassembly that inventories were installed
    *           under
    * @param aHighestKey
    *           The highest inventory that the installation occurred under
    * @param aEventDate
    *           The date of the installation
    * @param aInventories
    *           A list of inventories that were installed
    */
   private void assertInvRowsAsExpected( InventoryKey aAssemblyKey, InventoryKey aHighestKey,
         Date aEventDate, InventoryKey... aInventories ) {

      for ( InventoryKey lInventoryKey : aInventories ) {
         DataSetArgument lArgs = new DataSetArgument();
         lArgs.add( lInventoryKey, INV_COLUMNS );
         QuerySet lQuerySet = QuerySetFactory.getInstance().executeQuery( INV_INSTALL_COLUMNS,
               "inv_install", lArgs );

         if ( !lQuerySet.next() ) {
            fail( "Expected to find in inv_attach inv_no_id = "
                  .concat( lInventoryKey.toValueString() ) );
         }

         InventoryKey lResultInvKey = lQuerySet.getKey( InventoryKey.class, INV_COLUMNS );
         assertEquals( "Inserted inv_no_id not as expected", lInventoryKey, lResultInvKey );

         // The assembly key of a component on an installed assembly is the assembly. The assembly
         // key of an installed assembly is the highest inventory
         InventoryKey lResultAssmblInvKey =
               lQuerySet.getKey( InventoryKey.class, ASSMBL_INV_COLUMNS );
         InventoryKey lExpectedAssmlbInvKey =
               ( lInventoryKey == aAssemblyKey ? aHighestKey : aAssemblyKey );
         assertEquals( "Inserted assmbl_inv_no_id not as expected for inv_no_id = ".concat(
               lInventoryKey.toValueString() ), lExpectedAssmlbInvKey, lResultAssmblInvKey );

         InventoryKey lHInvKeyResult = lQuerySet.getKey( InventoryKey.class, H_INV_COLUMNS );
         assertEquals( "Inserted h_inv_no_id not as expected for inv_no_id = "
               .concat( lInventoryKey.toValueString() ), aHighestKey, lHInvKeyResult );

         assertEquals( "Inserted date incorrect", DateUtils.round( aEventDate, Calendar.MINUTE ),
               DateUtils.round( lQuerySet.getDate( "event_dt" ), Calendar.MINUTE ) );

      } ;

      // Assert no other rows were added
      assertEquals( "Incorrect number of rows were added", aInventories.length,
            QuerySetFactory.getInstance().executeQueryTable( "inv_install", new DataSetArgument() )
                  .getRowCount() );
   }

}
