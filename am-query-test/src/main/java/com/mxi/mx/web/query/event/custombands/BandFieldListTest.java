
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
public final class BandFieldListTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), BandFieldListTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test band
    *
    * @throws Exception
    *            if an error occurs in execution
    */
   @Test
   public void testBand() throws Exception {
      execute( 1 );
      assertEquals( 1, iDataSet.getRowCount() );
      iDataSet.next();
      testRow( "4650:1111", "4650:1111:222", "4650:1111:222:33", "Free Form Text",
            "Free Form Text Notes", 1, "0:TEXT", "LBS" );
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
    * Method for testing a specific row returned by dataset.
    *
    * @param aBandGroup
    *           BandGroup
    * @param aBand
    *           Band
    * @param aField
    *           Field
    * @param aName
    *           Name
    * @param aDesc
    *           Description
    * @param aOrdSeq
    *           Order Sequence
    * @param aDomain
    *           Domain
    * @param aDataType
    *           Data type
    *
    * @throws Exception
    *            if an error occurs in execution
    */
   private void testRow( String aBandGroup, String aBand, String aField, String aName, String aDesc,
         int aOrdSeq, String aDomain, String aDataType ) throws Exception {

      MxAssert.assertEquals( aBandGroup, iDataSet.getString( "band_group_key" ) );
      MxAssert.assertEquals( aBand, iDataSet.getString( "band_key" ) );
      MxAssert.assertEquals( aField, iDataSet.getString( "field_key" ) );
      MxAssert.assertEquals( aName, iDataSet.getString( "desc_sdesc" ) );
      MxAssert.assertEquals( aDesc, iDataSet.getString( "desc_ldesc" ) );
      MxAssert.assertEquals( aOrdSeq, iDataSet.getInt( "order_seq" ) );
      MxAssert.assertEquals( aDomain, iDataSet.getString( "domain_key" ) );
   }
}
