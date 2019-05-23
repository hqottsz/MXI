package com.mxi.mx.db.trigger.inventory;

import static org.junit.Assert.assertTrue;

import java.util.Date;
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
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;


/**
 * This class tests inventory condition changed to locked trigger
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class LockedInventoryTriggerTest implements MxAfterTrigger<UUID> {

   private static Boolean triggerCalled;
   private static final String INVENTORY_ID = "601435E495494F34965B1588F5A6036B";
   private static final boolean LOCKED_BOOL = true;
   private static final String LOCKED_NOTE = "inventory locked";
   private static final Date LOCK_LOCAL_DATE = new Date();
   private static final Date LOCK_GMT_DATE = new Date();
   private static final HumanResourceKey LOCK_AUTHORIZING_HR = HumanResourceKey.ADMIN;

   private LegacyKeyUtil legacyKeyUtil = new LegacyKeyUtil();
   private SessionContextFake sessionContextFake = new SessionContextFake();

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   @Before
   public void setUp() throws ObjectNotFoundException {
      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );
      GlobalParametersFake triggerParams =
            new GlobalParametersFake( ParmTypeEnum.TRIGGER_CACHE.name() );
      GlobalParameters.setInstance( triggerParams );
      triggerCalled = false;
      TriggerFactory.setInstance( null );
   }


   @Test
   public void testInventoryLockedTrigger() throws MxException, TriggerException,
         KeyConversionException, ObjectNotFoundException, FinderException {
      InventoryKey inventoryKey =
            legacyKeyUtil.altIdToLegacyKey( INVENTORY_ID, InventoryKey.class );
      InventoryBean inventoryBean = new InventoryBean();
      inventoryBean.setSessionContext( sessionContextFake );
      inventoryBean.setLock( LOCKED_BOOL, null, LOCK_LOCAL_DATE, LOCK_GMT_DATE, LOCK_AUTHORIZING_HR,
            LOCKED_NOTE, inventoryKey );

      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   /**
    * This method gets called when the trigger is invoked
    */
   @Override
   public void after( UUID inventoryId ) throws TriggerException {
      triggerCalled = true;
   }

}
