
package com.mxi.mx.core.unittest.table.sched;

import java.util.Date;

import com.mxi.mx.core.key.RefLabourRoleTypeKey;
import com.mxi.mx.core.key.RefLabourTimeKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.SchedLabourRoleTableKey;
import com.mxi.mx.core.table.sched.AbstractSchedLabourRoleTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * DOCUMENT_ME
 *
 * @author wleroux
 */
public class SchedLabourRole extends AbstractSchedLabourRoleTable<SchedLabourRoleTableKey> {

   /**
    * Creates a new {@linkplain SchedLabourRole} object.
    *
    * @param aSchedLabourRoleKey
    *           DOCUMENT_ME
    */
   public SchedLabourRole(SchedLabourRoleTableKey aSchedLabourRoleKey) {
      super( aSchedLabourRoleKey );
   }


   /**
    * Creates an instance of <code>SchedLabourRoleTable</code> for the purpose of getting and
    * updating its properties.
    *
    * @param aSchedLabourKey
    *           The labour key
    * @param aRoleTypeKey
    *           The labour role type key
    *
    * @return entity initialized with the SchedLabourKey and RefLabourRoleTypeKey
    */
   public static SchedLabourRole findByForeignKey( SchedLabourKey aSchedLabourKey,
         RefLabourRoleTypeKey aRoleTypeKey ) {

      // Intialize and return table entity
      return findByPrimaryKey( getPrimaryKey( aSchedLabourKey, aRoleTypeKey ) );
   }


   /**
    * Create an instance of SchedLabourRole for the purpose of getting and updating its properties.
    *
    * @param aSchedLabourRoleKey
    *           The sched_labour_role key
    *
    * @return SchedLabourRole entity initialized with the SchedLabourRoleKey
    */
   public static SchedLabourRole findByPrimaryKey( SchedLabourRoleTableKey aSchedLabourRoleKey ) {
      return new SchedLabourRole( aSchedLabourRoleKey );
   }


   /**
    * Asserts the end date/time property.
    *
    * @param aValue
    *           the expected value for the end date/time property.
    */
   public void assertActualEndDate( Date aValue ) {
      MxAssert.assertEquals( aValue, getActualEndDate() );
   }


   /**
    * Asserts the actual hours property.
    *
    * @param aValue
    *           the expected value for the actual hours property.
    */
   public void assertActualHours( Double aValue ) {
      MxAssert.assertEquals( aValue, getActualHours() );
   }


   /**
    * Asserts the start date/time property.
    *
    * @param aValue
    *           the expected value for the start date/time property.
    */
   public void assertActualStartDate( Date aValue ) {
      MxAssert.assertEquals( aValue, getActualStartDate() );
   }


   /**
    * Asserts the adj billing hours property.
    *
    * @param aValue
    *           the expected value for the adj billing hours property.
    */
   public void assertAdjustedBillingHours( Double aValue ) {
      MxAssert.assertEquals( aValue, getAdjustedBillingHours() );
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
    * Asserts the labour cost property.
    *
    * @param aValue
    *           the expected value for the labour cost property.
    */
   public void assertLabourCost( Double aValue ) {
      MxAssert.assertEquals( aValue, getLabourCost() );
   }


   /**
    * Asserts the refLabourRoleType property.
    *
    * @param aValue
    *           the expected value for refLabourRoleType property.
    */
   public void assertLabourRoleType( RefLabourRoleTypeKey aValue ) {
      MxAssert.assertEquals( aValue, getLabourRoleType() );
   }


   /**
    * Asserts the refLabourTime property.
    *
    * @param aValue
    *           the expected value for refLabourType property.
    */
   public void assertLabourTime( RefLabourTimeKey aValue ) {
      MxAssert.assertEquals( aValue, getLabourTime() );
   }


   /**
    * Asserts the sched hours property.
    *
    * @param aValue
    *           the expected value for the sched hours property.
    */
   public void assertSchedHours( Double aValue ) {
      MxAssert.assertEquals( aValue, getSchedHours() );
   }


   /**
    * Asserts the sched labour property.
    *
    * @param aValue
    *           the expected value for sched labour property.
    */
   public void assertSchedLabour( SchedLabourKey aValue ) {
      MxAssert.assertEquals( aValue, getSchedLabour() );
   }
}
