
package com.mxi.mx.core.query.location;

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
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the query com.mxi.mx.core.query.location.SelectLocationForSupplyLocation.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class SelectLocationForSupplyLocationTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            SelectLocationForSupplyLocationTest.class );
   }


   /**
    * Tests the retrieval of all location for supply locations.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testLocationForSupplyLocation() throws Exception {

      int lDbId = 4650;
      int lId = 1004;

      DataSet lDataSet = this.execute( new LocationKey( lDbId, lId ) );

      while ( lDataSet.next() ) {

         MxAssert.assertEquals( "loc_cd", "YVR/LINE", lDataSet.getString( "loc_cd" ) );
         MxAssert.assertEquals( "loc_name", "Line", lDataSet.getString( "loc_name" ) );
      }
   }


   /**
    * This method executes the query in SelectLocationForSupplyLocation.qrx
    *
    * @param aLocationKey
    *           the LocationKey object
    *
    * @return The dataset after execution.
    */
   private DataSet execute( LocationKey aLocationKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();

      String[] lLocationCodes = { "LINE", "TRACK", "SHOP" };

      // Serial Number
      lDataSetArgument.addWhereIn( "inv_loc.loc_type_cd", lLocationCodes );
      lDataSetArgument.add( "aSupplyLocationDbId", aLocationKey.getDbId() );
      lDataSetArgument.add( "aSupplyLocationId", aLocationKey.getId() );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
