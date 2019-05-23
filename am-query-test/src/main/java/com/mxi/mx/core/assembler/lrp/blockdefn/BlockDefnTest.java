
package com.mxi.mx.core.assembler.lrp.blockdefn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.mxi.mx.core.assembler.lrp.AssemblerTestCase;
import com.mxi.mx.core.assembler.lrp.data.DataLoader;
import com.mxi.mx.core.assembler.lrp.plan.PlanAssembler;
import com.mxi.mx.model.exception.PlanNameAlreadyExistsException;
import com.mxi.mx.model.lrp.Block;
import com.mxi.mx.model.lrp.BlockDefinition;
import com.mxi.mx.model.lrp.Event;
import com.mxi.mx.model.lrp.LRPPlan;
import com.mxi.mx.model.lrp.PlanNotFoundException;


/**
 * DOCUMENT_ME
 *
 * @author slevert
 */
public class BlockDefnTest extends AssemblerTestCase {

   LRPPlan iPlanToSave;

   private PlanAssembler iPlanAssembler = new PlanAssembler();


   @Before
   public void setUp() throws Exception {
      iPlanToSave = DataLoader.getPlan();
   }


   /**
    * DOCUMENT_ME
    *
    * @throws PlanNotFoundException
    * @throws PlanNameAlreadyExistsException
    */
   @Test
   public void testRemoveBlockDefinition()
         throws PlanNotFoundException, PlanNameAlreadyExistsException {
      LRPPlan lSavedPlan = iPlanAssembler.insertData( iPlanToSave );
      assertNotNull( lSavedPlan );

      LRPPlan lRetrievedPlan = iPlanAssembler.getData( lSavedPlan.getPrimaryKey() );
      assertNotNull( lRetrievedPlan );

      try {
         lRetrievedPlan.setBlocks( new HashMap<String, Block>() );

         lRetrievedPlan.setBlockDefinitions( new ArrayList<BlockDefinition>() );

         lRetrievedPlan.setEvents( new ArrayList<Event>() );

         lSavedPlan = iPlanAssembler.updateData( lRetrievedPlan );
         assertNotNull( lSavedPlan );
         assertEquals( 0, lSavedPlan.getBlocks().size() );
         assertEquals( 0, lSavedPlan.getBlockDefinitions().size() );
         assertEquals( 0, lSavedPlan.getEventList().size() );
      } catch ( Exception e ) {
         e.printStackTrace();
         fail( "Unexpected Exception updating data: " + e.getMessage() );
      }
   }
}
