package com.mxi.mx.common.table;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.DataSetSortSpec;
import com.mxi.mx.common.dataset.InvalidSearchCriteriaException;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.core.common.ejb.dao.QueryDAOBean;


@RunWith( BlockJUnit4ClassRunner.class )
public class QuerySetFactoryTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   @Test
   public void testFullQuery() {

      String lQueryName = getClass().getName();
      QuerySet lQuerySet = QuerySetFactory.getInstance().executeQuery( lQueryName,
            new DataSetArgument(), new ArrayList<DataSetSortSpec>() );

      int lCount = 0;
      while ( lQuerySet.next() ) {

         int lA = lQuerySet.getInt( "A" );
         int lB = lQuerySet.getInt( "B" );
         int lC = lQuerySet.getInt( "C" );
         String lD = lQuerySet.getString( "D" );

         Assert.assertNotNull( lA );
         Assert.assertNotNull( lB );
         Assert.assertNotNull( lC );
         Assert.assertNotNull( lD );

         lCount++;
      }

      Assert.assertEquals( 210, lCount );

   }


   @Test
   public void testFailMissingArgs() {

      String lQueryName = getClass().getName();
      try {
         QuerySetFactory.getInstance().executeQuery( lQueryName, null,
               new ArrayList<DataSetSortSpec>() );
         Assert.fail();
      } catch ( Exception e ) {
         // expect an error
      }
   }


   @Test
   public void testWhereClauseQuery1() {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereEquals( "C", "6" );

      String lQueryName = getClass().getName();
      QuerySet lQuerySet = QuerySetFactory.getInstance().executeQuery( lQueryName, lArgs,
            new ArrayList<DataSetSortSpec>() );

      int lCount = 0;
      while ( lQuerySet.next() ) {

         int lA = lQuerySet.getInt( "A" );
         int lB = lQuerySet.getInt( "B" );
         int lC = lQuerySet.getInt( "C" );
         String lD = lQuerySet.getString( "D" );

         Assert.assertNotNull( lA );
         Assert.assertNotNull( lB );
         Assert.assertNotNull( lC );
         Assert.assertNotNull( lD );

         lCount++;
      }

      Assert.assertEquals( 3, lCount );
   }


   @Test
   public void testWhereClauseQuery2() throws InvalidSearchCriteriaException {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereLike( "D", "01%6", false );

      String lQueryName = getClass().getName();
      QuerySet lQuerySet = QuerySetFactory.getInstance().executeQuery( lQueryName, lArgs,
            new ArrayList<DataSetSortSpec>() );

      int lCount = 0;
      while ( lQuerySet.next() ) {

         int lA = lQuerySet.getInt( "A" );
         int lB = lQuerySet.getInt( "B" );
         int lC = lQuerySet.getInt( "C" );
         String lD = lQuerySet.getString( "D" );

         Assert.assertNotNull( lA );
         Assert.assertNotNull( lB );
         Assert.assertNotNull( lC );
         Assert.assertNotNull( lD );

         lCount++;
      }

      Assert.assertEquals( 2, lCount );
   }


   @Test
   public void testOrderClauseQuery1() {

      DataSetArgument lArgs = new DataSetArgument();
      List<DataSetSortSpec> lSortArg = new ArrayList<DataSetSortSpec>();

      DataSetSortSpec lDataSetSortSpec = new DataSetSortSpec();
      lDataSetSortSpec.setExpression( "D" );
      lDataSetSortSpec.setAscending( true );

      lSortArg.add( lDataSetSortSpec );

      String lQueryName = getClass().getName();
      QuerySet lQuerySet =
            QuerySetFactory.getInstance().executeQuery( lQueryName, lArgs, lSortArg );

      Assert.assertTrue( lQuerySet.next() );

      int lA = lQuerySet.getInt( "A" );
      int lB = lQuerySet.getInt( "B" );

      Assert.assertEquals( 1, lA );
      Assert.assertEquals( 1, lB );

      Assert.assertTrue( lQuerySet.next() );
   }


   @Test
   public void testOrderClauseQuery2() {

      DataSetArgument lArgs = new DataSetArgument();
      List<DataSetSortSpec> lSortArg = new ArrayList<DataSetSortSpec>();

      DataSetSortSpec lDataSetSortSpec = new DataSetSortSpec();
      lDataSetSortSpec.setExpression( "B" );
      lDataSetSortSpec.setAscending( false );
      lSortArg.add( lDataSetSortSpec );

      lDataSetSortSpec = new DataSetSortSpec();
      lDataSetSortSpec.setExpression( "A" );
      lDataSetSortSpec.setAscending( true );
      lSortArg.add( lDataSetSortSpec );

      String lQueryName = getClass().getName();
      QuerySet lQuerySet =
            QuerySetFactory.getInstance().executeQuery( lQueryName, lArgs, lSortArg );

      Assert.assertTrue( lQuerySet.next() );

      int lA = lQuerySet.getInt( "A" );
      int lB = lQuerySet.getInt( "B" );

      Assert.assertEquals( 1, lA );
      Assert.assertEquals( 20, lB );

      Assert.assertTrue( lQuerySet.next() );
   }


   @Test
   public void testOrderClauseQuery3() {

      DataSetArgument lArgs = new DataSetArgument();
      List<DataSetSortSpec> lSortArg = new ArrayList<DataSetSortSpec>();

      DataSetSortSpec lDataSetSortSpec = new DataSetSortSpec();
      lDataSetSortSpec.setExpression( "B" );
      lDataSetSortSpec.setAscending( false );
      lSortArg.add( lDataSetSortSpec );

      lDataSetSortSpec = new DataSetSortSpec();
      lDataSetSortSpec.setExpression( "A" );
      lDataSetSortSpec.setAscending( true );
      lSortArg.add( lDataSetSortSpec );

      // checks to ensure an Order by Wrapper would be added
      String lQueryName = getClass().getName() + "2";
      QuerySet lQuerySet =
            QuerySetFactory.getInstance().executeQuery( lQueryName, lArgs, lSortArg );

      Assert.assertTrue( lQuerySet.next() );

      int lA = lQuerySet.getInt( "A" );
      int lB = lQuerySet.getInt( "B" );

      Assert.assertEquals( 1, lA );
      Assert.assertEquals( 20, lB );

      Assert.assertTrue( lQuerySet.next() );
   }


   @Test
   public void testOrderClauseQueryNone() {

      DataSetArgument lArgs = new DataSetArgument();
      List<DataSetSortSpec> lSortArg = new ArrayList<DataSetSortSpec>();

      String lQueryName = getClass().getName();
      QuerySet lQuerySet =
            QuerySetFactory.getInstance().executeQuery( lQueryName, lArgs, lSortArg );

      Assert.assertTrue( lQuerySet.next() );
   }


   @Test
   public void testOrderWhereClauseQuery1() {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereEquals( "A", "1" );

      List<DataSetSortSpec> lSortArg = new ArrayList<DataSetSortSpec>();

      DataSetSortSpec lDataSetSortSpec = new DataSetSortSpec();
      lDataSetSortSpec.setExpression( "B" );
      lDataSetSortSpec.setAscending( false );
      lSortArg.add( lDataSetSortSpec );

      lDataSetSortSpec = new DataSetSortSpec();
      lDataSetSortSpec.setExpression( "A" );
      lDataSetSortSpec.setAscending( true );
      lSortArg.add( lDataSetSortSpec );

      String lQueryName = getClass().getName();
      QuerySet lQuerySet =
            QuerySetFactory.getInstance().executeQuery( lQueryName, lArgs, lSortArg );

      Assert.assertTrue( lQuerySet.next() );

      Assert.assertEquals( 1, lQuerySet.getInt( "A" ) );
      Assert.assertEquals( 20, lQuerySet.getInt( "B" ) );

      int lCount = 0;
      do {

         int lA = lQuerySet.getInt( "A" );
         int lB = lQuerySet.getInt( "B" );
         int lC = lQuerySet.getInt( "C" );
         String lD = lQuerySet.getString( "D" );

         Assert.assertNotNull( lA );
         Assert.assertNotNull( lB );
         Assert.assertNotNull( lC );
         Assert.assertNotNull( lD );

         Assert.assertEquals( 20 - lCount, lB );

         lCount++;
      } while ( lQuerySet.next() );
      Assert.assertEquals( 20, lCount );
   }


   @Test
   public void testWindowClauseQuery1() {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addResultWindow( 50, 100 );

      List<DataSetSortSpec> lSortArg = new ArrayList<DataSetSortSpec>();

      String lQueryName = getClass().getName();
      QuerySet lQuerySet =
            QuerySetFactory.getInstance().executeQuery( lQueryName, lArgs, lSortArg );

      Assert.assertTrue( lQuerySet.next() );

      int lCount = 0;
      do {
         lCount++;
      } while ( lQuerySet.next() );
      Assert.assertEquals( 50, lCount );
   }


   @Test
   public void testWindowClauseQuery2() {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addResultWindow( 200, -1 );

      List<DataSetSortSpec> lSortArg = new ArrayList<DataSetSortSpec>();

      String lQueryName = getClass().getName();
      QuerySet lQuerySet =
            QuerySetFactory.getInstance().executeQuery( lQueryName, lArgs, lSortArg );

      Assert.assertTrue( lQuerySet.next() );

      int lCount = 0;
      do {
         lCount++;
      } while ( lQuerySet.next() );
      Assert.assertEquals( 10, lCount );
   }


   @Test
   public void testWindowClauseQuery3() {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addResultWindow( -1, 10 );

      List<DataSetSortSpec> lSortArg = new ArrayList<DataSetSortSpec>();

      String lQueryName = getClass().getName();
      QuerySet lQuerySet =
            QuerySetFactory.getInstance().executeQuery( lQueryName, lArgs, lSortArg );

      Assert.assertTrue( lQuerySet.next() );

      int lCount = 0;
      do {
         lCount++;
      } while ( lQuerySet.next() );
      Assert.assertEquals( 10, lCount );
   }


   @Test
   public void testAllClauseQuery1() {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereEquals( "A", "1" );

      List<DataSetSortSpec> lSortArg = new ArrayList<DataSetSortSpec>();

      DataSetSortSpec lDataSetSortSpec = new DataSetSortSpec();
      lDataSetSortSpec.setExpression( "B" );
      lDataSetSortSpec.setAscending( false );
      lSortArg.add( lDataSetSortSpec );
      lArgs.addResultWindow( 5, 15 );

      lDataSetSortSpec = new DataSetSortSpec();
      lDataSetSortSpec.setExpression( "A" );
      lDataSetSortSpec.setAscending( true );
      lSortArg.add( lDataSetSortSpec );

      String lQueryName = getClass().getName();
      QuerySet lQuerySet =
            QuerySetFactory.getInstance().executeQuery( lQueryName, lArgs, lSortArg );

      Assert.assertTrue( lQuerySet.next() );

      Assert.assertEquals( 1, lQuerySet.getInt( "A" ) );
      Assert.assertEquals( 15, lQuerySet.getInt( "B" ) );

      int lCount = 0;
      do {

         int lA = lQuerySet.getInt( "A" );
         int lB = lQuerySet.getInt( "B" );
         int lC = lQuerySet.getInt( "C" );
         String lD = lQuerySet.getString( "D" );

         Assert.assertNotNull( lA );
         Assert.assertNotNull( lB );
         Assert.assertNotNull( lC );
         Assert.assertNotNull( lD );

         Assert.assertEquals( 15 - lCount, lB );

         lCount++;
      } while ( lQuerySet.next() );
      Assert.assertEquals( 10, lCount );
   }


   @Test
   public void testWindowClauseQueryCount() {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addResultWindow( -1, 10 );

      List<DataSetSortSpec> lSortArg = new ArrayList<DataSetSortSpec>();

      String lQueryName = getClass().getName();
      QuerySet lQuerySet =
            QuerySetFactory.getInstance().executeQuery( lQueryName, lArgs, lSortArg );

      Assert.assertTrue( lQuerySet.next() );

      int lCount = 0;
      do {
         lCount++;
      } while ( lQuerySet.next() );
      Assert.assertEquals( 10, lCount );
      Assert.assertEquals( 210, lQuerySet.getRowCount() );
   }


   @Test
   public void testWindowClauseMaxRowsCount() {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addResultWindow( -1, 10 );

      List<DataSetSortSpec> lSortArg = new ArrayList<DataSetSortSpec>();

      String lQueryName = getClass().getName();
      QuerySet lQuerySet = ( ( QueryDAOBean ) QuerySetFactory.getInstance() )
            .executeQuery( lQueryName, lArgs, lSortArg, 100 );

      Assert.assertTrue( lQuerySet.next() );

      int lCount = 0;
      do {
         lCount++;
      } while ( lQuerySet.next() );
      Assert.assertEquals( 10, lCount );
      Assert.assertEquals( 100, lQuerySet.getRowCount() );
   }
}
