
package com.mxi.mx.web.query.ietm;

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
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.IetmDefinitionKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the IetmTopicSearch query file.
 *
 * @author akash
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class IetmTopicSearchTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), IetmTopicSearchTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test Search Results of Ietm Topic Search Query for Attachment
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testIetmSrchAttach() throws Exception {
      execute( new IetmDefinitionKey( "4650:2011" ), "A Attachment" );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:2011", "ALP (F-2004 Maintenance Manual)", "A Attachment", "TXT",
            "this is a long description of Attachment", "testing topic note of Attachment", true,
            true );
   }


   /**
    * Test Search Results of Ietm Topic Search Query for Technical Reference
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testIetmSrchTechRef() throws Exception {
      execute( new IetmDefinitionKey( "4650:2011" ), "A Tech Ref" );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:2011", "ALP (F-2004 Maintenance Manual)", "A Tech Ref", "PDF",
            "this is a long description of Technical Reference",
            "testing topic note of Technical Reference", true, false );
   }


   /**
    * Execute the query.
    *
    * @param aIetm
    *           Ietm Definition Key
    * @param aName
    *           Topic Name
    */
   private void execute( IetmDefinitionKey aIetm, String aName ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aIetm, new String[] { "aIetmDbId", "aIetmId" } );
      lArgs.add( "aIetmName", aName );
      lArgs.add( "aSearchMethod", "START_WITH" );
      lArgs.add( ( AssemblyKey ) null, "aAssmblDbId", "aAssmblCd" );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aIetm
    *           Ietm Definition Key
    * @param aIetmName
    *           Ietm Name
    * @param aName
    *           Topic Name
    * @param aType
    *           Topic Type
    * @param aDesc
    *           Description
    * @param aNote
    *           Note
    * @param aPrintable
    *           Printable
    * @param aIsAttachment
    *           Whether Ietm Topic is attachment
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aIetm, String aIetmName, String aName, String aType, String aDesc,
         String aNote, boolean aPrintable, boolean aIsAttachment ) throws Exception {
      MxAssert.assertEquals( aIetm, iDataSet.getString( "ietm_ietm_key" ) );
      MxAssert.assertEquals( aIetmName, iDataSet.getString( "ietm_reference" ) );
      MxAssert.assertEquals( aName, iDataSet.getString( "topic_sdesc" ) );
      MxAssert.assertEquals( aType, iDataSet.getString( "topic_type" ) );
      MxAssert.assertEquals( aDesc, iDataSet.getString( "desc_ldesc" ) );
      MxAssert.assertEquals( aNote, iDataSet.getString( "topic_note" ) );
      MxAssert.assertEquals( aPrintable, iDataSet.getBoolean( "print_bool" ) );
   }
}
