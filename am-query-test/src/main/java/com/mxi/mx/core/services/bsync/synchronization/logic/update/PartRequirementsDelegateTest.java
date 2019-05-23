package com.mxi.mx.core.services.bsync.synchronization.logic.update;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.bsync.synchronization.model.update.PartRequirementsDetails;
import com.mxi.mx.core.services.bsync.synchronization.model.update.PartRequirementsTO;
import com.mxi.mx.core.services.stask.taskpart.DuplicateBomPositionException;
import com.mxi.mx.core.services.stask.taskpart.PartRequirementTO;


public final class PartRequirementsDelegateTest {

   /**
    * This task definition represents a job card that is under a replacement task that has a part
    * requirement for a position that is not the same as the replacement position.
    */
   private static final TaskTaskKey JIC_NON_REPL_PART_REQ = new TaskTaskKey( 4650, 1001 );

   /**
    * This task definition represents a job car that is under a replacement task that a part
    * requirement that is the same as the replacement position.
    */
   private static final TaskTaskKey JIC_REPL_PART_REQ = new TaskTaskKey( 4650, 1002 );

   /** The replacement position on an actual instance of a replacment task. */
   private static final ConfigSlotPositionKey ACTUAL_REPL_POSITION =
         new ConfigSlotPositionKey( 4650, "ACFT", 100, 1 );

   /** The position that is not the replacement position. */
   private static final ConfigSlotPositionKey NON_REPL_POSITION =
         new ConfigSlotPositionKey( 4650, "ACFT", 50, 2 );

   /** An arbitrary human resource. */
   private static final HumanResourceKey HR_KEY = HumanResourceKey.ADMIN;
   private static final int USER_ID = 1;

   /** The class under test. */
   private PartRequirementsDelegate iDelegate;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   @Rule
   public OperateAsUserRule iOperateAsUserRule = new OperateAsUserRule( USER_ID, "testuser" );


   /**
    * Test that a new part requirement is created with the proper position when a job card is under
    * a replacement task but the part requirement is for a position other than the replacement
    * position.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNewPartRequirementJicUnderReplForNonReplPosition() throws Exception {
      Set<PartRequirementTO> lPartRequirements =
            iDelegate.getPartRequirements( JIC_NON_REPL_PART_REQ, ACTUAL_REPL_POSITION, HR_KEY );

      Iterator<PartRequirementTO> lIterator = lPartRequirements.iterator();
      PartRequirementTO lPartRequirement = lIterator.next();
      assertNotNull( "Expected a part requirement", lPartRequirement );

      assertFalse( "Expected only one part requirement", lIterator.hasNext() );

      assertEquals( "Expecting the position to be the position specified by the definition",
            NON_REPL_POSITION, lPartRequirement.getBomItemPositionKey() );
   }


   /**
    * Test that a new part requirement is created with the proper position when a job card is under
    * a replacement task and the part requirement is the replacement position.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNewPartRequirementJicUnderReplForReplPosition() throws Exception {
      Set<PartRequirementTO> lPartRequirements =
            iDelegate.getPartRequirements( JIC_REPL_PART_REQ, ACTUAL_REPL_POSITION, HR_KEY );

      Iterator<PartRequirementTO> lIterator = lPartRequirements.iterator();
      PartRequirementTO lPartRequirement = lIterator.next();
      assertNotNull( "Expected a part requirement", lPartRequirement );

      assertFalse( "Expected only one part requirement", lIterator.hasNext() );

      assertEquals( "Expecting the position to be the position specified by the parameter",
            ACTUAL_REPL_POSITION, lPartRequirement.getBomItemPositionKey() );
   }


   /**
    * Test that under the circumstances of a DuplicateBomPositionException the baseline sync process
    * will synchronize the task.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void synchronize_when_DuplicateBomPositionException() throws Exception {

      // data set up
      final TaskTaskKey lCurrentRevision = new TaskTaskKey( 4650, 1 );
      final TaskTaskKey lNewRevision = new TaskTaskKey( 4650, 2 );
      final TaskKey lSchedTask = new TaskKey( 4650, 1 );

      PartRequirementsDetails lTransferObject =
            new PartRequirementsTO( lCurrentRevision, lNewRevision, lSchedTask, HR_KEY );

      try {
         // execute
         iDelegate.synchronize( lTransferObject, HR_KEY );
      } catch ( DuplicateBomPositionException e ) {
         fail( "DuplicateBomPositionException exception should not be thrown" );
      }

   }


   @Before
   public void setUp() throws Exception {
      iDelegate = new PartRequirementsDelegate();

      UserParameters.getInstance( USER_ID, "SECURED_RESOURCE" )
            .setBoolean( "ACTION_REMOVE_HISTORIC_PART_REQUIREMENT", false );
   }


   @Before
   public void loadData() throws Exception {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }
}
