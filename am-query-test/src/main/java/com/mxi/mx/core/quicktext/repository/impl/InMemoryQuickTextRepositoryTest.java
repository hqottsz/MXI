package com.mxi.mx.core.quicktext.repository.impl;

import com.mxi.mx.core.quicktext.repository.QuickTextRepositoryBaseTest;

public class InMemoryQuickTextRepositoryTest
      extends QuickTextRepositoryBaseTest<InMemoryQuickTextRepository> {

   @Override
   protected InMemoryQuickTextRepository getInstance() {
      return new InMemoryQuickTextRepository();
   }

}
