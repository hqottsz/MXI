package com.mxi.am.api.query.part;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.Ignore;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;


public class GetApplPartSearchResultsTest {

   @ClassRule
   public static DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   private static final String QUERY_UNDER_TEST =
         "com.mxi.am.api.query.part.GetApplPartSearchResults";
   private static final String RESOURCE_FILE = "GetApplPartSearchResultsTest.xml";


   @BeforeClass
   public static void loadData() {
      XmlLoader.load( databaseConnectionRule.getConnection(), GetApplPartSearchResultsTest.class,
            RESOURCE_FILE );
   }


   @Test
   public void execute_multipleMatches_PartName() {
      DataSetArgument dataSetArgument = new DataSetArgument();
      dataSetArgument.add( "aNumRecs", 10 );
      dataSetArgument.add( "aAssmblCd", "ASSMBL1" );
      dataSetArgument.add( "aSearchParams", "PART FOR TESTING%" );
      dataSetArgument.add( "aAssemblyInvNoDbId", 4650 );
      dataSetArgument.add( "aAssemblyInvNoId", 9999 );

      QuerySet querySet = executeQuery( dataSetArgument );

      assertEquals( 3, querySet.getRowCount() );
   }


   @Test
   public void execute_multipleMatches_PartOEM() {
      DataSetArgument dataSetArgument = new DataSetArgument();
      dataSetArgument.add( "aNumRecs", 10 );
      dataSetArgument.add( "aAssmblCd", "ASSMBL1" );
      dataSetArgument.add( "aSearchParams", "PART-%" );
      dataSetArgument.add( "aAssemblyInvNoDbId", 4650 );
      dataSetArgument.add( "aAssemblyInvNoId", 9999 );

      QuerySet querySet = executeQuery( dataSetArgument );

      assertEquals( 3, querySet.getRowCount() );
   }


   @Test
   public void execute_numberOfRecords_lessThanMatches() {
      DataSetArgument dataSetArgument = new DataSetArgument();
      dataSetArgument.add( "aNumRecs", 1 );
      dataSetArgument.add( "aAssmblCd", "ASSMBL1" );
      dataSetArgument.add( "aSearchParams", "PART FOR TESTING 1" );
      dataSetArgument.add( "aAssemblyInvNoDbId", 4650 );
      dataSetArgument.add( "aAssemblyInvNoId", 9999 );

      QuerySet querySet = executeQuery( dataSetArgument );

      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void execute_exactMatch_isApplicable() {
      DataSetArgument dataSetArgument = new DataSetArgument();
      dataSetArgument.add( "aNumRecs", 10 );
      dataSetArgument.add( "aAssmblCd", "ASSMBL2" );
      dataSetArgument.add( "aSearchParams", "PART FOR TESTING 3" );
      dataSetArgument.add( "aAssemblyInvNoDbId", 4650 );
      dataSetArgument.add( "aAssemblyInvNoId", 9999 );

      QuerySet querySet = executeQuery( dataSetArgument );

      assertEquals( 1, querySet.getRowCount() );

      querySet.first();

      assertEquals( "PART-3", querySet.getString( "part_no" ) );
      assertEquals( "PART FOR TESTING 3", querySet.getString( "part_name" ) );
      assertEquals( "ASSMBL2", querySet.getString( "assembly_code" ) );
   }


   @Test
   public void execute_exactMatch_isApplicable_assemblyCodeNull() {
      DataSetArgument dataSetArgument = new DataSetArgument();
      dataSetArgument.add( "aNumRecs", 10 );
      dataSetArgument.add( "aAssmblCd", "" );
      dataSetArgument.add( "aSearchParams", "PART FOR TESTING 2" );
      dataSetArgument.add( "aAssemblyInvNoDbId", 4650 );
      dataSetArgument.add( "aAssemblyInvNoId", 9999 );

      QuerySet querySet = executeQuery( dataSetArgument );

      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void execute_inexistentPartOEM() {
      DataSetArgument dataSetArgument = new DataSetArgument();
      dataSetArgument.add( "aNumRecs", 10 );
      dataSetArgument.add( "aAssmblCd", "ASSMBL1" );
      dataSetArgument.add( "aSearchParams", "INEXISTENT PART OEM" );
      dataSetArgument.add( "aAssemblyInvNoDbId", 4650 );
      dataSetArgument.add( "aAssemblyInvNoId", 1000 );

      QuerySet querySet = executeQuery( dataSetArgument );

      assertTrue( querySet.isEmpty() );
   }


   @Test
   public void execute_inexistentPartDescription() {
      DataSetArgument dataSetArgument = new DataSetArgument();
      dataSetArgument.add( "aNumRecs", 10 );
      dataSetArgument.add( "aAssmblCd", "ASSMBL1" );
      dataSetArgument.add( "aSearchParams", "INEXISTENT PART DESCRIPTION" );
      dataSetArgument.add( "aAssemblyInvNoDbId", 4650 );
      dataSetArgument.add( "aAssemblyInvNoId", 1000 );

      QuerySet querySet = executeQuery( dataSetArgument );

      assertTrue( querySet.isEmpty() );
   }

   @Test
   public void execute_isNotApplicable() {
      DataSetArgument dataSetArgument = new DataSetArgument();
      dataSetArgument.add( "aNumRecs", 10 );
      dataSetArgument.add( "aAssmblCd", "ASSMBL3" );
      dataSetArgument.add( "aSearchParams", "PART FOR TESTING 4" );
      dataSetArgument.add( "aAssemblyInvNoDbId", 4650 );
      dataSetArgument.add( "aAssemblyInvNoId", 1004 );

      QuerySet querySet = executeQuery( dataSetArgument );

      assertTrue( querySet.isEmpty() );
   }


   private QuerySet executeQuery( DataSetArgument dataSetArgument ) {
      return QuerySetFactory.getInstance().executeQuery( QUERY_UNDER_TEST, dataSetArgument );
   }
}
