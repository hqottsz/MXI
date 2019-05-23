package com.mxi.mx.core.services.stocklevel.dataservices;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;


/**
 * Tests { {@link InvalidAllocationPercentageException}
 *
 * @author ruprlk
 */
public class InvalidAllocationPercentageExceptionTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    *
    * GIVEN an allocation percentage less than 100 WHEN data is processed THEN
    * InvalidStockLevelValueException should not be raised
    *
    * @throws InvalidStockLevelValueException
    */

   @Test( expected = Test.None.class )
   public void testValidAllocationPercentage() throws InvalidAllocationPercentageException {

      InvalidAllocationPercentageException.validate( "Attributename", 9d );

   }


   /**
    *
    * GIVEN an allocation percentage greater than 100 WHEN data is processed THEN
    * InvalidStockLevelValueException should be raised
    *
    * @throws InvalidStockLevelValueException
    */

   @Test( expected = InvalidAllocationPercentageException.class )
   public void testInvalidAllocationPercentage() throws InvalidAllocationPercentageException {

      InvalidAllocationPercentageException.validate( "Attributename", 101d );

   }

}
