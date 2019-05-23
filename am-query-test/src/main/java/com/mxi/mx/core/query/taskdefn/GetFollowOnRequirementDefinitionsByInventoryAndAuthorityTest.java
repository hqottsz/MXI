package com.mxi.mx.core.query.taskdefn;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.task.TaskTaskTable;


/**
 * This class tests the query
 * com.mxi.mx.core.query.taskdefn.GetFollowOnRequirementDefinitionsByInventoryAndAuthority.qrx
 *
 */
public class GetFollowOnRequirementDefinitionsByInventoryAndAuthorityTest {

   private static final String APPLICABLE_APPLICABILITY_RANGE = "A200-A300";
   private static final String NOT_APPLICABLE_APPLICABILITY_RANGE = "A100-A150";
   private static final String AIRCRAFT_PART_NO = "AC1";

   // Applicability rule based on Part No has to be in the specified format as per
   // getTaskApplicability function in database
   private static final String APPLICABILITY_RULE =
         "rootpart.part_no_oem = " + "'" + AIRCRAFT_PART_NO + "'";
   private static final String AIRCRAFT_SYS_SLOT = "ASYSCS";
   private static final String AIRCRAFT_SYSTEM = "ASYS";
   private static final String AIRCRAFT_APPLICABILITY_CODE = "A230";
   private static final String RESULT_COLUMN_ALT_ID = "alt_id";

   // Follow-on task definition codes
   private static final String FOLLOW_ON_TASK_DEFN_ALLOW_INITIALIZATION =
         "FOLLOW-ON TASK DEFN ALLOW INITIALIZATION";
   private static final String FOLLOW_ON_TASK_DEFN_PREVENT_INITIALIZATION =
         "FOLLOW-ON TASK DEFN PREVENT INITIALIZATION";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Test
   public void itReturnsFollowOnReqDefnApplicableToSystemInventoryAssembly() {

      final HumanResourceKey lHumanResourceKey =
            Domain.createHumanResource( aHumanResource -> aHumanResource.setAllAuthority( true ) );

      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly(
            aAircraftAssembly -> aAircraftAssembly.setRootConfigurationSlot(
                  aRootCs -> aRootCs.addConfigurationSlot( aConfigSlot -> {
                     aConfigSlot.setCode( AIRCRAFT_SYS_SLOT );
                     aConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
                  } ) ) );

      final ConfigSlotKey lAircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( lAircraftAssembly );

      final ConfigSlotKey lAircraftSystemConfigSlot =
            Domain.readSubConfigurationSlot( lAircraftRootConfigSlot, AIRCRAFT_SYS_SLOT );

      final PartNoKey lAircraftPart = Domain.createPart( aPart -> {
         aPart.setInventoryClass( RefInvClassKey.ACFT );
         aPart.setCode( AIRCRAFT_PART_NO );
      } );

      // Non-applicable Follow-on requirement definition against the aircraft root config slot
      Domain.createRequirementDefinition( aRequirementDefinition -> {
         aRequirementDefinition.setApplicabilityRange( NOT_APPLICABLE_APPLICABILITY_RANGE );
         aRequirementDefinition.againstConfigurationSlot( lAircraftRootConfigSlot );
         aRequirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
      } );

      // Non-applicable Follow-on requirement definition against the aircraft root config slot
      Domain.createRequirementDefinition( aRequirementDefinition -> {
         aRequirementDefinition.setApplicabilityRange( NOT_APPLICABLE_APPLICABILITY_RANGE );
         aRequirementDefinition.againstConfigurationSlot( lAircraftRootConfigSlot );
         aRequirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
      } );

      final TaskTaskKey lApplicableFollowOnReqDefinition =
            Domain.createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.setApplicabilityRange( APPLICABLE_APPLICABILITY_RANGE );
               aRequirementDefinition.setApplicabilityRule( APPLICABILITY_RULE );
               aRequirementDefinition.againstConfigurationSlot( lAircraftSystemConfigSlot );
               aRequirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
            } );

      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAircraftAssembly );
         aAircraft.setApplicabilityCode( AIRCRAFT_APPLICABILITY_CODE );
         aAircraft.setPart( lAircraftPart );
         aAircraft.addSystem( aSystem -> {
            aSystem.setPosition( new ConfigSlotPositionKey( lAircraftSystemConfigSlot, 0 ) );
            aSystem.setName( AIRCRAFT_SYSTEM );
         } );
      } );

      final InventoryKey lIventory = Domain.readSystem( lAircraft, AIRCRAFT_SYSTEM );
      // Execute Query
      QuerySet lQs = execute( lIventory, lHumanResourceKey );
      lQs.next();
      // Assert
      UUID lExpectedFollowOnReqDefinitionAltId =
            TaskTaskTable.findByPrimaryKey( lApplicableFollowOnReqDefinition ).getAlternateKey();
      Assert.assertEquals( "Unexpected number of follow on requirement definitions returned", 1,
            lQs.getRowCount() );
      Assert.assertEquals( "Unexpected follow on requirement definition returned",
            lExpectedFollowOnReqDefinitionAltId, lQs.getUuid( RESULT_COLUMN_ALT_ID ) );
   }


   @Test
   public void itReturnsFollowOnReqDefnAgainstEngineSubAssembly() {

      final HumanResourceKey lHumanResourceKey =
            Domain.createHumanResource( aHumanResource -> aHumanResource.setAllAuthority( true ) );

      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();

      final AssemblyKey lEngineAssembly = Domain.createEngineAssembly();

      final ConfigSlotKey lAircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( lAircraftAssembly );

      final ConfigSlotKey lEngineRootConfigSlot =
            Domain.readRootConfigurationSlot( lEngineAssembly );

      // Follow-on requirement definition against the aircraft root config slot
      Domain.createRequirementDefinition( aRequirementDefinition -> {
         aRequirementDefinition.againstConfigurationSlot( lAircraftRootConfigSlot );
         aRequirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
      } );

      // Follow-on requirement definition against the engine root config slot
      final TaskTaskKey lFollowOnReqDefnAgainstEngine =
            Domain.createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.againstConfigurationSlot( lEngineRootConfigSlot );
               aRequirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
            } );

      final InventoryKey lAircraft =
            Domain.createAircraft( aAircraft -> aAircraft.setAssembly( lAircraftAssembly ) );

      final InventoryKey lEngine = Domain.createEngine( aEngine -> {
         aEngine.setAssembly( lEngineAssembly );
         aEngine.setParent( lAircraft );
      } );

      // Execute Query
      QuerySet lQs = execute( lEngine, lHumanResourceKey );
      lQs.next();
      // Assert
      UUID lExpectedFollowOnReqDefinitionAltId =
            TaskTaskTable.findByPrimaryKey( lFollowOnReqDefnAgainstEngine ).getAlternateKey();
      Assert.assertEquals( "Unexpected number of follow on requirement definitions returned", 1,
            lQs.getRowCount() );
      Assert.assertEquals( "Unexpected follow on requirement definition returned",
            lExpectedFollowOnReqDefinitionAltId, lQs.getUuid( RESULT_COLUMN_ALT_ID ) );
   }


   @Test
   public void
         itDoesNotReturnFollowOnReqDefnDefinedAgainstConfigSlotOfAnAssemblyDifferentFromSystemInventoryAssembly() {

      final HumanResourceKey lHumanResourceKey =
            Domain.createHumanResource( aHumanResource -> aHumanResource.setAllAuthority( true ) );

      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly(
            aAircraftAssembly -> aAircraftAssembly.setRootConfigurationSlot(
                  aRootCs -> aRootCs.addConfigurationSlot( aConfigSlot -> {
                     aConfigSlot.setCode( AIRCRAFT_SYS_SLOT );
                     aConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
                  } ) ) );

      // Assembly different from the Fault system's assembly
      final AssemblyKey lEngineAssembly = Domain.createEngineAssembly();

      final ConfigSlotKey lAircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( lAircraftAssembly );

      final ConfigSlotKey lAircraftSystemConfigSlot =
            Domain.readSubConfigurationSlot( lAircraftRootConfigSlot, AIRCRAFT_SYS_SLOT );

      // Follow-on requirement definition against the engine root config slot
      Domain.createRequirementDefinition( aRequirementDefinition -> {
         aRequirementDefinition
               .againstConfigurationSlot( Domain.readRootConfigurationSlot( lEngineAssembly ) );
         aRequirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
      } );

      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAircraftAssembly );
         aAircraft.addSystem( aSystem -> {
            aSystem.setPosition( new ConfigSlotPositionKey( lAircraftSystemConfigSlot, 0 ) );
            aSystem.setName( AIRCRAFT_SYSTEM );
         } );
      } );

      final InventoryKey lInventory = Domain.readSystem( lAircraft, AIRCRAFT_SYSTEM );
      // Execute Query
      QuerySet lQs = execute( lInventory, lHumanResourceKey );
      lQs.next();
      // Assert
      Assert.assertEquals( "Unexpected number of follow on requirement definitions returned", 0,
            lQs.getRowCount() );
   }


   @Test
   public void
         itDoesNotReturnFollowOnReqDefnApplicableToSystemInventoryAssemblyWhenHumanResourceNotAuthorizedForAssembly() {

      final HumanResourceKey lHumanResourceKey =
            Domain.createHumanResource( aHumanResource -> aHumanResource.setAllAuthority( false ) );

      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();

      final ConfigSlotKey lAircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( lAircraftAssembly );

      Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setApplicabilityRange( APPLICABLE_APPLICABILITY_RANGE );
         aReqDefn.setApplicabilityRule( APPLICABILITY_RULE );
         aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
         aReqDefn.setTaskClass( RefTaskClassKey.FOLLOW );
      } );

      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAircraftAssembly );
         aAircraft.setApplicabilityCode( AIRCRAFT_APPLICABILITY_CODE );
      } );
      // Execute Query
      QuerySet lQs = execute( lAircraft, lHumanResourceKey );
      // Assert
      assertEquals(
            "Unexpectedly, Follow-on requirement definition returned despite the provided human resource lacking authority to access the inventory's assembly",
            0, lQs.getRowCount() );
   }


   /**
    * This is testing that follow-on task definitions with 'Prevent Manual Initialization' set as
    * true are filtered by the query.
    *
    */
   @Test
   public void itDoesNotReturnFollowOnTaskDefnsWithPreventInitializationSetAsTrue() {
      final HumanResourceKey lHumanResourceKey =
            Domain.createHumanResource( aHumanResource -> aHumanResource.setAllAuthority( true ) );

      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();

      final ConfigSlotKey lAircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( lAircraftAssembly );

      Domain.createRequirementDefinition( aRequirementDefn -> {
         aRequirementDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
         aRequirementDefn.setTaskClass( RefTaskClassKey.FOLLOW );
         aRequirementDefn.setPreventManualInitialization( true );
         aRequirementDefn.setCode( FOLLOW_ON_TASK_DEFN_PREVENT_INITIALIZATION );
      } );

      Domain.createRequirementDefinition( aRequirementDefn -> {
         aRequirementDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
         aRequirementDefn.setTaskClass( RefTaskClassKey.FOLLOW );
         aRequirementDefn.setPreventManualInitialization( false );
         aRequirementDefn.setCode( FOLLOW_ON_TASK_DEFN_ALLOW_INITIALIZATION );
      } );

      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAircraftAssembly );
      } );

      // Execute Query
      QuerySet lQs = execute( lAircraft, lHumanResourceKey );

      // Assert
      assertEquals( "Incorrect numbers of follow-on task definition are returned.", 1,
            lQs.getRowCount() );

      lQs.next();
      assertEquals( "Incorrect follow-on task definition returned.",
            FOLLOW_ON_TASK_DEFN_ALLOW_INITIALIZATION, lQs.getString( "task_code" ) );
   }


   private QuerySet execute( InventoryKey aInventoryKey, HumanResourceKey aHumanResourceKey ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventoryKey, "aInvDbId", "aInvId" );
      lArgs.add( aHumanResourceKey, "aHrDbId", "aHrId" );
      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }

}
