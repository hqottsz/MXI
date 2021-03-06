
package com.mxi.mx.core.unittest.table.lrp;

import java.util.Date;

import org.junit.Assert;

import com.mxi.mx.common.table.AbstractTable;
import com.mxi.mx.core.key.LrpInvTaskPlanKey;
import com.mxi.mx.core.unittest.MxAssert;
import com.mxi.mx.model.key.IdKey;
import com.mxi.mx.model.key.IdKeyImpl;


/**
 * This class is used to test with <code>lrp_inv_task_plan</code> table.
 */
public class LrpInvTaskPlanTable extends AbstractTable<LrpInvTaskPlanKey> {

   /**
    * Enumeration of column names in the lrp_inv_task_plan table.
    */
   public static enum ColumnName implements ColumnNameInt {
      INV_NO_DB_ID, INV_NO_ID, // Primary Key
      TASK_DEFN_DB_ID, TASK_DEFN_ID, // Primary Key
      LRP_DB_ID, LRP_ID, PUBLISHED_DT
   }


   /**
    * Creates a new LrpInvTaskPlanTable object for the purpose of getting/updating properties.
    *
    * @param aKey
    *           The aircraft key.
    */
   public LrpInvTaskPlanTable(LrpInvTaskPlanKey aKey) {
      super( aKey );
   }


   /**
    * Create an instance of lrp_inv_task_plan for the purpose of getting and updating its
    * properties.
    *
    * @param aKey
    *           The primary key.
    *
    * @return LrpInvTaskPlanTable entity initialized with the WorkscopeKey
    */
   public static LrpInvTaskPlanTable findByPrimaryKey( LrpInvTaskPlanKey aKey ) {

      // Return the SchedStask object
      return new LrpInvTaskPlanTable( aKey );
   }


   /**
    * Asserts that the row doesn't exist.
    */
   public void assertDoesNotExist() {
      if ( exists() ) {
         Assert.fail( "The lrp_inv_task_plan table does have the row" );
      }
   }


   /**
    * Asserts that the row exists
    */
   public void assertExist() {
      if ( !exists() ) {
         Assert.fail( "The lrp_inv_task_plan table does not have the row" );
      }
   }


   /**
    * Asserts that the <code>lrp_db_id:lrp_id</code> value are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertLrpPlanKey( IdKey aExpected ) {
      MxAssert.assertEquals( aExpected, getLrpPlanKey() );
   }


   /**
    * Asserts that the <code>published_dt</code> value are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertPublishedDate( Date aExpected ) {
      MxAssert.assertEquals( aExpected, getPublishedDate() );
   }


   /**
    * {@inheritDoc}
    */
   public IdKey getLrpPlanKey() {
      if ( isNull( ColumnName.LRP_ID ) ) {
         return null;
      } else {
         return new IdKeyImpl( getInt( ColumnName.LRP_DB_ID ), getInt( ColumnName.LRP_ID ) );
      }
   }


   /**
    * {@inheritDoc}
    */
   public Date getPublishedDate() {
      if ( isNull( ColumnName.PUBLISHED_DT ) ) {
         return null;
      } else {
         return getDate( ColumnName.PUBLISHED_DT );
      }
   }


   /**
    * Returns the value of the column name enum property.
    *
    * @return the value of the column name enum property.
    */
   @Override
   protected ColumnNameInt[] getColumnNameEnum() {
      return ColumnName.values();
   }
}
