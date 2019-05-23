package com.mxi.mx.core.maintenance.plan.phoneupdeferral.authorize;

import static com.mxi.mx.core.key.DataTypeKey.CDY;
import static com.mxi.mx.core.key.RefTaskDefinitionStatusKey.ACTV;
import static java.math.BigDecimal.ONE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.DeferralReference;
import com.mxi.am.domain.DeferralReference.FailedSystemInfo;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.builder.AssemblyBuilder;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.ConfigSlotPositionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.FormatUtil;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.CapabilityKey;
import com.mxi.mx.core.key.CapabilityLevelKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FailDeferRefKey;
import com.mxi.mx.core.key.FailDeferRefTaskDefnKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFailDeferKey;
import com.mxi.mx.core.key.RefFailureSeverityKey;
import com.mxi.mx.core.key.RefFaultSourceKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.services.MxCoreUtils;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.fail.FailDeferRef;
import com.mxi.mx.core.table.fail.FailDeferRefDegradCap;
import com.mxi.mx.core.table.fail.FailDeferRefTaskDefn;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.sd.SdFaultTable;
import com.mxi.mx.core.table.task.TaskDefnTable;
import com.mxi.mx.core.table.task.TaskTaskTable;
import com.mxi.mx.core.unittest.table.inv.InvInv;


public class AuthorizeDeferralOrchestratorTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   static final int USER_ID = 0;
   static final String TASK_BARCODE = "T0002JHF";
   static final String DEF_REF_NAME = "DEF-REF-1";
   static final CapabilityKey CAPABILITY_KEY = new CapabilityKey( 10, "ETOPS" );
   static final CapabilityLevelKey CAPABILITY_LEVEL_KEY =
         new CapabilityLevelKey( 10, "ETOPS1", CAPABILITY_KEY );
   static final String CAPABILITY_DESC = "Extended Operations";
   static final String DEFERRAL_REFERENCE_STATUS = "ACTV";
   static final String REQ_DEFINITION_CODE = "REQ_DEFN_CODE";
   static final String DEFERRAL_CLASS_CODE = "MEL A";
   static final String LOGBOOK_REFERENCE = "LOG-REF-1234";
   static final String LOGBOOK_DESCRIPTION = "LOG-1234-DESCRIPTION";
   static final String FAILED_SYSTEM_CODE = "10-1-15";
   static final String FAILED_SYSTEM_ASSEMBLY_CODE = "TRK-1";
   static final String FAILED_SYSTEM_POSITION_CODE = "POS-1";
   static final String AIRCRAFT_ASSEMBLY_CODE = "ACFT-1";
   private static final String OPERATIONAL_RESTRICTION_FROM_DEFERRAL_REFERENCE =
         "I am a restriction in deferral reference.";

   @Rule
   public OperateAsUserRule iOperateAsUserRule = new OperateAsUserRule( USER_ID, "SYSTEM" );

   private FailDeferRefKey iDeferralReferenceKey;
   private OrgHr iOrgHrTable;
   private InventoryKey iAircraftInventoryKey;
   private AssemblyKey iAircraftAssemblyKey;
   private AssemblyKey iFailedSystemAssemblyKey;
   private ConfigSlotKey iFailedSystemBomItemKey;
   private MxAuthorizeDeferralParameter iMxAuthorizeDeferralParameter;
   private HumanResourceKey iHr;

   // subject under test
   private AuthorizeDeferralOrchestrator iOrchestrator = new AuthorizeDeferralOrchestrator();


   @Before
   public void loadData() throws Exception {

      // set user with authority
      iOrgHrTable = OrgHr.findByUserId( new UserKey( USER_ID ) );
      iOrgHrTable.setAllAuthority( true );
      iOrgHrTable.update();
      iHr = iOrgHrTable.getPk();

      iAircraftAssemblyKey =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly.setCode( AIRCRAFT_ASSEMBLY_CODE );
               }
            } );

      iAircraftInventoryKey = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( iAircraftAssemblyKey );
         }
      } );

      // create failed system assembly
      iFailedSystemAssemblyKey = new AssemblyBuilder( FAILED_SYSTEM_ASSEMBLY_CODE ).build();
      iFailedSystemBomItemKey = new ConfigSlotBuilder( FAILED_SYSTEM_CODE )
            .withRootAssembly( iFailedSystemAssemblyKey ).build();
      new ConfigSlotPositionBuilder().withConfigSlot( iFailedSystemBomItemKey )
            .withPositionCode( FAILED_SYSTEM_POSITION_CODE ).build();

      // create failed system inventory
      Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

         @Override
         public void configure( TrackedInventory aTrackedInventory ) {
            aTrackedInventory.setParent( iAircraftInventoryKey );
            aTrackedInventory.setLastKnownConfigSlot( FAILED_SYSTEM_ASSEMBLY_CODE,
                  FAILED_SYSTEM_CODE, FAILED_SYSTEM_POSITION_CODE );
         }
      } );

      // set failed system for the deferral reference
      final FailedSystemInfo lFailedSystemInfo = new FailedSystemInfo();
      lFailedSystemInfo.setFailedSystemKey( iFailedSystemBomItemKey );
      lFailedSystemInfo.setFailedSystemAltId(
            EqpAssmblBom.findByPrimaryKey( iFailedSystemBomItemKey ).getAlternateKey() );

      iDeferralReferenceKey =
            Domain.createDeferralReference( new DomainConfiguration<DeferralReference>() {

               @Override
               public void configure( DeferralReference aDeferralReference ) {

                  aDeferralReference
                        .setFaultDeferralKey( new RefFailDeferKey( 0, DEFERRAL_CLASS_CODE ) );
                  aDeferralReference.setName( DEF_REF_NAME );
                  aDeferralReference.setFaultSeverityKey( RefFailureSeverityKey.MEL );
                  aDeferralReference.setAssemblyKey( iAircraftAssemblyKey );
                  aDeferralReference.setStatus( DEFERRAL_REFERENCE_STATUS );
                  aDeferralReference.setFailedSystemInfo( lFailedSystemInfo );
                  aDeferralReference.setOperationalRestrictions(
                        OPERATIONAL_RESTRICTION_FROM_DEFERRAL_REFERENCE );
               }
            } );

   }


   /**
    * Authorize a deferral that is associated to a deferral reference that does not have recurring
    * inspections
    *
    * @throws Exception
    */
   @Test
   public void orchestrate_deferralReferenceWithoutReccurringInspection() throws Exception {

      // set the parameter object
      setAuthorizeDeferralParameters();

      // execute
      final AuthorizeDeferralResponseTO lTO =
            iOrchestrator.orchestrate( iMxAuthorizeDeferralParameter );

      // authorization code, barcode and due date are not null
      assertNotNull( lTO.getAuthorizationCode() );
      assertNotNull( lTO.getBarcode() );
      assertNotNull( lTO.getDueDate() );

      // no system notification
      assertNull( lTO.getSystemNotification() );

      // fault is created and deferred
      EventKey lEventKey = getTaskEvent( lTO.getBarcode() );
      assertEquals( RefEventStatusKey.CFDEFER,
            EvtEventTable.findByPrimaryKey( lEventKey ).getEventStatus() );
   }


   /**
    * Authorize a deferral that is associated to a deferral reference that has recurring inspections
    *
    * @throws Exception
    */
   @Test
   public void orchestrate_deferralReferenceWithReccurringInspection() throws Exception {
      // create requirement definition
      final TaskTaskKey lReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aRequirementDefinition ) {

                  aRequirementDefinition.againstConfigurationSlot(
                        InvInvTable.findByPrimaryKey( iAircraftInventoryKey ).getBOMItem() );
                  aRequirementDefinition.setCode( REQ_DEFINITION_CODE );
                  aRequirementDefinition.setRevisionNumber( 1 );
                  aRequirementDefinition.setStatus( ACTV );
                  aRequirementDefinition.addRecurringSchedulingRule( CDY, ONE );
                  aRequirementDefinition.isOnCondition();
                  aRequirementDefinition.setRecurring( true );
                  aRequirementDefinition.setScheduledFromEffectiveDate( new Date() );
                  aRequirementDefinition.setMinimumForecastRange( ONE );
               }
            } );

      // associate the requirement with the deferral reference as recurring inspection
      TaskDefnTable lTaskDefnTable = TaskDefnTable
            .findByPrimaryKey( TaskTaskTable.findByPrimaryKey( lReqDefnKey ).getTaskDefn() );

      FailDeferRefTaskDefnKey lFailDeferRefTaskDefnKey = new FailDeferRefTaskDefnKey(
            FailDeferRef.findByPrimaryKey( iDeferralReferenceKey ).getAlternateKey(),
            lTaskDefnTable.getAlternateKey() );

      FailDeferRefTaskDefn lFailDeferRefTaskDefn =
            FailDeferRefTaskDefn.create( lFailDeferRefTaskDefnKey );
      lFailDeferRefTaskDefn.insert();

      // set the parameter object
      setAuthorizeDeferralParameters();

      // execute
      final AuthorizeDeferralResponseTO lTO =
            iOrchestrator.orchestrate( iMxAuthorizeDeferralParameter );

      // authorization code, barcode and due date are not null
      assertNotNull( lTO.getAuthorizationCode() );
      assertNotNull( lTO.getBarcode() );
      assertNotNull( lTO.getDueDate() );

      final String lSystemErrorMessage =
            "Create the recurring inspection task(s) manually if necessary. "
                  + "The system could not initialize one or more of the associated recurring inspection(s) for the fault "
                  + lTO.getBarcode() + " using the deferral reference " + DEF_REF_NAME + ".";

      // system notification has messages
      assertEquals( lSystemErrorMessage, lTO.getSystemNotification() );

      // fault is created and deferred
      EventKey lEventKey = getTaskEvent( lTO.getBarcode() );
      assertEquals( RefEventStatusKey.CFDEFER,
            EvtEventTable.findByPrimaryKey( lEventKey ).getEventStatus() );
   }


   /**
    * Authorize a deferral that is associated to a deferral reference that has capabilities
    *
    * @throws Exception
    */
   @Test
   public void orchestrate_deferralReferenceWithCapabilities() throws Exception {

      addCapabilities();
      addCapabilitiesLevels();
      assignCapabilitiesToAssembly();

      // add degraded capability to the deferral reference
      FailDeferRefDegradCap lFailDeferRefDegradCap = new FailDeferRefDegradCap();
      lFailDeferRefDegradCap.setDeferralReferenceId(
            FailDeferRef.findByPrimaryKey( iDeferralReferenceKey ).getAlternateKey() );
      lFailDeferRefDegradCap.setCapability( CAPABILITY_KEY );
      lFailDeferRefDegradCap.setCapabilityLevel( CAPABILITY_LEVEL_KEY );
      lFailDeferRefDegradCap.insert();

      // set the parameter object
      setAuthorizeDeferralParameters();

      // execute
      final AuthorizeDeferralResponseTO lTO =
            iOrchestrator.orchestrate( iMxAuthorizeDeferralParameter );

      // authorization code
      assertNotNull( lTO.getAuthorizationCode() );

      // no system notification or error messages
      assertNull( lTO.getSystemNotification() );

      // fault is created and deferred
      EventKey lEventKey = getTaskEvent( lTO.getBarcode() );
      assertEquals( RefEventStatusKey.CFDEFER,
            EvtEventTable.findByPrimaryKey( lEventKey ).getEventStatus() );
   }


   /**
    * Authorize a deferral that is associated to a deferral reference that has has operational
    * restrictions
    *
    * @throws Exception
    */
   @Test
   public void orchestrate_deferralReferenceWithOperationalRestrictions() throws Exception {

      // set the parameter object
      setAuthorizeDeferralParameters();

      // execute
      final AuthorizeDeferralResponseTO lTO =
            iOrchestrator.orchestrate( iMxAuthorizeDeferralParameter );

      // authorization code
      assertNotNull( lTO.getAuthorizationCode() );

      // no system notification or error messages
      assertNull( lTO.getSystemNotification() );

      // fault has the operational restrictions from the deferral reference now
      SdFaultTable lAfterDeferralAuthorizedFault =
            SdFaultTable.findByPrimaryKey( new FaultKey( getTaskEvent( lTO.getBarcode() ) ) );
      assertEquals(
            MxCoreUtils.appendString( iHr, null, OPERATIONAL_RESTRICTION_FROM_DEFERRAL_REFERENCE ),
            lAfterDeferralAuthorizedFault.getOperationalRestriction() );
   }


   /**
    * Get the event key related to the fault
    *
    * @param aBarcode
    *           corrective task barcode
    * @return EventKey fault event key
    */
   private EventKey getTaskEvent( String aBarcode ) {

      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aBarcode", aBarcode );
      DataSet lDataSet = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.task.GetTaskByBarcode", lDataSetArgument );

      lDataSet.first();
      TaskKey lTaskKey = lDataSet.getKey( TaskKey.class, "sched_db_id", "sched_id" );

      return SchedStaskTable.findByPrimaryKey( lTaskKey ).getFault().getEventKey();
   }


   /**
    * Set the information for the MxAuthorizeDeferralParameter object
    */
   private void setAuthorizeDeferralParameters() {
      iMxAuthorizeDeferralParameter = new MxAuthorizeDeferralParameter();

      iMxAuthorizeDeferralParameter.setAircraftId( FormatUtil
            .uuidToString( InvInv.findByPrimaryKey( iAircraftInventoryKey ).getAlternateKey() ) );
      iMxAuthorizeDeferralParameter.setDeferralReferenceId( FormatUtil.uuidToString(
            FailDeferRef.findByPrimaryKey( iDeferralReferenceKey ).getAlternateKey() ) );
      iMxAuthorizeDeferralParameter.setFaultSourceCd( RefFaultSourceKey.PILOT.getCd() );
      iMxAuthorizeDeferralParameter.setFailedSystemId( FormatUtil.uuidToString(
            EqpAssmblBom.findByPrimaryKey( iFailedSystemBomItemKey ).getAlternateKey() ) );
      iMxAuthorizeDeferralParameter.setFoundById( iOrgHrTable.getAlternateKey().toString() );
      iMxAuthorizeDeferralParameter.setLogbookDescription( LOGBOOK_DESCRIPTION );
      iMxAuthorizeDeferralParameter.setLogbookReference( LOGBOOK_REFERENCE );
   }


   /**
    *
    * Add capability codes
    *
    */
   private void addCapabilities() {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( CAPABILITY_KEY, "acft_cap_db_id", "acft_cap_cd" );
      lArgs.add( "desc_sdesc", CAPABILITY_DESC );
      lArgs.add( "cap_order", 1 );

      MxDataAccess.getInstance().executeInsert( "ref_acft_cap", lArgs );
   }


   /**
    *
    * Add capability levels
    *
    */
   private void addCapabilitiesLevels() {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( CAPABILITY_LEVEL_KEY, "acft_cap_level_db_id", "acft_cap_level_cd",
            "acft_cap_db_id", "acft_cap_cd" );
      lArgs.add( "desc_sdesc", CAPABILITY_DESC );
      lArgs.add( "level_order", 1 );

      MxDataAccess.getInstance().executeInsert( "ref_acft_cap_level", lArgs );
   }


   /**
    *
    * Assign capabilities to the aircraft assembly
    *
    */
   private void assignCapabilitiesToAssembly() {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "assmbl_db_id", iAircraftAssemblyKey.getDbId() );
      lArgs.add( "assmbl_cd", iAircraftAssemblyKey.getCd() );
      lArgs.add( "acft_cap_db_id", CAPABILITY_KEY.getDbId() );
      lArgs.add( "acft_cap_cd", CAPABILITY_KEY.getCd() );
      lArgs.add( "acft_cap_level_db_id", CAPABILITY_LEVEL_KEY.getDbId() );
      lArgs.add( "acft_cap_level_cd", CAPABILITY_LEVEL_KEY.getCd() );

      MxDataAccess.getInstance().executeInsert( "assmbl_cap_levels", lArgs );
   }

}
