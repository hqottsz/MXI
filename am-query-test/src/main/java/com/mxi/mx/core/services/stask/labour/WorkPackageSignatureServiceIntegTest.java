package com.mxi.mx.core.services.stask.labour;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.HumanResource;
import com.mxi.am.domain.Organization;
import com.mxi.am.domain.RepairOrder;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.WorkPackageSignatureRequirementBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.common.trigger.TriggerFactoryStub;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.key.RefWPSignReqStatusKey;
import com.mxi.mx.core.key.SchedWPKey;
import com.mxi.mx.core.key.SchedWPSignKey;
import com.mxi.mx.core.key.SchedWPSignReqKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.services.stask.maintrelease.AircraftReleaseTO;
import com.mxi.mx.core.services.stask.maintrelease.ComponentReleaseTO;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.JdbcEvtEventDao;
import com.mxi.mx.core.table.sched.JdbcSchedWPSignDao;
import com.mxi.mx.core.table.sched.JdbcSchedWPSignReqDao;
import com.mxi.mx.core.table.sched.SchedWPSignDao;
import com.mxi.mx.core.table.sched.SchedWPSignReqDao;
import com.mxi.mx.core.table.sched.SchedWPSignTable;
import com.mxi.mx.core.table.task.JdbcSchedWPDao;
import com.mxi.mx.core.table.task.SchedWPDao;
import com.mxi.mx.core.table.task.SchedWPTable;
import com.mxi.mx.core.trigger.MxCoreTriggerType;
import com.mxi.mx.core.trigger.inventory.MxReleaseNumberGenerator;


public class WorkPackageSignatureServiceIntegTest {

   private static final Date TODAY = new Date();
   private static final String RELEASE_NUMBER = "WO-10000";
   private static final String RELEASE_NOTES = "Released to service";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();
   @Rule
   public OperateAsUserRule iOperateAsUserRule = new OperateAsUserRule( 1, "testuser" );

   private WorkPackageSignatureService iWorkPackageSignatureService;
   private SchedWPSignDao iSchedWpSignDao;
   private SchedWPSignReqDao iSchedWpSignReqDao;
   private EvtEventDao iEvtEventDao;
   private SchedWPDao iSchedWPDao;
   private MxReleaseNumberGenerator iOrderNumberGenerator;

   // common test data
   private HumanResourceKey iValidHrKey;
   private LocationKey iLocationKey;


   @Before
   public void setUp() throws Exception {

      iWorkPackageSignatureService = new WorkPackageSignatureServiceImpl();
      iSchedWpSignDao = new JdbcSchedWPSignDao();
      iSchedWpSignReqDao = new JdbcSchedWPSignReqDao();
      iSchedWPDao = new JdbcSchedWPDao();
      iEvtEventDao = new JdbcEvtEventDao();

      // set up the fake TriggerFactory to be used by the CompleteService
      {
         iOrderNumberGenerator = mock( MxReleaseNumberGenerator.class );

         Map<String, Object> lTriggerMap = new HashMap<String, Object>( 1 );
         lTriggerMap.put( MxCoreTriggerType.WO_REL_NUM_GEN.toString(), iOrderNumberGenerator );
         lTriggerMap.put( MxCoreTriggerType.RO_REL_NUM_GEN.toString(), iOrderNumberGenerator );

         TriggerFactory lTriggerFactoryStub = new TriggerFactoryStub( lTriggerMap );
         TriggerFactory.setInstance( lTriggerFactoryStub );
      }

      setUpData();
   }


   /**
    *
    * Test the result of signing the last sign off requirement of a work package and releasing the
    * aircraft to service. Expected results are:
    * <ul>
    * <li>requirement sign off status should be 'complete'</li>
    * <li>the recorded hr key and date should match input</li>
    * <li>the work package status should be 'complete'</li>
    * <li>release notes remarks match input</li>
    * <li>release number matches input</li>
    * <li>the release to service boolean should be true</li>
    * </ul>
    */
   @Test
   public void sign_aircraftWP_lastRow_release() throws MxException, TriggerException {

      // GIVEN
      // create a work package with one sign off requirement
      TaskKey lWorkPackageKey = createAircraftWorkPackage();

      SchedWPSignReqKey lReqKeyLbr = new WorkPackageSignatureRequirementBuilder()
            .withLabourSkill( RefLabourSkillKey.LBR ).withWorkPackage( lWorkPackageKey ).build();

      AircraftReleaseTO lAircraftReleaseTO = new AircraftReleaseTO( iValidHrKey, TODAY, null );
      lAircraftReleaseTO.setReleaseNumber( RELEASE_NUMBER, "" );
      lAircraftReleaseTO.setReleaseNotes( RELEASE_NOTES, "" );
      lAircraftReleaseTO.setReleaseToService( true );

      // WHEN
      iWorkPackageSignatureService.sign( lReqKeyLbr, lAircraftReleaseTO );

      // THEN
      SchedWPSignTable lSchedWPSignTable = getSchedWPSignTable( lReqKeyLbr.getPk() );
      SchedWPTable lSchedWPTable = getSchedWPTable( lWorkPackageKey );

      assertThat( getWorkPackageStatus( lWorkPackageKey ),
            equalTo( RefEventStatusKey.COMPLETE.getCd() ) );
      assertThat( getWPSignReqStatus( lReqKeyLbr ), equalTo( RefWPSignReqStatusKey.COMPLETE ) );
      assertThat( lSchedWPSignTable.getHR(), equalTo( iValidHrKey ) );
      assertThat( lSchedWPSignTable.getSignOffDate().toString(), equalTo( TODAY.toString() ) );
      assertThat( lSchedWPTable.getReleaseRemarks(), equalTo( RELEASE_NOTES ) );
      assertThat( lSchedWPTable.getReleaseNumber(), equalTo( RELEASE_NUMBER ) );
      assertThat( lSchedWPTable.getReleaseToServiceBool(), is( true ) );
   }


   /**
    *
    * Test the result of signing the last sign off requirement of a work package without releasing
    * the aircraft. Expected results are:
    * <ul>
    * <li>requirement sign off status should be 'complete'</li>
    * <li>the recorded hr key and date should match input</li>
    * <li>the work package status should be 'complete'</li>
    * <li>release notes remarks are not set</li>
    * <li>release number is not set</li>
    * <li>the release to service boolean should be false</li>
    * </ul>
    */
   @Test
   public void sign_aircraftWP_lastRow_noRelease() throws MxException, TriggerException {

      // GIVEN
      // create a work package with one sign off requirement
      TaskKey lWorkPackageKey = createAircraftWorkPackage();

      SchedWPSignReqKey lReqKeyLbr = new WorkPackageSignatureRequirementBuilder()
            .withLabourSkill( RefLabourSkillKey.LBR ).withWorkPackage( lWorkPackageKey ).build();

      AircraftReleaseTO lAircraftReleaseTO = new AircraftReleaseTO( iValidHrKey, TODAY, null );
      lAircraftReleaseTO.setReleaseNumber( RELEASE_NUMBER, "" );
      lAircraftReleaseTO.setReleaseNotes( RELEASE_NOTES, "" );
      lAircraftReleaseTO.setReleaseToService( false );

      // WHEN
      iWorkPackageSignatureService.sign( lReqKeyLbr, lAircraftReleaseTO );

      // THEN
      SchedWPSignTable lSchedWPSignTable = getSchedWPSignTable( lReqKeyLbr.getPk() );
      SchedWPTable lSchedWPTable = getSchedWPTable( lWorkPackageKey );

      assertThat( getWorkPackageStatus( lWorkPackageKey ),
            equalTo( RefEventStatusKey.COMPLETE.getCd() ) );
      assertThat( getWPSignReqStatus( lReqKeyLbr ), equalTo( RefWPSignReqStatusKey.COMPLETE ) );
      assertThat( lSchedWPSignTable.getHR(), equalTo( iValidHrKey ) );
      assertThat( lSchedWPSignTable.getSignOffDate().toString(), equalTo( TODAY.toString() ) );
      assertThat( lSchedWPTable.getReleaseRemarks(), equalTo( null ) );
      assertThat( lSchedWPTable.getReleaseNumber(), equalTo( null ) );
      assertThat( lSchedWPTable.getReleaseToServiceBool(), is( false ) );
   }


   /**
    *
    * Test the result of signing a sign off requirement on a work package (not the last row).
    * Expected results are:
    * <ul>
    * <li>requirement sign off status should be 'complete'</li>
    * <li>the recorded hr key should match input</li>
    * <li>the work package status should remain 'in work'</li>
    * <li>the release to service boolean should be false</li>
    * </ul>
    */
   @Test
   public void sign_aircraftWP_nonLastRow() throws MxException, TriggerException {

      // GIVEN
      TaskKey lWorkPackageTaskKey = createAircraftWorkPackage();

      new WorkPackageSignatureRequirementBuilder().withLabourSkill( RefLabourSkillKey.LBR )
            .withWorkPackage( lWorkPackageTaskKey ).build();
      SchedWPSignReqKey lReqKeyEng =
            new WorkPackageSignatureRequirementBuilder().withLabourSkill( RefLabourSkillKey.ENG )
                  .withWorkPackage( lWorkPackageTaskKey ).build();

      AircraftReleaseTO lAircraftReleaseTO = new AircraftReleaseTO( iValidHrKey, TODAY, "" );
      // not valid since this is not the last row
      lAircraftReleaseTO.setReleaseToService( true );

      // WHEN
      iWorkPackageSignatureService.sign( lReqKeyEng, lAircraftReleaseTO );

      // THEN
      SchedWPSignTable lSchedWPSignTable = getSchedWPSignTable( lReqKeyEng.getPk() );
      SchedWPTable lSchedWPTable = getSchedWPTable( lWorkPackageTaskKey );

      assertThat( getWorkPackageStatus( lWorkPackageTaskKey ),
            equalTo( RefEventStatusKey.IN_WORK.getCd() ) );
      assertThat( getWPSignReqStatus( lReqKeyEng ), equalTo( RefWPSignReqStatusKey.COMPLETE ) );
      assertThat( lSchedWPSignTable.getHR(), equalTo( iValidHrKey ) );
      assertThat( lSchedWPSignTable.getSignOffDate().toString(), equalTo( TODAY.toString() ) );
      assertThat( lSchedWPTable.getReleaseToServiceBool(), is( false ) );
   }


   /**
    *
    * Test the result of signing the last sign off requirement of a repair order. Expected results
    * are:
    * <ul>
    * <li>requirement sign off status should be 'complete'</li>
    * <li>the recorded hr key and date should match input</li>
    * <li>the repair order status should be 'complete'</li>
    * <li>release notes remarks match input</li>
    * <li>release number matches input</li>
    * </ul>
    */
   @Test
   public void sign_componentWP_lastRow() throws MxException, TriggerException {

      // GIVEN
      TaskKey lRepairOrderTaskKey = createComponentWorkPackage();

      SchedWPSignReqKey lSchedWPSignReqKey =
            new WorkPackageSignatureRequirementBuilder().withLabourSkill( RefLabourSkillKey.LBR )
                  .withWorkPackage( lRepairOrderTaskKey ).build();

      ComponentReleaseTO lComponentReleaseTO = new ComponentReleaseTO( iValidHrKey, TODAY, "" );
      lComponentReleaseTO.setFinishUnserviceable( false );
      lComponentReleaseTO.setGenerateNewBatchNumber( false );
      lComponentReleaseTO.setReleaseNumber( RELEASE_NUMBER, "" );
      lComponentReleaseTO.setReleaseNotes( RELEASE_NOTES, "" );

      // WHEN
      iWorkPackageSignatureService.sign( lSchedWPSignReqKey, lComponentReleaseTO );

      // THEN
      SchedWPSignTable lSchedWPSignTable = getSchedWPSignTable( lSchedWPSignReqKey.getPk() );
      SchedWPTable lSchedWPTable = getSchedWPTable( lRepairOrderTaskKey );

      assertThat( getWorkPackageStatus( lRepairOrderTaskKey ),
            equalTo( RefEventStatusKey.COMPLETE.getCd() ) );
      assertThat( getWPSignReqStatus( lSchedWPSignReqKey ),
            equalTo( RefWPSignReqStatusKey.COMPLETE ) );
      assertThat( lSchedWPSignTable.getHR(), equalTo( iValidHrKey ) );
      assertThat( lSchedWPSignTable.getSignOffDate().toString(), equalTo( TODAY.toString() ) );
      assertThat( lSchedWPTable.getReleaseRemarks(), equalTo( RELEASE_NOTES ) );
      assertThat( lSchedWPTable.getReleaseNumber(), equalTo( RELEASE_NUMBER ) );
      assertThat( lSchedWPTable.getReleaseToServiceBool(), is( false ) );
   }


   /**
    *
    * Test the result of signing a sign off requirement on a repair order (not the last row).
    * Expected results are:
    * <ul>
    * <li>requirement sign off status should be 'complete'</li>
    * <li>the recorded hr key and date should match input</li>
    * <li>the repair order status should remain 'in work'</li>
    * </ul>
    */
   @Test
   public void sign_componentWP_nonLastRow() throws MxException, TriggerException {

      // GIVEN
      TaskKey lRepairOrderTaskKey = createComponentWorkPackage();

      new WorkPackageSignatureRequirementBuilder().withLabourSkill( RefLabourSkillKey.LBR )
            .withWorkPackage( lRepairOrderTaskKey ).build();
      SchedWPSignReqKey lReqKeyEng =
            new WorkPackageSignatureRequirementBuilder().withLabourSkill( RefLabourSkillKey.ENG )
                  .withWorkPackage( lRepairOrderTaskKey ).build();

      ComponentReleaseTO lComponentReleaseTO = new ComponentReleaseTO( iValidHrKey, TODAY, "" );
      lComponentReleaseTO.setFinishUnserviceable( false );
      lComponentReleaseTO.setGenerateNewBatchNumber( false );

      // WHEN
      iWorkPackageSignatureService.sign( lReqKeyEng, lComponentReleaseTO );

      // THEN
      SchedWPSignTable lSchedWPSignTable = getSchedWPSignTable( lReqKeyEng.getPk() );

      assertThat( getWorkPackageStatus( lRepairOrderTaskKey ),
            equalTo( RefEventStatusKey.IN_WORK.getCd() ) );
      assertThat( getWPSignReqStatus( lReqKeyEng ), equalTo( RefWPSignReqStatusKey.COMPLETE ) );
      assertThat( lSchedWPSignTable.getHR(), equalTo( iValidHrKey ) );
      assertThat( lSchedWPSignTable.getSignOffDate().toString(), equalTo( TODAY.toString() ) );
   }


   private void setUpData() {

      iLocationKey = Domain.createLocation( loc -> {
         loc.setCode( "DFW" );
      } );

      UserKey lUserKey = Domain.createUser();
      OrgKey lOrgkey = Domain.createOrganization( new DomainConfiguration<Organization>() {

         @Override
         public void configure( Organization aBuilder ) {
            aBuilder.setType( RefOrgTypeKey.OPERATOR );
         }
      } );

      iValidHrKey = Domain.createHumanResource( new DomainConfiguration<HumanResource>() {

         @Override
         public void configure( HumanResource aBuilder ) {
            aBuilder.setOrganization( lOrgkey );
            aBuilder.setUser( lUserKey );
         }
      } );
   }


   private TaskKey createAircraftWorkPackage() {
      return Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {

            aWorkPackage.setAircraft( new InventoryBuilder().withClass( RefInvClassKey.ACFT )
                  .atLocation( iLocationKey ).build() );
            aWorkPackage.setActualStartDate( TODAY );
            aWorkPackage.setActualEndDate( TODAY );
            aWorkPackage.setStatus( RefEventStatusKey.IN_WORK );
            aWorkPackage.setLocation( iLocationKey );
         }
      } );
   }


   private TaskKey createComponentWorkPackage() {

      PartNoKey lPartKey = Domain.createPart();
      InventoryKey lInventoryKey =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aInventory ) {
                  aInventory.setLocation( iLocationKey );
                  aInventory.setPartNumber( lPartKey );
                  aInventory.setOriginalAssembly( Domain.createEngineAssembly() );
               }
            } );

      return Domain.createRepairOrder( new DomainConfiguration<RepairOrder>() {

         @Override
         public void configure( RepairOrder aRepairOrder ) {

            aRepairOrder.setStatus( RefEventStatusKey.IN_WORK );
            aRepairOrder.setMainInventory( lInventoryKey );
            aRepairOrder.setActualStartDate( TODAY );
            aRepairOrder.setActualEndDate( TODAY );
            aRepairOrder.setLocationKey( iLocationKey );
         }
      } );
   }


   private SchedWPTable getSchedWPTable( TaskKey aSchedWPKey ) {
      return iSchedWPDao.findByPrimaryKey( new SchedWPKey( aSchedWPKey ) );
   }


   private RefWPSignReqStatusKey getWPSignReqStatus( SchedWPSignReqKey aWPSignReqKey ) {
      return iSchedWpSignReqDao.findByPrimaryKey( aWPSignReqKey ).getSignReqStatus();
   }


   private SchedWPSignTable getSchedWPSignTable( SchedWPSignKey aWPSignKey ) {
      return iSchedWpSignDao.findByPrimaryKey( aWPSignKey );
   }


   private String getWorkPackageStatus( TaskKey aWorkPackageKey ) {
      return iEvtEventDao.findByPrimaryKey( aWorkPackageKey ).getEventStatusCd();
   }

}
