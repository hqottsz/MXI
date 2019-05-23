
package com.mxi.mx.core.services.stocklevel.workitemgenerator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.services.stocklevel.WarehouseStockLevelWorkItemGenerator;


/**
 * This class tests the {@link WarehouseStockLevelWorkItemGenerator}.
 *
 * @author Libin Cai
 * @since February 12, 2019
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class WarehouseStockLevelWorkItemGeneratorForBatchTest
      extends WarehouseStockLevelWorkItemGeneratorCommonTestCases {

   @Before
   public void loadData() throws Exception {

      super.loadData( RefInvClassKey.BATCH, 62.0 );
   }


   @Test
   public void test_GIVEN_InvInStock_WHEN_ReduceBinQuantity_THEN_WorkItemGenerated()
         throws Exception {

      // Decrease quantity to trigger creating stock level work item
      double lDelta = -1;
      setBinQuantity( iBinQty + lDelta );

      assertThatOneWorkItemGenerated();
   }


   @Test
   public void test_GIVEN_KitContentInvInStock_WHEN_ReduceBinQuantity_THEN_NoWorkItemGenerated()
         throws Exception {

      createKitInvWithContent();

      // Decrease quantity to trigger creating stock level work item
      double lDelta = -1;
      setBinQuantity( iBinQty + lDelta );

      assertThatNoWorkItemGenerated();
   }


   @Test
   public void test_GIVEN_InvInStock_WHEN_IncreaseBinQuantity_THEN_WorkItemGenerated()
         throws Exception {

      // Increase quantity to trigger creating stock level work item
      double lDelta = 1;
      setBinQuantity( iBinQty + lDelta );

      assertThatOneWorkItemGenerated();
   }


   @Test
   public void test_GIVEN_KitContentInvInStock_WHEN_IncreaseBinQuantity_THEN_NoWorkItemGenerated()
         throws Exception {

      createKitInvWithContent();

      // Increase quantity to trigger creating stock level work item
      double lDelta = 1;
      setBinQuantity( iBinQty + lDelta );

      assertThatNoWorkItemGenerated();
   }

}
