
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
import com.mxi.mx.web.report.compliance.InvComplianceReport;
import com.mxi.mx.web.report.compliance.RefDocComplianceReport;


/**
 * Tests {@link RefDocComplianceReport}
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class InvComplianceReportTest extends ComplianceReportTestCase {

   /**
    * Tests that an assembly attached at the second position properly returns the requirements for
    * the root config slot.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testAssemblyAtPosition2IncludesRootLevelRequirements() throws Exception {

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
      new TaskBuilder().onInventory( iAssyInventory_Pos_2 ).withTaskRevision( lTaskRevision_Ref1 )
            .build();

      TaskKey lLastTask1 = new TaskBuilder().onInventory( iAssyInventory_Pos_1 )
            .withTaskRevision( lTaskRevision_Req ).asHistoric( RefEventStatusKey.COMPLETE ).build();
      new TaskBuilder().onInventory( iAssyInventory_Pos_1 ).withTaskRevision( lTaskRevision_Req )
            .withPrevTask( lLastTask1 ).build();

      TaskKey lLastTask2 = new TaskBuilder().onInventory( iAssyInventory_Pos_2 )
            .withTaskRevision( lTaskRevision_Req ).asHistoric( RefEventStatusKey.COMPLETE ).build();
      TaskKey lNextTask2 = new TaskBuilder().onInventory( iAssyInventory_Pos_2 )
            .withTaskRevision( lTaskRevision_Req ).withPrevTask( lLastTask2 ).build();

      // Generate Report
      InvComplianceReport lReport = new InvComplianceReport( iAssyInventory_Pos_2, iHr );
      lReport.generate();

      DataSet lDataSet = lReport.getDataSet();

      assertRow( lDataSet, iAssyInventory_Pos_2, lTaskRevision_Ref1 );
      assertRow( lDataSet, iAssyInventory_Pos_2, lTaskRevision_Req, lLastTask2, lNextTask2 );
      Assert.assertFalse( lDataSet.next() );
   }


   /**
    * Ensure that only assembly information is displayed: sub-assembly references should not be
    * displayed.
    */
   @Test
   public void testASSYReferences_Included() {

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
      new TaskBuilder().onInventory( iAssyInventory_Pos_2 ).withTaskRevision( lTaskRevision_Ref1 )
            .build();

      TaskKey lLastTask = new TaskBuilder().onInventory( iAssyInventory_Pos_1 )
            .withTaskRevision( lTaskRevision_Req ).asHistoric( RefEventStatusKey.COMPLETE ).build();
      TaskKey lNextTask = new TaskBuilder().onInventory( iAssyInventory_Pos_1 )
            .withTaskRevision( lTaskRevision_Req ).withPrevTask( lLastTask ).build();

      // Generate Report
      InvComplianceReport lReport = new InvComplianceReport( iAssyInventory_Pos_1, iHr );
      lReport.generate();

      DataSet lDataSet = lReport.getDataSet();

      assertRow( lDataSet, iAssyInventory_Pos_1, lTaskRevision_Ref1 );
      assertRow( lDataSet, iAssyInventory_Pos_1, lTaskRevision_Req, lLastTask, lNextTask );
      Assert.assertFalse( lDataSet.next() );
   }


   /**
    * Ensure reference documents on assembly are displayed.
    */
   @Test
   public void testBasic() {

      // Reference Documents
      TaskTaskKey lTaskRevision_Ref1 = new TaskRevisionBuilder().withTaskClass( REF )
            .withTaskCode( "REF_1" ).withConfigSlot( iConfigSlot_ACFT ).build();
      TaskDefnKey lTaskDefn_Ref1 =
            TaskTaskTable.findByPrimaryKey( lTaskRevision_Ref1 ).getTaskDefn();

      // Requirements
      TaskTaskKey lTaskRevision_Req = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withConfigSlot( iConfigSlot_ACFT ).withTaskCode( "REQ_1" )
            .compliesWith( lTaskDefn_Ref1 ).build();

      // Initialized Tasks
      new TaskBuilder().onInventory( iAcftInventory ).withTaskRevision( lTaskRevision_Ref1 )
            .build();

      TaskKey lLastTask = new TaskBuilder().onInventory( iAcftInventory )
            .withTaskRevision( lTaskRevision_Req ).asHistoric( RefEventStatusKey.COMPLETE ).build();
      TaskKey lNextTask = new TaskBuilder().onInventory( iAcftInventory )
            .withTaskRevision( lTaskRevision_Req ).withPrevTask( lLastTask ).build();

      // Generate Report
      InvComplianceReport lReport = new InvComplianceReport( iAcftInventory, iHr );
      lReport.generate();

      DataSet lDataSet = lReport.getDataSet();
      assertRow( lDataSet, iAcftInventory, lTaskRevision_Ref1 );
      assertRow( lDataSet, iAcftInventory, lTaskRevision_Req, lLastTask, lNextTask );
      Assert.assertFalse( lDataSet.next() );
   }


   /**
    * Ensure that only assembly information is displayed: SUBASSY references are still part of
    * assembly.
    */
   @Test
   public void testMultiplePositions_IncludedOnceEach() {

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

      TaskKey lLastTask = new TaskBuilder().onInventory( iAssyInventory_Pos_1 )
            .withTaskRevision( lTaskRevision_Req ).asHistoric( RefEventStatusKey.COMPLETE ).build();
      TaskKey lNextTask = new TaskBuilder().onInventory( iAssyInventory_Pos_1 )
            .withTaskRevision( lTaskRevision_Req ).withPrevTask( lLastTask ).build();

      // Generate Report
      InvComplianceReport lReport = new InvComplianceReport( iAcftInventory, iHr );
      lReport.generate();

      DataSet lDataSet = lReport.getDataSet();
      assertRow( lDataSet, iAcftInventory, lTaskRevision_Ref1 );
      assertRow( lDataSet, iAcftInventory, lTaskRevision_Req, lLastTask, lNextTask );
      assertRow( lDataSet, iAcftInventory, lTaskRevision_Req, null, null );
      Assert.assertFalse( lDataSet.next() );
   }


   /**
    * Ensure that only assembly information is displayed: parent assembly references should not be
    * displayed.
    */
   @Test
   public void testParentReferences_NotIncluded() {

      // Reference Documents
      TaskTaskKey lTaskRevision_Ref1 = new TaskRevisionBuilder().withTaskClass( REF )
            .withTaskCode( "REF_1" ).withConfigSlot( iConfigSlot_SUBASSY ).build();
      TaskDefnKey lTaskDefn_Ref1 =
            TaskTaskTable.findByPrimaryKey( lTaskRevision_Ref1 ).getTaskDefn();

      // Requirements
      new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withConfigSlot( iConfigSlot_SUBASSY ).withTaskCode( "REQ_1" )
            .compliesWith( lTaskDefn_Ref1 ).build();

      // Initialized Tasks
      new TaskBuilder().onInventory( iAssyInventory_Pos_1 ).withTaskRevision( lTaskRevision_Ref1 )
            .build();

      // Generate Report
      InvComplianceReport lReport = new InvComplianceReport( iAssyInventory_Pos_1, iHr );
      lReport.generate();

      DataSet lDataSet = lReport.getDataSet();
      Assert.assertFalse( lDataSet.next() );
   }


   /**
    * Ensure that only assembly information is displayed: SUBASSY references are still part of
    * assembly.
    */
   @Test
   public void testReferencesBetweenInventory() {

      // Reference Documents
      TaskTaskKey lTaskRevision_Ref1 = new TaskRevisionBuilder().withTaskClass( REF )
            .withTaskCode( "REF_1" ).withConfigSlot( iConfigSlot_ACFT ).build();
      TaskDefnKey lTaskDefn_Ref1 =
            TaskTaskTable.findByPrimaryKey( lTaskRevision_Ref1 ).getTaskDefn();

      TaskTaskKey lTaskRevision_Ref2 =
            new TaskRevisionBuilder().withTaskClass( REF ).withTaskCode( "REF_2" )
                  .withConfigSlot( iConfigSlot_SUBASSY ).compliesWith( lTaskDefn_Ref1 ).build();
      TaskDefnKey lTaskDefn_Ref2 =
            TaskTaskTable.findByPrimaryKey( lTaskRevision_Ref2 ).getTaskDefn();

      // Requirements
      TaskTaskKey lTaskRevision_Req = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withConfigSlot( iConfigSlot_SUBASSY ).withTaskCode( "REQ_1" )
            .compliesWith( lTaskDefn_Ref2 ).build();

      // Initialized Tasks
      new TaskBuilder().onInventory( iAcftInventory ).withTaskRevision( lTaskRevision_Ref1 )
            .build();
      new TaskBuilder().onInventory( iAssyInventory_Pos_1 ).withTaskRevision( lTaskRevision_Ref2 )
            .build();
      new TaskBuilder().onInventory( iAssyInventory_Pos_2 ).withTaskRevision( lTaskRevision_Ref2 )
            .build();

      TaskKey lLastTask = new TaskBuilder().onInventory( iAssyInventory_Pos_1 )
            .withTaskRevision( lTaskRevision_Req ).asHistoric( RefEventStatusKey.COMPLETE ).build();
      TaskKey lNextTask = new TaskBuilder().onInventory( iAssyInventory_Pos_1 )
            .withTaskRevision( lTaskRevision_Req ).withPrevTask( lLastTask ).build();

      // Generate Report
      InvComplianceReport lReport = new InvComplianceReport( iAcftInventory, iHr );
      lReport.generate();

      DataSet lDataSet = lReport.getDataSet();
      assertRow( lDataSet, iAcftInventory, lTaskRevision_Ref1 );
      assertRow( lDataSet, iAcftInventory, lTaskRevision_Ref2 );
      assertRow( lDataSet, iAcftInventory, lTaskRevision_Req, lLastTask, lNextTask );
      assertRow( lDataSet, iAcftInventory, lTaskRevision_Req, null, null );
      Assert.assertFalse( lDataSet.next() );
   }


   /**
    * Ensure that only assembly information is displayed: sub-assembly references should not be
    * displayed.
    */
   @Test
   public void testSubassemblyReferences_NotIncluded() {

      // Reference Documents
      TaskTaskKey lTaskRevision_Ref1 = new TaskRevisionBuilder().withTaskClass( REF )
            .withTaskCode( "REF_1" ).withConfigSlot( iConfigSlot_ASSY ).build();
      TaskDefnKey lTaskDefn_Ref1 =
            TaskTaskTable.findByPrimaryKey( lTaskRevision_Ref1 ).getTaskDefn();

      // Requirements
      new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withConfigSlot( iConfigSlot_ASSY ).withTaskCode( "REQ_1" )
            .compliesWith( lTaskDefn_Ref1 ).build();

      // Initialized Tasks
      new TaskBuilder().onInventory( iAssyInventory_Pos_1 ).withTaskRevision( lTaskRevision_Ref1 )
            .build();
      new TaskBuilder().onInventory( iAssyInventory_Pos_2 ).withTaskRevision( lTaskRevision_Ref1 )
            .build();

      // Generate Report
      InvComplianceReport lReport = new InvComplianceReport( iAcftInventory, iHr );
      lReport.generate();

      DataSet lDataSet = lReport.getDataSet();
      Assert.assertFalse( lDataSet.next() );
   }


   /**
    * Ensure that only assembly information is displayed: SUBASSY references are still part of
    * assembly.
    */
   @Test
   public void testSUBASSYReferences_Included() {

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
      new TaskBuilder().onInventory( iAssyInventory_Pos_2 ).withTaskRevision( lTaskRevision_Ref1 )
            .build();

      TaskKey lLastTask = new TaskBuilder().onInventory( iAssyInventory_Pos_1 )
            .withTaskRevision( lTaskRevision_Req ).asHistoric( RefEventStatusKey.COMPLETE ).build();
      TaskKey lNextTask = new TaskBuilder().onInventory( iAssyInventory_Pos_1 )
            .withTaskRevision( lTaskRevision_Req ).withPrevTask( lLastTask ).build();

      // Generate Report
      InvComplianceReport lReport = new InvComplianceReport( iAcftInventory, iHr );
      lReport.generate();

      DataSet lDataSet = lReport.getDataSet();
      assertRow( lDataSet, iAcftInventory, lTaskRevision_Ref1 );
      assertRow( lDataSet, iAcftInventory, lTaskRevision_Req, lLastTask, lNextTask );
      assertRow( lDataSet, iAcftInventory, lTaskRevision_Req, null, null );
      Assert.assertFalse( lDataSet.next() );
   }
}
