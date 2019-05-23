
package com.mxi.mx.core.services.stask.maintrelease;

import static com.mxi.am.domain.Domain.createRepairOrder;
import static com.mxi.am.domain.Domain.createWorkPackage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Date;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.RepairOrder;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.WorkPackageSignatureRequirementBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.exception.InvalidReftermException;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.EsigDocKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefCdKey;
import com.mxi.mx.core.key.RefInvCapabilityKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefReceiveCondKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.key.SchedWPSignEsigKey;
import com.mxi.mx.core.key.SchedWPSignReqKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.esigner.DocumentSignature;
import com.mxi.mx.core.services.esigner.EsignatureInterface;
import com.mxi.mx.core.services.inventory.oper.AircraftOperationalService;
import com.mxi.mx.core.services.stask.labour.WorkPackageSignatureService;
import com.mxi.mx.core.table.ref.RefInvCapability;
import com.mxi.mx.core.table.sched.JdbcSchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.unittest.table.stask.SchedStaskUtil;


/**
 * This class tests the {@link WorkPackageCompletionServiceImpl} class.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class WorkPackageCompletionServiceTest {

   private static final String TEST_CAPABILITY = "TESTCAPA";

   private TestableAircraftOperationalService iAircraftOperationalService;

   private Mockery iContext = new Mockery();

   private HumanResourceKey iHr;

   private EsignatureInterface iMockEsignatureService;

   private WorkPackageSignatureService iMockSignatureService;

   private WorkPackageCompletionServiceImpl iService;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private SchedStaskDao iMockSchedWPDao = new JdbcSchedStaskDao();


   /**
    * Tests that capability is updated using the AircraftOperationalService.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testThatCapabilityIsUpdated() throws Exception {
      final InventoryKey lAircraft = new InventoryBuilder().build();
      TaskKey lWorkPackage = createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( lAircraft );
         }
      } );
      final SchedWPSignReqKey lWPSignatureRequirementKey =
            new WorkPackageSignatureRequirementBuilder().build();

      iContext.checking( new Expectations() {

         {
            allowing( iMockSignatureService );
            allowing( iMockEsignatureService ).isESignatureRequired( with( equal( iHr ) ),
                  with( equal( lWPSignatureRequirementKey ) ) );
         }
      } );

      // build the TO with a valid condition
      final AircraftReleaseTO lAircraftReleaseTO = new AircraftReleaseTO( iHr, new Date(), "" );
      lAircraftReleaseTO.setCapability( TEST_CAPABILITY );

      // sign the relase
      iService.signAircraftRelease( lWPSignatureRequirementKey, lWorkPackage, lAircraftReleaseTO );

      iAircraftOperationalService.assertCapabilityWasSet( new AircraftKey( lAircraft ),
            new RefInvCapabilityKey( 1234, TEST_CAPABILITY ) );
   }


   /**
    * Tests that an electronic signature is created when enabled and the labour requires it.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatEsigCreatedWhenLabourRequiresEsigForAircraftRelease() throws Exception {

      // enable electronic signatures
      GlobalParameters.getInstance( ParmTypeEnum.LOGIC ).setBoolean( "ENABLE_MR_ESIG", true );

      // require esign for ENG labour skill
      RefLabourSkillKey lEngLabourSkill = RefLabourSkillKey.ENG;
      updateEsigReqBool( lEngLabourSkill, true );

      final TaskKey lWorkPackage = createWorkPackage();
      final SchedWPSignReqKey lWPSignatureRequirementKey =
            new WorkPackageSignatureRequirementBuilder().withLabourSkill( lEngLabourSkill ).build();

      final AircraftReleaseTO lAircraftReleaseTO = new AircraftReleaseTO( iHr, new Date(), "" );

      iContext.checking( new Expectations() {

         {
            allowing( iMockSignatureService );

            one( iMockEsignatureService ).createElectronicSignature(
                  with( equal( lWPSignatureRequirementKey ) ),
                  with( any( DocumentSignature.class ) ), with( equal( true ) ),
                  with( equal( iHr ) ) );
            will( returnValue( new SchedWPSignEsigKey( lWPSignatureRequirementKey,
                  new EsigDocKey( 1234, 1 ) ) ) );

            allowing( iMockEsignatureService ).isESignatureRequired( with( equal( iHr ) ),
                  with( equal( lWPSignatureRequirementKey ) ) );
            will( returnValue( true ) );
         }
      } );

      // sign the relase
      iService.signAircraftRelease( lWPSignatureRequirementKey, lWorkPackage, lAircraftReleaseTO );

      iContext.assertIsSatisfied();
   }


   /**
    * Tests that an electronic signature is created when enabled and the labour requires it.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatEsigCreatedWhenLabourRequiresEsigForComponentRelease() throws Exception {

      // enable electronic signatures
      GlobalParameters.getInstance( ParmTypeEnum.LOGIC ).setBoolean( "ENABLE_MR_ESIG", true );

      // require esign for ENG labour skill
      RefLabourSkillKey lEngLabourSkill = RefLabourSkillKey.ENG;
      updateEsigReqBool( lEngLabourSkill, true );

      final InventoryKey lComponent =
            new InventoryBuilder().withClass( RefInvClassKey.ASSY ).build();
      final TaskKey lRepairOrder = createRepairOrder( new DomainConfiguration<RepairOrder>() {

         @Override
         public void configure( RepairOrder aBuilder ) {
            aBuilder.setMainInventory( lComponent );
         }
      } );
      final SchedWPSignReqKey lWPSignatureRequirementKey =
            new WorkPackageSignatureRequirementBuilder().withLabourSkill( lEngLabourSkill ).build();

      final ComponentReleaseTO lComponentReleaseTO = new ComponentReleaseTO( iHr, new Date(), "" );
      lComponentReleaseTO.setFinishUnserviceable( true );
      lComponentReleaseTO.setGenerateNewBatchNumber( true );

      iContext.checking( new Expectations() {

         {
            allowing( iMockSignatureService );

            one( iMockEsignatureService ).createElectronicSignature(
                  with( equal( lWPSignatureRequirementKey ) ),
                  with( any( DocumentSignature.class ) ), with( equal( true ) ),
                  with( equal( iHr ) ) );
            will( returnValue( new SchedWPSignEsigKey( lWPSignatureRequirementKey,
                  new EsigDocKey( 4650, 1 ) ) ) );

            allowing( iMockEsignatureService ).isESignatureRequired( with( equal( iHr ) ),
                  with( equal( lWPSignatureRequirementKey ) ) );
            will( returnValue( true ) );
         }
      } );

      iService.signComponentRelease( lWPSignatureRequirementKey, lRepairOrder,
            lComponentReleaseTO );

      iContext.assertIsSatisfied();
   }


   /**
    * Tests that an electronic signature is not created when enabled and the labour does not require
    * it.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatEsigNotCreatedWhenLabourDoesNotRequireEsigForAircraftRelease()
         throws Exception {

      // enable electronic signatures
      GlobalParameters.getInstance( ParmTypeEnum.LOGIC ).setBoolean( "ENABLE_MR_ESIG", true );

      // do not require esign for ENG labour skill
      RefLabourSkillKey lEngLabourSkill = RefLabourSkillKey.ENG;
      updateEsigReqBool( lEngLabourSkill, false );

      final TaskKey lWorkPackage = Domain.createWorkPackage();
      final SchedWPSignReqKey lWPSignatureRequirementKey =
            new WorkPackageSignatureRequirementBuilder().withLabourSkill( lEngLabourSkill ).build();

      final AircraftReleaseTO lAircraftReleaseTO = new AircraftReleaseTO( iHr, new Date(), "" );

      iContext.checking( new Expectations() {

         {
            allowing( iMockSignatureService );
            allowing( iMockEsignatureService ).isESignatureRequired( with( equal( iHr ) ),
                  with( equal( lWPSignatureRequirementKey ) ) );
         }
      } );

      // sign the relase
      iService.signAircraftRelease( lWPSignatureRequirementKey, lWorkPackage, lAircraftReleaseTO );
      iContext.assertIsSatisfied();
   }


   /**
    * Tests that an electronic signature is not created when enabled and the labour does not require
    * it.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatEsigNotCreatedWhenLabourDoesNotRequireEsigForComponentRelease()
         throws Exception {

      // enable electronic signatures
      GlobalParameters.getInstance( ParmTypeEnum.LOGIC ).setBoolean( "ENABLE_MR_ESIG", true );

      // require esign for ENG labour skill
      RefLabourSkillKey lEngLabourSkill = RefLabourSkillKey.ENG;
      updateEsigReqBool( lEngLabourSkill, false );

      final InventoryKey lComponent =
            new InventoryBuilder().withClass( RefInvClassKey.ASSY ).build();
      final TaskKey lRepairOrder = createRepairOrder( new DomainConfiguration<RepairOrder>() {

         @Override
         public void configure( RepairOrder aBuilder ) {
            aBuilder.setMainInventory( lComponent );
         }
      } );
      final SchedWPSignReqKey lWPSignatureRequirementKey =
            new WorkPackageSignatureRequirementBuilder().withLabourSkill( lEngLabourSkill ).build();

      final ComponentReleaseTO lComponentReleaseTO = new ComponentReleaseTO( iHr, new Date(), "" );
      lComponentReleaseTO.setFinishUnserviceable( true );
      lComponentReleaseTO.setGenerateNewBatchNumber( true );

      iContext.checking( new Expectations() {

         {
            allowing( iMockSignatureService );
            allowing( iMockEsignatureService ).isESignatureRequired( with( equal( iHr ) ),
                  with( equal( lWPSignatureRequirementKey ) ) );
         }
      } );

      iService.signComponentRelease( lWPSignatureRequirementKey, lRepairOrder,
            lComponentReleaseTO );

      iContext.assertIsSatisfied();
   }


   /**
    * Tests that ETOPS ready is updated using the AircraftOperationalService.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testThatEtopsReadyIsUpdated() throws Exception {
      final InventoryKey lAircraft = new InventoryBuilder().build();
      TaskKey lWorkPackage = createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aBuilder ) {
            aBuilder.setAircraft( lAircraft );
         }
      } );
      final SchedWPSignReqKey lWPSignatureRequirementKey =
            new WorkPackageSignatureRequirementBuilder().build();

      iContext.checking( new Expectations() {

         {
            allowing( iMockSignatureService );
            allowing( iMockEsignatureService ).isESignatureRequired( with( equal( iHr ) ),
                  with( equal( lWPSignatureRequirementKey ) ) );
         }
      } );

      // build the TO with a valid condition
      final AircraftReleaseTO lAircraftReleaseTO = new AircraftReleaseTO( iHr, new Date(), "" );
      lAircraftReleaseTO.setETOPSReady( true );

      // sign the relase
      iService.signAircraftRelease( lWPSignatureRequirementKey, lWorkPackage, lAircraftReleaseTO );

      iAircraftOperationalService.assertEtopsReadyWasSet( new AircraftKey( lAircraft ), true );
   }


   /**
    * Tests that when an invalid reference term is used for the capability, an exception is thrown.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testThatInvalidReftermThrowsExceptionForAircraftRelease() throws Exception {
      TaskKey lWorkPackage = Domain.createWorkPackage();
      SchedWPSignReqKey lWPSignatureRequirementKey =
            new WorkPackageSignatureRequirementBuilder().build();

      // build the TO with an invalid condition
      AircraftReleaseTO lAircraftReleaseTO = new AircraftReleaseTO( iHr, new Date(), "" );
      lAircraftReleaseTO.setCapability( "INVALID" );

      try {

         // sign the relase
         iService.signAircraftRelease( lWPSignatureRequirementKey, lWorkPackage,
               lAircraftReleaseTO );
         fail( "Expected InvalidReftermException" );
      } catch ( InvalidReftermException eException ) {
         assertEquals( "[MXERR-30415] " + i18n.get( "core.err.30415", "INVALID" ),
               eException.getMessage() );
      }
   }


   /**
    * Tests that when an invalid reference term is used for the condition, an exception is thrown.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testThatInvalidReftermThrowsExceptionForComponentRelease() throws Exception {
      final InventoryKey lComponent =
            new InventoryBuilder().withClass( RefInvClassKey.ASSY ).build();
      TaskKey lRO = createRepairOrder( new DomainConfiguration<RepairOrder>() {

         @Override
         public void configure( RepairOrder aRepairOrder ) {
            aRepairOrder.setMainInventory( lComponent );
         }
      } );
      SchedWPSignReqKey lWPSignatureRequirementKey =
            new WorkPackageSignatureRequirementBuilder().build();

      // build the TO with an invalid condition
      ComponentReleaseTO lComponentReleaseTO = new ComponentReleaseTO( iHr, new Date(), "" );
      lComponentReleaseTO.setCondition( "INVALID" );
      lComponentReleaseTO.setFinishUnserviceable( true );
      lComponentReleaseTO.setReleaseNumber( new Date().toString(), "" );

      try {
         iService.signComponentRelease( lWPSignatureRequirementKey, lRO, lComponentReleaseTO );
         fail( "Expected InvalidReftermException" );
      } catch ( InvalidReftermException eException ) {
         assertEquals( "[MXERR-30415] " + i18n.get( "core.err.30415", "INVALID" ),
               eException.getMessage() );
      }
   }


   /**
    * Tests that when the condition is provided, the sched_stask.receive_cond_db_id/cd is updated.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatReceiveConditionIsUpdatedWhenProvided() throws Exception {
      final InventoryKey lComponent =
            new InventoryBuilder().withClass( RefInvClassKey.ASSY ).build();
      TaskKey lRO = createRepairOrder( new DomainConfiguration<RepairOrder>() {

         @Override
         public void configure( RepairOrder aRepairOrder ) {
            aRepairOrder.setMainInventory( lComponent );
         }
      } );
      final SchedWPSignReqKey lWPSignatureRequirementKey =
            new WorkPackageSignatureRequirementBuilder().build();

      // ensure the condition is null at first
      new SchedStaskUtil( lRO ).assertReceivedCond( null );

      iContext.checking( new Expectations() {

         {
            allowing( iMockSignatureService );
            allowing( iMockEsignatureService ).isESignatureRequired( with( equal( iHr ) ),
                  with( equal( lWPSignatureRequirementKey ) ) );
         }
      } );

      ComponentReleaseTO lComponentReleaseTO = new ComponentReleaseTO( iHr, new Date(), "" );
      lComponentReleaseTO.setCondition( RefReceiveCondKey.REP.getCd() );

      iService.signComponentRelease( lWPSignatureRequirementKey, lRO, lComponentReleaseTO );

      // ensure the condition was updated
      new SchedStaskUtil( lRO ).assertReceivedCond( RefReceiveCondKey.REP );
   }


   /**
    * Tests that the WorkPackageSignatureService is correctly called to sign the signature.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatSignatureIsSignedForAircraftRelease() throws Exception {
      final InventoryKey lAircraft = new InventoryBuilder().build();
      final TaskKey lWorkPackage = createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aBuilder ) {
            aBuilder.setAircraft( lAircraft );
         }
      } );
      final SchedWPSignReqKey lWPSignatureRequirementKey =
            new WorkPackageSignatureRequirementBuilder().build();

      final AircraftReleaseTO lAircraftReleaseTO = new AircraftReleaseTO( iHr, new Date(), "" );

      iContext.checking( new Expectations() {

         {
            one( iMockSignatureService ).sign( with( equal( lWPSignatureRequirementKey ) ),
                  with( same( lAircraftReleaseTO ) ) );

            allowing( iMockEsignatureService ).isESignatureRequired( with( equal( iHr ) ),
                  with( equal( lWPSignatureRequirementKey ) ) );
         }
      } );

      // sign the release
      iService.signAircraftRelease( lWPSignatureRequirementKey, lWorkPackage, lAircraftReleaseTO );

      iContext.assertIsSatisfied();
   }


   /**
    * Tests that the WorkPackageSignatureService is correctly called to sign the signature.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatSignatureIsSignedForComponentRelease() throws Exception {
      final InventoryKey lComponent =
            new InventoryBuilder().withClass( RefInvClassKey.ASSY ).build();
      final TaskKey lRO = createRepairOrder( new DomainConfiguration<RepairOrder>() {

         @Override
         public void configure( RepairOrder aRepairOrder ) {
            aRepairOrder.setMainInventory( lComponent );
         }
      } );
      final SchedWPSignReqKey lWPSignatureRequirementKey =
            new WorkPackageSignatureRequirementBuilder().build();

      final ComponentReleaseTO lComponentReleaseTO = new ComponentReleaseTO( iHr, new Date(), "" );
      lComponentReleaseTO.setFinishUnserviceable( true );
      lComponentReleaseTO.setGenerateNewBatchNumber( true );

      iContext.checking( new Expectations() {

         {
            one( iMockSignatureService ).sign( with( equal( lWPSignatureRequirementKey ) ),
                  with( equal( lComponentReleaseTO ) ) );
            allowing( iMockEsignatureService ).isESignatureRequired( with( equal( iHr ) ),
                  with( equal( lWPSignatureRequirementKey ) ) );

         }
      } );

      iService.signComponentRelease( lWPSignatureRequirementKey, lRO, lComponentReleaseTO );

      iContext.assertIsSatisfied();
   }


   @Before
   public void setUp() throws Exception {
      iMockSignatureService = iContext.mock( WorkPackageSignatureService.class );
      iMockEsignatureService = iContext.mock( EsignatureInterface.class );

      iAircraftOperationalService = new TestableAircraftOperationalService();

      // data setup
      RefInvCapability lRefInvCapability = RefInvCapability.create( TEST_CAPABILITY );
      lRefInvCapability.insert();

      iHr = new HumanResourceDomainBuilder().build();

      // create service class to be tested
      iService = new WorkPackageCompletionServiceImpl( iAircraftOperationalService,
            iMockSignatureService, iMockEsignatureService,
            GlobalParameters.getInstance( ParmTypeEnum.LOGIC ), iMockSchedWPDao );
   }


   /**
    * Updates the ESIG_REQ_BOOL column on the REF_LABOUR_SKILL table.
    *
    * @param aEngLabourSkill
    *           The labour skill
    * @param aValue
    *           The new value
    */
   private void updateEsigReqBool( RefLabourSkillKey aEngLabourSkill, boolean aValue ) {
      DataSetArgument lSetArgs = new DataSetArgument();
      lSetArgs.add( "esig_req_bool", aValue );
      MxDataAccess.getInstance().executeUpdate( aEngLabourSkill.getTableName(), lSetArgs,
            aEngLabourSkill.getPKWhereArg() );
   }


   /**
    * This is a testable version of the aircraft operational service.
    */
   private static class TestableAircraftOperationalService extends AircraftOperationalService {

      private AircraftKey iSetCapabilityAircraft;

      private RefCdKey iSetCapabilityCapability;

      private AircraftKey iSetEtopsReadyAircraft;

      private boolean iSetEtopsReadyBool;


      /**
       * Asserts that setCapability was called with the given arguments.
       *
       * @param aAircraft
       *           The expected aircraft
       * @param aCapability
       *           The expected capability
       */
      public void assertCapabilityWasSet( AircraftKey aAircraft, RefInvCapabilityKey aCapability ) {
         assertEquals( aAircraft, iSetCapabilityAircraft );
         assertEquals( aCapability, iSetCapabilityCapability );
      }


      /**
       * Asserts that setETOPSReady was called with the given arguments.
       *
       * @param aAircraft
       *           The expected aircraft
       * @param aETOPSReady
       *           The expected boolean for ETOPS ready
       */
      public void assertEtopsReadyWasSet( AircraftKey aAircraft, boolean aETOPSReady ) {
         assertEquals( aAircraft, iSetEtopsReadyAircraft );
         assertEquals( aETOPSReady, iSetEtopsReadyBool );
      }


      @Override
      public void setCapability( AircraftKey aAircraft, RefCdKey aCapability,
            RefStageReasonKey aReason, Date aLocalDate, Date aGMTDate,
            HumanResourceKey aAuthorizingHR, String aNote ) throws MxException, TriggerException {
         iSetCapabilityAircraft = aAircraft;
         iSetCapabilityCapability = aCapability;
      }


      @Override
      public void setETOPSReady( AircraftKey aAircraft, boolean aETOPSReady ) {
         iSetEtopsReadyAircraft = aAircraft;
         iSetEtopsReadyBool = aETOPSReady;
      }
   }
}
