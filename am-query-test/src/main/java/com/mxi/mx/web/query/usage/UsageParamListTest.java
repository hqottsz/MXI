package com.mxi.mx.web.query.usage;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.InventoryKey;


public class UsageParamListTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), UsageParamListTest.class );
   }


   @Test
   public void usageParamList_returnRows() {
      DataSet lQs = execute( new InventoryKey( 4650, 1 ) );
      assertEquals( 1, lQs.getRowCount() );
      lQs.next();
      assertEquals( "'UsgParam4650_1' AS UsgParam4650_1",
            lQs.getString( "formatted_usage_param_list" ) );
      assertEquals( "UsgParam4650_1", lQs.getString( "usage_param_list" ) );
      assertEquals( "description", lQs.getString( "usage_param_desc_list" ) );
      assertEquals( "code", lQs.getString( "usage_param_cd_list" ) );
   }


   private DataSet execute( InventoryKey aInventory ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventory, "aInventoryDbId", "aInventoryId" );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }

}
