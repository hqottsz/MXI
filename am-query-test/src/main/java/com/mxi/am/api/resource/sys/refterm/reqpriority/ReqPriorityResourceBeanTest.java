package com.mxi.am.api.resource.sys.refterm.reqpriority;

import java.security.Principal;
import java.util.ArrayList;
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
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.sys.refterm.reqpriority.impl.ReqPriorityResourceBean;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.testing.ResourceBeanTest;


@RunWith( MockitoJUnitRunner.class )
public class ReqPriorityResourceBeanTest extends ResourceBeanTest {

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( ReqPriorityResource.class ).to( ReqPriorityResourceBean.class );
               bind( Security.class ).to( CoreSecurity.class );
               bind( EJBContext.class ).toInstance( iEJBContext );
            }
         } );

   @Inject
   ReqPriorityResourceBean iReqPriorityResourceBean;

   @Mock
   private Principal iPrincipal;

   @Mock
   private EJBContext iEJBContext;

   private static final String CODE_1 = "AOG";
   private static final String CODE_2 = "NORMAL";
   private static final String CODE_3 = "BLKOUT";

   private static final Integer ORDER_1 = 10;
   private static final Integer ORDER_2 = 100;
   private static final Integer ORDER_3 = 0;

   private static final String NAME_1 = "Aircraft on Ground";
   private static final String NAME_2 = "Normal";
   private static final String NAME_3 = "N/A";

   private static final String DESC_1 =
         "Urgent demand since it is currently grounding the aircraft.";
   private static final String DESC_2 = "Can take more than one week; often 4-6 weeks";
   private static final String DESC_3 = "N/A";

   private static final String FAKE_CD = "AOG-1";


   @Before
   public void setUp() throws MxException {

      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );

      iReqPriorityResourceBean.setEJBContext( iEJBContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( iEJBContext.getCallerPrincipal() ).thenReturn( iPrincipal );
      Mockito.when( iPrincipal.getName() ).thenReturn( AUTHORIZED );

   }


   @Test
   public void testGetPriorityByCode() throws Exception {
      ReqPriority lReqPriority = iReqPriorityResourceBean.get( CODE_1 );
      Assert.assertTrue( getPriorityList().get( 0 ).equals( lReqPriority ) );
   }


   @Test
   public void testGetPriorityNotFound() throws Exception {
      try {
         ReqPriority lReqPriority = iReqPriorityResourceBean.get( FAKE_CD );
      } catch ( AmApiResourceNotFoundException aE ) {
         Assert.assertEquals( FAKE_CD, aE.getId() );
      }
   }


   @Test
   public void testSearchAllPriorities() {
      List<ReqPriority> lExpectedReqPriorities = getPriorityList();
      List<ReqPriority> lReqPriorities = iReqPriorityResourceBean.search();
      Assert.assertEquals( 3, lReqPriorities.size() );
      Assert.assertTrue( lExpectedReqPriorities.get( 0 ).equals( lReqPriorities.get( 0 ) ) );
      Assert.assertTrue( lExpectedReqPriorities.get( 1 ).equals( lReqPriorities.get( 1 ) ) );
      Assert.assertTrue( lExpectedReqPriorities.get( 2 ).equals( lReqPriorities.get( 2 ) ) );
   }


   private List<ReqPriority> getPriorityList() {
      List<ReqPriority> lReqPriorityList = new ArrayList<ReqPriority>();

      ReqPriority lReqPriority1 = new ReqPriority();
      lReqPriority1.setCode( CODE_1 );
      lReqPriority1.setOrder( ORDER_1 );
      lReqPriority1.setName( NAME_1 );
      lReqPriority1.setDescription( DESC_1 );
      lReqPriorityList.add( lReqPriority1 );

      ReqPriority lReqPriority2 = new ReqPriority();
      lReqPriority2.setCode( CODE_2 );
      lReqPriority2.setOrder( ORDER_2 );
      lReqPriority2.setName( NAME_2 );
      lReqPriority2.setDescription( DESC_2 );
      lReqPriorityList.add( lReqPriority2 );

      ReqPriority lReqPriority3 = new ReqPriority();
      lReqPriority3.setCode( CODE_3 );
      lReqPriority3.setOrder( ORDER_3 );
      lReqPriority3.setName( NAME_3 );
      lReqPriority3.setDescription( DESC_3 );
      lReqPriorityList.add( lReqPriority3 );

      return lReqPriorityList;
   }

}
