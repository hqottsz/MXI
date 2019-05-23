
package com.mxi.am.api.query.maintenance.eng.prog.assembly.capability;

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
public final class GetAssemblyCapabilitiesTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetAssemblyCapabilitiesTest.class );
   }


   @Test
   public void getAssemblyCapabilitiesByAssemblyIdTest() {
      String lAssemblyId = "5D732E2E0D41470E816E7ABF14C8AF38";

      QuerySet lQs = getAssemblyCapabilities( lAssemblyId );

      Assert.assertEquals( "RowCount", 7, lQs.getRowCount() );

   }


   /**
    *
    * Get assembly capabilities.
    *
    * @param aAssemblyId
    *           The assembly id
    * @return DataSet with query results
    */
   private QuerySet getAssemblyCapabilities( String aAssemblyId ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereEquals( "eqp_assmbl.alt_id", new RawId( aAssemblyId ).toString() );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

   }

}
