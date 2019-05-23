
package com.mxi.mx.web.query.tag;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Test for the GetAllTags Query
 *
 * @author slevert
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetAllTagsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   /**
    * test Get All Tags Query
    *
    * @throws Exception
    */
   @Test
   public void testGetAllTags() throws Exception {

      // execute the query
      DataSet lDs = execute( new DataSetArgument() );

      // There should be 0 row
      MxAssert.assertEquals( "Number of retrieved rows", 3, lDs.getRowCount() );

      lDs.next();

      assertRow( lDs, "4650:1", "Tag 1", "Tag Short Desc #1", "Tag Long Desc #1",
            new SimpleDateFormat( "dd-MMM-yyyy hh:mm" ).parse( "13-FEB-2008 11:26:01" ), "1",
            "user1" );

      lDs.next();

      assertRow( lDs, "4650:2", "Tag 2", "Tag Short Desc #2", "Tag Long Desc #2",
            new SimpleDateFormat( "dd-MMM-yyyy hh:mm" ).parse( "13-FEB-2008 10:40:13" ), "2",
            "user2" );

      lDs.next();

      assertRow( lDs, "4650:3", "Tag 3", "Tag Short Desc #3", "Tag Long Desc #3",
            new SimpleDateFormat( "dd-MMM-yyyy hh:mm" ).parse( "13-FEB-2008 10:39:51" ), "3",
            "user3" );
   }


   /**
    * create the test data
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), GetAllTagsTest.class,
            GetAllTagsData.getDataFile() );
   }


   /**
    * asserts that the dataset row contains the expected data
    *
    * @param aDs
    *           dataset
    * @param aTagKey
    *           tag db id
    * @param aTagCd
    *           tag code
    * @param aTagName
    *           tag name
    * @param aTagDescription
    *           tag description
    * @param aTagDate
    *           tag date
    * @param aUserKey
    *           hr db id
    * @param aUserName
    *           hr id
    */
   private void assertRow( DataSet aDs, String aTagKey, String aTagCd, String aTagName,
         String aTagDescription, Date aTagDate, String aUserKey, String aUserName ) {
      MxAssert.assertEquals( "tag_key", aTagKey, aDs.getString( "tag_key" ) );
      MxAssert.assertEquals( "tag_cd", aTagCd, aDs.getString( "tag_cd" ) );
      MxAssert.assertEquals( "tag_sdesc", aTagName, aDs.getString( "tag_sdesc" ) );
      MxAssert.assertEquals( "tag_ldesc", aTagDescription, aDs.getString( "tag_ldesc" ) );
      MxAssert.assertEquals( "tag_dt", aTagDate, aDs.getDate( "tag_dt" ) );
      MxAssert.assertEquals( "user_key", aUserKey, aDs.getString( "user_key" ) );
      MxAssert.assertEquals( "username", aUserName, aDs.getString( "username" ) );
   }


   /**
    * Execute the query
    *
    * @param aArgs
    *           the argument set
    *
    * @return the result
    */
   private DataSet execute( DataSetArgument aArgs ) {

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), aArgs );
   }
}
