package com.mxi.mx.core.services.fault;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QueryAccessObject;
import com.mxi.mx.core.flight.dao.FlightLegDao;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.maintenance.plan.deferralreference.service.DeferralReferenceAlertService;
import com.mxi.mx.core.services.event.stage.EventStageService;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.sd.SdFaultDao;
import com.mxi.mx.core.table.sd.SdFaultTable;


@RunWith( MockitoJUnitRunner.class )
public class FaultServiceFactoryTest {

   // Object under test
   private FaultServiceFactory iFactory;

   // Mocks
   private FlightLegDao iMockFlightLegDao;
   private QueryAccessObject iMockQao;
   private EventStageService iMockEventStageService;
   private FaultResolutionConfigSlotService iMockFaultResolutionConfigSlotService;
   private SchedStaskDao iMockSchedStaskDao;
   private DeferralReferenceAlertService iMockDeferralReferenceAlertService;
   private SdFaultDao iMockFaultDao;
   private EvtEventDao iMockEventDao;

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      iMockFlightLegDao = mock( FlightLegDao.class );
      iMockQao = mock( QueryAccessObject.class );
      iMockEventStageService = mock( EventStageService.class );
      iMockFaultResolutionConfigSlotService = mock( FaultResolutionConfigSlotService.class );
      iMockSchedStaskDao = mock( SchedStaskDao.class );
      iMockDeferralReferenceAlertService = mock( DeferralReferenceAlertService.class );
      iMockFaultDao = mock( SdFaultDao.class );
      iMockEventDao = mock( EvtEventDao.class );

      iFactory = new FaultServiceFactory( iMockFlightLegDao, iMockQao, iMockEventStageService,
            iMockFaultResolutionConfigSlotService, iMockSchedStaskDao,
            iMockDeferralReferenceAlertService, iMockFaultDao, iMockEventDao );
   }


   @Test( expected = NullPointerException.class )
   public void get_byNullCorrectiveTaskKey() {
      iFactory.get( ( TaskKey ) null );
   }


   @Test( expected = IllegalStateException.class )
   public void get_byTask_noFaultLinked() {
      SchedStaskTable lTaskWithoutFault = mock( SchedStaskTable.class );
      TaskKey lTaskWithoutFaultKey = mock( TaskKey.class );

      when( iMockSchedStaskDao.findByPrimaryKey( any( TaskKey.class ) ) )
            .thenReturn( lTaskWithoutFault );
      when( lTaskWithoutFault.getFault() ).thenReturn( null );

      iFactory.get( lTaskWithoutFaultKey );
   }


   @Test
   public void get_byCorrectiveTask() {
      setupTaskKeyAsCorrectiveTaskWithFault();

      FaultService lFaultService = iFactory.get( mock( TaskKey.class ) );

      assertNotNull( lFaultService );
   }


   private void setupTaskKeyAsCorrectiveTaskWithFault() {
      SchedStaskTable lCorrectiveTask = mock( SchedStaskTable.class );
      FaultKey lFaultKey = mock( FaultKey.class );

      when( iMockSchedStaskDao.findByPrimaryKey( any( TaskKey.class ) ) )
            .thenReturn( lCorrectiveTask );
      when( lCorrectiveTask.getFault() ).thenReturn( lFaultKey );
      when( iMockFaultDao.findByPrimaryKey( any( FaultKey.class ) ) )
            .thenReturn( mock( SdFaultTable.class ) );
      when( lFaultKey.getEventKey() ).thenReturn( mock( EventKey.class ) );
   }
}
