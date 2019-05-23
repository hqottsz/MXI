package com.mxi.am.api.resource.materials.proc.manufacturer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.security.Principal;
import java.util.List;

import javax.ejb.EJBContext;
import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Inject;
import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.exception.KeyConversionException;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query test for the Manufacturer REST API
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class ManufacturerResourceBeanTest extends ResourceBeanTest {

   private final Manufacturer manufacturer1 = new Manufacturer();
   private final Manufacturer manufacturer2 = new Manufacturer();
   private final Manufacturer manufacturer3 = new Manufacturer();
   private final Manufacturer manufacturer4 = new Manufacturer();
   private final Manufacturer manufacturer5 = new Manufacturer();
   private final Manufacturer manufacturer6 = new Manufacturer();

   @Inject
   private ManufacturerResourceBean testManufacturerResource;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext ejbContext;

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public ExpectedException thrown = ExpectedException.none();


   @Before
   public void setUp()
         throws NamingException, MxException, KeyConversionException, AmApiBusinessException {

      InjectorContainer.get().injectMembers( this );
      testManufacturerResource.setEJBContext( ejbContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      setupData();

      Mockito.when( ejbContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );

   }


   /**
    * Basic success path for get-by-id
    *
    * @throws AmApiResourceNotFoundException
    */
   @Test
   public void testSuccessGet() throws AmApiResourceNotFoundException {
      Manufacturer manufacturer = testManufacturerResource.get( manufacturer1.getId() );

      assertManufacturer( manufacturer1, manufacturer );
   }


   /**
    * Test a manufacturer that only has a code, no other data.
    *
    * @throws AmApiResourceNotFoundException
    */
   @Test
   public void testGetMinimalData() throws AmApiResourceNotFoundException {
      Manufacturer manufacturer = testManufacturerResource.get( manufacturer4.getId() );

      assertManufacturer( manufacturer4, manufacturer );
   }


   /**
    * Search by params, find multiple manufacturers.
    *
    */
   @Test
   public void testSearchByParamsSuccess() {
      ManufacturerSearchParameters parameters = new ManufacturerSearchParameters();
      parameters.addCode( manufacturer2.getCode() );
      parameters.addCode( manufacturer3.getCode() );
      parameters.addName( manufacturer2.getName() );
      parameters.addName( manufacturer3.getName() );

      List<Manufacturer> manufacturers = testManufacturerResource.search( parameters );
      assertEquals( "Incorrect number of manufacturers found: ", 2, manufacturers.size() );
      if ( manufacturer2.getCode().equals( manufacturers.get( 0 ).getCode() ) ) {
         assertManufacturer( manufacturer2, manufacturers.get( 0 ) );
         assertManufacturer( manufacturer3, manufacturers.get( 1 ) );
      } else if ( manufacturer3.getCode().equals( manufacturers.get( 0 ).getCode() ) ) {
         assertManufacturer( manufacturer3, manufacturers.get( 0 ) );
         assertManufacturer( manufacturer2, manufacturers.get( 1 ) );
      }
   }


   /**
    * Test that an exception is thrown when a get-by-id fails to return a result.
    *
    * @throws AmApiResourceNotFoundException
    */
   @Test
   public void testGetByIdNoResults() throws AmApiResourceNotFoundException {
      thrown.expect( AmApiResourceNotFoundException.class );
      thrown.expectMessage( "MANUFACTURER AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA not found" );

      testManufacturerResource.get( "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" );

   }


   /**
    * Test that a search that has no matches returns no manufacturers.
    */
   @Test
   public void testSearchNoResults() {
      ManufacturerSearchParameters parameters = new ManufacturerSearchParameters();
      parameters.addCode( "NONE_FOUND" );

      List<Manufacturer> manufacturers = testManufacturerResource.search( parameters );
      assertEquals( "Incorrect number of manufacturers found: ", 0, manufacturers.size() );
   }


   /**
    * Test search for two manufacturers with the same name - should return both.
    *
    */
   @Test
   public void testSearchDuplicateNames() {
      ManufacturerSearchParameters parameters = new ManufacturerSearchParameters();
      parameters.addName( "Duplicate Manufacturer" );

      List<Manufacturer> manufacturers = testManufacturerResource.search( parameters );
      assertEquals( "Incorrect number of manufacturers found: ", 2, manufacturers.size() );

   }


   /**
    * Test that an unconstrained search returns all manufacturers, and not an error.
    *
    * In addition to the manufacturers instantiated in this class, there are two extras in the query
    * DB, 'LOCAL' and 'N/A', that are created.
    */
   @Test
   public void testUnconstraintedSearch() {
      ManufacturerSearchParameters parameters = new ManufacturerSearchParameters();

      List<Manufacturer> manufacturers = testManufacturerResource.search( parameters );
      assertEquals( "Incorrect number of manufacturers found: ", 8, manufacturers.size() );

   }


   private void assertManufacturer( Manufacturer expectedManufacturer,
         Manufacturer retrievedManufacturer ) {

      Manufacturer.Address expectedAddress = expectedManufacturer.getAddress();
      Manufacturer.Address retrievedAddress = retrievedManufacturer.getAddress();

      assertEquals( "Id was not the expected value: ", expectedManufacturer.getId(),
            retrievedManufacturer.getId() );
      assertEquals( "Code was not the expected value: ", expectedManufacturer.getCode(),
            retrievedManufacturer.getCode() );
      assertEquals( "Name was not the expected value: ", expectedManufacturer.getName(),
            retrievedManufacturer.getName() );

      if ( expectedAddress != null ) {
         assertNotNull( "Returned address is null, though the expected address is not.",
               retrievedAddress );
         assertEquals( "Country Code was not the expected value: ",
               expectedAddress.getCountryCode(), retrievedAddress.getCountryCode() );
         assertEquals( "State Code was not the expected value: ", expectedAddress.getStateCode(),
               retrievedAddress.getStateCode() );
         assertEquals( "City Name was not the expected value: ", expectedAddress.getCity(),
               retrievedAddress.getCity() );
         assertEquals( "Street Address was not the expected value: ",
               expectedAddress.getStreetAddress(), retrievedAddress.getStreetAddress() );
         assertEquals( "Zip Code was not the expected value: ", expectedAddress.getZipCode(),
               retrievedAddress.getZipCode() );
         assertEquals( "Phone Number was not the expected value: ",
               expectedAddress.getPhoneNumber(), retrievedAddress.getPhoneNumber() );
         assertEquals( "Fax Number was not the expected value: ", expectedAddress.getFaxNumber(),
               retrievedAddress.getFaxNumber() );
         assertEquals( "Email Address was not the expected value: ", expectedAddress.getEmail(),
               retrievedAddress.getEmail() );
      }

   }


   /**
    * Creates the manufacturers that will be used as comparison
    *
    */
   private void setupData() {
      manufacturer1.setId( "10000000000000000000000000000000" );
      manufacturer1.setCode( "MANUFACT1" );
      manufacturer1.setName( "Test Manufacturer 1" );
      Manufacturer.Address manAddress1 = new Manufacturer.Address();
      manAddress1.setCountryCode( "CAN" );
      manAddress1.setStateCode( "ON" );
      manAddress1.setCity( "Ottawa" );
      manAddress1.setStreetAddress( "123 Airport Rd" );
      manAddress1.setZipCode( "A1B 2C3" );
      manAddress1.setPhoneNumber( "123-456-7890" );
      manAddress1.setFaxNumber( "098-765-4321" );
      manAddress1.setEmail( "build@airport.com" );
      manufacturer1.setAddress( manAddress1 );

      manufacturer2.setId( "20000000000000000000000000000000" );
      manufacturer2.setCode( "MANUFACT2" );
      manufacturer2.setName( "Test Manufacturer 2" );
      Manufacturer.Address manAddress2 = new Manufacturer.Address();
      manAddress2.setCountryCode( "USA" );
      manAddress2.setStateCode( "NY" );
      manAddress2.setCity( "New York" );
      manAddress2.setStreetAddress( "123 America Rd" );
      manAddress2.setZipCode( "12345" );
      manAddress2.setPhoneNumber( "555-456-7890" );
      manAddress2.setFaxNumber( "555-765-4321" );
      manAddress2.setEmail( "build@airportusa.com" );
      manufacturer2.setAddress( manAddress2 );

      manufacturer3.setId( "30000000000000000000000000000000" );
      manufacturer3.setCode( "MANUFACT3" );
      manufacturer3.setName( "Test Manufacturer 3" );
      Manufacturer.Address manAddress3 = new Manufacturer.Address();
      manAddress3.setCountryCode( "AUS" );
      manAddress3.setStateCode( "VIC" );
      manAddress3.setCity( "Blacktown" );
      manAddress3.setStreetAddress( "123 Australia Rd" );
      manAddress3.setZipCode( "ABCD" );
      manAddress3.setPhoneNumber( "111-456-7890" );
      manAddress3.setFaxNumber( "111-765-4321" );
      manAddress3.setEmail( "build@airportaus.com" );
      manufacturer3.setAddress( manAddress3 );

      manufacturer4.setId( "40000000000000000000000000000000" );
      manufacturer4.setCode( "MANUFACT4" );

      manufacturer5.setId( "40000000000000000000000000000000" );
      manufacturer5.setCode( "MANUFACT5" );
      manufacturer5.setName( "Duplicate Manufacturer" );

      manufacturer6.setId( "40000000000000000000000000000000" );
      manufacturer5.setCode( "MANUFACT6" );
      manufacturer6.setName( manufacturer5.getName() );
   }
}
