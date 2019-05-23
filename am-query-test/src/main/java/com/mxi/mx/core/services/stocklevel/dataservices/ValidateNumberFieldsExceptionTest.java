package com.mxi.mx.core.services.stocklevel.dataservices;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;


/**
 * This class test {@link ValidateNumberFieldsException.java} class
 *
 * @author ruprlk
 *
 */
public class ValidateNumberFieldsExceptionTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    *
    * GIVEN a string value representing a numeric value WHEN data is processed THEN
    * ValidateNumberFieldsException should not be raised
    *
    * @throws ValidateNumberFieldsException
    */

   @Test( expected = Test.None.class )
   public void testStringValueWithNumber() throws ValidateNumberFieldsException {

      ValidateNumberFieldsException.validate( "Attributename", "9" );

   }


   /**
    *
    * GIVEN a string value with letters in it WHEN data is processed THEN
    * ValidateNumberFieldsException should be raised
    *
    * @throws ValidateNumberFieldsException
    */

   @Test( expected = ValidateNumberFieldsException.class )
   public void testStringValueWithLetters() throws ValidateNumberFieldsException {

      ValidateNumberFieldsException.validate( "Attributename", "abc" );

   }


   /**
    *
    * GIVEN a string value with symbols in it WHEN data is processed THEN
    * ValidateNumberFieldsException should be raised
    *
    * @throws ValidateNumberFieldsException
    */
   @Test( expected = ValidateNumberFieldsException.class )
   public void testStringValueWithSymbol() throws ValidateNumberFieldsException {

      ValidateNumberFieldsException.validate( "Attributename", "5+6" );

   }

}
