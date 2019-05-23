package com.mxi.mx.wpl.loader.query;

import static org.junit.Assert.assertEquals;

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


@RunWith( BlockJUnit4ClassRunner.class )
public class GetWorkPackageSetUpTest {

   public static final String STATUS = "ACTIVE";

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), GetWorkPackageSetUpTest.class,
            "GetWorkPackageSetUpTest.xml" );
   }


   @Test
   public void testQuery() throws Exception {
      DataSet lResult = execute();

      assertEquals( 1, lResult.getRowCount() );
   }


   private DataSet execute() {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aStatusCd", STATUS );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
