package com.mxi.mx.core.services.stocklevel.dataservices;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.RefStockLowActionKey;


/**
 * Tests {@link InvalidStockLowActionForSupplyLocStockLevelException} class
 *
 * @author ruprlk
 */
public class InvalidStockLowActionForSupplyLocStockLevelExceptionTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    *
    * GIVEN the stock low action POREQ, WHen data is processed, THEN
    * InvalidStockLowActionForSupplyLocStockLevelException should not be raised
    *
    * @throws InvalidStockLowActionForSupplyLocStockLevelException
    */
   @Test( expected = Test.None.class )
   public void testStockLowActionPoreq()
         throws InvalidStockLowActionForSupplyLocStockLevelException {

      InvalidStockLowActionForSupplyLocStockLevelException
            .validate( RefStockLowActionKey.POREQ.getCd() );

   }


   /**
    *
    * GIVEN the stock low action SHIPREQ, WHen data is processed, THEN
    * InvalidStockLowActionForSupplyLocStockLevelException should not be raised
    *
    * @throws InvalidStockLowActionForSupplyLocStockLevelException
    */

   @Test( expected = Test.None.class )
   public void testStockLowActionShipreq()
         throws InvalidStockLowActionForSupplyLocStockLevelException {

      InvalidStockLowActionForSupplyLocStockLevelException
            .validate( RefStockLowActionKey.SHIPREQ.getCd() );

   }


   /**
    *
    * GIVEN the stock low action MANUAL, WHEN data is processed, THEN
    * InvalidStockLowActionForSupplyLocStockLevelException should not be raised
    *
    * @throws InvalidStockLowActionForSupplyLocStockLevelException
    */

   @Test( expected = Test.None.class )
   public void testStockLowActionManual()
         throws InvalidStockLowActionForSupplyLocStockLevelException {

      InvalidStockLowActionForSupplyLocStockLevelException
            .validate( RefStockLowActionKey.MANUAL.getCd() );

   }


   /**
    *
    * GIVEN the stock low action DISTREQ, WHEN data is processed, THEN
    * InvalidStockLowActionForSupplyLocStockLevelException should be raised
    *
    * @throws InvalidStockLowActionForSupplyLocStockLevelException
    */

   @Test( expected = InvalidStockLowActionForSupplyLocStockLevelException.class )
   public void testStockLowActionDistreq()
         throws InvalidStockLowActionForSupplyLocStockLevelException {

      InvalidStockLowActionForSupplyLocStockLevelException
            .validate( RefStockLowActionKey.DISTREQ.getCd() );

   }

}
