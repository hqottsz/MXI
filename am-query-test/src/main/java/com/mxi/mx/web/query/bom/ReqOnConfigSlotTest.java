package com.mxi.mx.web.query.bom;

import static com.mxi.mx.core.key.RefInvClassKey.ACFT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Part;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * Ensures <code>ReqOnConfigSlot</code> query functions properly
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class ReqOnConfigSlotTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * This test case is testing if the requirement definition with correct prevent manual
    * initialization is returned given an activated and configuration slot based requirement
    * definition with its prevent manual initialization which sets to true, which ensures the first
    * select of the union for REQ requirement is being tested.
    *
    * <pre>
    *    Given a activated and configuration slot based requirement definition
    *    And its prevent manual initialization is true
    *    When execute this query GetTasksForPart.qrx
    *    Then verify the task definition with correct prevent manual initialization is returned
    * </pre>
    */
   @Test
   public void testConfigSlotBasedRequirementDefinitionWithPreventManualInitializationIsReturned() {

      // Given a assembly
      final PartNoKey lAircraftPart = createAircraftPart();
      final AssemblyKey lAircraftAssembly = createAcftAssyWithAcftPart( lAircraftPart );
      final ConfigSlotKey lAircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( lAircraftAssembly );

      // Given a activated and configuration slot based requirement definition with prevent manual
      // initialization which sets to true
      final TaskTaskKey lReqDefinition = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
         aReqDefn.setTaskClass( RefTaskClassKey.REQ );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         aReqDefn.setPreventManualInitialization( true );
      } );

      // When execute this query ReqOnConfigSlot.qrx
      final DataSet lDs = executeQuery( lAircraftAssembly );

      // verify the task definition with correct prevent manual initialization is returned
      final int lExpectedRowCount = 1;
      final int lActualRowCount = lDs.getRowCount();
      assertEquals( "Unexpected Number of Rows returned", lExpectedRowCount, lActualRowCount );

      lDs.first();

      final TaskTaskKey lExpectedRequirementDefinitionKey = lReqDefinition;
      final TaskTaskKey lActualRequirementDefinitionKey =
            lDs.getKey( TaskTaskKey.class, "task_defn_key" );
      assertEquals( "Unexpected Task Definition key", lExpectedRequirementDefinitionKey,
            lActualRequirementDefinitionKey );

      final boolean lActualPreventManualInitialization =
            lDs.getBoolean( "prevent_manual_init_bool" );
      assertTrue( lActualPreventManualInitialization );
   }


   /**
    * This test case is testing if the requirement definition with correct prevent manual
    * initialization is returned given an activated and replacement task definition with its prevent
    * manual initialization which sets to true, which ensures the first select of the union for REPL
    * requirement is being tested.
    *
    * <pre>
    *    Given an activated and replacement requirement definition
    *    And its prevent manual initialization is true
    *    When execute this query GetTasksForPart.qrx
    *    Then verify the requirement definition with correct prevent manual initialization is returned
    * </pre>
    */
   @Test
   public void testReplacementRequirementDefinitionWithPreventManualInitializationIsReturned() {

      // Given a assembly
      final PartNoKey lAircraftPart = createAircraftPart();
      final AssemblyKey lAircraftAssembly = createAcftAssyWithAcftPart( lAircraftPart );
      final ConfigSlotKey lAircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( lAircraftAssembly );

      // Given an activated and replacement requirement definition with prevent manual
      // initialization which sets to true
      final TaskTaskKey lReqDefinition = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
         aReqDefn.setTaskClass( RefTaskClassKey.REPL );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         aReqDefn.setPreventManualInitialization( true );
      } );

      // When execute this query ReqOnConfigSlot.qrx
      final DataSet lDs = executeQuery( lAircraftAssembly );

      // verify the task definition with correct prevent manual initialization is returned
      final int lExpectedRowCount = 1;
      final int lActualRowCount = lDs.getRowCount();
      assertEquals( "Unexpected Number of Rows returned", lExpectedRowCount, lActualRowCount );

      lDs.first();

      final TaskTaskKey lExpectedRequirementDefinitionKey = lReqDefinition;
      final TaskTaskKey lActualRequirementDefinitionKey =
            lDs.getKey( TaskTaskKey.class, "task_defn_key" );
      assertEquals( "Unexpected Task Definition key", lExpectedRequirementDefinitionKey,
            lActualRequirementDefinitionKey );

      final boolean lActualPreventManualInitialization =
            lDs.getBoolean( "prevent_manual_init_bool" );
      assertTrue( lActualPreventManualInitialization );
   }


   /**
    * This test case is testing if the task definition with correct prevent manual initialization is
    * returned given a activated and part based task definition with its prevent manual
    * initialization which sets to true, which ensures the second select of the union is being
    * tested.
    *
    * <pre>
    *    Given a activated and part based task definition
    *    And its prevent manual initialization is true
    *    When execute this query GetTasksForPart.qrx
    *    Then verify the task definition with correct prevent manual initialization is returned
    * </pre>
    */
   @Test
   public void testPartBasedReqTaskDefinitionWithPreventManualInitializationIsReturned() {

      // Given a part
      final PartNoKey lAircraftPart = createAircraftPart();

      final AssemblyKey lAircraftAssembly = createAcftAssyWithAcftPart( lAircraftPart );

      final ConfigSlotKey lAircraftRootConfigSlot = null;

      // Given an activated requirement definition with prevent manual initialization
      final TaskTaskKey lReqDefinition = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
         aReqDefn.setTaskClass( RefTaskClassKey.REQ );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         aReqDefn.setPreventManualInitialization( true );
         aReqDefn.addPartNo( lAircraftPart );
      } );

      // When execute this query ReqOnConfigSlot.qrx
      final DataSet lDs = executeQuery( lAircraftAssembly );

      // verify the task definition with correct prevent manual initialization is returned
      final int lExpectedRowCount = 1;
      final int lActualRowCount = lDs.getRowCount();
      assertEquals( "Unexpected Number of Rows returned", lExpectedRowCount, lActualRowCount );

      lDs.first();

      final TaskTaskKey lExpectedRequirementDefinitionKey = lReqDefinition;
      final TaskTaskKey lActualRequirementDefinitionKey =
            lDs.getKey( TaskTaskKey.class, "task_defn_key" );
      assertEquals( "Unexpected Task Definition key", lExpectedRequirementDefinitionKey,
            lActualRequirementDefinitionKey );

      final boolean lActualPreventManualInitialization =
            lDs.getBoolean( "prevent_manual_init_bool" );
      assertTrue( lActualPreventManualInitialization );
   }


   private DataSet executeQuery( AssemblyKey aAssemblyKey ) {
      final DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( new ConfigSlotKey( aAssemblyKey.toString() + ":0" ),
            new String[] { "aAssmblDbId", "aAssmblCd", "aAssmblBomId" } );
      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   private AssemblyKey createAcftAssyWithAcftPart( final PartNoKey aAircraftPart ) {
      return Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

         @Override
         public void configure( AircraftAssembly aAcftAssy ) {
            aAcftAssy.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

               @Override
               public void configure( ConfigurationSlot aRootConfigSlot ) {
                  aRootConfigSlot.addPartGroup( new DomainConfiguration<PartGroup>() {

                     @Override
                     public void configure( PartGroup aRootCsPartGroup ) {
                        aRootCsPartGroup.setInventoryClass( ACFT );
                        aRootCsPartGroup.addPart( aAircraftPart );
                     }
                  } );
               }
            } );
         }
      } );
   }


   private PartNoKey createAircraftPart() {
      return Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aPart ) {
            aPart.setInventoryClass( RefInvClassKey.ACFT );
         }
      } );
   }
}
