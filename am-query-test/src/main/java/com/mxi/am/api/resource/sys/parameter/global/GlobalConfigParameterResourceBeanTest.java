package com.mxi.am.api.resource.sys.parameter.global;

import static org.junit.Assert.assertTrue;

import java.security.Principal;

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
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.sys.parameter.global.impl.GlobalConfigParameterResourceBean;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.core.table.utl.JdbcUtlConfigParmDao;
import com.mxi.mx.core.table.utl.UtlConfigParmDao;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Smoke Test for GlobalConfigParameterResourceBean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class GlobalConfigParameterResourceBeanTest extends ResourceBeanTest {

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( GlobalConfigParameterResource.class )
                     .to( GlobalConfigParameterResourceBean.class );
               bind( Security.class ).to( CoreSecurity.class );
               bind( UtlConfigParmDao.class ).to( JdbcUtlConfigParmDao.class );

            }
         } );

   @Inject
   GlobalConfigParameterResourceBean iGlobalConfigParameterResourceBean;

   @Mock
   private Principal iPrincipal;

   @Mock
   private EJBContext iEJBContext;

   private static final String VALID_PARAMETER_TYPE = "LOGIC";
   private static final String VALID_PARAMETER_NAME = "WORK_TYPE_REQUIRED_FOR_WP";
   private static final String VALID_PARAMETER_VALUE = "FALSE";

   private static final String INVALID_PARAMETER_TYPE = "INVALID_TYPE";
   private static final String INVALID_PARAMETER_NAME = "INVALID_NAME";


   private GlobalConfigParameter constructExpectedResults() {

      GlobalConfigParameter lExpectedGlobalConfigParameter = new GlobalConfigParameter();

      lExpectedGlobalConfigParameter.setName( VALID_PARAMETER_NAME );
      lExpectedGlobalConfigParameter.setType( VALID_PARAMETER_TYPE );
      lExpectedGlobalConfigParameter.setValue( VALID_PARAMETER_VALUE );

      return lExpectedGlobalConfigParameter;
   }


   @Before
   public void setUp() throws MxException {

      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );
      iGlobalConfigParameterResourceBean.setEJBContext( iEJBContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( iEJBContext.getCallerPrincipal() ).thenReturn( iPrincipal );
      Mockito.when( iPrincipal.getName() ).thenReturn( AUTHORIZED );

   }


   /**
    *
    * Test correct GlobalConfigParameter object returns for a valid type and name - success path
    *
    * @throws AmApiResourceNotFoundException
    */
   @Test
   public void testGetGlobalConfigParameterSuccess() throws AmApiResourceNotFoundException {

      GlobalConfigParameter lGlobalConfigParameter =
            iGlobalConfigParameterResourceBean.get( VALID_PARAMETER_TYPE, VALID_PARAMETER_NAME );
      assertTrue( constructExpectedResults().equals( lGlobalConfigParameter ) );

   }


   /**
    *
    * Test null returns for invalid type and name - not found
    *
    * @throws AmApiResourceNotFoundException
    */
   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetGlobalConfigParameterNotFound() throws AmApiResourceNotFoundException {
      iGlobalConfigParameterResourceBean.get( INVALID_PARAMETER_TYPE, INVALID_PARAMETER_NAME );
   }


   /**
    *
    * Test null returns for null type and name - bad request
    *
    * @throws AmApiResourceNotFoundException
    */
   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetGlobalConfigParameterBadRequest() throws AmApiResourceNotFoundException {
      iGlobalConfigParameterResourceBean.get( null, null );
   }

}
