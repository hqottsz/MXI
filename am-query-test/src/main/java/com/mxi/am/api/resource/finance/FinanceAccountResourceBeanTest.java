package com.mxi.am.api.resource.finance;

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

import com.google.inject.Inject;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.finance.impl.FinanceAccountResourceBean;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query test for FinanceAccount api
 *
 */

@RunWith( MockitoJUnitRunner.class )
public class FinanceAccountResourceBeanTest extends ResourceBeanTest {

   private static final String NAME = "EXPENSE-01";
   private static final String ID = "00000000000000000000000000000001";
   private static final String FAKE_ID = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
   private static final String EXTERNAL_ID = "00000000000000000000000000000003";
   private static final String ACCOUNT_TYPE_CODE = "EXPENSE";
   private static final String ACCOUNT_CODE = "EXPENSE-01";
   private static final boolean DEFAULT_BOOL = true;

   @Inject
   FinanceAccountResourceBean iFinanceAccountResourceBean;

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   @Mock
   private Principal iPrincipal;

   @Mock
   private EJBContext iEJBContext;


   @Before
   public void setUp() throws MxException {

      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );
      iFinanceAccountResourceBean.setEJBContext( iEJBContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( AUTHORIZED );

      initializeTest();

      Mockito.when( iEJBContext.getCallerPrincipal() ).thenReturn( iPrincipal );
      Mockito.when( iPrincipal.getName() ).thenReturn( AUTHORIZED );

   }


   @Test
   public void testGetFinanceAccountByIdSuccess() throws AmApiResourceNotFoundException {
      FinanceAccount lFinanceAccount = iFinanceAccountResourceBean.get( ID );
      assertFinanceAccountObject( getFinanceAccount(), lFinanceAccount );
   }


   @SuppressWarnings( "unused" )
   @Test
   public void testGetFinanceAccountByIdResourceNotFound() {
      try {
         FinanceAccount lFinanceAccount = iFinanceAccountResourceBean.get( FAKE_ID );
         Assert.assertTrue( "Should have thrown AmApiResourceNotFoundException but threw nothing",
               false );
      } catch ( AmApiResourceNotFoundException aE ) {
         Assert.assertEquals( FAKE_ID, aE.getId() );
      } catch ( Exception aE ) {
         Assert.assertTrue(
               "Should have thrown AmApiResourceNoFoundException but threw" + aE.getClass(),
               false );
      }
   }


   @Test
   public void testSearchAccountCode() {
      FinanceAccountSearchParameters lFinanceAccountSearchParameters =
            new FinanceAccountSearchParameters();
      lFinanceAccountSearchParameters.setAccountCode( ACCOUNT_CODE );
      List<FinanceAccount> lFinanceAccounts =
            iFinanceAccountResourceBean.search( lFinanceAccountSearchParameters );
      Assert.assertEquals( 1, lFinanceAccounts.size() );
      assertFinanceAccountObject( getFinanceAccount(), lFinanceAccounts.get( 0 ) );
   }


   @Test
   public void testSearchAccountType() {
      FinanceAccountSearchParameters lFinanceAccountSearchParameters =
            new FinanceAccountSearchParameters();
      lFinanceAccountSearchParameters.setAccountType( ACCOUNT_TYPE_CODE );
      List<FinanceAccount> lFinanceAccounts =
            iFinanceAccountResourceBean.search( lFinanceAccountSearchParameters );
      Assert.assertEquals( 1, lFinanceAccounts.size() );
      assertFinanceAccountObject( getFinanceAccount(), lFinanceAccounts.get( 0 ) );
   }


   @Test
   public void testSearchExternalKey() {
      FinanceAccountSearchParameters lFinanceAccountSearchParameters =
            new FinanceAccountSearchParameters();
      lFinanceAccountSearchParameters.setExternalKey( EXTERNAL_ID );
      List<FinanceAccount> lFinanceAccounts =
            iFinanceAccountResourceBean.search( lFinanceAccountSearchParameters );
      Assert.assertEquals( 1, lFinanceAccounts.size() );
      assertFinanceAccountObject( getFinanceAccount(), lFinanceAccounts.get( 0 ) );
   }


   @Test
   public void testSearchDefault() {
      FinanceAccountSearchParameters lFinanceAccountSearchParameters =
            new FinanceAccountSearchParameters();
      lFinanceAccountSearchParameters.setDefault( DEFAULT_BOOL );
      List<FinanceAccount> lFinanceAccounts =
            iFinanceAccountResourceBean.search( lFinanceAccountSearchParameters );
      Assert.assertEquals( 6, lFinanceAccounts.size() );
   }


   @Test
   public void testSearchNoParams() {
      FinanceAccountSearchParameters lFinanceAccountSearchParameters =
            new FinanceAccountSearchParameters();
      List<FinanceAccount> lFinanceAccounts =
            iFinanceAccountResourceBean.search( lFinanceAccountSearchParameters );
      Assert.assertEquals( 7, lFinanceAccounts.size() );
   }


   @Test
   public void testSearchAllParams() {
      FinanceAccountSearchParameters lFinanceAccountSearchParameters =
            new FinanceAccountSearchParameters();
      lFinanceAccountSearchParameters.setDefault( DEFAULT_BOOL );
      lFinanceAccountSearchParameters.setAccountType( ACCOUNT_TYPE_CODE );
      lFinanceAccountSearchParameters.setAccountCode( ACCOUNT_CODE );
      lFinanceAccountSearchParameters.setExternalKey( EXTERNAL_ID );
      List<FinanceAccount> lFinanceAccounts =
            iFinanceAccountResourceBean.search( lFinanceAccountSearchParameters );
      Assert.assertEquals( 1, lFinanceAccounts.size() );

   }


   private void assertFinanceAccountObject( FinanceAccount aExpectedFinanceAccount,
         FinanceAccount aActualFinanceAccount ) {
      Assert.assertEquals( aExpectedFinanceAccount.getAccountTypeCd(),
            aActualFinanceAccount.getAccountTypeCd() );
      Assert.assertEquals( aExpectedFinanceAccount.getAltId(), aActualFinanceAccount.getAltId() );
      Assert.assertEquals( aExpectedFinanceAccount.getClosed(), aActualFinanceAccount.getClosed() );
      Assert.assertEquals( aExpectedFinanceAccount.getCode(), aActualFinanceAccount.getCode() );
      Assert.assertEquals( aExpectedFinanceAccount.getExternalId(),
            aActualFinanceAccount.getExternalId() );
      Assert.assertEquals( aExpectedFinanceAccount.getName(), aActualFinanceAccount.getName() );
   }


   private FinanceAccount getFinanceAccount() {
      FinanceAccount lFinanceAccount = new FinanceAccount();

      lFinanceAccount.setName( NAME );
      lFinanceAccount.setCode( ACCOUNT_CODE );
      lFinanceAccount.setAccountTypeCd( ACCOUNT_TYPE_CODE );
      lFinanceAccount.setAltId( ID );
      lFinanceAccount.setExternalId( EXTERNAL_ID );

      return lFinanceAccount;
   }

}
