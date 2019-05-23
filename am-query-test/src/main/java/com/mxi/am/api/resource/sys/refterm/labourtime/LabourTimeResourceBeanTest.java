package com.mxi.am.api.resource.sys.refterm.labourtime;

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
import com.mxi.am.api.resource.sys.refterm.labourtime.impl.LabourTimeResourceBean;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query-Test for LabourTimeResourceBean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class LabourTimeResourceBeanTest extends ResourceBeanTest {

   private static final String INVALID_LABOUR_TIME_CODE = "INVALID";
   private static final String TEST_LABOUR_TIME_CODE = "TEST";
   private static final String TEST_LABOUR_TIME_DESC = "Test Labour Time";
   private static final String TEST_LABOUR_TIME_NAME = "Test";

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( LabourTimeResource.class ).to( LabourTimeResourceBean.class );
               bind( Security.class ).to( CoreSecurity.class );
            }
         } );

   @Inject
   LabourTimeResourceBean labourTimeResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext eJBContext;


   @Before
   public void setUp() throws NamingException, MxException, SQLException {
      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );
      labourTimeResourceBean.setEJBContext( eJBContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( eJBContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );
   }


   @Test
   public void testSearchSuccess() {
      List<LabourTime> labourTimes = labourTimeResourceBean.search();
      AssertLabourTimeList( labourTimes );
   }


   private void AssertLabourTimeList( List<LabourTime> LabourTimeList ) {
      boolean test = false;
      for ( LabourTime labourTime : LabourTimeList ) {
         if ( labourTime.getCode().equalsIgnoreCase( TEST_LABOUR_TIME_CODE ) ) {
            Assert.assertEquals( TEST_LABOUR_TIME_DESC, labourTime.getDescription() );
            Assert.assertEquals( TEST_LABOUR_TIME_NAME, labourTime.getName() );
            test = true;
         } else if ( labourTime.getCode().equalsIgnoreCase( INVALID_LABOUR_TIME_CODE ) ) {
            Assert.fail( "Found a LabourTime with rstat_cd other than 0" );
         }
      }
      if ( !test ) {
         Assert.fail( "Did not find all the expected labourTime" );
      }
   }
}
