
package com.mxi.mx.core.services.stocklevel.workitemgenerator;

import org.junit.Test;

import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.services.inventory.InventoryServiceFactory;
import com.mxi.mx.core.services.stocklevel.WarehouseStockLevelWorkItemGenerator;
import com.mxi.mx.core.unittest.table.inv.InvInv;


/**
 * This class tests the {@link WarehouseStockLevelWorkItemGenerator}.
 *
 * @author Libin Cai
 * @since September 4, 2018
 */
public abstract class WarehouseStockLevelWorkItemGeneratorCommonSerializedTestCases
      extends WarehouseStockLevelWorkItemGeneratorCommonTestCases {

   public void loadData( RefInvClassKey aInvClass ) throws Exception {

      super.loadData( aInvClass, 1 );
   }


   /**
    * This test case is for Scrap serialized inventory. Scrap batch has transfer and it is covered
    * in the complete transfer test case.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void test_GIVEN_InvInStock_WHEN_ScrapInventory_THEN_WorkItemGenerated() throws Exception {

      InvInv lInvInv = new InvInv( iInventory );
      lInvInv.setOwner( iOwner );
      lInvInv.update();

      InventoryServiceFactory.getInstance().getConditionService().scrap( iInventory, null, null,
            null, null );

      assertThatOneWorkItemGenerated();
   }

}
