package com.mxi.mx.core.services.dataservices;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.services.dataservices.exceptions.UnexpectedNumComponentsInFileException;


/**
 * Tests the UnexpectedNumComponentsInFileException class
 *
 * @author sufelk
 */

@RunWith( BlockJUnit4ClassRunner.class )
public class UnexpectedNumComponentsInFileExceptionTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    *
    * GIVEN a bulk load data feature with a file that should contain 10 elements, WHEN a file with 5
    * elements is uploaded, THEN an UnexpectedNumComponentsInFileException should be raised.
    *
    * @throws UnexpectedNumComponentsInFileException
    *            if the file does not contain the expected number of components in its header
    */
   @Test( expected = UnexpectedNumComponentsInFileException.class )
   public void testUnexpectedNumComponentsException_NumComponentsMismatched()
         throws UnexpectedNumComponentsInFileException {
      UnexpectedNumComponentsInFileException.validate( 10, 5 );

      Assert.fail(
            "Exception should be raised because the file does not contain the expected number of components." );
   }


   /**
    *
    * GIVEN a bulk load data feature with a file that should contain 10 elements, WHEN a file with
    * 10 elements is uploaded, THEN no exceptions should be raised
    *
    * @throws UnexpectedNumComponentsInFileException
    *            if the file does not contain the expected number of components in its header
    */
   @Test( expected = Test.None.class )
   public void testUnexpectedNumComponentsException_NumComponentsMatchesExpected()
         throws UnexpectedNumComponentsInFileException {
      UnexpectedNumComponentsInFileException.validate( 10, 10 );

   }
}
