
package com.mxi.mx.core.assembler.lrp.plan;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.mxi.mx.core.assembler.lrp.AssemblerTestCase;
import com.mxi.mx.core.assembler.lrp.data.DataLoader;
import com.mxi.mx.model.lrp.Aircraft;
import com.mxi.mx.model.lrp.Block;
import com.mxi.mx.model.lrp.BlockDefinition;
import com.mxi.mx.model.lrp.Event;
import com.mxi.mx.model.lrp.LRPPlan;
import com.mxi.mx.model.lrp.WorkPackage;


/**
 * Tests the plan assembler
 *
 * @author slevert
 */
public class PlanAssemblerTest extends AssemblerTestCase {

   LRPPlan iPlanToSave;

   private PlanAssembler iPlanAssembler = new PlanAssembler();


   @Before
   public void setUp() throws Exception {
      iPlanToSave = DataLoader.getPlan();
   }


   /**
    * Ensures the plan assembler can retrieve data
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testGetData() throws Exception {
      LRPPlan lSavedPlan = iPlanAssembler.insertData( iPlanToSave );
      assertNotNull( lSavedPlan );

      LRPPlan lRetrievedPlan = iPlanAssembler.getData( lSavedPlan.getPrimaryKey() );
      assertNotNull( lRetrievedPlan );
   }


   /**
    * Ensures the plan assembler can insert data
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testInsertData() throws Exception {
      LRPPlan lSavedPlan = iPlanAssembler.insertData( iPlanToSave );
      assertNotNull( lSavedPlan );

      List<Event> lEvents = lSavedPlan.getEventList();
      for ( Event lEvent : lEvents ) {
         assertNotNull( lEvent.getPrimaryKey() );
         if ( lEvent instanceof WorkPackage ) {
            WorkPackage lWorkPackage = ( WorkPackage ) lEvent;
            for ( Block lBlock : lWorkPackage.getWorkScope() ) {
               assertNotNull( lBlock.getPrimaryKey() );
            }
         }
      }

      List<BlockDefinition> lBlockDefinitions = lSavedPlan.getBlockDefinitions();
      for ( BlockDefinition lBlockDefinition : lBlockDefinitions ) {
         assertNotNull( lBlockDefinition.getPrimaryKey() );
      }

      List<Aircraft> lAircrafts = lSavedPlan.getAircrafts();
      for ( Aircraft lAircraft : lAircrafts ) {
         assertNotNull( lAircraft.getPrimaryKey() );
      }
   }


   /**
    * This test ensures that update plan works.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testUpdateData() throws Exception {

      // create plan
      LRPPlan lSavedPlan = iPlanAssembler.insertData( iPlanToSave );
      assertNotNull( lSavedPlan );

      // save plan
      LRPPlan lUpdatedPlan = iPlanAssembler.updateData( lSavedPlan );
      assertNotNull( lUpdatedPlan );
   }
}
