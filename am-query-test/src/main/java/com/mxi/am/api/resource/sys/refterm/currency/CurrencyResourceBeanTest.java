package com.mxi.am.api.resource.sys.refterm.currency;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.sql.SQLException;

import javax.ejb.EJBContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.sys.refterm.currency.impl.CurrencyResourceBean;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.testing.ResourceBeanTest;


@RunWith( MockitoJUnitRunner.class )
public class CurrencyResourceBeanTest extends ResourceBeanTest {

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( CurrencyResource.class ).to( CurrencyResourceBean.class );
               bind( Security.class ).to( CoreSecurity.class );
               bind( EJBContext.class ).toInstance( iEJBContext );
            }
         } );

   @Inject
   CurrencyResourceBean iCurrencyResourceBean;

   @Mock
   private Principal iPrincipal;

   @Mock
   private EJBContext iEJBContext;

   private static final String CURRENCY_CD = "CAD";
   private static final String CURRENCY_SDESC = "Canadian Dollar";
   private static final Integer SUB_UNITS_QTY = 2;
   private static final BigDecimal EXCHG_RATE = BigDecimal.ONE;

   private static final String FAKE_CURRENCY_CD = "INVALID";


   @Before
   public void setUp() throws MxException, SQLException {

      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );

      iCurrencyResourceBean.setEJBContext( iEJBContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( iEJBContext.getCallerPrincipal() ).thenReturn( iPrincipal );
      Mockito.when( iPrincipal.getName() ).thenReturn( AUTHORIZED );

   }


   @Test
   public void testGetCurrencyNotFound() throws Exception {
      try {
         iCurrencyResourceBean.get( FAKE_CURRENCY_CD );
      } catch ( AmApiResourceNotFoundException aE ) {
         Assert.assertEquals( FAKE_CURRENCY_CD, aE.getId() );
      }
   }


   @Test
   public void testGetCurrencyByCode() throws Exception {
      Currency lExpectedCurrency = new Currency();
      lExpectedCurrency.setCode( CURRENCY_CD );
      lExpectedCurrency.setExchangeRate( EXCHG_RATE );
      lExpectedCurrency.setName( CURRENCY_SDESC );
      lExpectedCurrency.setSubUnitsQuantity( SUB_UNITS_QTY );

      Currency lFoundCurrency = iCurrencyResourceBean.get( CURRENCY_CD );
      assertCurrency( lExpectedCurrency, lFoundCurrency );
   }


   private void assertCurrency( Currency aExpectedCurrency, Currency aActualCurrency )
         throws JsonProcessingException, IOException {

      Assert.assertEquals( aExpectedCurrency.getCode(), aActualCurrency.getCode() );
      Assert.assertEquals( aExpectedCurrency.getName(), aActualCurrency.getName() );
      Assert.assertEquals( aExpectedCurrency.getExchangeRate(), aActualCurrency.getExchangeRate() );
      Assert.assertEquals( aExpectedCurrency.getSubUnitsQuantity(),
            aActualCurrency.getSubUnitsQuantity() );

   }

}
