package com.mxi.am.api.resource.sys.refterm.tasksubclass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBContext;

import org.junit.Assert;
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
import com.mxi.am.api.exception.AmApiMandatoryParameterMissingException;
import com.mxi.am.api.resource.sys.refterm.tasksubclass.impl.TaskSubclassResourceBean;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query Test for TaskSubclassResourceBean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class TaskSubclassResourceBeanTest extends ResourceBeanTest {

   private static final String TASK_CLASS_CODE = "MPC";
   private static final String CODE1 = "MPCOPEN";
   private static final String DESCRIPTION1 = "Open Master Panel Card";
   private static final String NAME1 = "Open Master Panel Card";
   private static final String USER_CODE1 = "OPEN";

   private static final String CODE2 = "MPCCLOSE";
   private static final String DESCRIPTION2 = "Close Master Panel Card";
   private static final String NAME2 = "Close Master Panel Card";
   private static final String USER_CODE2 = "CLOSE";
   private static final int TASK_SUBCLASS_RECORD_COUNT = 2;

   List<TaskSubclass> expectedTaskSubclassList = new ArrayList<TaskSubclass>();

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( TaskSubclassResource.class ).to( TaskSubclassResourceBean.class );
               bind( Security.class ).to( CoreSecurity.class );
            }
         } );

   @Inject
   private TaskSubclassResourceBean taskSubclassResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext ejbContext;


   @Before
   public void setUp() throws MxException {

      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );
      taskSubclassResourceBean.setEJBContext( ejbContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( ejbContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );

      constructTaskSubclassList();
   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void testSuccessSearch() {
      TaskSubclassSearchParameters taskSubclassSearchParameters =
            new TaskSubclassSearchParameters();
      taskSubclassSearchParameters.setTaskClassCode( TASK_CLASS_CODE );
      List<TaskSubclass> actualTaskSubclassList =
            taskSubclassResourceBean.search( taskSubclassSearchParameters );
      assertEquals( TASK_SUBCLASS_RECORD_COUNT, actualTaskSubclassList.size() );
      assertTrue(
            "Expected: " + expectedTaskSubclassList + " but received: " + actualTaskSubclassList,
            actualTaskSubclassList.containsAll( expectedTaskSubclassList ) );
   }


   @Test
   public void testFailureSearchNoParameters() {
      try {
         TaskSubclassSearchParameters taskSubclassSearchParameters =
               new TaskSubclassSearchParameters();
         taskSubclassResourceBean.search( taskSubclassSearchParameters );
         Assert.fail( "No AmApiMandatoryParameterMissingException is thrown." );
      } catch ( AmApiMandatoryParameterMissingException e ) {
         Assert.assertEquals(
               "This API has mandatory search parameters. Valid parameters are: task_class_code",
               e.getMessage() );
      }
   }


   private void constructTaskSubclassList() {
      TaskSubclass taskSubclass1 = new TaskSubclass();
      taskSubclass1.setCode( CODE1 );
      taskSubclass1.setDescription( DESCRIPTION1 );
      taskSubclass1.setName( NAME1 );
      taskSubclass1.setTaskClassCode( TASK_CLASS_CODE );
      taskSubclass1.setUserCode( USER_CODE1 );

      TaskSubclass taskSubclass2 = new TaskSubclass();
      taskSubclass2.setCode( CODE2 );
      taskSubclass2.setDescription( DESCRIPTION2 );
      taskSubclass2.setName( NAME2 );
      taskSubclass2.setTaskClassCode( TASK_CLASS_CODE );
      taskSubclass2.setUserCode( USER_CODE2 );

      expectedTaskSubclassList.add( taskSubclass1 );
      expectedTaskSubclassList.add( taskSubclass2 );
   }

}
