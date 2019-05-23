package com.mxi.am.api.resource.sys.refterm.impact;

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
import com.mxi.am.api.resource.sys.refterm.impact.impl.ImpactResourceBean;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query test for Impact ResourceBean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class ImpactResourceBeanTest extends ResourceBeanTest {

   private static final String IMPACT_CODE = "ETOPS";
   public static final String IMPACT_NAME = "ETOPS";
   public static final String IMPACT_DESCRIPTION = "ETOPS Description...";

   private static final String IMPACT_CODE2 = "EL";
   public static final String IMPACT_NAME2 = "Electrical Load";
   public static final String IMPACT_DESCRIPTION2 = "Electrical Load Description...";

   private static final String IMPACT_CODE3 = "WB";
   public static final String IMPACT_NAME3 = "Weight and Balance";
   public static final String IMPACT_DESCRIPTION3 = "Weight and Balance Description...";

   private static final int IMPACT_RECORD_COUNT = 4;
   private static final String NON_EXIST_IMPACT_CODE = "XXX";
   private static final String NULL_IMPACT_CODE = null;

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( ImpactResource.class ).to( ImpactResourceBean.class );
               bind( Security.class ).to( CoreSecurity.class );
               bind( EJBContext.class ).toInstance( ejbContext );
            }
         } );

   @Inject
   private ImpactResourceBean impactResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext ejbContext;


   @Before
   public void setUp() throws MxException {

      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );
      impactResourceBean.setEJBContext( ejbContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( ejbContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );

   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void testGetImpactByCodeSuccess200() throws AmApiResourceNotFoundException {
      Impact impact = impactResourceBean.get( IMPACT_CODE );
      assertEquals( defaultImpactBuilder(), impact );
   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void testSearchAllImpactsSuccess200() {
      List<Impact> impactList = impactResourceBean.search();
      assertEquals( IMPACT_RECORD_COUNT, impactList.size() );
      assertTrue( impactList.containsAll( defaultImpactListBuilder() ) );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetImpactByNonExistCodeFailure404() throws AmApiResourceNotFoundException {
      impactResourceBean.get( NON_EXIST_IMPACT_CODE );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetImpactByNullCodeFailure404() throws AmApiResourceNotFoundException {
      impactResourceBean.get( NULL_IMPACT_CODE );
   }


   private Impact defaultImpactBuilder() {
      Impact impact = new Impact();
      impact.setCode( IMPACT_CODE );
      impact.setName( IMPACT_NAME );
      impact.setDescription( IMPACT_DESCRIPTION );
      return impact;
   }


   private List<Impact> defaultImpactListBuilder() {

      List<Impact> impactList = new ArrayList<Impact>();

      Impact impact1 = new Impact();
      impact1.setCode( IMPACT_CODE );
      impact1.setName( IMPACT_NAME );
      impact1.setDescription( IMPACT_DESCRIPTION );

      Impact impact2 = new Impact();
      impact2.setCode( IMPACT_CODE2 );
      impact2.setName( IMPACT_NAME2 );
      impact2.setDescription( IMPACT_DESCRIPTION2 );

      Impact impact3 = new Impact();
      impact3.setCode( IMPACT_CODE3 );
      impact3.setName( IMPACT_NAME3 );
      impact3.setDescription( IMPACT_DESCRIPTION3 );

      impactList.add( impact1 );
      impactList.add( impact2 );
      impactList.add( impact3 );

      return impactList;
   }
}
