package com.mxi.mx.core.services.location.dataservices;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;


/**
 * Tests the InvalidBinRouteOrderException class
 *
 * @author srchlk
 *
 */
public class InvalidBinRouteOrderExceptionTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    *
    * GIVEN a bin location, WHEN an integer value added to route order field, THEN
    * InvalidBinRouteOrderException should not be raised
    *
    * @throws InvalidBinRouteOrderException
    */

   @Test( expected = Test.None.class )
   public void testAddIntegerAsRouteOrder() throws InvalidBinRouteOrderException {

      InvalidBinRouteOrderException.validate( "aRouteOrder", "10" );

   }


   /**
    *
    * GIVEN a bin location, WHEN a string value added to route order field, THEN
    * InvalidBinRouteOrderException should be raised
    *
    * @throws InvalidBinRouteOrderException
    */
   @Test( expected = InvalidBinRouteOrderException.class )
   public void testAddStringAsRouteOrder() throws InvalidBinRouteOrderException {

      InvalidBinRouteOrderException.validate( "aRouteOrder", "ABC" );

   }


   /**
    *
    * GIVEN a bin location, WHEN a decimal value added to route order field, THEN
    * InvalidBinRouteOrderException should be raised
    *
    * @throws InvalidBinRouteOrderException
    */
   @Test( expected = InvalidBinRouteOrderException.class )
   public void testAddDecimalAsRouteOrder() throws InvalidBinRouteOrderException {

      InvalidBinRouteOrderException.validate( "aRouteOrder", "5.7" );

   }
}
