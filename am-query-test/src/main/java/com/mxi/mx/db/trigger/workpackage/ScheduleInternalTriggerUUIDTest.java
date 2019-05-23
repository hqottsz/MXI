package com.mxi.mx.db.trigger.workpackage;

import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.MxBeforeAndAfterTrigger;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.core.ejb.stask.STaskBean;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.stask.status.ScheduleInternalTO;


/**
 * This class tests the Schedule Internal Trigger.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class ScheduleInternalTriggerUUIDTest implements MxBeforeAndAfterTrigger<UUID> {

   private static Boolean beforeTriggerCalled;
   private static Boolean afterTriggerCalled;

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   private STaskBean sTaskBean;

   private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
   private final static DateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat( DATE_FORMAT );
   private final static String SCHED_START_DATE = "2018-11-14 10:12:14";
   private final static String SCHED_END_DATE = "2018-11-15 11:13:15";
   private static final String HR_KEY = "4650:101";
   private static final String SCHED_LOC = "ABC/LINE";
   private static final String TASK_KEY = "4650:1";
   private static final String WP_DESC = "A work package";


   @Before
   public void setUp() {
      sTaskBean = new STaskBean();
      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );

      GlobalParametersFake triggerParams =
            new GlobalParametersFake( ParmTypeEnum.TRIGGER_CACHE.name() );
      GlobalParameters.setInstance( triggerParams );

      beforeTriggerCalled = false;
      afterTriggerCalled = false;

      TriggerFactory.setInstance( null );
   }


   @Test
   public void testScheduleInternalTrigger() throws ParseException, AmApiResourceNotFoundException,
         AmApiBusinessException, MxException, TriggerException {

      ScheduleInternalTO scheduleInternalTO = new ScheduleInternalTO();
      scheduleInternalTO.setDescription( WP_DESC );
      scheduleInternalTO.setSchedDates( SIMPLE_DATE_FORMAT.parse( SCHED_START_DATE ),
            SIMPLE_DATE_FORMAT.parse( SCHED_END_DATE ) );
      scheduleInternalTO.setWorkLocationCd( SCHED_LOC );

      HumanResourceKey humanResourceKey = new HumanResourceKey( HR_KEY );
      TaskKey taskKey = new TaskKey( TASK_KEY );

      sTaskBean.schedule( taskKey, scheduleInternalTO, humanResourceKey );

      assertTrue( "Before Trigger [MX_TS_SCHEDULE_INTERNAL] did not get invoked",
            beforeTriggerCalled );
      assertTrue( "After Trigger [MX_TS_SCHEDULE_INTERNAL] did not get invoked",
            afterTriggerCalled );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void before( UUID aPk ) throws TriggerException {
      beforeTriggerCalled = true;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void after( UUID aPk ) throws TriggerException {
      afterTriggerCalled = true;
   }

}
