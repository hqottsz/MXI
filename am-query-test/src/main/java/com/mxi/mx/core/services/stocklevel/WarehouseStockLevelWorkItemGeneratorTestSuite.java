package com.mxi.mx.core.services.stocklevel;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.mxi.mx.core.services.stocklevel.workitemgenerator.WarehouseStockLevelWorkItemGeneratorForBatchTest;
import com.mxi.mx.core.services.stocklevel.workitemgenerator.WarehouseStockLevelWorkItemGeneratorForKitTest;
import com.mxi.mx.core.services.stocklevel.workitemgenerator.WarehouseStockLevelWorkItemGeneratorForSerTest;
import com.mxi.mx.core.services.stocklevel.workitemgenerator.WarehouseStockLevelWorkItemGeneratorForTrkTest;


@RunWith( Suite.class )
@Suite.SuiteClasses( { WarehouseStockLevelWorkItemGeneratorForBatchTest.class,
      WarehouseStockLevelWorkItemGeneratorForSerTest.class,
      WarehouseStockLevelWorkItemGeneratorForTrkTest.class,
      WarehouseStockLevelWorkItemGeneratorForKitTest.class } )
public class WarehouseStockLevelWorkItemGeneratorTestSuite {

   // the class remains empty,
   // used only as a holder for the above annotations
}
