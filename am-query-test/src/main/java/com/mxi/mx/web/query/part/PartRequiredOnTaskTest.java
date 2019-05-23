
package com.mxi.mx.web.query.part;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.EqpPartBaselineKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskPartListKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.core.table.eqp.EqpPartBaselineTable;
import com.mxi.mx.core.table.task.JdbcTaskDefnDao;
import com.mxi.mx.core.table.task.TaskDefnDao;
import com.mxi.mx.core.table.task.TaskDefnTable;
import com.mxi.mx.core.table.task.TaskPartList;
import com.mxi.mx.core.table.task.TaskTaskTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Ensures <code>PartRequiredOnTask</code> query functions properly
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class PartRequiredOnTaskTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private static final PartNoKey PART_NO = new PartNoKey( 1, 1 );

   private static final TaskDefnKey TASK_DEFN_JIC_1 = new TaskDefnKey( 100, 101 );

   private static final TaskTaskKey TASK_TASK_JIC_SUPRSEDE_1 = new TaskTaskKey( 100, 101 );

   private static final TaskTaskKey TASK_TASK_JIC_ACTV_1 = new TaskTaskKey( 100, 102 );

   private static final TaskTaskKey TASK_TASK_JIC_REVISION_1 = new TaskTaskKey( 100, 103 );

   private static final int SUPRSEDE_REV_JIC_1 = 1;

   private static final int ACTV_REV_JIC_1 = 2;

   private static final int LATEST_REV_JIC_1 = 3;

   private static final TaskDefnKey TASK_DEFN_JIC_2 = new TaskDefnKey( 100, 104 );

   private static final TaskTaskKey TASK_TASK_JIC_SUPRSEDE_2 = new TaskTaskKey( 100, 104 );

   private static final TaskTaskKey TASK_TASK_JIC_ACTV_2 = new TaskTaskKey( 100, 105 );

   private static final int SUPRSEDE_REV_JIC_2 = 1;

   private static final int ACTV_REV_JIC_2 = 2;

   private static final int LATEST_REV_JIC_2 = 2;

   private static final String CONFIG_SLOT_CD = "01-02-03";

   private static final ConfigSlotKey BOM_ITEM = new ConfigSlotKey( 1, "TESTBOM", 1 );

   private static final ConfigSlotPositionKey BOM_ITEM_POSITION =
         new ConfigSlotPositionKey( BOM_ITEM, 1 );

   private static final PartGroupKey BOM_PART = new PartGroupKey( 1, 1 );

   protected Injector iInjector;


   /**
    * Creates the database data
    */
   @Before
   public void loadData() {

      iInjector = Guice.createInjector( new AbstractModule() {

         @Override
         protected void configure() {
            bind( TaskDefnDao.class ).to( JdbcTaskDefnDao.class );
         }
      } );

      iInjector.injectMembers( this );
      InjectorContainer.set( iInjector );

      // Create a TRK config slot
      EqpAssmblBom lEqpAssmblBom = EqpAssmblBom.create( BOM_ITEM );
      lEqpAssmblBom.setConfigClass( RefBOMClassKey.TRK );
      lEqpAssmblBom.insert();

      // update the AIA code for the config slot.
      DataSetArgument lUpdateArgs = new DataSetArgument();
      lUpdateArgs.add( "assmbl_bom_cd", CONFIG_SLOT_CD );

      DataSetArgument lWhereArgs = new DataSetArgument();
      lWhereArgs.add( BOM_ITEM, "assmbl_db_id", "assmbl_cd", "assmbl_bom_id" );

      MxDataAccess.getInstance().executeUpdate( "eqp_assmbl_bom", lUpdateArgs, lWhereArgs );

      // Create a part group for the part PART_NO
      EqpPartBaselineTable lEqpPartBaselineTable =
            EqpPartBaselineTable.create( new EqpPartBaselineKey( BOM_PART, PART_NO ) );
      lEqpPartBaselineTable.setInterchgOrd( 1 );
      lEqpPartBaselineTable.setApprovedBool( true );
      lEqpPartBaselineTable.insert();

      // *********************************************************************
      // Create a JIC and assign part as the part requirement
      // *********************************************************************

      // Create a JIC task definition
      TaskDefnTable lTaskDefn = TaskDefnTable.create( TASK_DEFN_JIC_1 );
      lTaskDefn.setLastRevisionOrd( LATEST_REV_JIC_1 );
      lTaskDefn.insert();

      // JIC REVISION 1: Create a JIC task definition with SUPRSEDE revision
      TaskTaskTable lTaskTask = TaskTaskTable.create();
      lTaskTask.setBomItem( BOM_ITEM_POSITION.getBomItemKey() );
      lTaskTask.setRevisionOrd( SUPRSEDE_REV_JIC_1 );
      lTaskTask.setTaskClass( RefTaskClassKey.JIC );
      lTaskTask.setTaskDefn( TASK_DEFN_JIC_1 );
      lTaskTask.setTaskDefStatus( RefTaskDefinitionStatusKey.SUPRSEDE );
      lTaskTask.insert( TASK_TASK_JIC_SUPRSEDE_1 );

      // Assign part group to JIC rev1 as a part requirement
      TaskPartList lTaskPartList =
            TaskPartList.create( new TaskPartListKey( TASK_TASK_JIC_SUPRSEDE_1, 1 ) );
      lTaskPartList.setBomItemPosition( BOM_ITEM_POSITION );
      lTaskPartList.setBOMPart( BOM_PART );
      lTaskPartList.insert();

      // JIC REVISION 2: Create a JIC task definition with ACTV revision
      lTaskTask = TaskTaskTable.create();
      lTaskTask.setBomItem( BOM_ITEM_POSITION.getBomItemKey() );
      lTaskTask.setRevisionOrd( ACTV_REV_JIC_1 );
      lTaskTask.setTaskClass( RefTaskClassKey.JIC );
      lTaskTask.setTaskDefn( TASK_DEFN_JIC_1 );
      lTaskTask.setTaskDefStatus( RefTaskDefinitionStatusKey.ACTV );
      lTaskTask.insert( TASK_TASK_JIC_ACTV_1 );

      // Assign part group to JIC rev2 as a part requirement
      lTaskPartList = TaskPartList.create( new TaskPartListKey( TASK_TASK_JIC_ACTV_1, 2 ) );
      lTaskPartList.setBomItemPosition( BOM_ITEM_POSITION );
      lTaskPartList.setBOMPart( BOM_PART );
      lTaskPartList.insert();

      // JIC REVISION 3: Create a JIC task definition with REVISION revision
      lTaskTask = TaskTaskTable.create();
      lTaskTask.setBomItem( BOM_ITEM_POSITION.getBomItemKey() );
      lTaskTask.setRevisionOrd( LATEST_REV_JIC_1 );
      lTaskTask.setTaskClass( RefTaskClassKey.JIC );
      lTaskTask.setTaskDefn( TASK_DEFN_JIC_1 );
      lTaskTask.setTaskDefStatus( RefTaskDefinitionStatusKey.REVISION );
      lTaskTask.insert( TASK_TASK_JIC_REVISION_1 );

      // Assign part group to JIC rev3 as a part requirement
      lTaskPartList = TaskPartList.create( new TaskPartListKey( TASK_TASK_JIC_REVISION_1, 3 ) );
      lTaskPartList.setBomItemPosition( BOM_ITEM_POSITION );
      lTaskPartList.setBOMPart( BOM_PART );
      lTaskPartList.insert();

      // *********************************************************************
      // Create a JIC and assign part group as the tool requirement
      // *********************************************************************
      // Create new a JIC task definition
      lTaskDefn = TaskDefnTable.create( TASK_DEFN_JIC_2 );
      lTaskDefn.setLastRevisionOrd( LATEST_REV_JIC_2 );
      lTaskDefn.insert();

      // JIC REVISION 1: Create a JIC with SUPRSEDE revision
      lTaskTask = TaskTaskTable.create();
      lTaskTask.setBomItem( BOM_ITEM_POSITION.getBomItemKey() );
      lTaskTask.setRevisionOrd( SUPRSEDE_REV_JIC_2 );
      lTaskTask.setTaskClass( RefTaskClassKey.JIC );
      lTaskTask.setTaskDefn( TASK_DEFN_JIC_2 );
      lTaskTask.setTaskDefStatus( RefTaskDefinitionStatusKey.SUPRSEDE );
      lTaskTask.insert( TASK_TASK_JIC_SUPRSEDE_2 );

      // Assign part group to JIC rev1 as a tool requirement
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( TASK_TASK_JIC_SUPRSEDE_2, "task_db_id", "task_id" );
      lArgs.add( "task_tool_id", 1 );
      lArgs.add( BOM_PART, "bom_part_db_id", "bom_part_id" );

      MxDataAccess.getInstance().executeInsert( "task_tool_list", lArgs );

      // JIC REVISION 2: Create a JIC task definition with ACTV revision
      lTaskTask = TaskTaskTable.create();
      lTaskTask.setBomItem( BOM_ITEM_POSITION.getBomItemKey() );
      lTaskTask.setRevisionOrd( ACTV_REV_JIC_2 );
      lTaskTask.setTaskClass( RefTaskClassKey.JIC );
      lTaskTask.setTaskDefn( TASK_DEFN_JIC_2 );
      lTaskTask.setTaskDefStatus( RefTaskDefinitionStatusKey.ACTV );
      lTaskTask.insert( TASK_TASK_JIC_ACTV_2 );

      // Assign part group to JIC rev2 as a tool requirement
      lArgs = new DataSetArgument();
      lArgs.add( TASK_TASK_JIC_ACTV_2, "task_db_id", "task_id" );
      lArgs.add( "task_tool_id", 2 );
      lArgs.add( BOM_PART, "bom_part_db_id", "bom_part_id" );

      MxDataAccess.getInstance().executeInsert( "task_tool_list", lArgs );
   }


   /**
    * Setup test scenario for a part where:<br>
    *
    * <ol>
    * <li>Multiple JIC revisions exist</li>
    * </ol>
    * <br>
    * Expected Result: Return only the JIC revision that is currently ACTV<br>
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQuery() throws Exception {
      DataSet lDs = execute();

      MxAssert.assertEquals( "Number of retrieved rows", 2, lDs.getRowCount() );

      lDs.addSort( "dsString(task_key)", true );

      // Part assigned to JIC as part requirement
      lDs.first();

      assertEquals( "task_key", TASK_TASK_JIC_ACTV_1.getDbId() + ":" + TASK_TASK_JIC_ACTV_1.getId(),
            lDs.getString( "task_key" ) );
      assertEquals( "status", "ACTV", lDs.getString( "status" ) );
      assertEquals( "revision_ord", ACTV_REV_JIC_1, lDs.getInt( "revision_ord" ) );
      assertEquals( "assembly_key", BOM_ITEM_POSITION.getCd(), lDs.getString( "assmbl_cd" ) );
      assertEquals( "config_slot", CONFIG_SLOT_CD, lDs.getString( "assmbl_bom_cd" ) );

      // Part assigned to JIC as tool requirement
      lDs.next();

      assertEquals( "task_key", TASK_TASK_JIC_ACTV_2.getDbId() + ":" + TASK_TASK_JIC_ACTV_2.getId(),
            lDs.getString( "task_key" ) );
      assertEquals( "status", "ACTV", lDs.getString( "status" ) );
      assertEquals( "revision_ord", ACTV_REV_JIC_2, lDs.getInt( "revision_ord" ) );
      assertEquals( "assembly_key", BOM_ITEM_POSITION.getCd(), lDs.getString( "assmbl_cd" ) );
      assertEquals( "config_slot", CONFIG_SLOT_CD, lDs.getString( "assmbl_bom_cd" ) );
   }


   /**
    * Execute the query.
    *
    * @return the result
    */
   private DataSet execute() {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( PART_NO, "aPartNoDbId", "aPartNoId" );

      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      return lDs;
   }
}
