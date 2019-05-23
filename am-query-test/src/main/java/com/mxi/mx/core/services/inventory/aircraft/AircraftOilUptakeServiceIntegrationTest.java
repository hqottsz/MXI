package com.mxi.mx.core.services.inventory.aircraft;

import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.common.trigger.TriggerFactoryStub;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.EventLocationKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.InventoryParmDataKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.StageKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.inventory.aircraft.model.RecordOilUptakeTO;
import com.mxi.mx.core.services.inventory.aircraft.model.RecordOilUptakeTO.RecordOilUptakeInventory;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtLocTable;
import com.mxi.mx.core.table.evt.EvtStageDao;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvParmData;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.trigger.MxCoreTriggerType;
import com.mxi.mx.core.trigger.ordernumber.MxOrderNumberGenerator;
import com.mxi.mx.core.unittest.table.inv.InvInv;
import com.mxi.mx.repository.workpackage.WorkPackageRepository;


@RunWith( BlockJUnit4ClassRunner.class )
public class AircraftOilUptakeServiceIntegrationTest {

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   // Test Data
   private static final UUID AIRCRAFT_ONE_ALT_ID =
         UUID.fromString( "12345678-90AB-CDEF-1234-567890ABCDEF" );
   private static final UUID AIRCRAFT_TWO_ALT_ID =
         UUID.fromString( "09876543-21AB-CDEF-1234-567890ABCDEF" );
   private static final UUID AIRCRAFT_THREE_ALT_ID =
         UUID.fromString( "19876543-21AB-CDEF-1234-567890ABCDEF" );
   private static final UUID AIRCRAFT_FOUR_ALT_ID =
         UUID.fromString( "29876543-21AB-CDEF-1234-567890ABCDEF" );
   private static final UUID ENGINE_1_ALT_ID_AIRCRAFT_1 =
         UUID.fromString( "22345678-90AB-CDEF-1234-567890ABCDEF" );
   private static final UUID ENGINE_2_ALT_ID_AIRCRAFT_1 =
         UUID.fromString( "32345678-90AB-CDEF-1234-567890ABCDEF" );
   private static final UUID ENGINE_1_ALT_ID_AIRCRAFT_2 =
         UUID.fromString( "42345678-90AB-CDEF-1234-567890ABCDEF" );
   private static final UUID ENGINE_1_ALT_ID_AIRCRAFT_3 =
         UUID.fromString( "52345678-90AB-CDEF-1234-567890ABCDEF" );
   private static final UUID ENGINE_1_ALT_ID_AIRCRAFT_4 =
         UUID.fromString( "62345678-90AB-CDEF-1234-567890ABCDEF" );

   private static final DataTypeKey DATA_TYPE_KEY = new DataTypeKey( "4650:10004" );
   private static final String NOTES = "Test oil uptake recording notes.";
   private static final String EMPTY_NOTE = "";
   private static final String EXPECTED_TASK_NAME = "Oil Uptake Recording";
   private static final InventoryKey AIRCRAFT = new InventoryKey( "4650:1" );
   private static final LocationKey LINE_LOCATION = new LocationKey( "4650:2" );
   private static final LocationKey LINE_UNDER_COMPANY = new LocationKey( "4650:5" );

   private HumanResourceKey iHumanResourceKey;
   private WorkPackageRepository workPackageRepository;

   // Object under test
   private AircraftOilUptakeService iAircraftOilUptakeService;


   @Before
   public void setUp() {

      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );

      MxOrderNumberGenerator lOrderNumberGenerator = mockOrderNumberGenerator();

      Map<String, Object> lTriggerMap = new HashMap<String, Object>( 1 );
      {
         lTriggerMap.put( MxCoreTriggerType.WO_NUM_GEN.toString(), lOrderNumberGenerator );
      }

      TriggerFactory lTriggerFactoryStub = new TriggerFactoryStub( lTriggerMap );
      TriggerFactory.setInstance( lTriggerFactoryStub );

      iHumanResourceKey = new HumanResourceDomainBuilder().build();

      UserParameters.setInstance( iHumanResourceKey.getId(), "LOGIC",
            new UserParametersFake( iHumanResourceKey.getId(), "LOGIC" ) );
      SecurityIdentificationUtils
            .setInstance( new SecurityIdentificationStub( iHumanResourceKey ) );
      iAircraftOilUptakeService = new AircraftOilUptakeService();
      workPackageRepository = InjectorContainer.get().getInstance( WorkPackageRepository.class );
   }


   @Test
   public void recordOilUptake_createsTaskWithOilUptakeMeasurements_completeAircraft()
         throws Throwable {

      List<RecordOilUptakeInventory> lUptakes = Arrays.asList(
            new RecordOilUptakeInventory( ENGINE_1_ALT_ID_AIRCRAFT_1, new BigDecimal( "2.25" ) ),
            new RecordOilUptakeInventory( ENGINE_2_ALT_ID_AIRCRAFT_1, new BigDecimal( "3.75" ) ) );

      TaskKey lTask = iAircraftOilUptakeService
            .recordOilUptake( buildRecordOilUptakeTO( AIRCRAFT_ONE_ALT_ID, lUptakes, NOTES ) );

      assertCorrectTaskAndWorkPackageHandling( lTask );
      assertTrue( String.format(
            "Expected to see a history note %s added for the task, but did not find one!", NOTES ),
            isHistoryNoteAdded( lTask ) );

      assertEquals( lUptakes.size(), getMeasurementCount( lTask ) );

      lUptakes.forEach( aUptake -> assertMeasurementIsCorrect( lTask, aUptake ) );

   }


   @Test
   public void recordOilUptake_createsTaskWithOilUptakeMeasurements_aircraftWithHoles()
         throws Throwable {

      List<RecordOilUptakeInventory> lUptakes = Arrays.asList(
            new RecordOilUptakeInventory( ENGINE_1_ALT_ID_AIRCRAFT_2, new BigDecimal( "2.25" ) ) );

      TaskKey lTask = iAircraftOilUptakeService
            .recordOilUptake( buildRecordOilUptakeTO( AIRCRAFT_TWO_ALT_ID, lUptakes, NOTES ) );

      assertCorrectTaskAndWorkPackageHandling( lTask );
      assertTrue( String.format(
            "Expected to see a history note %s added for the task, but did not find one!", NOTES ),
            isHistoryNoteAdded( lTask ) );

      assertEquals( lUptakes.size(), getMeasurementCount( lTask ) );

      lUptakes.forEach( aUptake -> assertMeasurementIsCorrect( lTask, aUptake ) );
   }


   @Test
   public void recordOilUptake_doesNotAddBlankStageHistoryNote() throws Throwable {

      List<RecordOilUptakeInventory> lUptakes = Arrays.asList(
            new RecordOilUptakeInventory( ENGINE_1_ALT_ID_AIRCRAFT_1, new BigDecimal( "1" ) ) );

      TaskKey lTask = iAircraftOilUptakeService
            .recordOilUptake( buildRecordOilUptakeTO( AIRCRAFT_ONE_ALT_ID, lUptakes, EMPTY_NOTE ) );

      assertFalse( "A history note was added to the task, however empty uptake notes should not "
            + "generate a stage history note.", isHistoryNoteAdded( lTask ) );

   }


   @Test
   public void recordOilUptake_createTaskAndAddToExistingWorkPackage() throws Throwable {

      List<RecordOilUptakeInventory> lUptakes = Arrays.asList(
            new RecordOilUptakeInventory( ENGINE_1_ALT_ID_AIRCRAFT_1, new BigDecimal( "2.25" ) ),
            new RecordOilUptakeInventory( ENGINE_2_ALT_ID_AIRCRAFT_1, new BigDecimal( "3.75" ) ) );

      TaskKey lWorkPackageKey = createWorkPackage( RefEventStatusKey.IN_WORK );

      TaskKey lTask = iAircraftOilUptakeService
            .recordOilUptake( buildRecordOilUptakeTO( AIRCRAFT_ONE_ALT_ID, lUptakes, NOTES ) );

      assertTaskInWorkPackage( lTask, lWorkPackageKey );
      assertTaskCompleted( lTask );
   }


   @Test
   public void recordOilUptake_createQuickCheck_underAirportSupplyLocation() throws Throwable {

      List<RecordOilUptakeInventory> uptakes = Arrays.asList(
            new RecordOilUptakeInventory( ENGINE_1_ALT_ID_AIRCRAFT_1, new BigDecimal( "2" ) ),
            new RecordOilUptakeInventory( ENGINE_2_ALT_ID_AIRCRAFT_1, new BigDecimal( "3" ) ) );

      iAircraftOilUptakeService
            .recordOilUptake( buildRecordOilUptakeTO( AIRCRAFT_ONE_ALT_ID, uptakes, NOTES ) );

      assertWorkPackageLocation( AIRCRAFT_ONE_ALT_ID, LINE_LOCATION );
   }


   @Test
   public void recordOilUptake_createQuickCheck_underCompanySupplyLocation() throws Throwable {

      List<RecordOilUptakeInventory> uptakes = Arrays.asList(
            new RecordOilUptakeInventory( ENGINE_1_ALT_ID_AIRCRAFT_3, new BigDecimal( "3" ) ) );

      iAircraftOilUptakeService
            .recordOilUptake( buildRecordOilUptakeTO( AIRCRAFT_THREE_ALT_ID, uptakes, NOTES ) );

      assertWorkPackageLocation( AIRCRAFT_THREE_ALT_ID, LINE_UNDER_COMPANY );

   }


   @Test( expected = MxException.class )
   public void recordOilUptake_createQuickCheck_noCompanySupplyFound() throws Throwable {
      List<RecordOilUptakeInventory> uptakes = Arrays.asList(
            new RecordOilUptakeInventory( ENGINE_1_ALT_ID_AIRCRAFT_4, new BigDecimal( "4" ) ) );

      iAircraftOilUptakeService
            .recordOilUptake( buildRecordOilUptakeTO( AIRCRAFT_FOUR_ALT_ID, uptakes, NOTES ) );
   }


   private void assertWorkPackageLocation( UUID aircraftUUID, LocationKey expectedLocation ) {

      Optional<com.mxi.mx.domain.maintenance.WorkPackage> completedWorkPackage =
            workPackageRepository.find( aircraftUUID, RefEventStatusKey.COMPLETE );

      if ( !completedWorkPackage.isPresent() ) {
         fail( "No completed work package containing the record oil uptake task was found." );
      }

      EventKey workpackageEventKey = completedWorkPackage.get().getKey().getEventKey();

      /*-----------------------------------------
      - get the work package location from evt_loc
      ------------------------------------------*/
      DataSetArgument args = new DataSetArgument();
      args.add( workpackageEventKey.getEventKey(), EvtEventDao.ColumnName.EVENT_DB_ID.name(),
            EvtEventDao.ColumnName.EVENT_ID.name() );

      QuerySet querySet =
            QuerySetFactory.getInstance().executeQueryTable( EventLocationKey.TABLE_NAME, args );

      if ( !querySet.first() ) {
         fail( "The work package location was not found." );
      }

      // get the work package location
      LocationKey workPackageLocationKey =
            new LocationKey( querySet.getInt( EvtLocTable.ColumnName.LOC_DB_ID.name() ),
                  querySet.getInt( EvtLocTable.ColumnName.LOC_ID.name() ) );

      assertEquals( expectedLocation, workPackageLocationKey );
   }


   private boolean isHistoryNoteAdded( TaskKey aTaskKey ) {

      DataSetArgument lArgs = new DataSetArgument();
      {
         EventKey lEventKey = aTaskKey.getEventKey();
         lArgs.add( lEventKey, EvtStageDao.COL_EVENT_DB_ID, EvtStageDao.COL_EVENT_ID );
         lArgs.add( iHumanResourceKey, EvtStageDao.COL_HR_DB_ID, EvtStageDao.COL_HR_ID );
         lArgs.add( EvtStageDao.COL_STAGE_NOTE, NOTES );
      }

      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( StageKey.TABLE_NAME, lArgs );
      return !lQs.isEmpty();
   }


   private void assertTaskInWorkPackage( TaskKey aTask, TaskKey aWorkPackage ) {
      DataSetArgument lArgs = new DataSetArgument();
      {
         lArgs.add( aTask.getEventKey(), EvtEventDao.ColumnName.EVENT_DB_ID.name(),
               EvtEventDao.ColumnName.EVENT_ID.name() );
         lArgs.add( aWorkPackage, EvtEventDao.ColumnName.H_EVENT_DB_ID.name(),
               EvtEventDao.ColumnName.H_EVENT_ID.name() );
      }

      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( EventKey.TABLE_NAME, lArgs );

      if ( !lQs.first() ) {
         fail( "The task was not assigned to the correct work package." );
      }
   }


   private void assertTaskCompleted( TaskKey aTask ) {

      DataSetArgument lArgs = new DataSetArgument();

      {
         lArgs.add( aTask.getEventKey(), EvtEventDao.ColumnName.EVENT_DB_ID.name(),
               EvtEventDao.ColumnName.EVENT_ID.name() );
      }
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( EventKey.TABLE_NAME, lArgs );

      if ( !lQs.first() ) {
         fail( "No task event could be found linked to the record oil uptake task created." );
      }

      // assert task name is correct
      assertThat( lQs.getString( EvtEventDao.ColumnName.EVENT_SDESC.name() ),
            equalToIgnoringCase( EXPECTED_TASK_NAME ) );

      // assert task is complete
      assertThat( lQs.getString( EvtEventDao.ColumnName.EVENT_STATUS_CD.name() ),
            equalToIgnoringCase( RefEventStatusKey.COMPLETE.getCd() ) );;
   }


   /**
    * asserts that:
    * <ul>
    * <li>task is created</li> *
    * <li>task name is correct</li>
    * <li>task is complete</li>
    * <li>task belongs to a work package</li>
    * <li>work package is complete</li>
    * </ul>
    *
    * @param aTaskKey
    */
   private void assertCorrectTaskAndWorkPackageHandling( TaskKey aTaskKey ) {

      assertNotNull( aTaskKey );
      assertTaskCompleted( aTaskKey );

      /*-----------------------------------------
      - get the task's data from sched_stask
      ------------------------------------------*/
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( aTaskKey.getEventKey(), SchedStaskDao.ColumnName.SCHED_DB_ID.name(),
            SchedStaskDao.ColumnName.SCHED_ID.name() );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( TaskKey.TABLE_NAME, lArgs );

      // assert task data was found
      if ( !lQs.first() ) {
         fail( "No scheduled task data could be found linked to the record oil uptake task." );
      }

      // get the work package id from WO_ID
      EventKey lWPEventKey = new EventKey( lQs.getInt( SchedStaskDao.ColumnName.WO_DB_ID.name() ),
            lQs.getInt( SchedStaskDao.ColumnName.WO_ID.name() ) );

      // assert work package id was found
      assertThat( lWPEventKey, notNullValue() );

      /*------------------------------------------
      - get the work package's data from evt_event
      --------------------------------------------*/
      lArgs.clear();
      lArgs.add( lWPEventKey, EvtEventDao.ColumnName.EVENT_DB_ID.name(),
            EvtEventDao.ColumnName.EVENT_ID.name() );
      lQs = QuerySetFactory.getInstance().executeQueryTable( EventKey.TABLE_NAME, lArgs );

      // assert work package data was found
      if ( !lQs.first() ) {
         fail( "No work package event information could be found." );
      }

      // assert work package is complete
      assertThat( lQs.getString( EvtEventDao.ColumnName.EVENT_STATUS_CD.name() ),
            equalToIgnoringCase( RefEventStatusKey.COMPLETE.getCd() ) );
   }


   private int getMeasurementCount( TaskKey aTaskKey ) {

      DataSetArgument lArgs = new DataSetArgument();
      {
         EventKey lEventKey = aTaskKey.getEventKey();
         lArgs.add( lEventKey, InvParmData.ColumnName.EVENT_DB_ID.name(),
               InvParmData.ColumnName.EVENT_ID.name() );
      }

      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQueryTable( InventoryParmDataKey.TABLE_NAME, lArgs );

      return lQs.getRowCount();
   }


   private void assertMeasurementIsCorrect( TaskKey aTaskKey, RecordOilUptakeInventory aUptake ) {
      InvInvTable lInv = InvInv.findByAltId( aUptake.getInventoryId() );
      DataSetArgument lArgs = new DataSetArgument();
      {
         EventKey lEventKey = aTaskKey.getEventKey();
         lArgs.add( lEventKey, InvParmData.ColumnName.EVENT_DB_ID.name(),
               InvParmData.ColumnName.EVENT_ID.name() );
         lArgs.add( lInv.getPk(), InvParmData.ColumnName.INV_NO_DB_ID.name(),
               InvParmData.ColumnName.INV_NO_ID.name() );
         lArgs.add( DATA_TYPE_KEY, InvParmData.ColumnName.DATA_TYPE_DB_ID.name(),
               InvParmData.ColumnName.DATA_TYPE_ID.name() );
      }

      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQueryTable( InventoryParmDataKey.TABLE_NAME, lArgs );

      assertTrue( "Created task is missing the measurement for " + aUptake,
            lQs.first() && lQs.getRowCount() == 1 );

      assertEquals( aUptake.getOilUptakeAmount(),
            lQs.getBigDecimal( InvParmData.ColumnName.PARM_QT.name() ) );

   }


   private RecordOilUptakeTO buildRecordOilUptakeTO( UUID aAircraftUUID,
         List<RecordOilUptakeInventory> aUptakeValues, String aNotes ) {

      return new RecordOilUptakeTO( aAircraftUUID, new Date(), iHumanResourceKey, aNotes,
            aUptakeValues );
   }


   private TaskKey createWorkPackage( RefEventStatusKey aStatusKey ) {
      TaskKey lWorkPackageKeyKey =
            Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

               @Override
               public void configure( WorkPackage aWorkPackage ) {
                  aWorkPackage.setAircraft( AIRCRAFT );
                  aWorkPackage.setStatus( aStatusKey );
                  aWorkPackage.setLocation( Domain.createLocation() );
               }
            } );

      return lWorkPackageKeyKey;
   }


   private MxOrderNumberGenerator mockOrderNumberGenerator() {
      return new MxOrderNumberGenerator() {

         @Override
         public String getRepairOrderNumber( TaskKey aTaskKey ) {
            throw new UnsupportedOperationException();
         }


         @Override
         public String getWorkOrderNumber( TaskKey aTaskKey ) {
            return ( "WO - " + aTaskKey.getId() );
         }

      };
   }
}
