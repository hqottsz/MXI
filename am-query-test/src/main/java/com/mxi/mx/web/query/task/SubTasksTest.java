package com.mxi.mx.web.query.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

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
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * This class tests the SubTasks query.
 *
 */
public class SubTasksTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private static final Integer JIC2_ORDER = Integer.valueOf( 1 );
   private static final Integer JIC1_ORDER = Integer.valueOf( 2 );
   private static final String PARENT_TRK_CONFIG_SLOT = "PARENT_TRK_CONFIG_SLOT";
   private static final String PARENT_TRK_PART_GROUP = "PGP";
   private static final String ASSEMBLY_CODE = "A320";


   /**
    * <pre>
    * Given a requirement definition against a TRK config slot
    * AND the requirement definition has more than one job card definitions assigned to it with a defined order
    * AND the requirement definition is on-condition with a requirement based on it
    * AND there are more than one job cards based on the job card definitions which are assigned to the requirement
    * The query returns the order of the job cards within the requirement same as what is defined in the baseline
    * </pre>
    *
    */
   @Test
   public void itReturnsReqWithJICsOrderedDefinedinRequirementDefinition() throws Exception {

      PartNoKey lParentTrkPart = Domain.createPart( aTrkPart -> {
         aTrkPart.setInventoryClass( RefInvClassKey.TRK );
      } );

      AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly( aAircraftAssembly -> {
         aAircraftAssembly
               .setRootConfigurationSlot( aRootCs -> aRootCs.addConfigurationSlot( aParentTrkCs -> {
                  aParentTrkCs.setCode( PARENT_TRK_CONFIG_SLOT );
                  aParentTrkCs.setConfigurationSlotClass( RefBOMClassKey.TRK );
                  aParentTrkCs.addPartGroup( aPartGroup -> {
                     aPartGroup.setInventoryClass( RefInvClassKey.TRK );
                     aPartGroup.setCode( PARENT_TRK_PART_GROUP );
                     aPartGroup.addPart( lParentTrkPart );
                  } );
               } ) );
         aAircraftAssembly.setCode( ASSEMBLY_CODE );
      } );

      ConfigSlotKey lRootCs = Domain.readRootConfigurationSlot( lAircraftAssembly );
      ConfigSlotKey lParentTrkCs =
            Domain.readSubConfigurationSlot( lRootCs, PARENT_TRK_CONFIG_SLOT );

      TaskTaskKey lJicDefinition1 = Domain.createJobCardDefinition( aJicDefn -> {
         aJicDefn.setCode( "JIC1" );
         aJicDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         aJicDefn.setConfigurationSlot( lParentTrkCs );
      } );
      TaskTaskKey lJicDefinition2 = Domain.createJobCardDefinition( aJicDefn -> {
         aJicDefn.setCode( "JIC2" );
         aJicDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         aJicDefn.setConfigurationSlot( lParentTrkCs );
      } );
      Map<TaskTaskKey, Integer> lJobCardDefinitionsOrder = new HashMap<>();
      lJobCardDefinitionsOrder.put( lJicDefinition2, JIC2_ORDER );
      lJobCardDefinitionsOrder.put( lJicDefinition1, JIC1_ORDER );

      TaskTaskKey lReqDefn = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setOnCondition( true );
         aReqDefn.setTaskClass( RefTaskClassKey.REQ );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         aReqDefn.againstConfigurationSlot( lParentTrkCs );
         aReqDefn.addJobCardDefinition( lJicDefinition1 );
         aReqDefn.addJobCardDefinition( lJicDefinition2 );
         aReqDefn.setJobCardDefinitionsOrder( lJobCardDefinitionsOrder );
      } );

      InventoryKey lAircraft =
            Domain.createAircraft( aAircraft -> aAircraft.setAssembly( lAircraftAssembly ) );

      ConfigSlotPositionKey lParentComponentPosition = new ConfigSlotPositionKey( lParentTrkCs, 1 );
      PartGroupKey lParentPartGroupKey =
            Domain.readPartGroup( lParentTrkCs, PARENT_TRK_PART_GROUP );

      InventoryKey lParentComponent = Domain.createTrackedInventory( aTrk -> {
         aTrk.setLastKnownConfigSlot( ASSEMBLY_CODE, PARENT_TRK_CONFIG_SLOT, "1" );
         aTrk.setParent( lAircraft );
         aTrk.setPosition( lParentComponentPosition );
         aTrk.setPartGroup( lParentPartGroupKey );
         aTrk.setPartNumber( lParentTrkPart );
      } );

      TaskKey lJic1 = Domain.createJobCard( aJic -> {
         aJic.setDefinition( lJicDefinition1 );
         aJic.setInventory( lParentComponent );
      } );

      TaskKey lJic2 = Domain.createJobCard( aJic -> {
         aJic.setDefinition( lJicDefinition2 );
         aJic.setInventory( lParentComponent );
      } );

      TaskKey lRequirement = Domain.createRequirement( aReq -> {
         aReq.setDefinition( lReqDefn );
         aReq.setInventory( lParentComponent );
         aReq.addJobCard( lJic1 );
         aReq.addJobCard( lJic2 );
      } );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lRequirement, "aTaskDbId", "aTaskId" );
      DataSet lResultDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      if ( lResultDs.getRowCount() != 2 ) {
         fail( "Expected two rows corresponding to job cards to be returned." );
      }
      while ( lResultDs.next() ) {
         EventKey lReturnedJic = lResultDs.getKey( EventKey.class, "subtask_key" );
         if ( lJic1.equals( lReturnedJic ) ) {
            assertEquals(
                  "The actual job card returned after being assigned to the requirement is being retrieved with a different order than the one defined in the requirement definition",
                  JIC1_ORDER, lResultDs.getInteger( "task_jic_req_map.jic_task_order" ) );
         }
         if ( lJic2.equals( lReturnedJic ) ) {
            assertEquals(
                  "The actual job card returned after being assigned to the requirement is being retrieved with a different order than the one defined in the requirement definition",
                  JIC2_ORDER, lResultDs.getInteger( "task_jic_req_map.jic_task_order" ) );
         }
      }

   }

}
