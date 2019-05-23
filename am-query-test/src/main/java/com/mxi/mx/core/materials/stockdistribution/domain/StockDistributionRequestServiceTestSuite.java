package com.mxi.mx.core.materials.stockdistribution.domain;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.mxi.mx.core.materials.stockdistribution.domain.stockdistreqservice.StockDistributionRequestServiceForBatchTest;
import com.mxi.mx.core.materials.stockdistribution.domain.stockdistreqservice.StockDistributionRequestServiceForKitTest;
import com.mxi.mx.core.materials.stockdistribution.domain.stockdistreqservice.StockDistributionRequestServiceForSerTest;
import com.mxi.mx.core.materials.stockdistribution.domain.stockdistreqservice.StockDistributionRequestServiceForTrkTest;


@RunWith( Suite.class )
@Suite.SuiteClasses( { StockDistributionRequestServiceForBatchTest.class,
      StockDistributionRequestServiceForSerTest.class,
      StockDistributionRequestServiceForTrkTest.class,
      StockDistributionRequestServiceForKitTest.class } )

public class StockDistributionRequestServiceTestSuite {
   // the class remains empty,
   // used only as a holder for the above annotations
}
