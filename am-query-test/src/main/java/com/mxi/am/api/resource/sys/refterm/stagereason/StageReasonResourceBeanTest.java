package com.mxi.am.api.resource.sys.refterm.stagereason;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.EJBContext;
import javax.naming.NamingException;
import javax.persistence.NonUniqueResultException;

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
import com.mxi.am.api.exception.AmApiInternalServerException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.sys.refterm.stagereason.impl.StageReasonResourceBean;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query-Test for StageReasonResourceBean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class StageReasonResourceBeanTest extends ResourceBeanTest {

   private static final String STAGE_REASON_CODE = "QUERYTEST";
   private static final String STAGE_REASON_CODE_NOT_FOUND = "QUERYTESTNOTFOUND";
   private static final String STAGE_REASON_CODE_INVALID = "QUERYTESTINVALID";
   private static final String STAGE_REASON_CODE_DOUBLE = "QUERYTESTDOUBLE";
   private static final String STAGE_REASON_DESCRIPTION = "Reason for query test";
   private static final String STAGE_REASON_EVENT_STATUS_CODE = "TEST";
   private static final String STAGE_REASON_NAME = "Query test";
   private static final String STAGE_REASON_USER_CODE = "QUERYTEST";
   private static final int LIST_FOUND_SIZE = 39;
   private static final String EXCEPTION_TYPE_ID = "Stage Reason";
   private static final String DOUBLE_ENTRY_EXCEPTION =
         "Error: only one record is expected for stage reason code 'QUERYTESTDOUBLE' but more than one found.";

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( StageReasonResource.class ).to( StageReasonResourceBean.class );
               bind( Security.class ).to( CoreSecurity.class );
            }
         } );

   @Inject
   StageReasonResourceBean stageReasonResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext eJBContext;


   @Before
   public void setUp() throws NamingException, MxException, SQLException {
      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );
      stageReasonResourceBean.setEJBContext( eJBContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( eJBContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );
   }


   @Test
   @CSIContractTest( Project.UPS )
   public void testGetSuccess() throws AmApiResourceNotFoundException {
      StageReason stageReason = stageReasonResourceBean.get( STAGE_REASON_CODE );
      assertStageReason( stageReason );
   }


   @Test
   public void testGetNotFound() {
      try {
         stageReasonResourceBean.get( STAGE_REASON_CODE_NOT_FOUND );
         Assert.fail( "Did not throw AmApiResourceNotFoundException" );
      } catch ( AmApiResourceNotFoundException e ) {
         Assert.assertEquals( "Incorrect Exception ID: ", STAGE_REASON_CODE_NOT_FOUND, e.getId() );
         Assert.assertEquals( "Incorrect Exception type: ", EXCEPTION_TYPE_ID, e.getIdType() );
         Assert.assertEquals( "Incorrect Exception message: ",
               EXCEPTION_TYPE_ID + " " + STAGE_REASON_CODE_NOT_FOUND + " not found",
               e.getMessage() );
      } catch ( Exception e ) {
         Assert.fail( "Did not throw a AmApiResourceNotFoundException, instead threw "
               + e.getClass().getName() );
      }
   }


   @Test
   public void testGetInvalid() {
      try {
         stageReasonResourceBean.get( STAGE_REASON_CODE_INVALID );
         Assert.fail( "Did not throw AmApiResourceNotFoundException" );
      } catch ( AmApiResourceNotFoundException e ) {
         Assert.assertEquals( "Incorrect Exception ID: ", STAGE_REASON_CODE_INVALID, e.getId() );
         Assert.assertEquals( "Incorrect Exception type: ", EXCEPTION_TYPE_ID, e.getIdType() );
         Assert.assertEquals( "Incorrect Exception message: ",
               EXCEPTION_TYPE_ID + " " + STAGE_REASON_CODE_INVALID + " not found", e.getMessage() );
      } catch ( Exception e ) {
         Assert.fail( "Did not throw a AmApiResourceNotFoundException, instead threw "
               + e.getClass().getName() );
      }
   }


   @Test
   public void testGetDoubleEntry() {
      try {
         stageReasonResourceBean.get( STAGE_REASON_CODE_DOUBLE );
         Assert.fail( "Did not throw AmApiInternalServerException" );
      } catch ( AmApiInternalServerException e ) {
         if ( e.getCause().getClass() == ( NonUniqueResultException.class ) ) {
            NonUniqueResultException nonUniqueResultException =
                  ( NonUniqueResultException ) e.getCause();
            Assert.assertEquals( "Incorrect Exception message: ", DOUBLE_ENTRY_EXCEPTION,
                  nonUniqueResultException.getMessage() );

         } else {
            Assert.fail(
                  "Cause of AmApiInternalServerException was not NonUniqueResultException but instead was: "
                        + e.getCause().getClass().getName() );
         }

      } catch ( Exception e ) {
         Assert.fail( "Did not throw a AmApiInternalServerException, instead threw "
               + e.getClass().getName() );
      }
   }


   @Test
   public void testSearchAllSuccess() {
      List<StageReason> stageReasons =
            stageReasonResourceBean.search( new StageReasonSearchParameters() );
      Assert.assertEquals( "Incorrect number of stage reasons returned: ", LIST_FOUND_SIZE,
            stageReasons.size() );
   }


   @Test
   public void testSearchEventStatusCodeSuccess() {
      List<StageReason> stageReasons = stageReasonResourceBean
            .search( new StageReasonSearchParameters( STAGE_REASON_EVENT_STATUS_CODE ) );
      Assert.assertEquals( "Incorrect number of stage reasons returned: ", 1, stageReasons.size() );
      assertStageReason( stageReasons.get( 0 ) );
   }


   private void assertStageReason( StageReason stageReason ) {
      Assert.assertEquals( "Incorrect Code: ", STAGE_REASON_CODE, stageReason.getCode() );
      Assert.assertEquals( "Incorrect Description: ", STAGE_REASON_DESCRIPTION,
            stageReason.getDescription() );
      Assert.assertEquals( "Incorrect Event Status Code: ", STAGE_REASON_EVENT_STATUS_CODE,
            stageReason.getStatusCode() );
      Assert.assertEquals( "Incorrect Name: ", STAGE_REASON_NAME, stageReason.getName() );
      Assert.assertEquals( "Incorrect User Code: ", STAGE_REASON_USER_CODE,
            stageReason.getUserCode() );
   }

}
