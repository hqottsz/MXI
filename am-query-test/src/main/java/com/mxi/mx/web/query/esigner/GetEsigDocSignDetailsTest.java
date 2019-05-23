
package com.mxi.mx.web.query.esigner;

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
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class runs a unit test on the GetEsigDocSignDetails query file.
 *
 * @author krangaswamy
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetEsigDocSignDetailsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetEsigDocSignDetailsTest.class );
   }


   /** The ESIG_DOC_SIGN_KEY */
   private static final String ESIG_DOC_SIGN_KEY = "4650:100027";

   /** The ESIG_DOC_KEY */
   private static final String ESIG_DOC_KEY = "4650:100027";

   /** The HR_KEY */
   private static final String HR_KEY = "4650:6000134";

   /** The HR_CERT_KEY */
   private static final String HR_CERT_KEY = "4650:6000134:100003";

   /** The BLOB_INFO_KEY */
   private static final String BLOB_INFO_KEY = "4650:100059";

   /** The SIG_DT */
   private static final String SIG_DT = "10-Jul-2009 03:31:12";

   /** The USERNAME */
   private static final String USERNAME = "krangaswamy";


   /**
    * Execute GetEsigDocSignDetailsTest.qrx
    */
   @Test
   public void testGetEsigDocSignDetails() {

      DataSetArgument lArgs = new DataSetArgument();

      // Parameters required by the query.
      lArgs.add( "aSignDbId", 4650 );
      lArgs.add( "aSignId", 100027 );

      // Execute query!
      DataSet lResult = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Get the first row.
      lResult.next();

      // Verify the contents of the row returned.
      testRow( lResult );
   }


   /**
    * Verify the contents of the row returned.
    *
    * @param aResult
    *           the dataset
    */
   private void testRow( DataSet aResult ) {

      // Determine if the following are returned :
      // esig_doc_sign.sign_db_id || ':' || esig_doc_sign.sign_id
      MxAssert.assertEquals( ESIG_DOC_SIGN_KEY, aResult.getString( "esig_doc_sign_key" ) );

      // esig_doc_sign.doc_db_id || ':' ||esig_doc_sign.doc_id
      MxAssert.assertEquals( ESIG_DOC_KEY, aResult.getString( "esig_doc_key" ) );

      // esig_doc_sign.hr_db_id || ':' || esig_doc_sign.hr_id
      MxAssert.assertEquals( HR_KEY, aResult.getString( "hr_key" ) );

      // esig_doc_sign.hr_db_id || ':' || esig_doc_sign.hr_id ':' || esig_doc_sign.cert_id
      MxAssert.assertEquals( HR_CERT_KEY, aResult.getString( "hr_cert_key" ) );

      // cor_blob_info.blob_db_id || ':' || cor_blob_info.blob_id
      MxAssert.assertEquals( BLOB_INFO_KEY, aResult.getString( "blob_info_key" ) );

      // esig_doc_sign.sig_dt
      MxAssert.assertEquals( SIG_DT, aResult.getString( "sig_dt" ) );

      // utl_user.username
      MxAssert.assertEquals( USERNAME, aResult.getString( "username" ) );

      // Determine if one row is returned.
      MxAssert.assertEquals( "Number of retrieved rows", 1, aResult.getRowCount() );
   }
}
