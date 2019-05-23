package com.mxi.mx.db.trigger.fault;

import static org.junit.Assert.assertTrue;

import java.security.Principal;
import java.util.UUID;

import javax.ejb.EJBContext;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.exception.KeyConversionException;
import com.mxi.am.api.resource.maintenance.exec.fault.Fault;
import com.mxi.am.api.resource.maintenance.exec.fault.FaultSearchParameters;
import com.mxi.am.api.resource.maintenance.exec.fault.impl.FaultResourceBean;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.ejb.EjbFactory;
import com.mxi.mx.common.ejb.EjbFactoryStub;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.MxAfterTrigger;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.integration.exceptions.IntegrationException;


/**
 * This class tests the Edit fault trigger.
 *
 */

@RunWith( MockitoJUnitRunner.class )
public class EditFaultTriggerTest implements MxAfterTrigger<UUID> {

   private static Boolean triggerCalled;
   private static final String FAULT_ID = "00000000000000000000000000000001";
   private static final String DESCRIPTION = "Edited Fault";

   @Mock
   private EJBContext mockEJBSecurityContext;

   @Mock
   private Principal mockPrincipal;

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   private FaultResourceBean faultResourceBean;


   @Before
   public void setUp() {

      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );

      faultResourceBean = InjectorContainer.get().getInstance( FaultResourceBean.class );
      faultResourceBean.setEJBContext( mockEJBSecurityContext );
      Mockito.when( mockEJBSecurityContext.getCallerPrincipal() ).thenReturn( mockPrincipal );
      EjbFactory.setSingleton( new EjbFactoryStub() );
      GlobalParametersFake triggerParams =
            new GlobalParametersFake( ParmTypeEnum.TRIGGER_CACHE.name() );
      GlobalParameters.setInstance( triggerParams );
      triggerCalled = false;
      TriggerFactory.setInstance( null );

   }


   @Test
   public void testEditFaultTriggerThroughAPI() throws KeyConversionException, MxException,
         TriggerException, IntegrationException, AmApiResourceNotFoundException {

      FaultSearchParameters faultSearchParameters = new FaultSearchParameters();
      Fault fault = faultResourceBean.get( FAULT_ID );
      fault.setName( DESCRIPTION );
      faultResourceBean.put( FAULT_ID, fault, faultSearchParameters );

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
