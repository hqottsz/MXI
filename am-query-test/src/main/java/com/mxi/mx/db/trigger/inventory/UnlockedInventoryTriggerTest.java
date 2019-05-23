package com.mxi.mx.db.trigger.inventory;

import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
 * This class tests the inventory condition changed to unlocked trigger
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class UnlockedInventoryTriggerTest implements MxAfterTrigger<UUID> {

   private static Boolean triggerCalled;
   private static final String INVENTORY_ID = "601435E495494F34965B1588F5A6036B";
   private static final String LOCK_LOCAL_DATE = "08-22-3018 17:30:00";
   private static final String LOCK_GMT_DATE = "08-22-3018 22:00:00";
   private static final String DATE_FORMAT = "MM-dd-yyyy HH:mm:ss";
   private static final String NOTE = "Inventory unlocked";
   private LegacyKeyUtil legacyKeyUtil = new LegacyKeyUtil();
   private SessionContextFake sessionContextFake = new SessionContextFake();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();


   @Before
   public void setUp() throws MxException {
      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );
      GlobalParametersFake triggerParams =
            new GlobalParametersFake( ParmTypeEnum.TRIGGER_CACHE.name() );
      GlobalParameters.setInstance( triggerParams );
      triggerCalled = false;
      TriggerFactory.setInstance( null );
   }


   @Test
   public void testInventoryConditionChangedToUnlocked() throws ParseException, MxException,
         TriggerException, ObjectNotFoundException, FinderException, KeyConversionException {
      DateFormat format = new SimpleDateFormat( DATE_FORMAT );
      // Admin User
      HumanResourceKey humanResourceKey = new HumanResourceKey( 0, 3 );
      Date lockLocalDate = format.parse( LOCK_LOCAL_DATE );
      Date lockGMTDate = format.parse( LOCK_GMT_DATE );
      InventoryKey inventoryKey =
            legacyKeyUtil.altIdToLegacyKey( INVENTORY_ID, InventoryKey.class );
      InventoryBean inventoryBean = new InventoryBean();
      inventoryBean.setSessionContext( sessionContextFake );
      inventoryBean.setLock( false, null, lockLocalDate, lockGMTDate, humanResourceKey, NOTE,
            inventoryKey );
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
