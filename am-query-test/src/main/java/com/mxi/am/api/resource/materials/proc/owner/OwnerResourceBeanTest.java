package com.mxi.am.api.resource.materials.proc.owner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.materials.proc.owner.impl.OwnerResourceBean;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query test for the Owner Resource Bean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class OwnerResourceBeanTest extends ResourceBeanTest {

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( OwnerResource.class ).to( OwnerResourceBean.class );
               bind( Security.class ).to( CoreSecurity.class );
            }
         } );

   @Inject
   private OwnerResourceBean ownerResourceBean;

   private Owner owner1 = new Owner();
   private Owner owner2 = new Owner();
   private Owner owner3 = new Owner();
   private Owner defaultOwner = new Owner();

   private static final String ORG_ID = "00000000000000000000000000000101";

   private static final String OWNER1_ID = "00000000000000000000000000000001";
   private static final String OWNER1_CD = "OWNER1";
   private static final String OWNER1_NAME = "Owner 1";

   private static final String OWNER2_ID = "00000000000000000000000000000002";
   private static final String OWNER2_CD = "OWNER2";
   private static final String OWNER2_NAME = "Owner 2";

   private static final String OWNER3_ID = "00000000000000000000000000000003";
   private static final String OWNER3_CD = "OWNER3";
   private static final String OWNER3_NAME = "Owner 3";

   private static final String DEFAULT_OWNER_ID = "00000000000000000000000000000004";
   private static final String DEFAULT_OWNER_CD = "DEF_OWN";
   private static final String DEFAULT_OWNER_NAME = "Default Owner";

   private static final String INVALID_ID = "00000000000000000000000000000044";
   private static final String EXCEPTION_TYPE_ID = "OWNER";

   private static final String INVALID_OWNER_CD = "OWNER4";


   /**
    * Setup injection binds
    *
    * @throws MxException
    */
   @Before
   public void setUp() throws MxException {
      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );

      constructOwners();
      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );
      initializeTest();

   }


   /**
    * Test search-owners-by-code functionality
    *
    */
   @Test
   @SuppressWarnings( "unchecked" )
   @CSIContractTest( Project.UPS )
   public void testSearchOwnersSuccess() {
      List<String> ownerCodes = new ArrayList<String>();
      ownerCodes.add( OWNER1_CD );
      ownerCodes.add( OWNER2_CD );

      OwnerSearchParameters ownerSearchParameters = new OwnerSearchParameters( ownerCodes, false );

      List<Owner> owners = ownerResourceBean.search( ownerSearchParameters );
      assertEquals( "Incorrect number of owners returned: ", 2, owners.size() );

      assertTrue(
            "Returned list of owners [" + owners + "] do not contain the owner [" + owner1 + "].",
            owners.contains( owner1 ) );
      assertTrue(
            "Returned list of owners [" + owners + "] do not contain the owner [" + owner2 + "].",
            owners.contains( owner2 ) );

   }


   @Test
   public void testSearchOwnersNotFoundSuccess() {
      List<String> ownerCodes = new ArrayList<String>();
      ownerCodes.add( INVALID_OWNER_CD );

      OwnerSearchParameters ownerSearchParameters = new OwnerSearchParameters( ownerCodes, null );

      List<Owner> owners = ownerResourceBean.search( ownerSearchParameters );

      assertEquals( "Incorrect number of owners returned: ", 0, owners.size() );
   }


   @Test
   public void testSearchDefaultOwnerSuccess() {
      OwnerSearchParameters ownerSearchParameters = new OwnerSearchParameters( null, true );

      List<Owner> owners = ownerResourceBean.search( ownerSearchParameters );
      assertEquals( "Incorrect number of owners returned: ", 1, owners.size() );

      assertTrue( "Returned list of owners [" + owners + "] do not contain the owner ["
            + defaultOwner + "].", owners.contains( defaultOwner ) );

   }


   @Test
   public void testSearchNonDefaultOwnerByCodesSuccess() {
      List<String> ownerCodes = new ArrayList<String>();
      ownerCodes.add( OWNER1_CD );
      ownerCodes.add( OWNER2_CD );

      OwnerSearchParameters ownerSearchParameters = new OwnerSearchParameters( ownerCodes, false );

      List<Owner> owners = ownerResourceBean.search( ownerSearchParameters );
      assertEquals( "Incorrect number of owners returned: ", 2, owners.size() );

      assertTrue(
            "Returned list of owners [" + owners + "] do not contain the owner [" + owner1 + "].",
            owners.contains( owner1 ) );
      assertTrue(
            "Returned list of owners [" + owners + "] do not contain the owner [" + owner2 + "].",
            owners.contains( owner2 ) );

   }


   @Test
   public void testSearchOwnersNullParameters() {

      List<Owner> owners = ownerResourceBean.search( new OwnerSearchParameters( null, null ) );
      Assert.assertEquals( "Incorrect number of owners returned: ", 4, owners.size() );

      assertTrue(
            "Returned list of owners [" + owners + "] do not contain the owner [" + owner1 + "].",
            owners.contains( owner1 ) );
      assertTrue(
            "Returned list of owners [" + owners + "] do not contain the owner [" + owner2 + "].",
            owners.contains( owner2 ) );
      assertTrue(
            "Returned list of owners [" + owners + "] do not contain the owner [" + owner3 + "].",
            owners.contains( owner3 ) );
      assertTrue( "Returned list of owners [" + owners + "] do not contain the owner ["
            + defaultOwner + "].", owners.contains( defaultOwner ) );

   }


   /**
    * Test get-owners-by-id functionality.
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   @CSIContractTest( Project.EXT_MATERIAL_PLANNER )
   public void testGetOwnerByIdSuccess() throws AmApiResourceNotFoundException {
      Owner owner = ownerResourceBean.get( OWNER3_ID );

      assertEquals( "Incorrect returned owner: ", owner3, owner );

   }


   /**
    * Test get-owners-by-id functionality.
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void testGetOwnerByIdNotFound() throws AmApiResourceNotFoundException {
      try {
         Owner owner = ownerResourceBean.get( INVALID_ID );
         Assert.fail( "Did not throw AmApiResourceNotFoundException" );
      } catch ( AmApiResourceNotFoundException e ) {
         String message = EXCEPTION_TYPE_ID + " " + INVALID_ID + " not found";
         assertEquals( "Incorrect Error Message", message, e.getMessage() );
      }

   }


   private void constructOwners() {
      owner1.setId( OWNER1_ID );
      owner1.setCode( OWNER1_CD );
      owner1.setName( OWNER1_NAME );
      owner1.setOrganizationId( ORG_ID );

      owner2.setId( OWNER2_ID );
      owner2.setCode( OWNER2_CD );
      owner2.setName( OWNER2_NAME );
      owner2.setOrganizationId( ORG_ID );

      owner3.setId( OWNER3_ID );
      owner3.setCode( OWNER3_CD );
      owner3.setName( OWNER3_NAME );
      owner3.setOrganizationId( ORG_ID );

      defaultOwner.setId( DEFAULT_OWNER_ID );
      defaultOwner.setCode( DEFAULT_OWNER_CD );
      defaultOwner.setName( DEFAULT_OWNER_NAME );
      defaultOwner.setOrganizationId( ORG_ID );
   }

}
