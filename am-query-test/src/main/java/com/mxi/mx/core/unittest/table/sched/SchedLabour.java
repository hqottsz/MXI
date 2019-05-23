
package com.mxi.mx.core.unittest.table.sched;

import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.EventKeyInterface;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefLabourStageKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * DOCUMENT_ME
 *
 * @author wleroux
 */
public class SchedLabour extends com.mxi.mx.core.table.sched.SchedLabourTable {

   /**
    * Creates a new SchedLabourKey object for the purpose of getting/updating properties.
    *
    * @param aSchedLabourKey
    */
   public SchedLabour(SchedLabourKey aSchedLabourKey) {
      super( aSchedLabourKey );
   }


   /**
    * Creates a new {@linkplain SchedLabour} object.
    */
   protected SchedLabour() {
      super();
   }


   /**
    * Count the number of labour rows in the 'sched_labour' table.
    *
    * @param aEvent
    *           The task
    *
    * @return int representing the number of labour rows.
    */
   public static int countLabours( EventKeyInterface aEvent ) {

      // Query return value
      String[] lQuery = new String[] { "count(*) as COUNT" };

      // Obtain actual value
      DataSetArgument lWhereClause = new DataSetArgument();
      lWhereClause.add( aEvent.getEventKey(), ColumnName.SCHED_DB_ID.toString(),
            ColumnName.SCHED_ID.toString() );

      // Execute the query
      DataSet lReturn = MxDataAccess.getInstance().executeQuery( lQuery, SchedLabourKey.TABLE_NAME,
            lWhereClause );

      // Return the count
      return lReturn.getIntAt( 1, "COUNT" );
   }


   /**
    * Create an instance of SchedLabour for the purpose of getting and updating its properties.
    *
    * @param aEvent
    *           The Event Foreign Key
    * @param aLabourSkillKey
    *           The Labour Skill Foreign Key
    *
    * @return The new object.
    */
   public static SchedLabour findByForeignKey( EventKey aEvent,
         RefLabourSkillKey aLabourSkillKey ) {

      // Fetch the labour data
      DataSetArgument lTaskArgs = new DataSetArgument();

      // task foreign key
      lTaskArgs.add( aEvent, ColumnName.SCHED_DB_ID.toString(), ColumnName.SCHED_ID.toString() );

      // labour skill foreign key
      lTaskArgs.add( aLabourSkillKey, ColumnName.LABOUR_SKILL_DB_ID.toString(),
            ColumnName.LABOUR_SKILL_CD.toString() );

      // get the rows
      DataSet lDataSet = MxDataAccess.getInstance().executeQuery( SchedLabourKey.TABLE_NAME,
            lTaskArgs, ColumnName.LABOUR_DB_ID.toString(), ColumnName.LABOUR_ID.toString() );

      SchedLabourKey lLabourKey = null;
      if ( !lDataSet.isEmpty() ) {
         lDataSet.first();
         lLabourKey = lDataSet.getKey( SchedLabourKey.class, ColumnName.LABOUR_DB_ID.toString(),
               ColumnName.LABOUR_ID.toString() );
      }

      if ( lLabourKey != null ) {
         return new SchedLabour( lLabourKey );
      } else {
         return new SchedLabour();
      }
   }


   /**
    * Create an instance of SchedLabour for the purpose of getting and updating its properties.
    *
    * @param aSchedLabourKey
    *
    * @return The new object.
    */
   public static SchedLabour findByPrimaryKey( SchedLabourKey aSchedLabourKey ) {
      return new SchedLabour( aSchedLabourKey );
   }


   /**
    * Asserts the certification property.
    *
    * @param aValue
    *           the expected value for the certification property.
    */
   public void assertCertification( boolean aValue ) {
      MxAssert.assertEquals( aValue, isCertificationBool() );
   }


   /**
    * Asserts the current status order property.
    *
    * @param aValue
    *           the expected value for the current status order property.
    */
   public void assertCurrentStatusOrder( int aValue ) {
      MxAssert.assertEquals( aValue, getCurrentStatusOrder() );
   }


   /**
    * Asserts that the primary key does not exists in the table
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
    * Asserts the independent inspection property.
    *
    * @param aValue
    *           the expected value for the independent inspection property.
    */
   public void assertIndependentInspection( boolean aValue ) {
      MxAssert.assertEquals( aValue, isIndependentInspectionBool() );
   }


   /**
    * Asserts the ref labour skill property.
    *
    * @param aValue
    *           the expected value for ref labour skill property.
    */
   public void assertLabourSkill( RefLabourSkillKey aValue ) {
      MxAssert.assertEquals( aValue, getLabourSkill() );
   }


   /**
    * Asserts the ref labour stage property.
    *
    * @param aValue
    *           the expected value for ref labour stage property.
    */
   public void assertLabourStage( RefLabourStageKey aValue ) {
      MxAssert.assertEquals( aValue, getLabourStage() );
   }


   /**
    * Asserts the ref task property.
    *
    * @param aValue
    *           the expected value for task property.
    */
   public void assertTask( TaskKey aValue ) {
      MxAssert.assertEquals( aValue, getTask() );
   }


   /**
    * Asserts the performed property.
    *
    * @param aValue
    *           the expected value for the performed property.
    */
   public void assertWorkPerformed( boolean aValue ) {
      MxAssert.assertEquals( aValue, isWorkPerformedBool() );
   }
}
