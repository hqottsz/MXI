package com.mxi.mx.core.materials.stockdistribution.domain.stockdistreqservice;

import org.junit.Before;

import com.mxi.mx.core.key.RefInvClassKey;


public class StockDistributionRequestServiceForKitTest
      extends StockDistributionRequestServiceCommonTestCases {

   @Before
   public void loadData() {
      super.loadData( RefInvClassKey.KIT );
   }

}
