package com.mxi.am.api.resource.maintenance.prog.taskdefn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.security.Principal;
import java.util.Arrays;
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
import com.mxi.am.api.resource.maintenance.prog.taskdefn.impl.TaskDefinitionResourceBean;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.core.materials.tracking.measurement.datatype.dao.DataTypeDao;
import com.mxi.mx.core.materials.tracking.measurement.datatype.dao.JdbcDataTypeEntityDao;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query Test for Task Definition API
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class TaskDefinitionResourceBeanTest extends ResourceBeanTest {

   TaskDefinition taskDef1 = new TaskDefinition();
   TaskDefinition taskDef2 = new TaskDefinition();

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( Security.class ).to( CoreSecurity.class );
               bind( DataTypeDao.class ).to( JdbcDataTypeEntityDao.class );
            }
         } );

   @Inject
   TaskDefinitionResourceBean testTaskDefinitionResource;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext ejbContext;


   @Before
   public void init() throws MxException {
      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );
      InjectorContainer.set( iInjector );
      testTaskDefinitionResource.setEJBContext( ejbContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( ejbContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );
      constructTaskDefinitions();
   }


   @Test
   public void testGetById_Success() throws Exception {
      TaskDefinition result = testTaskDefinitionResource.get( taskDef1.getId() );

      assertTaskDefinition( taskDef1, result );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetById_InvalidIdFail() throws Exception {
      testTaskDefinitionResource.get( "00000000000000000000000000000626" );
   }


   /**
    * Search the task definitions for a variety of parameters and get the correct result back.
    *
    */
   @CSIContractTest( Project.SWA_MOPP )
   @Test
   public void testSearch_Success() throws Exception {
      TaskDefinitionSearchParameters taskDefSearchParams = new TaskDefinitionSearchParameters();

      taskDefSearchParams.setClassCode( taskDef1.getClassCd() );
      taskDefSearchParams.setSubclassCode( taskDef1.getSubclassCd() );
      taskDefSearchParams.setStatusCodes( Arrays.asList( taskDef1.getStatusCd() ) );
      taskDefSearchParams.setAssemblyId( taskDef1.getAssmblId() );

      List<TaskDefinition> results = testTaskDefinitionResource.search( taskDefSearchParams );

      assertEquals( "Unexpected number of results returned: ", 1, results.size() );

      assertTaskDefinition( taskDef1, results.get( 0 ) );
   }


   @Test
   public void testSearch_NoResultsSuccess() throws Exception {
      TaskDefinitionSearchParameters taskDefSearchParams = new TaskDefinitionSearchParameters();

      taskDefSearchParams.setClassCode( "JIC" );

      List<TaskDefinition> results = testTaskDefinitionResource.search( taskDefSearchParams );

      assertEquals( "Unexpected number of results returned: ", 0, results.size() );
   }


   @Test
   public void testSearch_MultipleResultsSuccess() throws Exception {
      TaskDefinitionSearchParameters taskDefSearchParams = new TaskDefinitionSearchParameters();

      taskDefSearchParams.setStatusCodes( Arrays.asList( "ACTV" ) );

      List<TaskDefinition> results = testTaskDefinitionResource.search( taskDefSearchParams );

      assertEquals( "Unexpected number of results returned: ", 2, results.size() );

      if ( results.get( 0 ).getId().equals( taskDef1.getId() ) ) {
         assertTaskDefinition( taskDef1, results.get( 0 ) );
         assertTaskDefinition( taskDef2, results.get( 1 ) );
      } else {
         assertTaskDefinition( taskDef2, results.get( 0 ) );
         assertTaskDefinition( taskDef1, results.get( 1 ) );
      }
   }


   private void assertTaskDefinition( TaskDefinition expected, TaskDefinition actual ) {

      if ( actual == null ) {
         fail( "Returned Task Definition was null." );
      }

      assertEquals( "Incorrect Id returned: ", expected.getId(), actual.getId() );
      assertEquals( "Incorrect Name returned: ", expected.getName(), actual.getName() );
      assertEquals( "Incorrect Schedule From Code returned: ", expected.getSchedFromCd(),
            actual.getSchedFromCd() );
      assertEquals( "Incorrect Revision Order returned: ", expected.getRevisionOrder(),
            actual.getRevisionOrder() );
      assertEquals( "Incorrect Assembly Code returned: ", expected.getAssmblCd(),
            actual.getAssmblCd() );
      assertEquals( "Incorrect Class Mode Code returned: ", expected.getClassModeCd(),
            actual.getClassModeCd() );
      assertEquals( "Incorrect Code returned: ", expected.getCode(), actual.getCode() );
      assertEquals( "Incorrect Status returned: ", expected.getStatusCd(), actual.getStatusCd() );
      assertEquals( "Incorrect Recurring Bool returned: ", expected.isRecurring(),
            actual.isRecurring() );
      assertEquals( "Incorrect Root Task Definition Id returned: ",
            expected.getRootTaskDefinitionId(), actual.getRootTaskDefinitionId() );
      assertEquals( "Incorrect Task Must Remove returned: ", expected.getTaskMustRemove(),
            actual.getTaskMustRemove() );
      assertEquals( "Incorrect Engineering Notes returned: ", expected.getEngineeringNotes(),
            actual.getEngineeringNotes() );
      assertEquals( "Incorrect Assembly Id returned: ", expected.getAssmblId(),
            actual.getAssmblId() );
      assertEquals( "Incorrect Part Group Code returned: ", expected.getPartGroupCd(),
            actual.getPartGroupCd() );
      assertEquals( "Incorrect Config Slot Id returned: ", expected.getConfigSlotId(),
            actual.getConfigSlotId() );
      assertEquals( "Incorrect Config Slot Code returned: ", expected.getConfigSlotCd(),
            actual.getConfigSlotCd() );
      assertEquals( "Incorrect Class Code returned: ", expected.getClassCd(), actual.getClassCd() );
      assertEquals( "Incorrect Organization Id returned: ", expected.getOrganizationId(),
            actual.getOrganizationId() );
   }


   private void constructTaskDefinitions() {
      taskDef1.setId( "00000000000000000000000000001626" );
      taskDef1.setName( "TEST TASK" );
      taskDef1.setSchedFromCd( "MANUFACT_DT" );
      taskDef1.setRevisionOrder( 1 );
      taskDef1.setAssmblCd( "F-2000" );
      taskDef1.setClassModeCd( "REQ" );
      taskDef1.setCode( "CD-OF-TASK" );
      taskDef1.setStatusCd( "ACTV" );
      taskDef1.setRecurring( false );
      taskDef1.setRootTaskDefinitionId( "00000000000000000000000000001057" );
      taskDef1.setTaskMustRemove( "NA" );
      taskDef1.setEngineeringNotes( "ENGINEERING NOTE" );
      taskDef1.setAssmblId( "00000000000000000000000000021057" );
      taskDef1.setPartGroupCd( "F-2000" );
      taskDef1.setConfigSlotId( "00000000000000000000000000001626" );
      taskDef1.setConfigSlotCd( "53-00-00" );
      taskDef1.setClassCd( "REQ" );
      taskDef1.setSubclassCd( "REQOPC" );
      taskDef1.setOrganizationId( "00000000000000000000000000001029" );

      taskDef2.setId( "00000000000000000000000000002626" );
      taskDef2.setName( "TEST TASK 2" );
      taskDef2.setSchedFromCd( "MANUFACT_DT" );
      taskDef2.setRevisionOrder( 2 );
      taskDef2.setAssmblCd( "F-2000" );
      taskDef2.setClassModeCd( "REQ" );
      taskDef2.setCode( "CD-OF-TASK-2" );
      taskDef2.setStatusCd( "ACTV" );
      taskDef2.setRecurring( false );
      taskDef2.setRootTaskDefinitionId( "00000000000000000000000000001058" );
      taskDef2.setTaskMustRemove( "NA" );
      taskDef2.setAssmblId( "00000000000000000000000000021057" );
      taskDef2.setPartGroupCd( "F-2000" );
      taskDef2.setConfigSlotId( "00000000000000000000000000001626" );
      taskDef2.setConfigSlotCd( "53-00-00" );
      taskDef2.setClassCd( "CORR" );
      taskDef2.setOrganizationId( "00000000000000000000000000001029" );
   }
}
