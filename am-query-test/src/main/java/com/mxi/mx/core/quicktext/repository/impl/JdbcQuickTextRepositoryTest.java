package com.mxi.mx.core.quicktext.repository.impl;

import org.junit.Rule;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.quicktext.repository.QuickTextRepositoryBaseTest;


public class JdbcQuickTextRepositoryTest
      extends QuickTextRepositoryBaseTest<JdbcQuickTextRepository> {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();


   @Override
   protected JdbcQuickTextRepository getInstance() {
      return new JdbcQuickTextRepository();
   }

}
