package com.mxi.mx.web.query.taskdefn;

import static com.mxi.am.domain.Domain.createAircraftAssembly;
import static com.mxi.am.domain.Domain.createMaintenanceProgram;
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
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.MaintPrgmDefnKey;
import com.mxi.mx.core.key.MaintPrgmKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.maint.MaintPrgmDefnTable;


public class SelectReqTest {

   private static final String REQUIREMENT_DEFINITION_CODE = "REQUIREMENT_DEFINITION_CODE";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    *
    * The query returns active requirement definitions that are against the config-slot of the
    * provided assembly that are assignable to a Maintenance Programs.
    *
    * <pre>
    *
    * Given - an aircraft assembly with a root config-slot and
    *         the aircraft assembly has a maintenance program and
    *         a requirement definition that is allowed to be assigned to Maintenance Programs(has a task class of REQ)
    *            against the root config-slot of the aircraft assembly
    *
    * When  - the SelectReq query is executed
    *
    * Then  - the requirement is returned
    *
    * </pre>
    *
    */
   @Test
   public void itReturnsConfigSlotReqDefnsThatAreAssignableToMPs() {
      // Given
      final AssemblyKey lAircraftAssembly = createAircraftAssembly(
            aAircraftAssembly -> aAircraftAssembly.addMaintenanceProgramDefinition() );

      final TaskTaskKey lRequirementDefinition =
            createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.setCode( REQUIREMENT_DEFINITION_CODE );
               aRequirementDefinition.againstConfigurationSlot(
                     Domain.readRootConfigurationSlot( lAircraftAssembly ) );
               aRequirementDefinition.setTaskClass( RefTaskClassKey.REQ );
               aRequirementDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
            } );

      final MaintPrgmKey lMaintPrgmKey =
            createMaintenanceProgram( aMaintenanceProgram -> aMaintenanceProgram.basedOnDefinition(
                  getMaintenanceProgramDefinition( lAircraftAssembly ).getPk() ) );

      // When
      DataSet lDs = executeQuery( lMaintPrgmKey );

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
    *         the aircraft assembly has a maintenance program and
    *         a requirement definition that is not allowed to be assigned to Maintenance Programs(has a task class of FOLLOW)
    *            against the root config-slot of the aircraft assembly
    *
    * When  - the SelectReq query is executed
    *
    * Then  - the requirement is not returned
    *
    * </pre>
    *
    */
   @Test
   public void itDoesNotReturnConfigSlotReqDefnsThatAreNotAssignableToMPs() {
      // Given
      final AssemblyKey lAircraftAssembly = createAircraftAssembly(
            aAircraftAssembly -> aAircraftAssembly.addMaintenanceProgramDefinition() );

      createRequirementDefinition( aRequirementDefinition -> {
         aRequirementDefinition.setCode( REQUIREMENT_DEFINITION_CODE );
         aRequirementDefinition
               .againstConfigurationSlot( Domain.readRootConfigurationSlot( lAircraftAssembly ) );
         aRequirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
         aRequirementDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
      } );

      final MaintPrgmKey lMaintPrgmKey =
            createMaintenanceProgram( aMaintenanceProgram -> aMaintenanceProgram.basedOnDefinition(
                  getMaintenanceProgramDefinition( lAircraftAssembly ).getPk() ) );

      // When
      DataSet lDs = executeQuery( lMaintPrgmKey );

      // Then
      assertThat( "No rows should have been returned from the query.", lDs.getRowCount(), is( 0 ) );

   }


   /**
    *
    * The query should not return active requirement definitions that are against the config-slot of
    * the provided assembly that are not assignable to a Maintenance Programs.
    *
    * <pre>
    *
    * Given - an aircraft assembly with a root config-slot and
    *         the aircraft assembly has a maintenance program and
    *         a requirement definition that is not allowed to be assigned to Maintenance Programs(has a task class of REPREF)
    *            against the root config-slot of the aircraft assembly
    *
    * When  - the SelectReq query is executed
    *
    * Then  - the requirement is not returned
    *
    * </pre>
    *
    */
   @Test
   public void itDoesNotReturnConfigSlotReqDefnsOfClassRepRefThatAreNotAssignableToMPs() {
      // Given
      final AssemblyKey lAircraftAssembly = createAircraftAssembly(
            aAircraftAssembly -> aAircraftAssembly.addMaintenanceProgramDefinition() );

      createRequirementDefinition( aRequirementDefinition -> {
         aRequirementDefinition.setCode( REQUIREMENT_DEFINITION_CODE );
         aRequirementDefinition
               .againstConfigurationSlot( Domain.readRootConfigurationSlot( lAircraftAssembly ) );
         aRequirementDefinition.setTaskClass( RefTaskClassKey.REPREF );
         aRequirementDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
      } );

      final MaintPrgmKey lMaintPrgmKey =
            createMaintenanceProgram( aMaintenanceProgram -> aMaintenanceProgram.basedOnDefinition(
                  getMaintenanceProgramDefinition( lAircraftAssembly ).getPk() ) );

      // When
      DataSet lDs = executeQuery( lMaintPrgmKey );

      // Then
      assertThat( "No rows should have been returned from the query.", lDs.getRowCount(), is( 0 ) );

   }


   private DataSet executeQuery( MaintPrgmKey aMaintPrgmKey ) {

      // Provide arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aMaintPrgmDbId", aMaintPrgmKey.getDbId() );
      lArgs.add( "aMaintPrgmId", aMaintPrgmKey.getId() );
      lArgs.add( "aSearchMethod", "" );
      lArgs.add( "aCode", "" );
      lArgs.add( "aName", "" );
      lArgs.add( "aConfigSlotCd", "" );
      lArgs.add( "aTaskClassCd", "" );
      lArgs.add( "aTaskClassDbId", "" );
      lArgs.add( "aTaskSubclassCd", "" );
      lArgs.add( "aTaskSubclassDbId", "" );
      lArgs.add( "aOriginatorDbId", "" );
      lArgs.add( "aOriginatorCd", "" );
      lArgs.add( "aWorkTypeDbId", "" );
      lArgs.add( "aWorkTypeCd", "" );

      // Execute query
      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

   }


   private MaintPrgmDefnTable getMaintenanceProgramDefinition( AssemblyKey aAircraftAssembly ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aAircraftAssembly, "assmbl_db_id", "assmbl_cd" );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "maint_prgm_defn", lArgs );
      lQs.next();
      MaintPrgmDefnKey lMaintPrgmDefnKey =
            lQs.getKey( MaintPrgmDefnKey.class, "maint_prgm_defn_db_id", "maint_prgm_defn_id" );
      return MaintPrgmDefnTable.findByPrimaryKey( lMaintPrgmDefnKey );
   }

}
