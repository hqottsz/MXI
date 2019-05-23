
package com.mxi.mx.core.unittest.table.utl;

import org.junit.Assert;

import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Class for accessing data from the UTL_USER table.
 */
public class UtlUser extends com.mxi.mx.core.table.utl.UtlUser {

   /**
    * Initializes the class.
    *
    * @param aUser
    *           primary key of the table.
    */
   public UtlUser(UserKey aUser) {
      super( aUser );
   }


   /**
    * Asserts that the row does not exist in the table
    */
   public void assertDoesNotExist() {

      MxAssert.assertFalse( "The utl_user table does have the row.", exists() );
   }


   /**
    * Asserts the email_addr column.
    *
    * @param aExpected
    *           expected value of the column.
    */
   public void assertEmailAddress( String aExpected ) {

      // Assert that the actual and expected match
      MxAssert.assertEquals( "email_addr", aExpected, getString( ColumnName.EMAIL_ADDR ) );
   }


   /**
    * Asserts that the row exists in the table
    */
   public void assertExist() {

      Assert.assertTrue( "The utl_user table does not have the row.", exists() );
   }


   /**
    * Asserts the first_name column.
    *
    * @param aExpected
    *           expected value of the column.
    */
   public void assertFirstName( String aExpected ) {

      // Assert that the actual and expected match
      MxAssert.assertEquals( "first_name", aExpected, getFirstName() );
   }


   /**
    * Asserts the last_name column.
    *
    * @param aExpected
    *           expected value of the column.
    */
   public void assertLastName( String aExpected ) {

      // Assert that the actual and expected match
      MxAssert.assertEquals( "last_name", aExpected, getLastName() );
   }


   /**
    * Asserts the locked_bool column.
    *
    * @param aExpected
    *           expected value of the column.
    */
   public void assertLocked( boolean aExpected ) {

      // Assert that the actual and expected match
      MxAssert.assertEquals( "locked_bool", aExpected, getLockedBool() );
   }


   /**
    * Asserts the middle_name column.
    *
    * @param aExpected
    *           expected value of the column.
    */
   public void assertMiddleName( String aExpected ) {

      // Assert that the actual and expected match
      MxAssert.assertEquals( "middle_name", aExpected, getMiddleName() );
   }


   /**
    * Asserts the username column.
    *
    * @param aExpected
    *           expected value of the column.
    */
   public void assertUsername( String aExpected ) {

      // Assert that the actual and expected match
      MxAssert.assertEquals( "username", aExpected, getUsername() );
   }
}
