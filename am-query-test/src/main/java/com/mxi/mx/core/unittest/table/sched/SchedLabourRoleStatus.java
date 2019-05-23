
package com.mxi.mx.core.unittest.table.sched;

import com.mxi.mx.common.key.DbKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.RefLabourRoleStatusKey;
import com.mxi.mx.core.key.RefLabourRoleTypeKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.SchedLabourRoleStatusKey;
import com.mxi.mx.core.services.stask.labour.DefaultLabourRoleStatusFinder;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * DOCUMENT_ME
 *
 * @author wleroux
 */
public class SchedLabourRoleStatus extends com.mxi.mx.core.table.sched.SchedLabourRoleStatusTable {

   /**
    * Creates a new {@linkplain SchedLabourRole} object.
    *
    * @param aSchedLabourRoleStatusKey
    *           sched labour role status key
    */
   public SchedLabourRoleStatus(SchedLabourRoleStatusKey aSchedLabourRoleStatusKey) {
      super( aSchedLabourRoleStatusKey );
   }


   /**
    * Creates an instance of SchedLabourRoleStatus for the purpose of getting and updating its
    * properties
    *
    * @param aLabourKey
    *           the labour key
    * @param aLabourRoleTypeKey
    *           the labour role type key
    * @param aStatusOrder
    *           the status order
    *
    * @return the current SchedLabourRoleStatus for the labour with a specified role.
    */
   public static SchedLabourRoleStatus findByLabourRoleTypeKey( SchedLabourKey aLabourKey,
         RefLabourRoleTypeKey aLabourRoleTypeKey, int aStatusOrder ) {
      SchedLabourRoleStatusKey lPrimaryKey = new DefaultLabourRoleStatusFinder()
            .findByForeignKey( aLabourKey, aLabourRoleTypeKey, aStatusOrder );

      // Initialize and return table entity
      return findByPrimaryKey( lPrimaryKey );
   }


   /**
    * Create an instance of SchedLabourRoleStatus for the purpose of getting and updating its
    * properties.
    *
    * @param aSchedLabourRoleStatusKey
    *
    * @return the table object
    */
   public static SchedLabourRoleStatus
         findByPrimaryKey( SchedLabourRoleStatusKey aSchedLabourRoleStatusKey ) {
      return new SchedLabourRoleStatus( aSchedLabourRoleStatusKey );
   }


   /**
    * Asserts that the primary key does not exist in the table
    */
   public void assertDoesNotExists() {
      MxAssert.assertFalse( exists() );
   }


   /**
    * Asserts that the primary key exists in the table
    */
   public void assertExists() {
      MxAssert.assertTrue( exists() );
   }


   /**
    * Asserts the aHumanResourceKey property.
    *
    * @param aValue
    *           the expected value for aHumanResourceKey property.
    */
   public void assertHr( HumanResourceKey aValue ) {
      MxAssert.assertEquals( aValue, getHumanResource() );
   }


   /**
    * Asserts the aRefLabourRoleStatusKey property.
    *
    * @param aValue
    *           the expected value for aRefLabourRoleStatusKey property.
    */
   public void assertLabourRoleStatus( RefLabourRoleStatusKey aValue ) {
      MxAssert.assertEquals( aValue, getLabourRoleStatus() );
   }


   /**
    * Asserts the aSchedLabourRoleKey property.
    *
    * @param aValue
    *           the expected value for aSchedLabourRoleKey property.
    */
   public void assertSchedLabourRole( DbKey aValue ) {
      MxAssert.assertEquals( aValue, getSchedLabourRole() );
   }


   /**
    * Asserts the aStatusOrder property.
    *
    * @param aValue
    *           the expected value for aCurrentBool property.
    */
   public void assertStatusOrder( int aValue ) {
      MxAssert.assertEquals( aValue, getStatusOrder() );
   }
}
