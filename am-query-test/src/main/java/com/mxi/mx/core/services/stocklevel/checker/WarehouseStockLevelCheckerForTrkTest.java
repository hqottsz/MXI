
package com.mxi.mx.core.services.stocklevel.checker;

import org.junit.Before;

import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.services.stocklevel.WarehouseStockLevelChecker;


/**
 * This class tests the {@link WarehouseStockLevelChecker}.
 *
 * @author Libin Cai
 * @since February 4, 2019
 */
public final class WarehouseStockLevelCheckerForTrkTest
      extends WarehouseStockLevelCheckerCommonTestCases {

   @Before
   public void loadData() throws Exception {

      super.loadData( RefInvClassKey.TRK, 2, 1 );
   }

}
