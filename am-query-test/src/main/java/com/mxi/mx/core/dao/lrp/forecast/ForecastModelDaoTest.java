
package com.mxi.mx.core.dao.lrp.forecast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.mxi.am.db.connection.loader.SqlLoader;
import com.mxi.mx.core.dao.lrp.DaoTestCase;
import com.mxi.mx.core.dao.lrp.forecast.model.ForecastModelDao;
import com.mxi.mx.model.key.IdKey;
import com.mxi.mx.model.key.IdKeyImpl;
import com.mxi.mx.model.lrp.ForecastModel;
import com.mxi.mx.model.lrp.ForecastRange;
import com.mxi.mx.model.lrp.ForecastRate;


/**
 * Forecast Model Dao Tests to ensure we can retrieve data using the various API methods.
 *
 * @author slevert
 */
public class ForecastModelDaoTest extends DaoTestCase {

   private ForecastModelDao iDao;


   @Override
   @Before
   public void loadData() throws Exception {
      iDao = iFactory.getForecastModelDao();
   }


   /**
    * Gets All Forecast Models.<br>
    *
    * @throws Exception
    *            if an unexpected error occurs
    */
   @Test
   public void testGetAllForecastModels() throws Exception {
      SqlLoader.load( iDatabaseConnectionRule.getConnection(), getClass(),
            "CreateLargeSetOfForecastModels.sql" );

      Collection<ForecastModel> lModels = iDao.getForecastModels();

      assertNotNull( "Models is null", lModels );
      assertEquals( 144, lModels.size() );

      // make sure every model has a range
      for ( ForecastModel lModel : lModels ) {
         assertNotNull( "Model is null", lModel );

         List<ForecastRange> lRanges = lModel.getRange();
         assertNotNull( "Ranges is null", lRanges );
         assertEquals( true, lRanges.size() > 0 );
         for ( ForecastRange lRange : lRanges ) {
            assertNotNull( "Range is null", lRange );

            List<ForecastRate> lRates = lRange.getRate();
            assertNotNull( "Rates is null", lRates );

            // make sure every range has at least 1 rate
            assertEquals( true, lRates.size() > 0 );
         }
      }
   }


   /**
    * Gets a Forecast Model using a key.<br>
    *
    * @throws Exception
    *            if an unexpected error occurs
    */
   @Test
   public void testGetForecastModelUsingKey() throws Exception {
      SqlLoader.load( iDatabaseConnectionRule.getConnection(), getClass(),
            "CreateForecastModels.sql" );

      IdKey lKey = new IdKeyImpl( 4650, 100051 );
      ForecastModel lModel = iDao.getForecastModel( lKey );

      assertNotNull( lModel );
      assertEquals( lKey, lModel.getActualRefKey() );

      // make sure every model has at least 1 range
      List<ForecastRange> lRanges = lModel.getRange();
      assertNotNull( lRanges );
      assertEquals( 1, lRanges.size() );
      for ( ForecastRange lRange : lRanges ) {
         List<ForecastRate> lRates = lRange.getRate();
         assertNotNull( lRates );

         // make sure every range has at least 1 rate
         assertEquals( 4, lRates.size() );
      }
   }


   /**
    * Gets Forecast Models using a collection of keys.<br>
    *
    * @throws Exception
    *            if an unexpected error occurs
    */
   @Test
   public void testGetForecastModelUsingKeys() throws Exception {
      SqlLoader.load( iDatabaseConnectionRule.getConnection(), getClass(),
            "CreateForecastModels.sql" );

      List<IdKey> lKeys = new ArrayList<IdKey>();
      lKeys.add( new IdKeyImpl( 4650, 100051 ) );
      lKeys.add( new IdKeyImpl( 4650, 100052 ) );

      Collection<ForecastModel> lModels = iDao.getForecastModels( lKeys );

      assertEquals( 2, lModels.size() );

      // make sure every model has at least 1 range
      for ( ForecastModel lModel : lModels ) {
         assertTrue( "Model Key " + lModel.getActualRefKey() + " not found in results.",
               lKeys.contains( lModel.getActualRefKey() ) );

         List<ForecastRange> lRanges = lModel.getRange();
         assertNotNull( lRanges );
         assertEquals( 1, lRanges.size() );
         for ( ForecastRange lRange : lRanges ) {
            List<ForecastRate> lRates = lRange.getRate();
            assertNotNull( lRates );

            // make sure every range has at least 1 rate
            assertEquals( 4, lRates.size() );
         }
      }
   }
}
