package com.mxi.am.api.resource.sys.refterm.removereason;

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
import com.mxi.am.api.resource.sys.refterm.removereason.impl.RemoveReasonResourceBean;
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
public class RemoveReasonResourceBeanTest extends ResourceBeanTest {

   private static final String TEST_REMOVE_REASON_CODE = "TEST";
   private static final String TEST_REMOVE_REASON_DESC = "Test remove reason";
   private static final String TEST_REMOVE_REASON_NAME = "Test";
   private static final String TEST_REMOVE_REASON_S2K_CODE = "T";
   private static final String TEST_REMOVE_REASON_INV_COND_CODE = "RFI";
   private static final String INVALID_REMOVE_REASON_CODE = "INVALID";

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( RemoveReasonResource.class ).to( RemoveReasonResourceBean.class );
               bind( Security.class ).to( CoreSecurity.class );
            }
         } );

   @Inject
   RemoveReasonResourceBean removeReasonResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext eJBContext;


   @Before
   public void setUp() throws NamingException, MxException, SQLException {
      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );
      removeReasonResourceBean.setEJBContext( eJBContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( eJBContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );
   }


   @Test
   public void testSeachSuccess() {
      List<RemoveReason> removeReaons = removeReasonResourceBean.search();
      assertRemoveReason( removeReaons );
   }


   private void assertRemoveReason( List<RemoveReason> removeReaonsList ) {
      boolean test = false;
      for ( RemoveReason removeReason : removeReaonsList ) {
         if ( removeReason.getCode().equalsIgnoreCase( TEST_REMOVE_REASON_CODE ) ) {
            Assert.assertEquals( TEST_REMOVE_REASON_DESC, removeReason.getDescription() );
            Assert.assertEquals( TEST_REMOVE_REASON_NAME, removeReason.getName() );
            Assert.assertEquals( TEST_REMOVE_REASON_S2K_CODE, removeReason.getSpec2kCode() );
            Assert.assertEquals( TEST_REMOVE_REASON_INV_COND_CODE,
                  removeReason.getInventoryConditionCode() );
            test = true;
         } else if ( removeReason.getCode().equalsIgnoreCase( INVALID_REMOVE_REASON_CODE ) ) {
            Assert.fail( "Found LabourTime with rstat_cd other than 0" );
         }
      }
      if ( !test ) {
         Assert.fail( "Did not find all the expected labourTime" );
      }
   }
}
