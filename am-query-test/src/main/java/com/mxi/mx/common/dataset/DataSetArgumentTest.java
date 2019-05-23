
package com.mxi.mx.common.dataset;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument.UnstructuredWhereClause;


@RunWith( BlockJUnit4ClassRunner.class )
public class DataSetArgumentTest {

   @ClassRule
   public static final DatabaseConnectionRule sDatabaseConnectionRule =
         new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), DataSetArgument.class );
   }


   @Test
   public void testDataSetArgumentsAddWhereUnstructured() {

      /* simplest test case using varchar column */
      UnstructuredWhereClause lClause = UnstructuredWhereClause.startSql( "first_name = " );
      lClause.appendValue( "Rob" );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereUnstructured( lClause );
      Set<Integer> lTypeIds = executeQuery( lArgs );
      Assert.assertEquals( 1, lTypeIds.size() );
      Assert.assertTrue( lTypeIds.contains( 1000 ) );

      /* simplest test case using long column */
      lClause = UnstructuredWhereClause.startSql( "user_id = " );
      lClause.appendValue( 1000l );

      lArgs = new DataSetArgument();
      lArgs.addWhereUnstructured( lClause );
      lTypeIds = executeQuery( lArgs );
      Assert.assertEquals( 1, lTypeIds.size() );
      Assert.assertTrue( lTypeIds.contains( 1000 ) );

      /* test case using a varchar and a long column */
      lClause = UnstructuredWhereClause.startSql( "first_name = " );
      lClause.appendValue( "Rob" ).appendSql( " AND user_id = " ).appendValue( 1000l );

      lArgs = new DataSetArgument();
      lArgs.addWhereUnstructured( lClause );
      lTypeIds = executeQuery( lArgs );
      Assert.assertEquals( 1, lTypeIds.size() );
      Assert.assertTrue( lTypeIds.contains( 1000 ) );

      /* same as previous test case, but wrapped in brackets to test the appendSql(...) method */
      lClause = UnstructuredWhereClause.startSql( "(first_name = " );
      lClause.appendValue( "Rob" ).appendSql( " AND user_id = " ).appendValue( 1000l )
            .appendSql( ")" );

      lArgs = new DataSetArgument();
      lArgs.addWhereUnstructured( lClause );
      executeQuery( lArgs );
      Assert.assertEquals( 1, lTypeIds.size() );
      Assert.assertTrue( lTypeIds.contains( 1000 ) );
   }


   @Test
   public void testLikeWithNumber() {

      /* test to confirm LIKE can be used on a number field, not just text */
      UnstructuredWhereClause lClause = UnstructuredWhereClause.startSql( "user_id LIKE " );
      lClause.appendValue( "1000%" );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereUnstructured( lClause );
      Set<Integer> lTypeIds = executeQuery( lArgs );
      Assert.assertEquals( 1, lTypeIds.size() );
      Assert.assertTrue( lTypeIds.contains( 1000 ) );
   }


   @Test
   public void testLowerLikeLower() {

      /* extends previous test to confirm LOWER can be used on a number field */
      UnstructuredWhereClause lClause = UnstructuredWhereClause.startSql( "user_id LIKE LOWER(" );
      lClause.appendValue( "1000%" ).appendSql( ")" );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereUnstructured( lClause );
      Set<Integer> lTypeIds = executeQuery( lArgs );
      Assert.assertEquals( 1, lTypeIds.size() );
      Assert.assertTrue( lTypeIds.contains( 1000 ) );
   }


   @Test
   public void testExistsLowerLikeLower() {

      UnstructuredWhereClause lClause = UnstructuredWhereClause.startSql(
            "EXISTS ( SELECT 1 FROM  utl_user where LOWER (utl_user.first_name) = LOWER(" );
      lClause.appendValue( "rob" ).appendSql( "))" );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereUnstructured( lClause );
      Set<Integer> lTypeIds = executeQuery( lArgs );
   }


   private Set<Integer> executeQuery( DataSetArgument aArgs ) {
      QuerySet lQs = QueryExecutor.executeQuery( getClass(), aArgs );
      Set<Integer> lUserIds = new HashSet<>();
      while ( lQs.next() ) {
         lUserIds.add( lQs.getInteger( "user_id" ) );
      }
      return lUserIds;
   }
}
