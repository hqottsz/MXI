
package com.mxi.mx.core.unittest.table.lrp;

import org.junit.Assert;

import com.mxi.mx.common.table.AbstractTable;
import com.mxi.mx.core.key.LrpEventWorkscopeKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.unittest.MxAssert;
import com.mxi.mx.model.key.IdKey;
import com.mxi.mx.model.key.IdKeyImpl;


/**
 * This class is used to test with <code>lrp_event_workscope</code> table.
 */
public class LrpEventWorkscopeTable extends AbstractTable<LrpEventWorkscopeKey> {

   /**
    * Enumeration of column names in the lrp_event_workscope table.
    */
   public static enum ColumnName implements ColumnNameInt {
      LRP_WORKSCOPE_DB_ID, LRP_WORKSCOPE_ID, // Primary Key
      LRP_EVENT_DB_ID, LRP_EVENT_ID, LRP_DB_ID, LRP_ID, TASK_DEFN_DB_ID, TASK_DEFN_ID, SCHED_DB_ID,
      SCHED_ID, PREV_WORKSCOPE_DB_ID, PREV_WORKSCOPE_ID
   }


   /**
    * Creates a new LrpEventWorkscopeTable object for the purpose of getting/updating properties.
    *
    * @param aKey
    *           The workscope key.
    */
   public LrpEventWorkscopeTable(LrpEventWorkscopeKey aKey) {
      super( aKey );
   }


   /**
    * Create an instance of lrp_event_workscope for the purpose of getting and updating its
    * properties.
    *
    * @param aKey
    *           Workscope Key
    *
    * @return LrpEventWorkscopeTable entity initialized with the WorkscopeKey
    */
   public static LrpEventWorkscopeTable findByPrimaryKey( LrpEventWorkscopeKey aKey ) {

      // Return the SchedStask object
      return new LrpEventWorkscopeTable( aKey );
   }


   /**
    * Asserts that the row doesn't exist.
    */
   public void assertDoesNotExist() {
      if ( exists() ) {
         Assert.fail( "The lrp_event_workscope table does have the row" );
      }
   }


   /**
    * Asserts that the row with <code>lrp_workscope_db_id:lrp_workscope_id
    */
   public void assertExist() {
      if ( !exists() ) {
         Assert.fail( "The lrp_event_workscope table does not have the row" );
      }
   }


   /**
    * Asserts that the <code>lrp_event_db_id:lrp_event_id</code> value are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertLrpEventKey( IdKey aExpected ) {
      MxAssert.assertEquals( aExpected, getLrpEventKey() );
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
    * Asserts that the <code>prev_workscope_db_id:prev_workscope_id</code> value are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertPrevWorkscopeKey( IdKey aExpected ) {
      MxAssert.assertEquals( aExpected, getPrevWorkscopeKey() );
   }


   /**
    * Asserts that the <code>sched_db_id:sched_id</code> value are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertSchedStaskKey( TaskKey aExpected ) {
      MxAssert.assertEquals( aExpected, getSchedStaskKey() );
   }


   /**
    * Asserts that the <code>task_defn_db_id:task_defn_id</code> value are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertTaskDefnKey( TaskDefnKey aExpected ) {
      MxAssert.assertEquals( aExpected, getTaskDefnKey() );
   }


   /**
    * {@inheritDoc}
    */
   public IdKey getLrpEventKey() {
      if ( isNull( ColumnName.LRP_EVENT_ID ) ) {
         return null;
      } else {
         return new IdKeyImpl( getInt( ColumnName.LRP_EVENT_DB_ID ),
               getInt( ColumnName.LRP_EVENT_ID ) );
      }
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
   public IdKey getPrevWorkscopeKey() {
      if ( isNull( ColumnName.PREV_WORKSCOPE_ID ) ) {
         return null;
      } else {
         return new IdKeyImpl( getInt( ColumnName.PREV_WORKSCOPE_DB_ID ),
               getInt( ColumnName.PREV_WORKSCOPE_ID ) );
      }
   }


   /**
    * {@inheritDoc}
    */
   public TaskKey getSchedStaskKey() {
      if ( isNull( ColumnName.SCHED_ID ) ) {
         return null;
      } else {
         return new TaskKey( getInt( ColumnName.SCHED_DB_ID ), getInt( ColumnName.SCHED_ID ) );
      }
   }


   /**
    * {@inheritDoc}
    */
   public TaskDefnKey getTaskDefnKey() {
      if ( isNull( ColumnName.TASK_DEFN_ID ) ) {
         return null;
      } else {
         return new TaskDefnKey( getInt( ColumnName.TASK_DEFN_DB_ID ),
               getInt( ColumnName.TASK_DEFN_ID ) );
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
