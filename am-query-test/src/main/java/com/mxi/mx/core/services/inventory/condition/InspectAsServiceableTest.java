package com.mxi.mx.core.services.inventory.condition;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.unittest.table.inv.InvInv;


/**
 * Test inspect as serviceable.
 *
 * @author Libin Cai
 * @created December 7, 2017
 */
@RunWith( Theories.class )
public final class InspectAsServiceableTest extends AbstractConditionServiceTestCase {

   @DataPoint
   public static RefInvClassKey iSerInvClass = RefInvClassKey.SER;
   @DataPoint
   public static RefInvClassKey iBatchInvClass = RefInvClassKey.BATCH;


   @Test
   public void
         testGivenIncompleteInventoryAndRFBEnabledWhenInspectAsServiceableThenInventoryIsStillIncomplete()
               throws Exception {

      iInventory = new InventoryBuilder().atLocation( iDockLocation ).withPartNo( iPart )
            .withCondition( RefInvCondKey.INSPREQ ).build();

      // inspect inventory as serviceable
      new DefaultConditionService().inspectInventory( iInventory, iInspectInventoryTO, iHr );

      // assert that the inventory is still incomplete
      InvInv lInvInv = new InvInv( iInventory );
      lInvInv.assertCondCd( RefInvCondKey.RFI.getCd() );
      lInvInv.assertCompleteBoolean( false );
   }


   /*
    * Test that quarantined inventory should be serviceable after being inspected as serviceable
    * Test both SER and BATCH parts via parameter aInvClassKey
    */
   @Theory
   @Test
   public void testQuarantinedInventoryIsServiceableAfterInspectAsServiceable(
         RefInvClassKey aInvClassKey ) throws Exception {

      PartNoKey lPart = new PartNoBuilder().withInventoryClass( aInvClassKey )
            .withStatus( RefPartStatusKey.ACTV ).build();

      // quarantined inventory
      iInventory = new InventoryBuilder().withPartNo( lPart ).atLocation( iDockLocation )
            .withCondition( RefInvCondKey.QUAR ).build();

      // inspect inventory as serviceable
      new DefaultConditionService().inspectInventory( iInventory, iInspectInventoryTO, iHr );

      // assert that the inventory is RFI
      InvInv lInvInv = new InvInv( iInventory );
      lInvInv.assertCondCd( RefInvCondKey.RFI.getCd() );
   }


   @Override
   @Before
   public void loadData() throws Exception {

      super.loadData();

      GlobalParametersFake lConfigParms = new GlobalParametersFake( ParmTypeEnum.LOGIC.name() );
      lConfigParms.setBoolean( "ENABLE_READY_FOR_BUILD", true );
      GlobalParameters.setInstance( lConfigParms );

      int lUserId = 999;
      UserParametersFake lUserParms = new UserParametersFake( lUserId, ParmTypeEnum.LOGIC.name() );
      lUserParms.setBoolean( "ALLOW_AUTO_COMPLETION", false );
      UserParameters.setInstance( lUserId, ParmTypeEnum.LOGIC.name(), lUserParms );

   }


   @After
   public void teardown() {
      GlobalParameters.setInstance( ParmTypeEnum.LOGIC.name(), null );
   }

}
