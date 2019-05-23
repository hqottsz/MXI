
package com.mxi.mx.core.unittest.table.sched;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.PanelKey;
import com.mxi.mx.core.key.SchedPanelKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class is used to check values in the <code>sched_panel</code> table.
 *
 * @author jprimeau
 * @created September 26, 2005
 */
public class SchedPanel extends com.mxi.mx.core.table.sched.SchedPanel {

   /**
    * Initializes the class.
    *
    * @param aSchedPanelKey
    *           Primary key for the table.
    */
   public SchedPanel(SchedPanelKey aSchedPanelKey) {
      super( aSchedPanelKey );
   }


   /**
    * Finds a sched_panel row by task and panel.
    *
    * @param aTask
    *           The task
    * @param aPanel
    *           The panel
    *
    * @return The sched panel row.
    */
   public static SchedPanel findByTaskAndPanel( TaskKey aTask, PanelKey aPanel ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, "sched_db_id", "sched_id" );
      lArgs.add( aPanel, "panel_db_id", "panel_id" );

      QuerySet lResult = QuerySetFactory.getInstance()
            .executeQuery( new String[] { "sched_panel_id" }, "sched_panel", lArgs );

      if ( lResult.first() ) {
         return new SchedPanel( new SchedPanelKey( aTask, lResult.getInt( "sched_panel_id" ) ) );
      }

      return null;
   }


   /**
    * Assert row with <code>cls_mpc_sched_db_id:cls_mpc_sched_id:cls_mpc_sched_panel_id</code> is of
    * expected.
    *
    * @param aClosePanel
    *           close mpc sched panel
    */
   public void assertClosePanel( SchedPanelKey aClosePanel ) {
      MxAssert.assertEquals( "ClosePanel", aClosePanel, getClsMpcSched() );
   }


   /**
    * Asserts that the row with <code>sched_db_id:sched_id:sched_panel_id</code> does not exist in
    * table.
    */
   public void assertDoesNotExist() {
      MxAssert.assertFalse( "The sched_panel table has the row.", exists() );
   }


   /**
    * Asserts that the row with <code>sched_db_id:sched_id:sched_panel_id</code> exists in the
    * table.
    */
   public void assertExist() {
      MxAssert.assertTrue( "The sched_panel table does not have the row.", exists() );
   }


   /**
    * Assert row with <code>opn_mpc_sched_db_id:opn_mpc_sched_id:opn_mpc_sched_panel_id</code> is of
    * expected.
    *
    * @param aOpenPanel
    *           open mpc sched panel
    */
   public void assertOpenPanel( SchedPanelKey aOpenPanel ) {
      MxAssert.assertEquals( "OpenPanel", aOpenPanel, getOpnMpcSched() );
   }


   /**
    * Assert the value of the <panel_db_id:panel_id > columns.
    *
    * @param aPanel
    *           Type Code.
    */
   public void assertPanel( PanelKey aPanel ) {
      MxAssert.assertEquals( "Panel", aPanel, getPanel() );
   }
}
