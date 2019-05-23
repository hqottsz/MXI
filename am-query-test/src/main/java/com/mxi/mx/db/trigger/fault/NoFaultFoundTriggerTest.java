package com.mxi.mx.db.trigger.fault;

import static org.junit.Assert.assertTrue;

import java.util.UUID;

import javax.ejb.ObjectNotFoundException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.google.inject.Injector;
import com.mxi.am.api.exception.KeyConversionException;
import com.mxi.am.api.util.LegacyKeyUtil;
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
import com.mxi.mx.core.ejb.fault.FaultBean;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;


/**
 * This class tests No Fault Found Trigger
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class NoFaultFoundTriggerTest implements MxAfterTrigger<UUID> {

   private static final String AUTHORIZING_HR_KEY = "4650:7000";
   private static final String FAULT_ID = "00000000000000000000000000000103";

   private static Boolean triggerCalled;

   private FaultBean faultBean;
   private LegacyKeyUtil legacyKeyUtil = new LegacyKeyUtil();
   private SessionContextFake sessionContext = new SessionContextFake();
   private Injector iInjector;

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   @Before
   public void setUp() {
      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );
      GlobalParametersFake triggerParams =
            new GlobalParametersFake( ParmTypeEnum.TRIGGER_CACHE.name() );
      GlobalParameters.setInstance( triggerParams );
      triggerCalled = false;
      TriggerFactory.setInstance( null );
   }


   /**
    * Tests the MX_CF_NFF trigger
    *
    * @throws ObjectNotFoundException
    * @throws TriggerException
    * @throws MxException
    * @throws KeyConversionException
    */
   @Test
   public void testNoFaultFoundTrigger()
         throws ObjectNotFoundException, TriggerException, MxException, KeyConversionException {
      faultBean = new FaultBean();

      HumanResourceKey humanResourceKey = new HumanResourceKey( AUTHORIZING_HR_KEY );
      FaultKey faultKey = legacyKeyUtil.altIdToLegacyKey( FAULT_ID, FaultKey.class );

      faultBean.setSessionContext( sessionContext );
      faultBean.markAsNoFaultFound( faultKey, humanResourceKey );

      assertTrue( "Trigger did not get invoked", triggerCalled );

   }


   /**
    * This method gets called when the trigger is invoked
    */
   @Override
   public void after( UUID faultId ) throws TriggerException {
      triggerCalled = true;
   }

}
