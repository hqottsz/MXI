package com.mxi.am.api.resource.sys.refterm.taskrevisionreason;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBContext;
import javax.persistence.NonUniqueResultException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.sys.refterm.taskrevisionreason.impl.TaskRevisionReasonResourceBean;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query Tests for Task Revision Reason ResourceBean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class TaskRevisionReasonResourceBeanTest extends ResourceBeanTest {

   private static final String TASK_REV_REASON_CODE = "ADMIN";
   private static final String TASK_REV_REASON_NAME = "Administrative Changes";
   private static final String TASK_REV_REASON_DESCRIPTION =
         "Changes made for Administrative purposes.";

   private static final String TASK_REV_REASON_CODE2 = "NEW";
   private static final String TASK_REV_REASON_NAME2 = "New Requirements";
   private static final String TASK_REV_REASON_DESCRIPTION2 = "This Block/Req/Job Card is new.";

   private static final String TASK_REV_REASON_CODE3 = "OBSOLETE";
   private static final String TASK_REV_REASON_NAME3 = "Obsolete Requirements";
   private static final String TASK_REV_REASON_DESCRIPTION3 =
         "This Block/Req/Job Card is obsolete.";

   private static final String INVALID_TASK_REV_REASON_CD = "";
   private static final String NULL_TASK_REV_REASON_CD = null;
   private static final int TASK_REVISION_REASON_RECORD_COUNT = 4;
   private static final String DUPLICATE_TASK_REV_REASON_CD = "OBSOLETE";

   TaskRevisionReason actualTaskRevisionReason = new TaskRevisionReason();
   List<TaskRevisionReason> taskRevisionReasonObjectList = new ArrayList<TaskRevisionReason>();

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( TaskRevisionReasonResource.class ).to( TaskRevisionReasonResourceBean.class );
               bind( Security.class ).to( CoreSecurity.class );
            }
         } );

   @Inject
   private TaskRevisionReasonResourceBean taskRevisionReasonResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext ejbContext;


   @Before
   public void setUp() throws MxException {

      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );
      taskRevisionReasonResourceBean.setEJBContext( ejbContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      constructTaskRevisionReasonObject();
      constructTaskRevisionReasonList();
   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void get_success_validTaskRevisionReasonCode() throws AmApiResourceNotFoundException {
      TaskRevisionReason taskRevisionReason =
            taskRevisionReasonResourceBean.get( TASK_REV_REASON_CODE );
      assertEquals( actualTaskRevisionReason, taskRevisionReason );
   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void search_success() throws AmApiResourceNotFoundException {

      List<TaskRevisionReason> taskRevisionReasonList = taskRevisionReasonResourceBean.search();
      assertEquals( "Task revision reason count mismatched", TASK_REVISION_REASON_RECORD_COUNT,
            taskRevisionReasonList.size() );
      assertTrue( "Incorrect Task Revision Reason list returned",
            taskRevisionReasonObjectList.containsAll( taskRevisionReasonList ) );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void get_failure_invalidTaskRevisionReasonCode() throws AmApiResourceNotFoundException {
      taskRevisionReasonResourceBean.get( INVALID_TASK_REV_REASON_CD );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void get_failure_nullTaskRevisionReasonCode() throws AmApiResourceNotFoundException {
      taskRevisionReasonResourceBean.get( NULL_TASK_REV_REASON_CD );
   }


   @Test( expected = NonUniqueResultException.class )
   public void get_failure_duplicateTaskRevisionCode() throws AmApiResourceNotFoundException {
      taskRevisionReasonResourceBean.get( DUPLICATE_TASK_REV_REASON_CD );

   }


   public void constructTaskRevisionReasonObject() {
      actualTaskRevisionReason.setCode( TASK_REV_REASON_CODE );
      actualTaskRevisionReason.setName( TASK_REV_REASON_NAME );
      actualTaskRevisionReason.setDescription( TASK_REV_REASON_DESCRIPTION );
   }


   public void constructTaskRevisionReasonList() {
      TaskRevisionReason taskRevisionReason = new TaskRevisionReason();
      taskRevisionReason.setCode( TASK_REV_REASON_CODE );
      taskRevisionReason.setName( TASK_REV_REASON_NAME );
      taskRevisionReason.setDescription( TASK_REV_REASON_DESCRIPTION );

      TaskRevisionReason taskRevisionReason2 = new TaskRevisionReason();
      taskRevisionReason2.setCode( TASK_REV_REASON_CODE2 );
      taskRevisionReason2.setName( TASK_REV_REASON_NAME2 );
      taskRevisionReason2.setDescription( TASK_REV_REASON_DESCRIPTION2 );

      TaskRevisionReason taskRevisionReason3 = new TaskRevisionReason();
      taskRevisionReason3.setCode( TASK_REV_REASON_CODE3 );
      taskRevisionReason3.setName( TASK_REV_REASON_NAME3 );
      taskRevisionReason3.setDescription( TASK_REV_REASON_DESCRIPTION3 );

      taskRevisionReasonObjectList.add( taskRevisionReason );
      taskRevisionReasonObjectList.add( taskRevisionReason2 );
      taskRevisionReasonObjectList.add( taskRevisionReason3 );

   }

}
