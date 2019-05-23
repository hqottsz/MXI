
package com.mxi.am.api.query.maintenance.plan.flight;

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
import com.mxi.mx.core.api.resource.model.RawId;


/**
 * Tests the {@link GetFlightCapabilities} query.
 *
 * @author hzheng
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetFlightCapabilitiesTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetFlightCapabilitiesTest.class );
   }


   @Test
   public void getFlightCapabilitiesByFlightLegIdTest() {
      String lFlightLegId = "9225D541F9F711E688FDC4346B7E1B4B";

      QuerySet lQs = getFlightCapabilities( lFlightLegId );

      Assert.assertEquals( "RowCount", 3, lQs.getRowCount() );

   }


   /**
    *
    * Get flight capabilities.
    *
    * @param aFlightLegId
    *           The flight leg unique identify
    * @return DataSet with query results
    */
   private QuerySet getFlightCapabilities( String aFlightLegId ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereEquals( "fl_requirement.fl_leg_id", new RawId( aFlightLegId ).toString() );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

   }

}
