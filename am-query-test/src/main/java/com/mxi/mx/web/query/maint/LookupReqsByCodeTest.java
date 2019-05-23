package com.mxi.mx.web.query.maint;

import static com.mxi.am.domain.Domain.createRequirementDefinition;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskTaskKey;


public class LookupReqsByCodeTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String REQUIREMENT_DEFINITION_CODE = "REQUIREMENT_DEFINITION_CODE";


   /**
    *
    * The query returns active requirement definitions that are against the config-slot of the
    * provided assembly that are assignable to a Maintenance Programs.
    *
    * <pre>
    *
    * Given - an aircraft assembly with a root config-slot and
    *         a requirement definition that is allowed to be assigned to Maintenance Programs(has a task class of REQ)
    *            against the root config-slot of the aircraft assembly
    *
    * When  - the LookupReqsByCode query is executed
    *
    * Then  - the requirement is returned
    *
    * </pre>
    *
    */
   @Test
   public void itReturnsConfigSlotReqDefnsThatAreAssignableToMPs() {
      // Given
      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();

      final TaskTaskKey lRequirementDefinition =
            createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.setCode( REQUIREMENT_DEFINITION_CODE );
               aRequirementDefinition.againstConfigurationSlot(
                     Domain.readRootConfigurationSlot( lAircraftAssembly ) );
               aRequirementDefinition.setTaskClass( RefTaskClassKey.REQ );
               aRequirementDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
            } );

      // When
      DataSet lDs = executeQuery( lAircraftAssembly, REQUIREMENT_DEFINITION_CODE );

      // Then
      assertThat( "Only 1 row should have been returned from the query.", lDs.getRowCount(),
            is( 1 ) );
      lDs.first();
      assertThat( "The wrong requirement definition was returned from the query.",
            lRequirementDefinition, is( lDs.getKey( TaskTaskKey.class, "task_task_key" ) ) );
   }


   /**
    *
    * The query should not return active requirement definitions that are against the config-slot of
    * the provided assembly that are not assignable to a Maintenance Programs.
    *
    * <pre>
    *
    * Given - an aircraft assembly with a root config-slot and
    *         a requirement definition that is not allowed to be assigned to Maintenance Programs(has a task class of FOLLOW)
    *            against the root config-slot of the aircraft assembly
    *
    * When  - the LookupReqsByCode query is executed
    *
    * Then  - the requirement is not returned
    *
    * </pre>
    *
    */
   @Test
   public void itDoesNotReturnConfigSlotReqDefnsThatAreNotAssignableToMPs() {

      // Given
      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();
      final TaskTaskKey lRequirementDefinition =
            createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.setCode( REQUIREMENT_DEFINITION_CODE );
               aRequirementDefinition.againstConfigurationSlot(
                     Domain.readRootConfigurationSlot( lAircraftAssembly ) );
               aRequirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               aRequirementDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
            } );

      // When
      DataSet lDs = executeQuery( lAircraftAssembly, REQUIREMENT_DEFINITION_CODE );

      // Then
      assertThat( "No rows should have been returned from the query.", lDs.getRowCount(), is( 0 ) );
   }


   private DataSet executeQuery( AssemblyKey aAssembly, String aSearchCode ) {

      // Provide arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aSearchCode", aSearchCode );
      lArgs.add( "aAssemblyDbId", aAssembly.getDbId() );
      lArgs.add( "aAssemblyCd", aAssembly.getCd() );

      // Execute query
      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

   }

}
