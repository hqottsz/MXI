package com.mxi.mx.core.ejb.stask.deadline;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.security.Principal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

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
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.exception.KeyConversionException;
import com.mxi.am.api.resource.maintenance.exec.fault.Fault;
import com.mxi.am.api.resource.maintenance.exec.fault.Fault.Deadline;
import com.mxi.am.api.resource.maintenance.exec.fault.impl.FaultResourceBean;
import com.mxi.am.api.util.LegacyKeyUtil;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.domain.Domain;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.api.resource.maintenance.exec.fault.resultevent.dao.JdbcResultEventDao;
import com.mxi.mx.core.api.resource.maintenance.exec.fault.resultevent.dao.ResultEventDao;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.maintenance.exec.fault.domain.FaultRepository;
import com.mxi.mx.core.maintenance.exec.fault.infra.JdbcFaultRepository;
import com.mxi.mx.core.maintenance.plan.deferralreference.domain.DeferralReferenceRepository;
import com.mxi.mx.core.maintenance.plan.deferralreference.infra.JdbcDeferralReferenceRepository;
import com.mxi.mx.core.services.stask.deadline.DeadlineService;
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
import com.mxi.mx.core.table.sd.JdbcSdFaultDao;
import com.mxi.mx.core.table.sd.SdFaultDao;
import com.mxi.mx.core.table.utl.JdbcUtlConfigParmDao;
import com.mxi.mx.core.table.utl.UtlConfigParmDao;
import com.mxi.mx.repository.fault.repairreference.JdbcRepairReferenceRepository;
import com.mxi.mx.repository.fault.repairreference.RepairReferenceRepository;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query test class for DeadlineBean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class DeadlineBeanTest extends ResourceBeanTest {

   @Mock
   private EJBContext ejbContext;

   @Mock
   private Principal principal;

   private static final String ID = "00000000000000000000000000000001";
   private static final int USAGE_DATA_TYPE_DB_ID = 100;
   private static final int USAGE_DATA_TYPE_ID = 9;

   private LegacyKeyUtil legacyKeyUtil;
   private HumanResourceKey humanResourceKey;

   FaultResourceBean faultResourceBean;

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( ResultEventDao.class ).to( JdbcResultEventDao.class );
               bind( EvtEventDao.class ).to( JdbcEvtEventDao.class );
               bind( EvtStageDao.class ).to( JdbcEvtStageDao.class );
               bind( SdFaultDao.class ).to( JdbcSdFaultDao.class );
               bind( SchedStaskDao.class ).to( JdbcSchedStaskDao.class );
               bind( UtlConfigParmDao.class ).to( JdbcUtlConfigParmDao.class );
               bind( Security.class ).to( CoreSecurity.class );
               bind( InvCndChgEventDao.class ).to( JdbcInvCndChgEventDao.class );
               bind( InvCndChgInvDao.class ).to( JdbcInvCndChgInvDao.class );
               bind( FaultRepository.class ).to( JdbcFaultRepository.class );
               bind( DeferralReferenceRepository.class )
                     .to( JdbcDeferralReferenceRepository.class );
               bind( RepairReferenceRepository.class ).to( JdbcRepairReferenceRepository.class );
            }
         } );


   /**
    * {@inheritDoc}
    */
   @Override
   protected void initializeTest() throws MxException {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), DeadlineBeanTest.class );
      initializeSecurityContext();
   }


   @Before
   public void setUp() throws MxException, ParseException {

      legacyKeyUtil = new LegacyKeyUtil();
      humanResourceKey = Domain.createHumanResource();

      faultResourceBean = InjectorContainer.get().getInstance( FaultResourceBean.class );
      faultResourceBean.setEJBContext( ejbContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( ejbContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );

   }


   /**
    * This method will test whether sched_stask revision date is updated when a calendar deadline is
    * unassigned from the fault
    *
    * @throws AmApiResourceNotFoundException
    * @throws MxException
    * @throws TriggerException
    * @throws KeyConversionException
    * @throws InterruptedException
    */
   @Test
   public void testRemoveCalendarDeadline() throws AmApiResourceNotFoundException, MxException,
         TriggerException, KeyConversionException, InterruptedException {

      Fault faultBeforeRemoveDeadline = getFault( ID );

      // Revision date before removing deadline
      Date revsionDateBeforeRemoveDeadline = faultBeforeRemoveDeadline.getLastModifiedDate();

      // To ensure an update in the revision date
      Thread.sleep( 2000 );

      TaskKey taskKey =
            legacyKeyUtil.altIdToLegacyKey( faultBeforeRemoveDeadline.getId(), TaskKey.class );

      // Remove Calendar deadline from the created fault
      DeadlineService deadlineService = new DeadlineService( taskKey );
      deadlineService.setCalendarDeadline( null, humanResourceKey, true );

      Fault faultAfterRemoveDeadline = getFault( ID );

      // Revision date after removing deadline
      Date revsionDateAfterRemoveDeadline = faultAfterRemoveDeadline.getLastModifiedDate();

      // Assert the deadline after removing deadline from fault
      assertDeadlineAfterRemove( faultBeforeRemoveDeadline.getUsageDeadlines(),
            faultAfterRemoveDeadline.getUsageDeadlines() );

      assertTrue( "The revision date of task was not updated",
            revsionDateBeforeRemoveDeadline.before( revsionDateAfterRemoveDeadline ) );

   }


   /**
    * This method will test whether sched_stask revision date is updated when a usage deadline is
    * unassigned from the fault
    *
    * @throws AmApiResourceNotFoundException
    * @throws MxException
    * @throws TriggerException
    * @throws KeyConversionException
    * @throws InterruptedException
    */
   @Test
   public void testUsageRemoveDeadline() throws AmApiResourceNotFoundException, MxException,
         TriggerException, KeyConversionException, InterruptedException {

      Fault faultBeforeRemoveDeadline = getFault( ID );

      // Revision date before removing deadline
      Date revsionDateBeforeRemoveDeadline = faultBeforeRemoveDeadline.getLastModifiedDate();

      // To ensure an update in the revision date
      Thread.sleep( 2000 );

      TaskKey taskKey =
            legacyKeyUtil.altIdToLegacyKey( faultBeforeRemoveDeadline.getId(), TaskKey.class );

      // Remove Usage deadline from the created fault
      DeadlineService deadlineService = new DeadlineService( taskKey );
      DataTypeKey dataType = new DataTypeKey( USAGE_DATA_TYPE_DB_ID, USAGE_DATA_TYPE_ID );
      deadlineService.removeUsageDeadline( dataType, humanResourceKey, true );

      Fault faultAfterRemoveDeadline = getFault( ID );

      // Revision date after removing deadline
      Date revsionDateAfterRemoveDeadline = faultAfterRemoveDeadline.getLastModifiedDate();

      // Assert the deadline after removing deadline from fault
      assertDeadlineAfterRemove( faultBeforeRemoveDeadline.getUsageDeadlines(),
            faultAfterRemoveDeadline.getUsageDeadlines() );

      assertTrue( "The revision date of task was not updated",
            revsionDateBeforeRemoveDeadline.before( revsionDateAfterRemoveDeadline ) );

   }


   private void assertDeadlineAfterRemove( List<Deadline> deadlinesBeforeRemoveDeadline,
         List<Deadline> deadlinesAfterRemoveDeadline ) {

      Assert.assertNotSame( deadlinesBeforeRemoveDeadline.size(),
            deadlinesAfterRemoveDeadline.size() );

      Boolean result = deadlinesAfterRemoveDeadline
            .contains( deadlinesBeforeRemoveDeadline.get( 0 ) )
            && deadlinesAfterRemoveDeadline.contains( deadlinesBeforeRemoveDeadline.get( 1 ) )
            && deadlinesAfterRemoveDeadline.contains( deadlinesBeforeRemoveDeadline.get( 2 ) );

      assertFalse( result );

   }


   public Fault getFault( String id ) throws AmApiResourceNotFoundException {
      Fault fault = faultResourceBean.get( id );
      return fault;
   }

}
