package com.mxi.am.api.resource.organization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.Principal;
import java.util.List;

import javax.ejb.EJBContext;
import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.exception.KeyConversionException;
import com.mxi.am.api.resource.organization.impl.OrganizationResourceBean;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Database-level tests for Organization API
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class OrganizationResourceBeanTest extends ResourceBeanTest {

   private static String INVALID_ORG_CD = "INV_ORG";
   private static String TEST_OWNER_CD = "TEST_OWN";
   private static String TEST_OWNER_NAME = "Test Owner";
   private static String TEST_OWNER_ID = "00000000000000000000000000000001";
   private static String INVALID_TEST_OWNER_ID = "00000000000000000000000000000002";
   private static String TEST_LDESC = "Long Description";
   private static String TEST_TYPE_CD = "OPERATOR";
   private static String TEST_CD_PATH = "MXI/";
   private static String TEST_PARENT_ID = "00000000000000000000000000000005";
   private static boolean TEST_COMPANY_BOOL = true;

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( OrganizationResource.class ).to( OrganizationResourceBean.class );
               bind( Security.class ).to( CoreSecurity.class );
               bind( EJBContext.class ).toInstance( iEJBContext );
            }
         } );

   @Inject
   OrganizationResourceBean iOrganizationResourceBean;

   @Mock
   private Principal iPrincipal;

   @Mock
   private EJBContext iEJBContext;


   @Before
   public void setUp()
         throws NamingException, MxException, KeyConversionException, AmApiBusinessException {

      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );
      iOrganizationResourceBean.setEJBContext( iEJBContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( iEJBContext.getCallerPrincipal() ).thenReturn( iPrincipal );
      Mockito.when( iPrincipal.getName() ).thenReturn( AUTHORIZED );

   }


   @Test
   /**
    * Test finding a single organization code.
    */
   public void testSuccessSearchOrganizationByCode() {
      OrganizationSearchParameters lParameters = new OrganizationSearchParameters();
      lParameters.addOrganizationCode( TEST_OWNER_CD );
      List<Organization> lOrgs = iOrganizationResourceBean.search( lParameters );

      assertEquals( "Expected a single result to be returned for code " + TEST_OWNER_CD + ":", 1,
            lOrgs.size() );

      Organization lOrganizationExpected = constructOrganization();

      assertEquals( "Expected organization object does not match with actual object",
            lOrganizationExpected, lOrgs.get( 0 ) );
   }


   @Test
   /**
    * Tests that putting an invalid code will return an empty list.
    */
   public void testSuccessOrganizationNotFound() {
      OrganizationSearchParameters lParameters = new OrganizationSearchParameters();
      lParameters.addOrganizationCode( INVALID_ORG_CD );
      List<Organization> lOrgs = iOrganizationResourceBean.search( lParameters );

      assertTrue(
            "Expected empty list of organization codes, but found " + lOrgs.size() + " elements.",
            lOrgs.isEmpty() );
   }


   /**
    * Test get organization by id.
    */
   @Test
   public void testSuccessGetOrganizationById() throws AmApiResourceNotFoundException {

      Organization lOrg = iOrganizationResourceBean.get( TEST_OWNER_ID );

      Organization lOrganizationExpected = constructOrganization();

      assertEquals( "Expected organization object does not match with actual object",
            lOrganizationExpected, lOrg );
   }


   /**
    * Tests get organization by invalid id.
    */
   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetOrganizationByIdNotFound() throws AmApiResourceNotFoundException {
      iOrganizationResourceBean.get( INVALID_TEST_OWNER_ID );
   }


   private Organization constructOrganization() {

      Organization lOrganization = new Organization();

      lOrganization.setId( TEST_OWNER_ID );
      lOrganization.setOrgCode( TEST_OWNER_CD );
      lOrganization.setShortDesc( TEST_OWNER_NAME );
      lOrganization.setLongDesc( TEST_LDESC );
      lOrganization.setOrgType( TEST_TYPE_CD );
      lOrganization.setCodePath( TEST_CD_PATH );
      lOrganization.setParentOrgId( TEST_PARENT_ID );
      lOrganization.setIsCompany( TEST_COMPANY_BOOL );

      return lOrganization;
   }
}
