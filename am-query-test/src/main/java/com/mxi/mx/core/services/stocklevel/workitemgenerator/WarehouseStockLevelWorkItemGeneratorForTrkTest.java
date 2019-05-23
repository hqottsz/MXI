
package com.mxi.mx.core.services.stocklevel.workitemgenerator;

import org.junit.Before;

import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.services.stocklevel.WarehouseStockLevelWorkItemGenerator;


/**
 * This class tests the {@link WarehouseStockLevelWorkItemGenerator}.
 *
 * @author Libin Cai
 * @since February 12, 2019
 */
public class WarehouseStockLevelWorkItemGeneratorForTrkTest
      extends WarehouseStockLevelWorkItemGeneratorCommonSerializedTestCases {

   @Before
   public void loadData() throws Exception {

      super.loadData( RefInvClassKey.TRK );
   }
}
