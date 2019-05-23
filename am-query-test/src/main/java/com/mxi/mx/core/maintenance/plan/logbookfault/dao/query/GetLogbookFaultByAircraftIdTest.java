
package com.mxi.mx.core.maintenance.plan.logbookfault.dao.query;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;


/**
 * Test GetLogbookFaultByAircraftId.qrx returns the appropriate data in JIRA issue OPER-4354
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetLogbookFaultByAircraftIdTest {

   private String iUUID_LOOSE_INV = "9D1A7216-DA90-11E5-87B1-FB2D7B2472DF";
   private String iUUID_ATTACHED_INV = "8D1A7216-DA90-11E5-87B1-FB2D7B2478DF";

   @ClassRule
   public static DatabaseConnectionRule sConnection = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sConnection.getConnection(), GetLogbookFaultByAircraftIdTest.class );
   }


   /**
    * Retrieves appropriate fault with loose inventory
    */
   @Test
   public void testGetLogbookFaultWithLooseInventory() {
      QuerySet lQuerySet = executeQuery( java.util.UUID.fromString( iUUID_LOOSE_INV ) );

      Assert.assertEquals( "Number of retrieved rows", 1, lQuerySet.getRowCount() );

      lQuerySet.next();

      // ensure the value of inv_loose_bool is as expected
      Assert.assertEquals( 1, lQuerySet.getInt( "inv_loose_bool" ) );

   }


   /**
    * Retrieves appropriate fault with attached inventory
    */
   @Test
   public void testGetLogbookFaultWithAttachedInventory() {
      QuerySet lQuerySet = executeQuery( java.util.UUID.fromString( iUUID_ATTACHED_INV ) );

      Assert.assertEquals( "Number of retrieved rows", 1, lQuerySet.getRowCount() );

      lQuerySet.next();

      // ensure the value of inv_loose_bool is as expected
      Assert.assertEquals( 0, lQuerySet.getInt( "inv_loose_bool" ) );

   }


   /**
    * Executes query
    *
    * @param aUuid
    *           the UUID for aircraft id
    *
    * @return the QuerySet object
    */
   protected QuerySet executeQuery( java.util.UUID aUuid ) {
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aAircraftId", aUuid );

      return QuerySetFactory.getInstance().executeQuery(
            "com.mxi.mx.core.maintenance.plan.logbookfault.dao.query.GetLogbookFaultByAircraftId",
            lArgs );

   }
}
