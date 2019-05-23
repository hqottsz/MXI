package com.mxi.am.api.resource.sys.refterm.taskpriority;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBContext;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.sys.refterm.taskpriority.impl.TaskPriorityResourceBean;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query test for Task Priority ResourceBean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class TaskPriorityResourceBeanTest extends ResourceBeanTest {

   private static final String TASK_PRIORITY_CODE = "LOW";
   public static final String TASK_PRIORITY_NAME = "Low priority";
   public static final String TASK_PRIORITY_DESCRIPTION = "Low priority";
   public static final int TASK_PRIORITY_ORDER = 1;

   private static final String TASK_PRIORITY_CODE2 = "HIGH";
   public static final String TASK_PRIORITY_NAME2 = "High priority";
   public static final String TASK_PRIORITY_DESCRIPTION2 = "High priority";
   public static final int TASK_PRIORITY_ORDER2 = 2;

   private static final int TASK_PRIORITY_RECORD_COUNT = 2;
   private static final String NON_EXIST_TASK_PRIORITY_CODE = "XXX";
   private static final String NULL_TASK_PRIORITY_CODE = null;

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( TaskPriorityResource.class ).to( TaskPriorityResourceBean.class );
               bind( Security.class ).to( CoreSecurity.class );
               bind( EJBContext.class ).toInstance( ejbContext );
            }
         } );

   @Inject
   private TaskPriorityResourceBean taskPriorityResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext ejbContext;


   @Before
   public void setUp() throws MxException {

      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );
      taskPriorityResourceBean.setEJBContext( ejbContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( ejbContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );

   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void testGetTaskPriorityByCodeSuccess200() throws AmApiResourceNotFoundException {
      TaskPriority taskPriority = taskPriorityResourceBean.get( TASK_PRIORITY_CODE );
      assertEquals( defaultTaskPriorityBuilder(), taskPriority );
   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void testSearchAllTaskPrioritiesSuccess200() {
      List<TaskPriority> taskPriorityList = taskPriorityResourceBean.search();
      assertEquals( TASK_PRIORITY_RECORD_COUNT, taskPriorityList.size() );
      assertTrue( taskPriorityList.containsAll( defaultTaskPriorityListBuilder() ) );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetTaskPriorityByNonExistCodeFailure404() throws AmApiResourceNotFoundException {
      taskPriorityResourceBean.get( NON_EXIST_TASK_PRIORITY_CODE );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetTaskPriorityByNullCodeFailure404() throws AmApiResourceNotFoundException {
      taskPriorityResourceBean.get( NULL_TASK_PRIORITY_CODE );
   }


   private TaskPriority defaultTaskPriorityBuilder() {
      TaskPriority taskPriority = new TaskPriority();
      taskPriority.setCode( TASK_PRIORITY_CODE );
      taskPriority.setName( TASK_PRIORITY_NAME );
      taskPriority.setDescription( TASK_PRIORITY_DESCRIPTION );
      taskPriority.setOrder( TASK_PRIORITY_ORDER );
      return taskPriority;
   }


   private List<TaskPriority> defaultTaskPriorityListBuilder() {

      List<TaskPriority> taskPriorityList = new ArrayList<TaskPriority>();

      TaskPriority taskPriority1 = new TaskPriority();
      taskPriority1.setCode( TASK_PRIORITY_CODE );
      taskPriority1.setName( TASK_PRIORITY_NAME );
      taskPriority1.setDescription( TASK_PRIORITY_DESCRIPTION );
      taskPriority1.setOrder( TASK_PRIORITY_ORDER );

      TaskPriority taskPriority2 = new TaskPriority();
      taskPriority2.setCode( TASK_PRIORITY_CODE2 );
      taskPriority2.setName( TASK_PRIORITY_NAME2 );
      taskPriority2.setDescription( TASK_PRIORITY_DESCRIPTION2 );
      taskPriority2.setOrder( TASK_PRIORITY_ORDER2 );

      taskPriorityList.add( taskPriority1 );
      taskPriorityList.add( taskPriority2 );

      return taskPriorityList;
   }
}
