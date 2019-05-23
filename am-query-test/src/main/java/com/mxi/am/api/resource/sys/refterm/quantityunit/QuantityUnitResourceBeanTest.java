package com.mxi.am.api.resource.sys.refterm.quantityunit;

import java.math.BigDecimal;
import java.sql.SQLException;

import javax.ejb.EJBContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.sys.refterm.quantityunit.impl.QuantityUnitResourceBean;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.testing.ResourceBeanTest;


@RunWith( MockitoJUnitRunner.class )
public class QuantityUnitResourceBeanTest extends ResourceBeanTest {

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( QuantityUnitResource.class ).to( QuantityUnitResourceBean.class );
               bind( Security.class ).to( CoreSecurity.class );
               bind( EJBContext.class ).toInstance( ejbContext );
            }
         } );

   @Inject
   QuantityUnitResourceBean quantityUnitResourceBean;

   @Mock
   private EJBContext ejbContext;

   private static final String QTY_UNIT_CD = "EA";
   private static final String QTY_UNIT_SDESC = "Each";
   private static final BigDecimal DECIMAL_PLACES_QTY = BigDecimal.ZERO;

   private static final String FAKE_QTY_UNIT_CD = "INVALID";


   @Before
   public void setUp() throws MxException, SQLException {

      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );

      quantityUnitResourceBean.setEJBContext( ejbContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetQtyUnitNotFound() throws Exception {
      quantityUnitResourceBean.get( FAKE_QTY_UNIT_CD );
   }


   @Test
   public void testGetQtyUnitByCode() throws Exception {
      QuantityUnit expectedQuantityUnit = new QuantityUnit();
      expectedQuantityUnit.setCode( QTY_UNIT_CD );
      expectedQuantityUnit.setDecimalPlacesQuantity( DECIMAL_PLACES_QTY );
      expectedQuantityUnit.setName( QTY_UNIT_SDESC );

      QuantityUnit actualQuantityUnit = quantityUnitResourceBean.get( QTY_UNIT_CD );
      Assert.assertEquals( expectedQuantityUnit, actualQuantityUnit );
   }

}
