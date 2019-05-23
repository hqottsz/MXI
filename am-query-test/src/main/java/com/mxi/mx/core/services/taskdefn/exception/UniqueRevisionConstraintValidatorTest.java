
package com.mxi.mx.core.services.taskdefn.exception;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;


/**
 * Tests the behaviour of the UniqueRevisionConstraintValidator.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class UniqueRevisionConstraintValidatorTest {

   // Create a test "unique revision constraint violation" exception that has a message
   // containing the SQL error code and the constraint name.
   private static final Exception UNIQUE_REVISION_EXCEPTION =
         new Exception( UniqueRevisionConstraintValidator.CONSTRAINT_VIOLATION_ERROR_CODE
               + UniqueRevisionConstraintValidator.UNIQUE_REVISION_CONSTRAINT_NAME );

   private static final String TASK_DEFN_DESC = "task defn desc";

   private final UniqueRevisionConstraintValidator iValidator =
         new UniqueRevisionConstraintValidator();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Ensure the validator throws a RevisionAlreadyExistsException with the correct task definition
    * description.
    */
   @Test
   public void testCorrectTaskDefnDescUsed() {

      Exception lTestException = new Exception( UNIQUE_REVISION_EXCEPTION );

      try {
         iValidator.check( lTestException, TASK_DEFN_DESC );
         fail( "Expected RevisionAlreadyExistsException but it was not thrown." );
      } catch ( RevisionAlreadyExistsException e ) {
         assertTrue( e.getMessageArgument( 0 ).contains( TASK_DEFN_DESC ) );
      }
   }


   /**
    * Ensure the validator throws a RevisionAlreadyExistsException when checking an exception that
    * was caused by the "unique revision constraint violation" exception and the constraint
    * violation exception is at the max depth.
    */
   @Test
   public void testWithExceptionCausedByConstraintViolationAtChainDepth() {

      Exception lTestException =
            generateExceptionChain( UniqueRevisionConstraintValidator.MAX_CAUSE_CHAIN_DEPTH );

      try {
         iValidator.check( lTestException, "" );
         fail( "Expected RevisionAlreadyExistsException but it was not thrown." );
      } catch ( RevisionAlreadyExistsException e ) {
         ;
      }
   }


   /**
    * Ensure the validator does not throw a RevisionAlreadyExistsException when checking an
    * exception that was caused by the "unique revision constraint violation" exception BUT the
    * constraint violation exception is beyond the max chain depth.
    */
   @Test
   public void testWithExceptionCausedByConstraintViolationBeyondChainDepth() {

      Exception lTestException =
            generateExceptionChain( UniqueRevisionConstraintValidator.MAX_CAUSE_CHAIN_DEPTH + 1 );

      try {
         iValidator.check( lTestException, "" );
      } catch ( RevisionAlreadyExistsException e ) {
         fail( "Unexpected RevisionAlreadyExistsException" );
      }
   }


   /**
    * Ensure the validator throws a RevisionAlreadyExistsException when checking an exception that
    * was caused by the "unique revision constraint violation" exception and the constraint
    * violation exception is lower in the "cause chain".
    */
   @Test
   public void testWithExceptionCausedByConstraintViolationLowerInCauseChain() {

      assert ( UniqueRevisionConstraintValidator.MAX_CAUSE_CHAIN_DEPTH > 3 );

      Exception lTestException = generateExceptionChain( 3 );

      try {
         iValidator.check( lTestException, "" );
         fail( "Expected RevisionAlreadyExistsException but it was not thrown." );
      } catch ( RevisionAlreadyExistsException e ) {
         ;
      }
   }


   /**
    * Ensure the validator does not throw a RevisionAlreadyExistsException when checking an
    * exception that was caused by an exception with the constraint violation error code but with a
    * different constraint name.
    */
   @Test
   public void testWithExceptionCausedByDifferentConstraintName() {

      Exception lTestException =
            new Exception( UniqueRevisionConstraintValidator.CONSTRAINT_VIOLATION_ERROR_CODE
                  + "another constraint name" );
      lTestException = new Exception( "", lTestException );

      try {
         iValidator.check( lTestException, "" );
      } catch ( RevisionAlreadyExistsException e ) {
         fail( "Unexpected RevisionAlreadyExistsException" );
      }
   }


   /**
    * Ensure the validator does not throw a RevisionAlreadyExistsException when checking an
    * exception that was caused by an exception against the unique revision constraint name but with
    * a different error code.
    */
   @Test
   public void testWithExceptionCausedByDifferentErrorInvolvingConstraintName() {

      Exception lTestException = new Exception( "another error code"
            + UniqueRevisionConstraintValidator.UNIQUE_REVISION_CONSTRAINT_NAME );
      lTestException = new Exception( "", lTestException );

      try {
         iValidator.check( lTestException, "" );
      } catch ( RevisionAlreadyExistsException e ) {
         fail( "Unexpected RevisionAlreadyExistsException" );
      }
   }


   /**
    * Ensure the validator throws a RevisionAlreadyExistsException when checking an exception that
    * was immediately caused by the "unique revision constraint violation" exception.
    */
   @Test
   public void testWithExceptionImmediatelyCausedByUniqueRevisionConstraintViolation() {

      Exception lTestException = new Exception( UNIQUE_REVISION_EXCEPTION );

      try {
         iValidator.check( lTestException, "" );
         fail( "Expected RevisionAlreadyExistsException but it was not thrown." );
      } catch ( RevisionAlreadyExistsException e ) {
         ;
      }
   }


   /**
    * Ensure the validator does not throw a RevisionAlreadyExistsException when checking an
    * exception that was not caused by the "unique revision constraint violation" exception.
    */
   @Test
   public void testWithExceptionNotCausedByConstraintViolation() {

      Exception lTestException = new Exception( "some other exception" );
      lTestException = new Exception( "", lTestException );

      try {
         iValidator.check( lTestException, "" );
      } catch ( RevisionAlreadyExistsException e ) {
         fail( "Unexpected RevisionAlreadyExistsException" );
      }
   }


   /**
    * Ensure the validator does not throw a RevisionAlreadyExistsException when checking a null
    * exception.
    */
   @Test
   public void testWithNullException() {
      try {
         iValidator.check( null, "" );
      } catch ( RevisionAlreadyExistsException e ) {
         fail( "Unexpected RevisionAlreadyExistsException" );
      }
   }


   /**
    * Generates an exception chain with a "unique revision constraint violation" exception at the
    * provided chain depth.
    *
    * @param aChainDepth
    *           depth at which the target exception exists
    *
    * @return exception containing a cause exception chain
    */
   private Exception generateExceptionChain( int aChainDepth ) {
      int lDepth = 1;

      Exception lException = new Exception( UNIQUE_REVISION_EXCEPTION );

      while ( lDepth++ < aChainDepth ) {

         // Note: the chained exceptions each need their own message, otherwise they will use the
         // message of the cause exception.
         lException = new Exception( "" + lDepth, lException );
      }

      return lException;
   }

}
