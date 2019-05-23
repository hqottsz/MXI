package com.mxi.am.api.resource.erp.location;

import static org.junit.Assert.assertEquals;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJBContext;
import javax.naming.NamingException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Inject;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiBadRequestException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.erp.location.impl.LocationResourceBean;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.key.TimeZoneKey;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * new query tests for location api
 *
 */

@RunWith( MockitoJUnitRunner.class )
public class LocationResourceBeanTest extends ResourceBeanTest {

   private static final String FAKE_ID = "0000000000000000000000000000000A";

   private static Location location1 = new Location();
   private static Location location2 = new Location();
   private static Location location3 = new Location();
   private static Location location4 = new Location();
   private static Location location5 = new Location();

   @Inject
   LocationResourceBean iLocationResourceBean;

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   @Mock
   private Principal iPrincipal;

   @Mock
   private EJBContext iEJBContext;


   @BeforeClass
   public static void init() {
      buildTestLocations();
   }


   @Before
   public void setUp() throws NamingException, MxException {

      InjectorContainer.get().injectMembers( this );

      iLocationResourceBean.setEJBContext( iEJBContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( AUTHORIZED );

      initializeTest();

      Mockito.when( iEJBContext.getCallerPrincipal() ).thenReturn( iPrincipal );
      Mockito.when( iPrincipal.getName() ).thenReturn( AUTHORIZED );

   }


   @Test
   @CSIContractTest( { Project.SWA_AC_STATUS, Project.SWA_MOPP, Project.SWA_WP_STATUS,
         Project.UPS } )
   public void get_success() throws Exception {
      Location lLocation = iLocationResourceBean.get( location1.getId() );
      assertLocationObject( location1, lLocation );

   }


   @Test
   public void get_failure_locationNotFound() throws Exception {
      try {
         iLocationResourceBean.get( FAKE_ID );
      } catch ( AmApiResourceNotFoundException aE ) {
         Assert.assertEquals( FAKE_ID, aE.getId() );
      }
   }


   @Test
   @CSIContractTest( { Project.SWA_FQC, Project.SWA_WP_STATUS, Project.UPS } )
   public void search_success_byLocationCode() {
      LocationSearchParameters lLocationSearchParams = new LocationSearchParameters();
      lLocationSearchParams.setLocationCodes( Arrays.asList( location1.getCode() ) );
      List<Location> lLocations = iLocationResourceBean.search( lLocationSearchParams );
      Assert.assertEquals( "Retrieved location list should only have one location.", 1,
            lLocations.size() );
      assertLocationObject( location1, lLocations.get( 0 ) );
   }


   @Test
   public void search_success_byTypeCodes() {
      LocationSearchParameters lLocationSearchParams = new LocationSearchParameters();
      lLocationSearchParams.setLocationTypeCodes( Arrays.asList( location1.getType() ) );
      List<Location> lLocations = iLocationResourceBean.search( lLocationSearchParams );
      Assert.assertEquals( 1, lLocations.size() );
      assertLocationObject( location1, lLocations.get( 0 ) );
   }


   @Test
   public void search_success_byName() {
      LocationSearchParameters lLocationSearchParams = new LocationSearchParameters();
      lLocationSearchParams.setName( location1.getName() );
      List<Location> lLocations = iLocationResourceBean.search( lLocationSearchParams );
      Assert.assertEquals( 1, lLocations.size() );
      assertLocationObject( location1, lLocations.get( 0 ) );
   }


   @Test
   @CSIContractTest( Project.EXT_MATERIAL_PLANNER )
   public void search_success_byIds() {
      LocationSearchParameters locationSearchParams = new LocationSearchParameters();
      locationSearchParams.setIds( Arrays.asList( location1.getId() ) );

      List<Location> locations = iLocationResourceBean.search( locationSearchParams );

      assertEquals( "Incorrect number of locations returned: ", 1, locations.size() );
      assertLocationObject( location1, locations.get( 0 ) );
   }


   @Test
   @CSIContractTest( Project.SWA_MOPP )
   public void search_success_byAllParams() {
      LocationSearchParameters lLocationSearchParams = new LocationSearchParameters(
            Arrays.asList( location1.getCode() ), true, Arrays.asList( location1.getType() ), null,
            null, location1.getName(), false, new ArrayList<String>() );
      List<Location> lLocations = iLocationResourceBean.search( lLocationSearchParams );

      assertEquals( "Incorrect number of locations returned: ", 1, lLocations.size() );
      assertLocationObject( location1, lLocations.get( 0 ) );
      assertEquals( "Incorrect number of sublocations returned: ", 1,
            lLocations.get( 0 ).getSubLocations().size() );
      assertLocationObject( location4, lLocations.get( 0 ).getSubLocations().get( 0 ) );
   }


   @Test
   public void update_success() throws Exception {
      Location newLocation = location5;
      newLocation.setName( "NewName" );

      Location updatedLocation = iLocationResourceBean.update( location5.getId(), newLocation );
      assertLocationObject( newLocation, updatedLocation );
   }


   @Test
   public void update_success_timeZoneCodeWritableWhenSupply() throws Exception {
      Location newLocation = iLocationResourceBean.get( location5.getId() );
      newLocation.setTimeZoneCode( TimeZoneKey.NEW_YORK.getCd() );
      newLocation.setSupply( true );

      Location updatedLocation = iLocationResourceBean.update( location5.getId(), newLocation );
      assertEquals( newLocation.getTimeZoneCode(), updatedLocation.getTimeZoneCode() );
   }


   @Test
   public void update_success_timeZoneCodeReadOnlyWhenNotSupply() throws Exception {
      Location newLocation = iLocationResourceBean.get( location5.getId() );
      newLocation.setTimeZoneCode( TimeZoneKey.NEW_YORK.getCd() );
      newLocation.setSupply( false );

      Location updatedLocation = iLocationResourceBean.update( location5.getId(), newLocation );
      assertEquals( TimeZoneKey.LOS_ANGELES.getCd(), updatedLocation.getTimeZoneCode() );
   }


   @Test
   public void update_failure_locationNotFound() throws Exception {
      Location lNewLocation = location5;
      lNewLocation.setName( "NewerName" );
      try {
         iLocationResourceBean.update( FAKE_ID, lNewLocation );
      } catch ( AmApiResourceNotFoundException aE ) {
         Assert.assertEquals( FAKE_ID, aE.getId() );
      }
   }


   @Test
   public void update_failure_nullLocation() throws Exception {
      Location lNewLocation = null;
      try {
         iLocationResourceBean.update( location1.getId(), lNewLocation );
      } catch ( AmApiBadRequestException aE ) {
         Assert.assertEquals( "Missing request body.", aE.getMessage() );
      }
   }


   @Test
   public void create_success_timeZoneWritableWhenSupply() {
      Location lLocation = new Location();
      lLocation.setName( "random airport" );
      lLocation.setCode( "RANDOM" );
      lLocation.setSupply( true );
      lLocation.setCountryCode( "USA" );
      lLocation.setStateCode( "CA" );
      lLocation.setTimeZoneCode( "America/Los_Angeles" );
      lLocation.setTimeZoneUserCode( "PST" );
      lLocation.setType( "AIRPORT" );

      Location lNewLocation = iLocationResourceBean.create( lLocation );

      Assert.assertEquals( lLocation.getName(), lNewLocation.getName() );
      Assert.assertEquals( lLocation.getCode(), lNewLocation.getCode() );
      Assert.assertEquals( lLocation.getCountryCode(), lNewLocation.getCountryCode() );
      Assert.assertEquals( lLocation.getStateCode(), lNewLocation.getStateCode() );
      Assert.assertEquals( lLocation.getTimeZoneCode(), lNewLocation.getTimeZoneCode() );
      Assert.assertEquals( lLocation.getType(), lNewLocation.getType() );
      Assert.assertEquals( lLocation.isSupply(), lNewLocation.isSupply() );

   }


   @Test
   public void create_success_timeZoneReadOnlyWhenNotSupply() {
      Location lLocation = new Location();
      lLocation.setName( "random airport" );
      lLocation.setCode( "RANDOM" );
      lLocation.setSupply( false );
      lLocation.setCountryCode( "USA" );
      lLocation.setStateCode( "CA" );
      lLocation.setTimeZoneCode( TimeZoneKey.LOS_ANGELES.getCd() );
      lLocation.setTimeZoneUserCode( "PST" );
      lLocation.setType( "AIRPORT" );

      Location lNewLocation = iLocationResourceBean.create( lLocation );

      Assert.assertEquals( lLocation.getName(), lNewLocation.getName() );
      Assert.assertEquals( lLocation.getCode(), lNewLocation.getCode() );
      Assert.assertEquals( lLocation.getCountryCode(), lNewLocation.getCountryCode() );
      Assert.assertEquals( lLocation.getStateCode(), lNewLocation.getStateCode() );
      Assert.assertEquals( TimeZoneKey.NEW_YORK.getCd(), lNewLocation.getTimeZoneCode() );
      Assert.assertEquals( lLocation.getType(), lNewLocation.getType() );
      Assert.assertEquals( lLocation.isSupply(), lNewLocation.isSupply() );

   }


   private void assertLocationObject( Location aExpectedLocation, Location aActualLocation ) {
      Assert.assertEquals( "Incorrect name: ", aExpectedLocation.getName(),
            aActualLocation.getName() );
      Assert.assertEquals( "Incorrect id: ", aExpectedLocation.getId(), aActualLocation.getId() );
      Assert.assertEquals( "Incorrect location type: ", aExpectedLocation.getType(),
            aActualLocation.getType() );
      Assert.assertEquals( "Incorrect country code: ", aExpectedLocation.getCountryCode(),
            aActualLocation.getCountryCode() );
      Assert.assertEquals( "Incorrect location code: ", aExpectedLocation.getCode(),
            aActualLocation.getCode() );
      Assert.assertEquals( "Incorrect timezone code: ", aExpectedLocation.getTimeZoneCode(),
            aActualLocation.getTimeZoneCode() );
      Assert.assertEquals( "Incorrect timezone user code: ",
            aExpectedLocation.getTimeZoneUserCode(), aActualLocation.getTimeZoneUserCode() );
      Assert.assertEquals( "Incorrect parent location id: ", aExpectedLocation.getParentId(),
            aActualLocation.getParentId() );
      Assert.assertEquals( "Incorrect state code: ", aExpectedLocation.getStateCode(),
            aActualLocation.getStateCode() );
      Assert.assertEquals( "Incorrect result for 'isSupply': ", aExpectedLocation.isSupply(),
            aActualLocation.isSupply() );

   }


   private static void buildTestLocations() {
      location1.setId( "00000000000000000000000000000001" );
      location1.setType( "AIRPORT" );
      location1.setCountryCode( "USA" );
      location1.setName( "OTTAWA" );
      location1.setCode( "YOW" );
      location1.setTimeZoneCode( "America/Los_Angeles" );
      location1.setTimeZoneUserCode( "PST" );
      location1.setParentId( "00000000000000000000000000000003" );
      location1.setStateCode( "CA" );
      location1.setSupply( false );

      location2.setId( "00000000000000000000000000000002" );
      location2.setTimeZoneCode( "America/Los_Angeles" );

      location3.setId( "00000000000000000000000000000003" );
      location3.setTimeZoneCode( "America/Los_Angeles" );

      location4.setId( "00000000000000000000000000000004" );
      location4.setTimeZoneCode( "America/Los_Angeles" );
      location4.setCode( "YOW/LINE" );
      location4.setName( "OTTAWA/LINE" );
      location4.setTimeZoneUserCode( "PST" );
      location4.setParentId( location1.getId() );
      location4.setSupply( false );

      location5.setId( "00000000000000000000000000000005" );
      location5.setTimeZoneCode( "America/Los_Angeles" );
      location5.setName( "OldName" );
      location5.setSupply( false );
      location5.setType( "LINE" );
      location5.setCountryCode( "USA" );
      location5.setCode( "YYZ" );
      location5.setTimeZoneUserCode( "PST" );
      location5.setStateCode( "CA" );

   }

}
