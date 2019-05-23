package com.mxi.mx.web.query.inventory;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;


public class InventorySearchByQuarantineTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   private static final int HR_DB_ID = 4650;
   private static final int HR_ID = 1;
   private static final int NUMBER_OF_ROWS = 100;


   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(),
            InventorySearchByQuarantineTest.class, "InventorySearchByQuarantineTest.xml" );
   }


   @Test
   public void execute_searchByQuarantine_withPartName() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereLike( "eqp_part_no.part_no_oem", "PN-TEST-ABC" );
      lArgs.addWhereLike( "eqp_part_no.part_no_sdesc", "Part ABC%" );
      lArgs.addWhereBoolean( "quar_quar.historic_bool", false );

      DataSet lResult = execute( lArgs, NUMBER_OF_ROWS, HR_DB_ID, HR_ID );

      assertEquals( "Number of retrieved rows: ", 1, lResult.getRowCount() );
   }


   @Test
   public void execute_searchByQuarantine_withPartName_noResults() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereLike( "eqp_part_no.part_no_oem", "PN-TEST-ABC" );
      lArgs.addWhereLike( "eqp_part_no.part_no_sdesc", "INEXISTENT" );
      lArgs.addWhereBoolean( "quar_quar.historic_bool", false );

      DataSet lResult = execute( lArgs, NUMBER_OF_ROWS, HR_DB_ID, HR_ID );

      assertEquals( "Number of retrieved rows: ", 0, lResult.getRowCount() );
   }


   private DataSet execute( DataSetArgument aArgs, Integer aRowNum, Integer aHrDbId,
         Integer aHrId ) {

      aArgs.add( "aRowNum", aRowNum );
      aArgs.add( "aHrDbId", aHrDbId );
      aArgs.add( "aHrId", aHrId );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), aArgs );
   }

}
