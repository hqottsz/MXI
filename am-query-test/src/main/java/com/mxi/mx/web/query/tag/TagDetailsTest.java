
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
 * Test for the TagDetails Query
 *
 * @author okamenskova
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class TagDetailsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   /**
    * test TagDetails Query
    *
    * @throws Exception
    */
   @Test
   public void testTagDetails() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aTagTagDbId", 4650 );
      lArgs.add( "aTagTagId", 2 );

      // execute the query
      DataSet lDs = execute( lArgs );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );

      lDs.next();

      assertRow( lDs, "Tag 2", "Tag Short Desc #2", "Tag Long Desc #2",
            new SimpleDateFormat( "dd-MMM-yyyy hh:mm" ).parse( "13-FEB-2008 10:40:13" ), "2",
            "last2, first2" );
   }


   /**
    * create the test data
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), TagDetailsTest.class,
            new GetAllTagsData().getDataFile() );
   }


   /**
    * asserts that the dataset row contains the expected data
    *
    * @param aDs
    *           dataset
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
   private void assertRow( DataSet aDs, String aTagCd, String aTagName, String aTagDescription,
         Date aTagDate, String aUserKey, String aUserName ) {
      MxAssert.assertEquals( "tag_cd", aTagCd, aDs.getString( "tag_cd" ) );
      MxAssert.assertEquals( "tag_sdesc", aTagName, aDs.getString( "tag_sdesc" ) );
      MxAssert.assertEquals( "tag_ldesc", aTagDescription, aDs.getString( "tag_ldesc" ) );
      MxAssert.assertEquals( "tag_dt", aTagDate, aDs.getDate( "tag_dt" ) );
      MxAssert.assertEquals( "user_key", aUserKey, aDs.getString( "user_key" ) );
      MxAssert.assertEquals( "full_name", aUserName, aDs.getString( "full_name" ) );
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
