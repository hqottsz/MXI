package com.mxi.mx.web.jsp.controller.req;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.core.key.StockNoKey;


public class CreatePurchaseRequestForStockControllerHelperTest {

   @ClassRule
   public static final DatabaseConnectionRule iDatabaseConnectionRule =
         new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   @BeforeClass
   public static void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(),
            CreatePurchaseRequestForStockControllerHelperTest.class );
   }


   @Test
   public void getPartsForStockDs_orderedByAllocationPercentage() {
      StockNoKey lStockNoKey = new StockNoKey( 4650, 1 );
      String lHigherPercentagePartKey = "4650:2";
      String lLowerPercentagePartKey = "4650:1";

      DataSet lPartsForStockQueryCall =
            new CreatePurchaseRequestForStockControllerHelper().getPartsForStockDs( lStockNoKey );

      assertEquals( 2, lPartsForStockQueryCall.getRowCount() );
      lPartsForStockQueryCall.next();
      assertEquals( lHigherPercentagePartKey, lPartsForStockQueryCall.getString( "part_no_key" ) );
      lPartsForStockQueryCall.next();
      assertEquals( lLowerPercentagePartKey, lPartsForStockQueryCall.getString( "part_no_key" ) );
   }

}
