
package com.mxi.mx.core.unittest.table.stask;

import java.util.ArrayList;
import java.util.List;

import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefReqActionKey;
import com.mxi.mx.core.key.RefSchedPartStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.table.sched.SchedPartTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class is used to check values in the <code>sched_part</code> table.
 *
 * @author asmolko
 * @created March 18, 2002
 */
public class SchedPart extends SchedPartTable {

   /**
    * Initializes the class.
    *
    * @param aTaskPartKey
    *           primary key of the table.
    */
   public SchedPart(TaskPartKey aTaskPartKey) {
      super( aTaskPartKey );
   }


   /**
    * Creates a new SchedPart object.
    *
    * @param aEvent
    *           The task's event
    *
    * @return The list of sched_part objects.
    */
   public static List<SchedPart> findByEvent( EventKey aEvent ) {
      return findByTask( new TaskKey( aEvent ) );
   }


   /**
    * Creates a new SchedPart object.
    *
    * @param aTask
    *           The task
    *
    * @return The list of sched_part objects.
    */
   public static List<SchedPart> findByTask( TaskKey aTask ) {
      List<SchedPart> lSchedPartList = new ArrayList<SchedPart>();

      String[] lCols = new String[] { "sched_part_id" };

      // Obtain actual values
      DataSetArgument lWhereClause = new DataSetArgument();
      lWhereClause.add( "sched_db_id", aTask.getDbId() );
      lWhereClause.add( "sched_id", aTask.getId() );

      QuerySet lResult =
            QuerySetFactory.getInstance().executeQuery( lCols, "sched_part", lWhereClause );
      while ( lResult.next() ) {
         lSchedPartList
               .add( new SchedPart( new TaskPartKey( aTask, lResult.getInt( "sched_part_id" ) ) ) );
      }

      return lSchedPartList;
   }


   /**
    * Returns the SchedBomPart property of the SchedPart.
    *
    * @param aTaskPartKey
    *           Description of Parameter.
    *
    * @return The SchedBomPart value.
    */
   public static PartGroupKey getSchedBomPart( TaskPartKey aTaskPartKey ) {
      Integer lSchedBomPartDbId;
      Integer lSchedBomPartId;

      PartGroupKey lPartGroupKey = null;
      DataSetArgument lWhereClause;

      String[] lCols = { "sched_bom_part_db_id", "sched_bom_part_id" };

      // Obtain actual value
      lWhereClause = new DataSetArgument();
      lWhereClause.add( "sched_db_id", aTaskPartKey.getDbId() );
      lWhereClause.add( "sched_id", aTaskPartKey.getId() );
      lWhereClause.add( "sched_part_id", aTaskPartKey.getSchedPartId() );

      DataSet lDataSet =
            MxDataAccess.getInstance().executeQuery( lCols, "sched_part", lWhereClause );

      if ( !lDataSet.next() ) {

         return null;
      }

      // extract values
      lSchedBomPartDbId = lDataSet.getIntegerAt( 1, "sched_bom_part_db_id" );
      lSchedBomPartId = lDataSet.getIntegerAt( 1, "sched_bom_part_id" );

      if ( ( lSchedBomPartDbId != null ) && ( lSchedBomPartId != null ) ) {
         lPartGroupKey =
               new PartGroupKey( lSchedBomPartDbId.intValue(), lSchedBomPartId.intValue() );
      } else {
         lPartGroupKey = null;
      }

      return lPartGroupKey;
   }


   /**
    * Returns the list of SchedBomPart key of the SchedPart.
    *
    * @param aSchedParts
    *           The list of sched part rows
    *
    * @return The list of SchedBomPart key.
    */
   public static List<PartGroupKey> getSchedBomParts( List<SchedPart> aSchedParts ) {
      List<PartGroupKey> lBomPartKeys = new ArrayList<PartGroupKey>();

      for ( int i = 1; i < aSchedParts.size(); i++ ) {

         if ( aSchedParts.get( i ).getSchedBomPartKey() != null ) {
            lBomPartKeys.add( aSchedParts.get( i ).getSchedBomPartKey() );
         }
      }

      return lBomPartKeys;
   }


   /**
    * Returns the SchedQt property of the SchedPart.
    *
    * @param aTaskPartKey
    *           Description of Parameter.
    *
    * @return The SchedQt value.
    */
   public static double getSchedQt( TaskPartKey aTaskPartKey ) {
      double lSchedQt;

      DataSetArgument lWhereClause;

      String[] lCols = { "sched_qt" };

      // Obtain actual value
      lWhereClause = new DataSetArgument();
      lWhereClause.add( "sched_db_id", aTaskPartKey.getDbId() );
      lWhereClause.add( "sched_id", aTaskPartKey.getId() );
      lWhereClause.add( "sched_part_id", aTaskPartKey.getSchedPartId() );

      DataSet lDataSet =
            MxDataAccess.getInstance().executeQuery( lCols, "sched_part", lWhereClause );

      // extract values
      lSchedQt = lDataSet.getDoubleAt( 1, "sched_qt" );

      return lSchedQt;
   }


   /**
    * Returns the SpecPartNo property of the SchedPart.
    *
    * @param aTaskPartKey
    *           PK of entity
    *
    * @return The SpecPart value.
    */
   public static PartNoKey getSpecPartNo( TaskPartKey aTaskPartKey ) {
      Integer lSpecPartNoDbId;
      Integer lSpecPartNoId;

      PartNoKey lPartNoKey = null;
      DataSetArgument lWhereClause;

      String[] lCols = { "spec_part_no_db_id", "spec_part_no_id" };

      // Obtain actual value
      lWhereClause = new DataSetArgument();
      lWhereClause.add( "sched_db_id", aTaskPartKey.getDbId() );
      lWhereClause.add( "sched_id", aTaskPartKey.getId() );
      lWhereClause.add( "sched_part_id", aTaskPartKey.getSchedPartId() );

      DataSet lDataSet =
            MxDataAccess.getInstance().executeQuery( lCols, "sched_part", lWhereClause );

      if ( !lDataSet.next() ) {

         return null;
      }

      // extract values
      lSpecPartNoDbId = lDataSet.getIntegerAt( 1, "spec_part_no_db_id" );
      lSpecPartNoId = lDataSet.getIntegerAt( 1, "spec_part_no_id" );

      if ( ( lSpecPartNoDbId != null ) && ( lSpecPartNoId != null ) ) {
         lPartNoKey = new PartNoKey( lSpecPartNoDbId.intValue(), lSpecPartNoId.intValue() );
      } else {
         lPartNoKey = null;
      }

      return lPartNoKey;
   }


   /**
    * Look up first removed inventory from the sched part table.
    *
    * @param aTask
    *           aTask for which the first removed inventory will be returned.
    *
    * @return first returned inventory.
    */
   public static InventoryKey lookupFirstRemovedInventory( TaskKey aTask ) {

      // cols to select
      String[] lCols = { "inv_no_db_id", "inv_no_id" };

      DataSetArgument lWhereClause = new DataSetArgument();
      lWhereClause.add( "sched_db_id", aTask.getDbId() );
      lWhereClause.add( "sched_id", aTask.getId() );
      lWhereClause.add( "sched_part_id", 1 );

      // execute query
      DataSet lDataSet =
            MxDataAccess.getInstance().executeQuery( lCols, "sched_rmvd_part", lWhereClause );

      return new InventoryKey( lDataSet.getIntAt( 1, "inv_no_db_id" ),
            lDataSet.getIntAt( 1, "inv_no_id" ) );
   }


   /**
    * Look up new inventory from the inv_inv table.
    *
    * @return first returned inventory.
    */
   public static InventoryKey lookupNewInventory() {

      // return data from query.
      DataSet lMaxColDataSet = null;

      // string that determines the column that will be selected
      String lColumnToSelect;
      lColumnToSelect = "max(inv_no_id) as MAX_VAL";

      // colums to select when obtaining value
      String[] lMaxColName = new String[1];
      lMaxColName[0] = lColumnToSelect;

      // database query to get new ID value for new value
      lMaxColDataSet = MxDataAccess.getInstance().executeQuery( lMaxColName, "inv_inv",
            new DataSetArgument() );

      return new InventoryKey( 4650, lMaxColDataSet.getIntAt( 1, "MAX_VAL" ) );
   }


   /**
    * Asserts that the <code>"assmbl_db_id","assmbl_cd","assmbl_bom_id", "assmbl_pos_id"</code>
    * column for the <code>aLabour</code> primary key and <code>aExpected</code> are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertConfigSlotPosition( ConfigSlotPositionKey aExpected ) {

      MxAssert.assertEquals( "assmbl_db_id:assmbl_cd:assmbl_bom_id:assmbl_pos_id", aExpected,
            getConfigSlotPosition() );
   }


   /**
    * Asserts that the row with <code>event_db_id:event_id</code> = <code>iEvent</code> does not
    * exists in the table.
    */
   public void assertDoesNotExist() {
      MxAssert.assertFalse( "Should not exist", exists() );
   }


   /**
    * Asserts that the row with <code>event_db_id:event_id</code> = <code>iEvent</code> exists in
    * the table.
    */
   public void assertExist() {
      MxAssert.assertTrue( "Should exist", exists() );
   }


   /**
    * asserts the bom item position key
    *
    * @param aExpected
    *           expected value
    */
   public void assertNHBomItemPositionKey( ConfigSlotPositionKey aExpected ) {
      MxAssert.assertEquals( "Next Highest Position", aExpected, getNhBomPositionKey() );
   }


   /**
    * Asserts that the <code>part_note</code> column in the table is equal to the <code>
    * aExpected</code> parameter.
    *
    * @param aExpected
    *           expected value of the column.
    */
   public void assertPartNote( String aExpected ) {
      MxAssert.assertEquals( "part_note", aExpected, getPartNote() );
   }


   /**
    * Asserts that the <code>part_note</code> column in the table is equal to the <code>
    * aExpected</code> parameter.
    *
    * @param aExpected
    *           expected value of the column.
    */
   public void assertPartPosition( int aExpected ) {
      MxAssert.assertEquals( "assmbl_pos_id", aExpected, getInt( ColumnName.ASSMBL_POS_ID ) );
   }


   /**
    * Assert the part status
    *
    * @param aExpected
    *           The expected part status
    */
   public void assertPartStatus( RefSchedPartStatusKey aExpected ) {

      // Assert that the actual and expected match
      MxAssert.assertEquals( "part_status", aExpected, getPartStatus() );
   }


   /**
    * assert the request action key
    *
    * @param aExpected
    *           request action key
    */
   public void assertRequestAction( RefReqActionKey aExpected ) {
      MxAssert.assertEquals( "Request Action", aExpected, getRequestAction() );
   }


   /**
    * Asserts that the <code>"sched_bom_part_db_id", "sched_bom_part_id"</code> column for the
    * <code>
    * aTaskPart</code> primary key and <code>aExpected</code> are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertSchedBom( PartGroupKey aExpected ) {
      MxAssert.assertEquals( "sched_bom_part", aExpected, getSchedBomPartKey() );
   }


   /**
    * Asserts that the <code>sched_qt</code> column in the table is equal to the <code>
    * aExpected</code> parameter.
    *
    * @param aExpected
    *           expected value of the column.
    */
   public void assertSchedQt( Double aExpected ) {
      MxAssert.assertEquals( "sched_qt", aExpected, getSchedQt() );
   }


   /**
    * Asserts that the <code>spec_part_no_db_id:spec_part_no_id</code> column in the table is equal
    * to the <code>aExpected</code> parameter.
    *
    * @param aExpected
    *           expected value of the column.
    */
   public void assertSpecificPart( PartNoKey aExpected ) {
      MxAssert.assertEquals( "specific part", aExpected, getSpecificPartNoKey() );
   }
}
