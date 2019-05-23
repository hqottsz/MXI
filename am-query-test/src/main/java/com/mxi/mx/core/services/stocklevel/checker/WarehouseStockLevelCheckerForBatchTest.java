
package com.mxi.mx.core.services.stocklevel.checker;

import org.junit.Before;
import org.junit.Test;

import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.services.stocklevel.WarehouseStockLevelChecker;
import com.mxi.mx.core.table.inv.InvInvTable;


/**
 * This class tests the {@link WarehouseStockLevelChecker}.
 *
 * @author Libin Cai
 * @since September 11, 2018
 */
public final class WarehouseStockLevelCheckerForBatchTest
      extends WarehouseStockLevelCheckerCommonTestCases {

   @Before
   public void loadData() throws Exception {

      super.loadData( RefInvClassKey.BATCH, 60, 5 );
   }


   @Test
   public void test_GIVEN_InventoryInStock_WHEN_ReduceBinQuantity_THEN_StockDistRequestCreated()
         throws Exception {

      double lDecreasedQty = 2;

      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInvInWarehouseSubLoc );
      lInvInv.setBinQt( lInvInv.getBinQt() - lDecreasedQty );
      lInvInv.update();

      double lNewWarehouseQty = iWareHouseQty - lDecreasedQty;

      checkStockLowAndAssertStockDistReqQuantity( lNewWarehouseQty );
   }
}
