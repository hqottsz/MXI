package com.mxi.mx.db.trigger.flightmeasurement;

import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.ejb.EjbFactory;
import com.mxi.mx.common.ejb.EjbFactoryStub;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.MxAfterTrigger;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.core.ejb.assembly.AssemblyBean;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.RefDomainTypeKey;
import com.mxi.mx.core.services.assembly.UsageParmTO;


/**
 * This class tests the Flight Measurement Requirement Modified Triggers
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class CreateAssignFlightMeasurementRequirementTriggerTest implements MxAfterTrigger<UUID> {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private AssemblyBean assemblyBean;

   private SessionContextFake sessionContextFake;

   private EjbFactoryStub ejbFactoryStub;

   private static Boolean triggerCalled;

   private static final AssemblyKey ASSEMBLY_KEY = new AssemblyKey( "4650:ACFTFL1" );
   private static final String DATA_TYPE_CD = "TEST 5";
   private static final String DATA_TYPE_NAME = "TEST 5";
   private static final String DATA_TYPE_DESC = "Updated Measurement Details";


   @Before
   public void setupData() throws ParseException {

      assemblyBean = new AssemblyBean();
      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );
      GlobalParametersFake triggerParams =
            new GlobalParametersFake( ParmTypeEnum.TRIGGER_CACHE.name() );
      GlobalParameters.setInstance( triggerParams );

      sessionContextFake = new SessionContextFake();

      ejbFactoryStub = new EjbFactoryStub();
      EjbFactory.setSingleton( ejbFactoryStub );

      triggerCalled = false;

      TriggerFactory.setInstance( null );
   }


   /**
    *
    * This test will create a new measurement and then assign it to an assembly to see if the
    * trigger will be fired.
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testCreateAssignFlightMeasurementRequirementTrigger()
         throws MxException, TriggerException {

      UsageParmTO newMeasurement = new UsageParmTO();
      newMeasurement.setCode( DATA_TYPE_CD );
      newMeasurement.setPrecision( 2 );
      newMeasurement.setDescription( DATA_TYPE_DESC );
      newMeasurement.setDomainType( RefDomainTypeKey.TEXT );
      newMeasurement.setName( DATA_TYPE_NAME );

      assemblyBean.setSessionContext( sessionContextFake );

      assemblyBean.createAssignMeasurement( ASSEMBLY_KEY, newMeasurement );
      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   /**
    * This method gets called when the trigger is invoked {@inheritDoc}
    */
   @Override
   public void after( UUID assemblyId ) throws TriggerException {

      triggerCalled = true;
   }
}
