package com.mxi.am.api.resource.sys.refterm.spec2kcustomer;

import java.util.List;

import javax.ejb.EJBContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Inject;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.sys.refterm.spec2kCustomer.Spec2kCustomer;
import com.mxi.am.api.resource.sys.refterm.spec2kcustomer.impl.Spec2kCustomerResourceBean;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query-Test for spec2kCutomerResourceBean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class Spec2kCustomerResourceBeanTest extends ResourceBeanTest {

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   @Inject
   Spec2kCustomerResourceBean iSpec2kCustomerResourceBean;

   @Mock
   private EJBContext iEJBContext;

   private static final String SPEC2K_CODE_DEFAULT = "CTIRE";
   private static final String SPEC2K_CODE_DESC_DEFAULT = "Canadian Tire Store";
   private static final String SPEC2K_CODE = "FTACK";
   private static final String SPEC2K_CODE_DESC = "Ftack long description";


   @Before
   public void setUp() throws MxException {

      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );

      iSpec2kCustomerResourceBean.setEJBContext( iEJBContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();
   }


   /**
    *
    * Tests that the correct Spec2kCustomer is returned given to code.
    *
    * @throws AmApiResourceNotFoundException
    */
   @Test
   public void testGetSpec2kCustomerByCode() throws AmApiResourceNotFoundException {
      Spec2kCustomer lActualSpec2kCustomer = iSpec2kCustomerResourceBean.get( SPEC2K_CODE_DEFAULT );
      Spec2kCustomer lExpectedSpec2kCustomer = getDefaultSpec2kCustomer();
      Assert.assertEquals( lExpectedSpec2kCustomer, lActualSpec2kCustomer );
   }


   @Test
   public void testGetAllSpec2kCustomer() throws AmApiResourceNotFoundException {
      List<Spec2kCustomer> lActualSpec2kCustomers = iSpec2kCustomerResourceBean.search();
      Spec2kCustomer lExpectedDefaultSpec2kCustomer = getDefaultSpec2kCustomer();
      Spec2kCustomer lExpectedSpec2kCustomer = getASpec2kCustomer();

      Assert.assertEquals( 2, lActualSpec2kCustomers.size() );
      Assert.assertEquals( lExpectedDefaultSpec2kCustomer, lActualSpec2kCustomers.get( 0 ) );
      Assert.assertEquals( lExpectedSpec2kCustomer, lActualSpec2kCustomers.get( 1 ) );

   }


   /**
    *
    * Tests that a AmApiResourceNotFoundException is thrown when the code is not found.
    *
    */
   @Test
   public void testGetSpec2kCustomerByInvalidCode() {
      try {
         iSpec2kCustomerResourceBean.get( "INVALID" );
         Assert.fail( "Expected exception" );
      } catch ( AmApiResourceNotFoundException aE ) {
         Assert.assertEquals( "SPEC2K_CUSTOMER INVALID not found", aE.getMessage() );
      }
   }


   /**
    * Creates a default Spec2KCutomer object
    *
    * @return The default Spec2kCustomer object
    */
   private Spec2kCustomer getDefaultSpec2kCustomer() {
      Spec2kCustomer lSpec2kCustomer = new Spec2kCustomer();
      lSpec2kCustomer.setDefaultBool( true );
      lSpec2kCustomer.setDescription( SPEC2K_CODE_DESC_DEFAULT );
      lSpec2kCustomer.setSpec2kCustomerCode( SPEC2K_CODE_DEFAULT );
      return lSpec2kCustomer;

   }


   /**
    * Creates a Spec2KCutomer object
    *
    * @return The Spec2kCustomer object
    */
   private Spec2kCustomer getASpec2kCustomer() {
      Spec2kCustomer lSpec2kCustomer = new Spec2kCustomer();
      lSpec2kCustomer.setDefaultBool( false );
      lSpec2kCustomer.setDescription( SPEC2K_CODE_DESC );
      lSpec2kCustomer.setSpec2kCustomerCode( SPEC2K_CODE );
      return lSpec2kCustomer;

   }
}
