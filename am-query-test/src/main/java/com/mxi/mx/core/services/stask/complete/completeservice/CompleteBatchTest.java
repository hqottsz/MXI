package com.mxi.mx.core.services.stask.complete.completeservice;

import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static com.mxi.mx.core.key.RefDomainTypeKey.USAGE_PARM;
import static com.mxi.mx.core.key.RefEventStatusKey.ACTV;
import static com.mxi.mx.core.key.RefEventStatusKey.IN_WORK;
import static com.mxi.mx.core.key.RefTaskClassKey.RO;
import static com.mxi.mx.core.key.RefTaskMustRemoveKey.OFFPARENT;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.ibm.icu.impl.Assert;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.CalculatedUsageParameter;
import com.mxi.am.domain.ComponentWorkPackage;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.EngineAssembly;
import com.mxi.am.domain.Flight;
import com.mxi.am.domain.LabourSkill;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.Part;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.PartRequirement;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventInventoryKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.MimPartNumDataKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDepActionKey;
import com.mxi.mx.core.key.RefUsgSnapshotSrcTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.services.stask.complete.CompleteService;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.core.table.eqp.EqpAssmblPos;
import com.mxi.mx.core.table.eqp.EqpBomPart;
import com.mxi.mx.core.table.evt.EvtEvent;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.evt.EvtInvUsageTable;
import com.mxi.mx.core.table.evt.JdbcEvtEventDao;
import com.mxi.mx.core.table.inv.InvCurrUsage;
import com.mxi.mx.core.table.mim.MimDataType;
import com.mxi.mx.core.table.mim.MimPartNumData;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.sched.JdbcSchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.unittest.table.stask.SchedStaskUtil;


/**
 * Tests for {@linkplain CompleteService#completeBatch}
 *
 */
public class CompleteBatchTest {

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private static final String CALCULATION = "CALCULATION";
   private static final String CALCULATION_CONSTANT = "CONSTANT";
   private static final String CALC_USAGE_PARM_CODE = "CALC_USAGE_PARM_CODE";
   private static final String LOW_THRUST_RATING_CYCLE = "LOW_THRUST_RATING_CYCLE";
   private static final String HIGH_THRUST_RATING_CYCLE = "HIGH_THRUST_RATING_CYCLE";
   private static final BigDecimal CALCULATION_CONSTANT_VALUE = BigDecimal.valueOf( 2.0 );
   private static final String TRK_SERIALNO = "TRK1";
   private static final String ANOTHER_TRK_SERIALNO = "TRK2";
   private static final String LATEST_FLIGHT = "LATEST_FLIGHT";
   private static final String EARLIER_FLIGHT = "EARLIER_FLIGHT";
   private static final String TRK = "TRK";
   private static final String TRK_CONFIGSLOT_CODE = "TRK_CD";
   private static final String TRK_PART_GROUP_CODE = "TRK_PG_CD";
   private static final String TRK_POSITION_CD = "TRK_POS_CD";
   private static final String ENGINE_ASSEMBLY_CODE = "ENGASSY";
   private static final String TRK_CHILD_TRK_CONFIG_SLOT_CODE = "CHILDTRK";
   private static final String LABOUR_SKILL_CODE_AET = "AET";
   private static final String LABOUR_SKILL_CODE_INSP = "INSP";
   private static final String ASSEMBLY_CODE = "A320";

   private static final String ROOT_CD = "ROOT_CD";
   private static final String LOGIC = "LOGIC";

   private static final boolean WARNINGS_NOT_APPROVED = false;
   private static final Date COMPLETION_DATE = new Date();

   private UserParameters iOrigUserParameters;
   private HumanResourceKey iHR;
   private int iUserId;

   private HumanResourceKey iAuthorizingHr;

   private static final Date LATEST_FLIGHT_ARRIVAL_DATE = new Date();
   private static final BigDecimal FLIGHT_HOURS_DELTA = BigDecimal.valueOf( 2.0 );
   private static final BigDecimal FLIGHT_CYCLES_DELTA = BigDecimal.ONE;
   private static final Date LATEST_FLIGHT_DEPARTURE_DATE =
         DateUtils.addHours( LATEST_FLIGHT_ARRIVAL_DATE, -FLIGHT_HOURS_DELTA.intValue() );
   private static final Date EARLIER_FLIGHT_ARRIVAL_DATE =
         DateUtils.addDays( LATEST_FLIGHT_DEPARTURE_DATE, -2 );
   private static final Date EARLIER_FLIGHT_DEPARTURE_DATE =
         DateUtils.addHours( EARLIER_FLIGHT_ARRIVAL_DATE, -FLIGHT_HOURS_DELTA.intValue() );


   @Before
   public void setUp() {
      iAuthorizingHr = new HumanResourceDomainBuilder().build();

      GlobalParameters lGlobalParametersFake = new GlobalParametersFake( LOGIC );

      // Creating a component work package has logic to automatically created labour rows based on
      // the labour skills provided by the BLANK_RO_SIGNATURE config parm. By default those are
      // "AET" and "INSP", unfortunately both of those are 10-level labour skills
      // (ref_labour_skill) and do not exist in the am-query-test DB.
      lGlobalParametersFake.setString( "BLANK_RO_SIGNATURE", "" );

      // The UserParametersFake is used because the user parameter
      // ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP need to have a value.
      GlobalParameters.setInstance( LOGIC, lGlobalParametersFake );

      int lUserId = OrgHr.findByPrimaryKey( iAuthorizingHr ).getUserId();
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iAuthorizingHr ) );
      UserParametersFake lUserParms = new UserParametersFake( lUserId, "LOGIC" );
      lUserParms.setProperty( "HOLE_TOBE_RMVD_EXISTS", "INFO" );
      UserParameters.setInstance( lUserId, "LOGIC", lUserParms );

   }


   @After
   public void after() {
      UserParameters.setInstance( iUserId, LOGIC, iOrigUserParameters );
   }


   /**
    *
    * Description: It will adjust the current standard usage of a component that was removed via a
    * requirement assigned to a work package when that requirement is completed on a date after
    * which usage was recorded against the component.<br>
    *
    * The adjustment will equal the difference between the component's assembly inventory's current
    * usage and the usage snapshot of the work package<br>
    *
    * <pre>
    * Given an engine assembly and an engine based on this assembly
    * Given the engine is tracking a standard usage parameter and has current usage
    * Given a TRK attached to engine tracking the same standard usage parameter and has current
    * usage
    * Given the engine has usage adjustments
    * Given a started component work package against the engine with a usage snapshot
    * Given an active REQ against the engine with a removal part requirement for the TRK
    * Given the REQ is assigned to the component work package
    * When the REQ is completed as a batch on a date prior to engine usage adjustments
    * Then the standard parameters in current usage of the TRK is reduced by the difference between
    * the engine's current usage and the work package's usage snapshot
    * </pre>
    */
   @Test
   public void
         itAdjustsStandardUsageParmCurrentUsageOfComponentRemovdInReqAssigndToWPForReqCompletionDateBeforeHistoricalUsages()
               throws Exception {
      // createCalculationInDatabase() method should be called prior to any data setup in database
      // as the method performs an explicit roll-back followed by an implicit database commit
      createCalculationInDatabase();

      final Date lReqCompleteBatchDate = DateUtils.addDays( EARLIER_FLIGHT_DEPARTURE_DATE, -2 );

      final PartNoKey lTrkPartNo = createTRKPartNo();

      final AssemblyKey lEngineAssembly =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aEngineAssembly ) {
                  aEngineAssembly.setCode( ENGINE_ASSEMBLY_CODE );
                  aEngineAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootCs ) {
                              aRootCs.setConfigurationSlotClass( RefBOMClassKey.ROOT );
                              aRootCs.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void configure( ConfigurationSlot aBuilder ) {

                                          aBuilder.setConfigurationSlotClass( RefBOMClassKey.TRK );
                                          aBuilder.setCode( TRK_CONFIGSLOT_CODE );
                                          aBuilder.addPartGroup(
                                                new DomainConfiguration<PartGroup>() {

                                                   @Override
                                                   public void configure( PartGroup aBuilder ) {
                                                      aBuilder.setInventoryClass(
                                                            RefInvClassKey.TRK );
                                                      aBuilder.setCode( TRK_PART_GROUP_CODE );
                                                      aBuilder.addPart( lTrkPartNo );
                                                   }
                                                } );
                                       }
                                    } );
                           }
                        } );
               }
            } );
      final PartGroupKey lTrkPartGroupKey =
            EqpBomPart.getBomPartKey( lEngineAssembly, TRK_PART_GROUP_CODE );

      ConfigSlotKey lEngineTrkConfigSlot =
            EqpAssmblBom.getBomItemKey( ENGINE_ASSEMBLY_CODE, TRK_CONFIGSLOT_CODE );

      final ConfigSlotPositionKey lTrkConfigSlotPositionKey = new ConfigSlotPositionKey(
            lEngineTrkConfigSlot, EqpAssmblPos.getFirstPosId( lEngineTrkConfigSlot ) );
      final String lTrkPosCd = EqpAssmblPos.getPosCd( lTrkConfigSlotPositionKey );

      final BigDecimal lCurrentHoursTsn = new BigDecimal( 100 );
      final BigDecimal lCurrentCyclesTsn = new BigDecimal( 20 );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setAssembly( lEngineAssembly );
            aBuilder.addUsage( HOURS, lCurrentHoursTsn );
            aBuilder.addUsage( CYCLES, lCurrentCyclesTsn );
         }
      } );

      final LocationKey lInventoryLocation = createLocation();

      final InventoryKey lTrkRemovedInReq =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.setPartGroup( lTrkPartGroupKey );
                  aBuilder.setParent( lEngine );
                  aBuilder.setSerialNumber( TRK_SERIALNO );
                  aBuilder.setLastKnownConfigSlot( lTrkConfigSlotPositionKey.getCd(),
                        TRK_CONFIGSLOT_CODE, lTrkPosCd );
                  aBuilder.setPartNumber( lTrkPartNo );
                  aBuilder.addUsage( HOURS, lCurrentHoursTsn );
                  aBuilder.addUsage( CYCLES, lCurrentCyclesTsn );
                  aBuilder.setLocation( lInventoryLocation );
               }
            } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( LATEST_FLIGHT );
            aBuilder.setArrivalDate( LATEST_FLIGHT_ARRIVAL_DATE );
            aBuilder.setDepartureDate( LATEST_FLIGHT_DEPARTURE_DATE );
            aBuilder.addUsage( lEngine, HOURS, lCurrentHoursTsn );
            aBuilder.addUsage( lEngine, CYCLES, lCurrentCyclesTsn );
         }
      } );

      final BigDecimal lEarlierFlightHoursTsn = lCurrentHoursTsn.subtract( FLIGHT_HOURS_DELTA );
      final BigDecimal lEarlierFlightCyclesTsn = lCurrentCyclesTsn.subtract( FLIGHT_CYCLES_DELTA );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( EARLIER_FLIGHT );
            aBuilder.setArrivalDate( EARLIER_FLIGHT_ARRIVAL_DATE );
            aBuilder.setDepartureDate( EARLIER_FLIGHT_DEPARTURE_DATE );
            aBuilder.addUsage( lEngine, HOURS, lEarlierFlightHoursTsn );
            aBuilder.addUsage( lEngine, CYCLES, lEarlierFlightCyclesTsn );
         }
      } );

      final TaskKey lReq = createRequirementWithPartRemovalRequest( lEngine, lTrkPartNo,
            lTrkPartGroupKey, lEngineAssembly, lTrkConfigSlotPositionKey, lTrkRemovedInReq );

      // Labour Skill with Code "AET" required by production for Blank_RO signature
      createLabourSkill( LABOUR_SKILL_CODE_AET );

      // Labour Skill with Code "INSP" required by production for Blank_RO signature
      createLabourSkill( LABOUR_SKILL_CODE_INSP );

      final BigDecimal lWorkPackageHoursTsn = lEarlierFlightHoursTsn.subtract( FLIGHT_HOURS_DELTA );
      final BigDecimal lWorkPackageCyclesTsn =
            lEarlierFlightCyclesTsn.subtract( FLIGHT_CYCLES_DELTA );

      Domain.createComponentWorkPackage( new DomainConfiguration<ComponentWorkPackage>() {

         @Override
         public void configure( ComponentWorkPackage aBuilder ) {
            aBuilder.setStatus( RefEventStatusKey.IN_WORK );
            aBuilder.assignTask( lReq );
            aBuilder.addSubAssembly( lEngine );
            aBuilder.addUsageSnapshot( lEngine, HOURS, lWorkPackageHoursTsn );
            aBuilder.addUsageSnapshot( lEngine, CYCLES, lWorkPackageCyclesTsn );
         }
      } );

      // When
      new CompleteService( lReq ).completeBatch( iAuthorizingHr, lReqCompleteBatchDate, true );

      // Then
      // Assertions for TRK inventory removed in Requirement
      BigDecimal lExpectedTrkRemovedInReqCurrentUsageHours =
            lCurrentHoursTsn.subtract( lCurrentHoursTsn.subtract( lWorkPackageHoursTsn ) );
      assertCurrentUsage( lTrkRemovedInReq, HOURS, lExpectedTrkRemovedInReqCurrentUsageHours );
      BigDecimal lExpectedTrkRemovedInReqCurrentUsageCycles =
            lCurrentCyclesTsn.subtract( ( lCurrentCyclesTsn ).subtract( lWorkPackageCyclesTsn ) );
      assertCurrentUsage( lTrkRemovedInReq, CYCLES, lExpectedTrkRemovedInReqCurrentUsageCycles );

      dropCalculationFromDatabase();

   }


   /**
    *
    * Verify that a component work package is created for a component when it is removed via a
    * removal part requirement and it has an active next shop visit task.
    *
    * <pre>
    *   Given an aircraft with an installed TRK component
    *     And the TRK component has an off-parent, next-shop-visit task
    *     And an adhoc task against the aircraft with a removal part requirement for the TRK component
    *     And the adhoc task is assigned to an in-work work package
    *    When the adhoc task is completed and the component removed
    *    Then a component work package is created for the detach component
    * </pre>
    *
    */
   @Test
   public void itCreatesComponentWorkPackageWhenComponentWithNextShopVisitTaskIsDetached()
         throws Exception {

      //
      // Given an aircraft with an installed TRK component.
      //
      // Note, RemovePartService.removePart() requires the component to have a config slot position
      // and a part group with appropriate inventory class.
      AssemblyKey lAcftAssy = createAircraftAssembly();

      // Retrieve the TRK config slot position and part group.
      // Track code is retrieve from parent configuration slot
      ConfigSlotKey lTrkConfigSlot = Domain.readSubConfigurationSlot(
            Domain.readRootConfigurationSlot( lAcftAssy ), TRK_CONFIGSLOT_CODE );
      ConfigSlotPositionKey lTrkConfigSlotPos = new ConfigSlotPositionKey( lTrkConfigSlot, 1 );

      // Part group code is retrieving for part group and assigned to configuration slot key
      PartGroupKey lPartGroup = Domain.readPartGroup( lTrkConfigSlot, TRK_PART_GROUP_CODE );

      // Various logic requires a part be set.
      PartNoKey lPart = Domain.createPart();

      InventoryKey lAcft = Domain.createAircraft();

      InventoryKey lTRK = createInstalledTrkComponent( lAcft, lPart, lPartGroup );

      //
      // Given the TRK component has an off-parent, next-shop-visit task (by definition, NSV tasks
      // have soft deadlines which must be set).
      //
      createNextShopVisitTask( lTRK );

      //
      // Given an adhoc task against the aircraft with a removal part requirement for the TRK
      // component.
      //
      TaskKey lAdhocTask = createAdHocTask( lAcft, lTrkConfigSlotPos, lTRK, lPart, lPartGroup );

      //
      // Given the adhoc task is assigned to an in-work work package.
      //
      createWorkPackage( lAdhocTask, lAcft );

      //
      // When the adhoc task is completed and the component removed.
      //
      new CompleteService( lAdhocTask ).completeBatch( iHR, COMPLETION_DATE,
            WARNINGS_NOT_APPROVED );

      //
      // Then a component work package is created for the TRK component.
      //
      List<TaskKey> lCompWps = getComponentWorkpackages( lTRK );
      assertThat( "Unexpected number of component work packages.", lCompWps.size(), is( 1 ) );

   }


   /**
    *
    * Verify the next-shop-visit requirement task is assigned to the created component work package
    * of the detach component when it is removed via a removal part requirement and it has an active
    * next shop visit task.
    *
    * <pre>
    *   Given an aircraft with an installed TRK component
    *     And the TRK component has an off-parent, next-shop-visit task
    *     And an adhoc task against the aircraft with a removal part requirement for the TRK component
    *     And the adhoc task is assigned to an in-work work package
    *    When the adhoc task is completed and the component removed
    *    Then the next-shop-visit requirement task is assigned to the created component work package of the detach component
    * </pre>
    *
    */
   @Test
   public void
         itAssignsNextShopVisitTaskIntoComponentWorkPackageWhenComponentWithNextShopVisitIsDetached()
               throws Exception {

      //
      // Given an aircraft with an installed TRK component.
      //
      AssemblyKey lAcftAssy = createAircraftAssembly();

      // Retrieve the TRK config slot position and part group.
      ConfigSlotKey lTrkConfigSlot = Domain.readSubConfigurationSlot(
            Domain.readRootConfigurationSlot( lAcftAssy ), TRK_CONFIGSLOT_CODE );
      ConfigSlotPositionKey lTrkConfigSlotPos = new ConfigSlotPositionKey( lTrkConfigSlot, 1 );

      PartGroupKey lPartGroup = Domain.readPartGroup( lTrkConfigSlot, TRK_PART_GROUP_CODE );

      // Various logic requires a part be set.
      PartNoKey lPart = Domain.createPart();

      InventoryKey lAcft = Domain.createAircraft();

      InventoryKey lTRK = createInstalledTrkComponent( lAcft, lPart, lPartGroup );

      //
      // Given the TRK component has an off-parent, next-shop-visit task (by definition, NSV tasks
      // have soft deadlines which must be set).
      //
      TaskKey lNextShopVisit = createNextShopVisitTask( lTRK );

      //
      // Given an adhoc task against the aircraft with a removal part requirement for the TRK
      // component.
      //
      TaskKey lAdhocTask = createAdHocTask( lAcft, lTrkConfigSlotPos, lTRK, lPart, lPartGroup );

      //
      // Given the adhoc task is assigned to an in-work work package.
      //
      createWorkPackage( lAdhocTask, lAcft );

      //
      // When the adhoc task is completed and the component removed.
      //
      new CompleteService( lAdhocTask ).completeBatch( iHR, COMPLETION_DATE,
            WARNINGS_NOT_APPROVED );

      //
      // Then the next-shop-visit requirement task is assigned to the created component work package
      // of the detach component
      //
      List<TaskKey> lCompWps = getComponentWorkpackages( lTRK );
      assertThat( "Unexpected number of component work packages.", lCompWps.size(), is( 1 ) );

      assertThat(
            "Unexpected highest event of the NSV task's event. It should be the component work package's event.",
            EvtEvent.findByPrimaryKey( lNextShopVisit.getEventKey() ).getHEventKey(),
            is( lCompWps.get( 0 ).getEventKey() ) );

   }


   /**
    *
    * Verify the next-shop-visit requirement task still has a soft deadline when it is removed via a
    * removal part requirement and it has an active next shop visit task.
    *
    * <pre>
    *   Given an aircraft with an installed TRK component
    *     And the TRK component has an off-parent, next-shop-visit task
    *     And an adhoc task against the aircraft with a removal part requirement for the TRK component
    *     And the adhoc task is assigned to an in-work work package
    *    When the adhoc task is completed and the component removed
    *    Then the next-shop-visit requirement task is still marked as having a soft deadline
    * </pre>
    *
    */
   @Test
   public void
         ItDoesNotModifyTheSoftDeadlineOfNextShopVisitTaskWhenComponentWithNextShopVisitIsDetached()
               throws Exception {

      //
      // Given an aircraft with an installed TRK component.
      //
      AssemblyKey lAcftAssy = createAircraftAssembly();

      // Retrieve the TRK config slot position and part group.
      ConfigSlotKey lTrkConfigSlot = Domain.readSubConfigurationSlot(
            Domain.readRootConfigurationSlot( lAcftAssy ), TRK_CONFIGSLOT_CODE );
      ConfigSlotPositionKey lTrkConfigSlotPos = new ConfigSlotPositionKey( lTrkConfigSlot, 1 );

      PartGroupKey lPartGroup = Domain.readPartGroup( lTrkConfigSlot, TRK_PART_GROUP_CODE );

      // Various logic requires a part be set.
      PartNoKey lPart = Domain.createPart();

      InventoryKey lAcft = Domain.createAircraft();

      InventoryKey lTRK = createInstalledTrkComponent( lAcft, lPart, lPartGroup );

      //
      // Given the TRK component has an off-parent, next-shop-visit task (by definition, NSV tasks
      // have soft deadlines which must be set).
      //
      TaskKey lNextShopVisit = createNextShopVisitTask( lTRK );

      //
      // Given an adhoc task against the aircraft with a removal part requirement for the TRK
      // component.
      //
      TaskKey lAdhocTask = createAdHocTask( lAcft, lTrkConfigSlotPos, lTRK, lPart, lPartGroup );

      //
      // Given the adhoc task is assigned to an in-work work package.
      //
      createWorkPackage( lAdhocTask, lAcft );

      //
      // When the adhoc task is completed and the component removed.
      //
      new CompleteService( lAdhocTask ).completeBatch( iHR, COMPLETION_DATE,
            WARNINGS_NOT_APPROVED );

      //
      // Then the next-shop-visit requirement task is still marked as having a soft deadline
      //
      assertThat(
            "Expecting the next-shop-visit requirement task to still be marked as having a soft deadline.",
            SchedStaskTable.findByPrimaryKey( lNextShopVisit ).isSoftDeadlineBool(), is( true ) );

   }


   private AssemblyKey createAircraftAssembly() {
      // Note, RemovePartService.removePart() requires the component to have a config slot position
      // and a part group with appropriate inventory class.
      return Domain.createAircraftAssembly( aAcftAssy -> {
         aAcftAssy.setCode( ASSEMBLY_CODE );
         aAcftAssy.setRootConfigurationSlot( aRootCs -> {
            aRootCs.setCode( ROOT_CD );
            aRootCs.addConfigurationSlot( aTrkCs -> {
               aTrkCs.setCode( TRK_CONFIGSLOT_CODE );
               aTrkCs.addPosition( TRK_POSITION_CD );
               aTrkCs.addPartGroup( aPartGroup -> {
                  aPartGroup.setCode( TRK_PART_GROUP_CODE );
                  aPartGroup.setInventoryClass( RefInvClassKey.TRK );
               } );
            } );
         } );
      } );
   }


   private InventoryKey createInstalledTrkComponent( InventoryKey aAircraft, PartNoKey aPart,
         PartGroupKey aPartGroup ) {
      return Domain.createTrackedInventory( aTrk -> {
         aTrk.setParent( aAircraft );

         // Note, the AttachableInventoryService.detachInventory() and other areas of the code
         // requires the part number to be set.
         aTrk.setPartNumber( aPart );

         // Note, the RemovedPartService.removeParts() requires the part group, serial number,
         // location, and config slot position be set.
         aTrk.setPartGroup( aPartGroup );
         aTrk.setSerialNumber( TRK_SERIALNO );
         aTrk.setLocation( Domain.createLocation() );
         aTrk.setLastKnownConfigSlot( ASSEMBLY_CODE, TRK_CONFIGSLOT_CODE, TRK_POSITION_CD );
      } );

   }


   private TaskKey createNextShopVisitTask( InventoryKey aTrk ) {
      return Domain.createRequirement( aNsvTask -> {
         aNsvTask.setInventory( aTrk );
         aNsvTask.setSoftDeadline( true );
         aNsvTask.setDefinition( Domain.createRequirementDefinition( aNsvDefn -> {
            aNsvDefn.setNextShopVisit( true );
            aNsvDefn.setMustRemove( OFFPARENT );
         } ) );
      } );

   }


   private void createWorkPackage( TaskKey aAdhocTask, InventoryKey aircraft ) {
      Domain.createWorkPackage( aWp -> {
         aWp.addTask( aAdhocTask );
         aWp.setStatus( IN_WORK );
         aWp.setAircraft( aircraft );
      } );
   }


   private TaskKey createAdHocTask( InventoryKey aAircraft, ConfigSlotPositionKey aTrkConfigSlotPos,
         InventoryKey aTRK, PartNoKey aPart, PartGroupKey aPartGroup ) {

      return Domain.createAdhocTask( aAdhoc -> {
         aAdhoc.setInventory( aAircraft );
         aAdhoc.addPartRequirement( aPartRqmt -> {
            aPartRqmt.setPartRemovalRequest(
                  aPartRqmt.new PartRemovalRequest().withInventory( aTRK ).withPartNo( aPart ) );

            // Note, the RemovedPartService.removeParts() requires the config slot position and part
            // group be set.
            aPartRqmt.setConfigSlotPosition( aTrkConfigSlotPos );
            aPartRqmt.setPartGroup( aPartGroup );
         } );

         // Note, in order to complete the adhoc task it must have a status of active.
         aAdhoc.setStatus( ACTV );
      } );
   }


   private List<TaskKey> getComponentWorkpackages( InventoryKey aTRK ) {
      // Component work packages are a.k.a. Repair Orders (RO).
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( RO, "task_class_db_id", "task_class_cd" );
      lArgs.add( aTRK, "main_inv_no_db_id", "main_inv_no_id" );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", lArgs );

      List<TaskKey> lCompWps = new ArrayList<>();
      while ( lQs.next() ) {
         lCompWps.add( lQs.getKey( TaskKey.class, "sched_db_id", "sched_id" ) );
      }
      return lCompWps;
   }


   /**
    *
    * Description: It will adjust the current calculated usage of a component that was removed via a
    * requirement assigned to a work package when that requirement is completed on a date after
    * which usage was recorded against the component.<br>
    *
    * The adjustment will equal the difference between the component's assembly inventory's current
    * usage and the usage snapshot of the work package<br>
    *
    * <pre>
    * Given an engine assembly and an engine based on this assembly
    * Given the engine is tracking a standard usage parameter and has current usage
    * Given the engine is tracking a calculated usage parameter based on the standard usage
    * parameter
    * Given a TRK attached to the engine tracking the same standard & calculated usage parameter and
    * has current usage
    * Given the engine has usage adjustments
    * Given a started component work package against the engine with a usage snapshot
    * Given an active REQ against the engine with a removal part requirement for the TRK
    * Given the REQ is assigned to the component work package
    * When the REQ is completed as a batch on a date prior to engine usage adjustments
    * Then the calculated usage parameter in current usage of the TRK is reduced by the difference
    * between the engine's current usage and the work package's usage snapshot
    * </pre>
    */
   @Test
   public void
         itAdjustsCalculatedParmCurrentUsageOfComponentRemovdInReqAssigndToWPForReqCompletionDateBeforeHistoricalUsages()
               throws Exception {

      // createCalculationInDatabase() method should be called prior to any data setup in database
      // as the method performs an explicit roll-back followed by an implicit database commit
      createCalculationInDatabase();

      final Date lReqCompleteBatchDate = DateUtils.addDays( EARLIER_FLIGHT_DEPARTURE_DATE, -2 );

      final PartNoKey lTrkPartNo = createTRKPartNo();

      final AssemblyKey lEngineAssembly =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aEngineAssembly ) {
                  aEngineAssembly.setCode( ENGINE_ASSEMBLY_CODE );
                  aEngineAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootCs ) {
                              aRootCs.setConfigurationSlotClass( RefBOMClassKey.ROOT );
                              aRootCs.addCalculatedUsageParameter(
                                    new DomainConfiguration<CalculatedUsageParameter>() {

                                       @Override
                                       public void configure( CalculatedUsageParameter aBuilder ) {
                                          aBuilder.setCode( CALC_USAGE_PARM_CODE );
                                          aBuilder.setDatabaseCalculation( CALCULATION );
                                          aBuilder.setPrecisionQt( 2 );
                                          aBuilder.addConstant( CALCULATION_CONSTANT,
                                                CALCULATION_CONSTANT_VALUE );
                                          aBuilder.addParameter( DataTypeKey.CYCLES );
                                       }

                                    } );
                              aRootCs.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void configure( ConfigurationSlot aBuilder ) {

                                          aBuilder.setConfigurationSlotClass( RefBOMClassKey.TRK );
                                          aBuilder.setCode( TRK_CONFIGSLOT_CODE );
                                          aBuilder.addPartGroup(
                                                new DomainConfiguration<PartGroup>() {

                                                   @Override
                                                   public void configure( PartGroup aBuilder ) {
                                                      aBuilder.setInventoryClass(
                                                            RefInvClassKey.TRK );
                                                      aBuilder.setCode( TRK_PART_GROUP_CODE );
                                                      aBuilder.addPart( lTrkPartNo );
                                                   }
                                                } );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final PartGroupKey lTrkPartGroupKey =
            EqpBomPart.getBomPartKey( lEngineAssembly, TRK_PART_GROUP_CODE );

      ConfigSlotKey lEngineTrkConfigSlot =
            EqpAssmblBom.getBomItemKey( ENGINE_ASSEMBLY_CODE, TRK_CONFIGSLOT_CODE );

      final ConfigSlotPositionKey lTrkConfigSlotPositionKey = new ConfigSlotPositionKey(
            lEngineTrkConfigSlot, EqpAssmblPos.getFirstPosId( lEngineTrkConfigSlot ) );
      final String lTrkPosCd = EqpAssmblPos.getPosCd( lTrkConfigSlotPositionKey );

      final DataTypeKey lCalcUsageParm = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), CALC_USAGE_PARM_CODE );

      final BigDecimal lCurrentCyclesTsn = new BigDecimal( 20 );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setAssembly( lEngineAssembly );
            aBuilder.addUsage( CYCLES, lCurrentCyclesTsn );
            aBuilder.addUsage( lCalcUsageParm,
                  lCurrentCyclesTsn.multiply( CALCULATION_CONSTANT_VALUE ) );
         }
      } );

      final LocationKey lInventoryLocation = createLocation();

      final InventoryKey lTrkRemovedInReq =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.setPartGroup( lTrkPartGroupKey );
                  aBuilder.setParent( lEngine );
                  aBuilder.setSerialNumber( TRK_SERIALNO );
                  aBuilder.setLastKnownConfigSlot( lTrkConfigSlotPositionKey.getCd(),
                        TRK_CONFIGSLOT_CODE, lTrkPosCd );
                  aBuilder.setPartNumber( lTrkPartNo );
                  aBuilder.addUsage( CYCLES, lCurrentCyclesTsn );
                  aBuilder.addUsage( lCalcUsageParm,
                        lCurrentCyclesTsn.multiply( CALCULATION_CONSTANT_VALUE ) );
                  aBuilder.setLocation( lInventoryLocation );
               }
            } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( LATEST_FLIGHT );
            aBuilder.setArrivalDate( LATEST_FLIGHT_ARRIVAL_DATE );
            aBuilder.setDepartureDate( LATEST_FLIGHT_DEPARTURE_DATE );
            aBuilder.addUsage( lEngine, CYCLES, lCurrentCyclesTsn );
         }
      } );

      final BigDecimal lEarlierFlightCyclesTsn = lCurrentCyclesTsn.subtract( FLIGHT_CYCLES_DELTA );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( EARLIER_FLIGHT );
            aBuilder.setArrivalDate( EARLIER_FLIGHT_ARRIVAL_DATE );
            aBuilder.setDepartureDate( EARLIER_FLIGHT_DEPARTURE_DATE );
            aBuilder.addUsage( lEngine, CYCLES, lEarlierFlightCyclesTsn );
         }
      } );

      final TaskKey lReq = createRequirementWithPartRemovalRequest( lEngine, lTrkPartNo,
            lTrkPartGroupKey, lEngineAssembly, lTrkConfigSlotPositionKey, lTrkRemovedInReq );

      // Labour Skill with Code "AET" required by production for Blank_RO signature
      createLabourSkill( LABOUR_SKILL_CODE_AET );

      // Labour Skill with Code "INSP" required by production for Blank_RO signature
      createLabourSkill( LABOUR_SKILL_CODE_INSP );

      final BigDecimal lWorkPackageCyclesTsn =
            lEarlierFlightCyclesTsn.subtract( FLIGHT_CYCLES_DELTA );

      Domain.createComponentWorkPackage( new DomainConfiguration<ComponentWorkPackage>() {

         @Override
         public void configure( ComponentWorkPackage aBuilder ) {
            aBuilder.setStatus( RefEventStatusKey.IN_WORK );
            aBuilder.assignTask( lReq );
            aBuilder.addSubAssembly( lEngine );
            aBuilder.addUsageSnapshot( lEngine, CYCLES, lWorkPackageCyclesTsn );
         }
      } );

      // When
      new CompleteService( lReq ).completeBatch( iAuthorizingHr, lReqCompleteBatchDate, true );

      // Then
      // Assertions for TRK inventory removed in Requirement
      BigDecimal lExpectedTrkRemovedInReqCalcParmCurrentUsage =
            lCurrentCyclesTsn.subtract( ( lCurrentCyclesTsn ).subtract( lWorkPackageCyclesTsn ) )
                  .multiply( CALCULATION_CONSTANT_VALUE );
      assertCurrentUsage( lTrkRemovedInReq, lCalcUsageParm,
            lExpectedTrkRemovedInReqCalcParmCurrentUsage );

      dropCalculationFromDatabase();

   }


   /**
    *
    * Description: It will adjust the current ACC usage of a component that was removed via a
    * requirement assigned to a work package when that requirement is completed on a date after
    * which usage was recorded against the component.<br>
    *
    * The adjustment will equal the difference between the component's assembly inventory's current
    * usage and the usage snapshot of the work package<br>
    *
    * <pre>
    * Given two engine part numbers, each representing two different thrust ratings
    * Given an engine assembly and an engine based on it
    * Given two Assembly Specific Usage Parameters(ASUP), one for each part number, assigned to the
    * engine assembly and all sub-components
    * Given the engine is tracking the ASUPs and has current usage
    * Given a TRK attached to the engine tracking the same ASUPs and has current usage
    * Given the engine has usage adjustments
    * Given a started component work package against the engine with a usage snapshot
    * Given an active REQ against the engine with a removal part requirement for the TRK
    * Given the REQ is assigned to the component work package
    * When the REQ is completed as a batch on a date prior to engine usage adjustments
    * Then the ASUP in current usage of the TRK is reduced by the difference between the engine's
    * current usage and the work package's usage snapshot
    * </pre>
    */
   @Test
   public void
         itAdjustsAssemblySpecificUsageParmCurrentUsageOfComponentRemovdInReqAssigndToWPForReqCompletionDateBeforeHistoricalUsages()
               throws Exception {
      // createCalculationInDatabase() method should be called prior to any data setup in database
      // as the method performs an explicit roll-back followed by an implicit database commit
      createCalculationInDatabase();

      final Date lReqCompleteBatchDate = DateUtils.addDays( EARLIER_FLIGHT_DEPARTURE_DATE, -2 );

      final PartNoKey lLowThrustRatingEnginePartNo = createEnginePartNo();
      final PartNoKey lHighThrustRatingEnginePartNo = createEnginePartNo();

      final PartNoKey lTrkPartNo = createTRKPartNo();
      final AssemblyKey lEngineAssembly =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aEngineAssembly ) {
                  aEngineAssembly.setCode( ENGINE_ASSEMBLY_CODE );
                  aEngineAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootCs ) {
                              aRootCs.setConfigurationSlotClass( RefBOMClassKey.ROOT );;
                              aRootCs.addPartGroup( new DomainConfiguration<PartGroup>() {

                                 @Override
                                 public void configure( PartGroup aPartGroup ) {
                                    aPartGroup.addPart( lLowThrustRatingEnginePartNo );
                                    aPartGroup.addPart( lHighThrustRatingEnginePartNo );
                                 }
                              } );
                              aRootCs.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void configure( ConfigurationSlot aBuilder ) {

                                          aBuilder.setConfigurationSlotClass( RefBOMClassKey.TRK );
                                          aBuilder.setCode( TRK_CONFIGSLOT_CODE );
                                          aBuilder.addPartGroup(
                                                new DomainConfiguration<PartGroup>() {

                                                   @Override
                                                   public void configure( PartGroup aBuilder ) {
                                                      aBuilder.setInventoryClass(
                                                            RefInvClassKey.TRK );
                                                      aBuilder.setCode( TRK_PART_GROUP_CODE );
                                                      aBuilder.addPart( lTrkPartNo );
                                                   }
                                                } );
                                       }
                                    } );
                           }
                        } );
               }
            } );
      final DataTypeKey lLowThrustRatingCycle = createAccumulatedParm( LOW_THRUST_RATING_CYCLE,
            lEngineAssembly, lLowThrustRatingEnginePartNo, CYCLES );

      final DataTypeKey lHighThrustRatingCycle = createAccumulatedParm( HIGH_THRUST_RATING_CYCLE,
            lEngineAssembly, lHighThrustRatingEnginePartNo, CYCLES );

      final PartGroupKey lTrkPartGroupKey =
            EqpBomPart.getBomPartKey( lEngineAssembly, TRK_PART_GROUP_CODE );

      ConfigSlotKey lEngineTrkConfigSlot =
            EqpAssmblBom.getBomItemKey( ENGINE_ASSEMBLY_CODE, TRK_CONFIGSLOT_CODE );

      final ConfigSlotPositionKey lTrkConfigSlotPositionKey = new ConfigSlotPositionKey(
            lEngineTrkConfigSlot, EqpAssmblPos.getFirstPosId( lEngineTrkConfigSlot ) );
      final String lTrkPosCd = EqpAssmblPos.getPosCd( lTrkConfigSlotPositionKey );

      final BigDecimal lCurrentCyclesTsn = new BigDecimal( 20 );
      final BigDecimal llHighThrustRatingCycleTsn = lCurrentCyclesTsn.subtract( BigDecimal.TEN );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setAssembly( lEngineAssembly );
            aBuilder.setPartNumber( lLowThrustRatingEnginePartNo );
            aBuilder.addUsage( lLowThrustRatingCycle, lCurrentCyclesTsn );
            aBuilder.addUsage( lHighThrustRatingCycle, llHighThrustRatingCycleTsn );
         }
      } );

      // Required by the service layer code
      final LocationKey lLocation = createLocation();

      final InventoryKey lTrkRemovedInReq =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.setPartGroup( lTrkPartGroupKey );
                  aBuilder.setParent( lEngine );
                  aBuilder.setSerialNumber( TRK_SERIALNO );
                  aBuilder.setLastKnownConfigSlot( lTrkConfigSlotPositionKey.getCd(),
                        TRK_CONFIGSLOT_CODE, lTrkPosCd );
                  aBuilder.setPartNumber( lTrkPartNo );
                  aBuilder.addUsage( lLowThrustRatingCycle, lCurrentCyclesTsn );
                  aBuilder.addUsage( lHighThrustRatingCycle, llHighThrustRatingCycleTsn );
                  aBuilder.setLocation( lLocation );

               }
            } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( LATEST_FLIGHT );
            aBuilder.setArrivalDate( LATEST_FLIGHT_ARRIVAL_DATE );
            aBuilder.setDepartureDate( LATEST_FLIGHT_DEPARTURE_DATE );
            aBuilder.addUsage( lEngine, lLowThrustRatingCycle, lCurrentCyclesTsn );
            aBuilder.addUsage( lEngine, lHighThrustRatingCycle, llHighThrustRatingCycleTsn );
         }
      } );

      final BigDecimal lEarlierFlightCyclesTsn = lCurrentCyclesTsn.subtract( FLIGHT_CYCLES_DELTA );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( EARLIER_FLIGHT );
            aBuilder.setArrivalDate( EARLIER_FLIGHT_ARRIVAL_DATE );
            aBuilder.setDepartureDate( EARLIER_FLIGHT_DEPARTURE_DATE );
            aBuilder.addUsage( lEngine, lLowThrustRatingCycle, lEarlierFlightCyclesTsn );
            aBuilder.addUsage( lEngine, lHighThrustRatingCycle, llHighThrustRatingCycleTsn );
         }
      } );

      final TaskKey lReq = createRequirementWithPartRemovalRequest( lEngine, lTrkPartNo,
            lTrkPartGroupKey, lEngineAssembly, lTrkConfigSlotPositionKey, lTrkRemovedInReq );

      // Labour Skill with Code "AET" required by production for Blank_RO signature
      createLabourSkill( LABOUR_SKILL_CODE_AET );

      // Labour Skill with Code "INSP" required by production for Blank_RO signature
      createLabourSkill( LABOUR_SKILL_CODE_INSP );

      final BigDecimal lWorkPackageCyclesTsn =
            lEarlierFlightCyclesTsn.subtract( FLIGHT_CYCLES_DELTA );

      Domain.createComponentWorkPackage( new DomainConfiguration<ComponentWorkPackage>() {

         @Override
         public void configure( ComponentWorkPackage aBuilder ) {
            aBuilder.setStatus( RefEventStatusKey.IN_WORK );
            aBuilder.assignTask( lReq );
            aBuilder.addSubAssembly( lEngine );
            aBuilder.addUsageSnapshot( lEngine, lLowThrustRatingCycle, lWorkPackageCyclesTsn );
            aBuilder.addUsageSnapshot( lEngine, lHighThrustRatingCycle,
                  llHighThrustRatingCycleTsn );

         }
      } );

      // When
      new CompleteService( lReq ).completeBatch( iAuthorizingHr, lReqCompleteBatchDate, true );

      // Then
      // Assertions for TRK inventory removed in Requirement
      BigDecimal lExpectedTrkRemovedInReqLowThrustCyclesCurrentUsage =
            lCurrentCyclesTsn.subtract( ( lCurrentCyclesTsn ).subtract( lWorkPackageCyclesTsn ) );
      assertCurrentUsage( lTrkRemovedInReq, lLowThrustRatingCycle,
            lExpectedTrkRemovedInReqLowThrustCyclesCurrentUsage );
      assertCurrentUsage( lTrkRemovedInReq, lHighThrustRatingCycle, llHighThrustRatingCycleTsn );

      dropCalculationFromDatabase();

   }


   /**
    *
    * Description: It will adjust the current calculated usage of a sub-component of a component
    * that was removed via a requirement assigned to a work package when that requirement is
    * completed on a date after which usage was recorded against the component.<br>
    *
    * The adjustment will equal the difference between the component's assembly inventory's current
    * usage and the usage snapshot of the work package<br>
    *
    * <pre>
    * Given an engine assembly and an engine based on this assembly
    * Given the engine is tracking a standard usage parameter and has current usage
    * Given the engine is tracking a calculated usage parameter based on the standard usage
    * parameter
    * Given a TRK attached to the engine tracking the same standard & calculated usage parameter and
    * has current usage
    * Given a sub-component TRK attached to the TRK tracking the same standard & calculated usage
    * parameter and current usage
    * Given the engine has usage adjustments
    * Given a started component work package against the engine with a usage snapshot
    * Given an active REQ against the engine with a removal part requirement for the TRK
    * Given the REQ is assigned to the component work package
    * When the REQ is completed as a batch on a date prior to engine usage adjustments
    * Then the calculated usage parameter in current usage of the sub-component TRK is reduced by
    * the difference between the engine's current usage and the work package's usage snapshot
    * </pre>
    */
   @Test
   public void
         itAdjustsCalculatedParmCurrentUsageOfSubComponentRemovdInReqAssigndToWPForReqCompletionDateBeforeHistoricalUsages()
               throws Exception {

      // createCalculationInDatabase() method should be called prior to any data setup in database
      // as the method performs an explicit roll-back followed by an implicit database commit
      createCalculationInDatabase();

      final Date lReqCompleteBatchDate = DateUtils.addDays( EARLIER_FLIGHT_DEPARTURE_DATE, -2 );

      final PartNoKey lTrkPartNo = createTRKPartNo();

      final AssemblyKey lEngineAssembly =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aEngineAssembly ) {
                  aEngineAssembly.setCode( ENGINE_ASSEMBLY_CODE );
                  aEngineAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootCs ) {
                              aRootCs.setConfigurationSlotClass( RefBOMClassKey.ROOT );
                              aRootCs.addCalculatedUsageParameter(
                                    new DomainConfiguration<CalculatedUsageParameter>() {

                                       @Override
                                       public void configure( CalculatedUsageParameter aBuilder ) {
                                          aBuilder.setCode( CALC_USAGE_PARM_CODE );
                                          aBuilder.setDatabaseCalculation( CALCULATION );
                                          aBuilder.setPrecisionQt( 2 );
                                          aBuilder.addConstant( CALCULATION_CONSTANT,
                                                CALCULATION_CONSTANT_VALUE );
                                          aBuilder.addParameter( DataTypeKey.CYCLES );
                                       }
                                    } );
                              aRootCs.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void configure( ConfigurationSlot aBuilder ) {

                                          aBuilder.setConfigurationSlotClass( RefBOMClassKey.TRK );
                                          aBuilder.setCode( TRK_CONFIGSLOT_CODE );
                                          aBuilder.addPartGroup(
                                                new DomainConfiguration<PartGroup>() {

                                                   @Override
                                                   public void configure( PartGroup aBuilder ) {
                                                      aBuilder.setInventoryClass(
                                                            RefInvClassKey.TRK );
                                                      aBuilder.setCode( TRK_PART_GROUP_CODE );
                                                      aBuilder.addPart( lTrkPartNo );
                                                   }
                                                } );
                                          aBuilder.addConfigurationSlot(
                                                new DomainConfiguration<ConfigurationSlot>() {

                                                   @Override
                                                   public void
                                                         configure( ConfigurationSlot aBuilder ) {
                                                      aBuilder.setConfigurationSlotClass(
                                                            RefBOMClassKey.TRK );
                                                      aBuilder.setCode(
                                                            TRK_CHILD_TRK_CONFIG_SLOT_CODE );
                                                   }
                                                } );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final PartGroupKey lTrkPartGroupKey =
            EqpBomPart.getBomPartKey( lEngineAssembly, TRK_PART_GROUP_CODE );

      ConfigSlotKey lEngineTrkConfigSlot =
            EqpAssmblBom.getBomItemKey( ENGINE_ASSEMBLY_CODE, TRK_CONFIGSLOT_CODE );

      final ConfigSlotPositionKey lTrkConfigSlotPositionKey = new ConfigSlotPositionKey(
            lEngineTrkConfigSlot, EqpAssmblPos.getFirstPosId( lEngineTrkConfigSlot ) );
      final String lTrkPosCd = EqpAssmblPos.getPosCd( lTrkConfigSlotPositionKey );

      ConfigSlotKey lEngineTrkChildConfigSlot =
            EqpAssmblBom.getBomItemKey( ENGINE_ASSEMBLY_CODE, TRK_CHILD_TRK_CONFIG_SLOT_CODE );
      final ConfigSlotPositionKey lTrkChildConfigSlotPositionKey = new ConfigSlotPositionKey(
            lEngineTrkChildConfigSlot, EqpAssmblPos.getFirstPosId( lEngineTrkChildConfigSlot ) );
      final String lChildTrkPosCd = EqpAssmblPos.getPosCd( lTrkChildConfigSlotPositionKey );

      final DataTypeKey lCalcUsageParm = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), CALC_USAGE_PARM_CODE );

      final BigDecimal lCurrentCyclesTsn = new BigDecimal( 20 );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setAssembly( lEngineAssembly );
            aBuilder.addUsage( CYCLES, lCurrentCyclesTsn );
            aBuilder.addUsage( lCalcUsageParm,
                  lCurrentCyclesTsn.multiply( CALCULATION_CONSTANT_VALUE ) );
         }
      } );

      // Required by the service layer code
      final LocationKey lLocation = createLocation();

      final InventoryKey lTrkRemovedInReq =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.setPartGroup( lTrkPartGroupKey );
                  aBuilder.setParent( lEngine );
                  aBuilder.setSerialNumber( TRK_SERIALNO );
                  aBuilder.setLastKnownConfigSlot( lTrkConfigSlotPositionKey.getCd(),
                        TRK_CONFIGSLOT_CODE, lTrkPosCd );
                  aBuilder.setPartNumber( lTrkPartNo );
                  aBuilder.addUsage( CYCLES, lCurrentCyclesTsn );
                  aBuilder.addUsage( lCalcUsageParm,
                        lCurrentCyclesTsn.multiply( CALCULATION_CONSTANT_VALUE ) );
                  aBuilder.setLocation( lLocation );

               }
            } );

      final InventoryKey lSubComponentTrkRemovedInReq =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.setParent( lTrkRemovedInReq );
                  aBuilder.setSerialNumber( ANOTHER_TRK_SERIALNO );
                  aBuilder.setLastKnownConfigSlot( lTrkChildConfigSlotPositionKey.getCd(),
                        TRK_CHILD_TRK_CONFIG_SLOT_CODE, lChildTrkPosCd );
                  aBuilder.setPartNumber( lTrkPartNo );
                  aBuilder.addUsage( CYCLES, lCurrentCyclesTsn );
                  aBuilder.addUsage( lCalcUsageParm,
                        lCurrentCyclesTsn.multiply( CALCULATION_CONSTANT_VALUE ) );
                  aBuilder.setLocation( lLocation );

               }
            } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( LATEST_FLIGHT );
            aBuilder.setArrivalDate( LATEST_FLIGHT_ARRIVAL_DATE );
            aBuilder.setDepartureDate( LATEST_FLIGHT_DEPARTURE_DATE );
            aBuilder.addUsage( lEngine, CYCLES, lCurrentCyclesTsn );
         }
      } );

      final BigDecimal lEarlierFlightCyclesTsn = lCurrentCyclesTsn.subtract( FLIGHT_CYCLES_DELTA );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( EARLIER_FLIGHT );
            aBuilder.setArrivalDate( EARLIER_FLIGHT_ARRIVAL_DATE );
            aBuilder.setDepartureDate( EARLIER_FLIGHT_DEPARTURE_DATE );
            aBuilder.addUsage( lEngine, CYCLES, lEarlierFlightCyclesTsn );
         }
      } );

      final TaskKey lReq = createRequirementWithPartRemovalRequest( lEngine, lTrkPartNo,
            lTrkPartGroupKey, lEngineAssembly, lTrkConfigSlotPositionKey, lTrkRemovedInReq );

      // Labour Skill with Code "AET" required by production for Blank_RO signature
      createLabourSkill( LABOUR_SKILL_CODE_AET );

      // Labour Skill with Code "INSP" required by production for Blank_RO signature
      createLabourSkill( LABOUR_SKILL_CODE_INSP );

      final BigDecimal lWorkPackageCyclesTsn =
            lEarlierFlightCyclesTsn.subtract( FLIGHT_CYCLES_DELTA );

      Domain.createComponentWorkPackage( new DomainConfiguration<ComponentWorkPackage>() {

         @Override
         public void configure( ComponentWorkPackage aBuilder ) {
            aBuilder.setStatus( RefEventStatusKey.IN_WORK );
            aBuilder.assignTask( lReq );
            aBuilder.addSubAssembly( lEngine );
            aBuilder.addUsageSnapshot( lEngine, CYCLES, lWorkPackageCyclesTsn );
         }
      } );

      // When
      new CompleteService( lReq ).completeBatch( iAuthorizingHr, lReqCompleteBatchDate, true );

      // Then
      // Assertions for TRK inventory removed in Requirement
      BigDecimal lExpectedSubComponentTrkRemovedInReqCalcParmCurrentUsage =
            lCurrentCyclesTsn.subtract( ( lCurrentCyclesTsn ).subtract( lWorkPackageCyclesTsn ) )
                  .multiply( CALCULATION_CONSTANT_VALUE );
      assertCurrentUsage( lSubComponentTrkRemovedInReq, lCalcUsageParm,
            lExpectedSubComponentTrkRemovedInReqCalcParmCurrentUsage );

      dropCalculationFromDatabase();

   }


   /**
    *
    * Description: It will adjust the current standard usage of a component that was installed via a
    * requirement assigned to a work package when that requirement is completed on a date after
    * which usage was recorded against the component.<br>
    *
    * The adjustment will equal the difference between the component's assembly inventory's current
    * usage and the usage snapshot of the work package<br>
    *
    * <pre>
    * Given an engine assembly and an engine based on this assembly
    * Given the engine is tracking a standard usage parameter and has current usage
    * Given a loose TRK tracking the same standard usage parameter and has current usage
    * Given the engine has usage adjustments
    * Given a started component work package against the engine with a usage snapshot
    * Given an active REQ against the engine with a install part requirement for the TRK
    * Given the REQ is assigned to the component work package
    * When the REQ is completed as a batch on a date prior to engine usage adjustments
    * Then the standard parameters in current usage of the TRK is increased by the difference
    * between the engine's current usage and the work package's usage snapshot
    * </pre>
    */
   @Test
   public void
         itAdjustsStandardUsageParmCurrentUsageOfComponentInstalledInReqAssigndToWPForReqCompletionDateBeforeHistoricalUsages()
               throws Exception {
      // createCalculationInDatabase() method should be called prior to any data setup in database
      // as the method performs an explicit roll-back followed by an implicit database commit
      createCalculationInDatabase();

      final Date lReqCompleteBatchDate = DateUtils.addDays( EARLIER_FLIGHT_DEPARTURE_DATE, -2 );

      final PartNoKey lTrkPartNo = createTRKPartNo();

      final AssemblyKey lEngineAssembly =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aEngineAssembly ) {
                  aEngineAssembly.setCode( ENGINE_ASSEMBLY_CODE );
                  aEngineAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootCs ) {
                              aRootCs.setConfigurationSlotClass( RefBOMClassKey.ROOT );
                              aRootCs.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void configure( ConfigurationSlot aBuilder ) {

                                          aBuilder.setConfigurationSlotClass( RefBOMClassKey.TRK );
                                          aBuilder.setCode( TRK_CONFIGSLOT_CODE );
                                          aBuilder.addPartGroup(
                                                new DomainConfiguration<PartGroup>() {

                                                   @Override
                                                   public void configure( PartGroup aBuilder ) {
                                                      aBuilder.setInventoryClass(
                                                            RefInvClassKey.TRK );
                                                      aBuilder.setCode( TRK_PART_GROUP_CODE );
                                                      aBuilder.addPart( lTrkPartNo );
                                                   }
                                                } );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final PartGroupKey lTrkPartGroupKey =
            EqpBomPart.getBomPartKey( lEngineAssembly, TRK_PART_GROUP_CODE );

      ConfigSlotKey lEngineTrkConfigSlot =
            EqpAssmblBom.getBomItemKey( ENGINE_ASSEMBLY_CODE, TRK_CONFIGSLOT_CODE );

      final ConfigSlotPositionKey lTrkConfigSlotPositionKey = new ConfigSlotPositionKey(
            lEngineTrkConfigSlot, EqpAssmblPos.getFirstPosId( lEngineTrkConfigSlot ) );
      final String lTrkPosCd = EqpAssmblPos.getPosCd( lTrkConfigSlotPositionKey );

      final BigDecimal lCurrentHoursTsn = new BigDecimal( 100 );
      final BigDecimal lCurrentCyclesTsn = new BigDecimal( 20 );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setAssembly( lEngineAssembly );
            aBuilder.addUsage( HOURS, lCurrentHoursTsn );
            aBuilder.addUsage( CYCLES, lCurrentCyclesTsn );

         }
      } );

      // Required by the service layer code
      final LocationKey lLocation = createLocation();

      // Required by the part install request service layer code
      final OwnerKey lInvOwner = Domain.createOwner();

      final InventoryKey lTrkInstalledInReq =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.setPartGroup( lTrkPartGroupKey );
                  aBuilder.setSerialNumber( ANOTHER_TRK_SERIALNO );
                  aBuilder.setPartNumber( lTrkPartNo );
                  aBuilder.addUsage( HOURS, BigDecimal.ZERO );
                  aBuilder.addUsage( CYCLES, BigDecimal.ZERO );
                  aBuilder.setCondition( RefInvCondKey.RFI );
                  aBuilder.setLocation( lLocation );
                  aBuilder.setLastKnownConfigSlot( lTrkConfigSlotPositionKey.getCd(),
                        TRK_CONFIGSLOT_CODE, lTrkPosCd );
                  aBuilder.setOwner( lInvOwner );
               }
            } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( LATEST_FLIGHT );
            aBuilder.setArrivalDate( LATEST_FLIGHT_ARRIVAL_DATE );
            aBuilder.setDepartureDate( LATEST_FLIGHT_DEPARTURE_DATE );
            aBuilder.addUsage( lEngine, HOURS, lCurrentHoursTsn );
            aBuilder.addUsage( lEngine, CYCLES, lCurrentCyclesTsn );
         }
      } );

      final BigDecimal lEarlierFlightHoursTsn = lCurrentHoursTsn.subtract( FLIGHT_HOURS_DELTA );
      final BigDecimal lEarlierFlightCyclesTsn = lCurrentCyclesTsn.subtract( FLIGHT_CYCLES_DELTA );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( EARLIER_FLIGHT );
            aBuilder.setArrivalDate( EARLIER_FLIGHT_ARRIVAL_DATE );
            aBuilder.setDepartureDate( EARLIER_FLIGHT_DEPARTURE_DATE );
            aBuilder.addUsage( lEngine, HOURS, lEarlierFlightHoursTsn );
            aBuilder.addUsage( lEngine, CYCLES, lEarlierFlightCyclesTsn );
         }
      } );

      final TaskKey lReq = createRequirementWithPartInstallRequest( lEngine, lTrkPartNo,
            lTrkPartGroupKey, lEngineAssembly, lTrkConfigSlotPositionKey, lTrkInstalledInReq );

      // Labour Skill with Code "AET" required by production for Blank_RO signature
      createLabourSkill( LABOUR_SKILL_CODE_AET );

      // Labour Skill with Code "INSP" required by production for Blank_RO signature
      createLabourSkill( LABOUR_SKILL_CODE_INSP );

      final BigDecimal lWorkPackageHoursTsn = lEarlierFlightHoursTsn.subtract( FLIGHT_HOURS_DELTA );
      final BigDecimal lWorkPackageCyclesTsn =
            lEarlierFlightCyclesTsn.subtract( FLIGHT_CYCLES_DELTA );

      Domain.createComponentWorkPackage( new DomainConfiguration<ComponentWorkPackage>() {

         @Override
         public void configure( ComponentWorkPackage aBuilder ) {
            aBuilder.setStatus( RefEventStatusKey.IN_WORK );
            aBuilder.assignTask( lReq );
            aBuilder.addSubAssembly( lEngine );
            aBuilder.addUsageSnapshot( lEngine, HOURS, lWorkPackageHoursTsn );
            aBuilder.addUsageSnapshot( lEngine, CYCLES, lWorkPackageCyclesTsn );

         }
      } );

      // When
      new CompleteService( lReq ).completeBatch( iAuthorizingHr, lReqCompleteBatchDate, true );

      // Then
      // Assertions for TRK inventory removed in Requirement
      BigDecimal lExpectedTrkInstalledInReqCurrentUsageHours =
            lCurrentHoursTsn.subtract( lWorkPackageHoursTsn );
      assertCurrentUsage( lTrkInstalledInReq, HOURS, lExpectedTrkInstalledInReqCurrentUsageHours );
      BigDecimal lExpectedTrkInstalledInReqCurrentUsageCycles =
            ( lCurrentCyclesTsn ).subtract( lWorkPackageCyclesTsn );
      assertCurrentUsage( lTrkInstalledInReq, CYCLES,
            lExpectedTrkInstalledInReqCurrentUsageCycles );

      dropCalculationFromDatabase();

   }


   /**
    *
    * Description: It will adjust the current calculated usage of a component that was installed via
    * a requirement assigned to a work package when that requirement is completed on a date after
    * which usage was recorded against the component.<br>
    *
    * The adjustment will equal the difference between the component's assembly inventory's current
    * usage and the usage snapshot of the work package<br>
    *
    * <pre>
    * Given an engine assembly and an engine based on this assembly
    * Given the engine is tracking a standard usage parameter and has current usage
    * Given the engine is tracking a calculated usage parameter based on the standard usage
    * parameter
    * Given a loose TRK tracking the same standard & calculated usage parameter and has current
    * usage
    * Given the engine has usage adjustments
    * Given a started component work package against the engine with a usage snapshot
    *
    * Given an active REQ against the engine with a install part requirement for the TRK
    * Given the REQ is assigned to the component work package
    * When the REQ is completed as a batch on a date prior to engine usage adjustments
    * Then the calculated usage parameter in current usage of the TRK is increased by the difference
    * between the engine's current usage and the work package's usage snapshot
    * </pre>
    */
   @Test
   public void
         itAdjustsCalculatedParmCurrentUsageOfComponentInstalledInReqAssigndToWPForReqCompletionDateBeforeHistoricalUsages()
               throws Exception {

      // createCalculationInDatabase() method should be called prior to any data setup in database
      // as the method performs an explicit roll-back followed by an implicit database commit
      createCalculationInDatabase();

      final Date lReqCompleteBatchDate = DateUtils.addDays( EARLIER_FLIGHT_DEPARTURE_DATE, -2 );

      final PartNoKey lTrkPartNo = createTRKPartNo();

      final AssemblyKey lEngineAssembly =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aEngineAssembly ) {
                  aEngineAssembly.setCode( ENGINE_ASSEMBLY_CODE );
                  aEngineAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootCs ) {
                              aRootCs.setConfigurationSlotClass( RefBOMClassKey.ROOT );
                              aRootCs.addCalculatedUsageParameter(
                                    new DomainConfiguration<CalculatedUsageParameter>() {

                                       @Override
                                       public void configure( CalculatedUsageParameter aBuilder ) {
                                          aBuilder.setCode( CALC_USAGE_PARM_CODE );
                                          aBuilder.setDatabaseCalculation( CALCULATION );
                                          aBuilder.setPrecisionQt( 2 );
                                          aBuilder.addConstant( CALCULATION_CONSTANT,
                                                CALCULATION_CONSTANT_VALUE );
                                          aBuilder.addParameter( DataTypeKey.CYCLES );
                                       }
                                    } );
                              aRootCs.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void configure( ConfigurationSlot aBuilder ) {

                                          aBuilder.setConfigurationSlotClass( RefBOMClassKey.TRK );
                                          aBuilder.setCode( TRK_CONFIGSLOT_CODE );
                                          aBuilder.addPartGroup(
                                                new DomainConfiguration<PartGroup>() {

                                                   @Override
                                                   public void configure( PartGroup aBuilder ) {
                                                      aBuilder.setInventoryClass(
                                                            RefInvClassKey.TRK );
                                                      aBuilder.setCode( TRK_PART_GROUP_CODE );
                                                      aBuilder.addPart( lTrkPartNo );
                                                   }
                                                } );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final PartGroupKey lTrkPartGroupKey =
            EqpBomPart.getBomPartKey( lEngineAssembly, TRK_PART_GROUP_CODE );

      ConfigSlotKey lEngineTrkConfigSlot =
            EqpAssmblBom.getBomItemKey( ENGINE_ASSEMBLY_CODE, TRK_CONFIGSLOT_CODE );

      final ConfigSlotPositionKey lTrkConfigSlotPositionKey = new ConfigSlotPositionKey(
            lEngineTrkConfigSlot, EqpAssmblPos.getFirstPosId( lEngineTrkConfigSlot ) );
      final String lTrkPosCd = EqpAssmblPos.getPosCd( lTrkConfigSlotPositionKey );

      final DataTypeKey lCalcUsageParm = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), CALC_USAGE_PARM_CODE );

      final BigDecimal lCurrentCyclesTsn = new BigDecimal( 20 );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setAssembly( lEngineAssembly );
            aBuilder.addUsage( CYCLES, lCurrentCyclesTsn );
            aBuilder.addUsage( lCalcUsageParm,
                  lCurrentCyclesTsn.multiply( CALCULATION_CONSTANT_VALUE ) );

         }
      } );

      // Required by the service layer code
      final LocationKey lLocation = createLocation();

      // Required by the part install request service layer code
      final OwnerKey lInvOwner = Domain.createOwner();

      final InventoryKey lTrkInstalledInReq =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.setPartGroup( lTrkPartGroupKey );
                  aBuilder.setSerialNumber( ANOTHER_TRK_SERIALNO );
                  aBuilder.setPartNumber( lTrkPartNo );
                  aBuilder.addUsage( CYCLES, BigDecimal.ZERO );
                  aBuilder.addUsage( lCalcUsageParm, BigDecimal.ZERO );
                  aBuilder.setCondition( RefInvCondKey.RFI );
                  aBuilder.setLocation( lLocation );
                  aBuilder.setLastKnownConfigSlot( lTrkConfigSlotPositionKey.getCd(),
                        TRK_CONFIGSLOT_CODE, lTrkPosCd );
                  aBuilder.setOwner( lInvOwner );
               }
            } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( LATEST_FLIGHT );
            aBuilder.setArrivalDate( LATEST_FLIGHT_ARRIVAL_DATE );
            aBuilder.setDepartureDate( LATEST_FLIGHT_DEPARTURE_DATE );
            aBuilder.addUsage( lEngine, CYCLES, lCurrentCyclesTsn );
         }
      } );

      final BigDecimal lEarlierFlightCyclesTsn = lCurrentCyclesTsn.subtract( FLIGHT_CYCLES_DELTA );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( EARLIER_FLIGHT );
            aBuilder.setArrivalDate( EARLIER_FLIGHT_ARRIVAL_DATE );
            aBuilder.setDepartureDate( EARLIER_FLIGHT_DEPARTURE_DATE );
            aBuilder.addUsage( lEngine, CYCLES, lEarlierFlightCyclesTsn );
         }
      } );

      final TaskKey lReq = createRequirementWithPartInstallRequest( lEngine, lTrkPartNo,
            lTrkPartGroupKey, lEngineAssembly, lTrkConfigSlotPositionKey, lTrkInstalledInReq );

      // Labour Skill with Code "AET" required by production for Blank_RO signature
      createLabourSkill( LABOUR_SKILL_CODE_AET );

      // Labour Skill with Code "INSP" required by production for Blank_RO signature
      createLabourSkill( LABOUR_SKILL_CODE_INSP );

      final BigDecimal lWorkPackageCyclesTsn =
            lEarlierFlightCyclesTsn.subtract( FLIGHT_CYCLES_DELTA );

      Domain.createComponentWorkPackage( new DomainConfiguration<ComponentWorkPackage>() {

         @Override
         public void configure( ComponentWorkPackage aBuilder ) {
            aBuilder.setStatus( RefEventStatusKey.IN_WORK );
            aBuilder.assignTask( lReq );
            aBuilder.addSubAssembly( lEngine );
            aBuilder.addUsageSnapshot( lEngine, CYCLES, lWorkPackageCyclesTsn );

         }
      } );

      // When
      new CompleteService( lReq ).completeBatch( iAuthorizingHr, lReqCompleteBatchDate, true );

      // Then
      // Assertions for TRK inventory removed in Requirement
      BigDecimal lExpectedTrkInstalledInReqCalcParmCurrentUsage =
            ( ( lCurrentCyclesTsn ).subtract( lWorkPackageCyclesTsn ) )
                  .multiply( CALCULATION_CONSTANT_VALUE );
      assertCurrentUsage( lTrkInstalledInReq, lCalcUsageParm,
            lExpectedTrkInstalledInReqCalcParmCurrentUsage );

      dropCalculationFromDatabase();

   }


   /**
    *
    * Description: It will adjust the current ACC usage of a component that was installed via a
    * requirement assigned to a work package when that requirement is completed on a date after
    * which usage was recorded against the component.<br>
    *
    * The adjustment will equal the difference between the component's assembly inventory's current
    * usage and the usage snapshot of the work package<br>
    *
    * <pre>
    * Given two engine part numbers, each representing two different thrust ratings
    * Given an engine assembly and an engine based on it
    * Given two Assembly Specific Usage Parameters(ASUP), one for each part number, assigned to the
    * engine assembly and all sub-components
    * Given the engine is tracking the ASUPs and has current usage
    * Given a loose TRK tracking the same ASUPs and has current usage
    * Given the engine has usage adjustments
    * Given a started component work package against the engine with a usage snapshot
    * Given an active REQ against the engine with a install part requirement for the TRK
    * Given the REQ is assigned to the component work package
    * When the REQ is completed as a batch on a date prior to engine usage adjustments
    * Then the ASUP in current usage of the TRK is increased by the difference between the engine's
    * current usage and the work package's usage snapshot
    * </pre>
    */
   @Test
   public void
         itAdjustsAssemblySpecificUsageParmCurrentUsageOfComponentInstalledInReqAssigndToWPForReqCompletionDateBeforeHistoricalUsages()
               throws Exception {
      // createCalculationInDatabase() method should be called prior to any data setup in database
      // as the method performs an explicit roll-back followed by an implicit database commit
      createCalculationInDatabase();

      final Date lReqCompleteBatchDate = DateUtils.addDays( EARLIER_FLIGHT_DEPARTURE_DATE, -2 );

      final PartNoKey lLowThrustRatingEnginePartNo = createEnginePartNo();

      final PartNoKey lHighThrustRatingEnginePartNo = createEnginePartNo();

      final PartNoKey lTrkPartNo = createTRKPartNo();

      final AssemblyKey lEngineAssembly =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aEngineAssembly ) {
                  aEngineAssembly.setCode( ENGINE_ASSEMBLY_CODE );
                  aEngineAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootCs ) {
                              aRootCs.setConfigurationSlotClass( RefBOMClassKey.ROOT );;
                              aRootCs.addPartGroup( new DomainConfiguration<PartGroup>() {

                                 @Override
                                 public void configure( PartGroup aPartGroup ) {
                                    aPartGroup.addPart( lLowThrustRatingEnginePartNo );
                                    aPartGroup.addPart( lHighThrustRatingEnginePartNo );

                                 }
                              } );
                              aRootCs.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void configure( ConfigurationSlot aBuilder ) {

                                          aBuilder.setConfigurationSlotClass( RefBOMClassKey.TRK );
                                          aBuilder.setCode( TRK_CONFIGSLOT_CODE );
                                          aBuilder.addPartGroup(
                                                new DomainConfiguration<PartGroup>() {

                                                   @Override
                                                   public void configure( PartGroup aBuilder ) {
                                                      aBuilder.setInventoryClass(
                                                            RefInvClassKey.TRK );
                                                      aBuilder.setCode( TRK_PART_GROUP_CODE );
                                                      aBuilder.addPart( lTrkPartNo );
                                                   }
                                                } );
                                       }
                                    } );
                           }
                        } );
               }
            } );
      final DataTypeKey lLowThrustRatingCycle = createAccumulatedParm( LOW_THRUST_RATING_CYCLE,
            lEngineAssembly, lLowThrustRatingEnginePartNo, CYCLES );

      final DataTypeKey lHighThrustRatingCycle = createAccumulatedParm( HIGH_THRUST_RATING_CYCLE,
            lEngineAssembly, lHighThrustRatingEnginePartNo, CYCLES );

      final PartGroupKey lTrkPartGroupKey =
            EqpBomPart.getBomPartKey( lEngineAssembly, TRK_PART_GROUP_CODE );

      ConfigSlotKey lEngineTrkConfigSlot =
            EqpAssmblBom.getBomItemKey( ENGINE_ASSEMBLY_CODE, TRK_CONFIGSLOT_CODE );

      final ConfigSlotPositionKey lTrkConfigSlotPositionKey = new ConfigSlotPositionKey(
            lEngineTrkConfigSlot, EqpAssmblPos.getFirstPosId( lEngineTrkConfigSlot ) );
      final String lTrkPosCd = EqpAssmblPos.getPosCd( lTrkConfigSlotPositionKey );

      final BigDecimal lCurrentCyclesTsn = new BigDecimal( 20 );
      final BigDecimal llHighThrustRatingCycleTsn = lCurrentCyclesTsn.subtract( BigDecimal.TEN );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setAssembly( lEngineAssembly );
            aBuilder.setPartNumber( lLowThrustRatingEnginePartNo );
            aBuilder.addUsage( lLowThrustRatingCycle, lCurrentCyclesTsn );
            aBuilder.addUsage( lHighThrustRatingCycle, llHighThrustRatingCycleTsn );

         }
      } );

      // Required by the service layer code
      final LocationKey lLocation = createLocation();

      // Required by the part install request service layer code
      final OwnerKey lInvOwner = Domain.createOwner();

      final InventoryKey lTrkInstalledInReq =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.setPartGroup( lTrkPartGroupKey );
                  aBuilder.setSerialNumber( ANOTHER_TRK_SERIALNO );
                  aBuilder.setPartNumber( lTrkPartNo );
                  aBuilder.addUsage( lLowThrustRatingCycle, BigDecimal.ZERO );
                  aBuilder.addUsage( lHighThrustRatingCycle, llHighThrustRatingCycleTsn );
                  aBuilder.setCondition( RefInvCondKey.RFI );
                  aBuilder.setLocation( lLocation );
                  aBuilder.setLastKnownConfigSlot( lTrkConfigSlotPositionKey.getCd(),
                        TRK_CONFIGSLOT_CODE, lTrkPosCd );
                  aBuilder.setOwner( lInvOwner );
               }
            } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( LATEST_FLIGHT );
            aBuilder.setArrivalDate( LATEST_FLIGHT_ARRIVAL_DATE );
            aBuilder.setDepartureDate( LATEST_FLIGHT_DEPARTURE_DATE );
            aBuilder.addUsage( lEngine, lLowThrustRatingCycle, lCurrentCyclesTsn );
            aBuilder.addUsage( lEngine, lHighThrustRatingCycle, llHighThrustRatingCycleTsn );
         }
      } );

      final BigDecimal lEarlierFlightCyclesTsn = lCurrentCyclesTsn.subtract( FLIGHT_CYCLES_DELTA );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( EARLIER_FLIGHT );
            aBuilder.setArrivalDate( EARLIER_FLIGHT_ARRIVAL_DATE );
            aBuilder.setDepartureDate( EARLIER_FLIGHT_DEPARTURE_DATE );
            aBuilder.addUsage( lEngine, lLowThrustRatingCycle, lEarlierFlightCyclesTsn );
            aBuilder.addUsage( lEngine, lHighThrustRatingCycle, llHighThrustRatingCycleTsn );
         }
      } );

      final TaskKey lReq = createRequirementWithPartInstallRequest( lEngine, lTrkPartNo,
            lTrkPartGroupKey, lEngineAssembly, lTrkConfigSlotPositionKey, lTrkInstalledInReq );

      // Labour Skill with Code "AET" required by production for Blank_RO signature
      createLabourSkill( LABOUR_SKILL_CODE_AET );

      // Labour Skill with Code "INSP" required by production for Blank_RO signature
      createLabourSkill( LABOUR_SKILL_CODE_INSP );

      final BigDecimal lWorkPackageCyclesTsn =
            lEarlierFlightCyclesTsn.subtract( FLIGHT_CYCLES_DELTA );

      Domain.createComponentWorkPackage( new DomainConfiguration<ComponentWorkPackage>() {

         @Override
         public void configure( ComponentWorkPackage aBuilder ) {
            aBuilder.setStatus( RefEventStatusKey.IN_WORK );
            aBuilder.assignTask( lReq );
            aBuilder.addSubAssembly( lEngine );
            aBuilder.addUsageSnapshot( lEngine, lLowThrustRatingCycle, lWorkPackageCyclesTsn );
            aBuilder.addUsageSnapshot( lEngine, lHighThrustRatingCycle,
                  llHighThrustRatingCycleTsn );

         }
      } );

      // When
      new CompleteService( lReq ).completeBatch( iAuthorizingHr, lReqCompleteBatchDate, true );

      // Then
      // Assertions for TRK inventory removed in Requirement
      BigDecimal lExpectedTrkInstalledInReqLowThrustCyclesCurrentUsage =
            ( lCurrentCyclesTsn ).subtract( lWorkPackageCyclesTsn );
      assertCurrentUsage( lTrkInstalledInReq, lLowThrustRatingCycle,
            lExpectedTrkInstalledInReqLowThrustCyclesCurrentUsage );
      assertCurrentUsage( lTrkInstalledInReq, lHighThrustRatingCycle, llHighThrustRatingCycleTsn );

      dropCalculationFromDatabase();

   }


   /**
    *
    * Description: It will adjust the current calculated usage of a sub-component of a component
    * that was installed via a requirement assigned to a work package when that requirement is
    * completed on a date after which usage was recorded against the component.<br>
    *
    * The adjustment will equal the difference between the component's assembly inventory's current
    * usage and the usage snapshot of the work package<br>
    *
    * <pre>
    * Given an engine assembly and an engine based on this assembly
    * Given the engine is tracking a standard usage parameter and has current usage
    * Given the engine is tracking a calculated usage parameter based on the standard usage
    * parameter
    * Given a loose TRK tracking the same standard & calculated usage parameter and has current
    * usage
    * Given a sub-component TRK attached to the TRK tracking the same standard & calculated usage
    * parameter and current usage
    * Given the engine has usage adjustments
    * Given a started component work package against the engine with a usage snapshot
    * Given an active REQ against the engine with a install part requirement for the TRK
    * Given the REQ is assigned to the component work package
    * When the REQ is completed as a batch on a date prior to engine usage adjustments
    * Then the calculated usage parameter in current usage of the sub-component TRK is increased by
    * the difference between the engine's current usage and the work package's usage snapshot
    * </pre>
    */
   @Test
   public void
         itAdjustsCalculatedParmCurrentUsageOfSubComponentInstalledInReqAssigndToWPForReqCompletionDateBeforeHistoricalUsages()
               throws Exception {

      // createCalculationInDatabase() method should be called prior to any data setup in database
      // as the method performs an explicit roll-back followed by an implicit database commit
      createCalculationInDatabase();

      Date lReqCompleteBatchDate = DateUtils.addDays( EARLIER_FLIGHT_DEPARTURE_DATE, -2 );

      final PartNoKey lTrkPartNo = createTRKPartNo();

      final AssemblyKey lEngineAssembly =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aEngineAssembly ) {
                  aEngineAssembly.setCode( ENGINE_ASSEMBLY_CODE );
                  aEngineAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootCs ) {
                              aRootCs.setConfigurationSlotClass( RefBOMClassKey.ROOT );
                              aRootCs.addCalculatedUsageParameter(
                                    new DomainConfiguration<CalculatedUsageParameter>() {

                                       @Override
                                       public void configure( CalculatedUsageParameter aBuilder ) {
                                          aBuilder.setCode( CALC_USAGE_PARM_CODE );
                                          aBuilder.setDatabaseCalculation( CALCULATION );
                                          aBuilder.setPrecisionQt( 2 );
                                          aBuilder.addConstant( CALCULATION_CONSTANT,
                                                CALCULATION_CONSTANT_VALUE );
                                          aBuilder.addParameter( DataTypeKey.CYCLES );
                                       }
                                    } );
                              aRootCs.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void configure( ConfigurationSlot aBuilder ) {

                                          aBuilder.setConfigurationSlotClass( RefBOMClassKey.TRK );
                                          aBuilder.setCode( TRK_CONFIGSLOT_CODE );
                                          aBuilder.addPartGroup(
                                                new DomainConfiguration<PartGroup>() {

                                                   @Override
                                                   public void configure( PartGroup aBuilder ) {
                                                      aBuilder.setInventoryClass(
                                                            RefInvClassKey.TRK );
                                                      aBuilder.setCode( TRK_PART_GROUP_CODE );
                                                      aBuilder.addPart( lTrkPartNo );
                                                   }
                                                } );
                                          aBuilder.addConfigurationSlot(
                                                new DomainConfiguration<ConfigurationSlot>() {

                                                   @Override
                                                   public void
                                                         configure( ConfigurationSlot aBuilder ) {
                                                      aBuilder.setConfigurationSlotClass(
                                                            RefBOMClassKey.TRK );
                                                      aBuilder.setCode(
                                                            TRK_CHILD_TRK_CONFIG_SLOT_CODE );
                                                   }
                                                } );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final PartGroupKey lTrkPartGroupKey =
            EqpBomPart.getBomPartKey( lEngineAssembly, TRK_PART_GROUP_CODE );

      ConfigSlotKey lEngineTrkConfigSlot =
            EqpAssmblBom.getBomItemKey( ENGINE_ASSEMBLY_CODE, TRK_CONFIGSLOT_CODE );

      final ConfigSlotPositionKey lTrkConfigSlotPositionKey = new ConfigSlotPositionKey(
            lEngineTrkConfigSlot, EqpAssmblPos.getFirstPosId( lEngineTrkConfigSlot ) );
      final String lTrkPosCd = EqpAssmblPos.getPosCd( lTrkConfigSlotPositionKey );

      ConfigSlotKey lEngineTrkChildConfigSlot =
            EqpAssmblBom.getBomItemKey( ENGINE_ASSEMBLY_CODE, TRK_CHILD_TRK_CONFIG_SLOT_CODE );
      final ConfigSlotPositionKey lTrkChildConfigSlotPositionKey = new ConfigSlotPositionKey(
            lEngineTrkChildConfigSlot, EqpAssmblPos.getFirstPosId( lEngineTrkChildConfigSlot ) );
      final String lChildTrkPosCd = EqpAssmblPos.getPosCd( lTrkChildConfigSlotPositionKey );

      final DataTypeKey lCalcUsageParm = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), CALC_USAGE_PARM_CODE );

      final BigDecimal lCurrentCyclesTsn = new BigDecimal( 20 );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setAssembly( lEngineAssembly );
            aBuilder.addUsage( CYCLES, lCurrentCyclesTsn );
            aBuilder.addUsage( lCalcUsageParm,
                  lCurrentCyclesTsn.multiply( CALCULATION_CONSTANT_VALUE ) );

         }
      } );

      // Required by the service layer code
      final LocationKey lLocation = createLocation();

      // Required by the part install request service layer code
      final OwnerKey lInvOwner = Domain.createOwner();

      final InventoryKey lTrkInstalledInReq =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.setPartGroup( lTrkPartGroupKey );
                  aBuilder.setSerialNumber( TRK_SERIALNO );
                  aBuilder.setPartNumber( lTrkPartNo );
                  aBuilder.addUsage( CYCLES, BigDecimal.ZERO );
                  aBuilder.addUsage( lCalcUsageParm, BigDecimal.ZERO );
                  aBuilder.setCondition( RefInvCondKey.RFI );
                  aBuilder.setLocation( lLocation );
                  aBuilder.setLastKnownConfigSlot( lTrkConfigSlotPositionKey.getCd(),
                        TRK_CONFIGSLOT_CODE, lTrkPosCd );
                  aBuilder.setOwner( lInvOwner );
               }
            } );

      final InventoryKey lSubComponentTrkInstalledInReq =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.setParent( lTrkInstalledInReq );
                  aBuilder.setSerialNumber( ANOTHER_TRK_SERIALNO );
                  aBuilder.setLastKnownConfigSlot( lTrkChildConfigSlotPositionKey.getCd(),
                        TRK_CHILD_TRK_CONFIG_SLOT_CODE, lChildTrkPosCd );
                  aBuilder.setPartNumber( lTrkPartNo );
                  aBuilder.addUsage( CYCLES, BigDecimal.ZERO );
                  aBuilder.addUsage( lCalcUsageParm, BigDecimal.ZERO );
                  aBuilder.setLocation( lLocation );

               }
            } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( LATEST_FLIGHT );
            aBuilder.setArrivalDate( LATEST_FLIGHT_ARRIVAL_DATE );
            aBuilder.setDepartureDate( LATEST_FLIGHT_DEPARTURE_DATE );
            aBuilder.addUsage( lEngine, CYCLES, lCurrentCyclesTsn );
         }
      } );

      final BigDecimal lEarlierFlightCyclesTsn = lCurrentCyclesTsn.subtract( FLIGHT_CYCLES_DELTA );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( EARLIER_FLIGHT );
            aBuilder.setArrivalDate( EARLIER_FLIGHT_ARRIVAL_DATE );
            aBuilder.setDepartureDate( EARLIER_FLIGHT_DEPARTURE_DATE );
            aBuilder.addUsage( lEngine, CYCLES, lEarlierFlightCyclesTsn );
         }
      } );

      final TaskKey lReq = createRequirementWithPartInstallRequest( lEngine, lTrkPartNo,
            lTrkPartGroupKey, lEngineAssembly, lTrkConfigSlotPositionKey, lTrkInstalledInReq );

      // Labour Skill with Code "AET" required by production for Blank_RO signature
      createLabourSkill( LABOUR_SKILL_CODE_AET );

      // Labour Skill with Code "INSP" required by production for Blank_RO signature
      createLabourSkill( LABOUR_SKILL_CODE_INSP );

      final BigDecimal lWorkPackageCyclesTsn =
            lEarlierFlightCyclesTsn.subtract( FLIGHT_CYCLES_DELTA );

      Domain.createComponentWorkPackage( new DomainConfiguration<ComponentWorkPackage>() {

         @Override
         public void configure( ComponentWorkPackage aBuilder ) {
            aBuilder.setStatus( RefEventStatusKey.IN_WORK );
            aBuilder.assignTask( lReq );
            aBuilder.addSubAssembly( lEngine );
            aBuilder.addUsageSnapshot( lEngine, CYCLES, lWorkPackageCyclesTsn );

         }
      } );

      // When
      new CompleteService( lReq ).completeBatch( iAuthorizingHr, lReqCompleteBatchDate, true );

      // Then
      // Assertions for TRK inventory removed in Requirement
      BigDecimal lExpectedSubComponentTrkInstalledInReqCalcParmCurrentUsage = ( lCurrentCyclesTsn )
            .subtract( lWorkPackageCyclesTsn ).multiply( CALCULATION_CONSTANT_VALUE );
      assertCurrentUsage( lSubComponentTrkInstalledInReq, lCalcUsageParm,
            lExpectedSubComponentTrkInstalledInReqCalcParmCurrentUsage );

      dropCalculationFromDatabase();

   }


   /**
    * Description: It will set the IPN event date generated in correspondence to part transformation
    * to MOD requirement completion date.
    *
    * <pre>
    * Given two engine part numbers
    * Given an engine assembly and an engine based on the assembly and one of the part numbers
    * Given a started component work package against the engine
    * Given an active MOD requirement against the engine with a part transformation to another part number
    * Given the REQ is assigned to the component work package
    * When the REQ is completed as a batch
    * Then the part transformation event date is set to the MOD requirement completion date.
    * </pre>
    */
   @Test
   public void itSetsPartTransformationDateToRequirementCompletionDate() throws Exception {
      // Given
      final PartNoKey lLowThrustRatingEnginePartNo = createEnginePartNo();
      final PartNoKey lHighThrustRatingEnginePartNo = createEnginePartNo();
      final AssemblyKey lEngineAssembly =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.setCode( ASSEMBLY_CODE );
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.setConfigurationSlotClass( RefBOMClassKey.ROOT );
                        aBuilder.addPartGroup( new DomainConfiguration<PartGroup>() {

                           @Override
                           public void configure( PartGroup aBuilder ) {
                              aBuilder.setCode( TRK_PART_GROUP_CODE );
                              aBuilder.addPart( lHighThrustRatingEnginePartNo );
                              aBuilder.addPart( lLowThrustRatingEnginePartNo );

                           }
                        } );
                     }
                  } );

               }
            } );

      final ConfigSlotKey lEngineRootConfigSlot =
            Domain.readRootConfigurationSlot( lEngineAssembly );
      // Required by a validation in DefaultChangePartNoService
      final PartGroupKey lEnginePartGroup =
            Domain.readPartGroup( lEngineRootConfigSlot, TRK_PART_GROUP_CODE );

      // Required by DefaultChangePartNoService.setPartNoEvt method
      final OwnerKey lOwner = Domain.createOwner();

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setPartNumber( lLowThrustRatingEnginePartNo );
            aBuilder.setPartGroup( lEnginePartGroup );
            aBuilder.setOwner( lOwner );
            aBuilder.setAssembly( lEngineAssembly );
         }
      } );

      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aBuilder ) {
                  aBuilder.addPartTransformation( lLowThrustRatingEnginePartNo,
                        lHighThrustRatingEnginePartNo );
                  aBuilder.setTaskClass( RefTaskClassKey.MOD );
                  aBuilder.againstConfigurationSlot( lEngineRootConfigSlot );
               }
            } );

      final TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aBuilder ) {
            aBuilder.setInventory( lEngine );
            aBuilder.setTaskClass( RefTaskClassKey.MOD );
            aBuilder.setStatus( RefEventStatusKey.ACTV );
            aBuilder.setDefinition( lReqDefn );
         }
      } );

      Domain.createComponentWorkPackage( new DomainConfiguration<ComponentWorkPackage>() {

         @Override
         public void configure( ComponentWorkPackage aBuilder ) {
            aBuilder.assignTask( lReq );
            aBuilder.setStatus( RefEventStatusKey.IN_WORK );
            aBuilder.setInventory( lEngine );
         }
      } );

      Date lReqCompletionDate = DateUtils.addDays( DateUtils.getCurrentDateWithZeroTime(), -10 );
      // When
      new CompleteService( lReq ).completeBatch( iAuthorizingHr, lReqCompletionDate, true );
      // Then
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "event_type_cd", RefEventTypeKey.IPN.getCd() );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "evt_event", lArgs );
      if ( !lQs.next() )
         Assert.fail( "Part Transformation wasn't completed succesfully" );
      Date lActualPartTransformationDate = lQs.getDate( "event_dt" );
      assertEquals( "Part Transformation event date wasn't set to the requirement completion date",
            0, lActualPartTransformationDate.compareTo( lReqCompletionDate ) );

   }


   /**
    * This test case is testing when complete a requirement, the POSTCRT requirement is going to be
    * created.
    *
    * <pre>
    * Given an assembly.
    * And two requirement definitions on the assembly.
    * And the first requirement
    * definition has a POSTCRT relation with the second one.
    * And an actual first requirement on an inventory of the assembly.
    * When complete the first actual requirement.
    * Then an actual second task is created on the inventory.
    * </pre>
    *
    */
   @Test
   public void itCreatesActualTaskOfPostCrtTask() throws Exception {

      PartNoKey lPartNo = Domain.createPart();

      AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly( aAssembly -> {
         aAssembly.setRootConfigurationSlot( aConfigSlot -> {
            aConfigSlot.addPartGroup( aPartGroup -> {
               aPartGroup.addPart( lPartNo );
               aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
            } );
         } );
      } );

      ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAircraftAssembly );

      TaskTaskKey lFirstRequirmentDefn =
            Domain.createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.againstConfigurationSlot( lRootConfigSlot );
            } );

      TaskTaskKey lSecondRequirmentDefn =
            Domain.createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.POSTCRT,
                     lFirstRequirmentDefn );
               aRequirementDefinition.againstConfigurationSlot( lRootConfigSlot );
            } );

      InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setPart( lPartNo );
         aAircraft.setAssembly( lAircraftAssembly );
      } );

      TaskKey lFirstActualRequirement = Domain.createRequirement( aRequirement -> {
         aRequirement.setDefinition( lFirstRequirmentDefn );
         aRequirement.setInventory( lAircraft );
      } );

      Domain.createWorkPackage( aWorkPackage -> {
         aWorkPackage.addTask( lFirstActualRequirement );
         aWorkPackage.setStatus( RefEventStatusKey.IN_WORK );
         aWorkPackage.setAircraft( lAircraft );
      } );

      new CompleteService( lFirstActualRequirement ).completeBatch( iAuthorizingHr, new Date(),
            true );

      TaskKey lSecondActualRequirement =
            SchedStaskUtil.findLastCreatedTaskForTaskDefinition( lSecondRequirmentDefn );

      EvtEventDao lEvtEventDao = new JdbcEvtEventDao();
      SchedStaskDao lSchedStaskDao = new JdbcSchedStaskDao();
      assertNotNull( "Post created requirement is not created correctly.",
            lSecondActualRequirement );
      assertEquals( "Requirement is not under the proper inventory.", lAircraft,
            lSchedStaskDao.findByPrimaryKey( lSecondActualRequirement ).getMainInventory() );
      assertEquals( "Requirement is not active", RefEventStatusKey.ACTV, lEvtEventDao
            .findByPrimaryKey( lSecondActualRequirement.getEventKey() ).getEventStatus() );

   }


   /**
    * This test case is testing when completing a task under WP, the usage source is set based on
    * the WP usage source.
    *
    * <pre>
    *    Given a WP with usage source as "CUSTOMER".
    *    When complete a task under it.
    *    Then verify the task usage source is "CUSTOMER".
    * </pre>
    */
   @Test
   public void itAdjustUsageSnapshotBasedOnOffsetAndSetUsageSourceAsCustomer() throws Exception {
      BigDecimal trackedCycles = BigDecimal.valueOf( 5 );
      BigDecimal trackedHours = BigDecimal.valueOf( 15 );
      BigDecimal aircraftCycles = BigDecimal.valueOf( 5 );
      BigDecimal aircraftHours = BigDecimal.valueOf( 20 );
      BigDecimal aircraftWPCyclesValue = BigDecimal.valueOf( 5 );
      BigDecimal aircraftWPHoursValue = BigDecimal.TEN;

      InventoryKey track = Domain.createTrackedInventory( trk -> {
         trk.addUsage( CYCLES, trackedCycles );
         trk.addUsage( HOURS, trackedHours );
      } );
      InventoryKey aircraft = Domain.createAircraft( af -> {
         af.addTracked( track );
         af.addUsage( CYCLES, aircraftCycles );
         af.addUsage( HOURS, aircraftHours );
      } );
      UsageSnapshot aircraftWPCycles = new UsageSnapshot( aircraft, CYCLES, aircraftWPCyclesValue )
            .withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.CUSTOMER );
      UsageSnapshot aircraftWPHours = new UsageSnapshot( aircraft, HOURS, aircraftWPHoursValue )
            .withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.CUSTOMER );
      TaskKey taskInWp = Domain.createRequirement( rq -> {
         rq.setInventory( track );
      } );
      Domain.createWorkPackage( wp -> {
         wp.addUsageSnapshot( aircraftWPCycles );
         wp.addUsageSnapshot( aircraftWPHours );
         wp.addTask( taskInWp );
         wp.setStatus( RefEventStatusKey.IN_WORK );
      } );

      new CompleteService( taskInWp ).completeBatch( iAuthorizingHr, new Date(), true );
      EventInventoryKey eventInventoryKey =
            EvtInvTable.findByEventAndInventory( taskInWp, track ).getPk();
      double trackedTaskCycle = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( eventInventoryKey, CYCLES ) ).getTsnQt();
      double trackedTaskHours = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( eventInventoryKey, HOURS ) ).getTsnQt();

      assertEquals( "The cycle of the task is not calculated correctly.",
            trackedCycles.subtract( aircraftCycles.subtract( aircraftWPCyclesValue ) ).intValue(),
            BigDecimal.valueOf( trackedTaskCycle ).intValue() );
      assertEquals( "The hours of the task is not calculated correctly.",
            trackedHours.subtract( aircraftHours.subtract( aircraftWPHoursValue ) ).intValue(),
            BigDecimal.valueOf( trackedTaskHours ).intValue() );
      assertEquals( "Usage source cd is not set properly.", RefUsgSnapshotSrcTypeKey.CUSTOMER,
            EvtInvUsageTable
                  .findByPrimaryKey( new EventInventoryUsageKey( eventInventoryKey, HOURS ) )
                  .getUsageSnapshotSourceType() );
   }


   /**
    * This test case is testing when a dependent task is auto completed by a task completion, the
    * usage source will be set based on WP.
    *
    * <pre>
    *    Given a WP with usage source as "CUSTOMER".
    *    And a requirement under this WP.
    *    And a following task as "COMPLETE" relation to above requirement.
    *    When complete the requirement.
    *    Then verify the following task's complete usage snapshot source is "CUSTOMER".
    *    And the usage snapshot is recalculated based on offset.
    * </pre>
    */
   @Test
   public void itAdjustUsageSnapshotBasedOnOffsetAndSetUsageSourceAsCustomerForDependentTask()
         throws Exception {
      BigDecimal engingCycles = BigDecimal.valueOf( 5 );
      BigDecimal engineHours = BigDecimal.valueOf( 15 );
      BigDecimal aircraftCycles = BigDecimal.valueOf( 10 );
      BigDecimal aircraftHours = BigDecimal.valueOf( 20 );
      BigDecimal engineWPCyclesValue = BigDecimal.valueOf( 5 );
      BigDecimal engineWPHoursValue = BigDecimal.TEN;
      TaskTaskKey dependentReqDefinition = Domain.createRequirementDefinition();
      TaskTaskKey reqDefinition = Domain.createRequirementDefinition( rqd -> {
         rqd.addFollowingTaskDefinition( RefTaskDepActionKey.COMPLETE, dependentReqDefinition );
      } );
      InventoryKey engine = Domain.createEngine( eng -> {
         eng.addUsage( CYCLES, engingCycles );
         eng.addUsage( HOURS, engineHours );
      } );
      Domain.createAircraft( af -> {
         af.addEngine( engine );
         af.addUsage( CYCLES, aircraftCycles );
         af.addUsage( HOURS, aircraftHours );
      } );
      UsageSnapshot engineWPCycles = new UsageSnapshot( engine, CYCLES, engineWPCyclesValue )
            .withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.CUSTOMER );
      UsageSnapshot engineWPHours = new UsageSnapshot( engine, HOURS, engineWPHoursValue )
            .withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.CUSTOMER );
      TaskKey taskInWp = Domain.createRequirement( rq -> {
         rq.setInventory( engine );
         rq.setDefinition( reqDefinition );
      } );
      TaskKey dependentTask = Domain.createRequirement( rq -> {
         rq.setInventory( engine );
         rq.setDefinition( dependentReqDefinition );
      } );
      TaskKey workPackage = Domain.createWorkPackage( wp -> {
         wp.addUsageSnapshot( engineWPCycles );
         wp.addUsageSnapshot( engineWPHours );
         wp.addTask( taskInWp );
         wp.setStatus( RefEventStatusKey.IN_WORK );
      } );

      EvtEventDao evtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );

      new CompleteService( taskInWp ).completeBatch( iAuthorizingHr, new Date(), true );
      EventInventoryKey denpendentEventInventoryKey =
            EvtInvTable.findByEventAndInventory( dependentTask, engine ).getPk();
      assertEquals( "Usage source cd of dependent task is not set properly.",
            RefUsgSnapshotSrcTypeKey.CUSTOMER,
            EvtInvUsageTable
                  .findByPrimaryKey(
                        new EventInventoryUsageKey( denpendentEventInventoryKey, HOURS ) )
                  .getUsageSnapshotSourceType() );

      double engineTaskCycle = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( denpendentEventInventoryKey, CYCLES ) )
            .getTsnQt();
      double engineTaskHours = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( denpendentEventInventoryKey, HOURS ) )
            .getTsnQt();

      assertEquals( "The cycle of the task is not calculated correctly.",
            engingCycles.subtract( engingCycles.subtract( engineWPCyclesValue ) ).intValue(),
            BigDecimal.valueOf( engineTaskCycle ).intValue() );
      assertEquals( "The hours of the task is not calculated correctly.",
            engineHours.subtract( engineHours.subtract( engineWPHoursValue ) ).intValue(),
            BigDecimal.valueOf( engineTaskHours ).intValue() );
      assertEquals( "The dependent task is not moved into workpackage.", workPackage.getEventKey(),
            evtEventDao.findByPrimaryKey( dependentTask.getEventKey() ).getHEvent() );
   }


   private void assertCurrentUsage( InventoryKey aInventory, DataTypeKey aDataType,
         BigDecimal aExpectedUsage ) {

      assertEquals( "Inventory=" + aInventory + " , data type=" + aDataType + " : unexpected TSN",
            0, aExpectedUsage.compareTo( BigDecimal
                  .valueOf( InvCurrUsage.findTSNQtByInventory( aInventory, aDataType ) ) ) );

   }


   private DataTypeKey createAccumulatedParm( String aCode, AssemblyKey aAssemblyKey,
         PartNoKey aPartNoKey, DataTypeKey aAggregatedDataType ) {
      DataTypeKey lDataType = MimDataType.create( aCode, null, USAGE_PARM, null, 0, null );
      MimPartNumDataKey lMimPartNumDataKey =
            new MimPartNumDataKey( new ConfigSlotKey( aAssemblyKey, 0 ), lDataType );
      MimPartNumData.create( lMimPartNumDataKey );
      MimPartNumData lMimPartNumData = MimPartNumData.findByPrimaryKey( lMimPartNumDataKey );
      lMimPartNumData.setAssemblyPartNo( aPartNoKey.getDbId(), aPartNoKey.getId() );
      lMimPartNumData.setAggregatedDataType( aAggregatedDataType.getDbId(),
            aAggregatedDataType.getId() );
      lMimPartNumData.update();
      return lDataType;
   }


   private PartNoKey createTRKPartNo() {

      return Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aBuilder ) {
            aBuilder.setInventoryClass( RefInvClassKey.TRK );
            aBuilder.setShortDescription( TRK );
            aBuilder.setPartStatus( RefPartStatusKey.ACTV );

         }

      } );
   }


   private PartNoKey createEnginePartNo() {
      return Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aPart ) {
            aPart.setInventoryClass( RefInvClassKey.ASSY );
         }

      } );
   }


   // Required by the service layer code for part request install/removal
   private RefLabourSkillKey createLabourSkill( final String aSkillCode ) {
      return Domain.createLabourSkill( new DomainConfiguration<LabourSkill>() {

         @Override
         public void configure( LabourSkill aBuilder ) {

            aBuilder.setCode( aSkillCode );

         }
      } );

   }


   // Required by the service layer code for part request install/removal
   private LocationKey createLocation() {
      return Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aBuilder ) {
            aBuilder.setCode( "ATL" );
            aBuilder.setType( RefLocTypeKey.LINE );

         }

      } );

   }


   private TaskKey createRequirementWithPartInstallRequest( final InventoryKey aEngine,
         final PartNoKey aTrkPart, final PartGroupKey aTrkPartGroup,
         final AssemblyKey aEngineAssembly, final ConfigSlotPositionKey aTrkConfigSlotPosition,
         final InventoryKey aInstalledTrkInventory ) {
      return Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aBuilder ) {
            aBuilder.setInventory( aEngine );
            aBuilder.setStatus( RefEventStatusKey.ACTV );
            aBuilder.setTaskClass( RefTaskClassKey.REQ );
            aBuilder.addPartRequirement( new DomainConfiguration<PartRequirement>() {

               @Override
               public void configure( PartRequirement aBuilder ) {
                  aBuilder.setPartGroup( aTrkPartGroup );
                  aBuilder.setAssemblyConfigSlotPosition(
                        new ConfigSlotPositionKey( new ConfigSlotKey( aEngineAssembly, 0 ), 1 ) );
                  aBuilder.setConfigSlotPosition( aTrkConfigSlotPosition );
                  aBuilder.setPartInstallRequest( aBuilder.new PartInstallRequest()
                        .withInventory( aInstalledTrkInventory ).withPartNo( aTrkPart ) );

               }

            } );
         }

      } );
   }


   private TaskKey createRequirementWithPartRemovalRequest( final InventoryKey aEngine,
         final PartNoKey aTrkPart, final PartGroupKey aTrkPartGroup,
         final AssemblyKey aEngineAssembly, final ConfigSlotPositionKey aTrkConfigSlotPosition,
         final InventoryKey aRemovedTrkInventory ) {
      return Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aBuilder ) {
            aBuilder.setInventory( aEngine );
            aBuilder.setStatus( RefEventStatusKey.ACTV );
            aBuilder.setTaskClass( RefTaskClassKey.REQ );
            aBuilder.addPartRequirement( new DomainConfiguration<PartRequirement>() {

               @Override
               public void configure( PartRequirement aBuilder ) {
                  aBuilder.setPartGroup( aTrkPartGroup );
                  aBuilder.setAssemblyConfigSlotPosition(
                        new ConfigSlotPositionKey( new ConfigSlotKey( aEngineAssembly, 0 ), 1 ) );
                  aBuilder.setConfigSlotPosition( aTrkConfigSlotPosition );
                  aBuilder.setPartRemovalRequest( aBuilder.new PartRemovalRequest()
                        .withInventory( aRemovedTrkInventory ).withPartNo( aTrkPart ) );

               }

            } );
         }

      } );
   }


   private void createCalculationInDatabase() throws Exception {
      // Function creation is DDL which implicitly commits transaction
      // Domain.createCalculatedParameterEquationFunction() will perform explicit rollback before
      // function creation ensuring no data gets committed accidentally
      String lCreateFunctionStatement = "CREATE OR REPLACE FUNCTION " + CALCULATION + " ("
            + "aConstant NUMBER, aCyclesInput NUMBER" + " )" + " RETURN FLOAT" + " " + "IS "
            + "result FLOAT; " + "BEGIN" + " " + "result := aConstant * aCyclesInput ; " + "RETURN"
            + " " + " result;" + "END" + " " + CALCULATION + " ;";

      Domain.createCalculatedParameterEquationFunction( iDatabaseConnectionRule.getConnection(),
            lCreateFunctionStatement );

   }


   private void dropCalculationFromDatabase() throws SQLException {

      // Function dropping is DDL which implicitly commits transaction.
      // Domain.dropCalculatedParameterEquationFunction performs explicit rollback before function
      // drop ensuring no data gets committed accidentally.
      Domain.dropCalculatedParameterEquationFunction( iDatabaseConnectionRule.getConnection(),
            CALCULATION );
   }

}
