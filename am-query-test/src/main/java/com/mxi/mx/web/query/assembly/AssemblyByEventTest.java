
package com.mxi.mx.web.query.assembly;

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
 * This class runs a unit test on the AssemblyByEvent query file.
 *
 * @author srengasamy
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class AssemblyByEventTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), AssemblyByEventTest.class );
   }


   /**
    * Execute AssemblyByEvent.qrx
    */
   @Test
   public void testAssemblyByEvent() {

      DataSetArgument lArgs = new DataSetArgument();

      // Parameters required by the query.
      lArgs.add( "aCheckDbId", 4650 );
      lArgs.add( "aCheckId", 137030 );

      // Execute query!
      DataSet lResult = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Verify the contents of the row returned.
      lResult.next();
      testRow( lResult, "0", "ACFT", "4650", "370873", "4650:370873", "5000000", "A320" );
   }


   /**
    * Verify the contents of the row returned.
    *
    * @param aResult
    *           the dataset
    * @param aAssmbClassDbId
    *           the assembly class db id
    * @param aAssmblClassCd
    *           the assembly class cd
    * @param aInvNoDbId
    *           inv no db id
    * @param aInvNoId
    *           the inv no id
    * @param aInvKey
    *           the inv key
    * @param aOrigAssmblDbId
    *           the Original Assembly Db Id
    * @param aOrigAssmblId
    *           the Original Assembly Id
    */
   private void testRow( DataSet aResult, String aAssmbClassDbId, String aAssmblClassCd,
         String aInvNoDbId, String aInvNoId, String aInvKey, String aOrigAssmblDbId,
         String aOrigAssmblId ) {

      // Determine if the following are returned :
      MxAssert.assertEquals( "Number of retrieved rows", 1, aResult.getRowCount() );

      // eqp_assmbl.assmbl_class_db_id
      MxAssert.assertEquals( aAssmbClassDbId, aResult.getString( "assmbl_class_db_id" ) );

      // eqp_assmbl.assmbl_class_cd
      MxAssert.assertEquals( aAssmblClassCd, aResult.getString( "assmbl_class_cd" ) );

      // inv_inv.inv_no_db_id
      MxAssert.assertEquals( aInvNoDbId, aResult.getString( "inv_no_db_id" ) );

      // inv_inv.inv_no_id
      MxAssert.assertEquals( aInvNoId, aResult.getString( "inv_no_id" ) );

      // inv_inv.inv_no_db_id || ':' || inv_inv.inv_no_id
      MxAssert.assertEquals( aInvKey, aResult.getString( "inv_key" ) );

      // inv_inv.orig_assmbl_db_id
      MxAssert.assertEquals( aOrigAssmblDbId, aResult.getString( "orig_assmbl_db_id" ) );

      // inv_inv.orig_assmbl_cd
      MxAssert.assertEquals( aOrigAssmblId, aResult.getString( "orig_assmbl_cd" ) );
   }
}
