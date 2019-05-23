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


/**
 * The class tests the com.mxi.mx.core.query.req.GetSplitPartRequestByExternalIdAndMasterReqId
 * query.
 *
 * @author hzheng
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetSplitPartRequestByExternalIdAndMasterReqIdTest {

   @ClassRule
   public static DatabaseConnectionRule sConnection = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sConnection.getConnection(),
            GetSplitPartRequestByExternalIdAndMasterReqIdTest.class );
   }


   /**
    * Test Case 1: Tests that the split part request is retrieved correctly
    *
    * <p>
    * Preconditions:
    * <ol>
    * <li>The split part request is already created in database</li>
    * </ol>
    * </p>
    * <p>
    * Expected Results:
    * <ol>
    * <li>the split part request is retrieved</li>
    * </ol>
    * </p>
    *
    */
   @Test
   public void testGetSplitPartRequest() {
      // ARRANGE
      String lExternalId = "R48U00025ML-2";
      String lMasterId = "R48U00025ML";

      // ACT
      QuerySet lQuerySet = executeQuery( lExternalId, lMasterId );

      // ASSERT
      Assert.assertEquals( "Number of retrieved rows", 1, lQuerySet.getRowCount() );

      lQuerySet.next();

      // ensure the value of request_key is as expected
      Assert.assertEquals( "4650:2", lQuerySet.getString( "request_key" ) );

   }


   /**
    * Test Case 2: Tests that no split part request is retrieved due to wrong external id.
    *
    * <p>
    * Preconditions:
    * <ol>
    * <li>The split part request with external reference are already created in database</li>
    * </ol>
    * </p>
    * <p>
    * Expected Results:
    * <ol>
    * <li>no split part request is retrieved</li>
    * </ol>
    * </p>
    *
    */
   @Test
   public void testGetSplitPartRequestWithWrongExternalId() {
      // ARRANGE
      String lExternalId = "R48U00025ML-x";
      String lMasterId = "R48U00025ML";

      // ACT
      QuerySet lQuerySet = executeQuery( lExternalId, lMasterId );

      // ASSERT
      Assert.assertEquals( "Number of retrieved rows", 0, lQuerySet.getRowCount() );

   }


   /**
    * Executes query
    *
    * @param aExternalId
    *           the external reference for a split part request
    * @param aMasterReqId
    *           the master request id
    *
    * @return the QuerySet object
    */
   protected QuerySet executeQuery( String aExternalId, String aMasterReqId ) {
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aExternalId", aExternalId );
      lArgs.add( "aMasterReqId", aMasterReqId );

      return QuerySetFactory.getInstance().executeQuery(
            "com.mxi.mx.core.query.req.GetSplitPartRequestByExternalIdAndMasterReqId", lArgs );

   }
}
