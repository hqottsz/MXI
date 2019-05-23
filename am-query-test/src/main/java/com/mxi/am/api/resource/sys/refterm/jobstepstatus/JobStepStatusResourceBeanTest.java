package com.mxi.am.api.resource.sys.refterm.jobstepstatus;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.EJBContext;
import javax.naming.NamingException;

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
import com.mxi.am.api.resource.sys.refterm.jobstepstatus.impl.JobStepStatusResourceBean;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query-Test for JobStepStatusResourceBean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class JobStepStatusResourceBeanTest extends ResourceBeanTest {

   private static final String PENDING_STATUS_CODE = "MXPENDING";
   private static final String PARTIAL_STATUS_CODE = "MXPARTIAL";
   private static final String COMPLETE_STATUS_CODE = "MXCOMPLETE";
   private static final String NA_STATUS_CODE = "MXNA";
   private static final String INVALID_STATUS_CODE = "INVALID";
   private static final String TEST_STATUS_CODE = "TEST";
   private static final String TEST_DISPLAY_NAME = "Test";
   private static final Integer TEST_ORDER = 99;

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( JobStepStatusResource.class ).to( JobStepStatusResourceBean.class );
               bind( Security.class ).to( CoreSecurity.class );
            }
         } );

   @Inject
   JobStepStatusResourceBean jobStepStatusResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext eJBContext;


   @Before
   public void setUp() throws NamingException, MxException, SQLException {
      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );
      jobStepStatusResourceBean.setEJBContext( eJBContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( eJBContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );
   }


   @Test
   public void testSearchAllSuccess() {
      List<JobStepStatus> jobStepStatusList = jobStepStatusResourceBean.search();
      assertJobStepStatus( jobStepStatusList );
   }


   private void assertJobStepStatus( List<JobStepStatus> jobStepStatusList ) {
      boolean pending = false;
      boolean partial = false;
      boolean complete = false;
      boolean na = false;
      boolean test = false;
      for ( JobStepStatus jobStepStatus : jobStepStatusList ) {
         if ( jobStepStatus.getCode().equalsIgnoreCase( PENDING_STATUS_CODE ) ) {
            pending = true;
         } else if ( jobStepStatus.getCode().equalsIgnoreCase( PARTIAL_STATUS_CODE ) ) {
            Assert.assertTrue( "The JobStepStatus was not returned in the correct order", pending );
            partial = true;
         } else if ( jobStepStatus.getCode().equalsIgnoreCase( COMPLETE_STATUS_CODE ) ) {
            Assert.assertTrue( "The JobStepStatus was not returned in the correct order", partial );
            complete = true;
         } else if ( jobStepStatus.getCode().equalsIgnoreCase( NA_STATUS_CODE ) ) {
            Assert.assertTrue( "The JobStepStatus was not returned in the correct order",
                  complete );
            na = true;
         } else if ( jobStepStatus.getCode().equalsIgnoreCase( TEST_STATUS_CODE ) ) {
            Assert.assertTrue( "The JobStepStatus was not returned in the correct order", na );
            Assert.assertEquals( TEST_DISPLAY_NAME, jobStepStatus.getDisplayName() );
            Assert.assertEquals( false, jobStepStatus.getHistoric() );
            Assert.assertEquals( TEST_ORDER, jobStepStatus.getOrder() );
            test = true;
         } else if ( jobStepStatus.getCode().equalsIgnoreCase( INVALID_STATUS_CODE ) ) {
            Assert.fail( "Found a JobStepStatus with an rstat_cd other than 0" );
         }
      }

      if ( !pending || !partial || !complete || !na || !test ) {
         Assert.fail( "Did not find all the expected JobStepStatus" );
      }
   }
}
