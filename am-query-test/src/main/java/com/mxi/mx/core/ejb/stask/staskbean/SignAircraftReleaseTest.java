package com.mxi.mx.core.ejb.stask.staskbean;

import static com.mxi.mx.common.utils.DateUtils.addDays;
import static com.mxi.mx.common.utils.DateUtils.floorMillisecond;
import static com.mxi.mx.core.key.RefInvCondKey.INREP;
import static com.mxi.mx.core.key.RefInvCondKey.INSRV;
import static com.mxi.mx.core.key.RefPartStatusKey.ACTV;
import static org.hamcrest.Matchers.is;

import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.common.trigger.TriggerFactoryStub;
import com.mxi.mx.core.ejb.stask.STaskBean;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.SchedWPSignReqKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.plugin.releasenumber.WorkOrderReleaseNumberGenerator;
import com.mxi.mx.core.services.stask.maintrelease.AircraftReleaseTO;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.trigger.MxCoreTriggerType;


/**
 * Integration unit test for validating the signing of an aircraft release.
 *
 * Exercising: {@link STaskBean#signAircraftRelease(SchedWPSignReqKey, TaskKey, AircraftReleaseTO)}
 *
 */
public class SignAircraftReleaseTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private static final Date WORKPACKAGE_ACTUAL_END_DATE =
         floorMillisecond( addDays( new Date(), -2 ) );
   private static final String RELEASE_NUMBER = "RELEASE_NUMBER";
   private static final String RELEASE_REMARKS = "RELEASE_REMARKS";

   private UserKey user;
   private HumanResourceKey signedByHr;

   private STaskBean staskbean;


   @Before
   public void before() throws Exception {

      user = Domain.createUser();
      signedByHr = Domain.createHumanResource( hr -> {
         hr.setUser( user );
      } );
      UserParameters.setInstance( user.getId(), "LOGIC",
            new UserParametersFake( user.getId(), "LOGIC" ) );

      // Need to setup then WorkOrderReleaseNumberGenerator in order for the logic to generate a
      // release number.
      TriggerFactoryStub triggerFactoryStub = ( TriggerFactoryStub ) TriggerFactory.getInstance();
      triggerFactoryStub.setTrigger( MxCoreTriggerType.WO_REL_NUM_GEN.toString(),
            new WorkOrderReleaseNumberGenerator() );

      // The logic uses SecurityIdentificationUtils to obtain the "current user" and its org_hr row.
      // Other stubs within the FakeJavaEeDependenciesRule create a "current user" but do not create
      // an org_hr row for that user. So we will do it here.
      Domain.createHumanResource( currentHr -> {
         currentHr.setUser(
               new UserKey( SecurityIdentificationUtils.getInstance().getCurrentUserId() ) );
      } );

      staskbean = new STaskBean();
      staskbean.setSessionContext( new SessionContextFake() );
   }


   @After
   public void after() throws Exception {
      UserParameters.setInstance( user.getId(), "LOGIC", null );
      TriggerFactoryStub triggerFactoryStub = ( TriggerFactoryStub ) TriggerFactory.getInstance();
      triggerFactoryStub.setTrigger( MxCoreTriggerType.WO_REL_NUM_GEN.toString(), null );
   }


   /**
    * Verifies that an aircraft's condition changes from In-Repair to In-Service after a work
    * package is completed by signing a sign-off requirement without releasing to service (and there
    * are no other started work packages against the aircraft).
    */
   @Test
   public void aircraftConditionChangesToInServiceWhenSigningWithoutReleaseToService()
         throws Exception {

      // Given an aircraft with a condition other than In-Service.
      InventoryKey aircraft = Domain.createAircraft( acft -> {
         acft.setCondition( INREP );
         // Required by logic but not applicable to test.
         acft.setPart( Domain.createPart( part -> {
            part.setPartStatus( ACTV );
         } ) );
      } );

      // Given an in-work work package against the aircraft with a sign-off requirement.
      TaskKey workpackage = Domain.createWorkPackage( wp -> {
         wp.setStatus( RefEventStatusKey.IN_WORK );
         wp.setAircraft( aircraft );
         wp.addSignOffRequirement();
         // Required by logic but not applicable to test.
         wp.setLocation( Domain.createLocation() );
      } );
      SchedWPSignReqKey signOffRequirement = Domain.readSignOffRequirements( workpackage ).get( 0 );

      // When the sign-off requirement is signed without releasing to service.
      AircraftReleaseTO aircraftReleaseTO = buildTransferObject();
      aircraftReleaseTO.setReleaseToService( false );

      staskbean.signAircraftRelease( signOffRequirement, workpackage, aircraftReleaseTO );

      // Then the aircraft condition changes to In-Service.
      RefInvCondKey actualCondition = InvInvTable.findByPrimaryKey( aircraft ).getInvCond();
      Assert.assertThat( "Unexpected aircraft condition.", actualCondition, is( INSRV ) );

   }


   /**
    * Verifies that an aircraft's condition changes from In-Repair to In-Service after a work
    * package is completed by signing a sign-off requirement and releasing to service (and there are
    * no other started work packages against the aircraft).
    */
   @Test
   public void aircraftConditionChangesToInServiceWhenSigningWithReleaseToService()
         throws Exception {

      // Given an aircraft with a condition other than In-Service.
      InventoryKey aircraft = Domain.createAircraft( acft -> {
         acft.setCondition( INREP );
         // Required by logic but not applicable to test.
         acft.setPart( Domain.createPart( part -> {
            part.setPartStatus( ACTV );
         } ) );
      } );

      // Given an in-work work package against the aircraft with a sign-off requirement.
      TaskKey workpackage = Domain.createWorkPackage( wp -> {
         wp.setStatus( RefEventStatusKey.IN_WORK );
         wp.setAircraft( aircraft );
         wp.addSignOffRequirement();
         // Required by logic but not applicable to test.
         wp.setLocation( Domain.createLocation() );
      } );
      SchedWPSignReqKey signOffRequirement = Domain.readSignOffRequirements( workpackage ).get( 0 );

      // When the sign-off requirement is signed with releasing to service.
      AircraftReleaseTO aircraftReleaseTO = buildTransferObject();
      aircraftReleaseTO.setReleaseToService( true );

      staskbean.signAircraftRelease( signOffRequirement, workpackage, aircraftReleaseTO );

      // Then the aircraft condition changes to In-Service.
      RefInvCondKey actualCondition = InvInvTable.findByPrimaryKey( aircraft ).getInvCond();
      Assert.assertThat( "Unexpected aircraft condition.", actualCondition, is( INSRV ) );

   }


   /**
    * Verifies that an aircraft's condition does not change after a work package is completed by
    * signing a sign-off requirement and releasing to service when there is another in-work work
    * packages against the aircraft.
    */
   @Test
   public void
         aircraftConditionDoesNotChangesToInServiceWhenSigningWithReleaseToServiceAndAnotherStartedWorkpackageExists()
               throws Exception {

      // Given an aircraft with a condition other than In-Service.
      RefInvCondKey originalCondition = INREP;
      InventoryKey aircraft = Domain.createAircraft( acft -> {
         acft.setCondition( originalCondition );
      } );

      // Given an in-work work package against the aircraft with a sign-off requirement.
      TaskKey workpackage = Domain.createWorkPackage( wp -> {
         wp.setStatus( RefEventStatusKey.IN_WORK );
         wp.setAircraft( aircraft );
         wp.addSignOffRequirement();
         // Required by logic but not applicable to test.
         wp.setLocation( Domain.createLocation() );
      } );
      SchedWPSignReqKey signOffRequirement = Domain.readSignOffRequirements( workpackage ).get( 0 );

      // Given another in-work work package against the same aircraft.
      Domain.createWorkPackage( wp -> {
         wp.setAircraft( aircraft );
         wp.setStatus( RefEventStatusKey.IN_WORK );
      } );

      // When the sign-off requirement is signed with releasing to service.
      AircraftReleaseTO aircraftReleaseTO = buildTransferObject();
      aircraftReleaseTO.setReleaseToService( true );

      staskbean.signAircraftRelease( signOffRequirement, workpackage, aircraftReleaseTO );

      // Then the aircraft condition does not change.
      RefInvCondKey actualCondition = InvInvTable.findByPrimaryKey( aircraft ).getInvCond();
      Assert.assertThat( "Unexpected aircraft condition.", actualCondition,
            is( originalCondition ) );

   }


   /**
    * Verifies that after a work package is completed by signing a sign-off requirement and
    * releasing to service that the aircraft's release-to-service information matches the
    * information provided by the sign off.
    *
    * Interesting note: the release-date in the transfer object is never read or used, instead the
    * wp-actual-end-date in the transfer object is used as the release date!
    */
   @Test
   public void aircraftReleaseToServiceInfoMatchesSigningReleaseToServiceInfo() throws Exception {

      // Given an aircraft.
      InventoryKey aircraft = Domain.createAircraft( acft -> {
         // Required by logic but not applicable to test.
         acft.setPart( Domain.createPart() );
      } );

      // Given an in-work work package against the aircraft with a sign-off requirement.
      TaskKey workpackage = Domain.createWorkPackage( wp -> {
         wp.setStatus( RefEventStatusKey.IN_WORK );
         wp.setAircraft( aircraft );
         wp.addSignOffRequirement();
         // Required by logic but not applicable to test.
         wp.setLocation( Domain.createLocation() );
      } );
      SchedWPSignReqKey signOffRequirement = Domain.readSignOffRequirements( workpackage ).get( 0 );

      // When the sign-off requirement is signed with releasing to service information.
      AircraftReleaseTO aircraftReleaseTO =
            new AircraftReleaseTO( signedByHr, WORKPACKAGE_ACTUAL_END_DATE, "" );
      aircraftReleaseTO.setReleaseToService( true );
      aircraftReleaseTO.setReleaseNumber( RELEASE_NUMBER, "" );
      aircraftReleaseTO.setReleaseNotes( RELEASE_REMARKS, "" );

      staskbean.signAircraftRelease( signOffRequirement, workpackage, aircraftReleaseTO );

      // Then the aircraft's release information matches that provided by the
      // sign off.
      Date actualReleaseDate = InvInvTable.findByPrimaryKey( aircraft ).getReleaseDate();
      String actualReleaseNumber = InvInvTable.findByPrimaryKey( aircraft ).getReleaseNumber();
      String actualReleaseRemarks = InvInvTable.findByPrimaryKey( aircraft ).getReleaseRemarks();

      Assert.assertThat( "Unexpected release date.", actualReleaseDate,
            is( WORKPACKAGE_ACTUAL_END_DATE ) );
      Assert.assertThat( "Unexpected release number.", actualReleaseNumber, is( RELEASE_NUMBER ) );
      Assert.assertThat( "Unexpected release remarks.", actualReleaseRemarks,
            is( RELEASE_REMARKS ) );
   }


   /**
    * Verifies that when a work package is completed by signing a sign-off requirement and releasing
    * to service but not providing a release number that the aircraft's release number will be set
    * to a string with the following pattern: "WO <id>". Where the <id> is the id portion of the
    * work package primary key in the database (to the user this would appear as a random number).
    */
   @Test
   public void
         aircraftReleaseNumberMatchesWpReleaseNumberAfterSigningReleaseToServiceWithNoReleaseNumber()
               throws Exception {

      // Given an aircraft.
      InventoryKey aircraft = Domain.createAircraft( acft -> {
         // Required by logic but not applicable to test.
         acft.setPart( Domain.createPart() );
      } );

      // Given an in-work work package against the aircraft with a sign-off requirement and a work
      // package number.
      TaskKey workpackage = Domain.createWorkPackage( wp -> {
         wp.setStatus( RefEventStatusKey.IN_WORK );
         wp.setAircraft( aircraft );
         wp.addSignOffRequirement();
         // Required by logic but not applicable to test.
         wp.setLocation( Domain.createLocation() );
      } );
      SchedWPSignReqKey signOffRequirement = Domain.readSignOffRequirements( workpackage ).get( 0 );

      // When the sign-off requirement is signed with releasing to service but the release number is
      // not provided.
      AircraftReleaseTO aircraftReleaseTO =
            new AircraftReleaseTO( signedByHr, WORKPACKAGE_ACTUAL_END_DATE, "" );
      aircraftReleaseTO.setReleaseToService( true );

      staskbean.signAircraftRelease( signOffRequirement, workpackage, aircraftReleaseTO );

      // Then the aircraft's release number will be a hard coded string based on the work package's
      // DB PK (see method comment).
      String actualReleaseNumber = InvInvTable.findByPrimaryKey( aircraft ).getReleaseNumber();
      String expectedReleaseNumber = "WO - " + workpackage.getId();
      Assert.assertThat( "Unexpected release number.", actualReleaseNumber,
            is( expectedReleaseNumber ) );
   }


   private AircraftReleaseTO buildTransferObject() throws Exception {
      return new AircraftReleaseTO( signedByHr, WORKPACKAGE_ACTUAL_END_DATE, "" );
   }

}
