
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
public final class BandListTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), BandListTest.class );
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
   public void testBand() throws Exception {
      execute( 1 );
      assertEquals( 1, iDataSet.getRowCount() );
      iDataSet.next();
      testRow( "4650:1111", "4650:1111:222", "First Band", 1, false );
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception
    *            DOCUMENT ME!
    */
   @Test
   public void testBandNoViewPermission() throws Exception {
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
    * @param aBand
    *           DOCUMENT ME!
    * @param aName
    *           DOCUMENT ME!
    * @param aOrdSeq
    *           DOCUMENT ME!
    * @param aEditable
    *           DOCUMENT ME!
    *
    * @throws Exception
    *            DOCUMENT ME!
    */
   private void testRow( String aBandGroup, String aBand, String aName, int aOrdSeq,
         boolean aEditable ) throws Exception {

      MxAssert.assertEquals( aBandGroup, iDataSet.getString( "band_group_key" ) );
      MxAssert.assertEquals( aBand, iDataSet.getString( "band_key" ) );
      MxAssert.assertEquals( aName, iDataSet.getString( "desc_sdesc" ) );
      MxAssert.assertEquals( aOrdSeq, iDataSet.getInt( "order_seq" ) );
      MxAssert.assertEquals( aEditable, iDataSet.getBoolean( "edit_bool" ) );
   }
}
