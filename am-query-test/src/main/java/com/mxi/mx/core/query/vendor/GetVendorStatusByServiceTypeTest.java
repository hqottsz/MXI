
package com.mxi.mx.core.query.vendor;

import java.util.ArrayList;
import java.util.List;

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
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.RefServiceTypeKey;
import com.mxi.mx.core.key.RefVendorStatusKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This test case is used to test GetVendorStatusByServiceType.qrx
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetVendorStatusByServiceTypeTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetVendorStatusByServiceTypeTest.class );
   }


   /**
    * This test case is used to test the GetVendorStatusByServiceType
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testGetVendorStatusByServiceType() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( new VendorKey( 4650, 10004 ), "aVendorDbId", "aVendorId" );
      lArgs.add( new OrgKey( 0, 1 ), "aOrgDbId", "aOrgId" );

      List<RefServiceTypeKey> aServiceTypes = new ArrayList<RefServiceTypeKey>();
      aServiceTypes.add( new RefServiceTypeKey( 10, "INSPECTION" ) );
      aServiceTypes.add( new RefServiceTypeKey( 10, "MOD" ) );
      lArgs.addWhereIn( new String[] { "SERVICE_TYPE_DB_ID", "SERVICE_TYPE_CD" }, aServiceTypes );

      // execute the query
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Assert for No of Rows
      MxAssert.assertEquals( "Number of retrieved rows", 2, lDs.getRowCount() );

      lDs.next();
      testRow( lDs, new RefServiceTypeKey( "10:INSPECTION" ),
            new RefVendorStatusKey( "0:UNAPPRVD" ) );
      lDs.next();
      testRow( lDs, new RefServiceTypeKey( "10:MOD" ), new RefVendorStatusKey( "0:WARNING" ) );
   }


   /**
    * Tests a row in the dataset
    *
    * @param aDs
    *           the dataset.
    * @param aRefServiceTypeKey
    *           the ref service type key.
    * @param aVendorStatusKey
    *           the vendor status key.
    */
   private void testRow( DataSet aDs, RefServiceTypeKey aRefServiceTypeKey,
         RefVendorStatusKey aVendorStatusKey ) {

      // Check for the service type key
      MxAssert.assertEquals( "service_type_key", aRefServiceTypeKey,
            aDs.getKey( RefServiceTypeKey.class, "SERVICE_TYPE_DB_ID", "SERVICE_TYPE_CD" ) );

      // Check for the vendor status cd
      MxAssert.assertEquals( "vendor_status_key", aVendorStatusKey,
            aDs.getKey( RefVendorStatusKey.class, "VENDOR_STATUS_DB_ID", "VENDOR_STATUS_CD" ) );
   }
}
