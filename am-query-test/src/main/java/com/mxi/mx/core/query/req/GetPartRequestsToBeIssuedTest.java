
package com.mxi.mx.core.query.req;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

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
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.PartRequestKey;


/**
 * The class tests the com.mxi.mx.core.query.req.getPartRequestsToBeIssued query.
 *
 * @author dsewell
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetPartRequestsToBeIssuedTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetPartRequestsToBeIssuedTest.class );
   }


   private static final PartRequestKey PART_REQUEST_INCLUDED = new PartRequestKey( 4650, 1000 );

   private static final PartRequestKey PART_REQUEST_NOT_NEEDED = new PartRequestKey( 4650, 1001 );


   /**
    * Tests that the query only returns one of the part requests defined in the data XML file.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testQuery() throws Exception {
      // note that the default value for the PRINT_TICKET_INTERVAL parm is 24 hours

      // update all the part requests to be needed right now
      DataSetArgument lSetArgs = new DataSetArgument();
      lSetArgs.add( "req_by_dt", new Date() );

      DataSetArgument lWhereArgs = new DataSetArgument();
      lWhereArgs.add( "rstat_cd", 0 );

      MxDataAccess.getInstance().executeUpdate( "req_part", lSetArgs, lWhereArgs );

      // update the one part request that isn't needed until later to a later date
      Calendar lCalendar = Calendar.getInstance();
      lCalendar.add( Calendar.DATE, 2 );

      lSetArgs.add( "req_by_dt", lCalendar.getTime() );

      lWhereArgs = PART_REQUEST_NOT_NEEDED.getPKWhereArg();

      MxDataAccess.getInstance().executeUpdate( "req_part", lSetArgs, lWhereArgs );

      // execute the query
      DataSet lResult = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.req.getPartRequestsToBeIssued" );

      assertEquals( 1, lResult.getRowCount() );

      lResult.next();
      assertEquals( PART_REQUEST_INCLUDED,
            lResult.getKey( PartRequestKey.class, "req_part_db_id", "req_part_id" ) );
   }
}
