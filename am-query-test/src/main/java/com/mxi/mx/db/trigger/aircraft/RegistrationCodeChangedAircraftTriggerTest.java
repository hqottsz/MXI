package com.mxi.mx.db.trigger.aircraft;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

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
import com.mxi.mx.core.ejb.inventory.InventoryBean;
import com.mxi.mx.core.key.InventoryKey;


/**
 * This class tests the aircraft registration code changed trigger.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class RegistrationCodeChangedAircraftTriggerTest implements MxAfterTrigger<UUID> {

   private static final String INVENTORY_ID = "601435E495494F34965B1588F5A6036B";
   private static final String REGISTRATION_CODE = "Reg";
   private static final String FIN_NO = "111";

   private static Boolean triggerCalled;

   private LegacyKeyUtil legacyKeyUtil;
   private InventoryBean inventoryBean;
   private SessionContextFake sessionContextFake = new SessionContextFake();

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   @Before
   public void setUp() {
      legacyKeyUtil = new LegacyKeyUtil();
      inventoryBean = new InventoryBean();
      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );
      GlobalParametersFake triggerParams =
            new GlobalParametersFake( ParmTypeEnum.TRIGGER_CACHE.name() );
      GlobalParameters.setInstance( triggerParams );
      triggerCalled = false;
      TriggerFactory.setInstance( null );
   }


   @Test
   public void registrationCodeChangedAircraftTriggerTest() throws KeyConversionException,
         MxException, TriggerException, ObjectNotFoundException, FinderException {

      InventoryKey inventoryKey =
            legacyKeyUtil.altIdToLegacyKey( INVENTORY_ID, InventoryKey.class );
      InventoryBean inventoryBean = new InventoryBean();

      inventoryBean.setSessionContext( sessionContextFake );
      inventoryBean.setRegistrationCode( REGISTRATION_CODE, inventoryKey );

      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   @Test
   public void nonRegistrationCodeChangedAircraftTriggerTest() throws KeyConversionException,
         ObjectNotFoundException, FinderException, MxException, TriggerException {

      InventoryKey inventoryKey =
            legacyKeyUtil.altIdToLegacyKey( INVENTORY_ID, InventoryKey.class );

      inventoryBean.setSessionContext( sessionContextFake );
      inventoryBean.setFinNumber( FIN_NO, inventoryKey );

      assertFalse( "Trigger did get invoked", triggerCalled );
   }


   /**
    * This method gets called when the trigger is invoked
    */
   @Override
   public void after( UUID aircraftId ) throws TriggerException {
      triggerCalled = true;
   }

}
