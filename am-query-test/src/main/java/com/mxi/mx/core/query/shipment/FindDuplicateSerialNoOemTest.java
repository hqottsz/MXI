
package com.mxi.mx.core.query.shipment;

import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;


/**
 * This test ensures that the query com.mxi.mx.web.query.inventory.FindDuplicateSerialNoOem.qrx to
 * find similar serial number works correctly.
 *
 * @author swu
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class FindDuplicateSerialNoOemTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * This test runs the query to find similar or duplicate serial number. If all rows report pass,
    * the test is successful.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testFindDuplicateSerialNoOem() throws Exception {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aOemSerialNumber", "SN000015" );
      lArgs.add( "aPartNoDbId", 5001 );
      lArgs.add( "aPartNoId", 1772 );

      QuerySet lResults = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.web.query.inventory.FindDuplicateSerialNoOem", lArgs );

      while ( lResults.next() ) {
         String lTestResult = lResults.getString( "result" );

         if ( !lTestResult.equals( "PASS" ) ) {
            fail( lTestResult );
         }
      }

      // create a new inventory whose serial number contains special characters
      InventoryBuilder lInventory = new InventoryBuilder().withClass( RefInvClassKey.TRK )
            .withPartNo( new PartNoKey( "5001:1772" ) ).withSerialNo( "S?N#0$0001234***" );
      lArgs.add( "aOemSerialNumber", "SN00001234" );
      lResults = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.web.query.inventory.FindDuplicateSerialNoOem", lArgs );

      while ( lResults.next() ) {
         String lTestResult = lResults.getString( "result" );

         if ( !lTestResult.equals( "PASS" ) ) {
            fail( lTestResult );
         }
      }
   }

}
