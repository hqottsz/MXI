
package com.mxi.mx.report.query.inventory;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.core.key.InventoryKey;


/**
 * This class tests the query KitAssemblyTicket.qrx
 *
 * @author srengasamy
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class KitAssemblyTicketTest {

   @ClassRule
   public static final DatabaseConnectionRule sDatabaseConnectionRule =
         new DatabaseConnectionRule();


   @BeforeClass
   public static void setUp() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), KitAssemblyTicketTest.class );
   }


   /**
    * Tests the retrieval of the Kit Assembly Ticket.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testKitAssemblyTicket() throws Exception {

      int lInvNoDbId = 4650;
      int lInvNoId = 391148;

      QuerySet lQuerySet = this.execute( new InventoryKey( lInvNoDbId, lInvNoId ) );

      Assert.assertEquals( "Number of retrieved rows", 1, lQuerySet.getRowCount() );

      // user update status
      Assert.assertTrue( lQuerySet.next() );
      testRow( lQuerySet, "KITPART1(KITUNITTESTPART)", "XXX", "I000T96G", 1 );

      Assert.assertFalse( lQuerySet.next() );
   }


   /**
    * This method executes the query in KitAssemblyTicket.qrx
    *
    * @param aInventoryKey
    *           The inventory key.
    *
    * @return The QuerySet after execution.
    */
   private QuerySet execute( InventoryKey aInventoryKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aInvNoDbId", aInventoryKey.getDbId() );
      lDataSetArgument.add( "aInvNoId", aInventoryKey.getId() );

      return QueryExecutor.executeQuery( getClass(), lDataSetArgument );
   }


   /**
    * Assert the Row Coloumns.
    *
    * @param aQuerySet
    *           the QuerySet
    * @param aKitPartNumber
    *           the KitPartNumber
    * @param aKitSerialNumber
    *           the KitSerialNumber
    * @param aInventoryBarCode
    *           the InventoryBarCode
    * @param aProcurable
    *           the Procurable
    */
   private void testRow( QuerySet aQuerySet, String aKitPartNumber, String aKitSerialNumber,
         String aInventoryBarCode, int aProcurable ) {
      Assert.assertEquals( "kit_part_number", aKitPartNumber,
            aQuerySet.getString( "kit_part_number" ) );
      Assert.assertEquals( "kit_serial_number", aKitSerialNumber,
            aQuerySet.getString( "kit_serial_number" ) );

      Assert.assertEquals( "inventory_bar_code", aInventoryBarCode,
            aQuerySet.getString( "inventory_bar_code" ) );
      Assert.assertEquals( "procurable", aProcurable, aQuerySet.getInt( "procurable" ) );
   }
}
