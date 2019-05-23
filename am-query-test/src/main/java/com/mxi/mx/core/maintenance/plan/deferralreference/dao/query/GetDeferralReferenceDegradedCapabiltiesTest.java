
package com.mxi.mx.core.maintenance.plan.deferralreference.dao.query;

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
 * @author hzheng
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetDeferralReferenceDegradedCapabiltiesTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetDeferralReferenceDegradedCapabiltiesTest.class );
   }


   @Test
   public void getDegradedCapabilitiesByDeferralRefIdTest() {
      String lDeferralRefId = "4B2E16F3ED7F11E6A7768FB850B00E37";

      QuerySet lQs = getDegradedCapabilities( lDeferralRefId );

      Assert.assertEquals( "RowCount", 3, lQs.getRowCount() );

   }


   /**
    *
    * Get degraded capabilities.
    *
    * @param aDeferralRefId
    *           The deferral reference id
    * @return DataSet with query results
    */
   private QuerySet getDegradedCapabilities( String aDeferralRefId ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereEquals( "fail_defer_ref_degrad_cap.fail_defer_ref_id",
            new RawId( aDeferralRefId ).toString() );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

   }

}
