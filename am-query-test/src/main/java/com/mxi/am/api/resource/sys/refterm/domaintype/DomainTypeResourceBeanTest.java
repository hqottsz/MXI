package com.mxi.am.api.resource.sys.refterm.domaintype;

import java.security.Principal;
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
import com.mxi.am.api.exception.AmApiAuthorizationException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.sys.refterm.domaintype.impl.DomainTypeResourceBean;
import com.mxi.am.api.resource.sys.refterm.domaintype.impl.dao.DomainTypeDao;
import com.mxi.am.api.resource.sys.refterm.domaintype.impl.dao.JpaDomainTypeDao;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Database test for DomainTypeResourceBean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class DomainTypeResourceBeanTest extends ResourceBeanTest {

   private static final String CODE = "CME";
   private static final String DESCRIPTION = "Calendar based domain type";
   private static final String NAME = "Calendar Measurement";

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( DomainTypeResource.class ).to( DomainTypeResourceBean.class );
               bind( Security.class ).to( CoreSecurity.class );
               bind( EJBContext.class ).toInstance( ejbContext );
               bind( DomainTypeDao.class ).to( JpaDomainTypeDao.class );
            }
         } );

   @Inject
   private DomainTypeResourceBean domainTypeResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext ejbContext;


   @Before
   public void setUp() throws MxException {

      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );

      domainTypeResourceBean.setEJBContext( ejbContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( ejbContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );

   }


   @Test
   public void testGetDomainTypeByCode() throws Exception {
      DomainType domainType = domainTypeResourceBean.get( CODE );
      Assert.assertTrue( CODE.equals( domainType.getCode() ) );
      Assert.assertTrue( DESCRIPTION.equals( domainType.getDescription() ) );
      Assert.assertTrue( NAME.equals( domainType.getName() ) );
   }


   @Test
   public void testGetAllDomainTypeCodes() throws Exception {
      List<DomainType> domainTypes = domainTypeResourceBean.search();

      // We don't want to stick to a fixed number for testing in case some new code introduced in
      // the future may break this test but at the same time we verify it returns more than one
      // records
      Assert.assertTrue( domainTypes.size() > 1 );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetDomainTypeByCodeNotFound() throws Exception {
      domainTypeResourceBean.get( "INVALID_CODE" );
   }


   @Test( expected = AmApiAuthorizationException.class )
   public void testUnauthorizedGetMethod() throws AmApiResourceNotFoundException {

      Mockito.when( principal.getName() ).thenReturn( UNAUTHORIZED );

      domainTypeResourceBean.get( CODE );

   }


   @Test( expected = AmApiAuthorizationException.class )
   public void testUnauthorizedSearchMethod() throws AmApiResourceNotFoundException {

      Mockito.when( principal.getName() ).thenReturn( UNAUTHORIZED );

      domainTypeResourceBean.search();

   }

}
