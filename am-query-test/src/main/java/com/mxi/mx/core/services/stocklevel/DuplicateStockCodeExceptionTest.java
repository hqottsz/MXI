package com.mxi.mx.core.services.stocklevel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.services.stocklevel.dataservices.DuplicateStockCodeException;


/**
 * Tests the DuplicateStockCodeException class
 *
 * @author spatlk
 */

@RunWith( BlockJUnit4ClassRunner.class )
public class DuplicateStockCodeExceptionTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private String STOCK_CD = "STOCKCD";


   @Before
   public void setUp() throws Exception {

      // create a two stocks with same stock codes
      new StockBuilder().withStockCode( STOCK_CD ).build();
      new StockBuilder().withStockCode( STOCK_CD ).build();
   }


   /**
    *
    * GIVEN a duplicate stock code, WHEN the code is passed to the
    * {@link DuplicateStockCodeException.validate} method, THEN a DuplicateStockCodeException is
    * thrown
    *
    * @throws DuplicateStockCodeException
    *            because the stock level is duplicated
    */
   @Test( expected = DuplicateStockCodeException.class )
   public void testDuplicateStockCodeException() throws DuplicateStockCodeException {
      DuplicateStockCodeException.validate( STOCK_CD );

      Assert.fail( "DuplicateStockCodeException" );
   }
}
