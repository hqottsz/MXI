
package com.mxi.mx.core.services.req;

import javax.transaction.UserTransaction;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefPartProviderTypeKey;
import com.mxi.mx.core.key.TaskInstPartKey;


/**
 * This class tests the PartRequestGenerationService class.
 *
 * @author dsewell
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class PartRequestGenerationServiceDaoTest {

   private Mockery iContext = new Mockery();
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Tests that no alert is sent if the request is deleted.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testGenerateIssueTransferRecord_RequestDeleted() throws Exception {

      final UserTransaction lTx = iContext.mock( UserTransaction.class );

      iContext.checking( new Expectations() {

         {
            one( lTx ).rollback();
            exactly( 2 ).of( lTx ).begin();
            one( lTx ).commit();
         }
      } );

      // try to generate the issue transfer record for a non-existent part request
      new PartRequestGenerationService().generateIssueTransferRecord( lTx,
            new PartRequestKey( 4650, 1000 ) );

      iContext.assertIsSatisfied();
   }


   /**
    * Tests that no alert is sent when trying to generate a part request if the task is deleted.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testGeneratePartRequest_TaskDeleted() throws Exception {

      final UserTransaction lTx = iContext.mock( UserTransaction.class );

      iContext.checking( new Expectations() {

         {
            one( lTx ).rollback();
            exactly( 2 ).of( lTx ).begin();
            one( lTx ).commit();
         }
      } );

      // try to generate the part request for a non-existent task
      new PartRequestGenerationService().generatePartRequest( lTx,
            new TaskInstPartKey( 4650, 1000, 1, 1 ), new RefPartProviderTypeKey( "CUSTPROV" ) );

      iContext.assertIsSatisfied();
   }

}
