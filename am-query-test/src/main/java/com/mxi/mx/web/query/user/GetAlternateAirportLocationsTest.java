
package com.mxi.mx.web.query.user;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
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
import com.mxi.mx.common.utils.ObjectUtils;


/**
 * This class tests the query com.mxi.mx.web.query.shift.GetAlternateAirportLocations.qrx
 *
 * @author bparekh, yvakulenko
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetAlternateAirportLocationsTest {

   private static final Location ABQ_LINE = new Location();
   private static final Location ATL_HGR1 = new Location();
   private static final Location ATL_HGR1_TRK1 = new Location();
   private static final Location AMS_DOC = new Location();
   private static final Location AMS_SHOP = new Location();

   static {
      ABQ_LINE.iLocationKey = "4650:1000001";
      ABQ_LINE.iLocationCd = "ABQ/LINE";
      ABQ_LINE.iLocationLabel = "ABQ/LINE (ABQ/LINE)";

      ATL_HGR1.iLocationKey = "4650:1001";
      ATL_HGR1.iLocationCd = "ATL/HGR-1";
      ATL_HGR1.iLocationLabel = "ATL/HGR-1 (Hartsfield Hangar 1)";

      ATL_HGR1_TRK1.iLocationKey = "4650:1212";
      ATL_HGR1_TRK1.iLocationCd = "ATL/HGR-1/TRK-1";
      ATL_HGR1_TRK1.iLocationLabel = "ATL/HGR-1/TRK-1 (Track-1)";

      AMS_DOC.iLocationKey = "4650:1000119";
      AMS_DOC.iLocationCd = "AMS/DOCK";
      AMS_DOC.iLocationLabel = "AMS/DOCK (Default Dock)";

      AMS_SHOP.iLocationKey = "4650:1000120";
      AMS_SHOP.iLocationCd = "AMS/SHOP";
      AMS_SHOP.iLocationLabel = "AMS/SHOP (Default Shop)";
   }

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetAlternateAirportLocationsTest.class );
   }


   /**
    * Tests the retrieval of alternate airport locations.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testShiftLabel() throws Exception {
      List<Location> lLocations = this.execute();
      Assert.assertEquals( 4, lLocations.size() );
      Assert.assertTrue( lLocations.contains( ABQ_LINE ) );
      Assert.assertTrue( lLocations.contains( ATL_HGR1 ) );
      Assert.assertTrue( lLocations.contains( ATL_HGR1_TRK1 ) );
      Assert.assertTrue( lLocations.contains( AMS_SHOP ) );
      Assert.assertFalse( lLocations.contains( AMS_DOC ) );
   }


   /**
    * This method executes the query in GetAlternateAirportLocations.qrx
    *
    * @return The dataset after execution.
    */
   private List<Location> execute() {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      DataSet lDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
      List<Location> lRes = new ArrayList<Location>();
      while ( lDataSet.next() ) {
         Location lLocation = new Location();
         lLocation.iLocationKey = lDataSet.getString( "loc_key" );
         lLocation.iLocationCd = lDataSet.getString( "loc_cd" );
         lLocation.iLocationLabel = lDataSet.getString( "location_label" );
         lRes.add( lLocation );
      }
      return lRes;
   }


   private static final class Location {

      String iLocationCd;
      String iLocationLabel;
      String iLocationKey;


      @Override
      public String toString() {
         return iLocationKey + "," + iLocationCd + "," + iLocationLabel;
      }


      @Override
      public int hashCode() {
         int lHashCode = 17;
         lHashCode = ( lHashCode * 37 ) + ObjectUtils.optionalFieldHashCode( this.iLocationKey );
         lHashCode = ( lHashCode * 37 ) + ObjectUtils.optionalFieldHashCode( this.iLocationCd );
         lHashCode = ( lHashCode * 37 ) + ObjectUtils.optionalFieldHashCode( this.iLocationLabel );
         return lHashCode;
      }


      @Override
      public boolean equals( Object aObj ) {
         if ( aObj == this ) {
            return true;
         }

         if ( aObj == null ) {
            return false;
         }

         if ( aObj instanceof Location ) {
            Location lOther = ( Location ) aObj;

            return ObjectUtils.areEqual( this.iLocationKey, lOther.iLocationKey )
                  && ObjectUtils.areEqual( this.iLocationCd, lOther.iLocationCd )
                  && ObjectUtils.areEqual( this.iLocationLabel, lOther.iLocationLabel );
         }

         return false;
      }
   }
}
