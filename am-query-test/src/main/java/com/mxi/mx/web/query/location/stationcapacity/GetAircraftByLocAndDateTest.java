
package com.mxi.mx.web.query.location.stationcapacity;

import java.util.Date;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;


/**
 * Tests the query com.mxi.mx.web.query.location.stationcapacity.GetAircraftByLocAndDateTest
 *
 * @author jcimino
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetAircraftByLocAndDateTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   /**
    * Tests the query
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQuery() throws Exception {

      // execute the query
      execute( null, null, null );
   }


   /**
    * create the test data
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), GetAircraftByLocAndDateTest.class,
            new StationCapacityData().getDataFile() );
   }


   /**
    * Execute the query.
    *
    * @param aLocation
    *           location
    * @param aDate
    *           date
    * @param aHumanResource
    *           the hr
    *
    * @return the result
    */
   private DataSet execute( LocationKey aLocation, Date aDate, HumanResourceKey aHumanResource ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aLocation, new String[] { "aLocDbId", "aLocId" } );
      lArgs.add( "aDate", aDate );
      lArgs.add( aHumanResource, new String[] { "aHrDbId", "aHrId" } );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
