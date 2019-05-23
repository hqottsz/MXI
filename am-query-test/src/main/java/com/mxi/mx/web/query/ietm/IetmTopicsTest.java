
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
 * This class performs unit testing on the IetmSearch query file with the same package and name.
 *
 * @author akash
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class IetmTopicsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), IetmTopicsTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test Search Results of Ietm Search Query for Attachment
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testIetmSrchAttach() throws Exception {
      execute( new IetmDefinitionKey( "4650:2010" ), "A Attachment", true );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:2010:1", "A Attachment", "TXT", "this is a long description of Attachment",
            "testing topic note of Attachment", true, true );
   }


   /**
    * Test Search Results of Ietm Search Query for Technical Reference
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testIetmSrchTechRef() throws Exception {
      execute( new IetmDefinitionKey( "4650:2010" ), "A Tech Ref", false );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:2010:2", "A Tech Ref", "PDF",
            "this is a long description of Technical Reference",
            "testing topic note of Technical Reference", true, false );
   }


   /**
    * Execute the query.
    *
    * @param aIetm
    *           Ietm Definition Key
    * @param aName
    *           Ietm Topic Name
    * @param aIsAttachment
    *           Whether Ietm topic is attachment
    */
   private void execute( IetmDefinitionKey aIetm, String aName, boolean aIsAttachment ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aIetm, new String[] { "aIetmDbId", "aIetmId" } );
      lArgs.add( "aIetmName", aName );
      lArgs.add( "aSearchMethod", "START_WITH" );

      if ( aIsAttachment ) {
         lArgs.addWhere( "ietm_topic.attach_blob IS NOT NULL " );
      } else {
         lArgs.addWhere( "ietm_topic.attach_blob IS NULL " );
      }

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aIetmTopic
    *           Ietm Topic Key
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
   private void testRow( String aIetmTopic, String aName, String aType, String aDesc, String aNote,
         boolean aPrintable, boolean aIsAttachment ) throws Exception {
      MxAssert.assertEquals( aIetmTopic, iDataSet.getString( "ietm_topic_key" ) );
      MxAssert.assertEquals( aName, iDataSet.getString( "topic_sdesc" ) );
      MxAssert.assertEquals( aDesc, iDataSet.getString( "desc_ldesc" ) );
      MxAssert.assertEquals( aNote, iDataSet.getString( "topic_note" ) );
      MxAssert.assertEquals( aPrintable, iDataSet.getBoolean( "print_bool" ) );

      if ( aIsAttachment ) {
         MxAssert.assertEquals( aType, iDataSet.getString( "attach_type_cd" ) );
      } else {
         MxAssert.assertEquals( aType, iDataSet.getString( "ietm_type_cd" ) );
      }
   }
}
