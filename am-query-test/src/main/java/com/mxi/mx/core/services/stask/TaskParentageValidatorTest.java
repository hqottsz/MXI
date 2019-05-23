
package com.mxi.mx.core.services.stask;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Rule;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.TaskKey;


/**
 * Tests the behaviour of the TaskParentageException
 *
 * <pre>
 * Tests for valid parent-child configuration allow that:
 *   A block may be a child of a WP
 *   A requirement may be a child of a requirement, block or WP
 *   A JIC may be a child of a requirement or JIC
 *   A WP may not be a child of anything.
 *   A Block may not be a child of a block, req or JIC
 *   A Req may not be a child of a JIC
 *   A JIC may not be a child of a WP or Block.
 * </pre>
 */

@RunWith( Theories.class )
public class TaskParentageValidatorTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   // Task types to be validated
   private enum TaskType {
      CHECK, BLOCK, REQ, JIC;
   }


   public static @DataPoints TaskType[] iTasks = TaskType.values();


   private boolean isParentageAllowed( TaskType aParentTask, TaskType aChildTask ) {
      // A work package may be a parent of a requirement or block
      if ( aParentTask == TaskType.CHECK )
         return ( aChildTask == TaskType.BLOCK || aChildTask == TaskType.REQ );

      // A block may be a parent of a requirement
      if ( aParentTask == TaskType.BLOCK )
         return ( aChildTask == TaskType.REQ );

      // A requirement may be a parent of a requirement (in an ad-hoc scenario, though this is not
      // validated) or a jic
      if ( aParentTask == TaskType.REQ )
         return ( aChildTask == TaskType.REQ || aChildTask == TaskType.JIC );

      // a JIC may be a parent of a JIC (in an ad-hoc scenario, though this is not validated)
      if ( aParentTask == TaskType.JIC )
         return ( aChildTask == TaskType.JIC );

      throw new IllegalArgumentException();
   }


   private TaskKey createPerType( TaskType aTaskType ) {
      switch ( aTaskType ) {
         case CHECK:
            return Domain.createWorkPackage();
         case BLOCK:
            return Domain.createBlock();
         case REQ:
            return Domain.createRequirement();
         case JIC:
            return Domain.createJobCard();
         default:
            return null;
      }
   }


   @Theory
   public void testParentageIsValid( TaskType aParentType, TaskType aChildType ) {
      Assume.assumeTrue( isParentageAllowed( aParentType, aChildType ) );
      Assert.assertTrue( TaskParentageValidator.isValid( createPerType( aParentType ),
            createPerType( aChildType ) ) );
   }


   @Theory
   public void testParentageIsInvalid( TaskType aParentType, TaskType aChildType ) {
      Assume.assumeFalse( isParentageAllowed( aParentType, aChildType ) );
      Assert.assertFalse( TaskParentageValidator.isValid( createPerType( aParentType ),
            createPerType( aChildType ) ) );
   }
}
