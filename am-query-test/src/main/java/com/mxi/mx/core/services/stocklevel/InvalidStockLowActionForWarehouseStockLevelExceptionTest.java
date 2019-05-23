package com.mxi.mx.core.services.stocklevel;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.RefStockLowActionKey;


/**
 * This class test the {@link InvalidStockLowActionForWarehouseStockLevelException.java} class
 *
 * @author srchlk
 *
 */
public class InvalidStockLowActionForWarehouseStockLevelExceptionTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    *
    * GIVEN the stock low action MANUAL, WHEN data is processed, THEN
    * InvalidStockLowActionForWarehouseStockLevelException should not be raised
    *
    * @throws InvalidStockLowActionForWarehouseStockLevelException
    */

   @Test( expected = Test.None.class )
   public void testStockLowActionManual()
         throws InvalidStockLowActionForWarehouseStockLevelException {

      InvalidStockLowActionForWarehouseStockLevelException
            .validate( RefStockLowActionKey.MANUAL.getCd() );

   }


   /**
    *
    * GIVEN the stock low action DISTREQ, WHen data is processed, THEN
    * InvalidStockLowActionForWarehouseStockLevelException should not be raised
    *
    * @throws InvalidStockLowActionForWarehouseStockLevelException
    */

   @Test( expected = Test.None.class )
   public void testStockLowActionDistreq()
         throws InvalidStockLowActionForWarehouseStockLevelException {

      InvalidStockLowActionForWarehouseStockLevelException
            .validate( RefStockLowActionKey.DISTREQ.getCd() );

   }


   /**
    *
    * GIVEN the stock low action SHIPREQ, WHen data is processed, THEN
    * InvalidStockLowActionForWarehouseStockLevelException should be raised
    *
    * @throws InvalidStockLowActionForWarehouseStockLevelException
    */

   @Test( expected = InvalidStockLowActionForWarehouseStockLevelException.class )
   public void testStockLowActionShipreq()
         throws InvalidStockLowActionForWarehouseStockLevelException {

      InvalidStockLowActionForWarehouseStockLevelException
            .validate( RefStockLowActionKey.SHIPREQ.getCd() );

   }


   /**
    *
    * GIVEN the stock low action POREQ, WHen data is processed, THEN
    * InvalidStockLowActionForWarehouseStockLevelException should be raised
    *
    * @throws InvalidStockLowActionForWarehouseStockLevelException
    */
   @Test( expected = InvalidStockLowActionForWarehouseStockLevelException.class )
   public void testStockLowActionPoreq()
         throws InvalidStockLowActionForWarehouseStockLevelException {

      InvalidStockLowActionForWarehouseStockLevelException
            .validate( RefStockLowActionKey.POREQ.getCd() );

   }

}
