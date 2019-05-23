
package com.mxi.mx.core.services.inventory.condition;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefReqTypeKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.req.ReqPartTable;


/**
 * This class tests the
 * {@link DefaultBatchConditionService#quarantine(InventoryKey, LocationKey, double, RefStageReasonKey, String, HumanResourceKey)}
 * method for batch inventory.
 *
 * @author ksprung
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class DefaultBatchConditionQuarantineTest {

   private static final String QUAR_DESC = "plague";
   private static final RefStageReasonKey QUAR_REASON = RefStageReasonKey.ACQUP;

   private HumanResourceKey iHr;
   private LocationKey iDockLocation;
   private LocationKey iAirportLocation;
   private LocationKey iQuarLocation;

   private DefaultBatchConditionService iService;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Test that quarantining an inventory sets its condition to QUAR and moves it to the quarantine
    * location.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testQuarantineInventoryDetails() throws Exception {

      InventoryKey lInventory = new InventoryBuilder().withClass( RefInvClassKey.BATCH )
            .withCondition( RefInvCondKey.RFI ).atLocation( iDockLocation ).withBinQt( 10.0 )
            .build();

      iService.quarantine( lInventory, iQuarLocation, 10.0, QUAR_REASON, QUAR_DESC, iHr );

      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( lInventory );
      assertEquals( "inventory is quarantined", RefInvCondKey.QUAR, lInvInv.getInvCond() );
      assertEquals( "inventory is at quarantine location", iQuarLocation, lInvInv.getLocation() );

   }


   /**
    * Test that quarantining an inventory reserved remotely by a stock request sets the stock
    * request back to PROPEN.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testQuarantiningInventoryMakesRemoteStockRequestOpen() throws Exception {

      // create a stock request with remotely reserved inventory
      InventoryKey lInventory = new InventoryBuilder().withClass( RefInvClassKey.BATCH )
            .withCondition( RefInvCondKey.RFI ).atLocation( iDockLocation ).withBinQt( 10.0 )
            .build();
      PartRequestKey lStockRequest = new PartRequestBuilder().withType( RefReqTypeKey.STOCK )
            .withStatus( RefEventStatusKey.PRREMOTE ).withReservedInventory( lInventory )
            .withRemoteLocation( iDockLocation ).withRequestedQuantity( 10.0 ).build();

      iService.quarantine( lInventory, iQuarLocation, 10.0, QUAR_REASON, QUAR_DESC, iHr );

      assertEquals( "stock request is open", RefEventStatusKey.PROPEN,
            EvtEventTable.findByPrimaryKey( lStockRequest.getEventKey() ).getEventStatus() );
      assertEquals( "stock request has no reserved inventory", null,
            ReqPartTable.findByPrimaryKey( lStockRequest ).getInventory() );
   }


   /**
    * Test that quarantining an inventory reserved by a part request sets the part request back to
    * PROPEN.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testQuarantiningInventoryMakesPartRequestOpen() throws Exception {

      // create a part request with reserved inventory
      InventoryKey lInventory = new InventoryBuilder().withClass( RefInvClassKey.BATCH )
            .withCondition( RefInvCondKey.RFI ).atLocation( iDockLocation ).withBinQt( 10.0 )
            .build();
      PartRequestKey lPartRequest = new PartRequestBuilder().withType( RefReqTypeKey.TASK )
            .withStatus( RefEventStatusKey.PRAVAIL ).withReservedInventory( lInventory )
            .isNeededAt( iDockLocation ).withRequestedQuantity( 10.0 ).build();

      iService.quarantine( lInventory, iQuarLocation, 10.0, QUAR_REASON, QUAR_DESC, iHr );

      assertEquals( "part request is open", RefEventStatusKey.PROPEN,
            EvtEventTable.findByPrimaryKey( lPartRequest.getEventKey() ).getEventStatus() );
      assertEquals( "part request has no reserved inventory", null,
            ReqPartTable.findByPrimaryKey( lPartRequest ).getInventory() );
   }


   @Before
   public void loadData() throws Exception {

      iHr = new HumanResourceDomainBuilder().withUserId( 999 ).build();

      iAirportLocation =
            new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();
      iDockLocation = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK )
            .withSupplyLocation( iAirportLocation ).build();
      iQuarLocation = new LocationDomainBuilder().withType( RefLocTypeKey.QUAR )
            .withSupplyLocation( iAirportLocation ).build();

      iService = new DefaultBatchConditionService();
   }

}
