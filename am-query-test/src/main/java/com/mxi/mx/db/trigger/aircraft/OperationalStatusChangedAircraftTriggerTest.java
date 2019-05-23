package com.mxi.mx.db.trigger.aircraft;

import static org.junit.Assert.assertTrue;

import java.util.Date;
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
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.MxAfterTrigger;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefInvOperKey;
import com.mxi.mx.core.services.inventory.oper.AircraftOperationalService;
import com.mxi.mx.core.services.inventory.oper.InventoryStatus;


/**
 * This class tests the inventory operational status changed triggers.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class OperationalStatusChangedAircraftTriggerTest implements MxAfterTrigger<UUID> {

   private static Boolean triggerCalled;

   private static final AircraftKey AIRCRAFT = new AircraftKey( 4650, 224745 );
   private static final RefInvOperKey AWR = new RefInvOperKey( 0, "AWR" );
   private static final RefInvOperKey INM = new RefInvOperKey( 0, "INM" );
   private static final RefInvCondKey REPREQ = new RefInvCondKey( 0, "REPREQ" );

   private AircraftOperationalServiceStub aircraftOperationalServiceStub;

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   @Before
   public void setUp() {
      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );
      aircraftOperationalServiceStub = new AircraftOperationalServiceStub();
      GlobalParametersFake triggerParams =
            new GlobalParametersFake( ParmTypeEnum.TRIGGER_CACHE.name() );
      GlobalParameters.setInstance( triggerParams );
      triggerCalled = false;

      TriggerFactory.setInstance( null );
   }


   @Test
   public void testOperatingStatusChangedTrigger() throws TriggerException, MxException {

      Date date = new Date();

      InventoryStatus status =
            new InventoryStatus( AIRCRAFT.getInventoryKey(), true, AWR, INM, REPREQ, REPREQ );

      aircraftOperationalServiceStub.setOperatingStatus( AIRCRAFT, status, date );

      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   /**
    * This method gets called when the trigger is invoked
    */
   @Override
   public void after( UUID aircraftId ) throws TriggerException {
      triggerCalled = true;
   }


   /**
    * Create a stub of the AircraftOperationalService class to expose a protected method in order to
    * test it
    */
   private static class AircraftOperationalServiceStub extends AircraftOperationalService {

      @Override
      protected void setOperatingStatus( AircraftKey aircraft, InventoryStatus status, Date date )
            throws TriggerException, MxException {

         super.setOperatingStatus( aircraft, status, date );
      }
   }
}
