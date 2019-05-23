package com.mxi.mx.core.maintenance.exec.fault.infra;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.maintenance.exec.fault.domain.Reason;
import com.mxi.mx.core.maintenance.exec.fault.domain.ReasonRepository;


public class JdbcReasonRepositoryTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   private ReasonRepository repository;


   @Before
   public void setUp() {
      repository = InjectorContainer.get().getInstance( ReasonRepository.class );
   }


   @Test
   public void getReasons_happyPath() {
      List<Reason> reasons = repository.getReasons();
      assertEquals( 1, reasons.size() );
      assertEquals( "DEFER-NA", reasons.get( 0 ).getStageReasonCode() );
   }
}
