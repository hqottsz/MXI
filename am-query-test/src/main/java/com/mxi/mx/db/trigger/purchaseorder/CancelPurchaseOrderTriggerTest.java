package com.mxi.mx.db.trigger.purchaseorder;

import static org.junit.Assert.assertTrue;

import java.util.UUID;

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
import com.mxi.mx.core.ejb.po.PurchaseOrderBean;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.PurchaseOrderKey;


/**
 * This class tests Cancel Purchase order Trigger
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class CancelPurchaseOrderTriggerTest implements MxAfterTrigger<UUID> {

   private static final String AUTHORIZING_HR_KEY = "4650:100";
   private static final String PO_ID = "DCBA1FF71A5D4C58B873A75D2F23EB00";

   private static Boolean triggerCalled;

   private PurchaseOrderBean purchaseOrderBean;
   private LegacyKeyUtil legacyKeyUtil = new LegacyKeyUtil();
   private SessionContextFake sessionContextFake = new SessionContextFake();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

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


   /**
    * Tests the MX_PO_CANCEL trigger
    *
    * @throws TriggerException
    * @throws MxException
    * @throws KeyConversionException
    */
   @Test
   public void testCancelPurchaseOrderTrigger()
         throws KeyConversionException, MxException, TriggerException {
      purchaseOrderBean = new PurchaseOrderBean();

      HumanResourceKey humanResourceKey = new HumanResourceKey( AUTHORIZING_HR_KEY );
      PurchaseOrderKey purchaseOrderKey =
            legacyKeyUtil.altIdToLegacyKey( PO_ID, PurchaseOrderKey.class );
      purchaseOrderBean.setSessionContext( sessionContextFake );

      purchaseOrderBean.cancel( purchaseOrderKey, humanResourceKey, null, null );

      assertTrue( "Trigger did not get invoked", triggerCalled );

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void after( UUID purchaseOrderId ) throws TriggerException {
      triggerCalled = true;

   }

}
