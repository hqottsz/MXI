package com.mxi.am.api.resource.sys.refterm.ietmtype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJBContext;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query test for IetmType Resource Bean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class IetmTypeResourceBeanTest extends ResourceBeanTest {

   public static final String IETM_TYPE_CODE = "PDF";
   public static final String IETM_TYPE_NAME = "PDF";
   public static final String IETM_TYPE_DESCRIPTION = "pdf";

   public static final String IETM_TYPE_CODE2 = "AAA";
   public static final String IETM_TYPE_NAME2 = "AAA";
   public static final String IETM_TYPE_DESCRIPTION2 = "aaa";

   private static final int IETM_TYPE_RECORD_COUNT = 2;
   private static final String NON_EXISTENT_IETM_TYPE_CODE = "XXX";

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( IetmTypeResource.class ).to( IetmTypeResourceBean.class );
               bind( Security.class ).to( CoreSecurity.class );
               bind( EJBContext.class ).toInstance( ejbContext );
            }
         } );

   @Inject
   private IetmTypeResourceBean ietmTypeResourceBean;

   @Mock
   private EJBContext ejbContext;


   @Before
   public void setUp() throws MxException {

      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );
      ietmTypeResourceBean.setEJBContext( ejbContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void testGetIetmTypeByCodeSuccess() throws AmApiResourceNotFoundException {
      IetmType ietmType = ietmTypeResourceBean.get( IETM_TYPE_CODE );
      assertEquals( "Incorrect returned IetmType: ", defaultIetmTypeBuilder(), ietmType );
   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void testSearchAllIetmTypesSuccess() {
      List<IetmType> ietmTypeList = ietmTypeResourceBean.search();
      assertEquals(
            "Incorrect number of Ietm Type records returned, expected " + IETM_TYPE_RECORD_COUNT,
            IETM_TYPE_RECORD_COUNT, ietmTypeList.size() );
      assertTrue( "Incorrect Ietm Type list returned",
            ietmTypeList.containsAll( defaultIetmTypeListBuilder() ) );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetIetmTypeByNonExistentCodeFailure() throws AmApiResourceNotFoundException {
      ietmTypeResourceBean.get( NON_EXISTENT_IETM_TYPE_CODE );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetIetmTypeByNullCodeFailure() throws AmApiResourceNotFoundException {
      ietmTypeResourceBean.get( null );
   }


   private IetmType defaultIetmTypeBuilder() {
      IetmType ietmType = new IetmType();
      ietmType.setCode( IETM_TYPE_CODE );
      ietmType.setName( IETM_TYPE_NAME );
      ietmType.setDescription( IETM_TYPE_DESCRIPTION );
      return ietmType;
   }


   private Collection<IetmType> defaultIetmTypeListBuilder() {
      List<IetmType> ietmTypeList = new ArrayList<IetmType>();

      IetmType ietmType1 = new IetmType();
      ietmType1.setCode( IETM_TYPE_CODE );
      ietmType1.setName( IETM_TYPE_NAME );
      ietmType1.setDescription( IETM_TYPE_DESCRIPTION );

      IetmType ietmType2 = new IetmType();
      ietmType2.setCode( IETM_TYPE_CODE2 );
      ietmType2.setName( IETM_TYPE_NAME2 );
      ietmType2.setDescription( IETM_TYPE_DESCRIPTION2 );

      ietmTypeList.add( ietmType1 );
      ietmTypeList.add( ietmType2 );

      return ietmTypeList;
   }
}
