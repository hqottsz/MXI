package com.mxi.mx.db.trigger.fault;

import static org.junit.Assert.assertTrue;

import java.util.UUID;

import javax.ejb.ObjectNotFoundException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.api.exception.KeyConversionException;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.MxAfterTrigger;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.core.ejb.stask.TaskBean;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.TaskKey;


/**
 * This class tests MX_CF_DEFER trigger
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class DeferFaultTriggerTest implements MxAfterTrigger<UUID> {

   private static final String TASK_TO_BE_DEFERRED_KEY = "4650:32333";
   private static final String AUTHORIZING_HR_KEY = "4650:789";
   private static final String USER_NOTE = "User note";

   private static Boolean triggerCalled;

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   @Before
   public void setUp() {
      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );
      GlobalParametersFake triggerParams =
            new GlobalParametersFake( ParmTypeEnum.TRIGGER_CACHE.name() );
      GlobalParameters.setInstance( triggerParams );
      TriggerFactory.setInstance( null );
      triggerCalled = false;
   }


   /**
    *
    * Tests MX_CF_DEFER trigger
    *
    * @throws KeyConversionException
    * @throws MxException
    * @throws TriggerException
    * @throws ObjectNotFoundException
    *
    */
   @Test
   public void testFaultDeferTrigger()
         throws KeyConversionException, TriggerException, MxException, ObjectNotFoundException {
      TaskKey taskKey = new TaskKey( TASK_TO_BE_DEFERRED_KEY );
      HumanResourceKey humanResourceKey = new HumanResourceKey( AUTHORIZING_HR_KEY );

      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );

      taskBean.defer( taskKey, humanResourceKey, null, USER_NOTE );

      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   /**
    * This method gets called when the trigger is invoked
    *
    * @param faultId
    * @throws TriggerException
    */
   @Override
   public void after( UUID faultId ) throws TriggerException {
      triggerCalled = true;
   }

}
