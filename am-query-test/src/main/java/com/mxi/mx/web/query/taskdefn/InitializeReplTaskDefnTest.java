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
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * This is a test class for testing InitializeReplTaskDefn.qrx. The query, in name and description,
 * is intended to be used for REPL tasks but is not written as such. Therefore, multiple tests in
 * this test class do not use REPLs.
 *
 */
public class InitializeReplTaskDefnTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String ACTIVE_TASK_INSTANCES_RESULT_ATTRIBUTE = "active_task_instances";


   @Test
   public void itReturnsZeroActiveInstanceWhenNoActiveInstancesOfTaskDefinitionForAnInventory()
         throws Exception {
      // Arrange
      // Aircraft, Aircraft Assembly, Human Resource
      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();
      final InventoryKey lAircraft =
            Domain.createAircraft( aAircraft -> aAircraft.setAssembly( lAircraftAssembly ) );

      // Revision of a requirement definition
      final TaskTaskKey lReqDefn = Domain.createRequirementDefinition( aReqDefn -> aReqDefn
            .againstConfigurationSlot( Domain.readRootConfigurationSlot( lAircraftAssembly ) ) );

      // Requirement based on the requirement definition revision
      Domain.createRequirement( aReq -> {
         aReq.setDefinition( lReqDefn );
         aReq.setStatus( RefEventStatusKey.COMPLETE );
         aReq.setInventory( lAircraft );
      } );
      HumanResourceKey lHrKey = Domain.createHumanResource( aHr -> aHr.setAllAuthority( true ) );
      // Act
      ;
      DataSet lResultDs = execute( populateWhereClauseForAircraftAssyInv(), lReqDefn, lHrKey );
      // Assert
      Integer lExpectedNumberOfActiveTaskInstances = 0;
      assertEquals( "Unexpected number of rows returned", 1, lResultDs.getRowCount() );
      lResultDs.next();
      Integer lActualNumberOfActiveTaskInstances =
            lResultDs.getInteger( ACTIVE_TASK_INSTANCES_RESULT_ATTRIBUTE );
      assertEquals( "Unexpected number of active task instances returned for the inventory",
            lExpectedNumberOfActiveTaskInstances, lActualNumberOfActiveTaskInstances );
   }


   @Test
   public void itReturnOneActiveTaskInstanceWhenOneActiveTaskOfTaskDefinitionForAnInventory()
         throws Exception {
      // Arrange
      // Aircraft, Aircraft Assembly
      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();
      final InventoryKey lAircraft =
            Domain.createAircraft( aAircraft -> aAircraft.setAssembly( lAircraftAssembly ) );

      // Revision of a requirement definition
      final TaskTaskKey lReqDefn = Domain.createRequirementDefinition( aReqDefn -> aReqDefn
            .againstConfigurationSlot( Domain.readRootConfigurationSlot( lAircraftAssembly ) ) );

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
      DataSet lResultDs = execute( populateWhereClauseForAircraftAssyInv(), lReqDefn, lHrKey );
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
      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();
      final InventoryKey lAircraft =
            Domain.createAircraft( aAircraft -> aAircraft.setAssembly( lAircraftAssembly ) );
      final ConfigSlotKey lAircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( lAircraftAssembly );

      // Requirement definition
      final TaskTaskKey lReqDefn = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
      } );

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
      DataSet lResultDs = execute( populateWhereClauseForAircraftAssyInv(), lReqDefn, lHrKey );
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
         itReturnsTwoActiveTaskInstancesWhenTwoActiveTasksOfDifferentRevisionsOfTaskDefinitionForAnInventory()
               throws Exception {
      // Arrange
      // Aircraft, Aircraft Assembly
      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();
      final InventoryKey lAircraft =
            Domain.createAircraft( aAircraft -> aAircraft.setAssembly( lAircraftAssembly ) );
      final ConfigSlotKey lAircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( lAircraftAssembly );

      // 2 revisions of a requirement definition
      final TaskTaskKey lReqDefnFormerRevision = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
         aReqDefn.setRevisionNumber( 1 );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.REVISION );
      } );
      final TaskTaskKey lReqDefnActiveRevision = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
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
      DataSet lResultDs =
            execute( populateWhereClauseForAircraftAssyInv(), lReqDefnActiveRevision, lHrKey );
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
      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();
      final InventoryKey lAircraft =
            Domain.createAircraft( aAircraft -> aAircraft.setAssembly( lAircraftAssembly ) );
      final ConfigSlotKey lAircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( lAircraftAssembly );

      // 2 revisions of a requirement definition
      final TaskTaskKey lReqDefn = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
         aReqDefn.setRevisionNumber( 1 );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
      } );
      final TaskTaskKey lAnotherReqDefn = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
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
      DataSet lResultDs = execute( populateWhereClauseForAircraftAssyInv(), lReqDefn, lHrKey );
      // Assert
      assertEquals( "Unexpected number of rows returned", 1, lResultDs.getRowCount() );
      lResultDs.next();
      Integer lActualNumberOfActiveTaskInstances =
            lResultDs.getInteger( ACTIVE_TASK_INSTANCES_RESULT_ATTRIBUTE );
      Integer lExpectedNumberOfActiveTaskInstances = 1;
      assertEquals( "Unexpected number of active task instances returned for the inventory",
            lExpectedNumberOfActiveTaskInstances, lActualNumberOfActiveTaskInstances );
   }


   private DataSet execute( DataSetArgument aDataSetArguments, TaskTaskKey aTaskKey,
         HumanResourceKey aHumanResourceKey ) throws Exception {

      aDataSetArguments.add( aTaskKey, "aTaskDbId", "aTaskId" );
      aDataSetArguments.add( aHumanResourceKey, "aHrDbId", "aHrId" );

      // Execute the query
      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), aDataSetArguments );
   }


   private DataSetArgument populateWhereClauseForAircraftAssyInv() {
      DataSetArgument lArgs = new DataSetArgument();
      String lReplClause = "WHERE_REPL";
      String lTaskClause = "WHERE_TASK";
      lArgs.addFromTable( "FROM_REPL", "inv_inv," );
      lArgs.addWhere( lReplClause, "inv_inv.orig_assmbl_db_id  = task_task.repl_assmbl_db_id" );
      lArgs.addWhere( lReplClause, "inv_inv.orig_assmbl_cd     = task_task.repl_assmbl_cd" );
      lArgs.addWhere( lReplClause, "inv_inv.inv_class_cd IN ('ACFT', 'ASSY')" );
      lArgs.addWhere( lTaskClause, "task_inv_inv.orig_assmbl_db_id  = task_task.assmbl_db_id" );
      lArgs.addWhere( lTaskClause, "task_inv_inv.orig_assmbl_cd     = task_task.assmbl_cd" );
      lArgs.addWhere( lTaskClause, "task_inv_inv.inv_class_cd IN ('ACFT', 'ASSY')" );
      return lArgs;
   }
}
