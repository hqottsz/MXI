
package com.mxi.mx.web.query.event;

import static org.junit.Assert.assertEquals;

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
import com.mxi.mx.core.key.AuthorityKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 *
 * @author amar
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class AssemblySerialNoPopUpTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), AssemblySerialNoPopUpTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where assembly inventories are retrieved for an authority.
    *
    * <ol>
    * <li>Query for all assembly inventories.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testAssemblyForAuthority() throws Exception {
      execute( new AuthorityKey( "4650:7777" ) );
      assertEquals( 2, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:1232", "Engine-2", "PART_OEM", "Part_Desc", "locCd" );
      iDataSet.next();
      testRow( "4650:1233", "Engine-1", "PART_OEM", "Part_Desc", "Airbus 320" );
   }


   /**
    * Test the case where assembly inventories are not there for an authority.
    *
    * <ol>
    * <li>Query for all assembly inventories.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testNoAssemblyForAuthority() throws Exception {
      execute( new AuthorityKey( "4650:7778" ) );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Execute the query.
    *
    * @param aAuthority
    *           the authority for which all the assembly inventories are required.
    */
   private void execute( AuthorityKey aAuthority ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aAuthority, new String[] { "aAuthDbId", "aAuthId" } );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aAssemblyKey
    *           the assembly key.
    * @param aSerialNo
    *           the assembly serial no.
    * @param aPartNo
    *           the part no oem.
    * @param aPartDesc
    *           the part desc.
    * @param aLoc
    *           the assembly location
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aAssemblyKey, String aSerialNo, String aPartNo, String aPartDesc,
         String aLoc ) throws Exception {

      MxAssert.assertEquals( aAssemblyKey, iDataSet.getString( "inv_inv_key" ) );
      MxAssert.assertEquals( aSerialNo, iDataSet.getString( "serial_no_oem" ) );
      MxAssert.assertEquals( aPartNo, iDataSet.getString( "part_no_oem" ) );
      MxAssert.assertEquals( aPartDesc, iDataSet.getString( "part_no_sdesc" ) );
      MxAssert.assertEquals( aLoc, iDataSet.getString( "location" ) );
   }
}
