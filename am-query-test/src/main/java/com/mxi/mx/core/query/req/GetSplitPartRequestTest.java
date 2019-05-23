
package com.mxi.mx.core.query.req;

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
import com.mxi.mx.core.key.PartRequestKey;


/**
 * The class tests the com.mxi.mx.core.query.req.GetSplitPartRequest query.
 *
 * @author hzheng
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetSplitPartRequestTest {

   @ClassRule
   public static DatabaseConnectionRule sConnection = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sConnection.getConnection(), GetSplitPartRequestTest.class );
   }


   /**
    * Test Case 1: Tests that the newly split part request is retrieved correctly
    *
    * <p>
    * Preconditions:
    * <ol>
    * <li>Newly split part request is already created in database</li>
    * </ol>
    * </p>
    * <p>
    * Expected Results:
    * <ol>
    * <li>the newly split part request is retrieved</li>
    * </ol>
    * </p>
    *
    * @throws Exception
    *            if anything goes wrong
    */
   @Test
   public void testGetSplitPartRequest() {
      QuerySet lQuerySet = executeQuery( new PartRequestKey( 4650, 1 ) );

      Assert.assertEquals( "Number of retrieved rows", 1, lQuerySet.getRowCount() );

      lQuerySet.next();

      // ensure the value of request_key is as expected
      Assert.assertEquals( "4650:2", lQuerySet.getString( "request_key" ) );

   }


   /**
    * Executes query
    *
    * @param aPartRequestKey
    *           the key for a part request
    *
    * @return the QuerySet object
    */
   protected QuerySet executeQuery( PartRequestKey aPartRequestKey ) {
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( aPartRequestKey, new String[] { "aPartReqDbId", "aPartReqId" } );

      return QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.core.query.req.GetSplitPartRequest", lArgs );

   }
}
