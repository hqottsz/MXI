package com.mxi.mx.core.services.taskdefn;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.task.TaskTaskDepTable;
import com.mxi.mx.core.table.task.TaskTaskTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Tests the TaskDefnService.getUniqueBoolForBlockOrReq() method.
 *
 * @author gpichetto
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetUniqueFlagForReqOrBlockTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * This scenario consists of a task definition (REQ) that has multiple revisions. The last
    * revision does not include any relationship to other task definition or itself (ie: it is not
    * recurring, does not follow any other task, or has no previous link to another task). Then,
    * while the last revision is being activated the getUniqueBoolForBlockOrReq() will return false,
    * thus the UNIQUE_BOOL will be set to 0 in the TASK_TASK table.
    *
    * <ul>
    * <li>Create a REQ with revision 1, in SUPERSEDE status</li>
    * <li>Create REQ revision 2, in ACTV status</li>
    * <li>The REQ revision 2 is recurrent thus, a record in task_task_dep table is created</li>
    * <li>Create REQ revision 3, in REVISION status</li>
    * </ul>
    *
    * @throws MxException
    *            if any error occurs
    */
   @Test
   public void testRequirementHasMultipleRevisions() throws MxException {

      // Create REQ revision 1
      TaskTaskKey lTaskRevision_Req_Rev1 = new TaskRevisionBuilder()
            .withTaskClass( RefTaskClassKey.REQ ).withTaskCode( "REQ_1" )
            .withStatus( RefTaskDefinitionStatusKey.SUPRSEDE ).withRevisionNumber( 1 ).build();

      // Get the task definition key
      TaskTaskTable lTaskTaskTable = TaskTaskTable.findByPrimaryKey( lTaskRevision_Req_Rev1 );
      TaskDefnKey lTaskDefnKey = lTaskTaskTable.getTaskDefn();

      // Assert that the requirement revision 1 is not unique thus, the UNIQUE_BOOL will be set to 0
      // after the task definition is activated.
      MxAssert.assertFalse( TaskDefnService.getUniqueBoolForBlockOrReq( lTaskRevision_Req_Rev1 ) );

      // Create REQ revision 2
      TaskTaskKey lTaskRevision_Req_Rev2 =
            new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ ).withTaskCode( "REQ_1" )
                  .withStatus( RefTaskDefinitionStatusKey.ACTV ).withRevisionNumber( 2 )
                  .isRecurring().withTaskDefn( lTaskDefnKey ).build();

      // REQ revision 2 is recurring thus it is required
      // to insert a recod in the task_task_dep table
      TaskTaskDepTable lTaskTaskDep = TaskTaskDepTable
            .create( TaskTaskDepTable.generatePrimaryKey( lTaskRevision_Req_Rev2 ) );
      lTaskTaskDep.setDepTaskDefn( lTaskDefnKey );
      lTaskTaskDep.insert();

      // Assert that the requirement revision 2 is unique since it is recurrent thus, the
      // UNIQUE_BOOL will be set to 0 after the task definition is activated.
      MxAssert.assertTrue( TaskDefnService.getUniqueBoolForBlockOrReq( lTaskRevision_Req_Rev2 ) );

      // Create REQ revision 3
      TaskTaskKey lTaskRevision_Req_Rev3 =
            new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ ).withTaskCode( "REQ_1" )
                  .withStatus( RefTaskDefinitionStatusKey.REVISION ).withRevisionNumber( 3 )
                  .withTaskDefn( lTaskDefnKey ).build();

      // Assert that the requirement revision 3 is not unique thus, the UNIQUE_BOOL will be set to 0
      // after the task definition is activated.
      MxAssert.assertFalse( TaskDefnService.getUniqueBoolForBlockOrReq( lTaskRevision_Req_Rev3 ) );
   }

}
