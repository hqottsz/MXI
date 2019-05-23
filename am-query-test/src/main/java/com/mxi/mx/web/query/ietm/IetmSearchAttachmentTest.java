
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
import com.mxi.mx.core.key.IetmDefinitionKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the IetmSearchAttachment query file.
 *
 * @author akash
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class IetmSearchAttachmentTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), IetmSearchAttachmentTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test Search Results of Ietm Search By Attachment Query
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testIetmSrchByAttach() throws Exception {
      execute( new IetmDefinitionKey( "4650:2009" ), "A Attachment" );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:2009", "ANP (F-2002 Maintenance Manual)", "A Attachment", "TXT",
            "this is a long description", "testing topic note", true );
   }


   /**
    * Execute the query.
    *
    * @param aIetm
    *           Ietm Definition Key
    * @param aName
    *           Attachment Name
    */
   private void execute( IetmDefinitionKey aIetm, String aName ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aIetm, new String[] { "aIetmDbId", "aIetmId" } );
      lArgs.add( "aIetmName", aName );
      lArgs.add( "aType", ( String ) null );
      lArgs.add( "aRowNum", 100 );

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
    *           Attachment Name
    * @param aType
    *           Attachment Type
    * @param aDesc
    *           Description
    * @param aNote
    *           Note
    * @param aPrintable
    *           Printable
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aIetm, String aIetmName, String aName, String aType, String aDesc,
         String aNote, boolean aPrintable ) throws Exception {
      MxAssert.assertEquals( aIetm, iDataSet.getString( "ietm_ietm_key" ) );
      MxAssert.assertEquals( aIetmName, iDataSet.getString( "ietm_reference" ) );
      MxAssert.assertEquals( aName, iDataSet.getString( "topic_sdesc" ) );
      MxAssert.assertEquals( aType, iDataSet.getString( "topic_type" ) );
      MxAssert.assertEquals( aDesc, iDataSet.getString( "desc_ldesc" ) );
      MxAssert.assertEquals( aNote, iDataSet.getString( "topic_note" ) );
      MxAssert.assertEquals( aPrintable, iDataSet.getBoolean( "print_bool" ) );
   }
}
