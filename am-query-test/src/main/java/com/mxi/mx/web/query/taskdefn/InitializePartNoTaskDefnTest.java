package com.mxi.mx.web.query.taskdefn;

import static org.junit.Assert.assertEquals;

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
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * This is a test class for testing InitializePartNoTaskDefn.qrx
 *
 */
public class InitializePartNoTaskDefnTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String ACTIVE_TASK_INSTANCES_RESULT_ATTRIBUTE = "active_task_instances";
   private static final String TASK_KEY = "TASK_KEY";


   @Test
   public void itReturnsZeroActiveTaskInstanceWhenNoActiveTasksOfTaskDefinitionForAnInventory()
         throws Exception {
      // Arrange
      // Aircraft, Aircraft Assembly, Human Resource
      final PartNoKey lAircraftPart =
            Domain.createPart( aPart -> aPart.setInventoryClass( RefInvClassKey.ACFT ) );
      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly(
            aAssembly -> aAssembly.setRootConfigurationSlot( aRootCs -> aRootCs
                  .addPartGroup( aPartGroup -> aPartGroup.addPart( lAircraftPart ) ) ) );
      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAircraftAssembly );
         aAircraft.setPart( lAircraftPart );
      } );

      // Revision of a requirement definition
      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( aReqDefn -> aReqDefn.addPartNo( lAircraftPart ) );

      // Requirement based on the requirement definition revision
      Domain.createRequirement( aReq -> {
         aReq.setDefinition( lReqDefn );
         aReq.setStatus( RefEventStatusKey.COMPLETE );
         aReq.setInventory( lAircraft );
      } );
      HumanResourceKey lHrKey = Domain.createHumanResource( aHr -> aHr.setAllAuthority( true ) );
      // Act
      DataSet lResultDs = execute( lReqDefn, lHrKey );
      // Assert
      assertEquals( "Unexpected number of rows returned", 1, lResultDs.getRowCount() );
      lResultDs.next();
      Integer lExpectedNumberOfActiveTaskInstances = 0;
      Integer lActualNumberOfActiveTaskInstances =
            lResultDs.getInteger( ACTIVE_TASK_INSTANCES_RESULT_ATTRIBUTE );
      assertEquals( "Unexpected number of active task instances returned for the inventory",
            lExpectedNumberOfActiveTaskInstances, lActualNumberOfActiveTaskInstances );
   }


   @Test
   public void itReturnsOneActiveTaskInstanceWhenOneActiveTaskOfTaskDefinitionForAnInventory()
         throws Exception {
      // Arrange
      // Aircraft, Aircraft Assembly
      final PartNoKey lAircraftPart =
            Domain.createPart( aPart -> aPart.setInventoryClass( RefInvClassKey.ACFT ) );
      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly(
            aAssembly -> aAssembly.setRootConfigurationSlot( aRootCs -> aRootCs
                  .addPartGroup( aPartGroup -> aPartGroup.addPart( lAircraftPart ) ) ) );
      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAircraftAssembly );
         aAircraft.setPart( lAircraftPart );
      } );

      // Revision of a requirement definition
      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( aReqDefn -> aReqDefn.addPartNo( lAircraftPart ) );

      // Requirements based on the requirement definition revision
      Domain.createRequirement( aReq -> {
         aReq.setDefinition( lReqDefn );
         aReq.setStatus( RefEventStatusKey.COMPLETE );
         aReq.setInventory( lAircraft );
      } );
      Domain.createRequirement( aReq -> {
         aReq.setDefinition( lReqDefn );
         aReq.setStatus( RefEventStatusKey.ACTV );
         aReq.setInventory( lAircraft );
      } );
      HumanResourceKey lHrKey = Domain.createHumanResource( aHr -> aHr.setAllAuthority( true ) );
      // Act
      DataSet lResultDs = execute( lReqDefn, lHrKey );
      // Assert
      assertEquals( "Unexpected number of rows returned", 1, lResultDs.getRowCount() );
      lResultDs.next();
      Integer lExpectedNumberOfActiveTaskInstances = 1;
      Integer lActualNumberOfActiveTaskInstances =
            lResultDs.getInteger( ACTIVE_TASK_INSTANCES_RESULT_ATTRIBUTE );
      assertEquals( "Unexpected number of active task instances returned for the inventory",
            lExpectedNumberOfActiveTaskInstances, lActualNumberOfActiveTaskInstances );
   }


   @Test
   public void
         itReturnsTwoActiveTaskInstancesWhenTwoActiveTasksOfSameRevisionOfTaskDefinitionForAnInventory()
               throws Exception {
      // Arrange
      // Aircraft, Aircraft Assembly
      final PartNoKey lAircraftPart =
            Domain.createPart( aPart -> aPart.setInventoryClass( RefInvClassKey.ACFT ) );
      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly(
            aAssembly -> aAssembly.setRootConfigurationSlot( aRootCs -> aRootCs
                  .addPartGroup( aPartGroup -> aPartGroup.addPart( lAircraftPart ) ) ) );
      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAircraftAssembly );
         aAircraft.setPart( lAircraftPart );
      } );

      // Requirement definition
      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( aReqDefn -> aReqDefn.addPartNo( lAircraftPart ) );

      // Requirements based on the requirement definition revisions
      Domain.createRequirement( aReq -> {
         aReq.setDefinition( lReqDefn );
         aReq.setStatus( RefEventStatusKey.ACTV );
         aReq.setInventory( lAircraft );
      } );
      Domain.createRequirement( aReq -> {
         aReq.setDefinition( lReqDefn );
         aReq.setStatus( RefEventStatusKey.ACTV );
         aReq.setInventory( lAircraft );
      } );
      HumanResourceKey lHrKey = Domain.createHumanResource( aHr -> aHr.setAllAuthority( true ) );
      // Act
      DataSet lResultDs = execute( lReqDefn, lHrKey );
      // Assert
      assertEquals( "Unexpected number of rows returned", 1, lResultDs.getRowCount() );
      lResultDs.next();
      Integer lActualNumberOfActiveTaskInstances =
            lResultDs.getInteger( ACTIVE_TASK_INSTANCES_RESULT_ATTRIBUTE );
      Integer lExpectedNumberOfActiveTaskInstances = 2;
      assertEquals( "Unexpected number of active task instances returned for the inventory",
            lExpectedNumberOfActiveTaskInstances, lActualNumberOfActiveTaskInstances );
   }


   @Test
   public void
         itReturnsTwoActiveInstancesWhenTwoActiveTasksOfDifferentRevisionsOfTaskDefinitionForAnInventory()
               throws Exception {
      // Arrange
      // Aircraft, Aircraft Assembly
      final PartNoKey lAircraftPart =
            Domain.createPart( aPart -> aPart.setInventoryClass( RefInvClassKey.ACFT ) );
      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly(
            aAssembly -> aAssembly.setRootConfigurationSlot( aRootCs -> aRootCs
                  .addPartGroup( aPartGroup -> aPartGroup.addPart( lAircraftPart ) ) ) );
      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAircraftAssembly );
         aAircraft.setPart( lAircraftPart );
      } );

      // 2 revisions of a requirement definition
      final TaskTaskKey lReqDefnFormerRevision = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.addPartNo( lAircraftPart );
         aReqDefn.setRevisionNumber( 1 );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.REVISION );
      } );
      final TaskTaskKey lReqDefnActiveRevision = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.addPartNo( lAircraftPart );
         aReqDefn.setRevisionNumber( 2 );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         aReqDefn.setPreviousRevision( lReqDefnFormerRevision );
      } );

      // Requirements based on the requirement definition revisions
      Domain.createRequirement( aReq -> {
         aReq.setDefinition( lReqDefnFormerRevision );
         aReq.setStatus( RefEventStatusKey.ACTV );
         aReq.setInventory( lAircraft );
      } );
      Domain.createRequirement( aReq -> {
         aReq.setDefinition( lReqDefnActiveRevision );
         aReq.setStatus( RefEventStatusKey.ACTV );
         aReq.setInventory( lAircraft );
      } );
      HumanResourceKey lHrKey = Domain.createHumanResource( aHr -> aHr.setAllAuthority( true ) );
      // Act
      DataSet lResultDs = execute( lReqDefnActiveRevision, lHrKey );
      // Assert
      assertEquals( "Unexpected number of rows returned", 1, lResultDs.getRowCount() );
      lResultDs.next();
      Integer lActualNumberOfActiveTaskInstances =
            lResultDs.getInteger( ACTIVE_TASK_INSTANCES_RESULT_ATTRIBUTE );
      Integer lExpectedNumberOfActiveTaskInstances = 2;
      assertEquals( "Unexpected number of active task instances returned for the inventory",
            lExpectedNumberOfActiveTaskInstances, lActualNumberOfActiveTaskInstances );
   }


   @Test
   public void
         itReturnsOneActiveTaskInstanceWhenTwoActiveTasksOfDifferentTaskDefinitionsForAnInventory()
               throws Exception {
      // Arrange
      // Aircraft, Aircraft Assembly
      final PartNoKey lAircraftPart =
            Domain.createPart( aPart -> aPart.setInventoryClass( RefInvClassKey.ACFT ) );
      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly(
            aAssembly -> aAssembly.setRootConfigurationSlot( aRootCs -> aRootCs
                  .addPartGroup( aPartGroup -> aPartGroup.addPart( lAircraftPart ) ) ) );
      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAircraftAssembly );
         aAircraft.setPart( lAircraftPart );
      } );

      // 2 revisions of a requirement definition
      final TaskTaskKey lReqDefn = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.addPartNo( lAircraftPart );
         aReqDefn.setRevisionNumber( 1 );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
      } );
      final TaskTaskKey lAnotherReqDefn = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.addPartNo( lAircraftPart );
         aReqDefn.setRevisionNumber( 1 );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
      } );

      // Requirements based on the requirement definition revisions
      Domain.createRequirement( aReq -> {
         aReq.setDefinition( lReqDefn );
         aReq.setStatus( RefEventStatusKey.ACTV );
         aReq.setInventory( lAircraft );
      } );
      Domain.createRequirement( aReq -> {
         aReq.setDefinition( lAnotherReqDefn );
         aReq.setStatus( RefEventStatusKey.ACTV );
         aReq.setInventory( lAircraft );
      } );
      HumanResourceKey lHrKey = Domain.createHumanResource( aHr -> aHr.setAllAuthority( true ) );
      // Act
      DataSet lResultDs = execute( lAnotherReqDefn, lHrKey );
      // Assert
      assertEquals( "Unexpected number of rows returned", 1, lResultDs.getRowCount() );
      lResultDs.next();
      Integer lActualNumberOfActiveTaskInstances =
            lResultDs.getInteger( ACTIVE_TASK_INSTANCES_RESULT_ATTRIBUTE );
      Integer lExpectedNumberOfActiveTaskInstances = 1;
      assertEquals( "Unexpected row returned", lAnotherReqDefn,
            lResultDs.getKey( TaskTaskKey.class, TASK_KEY ) );
      assertEquals( "Unexpected number of active task instances returned for the inventory",
            lExpectedNumberOfActiveTaskInstances, lActualNumberOfActiveTaskInstances );
   }


   private DataSet execute( TaskTaskKey aTaskKey, HumanResourceKey aHumanResourceKey )
         throws Exception {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTaskKey, "aTaskDbId", "aTaskId" );
      lArgs.add( aHumanResourceKey, "aHrDbId", "aHrId" );
      // Execute the query
      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
