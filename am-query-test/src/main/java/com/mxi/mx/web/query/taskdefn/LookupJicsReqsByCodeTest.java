package com.mxi.mx.web.query.taskdefn;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.JobCardDefinition;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskTaskKey;


public class LookupJicsReqsByCodeTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String ASSIGN_TO_BLOCK_FALSE_REQ_CODE = "ASSIGN_TO_BLOCK_FALSE_REQ_CODE";
   private static final String ASSIGN_TO_BLOCK_TRUE_REQ_CODE = "ASSIGN_TO_BLOCK_TRUE_REQ_CODE";
   private static final String JOB_CARD_DEFN_CODE = "JOB_CARD_DEFN_CODE";


   /**
    * The query should return any requirement associated with the config slot of an assembly where
    * the value in the ASSIGN_TO_BLOCK_BOOL column is true.
    *
    * <pre>
    *
    * Given - an aircraft assembly with a root config-slot and
    *         a requirement definition that is allowed to be assigned to blocks (of class REQ) against the root config-slot of the aircraft assembly
    *
    * When - the LookupJicsReqsByCode query is executed in class mode REQ
    *
    * Then- only the requirement definition is returned
    *
    * </pre>
    */
   @Test
   public void itReturnsRequirementOfClassModeReqWithAssignToBlockBoolTrue() {

      // Given
      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();

      final TaskTaskKey lAssignToBlockTrueReq =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.setTaskClass( RefTaskClassKey.REQ );
                  aReqDefn.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aReqDefn.setCode( ASSIGN_TO_BLOCK_TRUE_REQ_CODE );
               }

            } );

      // When
      DataSet lDs = executeQuery( lAircraftAssembly, ASSIGN_TO_BLOCK_TRUE_REQ_CODE,
            RefTaskClassKey.REQ_CLASS_MODE_CD );

      // Then
      assertEquals( "Only 1 row should have returned from the query.", 1, lDs.getRowCount() );
      lDs.first();
      assertEquals( "" + lAssignToBlockTrueReq.getDbId() + ":" + lAssignToBlockTrueReq.getId(),
            lDs.getString( "task_task_key" ) );

   }


   /**
    * The query should not return any requirement where the value in the ASSIGN_TO_BLOCK_BOOL column
    * is false.
    *
    * <pre>
    *
    * Given - an aircraft assembly with a root config-slot and
    *         a requirement definition not allowed to be assigned to blocks (of class FOLLOW) against the root config-slot of the aircraft assembly
    *
    * When - the LookupJicsReqsByCode query is executed in class mode REQ
    *
    * Then- 0 rows are returned (FOLLOW value in ASSIGN_TO_BLOCK_BOOL is 0)
    *
    * </pre>
    */
   @Test
   public void itDoesntReturnRequirementOfClassModeReqWithAssignToBlockBoolFalse() {

      // Given
      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();

      final TaskTaskKey lAssignToBlockFalseReq =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.setTaskClass( RefTaskClassKey.FOLLOW );
                  aReqDefn.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aReqDefn.setCode( ASSIGN_TO_BLOCK_FALSE_REQ_CODE );
               }
            } );

      // When
      DataSet lDs = executeQuery( lAircraftAssembly, ASSIGN_TO_BLOCK_FALSE_REQ_CODE,
            RefTaskClassKey.REQ_CLASS_MODE_CD );

      // Then
      assertEquals( "No rows should have been returned from the query.", 0, lDs.getRowCount() );

   }


   /**
    * The query should return any JIC associated with the config slot of the assembly
    *
    * <pre>
    *
    * Given - an aircraft assembly with a root config-slot and
    *         a JIC against the root config-slot of the aircraft assembly
    *
    * When - the LookupJicsReqsByCode query is executed in class mode JIC
    *
    * Then- only the JIC is returned
    *
    * </pre>
    */
   @Test
   public void itReturnsJicWithClassModeJic() {

      // Given
      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();

      final TaskTaskKey lJobCardDefn =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aJobCardDefn ) {
                  aJobCardDefn.setConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aJobCardDefn.setStatus( RefTaskDefinitionStatusKey.BUILD );
                  aJobCardDefn.setCode( JOB_CARD_DEFN_CODE );

               }
            } );

      // When
      DataSet lDs = executeQuery( lAircraftAssembly, JOB_CARD_DEFN_CODE,
            RefTaskClassKey.JIC_CLASS_MODE_CD );

      // Then
      assertEquals( "Only 1 row should have returned from the query.", 1, lDs.getRowCount() );
      lDs.first();
      assertEquals( "" + lJobCardDefn.getDbId() + ":" + lJobCardDefn.getId(),
            lDs.getString( "task_task_key" ) );
   }


   /**
    * The query should not return any requirement where the value in the ASSIGN_TO_BLOCK_BOOL column
    * is false.
    *
    * <pre>
    *
    * Given - an aircraft assembly with a root config-slot and
    *         a requirement definition not allowed to be assigned to blocks (of class REPREF) against the root config-slot of the aircraft assembly
    *
    * When - the LookupJicsReqsByCode query is executed in class mode REQ
    *
    * Then- 0 rows are returned (REPREF value in ASSIGN_TO_BLOCK_BOOL is 0)
    *
    * </pre>
    */
   @Test
   public void itDoesNotReturnRequirementOfClassModeRepRefWithAssignToBlockBoolFalse() {

      // Given
      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();

      Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

         @Override
         public void configure( RequirementDefinition aReqDefn ) {
            aReqDefn.setTaskClass( RefTaskClassKey.REPREF );
            aReqDefn.againstConfigurationSlot(
                  Domain.readRootConfigurationSlot( lAircraftAssembly ) );
            aReqDefn.setCode( ASSIGN_TO_BLOCK_FALSE_REQ_CODE );
         }
      } );

      // When
      DataSet lDs = executeQuery( lAircraftAssembly, ASSIGN_TO_BLOCK_FALSE_REQ_CODE,
            RefTaskClassKey.REQ_CLASS_MODE_CD );

      // Then
      assertEquals( "No rows should have been returned from the query.", 0, lDs.getRowCount() );

   }


   private DataSet executeQuery( AssemblyKey aAssembly, String aSearchCode, String aClassMode ) {

      // Provide arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aSearchCode", aSearchCode );
      lArgs.add( "aAssemblyDbId", aAssembly.getDbId() );
      lArgs.add( "aAssemblyCd", aAssembly.getCd() );
      lArgs.add( "aClassModeCd", aClassMode );

      // Execute query
      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

   }

}
