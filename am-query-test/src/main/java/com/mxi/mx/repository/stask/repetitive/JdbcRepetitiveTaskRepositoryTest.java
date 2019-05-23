package com.mxi.mx.repository.stask.repetitive;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.TaskKey;


public class JdbcRepetitiveTaskRepositoryTest {

   // subject under test
   private RepetitiveTaskRepository iRepetitiveTaskRepository;

   // data set up
   private final TaskKey iRepetitiveActiveTask = new TaskKey( "4650:2" );
   private final TaskKey iRepetitiveForecastTask = new TaskKey( "4650:3" );
   private final TaskKey iInvalidTask = new TaskKey( "1:1" );

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      iRepetitiveTaskRepository = new JdbcRepetitiveTaskRepository();
   }


   @Test
   public void getForecast_valid() {
      List<TaskKey> lTasks = iRepetitiveTaskRepository.getForecast( iRepetitiveActiveTask );
      assertTrue( lTasks.contains( iRepetitiveForecastTask ) );
   }


   @Test
   public void getForecast_no_results() {
      List<TaskKey> lTasks = iRepetitiveTaskRepository.getForecast( iInvalidTask );
      assertTrue( lTasks.isEmpty() );
   }


   @Test
   public void getForecast_null_parameter() {
      List<TaskKey> lTasks = iRepetitiveTaskRepository.getForecast( null );
      assertTrue( lTasks.isEmpty() );
   }
}
