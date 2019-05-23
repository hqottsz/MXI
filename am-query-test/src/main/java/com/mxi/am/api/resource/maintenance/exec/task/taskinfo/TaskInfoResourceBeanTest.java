package com.mxi.am.api.resource.maintenance.exec.task.taskinfo;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.maintenance.exec.task.TaskType;
import com.mxi.am.api.resource.maintenance.exec.task.taskinfo.impl.TaskInfoResourceBean;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.testing.ResourceBeanTest;


@RunWith( MockitoJUnitRunner.class )
public class TaskInfoResourceBeanTest extends ResourceBeanTest {

   private static final String TASK_ONE_ID = "1E8BA890ED5F89FE39F34F40AA51718B";
   private static final String TASK_TWO_ID = "E39F34F40AA511E8BA890ED5F89F718B";
   private static final String INVENTORY_ID = "9CFBA066DA9011E587B1FB2D7B2472DF";
   private static final String TASK_BARCODE1 = "TASKONEBARCODE";
   private static final String TASK_BARCODE2 = "TASKTWOBARCODE";
   private static final String INVALID_BARCODE = "INVALID_BARCODE";
   private static final String TASK_CLASS_CODE1 = "INSP";
   private static final String TASK_CLASS_CODE2 = "REQ";
   private static final String PRIORITY = "HIGH";
   private static final String REQ_CLASS_MODE = "REQ";
   private static final String JIC_CLASS_MODE = "JIC";
   private static final String STATUS_DESCRIPTION = "Active";
   private static final String STATUS = "ACTV";
   private static final TaskType TASK_TYPE1 = TaskType.JobInstructionCard;
   private static final TaskType TASK_TYPE2 = TaskType.Requirement;
   private static final String DURATION = "0";
   private static final String LABOUR_ID = "9F718BE39F34F40AA511E8BA890ED5F8";
   private static final String WORK_PACKAGE_ID = "E8BA890ED5F89F718BE39F34F40AA511";
   private static final String INVALID_TASK_ID = "9F718BE39F34F40AA511E8BA890ED5F8";

   private TaskInfo iExpectedTask1 = new TaskInfo();
   private TaskInfo iExpectedTask2 = new TaskInfo();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( TaskInfoResource.class ).to( TaskInfoResourceBean.class );
               bind( Security.class ).to( CoreSecurity.class );
            }
         } );

   @Inject
   TaskInfoResourceBean iTaskInfoResourceBean;


   @Before
   public void setUp() throws MxException, AmApiBusinessException {
      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );
      initializeTest();
      constructExpectedResults();
   }


   /**
    *
    * Test correct taskinfo object returns for valid task_id
    *
    * @throws AmApiResourceNotFoundException
    *
    * @throws Exception
    */
   @Test
   @CSIContractTest( Project.UPS )
   public void testResultGetById() throws AmApiResourceNotFoundException {
      TaskInfo lResult = iTaskInfoResourceBean.get( TASK_ONE_ID );
      assertTaskEquals( iExpectedTask1, lResult );
   }


   /**
    *
    * Test correct taskinfo object returns for passed task barcode
    *
    * @throws Exception
    */
   @Test
   @CSIContractTest( Project.UPS )
   public void testResultSearchByBarcode() {
      TaskInfoSearchParameters lTaskInfoSearchParameters = new TaskInfoSearchParameters();
      lTaskInfoSearchParameters.setBarcode( TASK_BARCODE1 );

      List<TaskInfo> lResults = iTaskInfoResourceBean.search( lTaskInfoSearchParameters );

      assertEquals( "Incorrect number of task info objects returned: ", 1, lResults.size() );
      assertTaskEquals( iExpectedTask1, lResults.get( 0 ) );
   }


   /**
    *
    * Test correct taskinfo object returns for passed labour id
    *
    * @throws Exception
    */
   @Test
   @CSIContractTest( Project.UPS )
   public void testResultSearchByLabourId() {
      TaskInfoSearchParameters lTaskInfoSearchParameters = new TaskInfoSearchParameters();
      lTaskInfoSearchParameters.setLabourId( LABOUR_ID );

      List<TaskInfo> lResults = iTaskInfoResourceBean.search( lTaskInfoSearchParameters );

      assertEquals( "Incorrect number of task info objects returned: ", 1, lResults.size() );
      assertTaskEquals( iExpectedTask2, lResults.get( 0 ) );
   }


   /**
    *
    * Test correct taskinfo objects returns for passed workpackage id
    *
    * @throws Exception
    */
   @Test
   @CSIContractTest( Project.UPS )
   public void testResultsSearchByWorkpackageId() {
      TaskInfoSearchParameters lTaskInfoSearchParameters = new TaskInfoSearchParameters();
      lTaskInfoSearchParameters.setWorkPackageId( WORK_PACKAGE_ID );

      List<TaskInfo> lResults = iTaskInfoResourceBean.search( lTaskInfoSearchParameters );

      assertEquals( "Incorrect number of task info objects returned: ", 2, lResults.size() );
      assertTaskEquals( iExpectedTask1, lResults.get( 0 ) );
      assertTaskEquals( iExpectedTask2, lResults.get( 1 ) );
   }


   /**
    *
    * Test multiple taskinfo objects return for passed inventory id
    *
    * @throws Exception
    */
   @Test
   @CSIContractTest( Project.UPS )
   public void testResultsSearchByInventoryId() {
      TaskInfoSearchParameters lTaskInfoSearchParameters = new TaskInfoSearchParameters();
      lTaskInfoSearchParameters.setInventoryId( INVENTORY_ID );

      List<TaskInfo> lResults = iTaskInfoResourceBean.search( lTaskInfoSearchParameters );

      assertEquals( "Incorrect number of task info objects returned: ", 2, lResults.size() );
      assertTaskEquals( iExpectedTask1, lResults.get( 0 ) );
      assertTaskEquals( iExpectedTask2, lResults.get( 1 ) );
   }


   /**
    *
    * Test invalid task_id
    *
    * @throws AmApiResourceNotFoundException
    */
   @Test( expected = AmApiResourceNotFoundException.class )
   public void testTaskNotFoundById() throws AmApiResourceNotFoundException {
      iTaskInfoResourceBean.get( INVALID_TASK_ID );
   }


   /**
    *
    * Test empty list returns for invalid search parameters
    *
    * @throws Exception
    */
   @Test
   public void testEmptyListRetrieved() {
      TaskInfoSearchParameters lTaskInfoSearchParameters = new TaskInfoSearchParameters();
      lTaskInfoSearchParameters.setBarcode( INVALID_BARCODE );

      List<TaskInfo> lResults = iTaskInfoResourceBean.search( lTaskInfoSearchParameters );

      Assert.assertNotNull( "No task info objects were returned.", lResults );
      assertEquals( "Incorrect number of task info objects returned: ", 0, lResults.size() );
   }


   private void assertTaskEquals( TaskInfo aExpected, TaskInfo aActual ) {
      Assert.assertEquals( "Incorrect ID: ", aExpected.getId(), aActual.getId() );
      Assert.assertEquals( "Incorrect Barcode: ", aExpected.getBarcode(), aActual.getBarcode() );
      Assert.assertEquals( "Incorrect Inventory ID: ", aExpected.getInventoryId(),
            aActual.getInventoryId() );
      Assert.assertEquals( "Incorrect Priority: ", aExpected.getPriority(), aActual.getPriority() );
      Assert.assertEquals( "Incorrect Duration: ", aExpected.getDuration(), aActual.getDuration() );
      Assert.assertEquals( "Incorrect Status: ", aExpected.getStatus(), aActual.getStatus() );
      Assert.assertEquals( "Incorrect Task Class: ", aExpected.getTaskClass(),
            aActual.getTaskClass() );
      Assert.assertEquals( "Incorrect Task Class Mode: ", aExpected.getTaskClassMode(),
            aActual.getTaskClassMode() );
      Assert.assertEquals( "Incorrect Task Type: ", aExpected.getTaskType(),
            aActual.getTaskType() );
   }


   private void constructExpectedResults() {
      iExpectedTask1.setId( TASK_ONE_ID );
      iExpectedTask1.setBarcode( TASK_BARCODE1 );
      iExpectedTask1.setTaskClass( TASK_CLASS_CODE1 );
      iExpectedTask1.setPriority( PRIORITY );
      iExpectedTask1.setStatus( STATUS );
      iExpectedTask1.setTaskClassMode( JIC_CLASS_MODE );
      iExpectedTask1.setStatusDescription( STATUS_DESCRIPTION );
      iExpectedTask1.setInventoryId( INVENTORY_ID );
      iExpectedTask1.setTaskType( TASK_TYPE1 );
      iExpectedTask1.setDuration( DURATION );

      iExpectedTask2.setId( TASK_TWO_ID );
      iExpectedTask2.setBarcode( TASK_BARCODE2 );
      iExpectedTask2.setTaskClass( TASK_CLASS_CODE2 );
      iExpectedTask2.setPriority( PRIORITY );
      iExpectedTask2.setStatus( STATUS );
      iExpectedTask2.setTaskClassMode( REQ_CLASS_MODE );
      iExpectedTask2.setStatusDescription( STATUS_DESCRIPTION );
      iExpectedTask2.setInventoryId( INVENTORY_ID );
      iExpectedTask2.setTaskType( TASK_TYPE2 );
      iExpectedTask2.setDuration( DURATION );
   }
}
