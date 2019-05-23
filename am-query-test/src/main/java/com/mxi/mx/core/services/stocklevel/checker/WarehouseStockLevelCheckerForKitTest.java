
package com.mxi.mx.core.services.stocklevel.checker;

import org.junit.Before;

import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.services.stocklevel.WarehouseStockLevelChecker;


/**
 * This class tests the {@link WarehouseStockLevelChecker}.
 *
 * @author Libin Cai
 * @since February 7, 2019
 */
public final class WarehouseStockLevelCheckerForKitTest
      extends WarehouseStockLevelCheckerCommonTestCases {

   @Before
   public void loadData() throws Exception {

      super.loadData( RefInvClassKey.KIT, 2, 1 );
   }

}
