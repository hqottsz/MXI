package com.mxi.am.api.resource.sys.refterm.failtype;

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
import com.mxi.am.api.resource.sys.refterm.failtype.impl.FailTypeResourceBean;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query test for FailType Resource Bean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class FailTypeResourceBeanTest extends ResourceBeanTest {

   public static final String FAIL_TYPE_CODE = "A";
   public static final String FAIL_TYPE_NAME = "Bent, Buckled, Distorted, Twisted";
   public static final String FAIL_TYPE_DESCRIPTION = "Bent, Buckled, Distorted, Twisted";

   public static final String FAIL_TYPE_CODE2 = "B";
   public static final String FAIL_TYPE_NAME2 = "Binding, Jammed, Seized, Stiff, Dragging";
   public static final String FAIL_TYPE_DESCRIPTION2 = "Binding, Jammed, Seized, Stiff, Dragging";

   public static final String FAIL_TYPE_CODE3 = "C";
   public static final String FAIL_TYPE_NAME3 =
         "Broken, Burst, Ruptured, Torn, Open Circuit, Sheared";
   public static final String FAIL_TYPE_DESCRIPTION3 =
         "Broken, Burst, Ruptured, Torn, Open Circuit, Sheared";

   private static final int FAIL_TYPE_RECORD_COUNT = 3;
   private static final String NON_EXISTENT_FAIL_TYPE_CODE = "XXX";

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( FailTypeResource.class ).to( FailTypeResourceBean.class );
               bind( Security.class ).to( CoreSecurity.class );
               bind( EJBContext.class ).toInstance( ejbContext );
            }
         } );

   @Inject
   private FailTypeResourceBean failTypeResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext ejbContext;


   @Before
   public void setUp() throws MxException {

      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );
      failTypeResourceBean.setEJBContext( ejbContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( ejbContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );

   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void testGetFailTypeByCodeSuccess() throws AmApiResourceNotFoundException {
      FailType failType = failTypeResourceBean.get( FAIL_TYPE_CODE );
      assertEquals( dafaultFailTypeBuilder(), failType );
   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void testSearchAllFailTypesSuccess() {
      List<FailType> failTypeList = failTypeResourceBean.search();
      assertEquals( FAIL_TYPE_RECORD_COUNT, failTypeList.size() );
      assertTrue( failTypeList.containsAll( defaultFailTypeListBuilder() ) );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetFailTypeByNonExistentCodeFailure() throws AmApiResourceNotFoundException {
      failTypeResourceBean.get( NON_EXISTENT_FAIL_TYPE_CODE );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetFailTypeByNullCodeFailure() throws AmApiResourceNotFoundException {
      failTypeResourceBean.get( null );
   }


   private FailType dafaultFailTypeBuilder() {
      FailType failType = new FailType();
      failType.setCode( FAIL_TYPE_CODE );
      failType.setName( FAIL_TYPE_NAME );
      failType.setDescription( FAIL_TYPE_DESCRIPTION );
      return failType;
   }


   private List<FailType> defaultFailTypeListBuilder() {
      List<FailType> failTypeList = new ArrayList<FailType>();

      FailType failType1 = new FailType();
      failType1.setCode( FAIL_TYPE_CODE );
      failType1.setName( FAIL_TYPE_NAME );
      failType1.setDescription( FAIL_TYPE_DESCRIPTION );

      FailType failType2 = new FailType();
      failType2.setCode( FAIL_TYPE_CODE2 );
      failType2.setName( FAIL_TYPE_NAME2 );
      failType2.setDescription( FAIL_TYPE_DESCRIPTION2 );

      FailType failType3 = new FailType();
      failType3.setCode( FAIL_TYPE_CODE3 );
      failType3.setName( FAIL_TYPE_NAME3 );
      failType3.setDescription( FAIL_TYPE_DESCRIPTION3 );

      failTypeList.add( failType1 );
      failTypeList.add( failType2 );
      failTypeList.add( failType3 );

      return failTypeList;
   }

}
