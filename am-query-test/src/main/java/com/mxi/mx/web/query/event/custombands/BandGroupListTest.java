
package com.mxi.mx.web.query.event.custombands;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 *
 * @author amar
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class BandGroupListTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), BandGroupListTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * DOCUMENT ME!
    *
    * @throws Exception
    *            DOCUMENT ME!
    */
   @Test
   public void testBandGroup() throws Exception {
      execute( 1 );
      assertEquals( 1, iDataSet.getRowCount() );
      iDataSet.next();
      testRow( "4650:1111", "First Band Group", 1 );
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception
    *            DOCUMENT ME!
    */
   @Test
   public void testBandGroupNoViewPermission() throws Exception {
      execute( 2 );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Execute the query.
    *
    * @param aUserId
    *           the user id.
    */
   private void execute( int aUserId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aUserId", aUserId );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * DOCUMENT ME!
    *
    * @param aBandGroup
    *           DOCUMENT ME!
    * @param aName
    *           DOCUMENT ME!
    * @param aOrdSeq
    *           DOCUMENT ME!
    *
    * @throws Exception
    *            DOCUMENT ME!
    */
   private void testRow( String aBandGroup, String aName, int aOrdSeq ) throws Exception {

      MxAssert.assertEquals( aBandGroup, iDataSet.getString( "band_group_key" ) );
      MxAssert.assertEquals( aName, iDataSet.getString( "desc_sdesc" ) );
      MxAssert.assertEquals( aOrdSeq, iDataSet.getInt( "order_seq" ) );
   }
}
