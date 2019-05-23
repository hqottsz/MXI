
package com.mxi.mx.web.unittest.report;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.task.TaskTaskTable;
import com.mxi.mx.web.report.compliance.RefDocComplianceReport;


/**
 * Tests {@link RefDocComplianceReport}
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class RefDocComplianceReportTest extends ComplianceReportTestCase {

   /**
    * This scenario ensures that when a requirement complies to a reference document, that the
    * requirement is displayed.
    */
   @Test
   public void testBasic() {

      // Reference Documents
      TaskTaskKey lTaskRevision_Ref1 = new TaskRevisionBuilder().withTaskClass( REF )
            .withTaskCode( "REF_1" ).withConfigSlot( iConfigSlot_ACFT ).build();
      TaskDefnKey lTaskDefn_Ref1 =
            TaskTaskTable.findByPrimaryKey( lTaskRevision_Ref1 ).getTaskDefn();

      // Requirements
      TaskTaskKey lTaskRevision_Req =
            new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ ).withTaskCode( "REQ_1" )
                  .withConfigSlot( iConfigSlot_ACFT ).compliesWith( lTaskDefn_Ref1 ).build();

      // Initialized Tasks
      TaskKey lLastTask = new TaskBuilder().onInventory( iAcftInventory )
            .withTaskRevision( lTaskRevision_Req ).asHistoric( RefEventStatusKey.COMPLETE ).build();
      TaskKey lNextTask = new TaskBuilder().onInventory( iAcftInventory )
            .withTaskRevision( lTaskRevision_Req ).withPrevTask( lLastTask ).build();

      RefDocComplianceReport lReport =
            new RefDocComplianceReport( lTaskRevision_Ref1, lTaskDefn_Ref1, iHr );
      lReport.generate();

      DataSet lDataSet = lReport.getDataSet();

      assertRow( lDataSet, iAcftInventory, lTaskRevision_Ref1, null, null );
      assertRow( lDataSet, iAcftInventory, lTaskRevision_Req, lLastTask, lNextTask );
      Assert.assertFalse( lDataSet.next() );
   }


   /**
    * This scenario ensures that all levels of references are displayed
    */
   @Test
   public void testMultipleLevels() {

      // Reference Documents
      TaskTaskKey lTaskRevision_Ref1 = new TaskRevisionBuilder().withTaskClass( REF )
            .withTaskCode( "REF_1" ).withConfigSlot( iConfigSlot_ACFT ).build();
      TaskDefnKey lTaskDefn_Ref1 =
            TaskTaskTable.findByPrimaryKey( lTaskRevision_Ref1 ).getTaskDefn();

      TaskTaskKey lTaskRevision_Ref2 =
            new TaskRevisionBuilder().withTaskClass( REF ).withTaskCode( "REF_2" )
                  .compliesWith( lTaskDefn_Ref1 ).withConfigSlot( iConfigSlot_ACFT ).build();
      TaskDefnKey lTaskDefn_Ref2 =
            TaskTaskTable.findByPrimaryKey( lTaskRevision_Ref2 ).getTaskDefn();

      // Requirements
      TaskTaskKey lTaskRevision_Req =
            new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ ).withTaskCode( "REQ_1" )
                  .withConfigSlot( iConfigSlot_ACFT ).compliesWith( lTaskDefn_Ref2 ).build();

      // Initialize Tasks
      new TaskBuilder().onInventory( iAcftInventory ).withTaskRevision( lTaskRevision_Ref1 )
            .build();
      new TaskBuilder().onInventory( iAcftInventory ).withTaskRevision( lTaskRevision_Ref2 )
            .build();

      RefDocComplianceReport lReport =
            new RefDocComplianceReport( lTaskRevision_Ref1, lTaskDefn_Ref1, iHr );
      lReport.generate();

      DataSet lDataSet = lReport.getDataSet();
      assertRow( lDataSet, iAcftInventory, lTaskRevision_Ref1, null, null );
      assertRow( lDataSet, iAcftInventory, lTaskRevision_Ref2, null, null );
      assertRow( lDataSet, iAcftInventory, lTaskRevision_Req, null, null );
      Assert.assertFalse( lDataSet.next() );
   }


   /**
    * This scenario ensures that when a task complies to multiple references, all task rows will
    * have the proper last task and next task information
    */
   @Test
   public void testMultipleReferences() {

      // Reference Documents
      TaskTaskKey lTaskRevision_Ref1 = new TaskRevisionBuilder().withTaskClass( REF )
            .withTaskCode( "REF_1" ).withConfigSlot( iConfigSlot_ACFT ).build();
      TaskDefnKey lTaskDefn_Ref1 =
            TaskTaskTable.findByPrimaryKey( lTaskRevision_Ref1 ).getTaskDefn();

      TaskTaskKey lTaskRevision_Ref2 =
            new TaskRevisionBuilder().withTaskClass( REF ).withTaskCode( "REF_2" )
                  .compliesWith( lTaskDefn_Ref1 ).withConfigSlot( iConfigSlot_ACFT ).build();
      TaskDefnKey lTaskDefn_Ref2 =
            TaskTaskTable.findByPrimaryKey( lTaskRevision_Ref2 ).getTaskDefn();

      TaskTaskKey lTaskRevision_Ref3 =
            new TaskRevisionBuilder().withTaskClass( REF ).withTaskCode( "REF_3" )
                  .compliesWith( lTaskDefn_Ref1 ).withConfigSlot( iConfigSlot_ACFT ).build();
      TaskDefnKey lTaskDefn_Ref3 =
            TaskTaskTable.findByPrimaryKey( lTaskRevision_Ref3 ).getTaskDefn();

      // Requirements
      TaskTaskKey lTaskRevision_Req = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withConfigSlot( iConfigSlot_ACFT ).withTaskCode( "REQ_1" )
            .compliesWith( lTaskDefn_Ref2 ).compliesWith( lTaskDefn_Ref3 ).build();

      // Initialized Tasks
      new TaskBuilder().onInventory( iAcftInventory ).withTaskRevision( lTaskRevision_Ref1 )
            .build();
      new TaskBuilder().onInventory( iAcftInventory ).withTaskRevision( lTaskRevision_Ref2 )
            .build();
      new TaskBuilder().onInventory( iAcftInventory ).withTaskRevision( lTaskRevision_Ref3 )
            .build();

      TaskKey lLastTask = new TaskBuilder().onInventory( iAcftInventory )
            .withTaskRevision( lTaskRevision_Req ).asHistoric( RefEventStatusKey.COMPLETE ).build();
      TaskKey lNextTask = new TaskBuilder().onInventory( iAcftInventory )
            .withTaskRevision( lTaskRevision_Req ).withPrevTask( lLastTask ).build();

      // Generate Report
      RefDocComplianceReport lReport =
            new RefDocComplianceReport( lTaskRevision_Ref1, lTaskDefn_Ref1, iHr );
      lReport.generate();

      DataSet lDataSet = lReport.getDataSet();

      assertRow( lDataSet, iAcftInventory, lTaskRevision_Ref1, null, null );
      assertRow( lDataSet, iAcftInventory, lTaskRevision_Ref2, null, null );
      assertRow( lDataSet, iAcftInventory, lTaskRevision_Req, lLastTask, lNextTask );
      assertRow( lDataSet, iAcftInventory, lTaskRevision_Ref3, null, null );
      assertRow( lDataSet, iAcftInventory, lTaskRevision_Req, lLastTask, lNextTask );
      Assert.assertFalse( lDataSet.next() );
   }


   /**
    * Assembly slots have a SUBASSY and ASSY component to them. This test ensures that the
    * requirement is displayed only once.
    */
   @Test
   public void testSubassemblyConfigSlot_ListedOnce() {

      // Reference Documents
      TaskTaskKey lTaskRevision_Ref1 = new TaskRevisionBuilder().withTaskClass( REF )
            .withTaskCode( "REF_1" ).withConfigSlot( iConfigSlot_ASSY ).build();
      TaskDefnKey lTaskDefn_Ref1 =
            TaskTaskTable.findByPrimaryKey( lTaskRevision_Ref1 ).getTaskDefn();

      // Requirements
      TaskTaskKey lTaskRevision_Req = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withConfigSlot( iConfigSlot_ASSY ).withTaskCode( "REQ_1" )
            .compliesWith( lTaskDefn_Ref1 ).build();

      // Initialized Tasks
      new TaskBuilder().onInventory( iAssyInventory_Pos_1 ).withTaskRevision( lTaskRevision_Ref1 )
            .build();

      TaskKey lLastTask_Pos_1 = new TaskBuilder().onInventory( iAssyInventory_Pos_1 )
            .withTaskRevision( lTaskRevision_Req ).asHistoric( RefEventStatusKey.COMPLETE ).build();
      TaskKey lNextTask_Pos_1 = new TaskBuilder().onInventory( iAssyInventory_Pos_1 )
            .withTaskRevision( lTaskRevision_Req ).withPrevTask( lLastTask_Pos_1 ).build();

      new TaskBuilder().onInventory( iAssyInventory_Pos_2 ).withTaskRevision( lTaskRevision_Ref1 )
            .build();

      TaskKey lLastTask_Pos_2 = new TaskBuilder().onInventory( iAssyInventory_Pos_2 )
            .withTaskRevision( lTaskRevision_Req ).asHistoric( RefEventStatusKey.COMPLETE ).build();
      TaskKey lNextTask_Pos_2 = new TaskBuilder().onInventory( iAssyInventory_Pos_2 )
            .withTaskRevision( lTaskRevision_Req ).withPrevTask( lLastTask_Pos_2 ).build();

      // Generate Report
      RefDocComplianceReport lReport =
            new RefDocComplianceReport( lTaskRevision_Ref1, lTaskDefn_Ref1, iHr );
      lReport.generate();

      DataSet lDataSet = lReport.getDataSet();

      assertRow( lDataSet, iAssyInventory_Pos_1, lTaskRevision_Ref1 );
      assertRow( lDataSet, iAssyInventory_Pos_1, lTaskRevision_Req, lLastTask_Pos_1,
            lNextTask_Pos_1 );
      assertRow( lDataSet, iAssyInventory_Pos_2, lTaskRevision_Ref1 );
      assertRow( lDataSet, iAssyInventory_Pos_2, lTaskRevision_Req, lLastTask_Pos_2,
            lNextTask_Pos_2 );
      Assert.assertFalse( lDataSet.next() );
   }


   /**
    * Ensure that sub-assembly configuration slots are displayed properly
    */
   @Test
   public void testSubassemblyConfigSlot_MultiplePositions() {

      // Reference Documents
      TaskTaskKey lTaskRevision_Ref1 = new TaskRevisionBuilder().withTaskClass( REF )
            .withTaskCode( "REF_1" ).withConfigSlot( iConfigSlot_SUBASSY ).build();
      TaskDefnKey lTaskDefn_Ref1 =
            TaskTaskTable.findByPrimaryKey( lTaskRevision_Ref1 ).getTaskDefn();

      // Requirements
      TaskTaskKey lTaskRevision_Req = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withConfigSlot( iConfigSlot_SUBASSY ).withTaskCode( "REQ_1" )
            .compliesWith( lTaskDefn_Ref1 ).build();

      // Initialized Tasks
      new TaskBuilder().onInventory( iAssyInventory_Pos_1 ).withTaskRevision( lTaskRevision_Ref1 )
            .build();

      TaskKey lLastTask_Pos_1 = new TaskBuilder().onInventory( iAssyInventory_Pos_1 )
            .withTaskRevision( lTaskRevision_Req ).asHistoric( RefEventStatusKey.COMPLETE ).build();
      TaskKey lNextTask_Pos_1 = new TaskBuilder().onInventory( iAssyInventory_Pos_1 )
            .withTaskRevision( lTaskRevision_Req ).withPrevTask( lLastTask_Pos_1 ).build();

      new TaskBuilder().onInventory( iAssyInventory_Pos_2 ).withTaskRevision( lTaskRevision_Ref1 )
            .build();

      TaskKey lLastTask_Pos_2 = new TaskBuilder().onInventory( iAssyInventory_Pos_2 )
            .withTaskRevision( lTaskRevision_Req ).asHistoric( RefEventStatusKey.COMPLETE ).build();
      TaskKey lNextTask_Pos_2 = new TaskBuilder().onInventory( iAssyInventory_Pos_2 )
            .withTaskRevision( lTaskRevision_Req ).withPrevTask( lLastTask_Pos_2 ).build();

      // Generate Report
      RefDocComplianceReport lReport =
            new RefDocComplianceReport( lTaskRevision_Ref1, lTaskDefn_Ref1, iHr );
      lReport.generate();

      DataSet lDataSet = lReport.getDataSet();

      assertRow( lDataSet, iAcftInventory, lTaskRevision_Ref1 );
      assertRow( lDataSet, iAcftInventory, lTaskRevision_Req, lLastTask_Pos_1, lNextTask_Pos_1 );
      assertRow( lDataSet, iAcftInventory, lTaskRevision_Req, lLastTask_Pos_2, lNextTask_Pos_2 );
      Assert.assertFalse( lDataSet.next() );
   }
}
