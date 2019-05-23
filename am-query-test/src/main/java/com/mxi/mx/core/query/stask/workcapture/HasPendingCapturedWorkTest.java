
package com.mxi.mx.core.query.stask.workcapture;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.key.MxDbKey;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.InventoryParmDataKey;
import com.mxi.mx.core.key.RefLabourRoleTypeKey;
import com.mxi.mx.core.key.SchedActionKey;
import com.mxi.mx.core.key.SchedLabourActionKey;
import com.mxi.mx.core.key.SchedLabourInstPartKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.SchedLabourPanelKey;
import com.mxi.mx.core.key.SchedLabourParmDataKey;
import com.mxi.mx.core.key.SchedLabourRmvdPartKey;
import com.mxi.mx.core.key.SchedLabourRoleKey;
import com.mxi.mx.core.key.SchedLabourToolKey;
import com.mxi.mx.core.key.SchedPanelKey;
import com.mxi.mx.core.key.TaskInstPartKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskRmvdPartKey;
import com.mxi.mx.core.key.ToolKey;
import com.mxi.mx.core.table.sched.SchedLabourActionTable;
import com.mxi.mx.core.table.sched.SchedLabourRoleTable;
import com.mxi.mx.core.table.sched.SchedLabourTable;


/**
 * Ensures that hasPendingCaptureWork query returns a row when there is unsigned work captured.
 */
@RunWith( Theories.class )
public final class HasPendingCapturedWorkTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }


   @DataPoint
   public static final RefLabourRoleTypeKey CERT = RefLabourRoleTypeKey.CERT;
   @DataPoint
   public static final RefLabourRoleTypeKey INSP = RefLabourRoleTypeKey.INSP;
   @DataPoint
   public static final RefLabourRoleTypeKey TECH = RefLabourRoleTypeKey.TECH;

   @DataPoint
   public static final TaskKey TASK_WITH_INCOMPLETE_TECH = new TaskKey( 4650, 1 );
   private static final SchedLabourKey LABOUR_WITH_INCOMPLETE_TECH = new SchedLabourKey( 4650, 1 );

   @DataPoint
   public static final TaskKey TASK_WITH_COMPLETE_TECH = new TaskKey( 4650, 2 );
   private static final SchedLabourKey LABOUR_WITH_COMPLETE_TECH = new SchedLabourKey( 4650, 2 );

   @DataPoint
   public static final TaskKey TASK_WITH_ALL_COMPLETE = new TaskKey( 4650, 3 );
   private static final SchedLabourKey LABOUR_WITH_ALL_COMPLETE = new SchedLabourKey( 4650, 3 );

   private static final TaskKey TASK = TASK_WITH_INCOMPLETE_TECH;
   private static final SchedLabourKey LABOUR = LABOUR_WITH_INCOMPLETE_TECH;

   @DataPoints
   public static final MxDbKey[] TECH_WORK_CAPTURE =
         new MxDbKey[] { new SchedLabourInstPartKey( LABOUR, new TaskInstPartKey( 4650, 1, 1, 1 ) ),
               new SchedLabourRmvdPartKey( LABOUR, new TaskRmvdPartKey( 4650, 1, 1, 2 ) ),
               new SchedLabourToolKey( LABOUR, new ToolKey( 4650, 1, 1 ) ),
               new SchedLabourPanelKey( LABOUR, new SchedPanelKey( TASK, 1 ) ),
               new SchedLabourParmDataKey( LABOUR,
                     new InventoryParmDataKey( 4650, 1, 1, 0, 1, 4650, 1 ) ) };


   /**
    * Ensure that no captured work returns FALSE
    *
    * @param aTask
    *           the task
    */
   @Theory
   @Test
   public void testNoUnsignedWorkReturnsFalse( TaskKey aTask ) {
      assertFalse( hasUnsignedWorkCaptured( aTask ) );
   }


   /**
    * Ensure unsigned action note return true
    *
    * @param aRole
    *           the labour role
    */
   @Theory
   @Test
   public void testSignedActionNoteReturnsFalse( RefLabourRoleTypeKey aRole ) {
      addActionNote( LABOUR_WITH_ALL_COMPLETE, aRole );

      assertFalse( hasUnsignedWorkCaptured( TASK_WITH_ALL_COMPLETE ) );
   }


   /**
    * Ensure signed work capture return false
    *
    * @param aRole
    *           the labour role
    * @param aWorkKey
    *           the work key
    */
   @Theory
   @Test
   public void testTechWorkCaptureWhenSignedTechReturnsFalse( RefLabourRoleTypeKey aRole,
         MxDbKey aWorkKey ) {
      assumeThat( aWorkKey, isIn( TECH_WORK_CAPTURE ) );
      assumeThat( aRole, is( not( equalTo( TECH ) ) ) );

      addWorkCaptured( LABOUR_WITH_COMPLETE_TECH, aWorkKey );

      assertFalse( hasUnsignedWorkCaptured( TASK_WITH_COMPLETE_TECH ) );
   }


   /**
    * Ensure unsigned work capture return true
    *
    * @param aRole
    *           the labour role
    * @param aWorkKey
    *           the work key
    */
   @Theory
   @Test
   public void testTechWorkCaptureWhenUnsignedTechReturnsTrue( RefLabourRoleTypeKey aRole,
         MxDbKey aWorkKey ) {
      assumeThat( aWorkKey, isIn( TECH_WORK_CAPTURE ) );

      addWorkCaptured( LABOUR_WITH_INCOMPLETE_TECH, aWorkKey );

      assertTrue( hasUnsignedWorkCaptured( TASK_WITH_INCOMPLETE_TECH ) );
   }


   /**
    * Ensure unsigned action note return true
    *
    * @param aRole
    *           the labour role
    */
   @Theory
   @Test
   public void testUnsignedActionNoteReturnsTrue( RefLabourRoleTypeKey aRole ) {
      addActionNote( LABOUR_WITH_INCOMPLETE_TECH, aRole );

      assertTrue( hasUnsignedWorkCaptured( TASK_WITH_INCOMPLETE_TECH ) );
   }


   /**
    * Adds an action note
    *
    * @param aLabourKey
    *           the labour requirement
    * @param aRoleType
    *           the role type
    */
   private void addActionNote( SchedLabourKey aLabourKey, RefLabourRoleTypeKey aRoleType ) {
      TaskKey lTask = SchedLabourTable.findByPrimaryKey( aLabourKey ).getTask();

      SchedLabourRoleKey lRoleKey =
            SchedLabourRoleTable.findByForeignKey( aLabourKey, aRoleType ).getPk();

      SchedLabourActionTable lTable = SchedLabourActionTable
            .create( new SchedLabourActionKey( lRoleKey, new SchedActionKey( lTask, 1 ) ) );
      lTable.insert();
   }


   /**
    * Adds work capture to a labour requirement
    *
    * @param aLabour
    *           the labour requirement
    * @param aWorkKey
    *           the table that stores the work
    */
   private void addWorkCaptured( SchedLabourKey aLabour, MxDbKey aWorkKey ) {

      // Insert key
      MxDataAccess.getInstance().executeInsert( aWorkKey.getTableName(), aWorkKey.getPKWhereArg() );

      // Replace the labour_key with the one provided
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aLabour, "labour_db_id", "labour_id" );
      MxDataAccess.getInstance().executeUpdate( aWorkKey.getTableName(), lArgs,
            aWorkKey.getPKWhereArg() );
   }


   /**
    * Returns TRUE when the task has unsigned work
    *
    * @param aTask
    *           the task
    *
    * @return TRUE if unsigned work
    */
   private boolean hasUnsignedWorkCaptured( TaskKey aTask ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, "aTaskDbId", "aTaskId" );

      QuerySet lQs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.stask.workcapture.hasPendingCapturedWork", lArgs );

      return lQs.first();
   }
}
