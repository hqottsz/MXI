
package com.mxi.mx.core.unittest.table.po;

import java.util.Date;

import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.PurchaseOrderAuthKey;
import com.mxi.mx.core.key.RefPoAuthLvlKey;
import com.mxi.mx.core.key.RefPoAuthLvlStatusKey;
import com.mxi.mx.core.table.po.PoAuthTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Class for accessing data from the PO_AUTH table.
 *
 * @author $Author: dmurphy $
 * @version $Revision: 1.1 $
 */
public class PoAuth extends PoAuthTable {

   /**
    * Initializes the class.
    *
    * @param aKey
    *           primary key of the table.
    */
   public PoAuth(PurchaseOrderAuthKey aKey) {

      super( aKey );
   }


   /**
    * Asserts that the AOG Override bool is correct on the purchase order
    *
    * @param aExpected
    *           The expected Vendor Note
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertAOGOverrideBool( boolean aExpected ) throws Exception {

      // Assert that the actual and expected match
      MxAssert.assertEquals( "aog_override_bool", aExpected, isAOGOverride() );
   }


   /**
    * Asserts that the auth_dt is correct on the purchase order
    *
    * @param aExpected
    *           The expected Authorizing HR
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertAuthDate( Date aExpected ) throws Exception {
      MxAssert.assertEquals( "auth_dt", aExpected, getAuthDate() );
   }


   /**
    * Asserts that the authorization level status is correct on the purchase order
    *
    * @param aExpected
    *           The expected currency
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertAuthLvlStatus( RefPoAuthLvlStatusKey aExpected ) throws Exception {

      // Assert that the actual and expected match
      MxAssert.assertEquals( "auth_lvl_status_db_id:auth_lvl_status_cd", aExpected,
            getAuthLvlStatusKey() );
   }


   /**
    * Asserts that the authorization level is correct on the purchase order
    *
    * @param aExpected
    *           The expected currency
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertAuthorizationLevel( RefPoAuthLvlKey aExpected ) throws Exception {

      MxAssert.assertEquals( "po_auth_lvl_db_id:po_auth_lvl_cd", aExpected, getPoAuthLvl() );
   }


   /**
    * Asserts that the auth_note is correct on the purchase order
    *
    * @param aExpected
    *           The exchange rate
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertAuthorizationNote( String aExpected ) throws Exception {

      MxAssert.assertEquals( "auth_note", aExpected, getAuthNote() );
   }


   /**
    * Asserts that the Authorizing Human Resource is correct on the purchase order
    *
    * @param aExpected
    *           The expected Authorizing HR
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertAuthorizingHR( HumanResourceKey aExpected ) throws Exception {

      MxAssert.assertEquals( "auth_hr_db_id:auth_hr_id", aExpected, getAuthHr() );
   }


   /**
    * Asserts that the authorization level status is correct on the purchase order
    *
    * @param aExpected
    *           The expected currency
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertBudgetStatus( RefPoAuthLvlStatusKey aExpected ) throws Exception {

      // Assert that the actual and expected match
      MxAssert.assertEquals( "auth_lvl_status_db_id:auth_lvl_status_cd", aExpected,
            getAuthLvlStatusKey() );
   }


   /**
    * Asserts that the table has a row corresponding to the current primary key.
    */
   public void assertExist() {
      if ( !exists() ) {
         MxAssert.fail( "The po_auth table does not have a row" );
      }
   }


   /**
    * Asserts that the hist_bool is correct
    *
    * @param aExpected
    *           The expected hist bool
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertHistBool( boolean aExpected ) throws Exception {
      MxAssert.assertEquals( aExpected, isHistoric() );
   }
}
