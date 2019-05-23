package com.mxi.am.api.resource.maintenance.exec.work.pkg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.ibm.icu.util.Calendar;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.maintenance.exec.work.pkg.impl.WorkpackageControllerBean;
import com.mxi.am.api.resource.maintenance.exec.work.pkg.impl.WorkpackageControllerInterface;
import com.mxi.am.api.resource.maintenance.exec.work.pkg.impl.WorkpackageResourceBean;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.ejb.task.STaskLocalHomeStub;
import com.mxi.mx.common.ejb.task.TaskLocaHomelStub;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.common.trigger.TriggerFactoryStub;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.core.ejb.stask.STaskLocalHome;
import com.mxi.mx.core.ejb.stask.TaskLocalHome;
import com.mxi.mx.core.maintenance.exec.fault.domain.FaultRepository;
import com.mxi.mx.core.maintenance.exec.fault.infra.JdbcFaultRepository;
import com.mxi.mx.core.maintenance.plan.deferralreference.domain.DeferralReferenceRepository;
import com.mxi.mx.core.maintenance.plan.deferralreference.infra.JdbcDeferralReferenceRepository;
import com.mxi.mx.core.plugin.ordernumber.RepairOrderNumberGenerator;
import com.mxi.mx.core.plugin.ordernumber.WorkOrderNumberGenerator;
import com.mxi.mx.core.table.acevent.InvCndChgEventDao;
import com.mxi.mx.core.table.acevent.InvCndChgInvDao;
import com.mxi.mx.core.table.acevent.JdbcInvCndChgEventDao;
import com.mxi.mx.core.table.acevent.JdbcInvCndChgInvDao;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtStageDao;
import com.mxi.mx.core.table.evt.JdbcEvtEventDao;
import com.mxi.mx.core.table.evt.JdbcEvtStageDao;
import com.mxi.mx.core.table.sched.JdbcSchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.task.JdbcSchedWPDao;
import com.mxi.mx.core.table.task.SchedWPDao;
import com.mxi.mx.core.table.utl.JdbcUtlConfigParmDao;
import com.mxi.mx.core.table.utl.UtlConfigParmDao;
import com.mxi.mx.core.trigger.MxCoreTriggerType;
import com.mxi.mx.repository.fault.repairreference.JdbcRepairReferenceRepository;
import com.mxi.mx.repository.fault.repairreference.RepairReferenceRepository;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Smoke test class for WorkpackageResourceBean
 *
 *
 */

@RunWith( MockitoJUnitRunner.class )
public class WorkpackageResourceBeanTest extends ResourceBeanTest {

   private WorkpackageResourceBean workpackageResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext ejbContext;

   private static final String WP_DELAY_ID = "28B2673ABD6249DBBA36F2B3173B086E";
   private static final String IN_WORK_WP_ID = "AC9D774765E54A7080D166BB5CDD7649";
   private static final String COMPLETE_WP_ID = "FC05D81C502A4FFE9567A22A989AD3D9";
   private static final String WP_UPDATE_ID = "29B2673ABD6249DBBB36F2B3173B08FF";

   private static final DateFormat DATE_FORMAT = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

   private Workpackage workpackage1;
   private Workpackage workpackage2;

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( WorkpackageResource.class ).to( WorkpackageResourceBean.class );
               bind( WorkpackageControllerInterface.class ).to( WorkpackageControllerBean.class );
               bind( EvtEventDao.class ).to( JdbcEvtEventDao.class );
               bind( EvtStageDao.class ).to( JdbcEvtStageDao.class );
               bind( SchedStaskDao.class ).to( JdbcSchedStaskDao.class );
               bind( SchedWPDao.class ).to( JdbcSchedWPDao.class );
               bind( UtlConfigParmDao.class ).to( JdbcUtlConfigParmDao.class );
               bind( TaskLocalHome.class ).to( TaskLocaHomelStub.class );
               bind( STaskLocalHome.class ).to( STaskLocalHomeStub.class );
               bind( Security.class ).to( CoreSecurity.class );
               bind( EJBContext.class ).toInstance( ejbContext );
               bind( InvCndChgEventDao.class ).to( JdbcInvCndChgEventDao.class );
               bind( InvCndChgInvDao.class ).to( JdbcInvCndChgInvDao.class );
               bind( FaultRepository.class ).to( JdbcFaultRepository.class );
               bind( DeferralReferenceRepository.class )
                     .to( JdbcDeferralReferenceRepository.class );
               bind( RepairReferenceRepository.class ).to( JdbcRepairReferenceRepository.class );
            }
         } );


   @Before
   public void setUp() throws MxException, ParseException {

      workpackageResourceBean =
            InjectorContainer.get().getInstance( WorkpackageResourceBean.class );

      workpackageResourceBean.setEJBContext( ejbContext );

      Map<String, Object> triggerMap = new HashMap<>( 2 );
      {
         triggerMap.put( MxCoreTriggerType.WO_NUM_GEN.toString(), new WorkOrderNumberGenerator() );
         triggerMap.put( MxCoreTriggerType.RO_NUM_GEN.toString(),
               new RepairOrderNumberGenerator() );
      }
      TriggerFactory triggerFactoryStub = new TriggerFactoryStub( triggerMap );
      TriggerFactory.setInstance( triggerFactoryStub );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );
      initializeTest();
      createExpectedResults();

      Mockito.when( ejbContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );
   }


   @Test
   @CSIContractTest( Project.SWA_AC_STATUS )
   public void search_success_inWorkWorkpackagesByAircraftId() {
      WorkpackageSearchParameters params = new WorkpackageSearchParameters();
      params.setStatus( "IN WORK" );
      params.setHighestInvIds( Arrays.asList( workpackage2.getAircraftId() ) );

      List<Workpackage> results = workpackageResourceBean.search( params );

      assertEquals( "Search did not produce the expected number of results: ", 1, results.size() );
      Workpackage workpackage = results.get( 0 );
      assertEquals( "The id of the returned workpackage is not the expected value: ",
            workpackage2.getId(), workpackage.getId() );
      assertEquals( "The aircraft id of the returned workpackage is not the expected value: ",
            workpackage2.getAircraftId(), workpackage.getAircraftId() );
      assertEquals( "The status of the returned workpackage is not the expected value: ", "IN WORK",
            workpackage.getStatus() );
      assertEquals( "The location id of the returned workpackage is not the expected value: ",
            workpackage2.getLocationId(), workpackage.getLocationId() );

   }


   @Test
   public void update_success_updateName()
         throws AmApiBusinessException, AmApiResourceNotFoundException {

      final String newName = "newWorkpackageName";

      Workpackage lWorkpackage = workpackageResourceBean.get( workpackage1.getId() );
      lWorkpackage.setName( newName );

      Workpackage updatedWorkpackage =
            workpackageResourceBean.update( lWorkpackage.getId(), lWorkpackage );

      assertEquals( "Returned workpackage's id is incorrect: ", workpackage1.getId(),
            updatedWorkpackage.getId() );
      assertEquals( "Workpackage's new name is incorrect", newName, updatedWorkpackage.getName() );
   }


   @CSIContractTest( Project.SWA_FQC )
   @Test
   public void update_success_startWorkpackage()
         throws AmApiBusinessException, AmApiResourceNotFoundException {

      final String newStatus = "IN WORK";

      Workpackage startWorkpackage = workpackageResourceBean.get( workpackage1.getId() );
      startWorkpackage.setStatus( newStatus );

      Workpackage lUpdatedWpAfterResponse =
            workpackageResourceBean.update( startWorkpackage.getId(), startWorkpackage );

      Workpackage lUpdatedWp = workpackageResourceBean.get( startWorkpackage.getId() );
      assertEquals( "Workpackage's new status should be set to 'IN WORK'", newStatus,
            lUpdatedWpAfterResponse.getStatus() );
      assertEquals( "Workpackage's new status should be set to 'IN WORK'", newStatus,
            lUpdatedWp.getStatus() );
   }


   @Test
   public void update_success_delayWorkpackage()
         throws AmApiBusinessException, AmApiResourceNotFoundException, ParseException {
      // Schedule and start the workpackage
      Workpackage lDelayWorkpackage = workpackageResourceBean.get( WP_DELAY_ID );

      Calendar lDelayCalendar = Calendar.getInstance();
      lDelayWorkpackage.setSchedStartDate( lDelayCalendar.getTime() );
      lDelayCalendar.add( Calendar.DATE, 5 );
      lDelayWorkpackage.setSchedEndDate( lDelayCalendar.getTime() );
      lDelayWorkpackage.setStatus( "IN WORK" );
      workpackageResourceBean.update( WP_DELAY_ID, lDelayWorkpackage );

      lDelayWorkpackage = workpackageResourceBean.get( WP_DELAY_ID );
      assertEquals( "Workpackage's new status should be set to 'IN WORK'", "IN WORK",
            lDelayWorkpackage.getStatus() );

      // then delay it to 5 days after scheduled end date
      lDelayCalendar.add( Calendar.DATE, 5 );
      lDelayCalendar.set( Calendar.MILLISECOND, 0 ); // millis = 0 because they get truncated
      lDelayWorkpackage.setEndDate( lDelayCalendar.getTime() );
      workpackageResourceBean.update( WP_DELAY_ID, lDelayWorkpackage );

      Workpackage lUpdatedWorkpackage = workpackageResourceBean.get( WP_DELAY_ID );

      // make sure it has been delayed
      assertEquals( "Work package end date should have been changed: ", lDelayCalendar.getTime(),
            lUpdatedWorkpackage.getEndDate() );
   }


   @Test
   public void update_success_setLocationToNull()
         throws AmApiResourceNotFoundException, AmApiBusinessException {

      Workpackage workpackageToBeUpdated = workpackageResourceBean.get( WP_UPDATE_ID );
      String locationId = workpackageToBeUpdated.getLocationId();

      workpackageToBeUpdated.setLocationId( null );

      Workpackage updatedWorkpackage = workpackageResourceBean
            .update( workpackageToBeUpdated.getId(), workpackageToBeUpdated );

      assertEquals( "Location should not have been updated.", locationId,
            updatedWorkpackage.getLocationId() );
   }


   @Test
   public void update_success_emptyACTVWorkpackageLocation()
         throws AmApiResourceNotFoundException, AmApiBusinessException {

      Workpackage workpackageToBeUpdated = workpackageResourceBean.get( WP_UPDATE_ID );

      workpackageToBeUpdated.setLocationId( "" );

      Workpackage updatedWorkpackage = workpackageResourceBean
            .update( workpackageToBeUpdated.getId(), workpackageToBeUpdated );

      assertNull( "Location should have been updated to null for ACTV workpackage.",
            updatedWorkpackage.getLocationId() );
   }


   @CSIContractTest( Project.SWA_FQC )
   @Test
   public void update_failure_removeLocationOfCommitWorkpackage()
         throws AmApiBusinessException, AmApiResourceNotFoundException {

      Workpackage lStartWorkpackage = workpackageResourceBean.get( workpackage1.getId() );
      lStartWorkpackage.setLocationId( "" );

      try {
         workpackageResourceBean.update( lStartWorkpackage.getId(), lStartWorkpackage );
         Assert.fail( "Did not throw AmApiBusinessException" );
      } catch ( AmApiBusinessException e ) {
         Assert.assertTrue( e.getMessage().contains(
               "Cannot reset the scheduled location of a work package with a status of COMMIT, IN WORK or COMPLETE" ) );
      }
   }


   @CSIContractTest( Project.SWA_FQC )
   @Test
   public void update_failure_removeLocationOfInWorkWorkpackage()
         throws AmApiBusinessException, AmApiResourceNotFoundException {

      Workpackage lStartWorkpackage = workpackageResourceBean.get( IN_WORK_WP_ID );
      lStartWorkpackage.setLocationId( "" );

      try {
         workpackageResourceBean.update( lStartWorkpackage.getId(), lStartWorkpackage );
         Assert.fail( "Did not throw AmApiBusinessException" );
      } catch ( AmApiBusinessException e ) {
         Assert.assertTrue( e.getMessage().contains(
               "Cannot reset the scheduled location of a work package with a status of COMMIT, IN WORK or COMPLETE" ) );
      }
   }


   @CSIContractTest( Project.SWA_FQC )
   @Test
   public void update_failure_removeLocationOfCompleteWorkpackage()
         throws AmApiBusinessException, AmApiResourceNotFoundException {

      Workpackage lStartWorkpackage = workpackageResourceBean.get( COMPLETE_WP_ID );
      lStartWorkpackage.setLocationId( "" );

      try {
         workpackageResourceBean.update( lStartWorkpackage.getId(), lStartWorkpackage );
         Assert.fail( "Did not throw AmApiBusinessException" );
      } catch ( AmApiBusinessException e ) {
         Assert.assertTrue( e.getMessage().contains(
               "Cannot reset the scheduled location of a work package with a status of COMMIT, IN WORK or COMPLETE" ) );
      }
   }


   @Test
   public void create_success_createWithoutLocation()
         throws ParseException, AmApiBusinessException {
      Workpackage workpackageToCreate = getWorkpackageToCreate();
      workpackageToCreate.setLocationId( null );

      Workpackage createdWorkpackage = workpackageResourceBean.create( workpackageToCreate );

      assertEquals( "Incorrect Inventory ID:", workpackageToCreate.getInventoryId(),
            createdWorkpackage.getInventoryId() );
      assertEquals( "Incorrect Name:", workpackageToCreate.getName(),
            createdWorkpackage.getName() );
      assertEquals( "IncorrectSched End Date:", workpackageToCreate.getSchedEndDate(),
            createdWorkpackage.getSchedEndDate() );
      assertEquals( "Incorrect Sched Start Date:", workpackageToCreate.getSchedStartDate(),
            createdWorkpackage.getSchedStartDate() );
      assertNull( "Location should be null.", createdWorkpackage.getLocationId() );

   }


   private Workpackage getWorkpackageToCreate() throws ParseException {
      Workpackage workpackage = new Workpackage();
      workpackage.setInventoryId( "601435E495494F34965B1588F5A6036D" );
      workpackage.setName( "Test Create WP" );
      workpackage.setSchedStartDate( DATE_FORMAT.parse( "2020-03-17 15:00:00" ) );
      workpackage.setSchedEndDate( DATE_FORMAT.parse( "2020-03-17 17:00:00" ) );
      workpackage.setRequestParts( false );
      workpackage.setHeavyMaintenanceBool( false );
      return workpackage;

   }


   private void createExpectedResults() throws ParseException {
      workpackage1 = new Workpackage();
      workpackage1.setName( "Started Workpackage" );
      workpackage1.setId( "29B2673ABD6249DBBB36F2B3173B086E" );
      workpackage1.setNumber( "WO - 131745" );
      workpackage1.setSchedStartDate( DATE_FORMAT.parse( "2016-01-08 13:00:00" ) );
      workpackage1.setEndDate( DATE_FORMAT.parse( "2016-01-09 14:00:00" ) );
      workpackage1.setStatus( "COMMIT" );
      workpackage1.setLocationId( "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" );
      workpackage1.setInventoryId( "9CFBA066DA9011E587B1FB2D7B2472DF" );
      workpackage1.setDescription( "Workpackage to be started." );
      workpackage1.setHighestInventoryId( "9CFBA066DA9011E587B1FB2D7B2472DF" );
      workpackage1.setBarcode( "T000171Z" );
      workpackage1.setTaskClass( "CHECK" );

      workpackage2 = new Workpackage();
      workpackage2.setName( "IN WORK Workpackage" );
      workpackage2.setId( "AC9D774765E54A7080D166BB5CDD7649" );
      workpackage2.setNumber( "WO - 131845" );
      workpackage2.setSchedStartDate( DATE_FORMAT.parse( "2016-01-08 13:00:00" ) );
      workpackage2.setHighestInventoryId( "9CFBA066DA9011E587B1FB2D7B2472DF" );
      workpackage2.setStatus( "IN WORK" );
      workpackage2.setDescription( "IN WORK Workpackage" );
      workpackage2.setBarcode( "T000181Z" );
      workpackage2.setEndDate( DATE_FORMAT.parse( "2016-01-09 14:00:00" ) );
      workpackage2.setLocationId( "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" );
      workpackage2.setInventoryId( "9CFBA066DA9011E587B1FB2D7B2472DF" );
      workpackage2.setTaskClass( "CHECK" );
      workpackage2.setAircraftId( "601435E495494F34965B1588F5A6036D" );
   }
}
