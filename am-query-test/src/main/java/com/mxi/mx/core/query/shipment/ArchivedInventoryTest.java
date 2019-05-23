
package com.mxi.mx.core.query.shipment;

import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.EventBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;


/**
 * This test ensures that the query com.mxi.mx.core.query.inventory.ArchivedInventory.qrx returns
 * inventory record even if it has no records in evt_inv or evt_event table
 *
 * @author sdevi
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class ArchivedInventoryTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Tests the query
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testArchivedInvnetory() throws Exception {

      PartNoKey lPartNoKey = new PartNoKey( "4650:1772" );

      // create a new inventory with archive condition
      InventoryKey lInventoryKey1 =
            new InventoryBuilder().withClass( RefInvClassKey.TRK ).withPartNo( lPartNoKey )
                  .withSerialNo( "SN000015" ).withCondition( RefInvCondKey.ARCHIVE ).build();

      // run the query
      QuerySet lResults1 = executeQuery( lPartNoKey, "SN000015" );

      // make sure the query returns the record
      while ( lResults1.next() ) {
         String lTestResult = lResults1.getString( "inventory_key" );

         if ( !lTestResult.equals( lInventoryKey1.toString() ) ) {
            fail( lTestResult );
         }
      }

      // create a new inventory with event records
      InventoryKey lInventoryKey2 =
            new InventoryBuilder().withClass( RefInvClassKey.TRK ).withPartNo( lPartNoKey )
                  .withSerialNo( "SN000016" ).withCondition( RefInvCondKey.ARCHIVE ).build();
      new EventBuilder().onInventory( lInventoryKey2 ).build();

      QuerySet lResults2 = executeQuery( lPartNoKey, "SN000016" );

      // make sure the query returns the record
      while ( lResults2.next() ) {
         String lTestResult = lResults2.getString( "inventory_key" );

         if ( !lTestResult.equals( lInventoryKey2.toString() ) ) {
            fail( lTestResult );
         }
      }
   }


   /**
    * Executes the query being tested and returns the results.
    *
    * @param aPartNo
    *           The part no
    * @param aSerialNo
    *           The serial no
    *
    * @return The results of the query
    */
   private QuerySet executeQuery( PartNoKey aPartNo, String aSerialNo ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPartNo, new String[] { "aPartNoDbId", "aPartNoId" } );
      lArgs.add( "aSerialNo", aSerialNo );

      // run the query
      QuerySet lResults = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.core.query.inventory.ArchivedInventory", lArgs );

      return lResults;
   }
}
