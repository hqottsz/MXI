package com.mxi.mx.core.services.stocklevel;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.mxi.mx.core.services.stocklevel.checker.WarehouseStockLevelCheckerForBatchTest;
import com.mxi.mx.core.services.stocklevel.checker.WarehouseStockLevelCheckerForKitTest;
import com.mxi.mx.core.services.stocklevel.checker.WarehouseStockLevelCheckerForSerTest;
import com.mxi.mx.core.services.stocklevel.checker.WarehouseStockLevelCheckerForTrkTest;


@RunWith( Suite.class )
@Suite.SuiteClasses( { WarehouseStockLevelCheckerForBatchTest.class,
      WarehouseStockLevelCheckerForSerTest.class, WarehouseStockLevelCheckerForTrkTest.class,
      WarehouseStockLevelCheckerForKitTest.class } )

public class WarehouseStockLevelCheckerTestSuite {
   // the class remains empty,
   // used only as a holder for the above annotations
}
