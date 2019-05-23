package com.mxi.am.api.query.part;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;


public class GetPartSearchResultsTest {

   @ClassRule
   public static DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   private static final String QUERY_UNDER_TEST = "com.mxi.am.api.query.part.GetPartSearchResults";
   private static final String RESOURCE_FILE = "GetPartSearchResultsTest.xml";


   @BeforeClass
   public static void loadData() {
      XmlLoader.load( databaseConnectionRule.getConnection(), GetApplPartSearchResultsTest.class,
            RESOURCE_FILE );
   }


   @Test
   public void execute_multipleMatches_PartName() {
      DataSetArgument dataSetArgument = new DataSetArgument();
      dataSetArgument.add( "aNumRecs", 10 );
      dataSetArgument.add( "aAssmblCd", "" );
      dataSetArgument.add( "aSearchParams", "TEST%" );

      QuerySet querySet = executeQuery( dataSetArgument );

      assertEquals( 2, querySet.getRowCount() );
   }


   @Test
   public void execute_multipleMatches_PartOEM() {
      DataSetArgument dataSetArgument = new DataSetArgument();
      dataSetArgument.add( "aNumRecs", 10 );
      dataSetArgument.add( "aAssmblCd", "" );
      dataSetArgument.add( "aSearchParams", "PART%" );

      QuerySet querySet = executeQuery( dataSetArgument );

      assertEquals( 2, querySet.getRowCount() );
   }


   @Test
   public void execute_exactMatch_PartName() {
      DataSetArgument dataSetArgument = new DataSetArgument();
      dataSetArgument.add( "aNumRecs", 10 );
      dataSetArgument.add( "aAssmblCd", "737-MAX" );
      dataSetArgument.add( "aSearchParams", "TEST PART 2" );

      QuerySet querySet = executeQuery( dataSetArgument );

      assertEquals( 1, querySet.getRowCount() );

      querySet.first();

      assertEquals( "PART-2", querySet.getString( "part_no" ) );
      assertEquals( "TEST PART 2", querySet.getString( "part_name" ) );
      assertEquals( "COMHW", querySet.getString( "assembly_code" ) );
   }


   @Test
   public void execute_exactMatch_PartOEM() {
      DataSetArgument dataSetArgument = new DataSetArgument();
      dataSetArgument.add( "aNumRecs", 10 );
      dataSetArgument.add( "aAssmblCd", "737-MAX" );
      dataSetArgument.add( "aSearchParams", "PART-1" );

      QuerySet querySet = executeQuery( dataSetArgument );

      assertEquals( 1, querySet.getRowCount() );

      querySet.first();

      assertEquals( "PART-1", querySet.getString( "part_no" ) );
      assertEquals( "TEST PART 1", querySet.getString( "part_name" ) );
      assertEquals( "COMHW", querySet.getString( "assembly_code" ) );
   }


   @Test
   public void execute_numberOfRecords_lessThanMatches() {
      DataSetArgument dataSetArgument = new DataSetArgument();
      dataSetArgument.add( "aNumRecs", 1 );
      dataSetArgument.add( "aAssmblCd", "" );
      dataSetArgument.add( "aSearchParams", "PART%" );

      QuerySet querySet = executeQuery( dataSetArgument );

      assertEquals( 1, querySet.getRowCount() );
   }


   private QuerySet executeQuery( DataSetArgument dataSetArgument ) {
      return QuerySetFactory.getInstance().executeQuery( QUERY_UNDER_TEST, dataSetArgument );
   }
}
