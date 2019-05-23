package com.mxi.mx.core.services.req;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.holder.StringHolder;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.TaskKey;


@RunWith( BlockJUnit4ClassRunner.class )
public class PartRequestIsForSuppressedTaskExceptionTest {

   private static final String SUPRESSING_TASK_BARCODE = "TRUMP_TASK";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Test
   public void validateSuppressedTask() {
      TaskKey taskKey = createSuppressedTask();
      try {

         PartRequestIsForSuppressedTaskException.validate( taskKey );
         fail( "Expected PartRequestIsForSuppressedTaskException" );

      } catch ( PartRequestIsForSuppressedTaskException e ) {

         // Verify that the exception was thrown and contains the expected message.
         assertEquals( "core.err.31646", e.getMessageKey() );
         assertEquals( e.getMessageArgument( 0 ), SUPRESSING_TASK_BARCODE );

      }
   }


   @Test
   public void validateUnsuppressedTask() {
      TaskKey taskKey = createUnsuppressedTask();
      try {

         PartRequestIsForSuppressedTaskException.validate( taskKey );

      } catch ( PartRequestIsForSuppressedTaskException e ) {

         fail( "PartRequestIsForSuppressedTaskException not expected." );

      }
   }


   /**
    * Tests that the message is correct
    */
   @Test
   public void testContructor() {

      PartRequestIsForSuppressedTaskException lExp = new PartRequestIsForSuppressedTaskException(
            new StringHolder( SUPRESSING_TASK_BARCODE ) );
      assertEquals( "getMessageKey() ", "core.err.31646", lExp.getMessageKey() );
      assertEquals( lExp.getMessageArgument( 0 ), SUPRESSING_TASK_BARCODE );

   }


   /**
    * Tests that the passing null doesn't break the constructor
    */
   @Test
   public void testContructorNullArg() {

      PartRequestIsForSuppressedTaskException lExp =
            new PartRequestIsForSuppressedTaskException( null );
      assertEquals( "getMessageKey() ", "core.err.31646", lExp.getMessageKey() );

   }


   /**
    * Helper method that sets up data.
    *
    */
   private TaskKey createSuppressedTask() {

      TaskKey lSupressingTask = new TaskBuilder().withBarcode( SUPRESSING_TASK_BARCODE ).build();
      return new TaskBuilder().withSuppressingTask( lSupressingTask ).build();

   }


   /**
    * Helper method that sets up data.
    *
    */
   private TaskKey createUnsuppressedTask() {

      return new TaskBuilder().withBarcode( SUPRESSING_TASK_BARCODE ).build();

   }
}
