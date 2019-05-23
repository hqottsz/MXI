
package com.mxi.mx.core.unittest.org;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.utils.ObjectUtils;
import com.mxi.mx.common.utils.StringUtils;
import com.mxi.mx.core.dao.org.OrganizationDaoImpl;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.RefCountryKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.key.StateKey;
import com.mxi.mx.core.model.organization.Address;
import com.mxi.mx.core.model.organization.Organization;
import com.mxi.mx.core.services.org.AddressTO;
import com.mxi.mx.core.services.org.ContactTO;
import com.mxi.mx.core.unittest.api.org.OrganizationServiceTestDelegate;


/**
 * This class contains scenarios related to Org Model. It by-passess EJB layer, goes straight to
 * service classes for business logic as well as it uses JSP controllers to reteive information that
 * we display on UI for verification purposes. Can be run outside of appserver, as long as unitest
 * query db is accessible.
 *
 * @author asmolko
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class OrganizationAddressTest {

   private RefCountryKey iCanada = new RefCountryKey( 1234, "CA" );

   private ContactTO iEmptyContact = new ContactTO();
   private StateKey iOntario = new StateKey( 1234, "CA", "ON" );

   private OrganizationServiceTestDelegate iOrgService;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Sets up test data.
    *
    * @throws Exception
    */
   @Before
   public void setUp() throws Exception {
      // Create data access helpers.
      iOrgService = new OrganizationServiceTestDelegate();

      // Create country data.
      DataSetArgument lCountryArgs = new DataSetArgument();
      lCountryArgs.add( iCanada, "country_db_id", "country_cd" );
      MxDataAccess.getInstance().executeInsert( "REF_COUNTRY", lCountryArgs );

      // Create state data.
      DataSetArgument lStateArgs = new DataSetArgument();
      lStateArgs.add( iOntario, "country_db_id", "country_cd", "state_cd" );
      MxDataAccess.getInstance().executeInsert( "REF_STATE", lStateArgs );
   }


   /**
    * Tests creation of an organization with country+state data.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testOrgCreateWithCountryAndState() throws Exception {

      // Test that an organization created with a country + state retains its information.
      AddressTO lAddress = new AddressTO();
      lAddress.setAddress1( "75 Nicholas Street" );
      lAddress.setCity( "Ottawa" );
      lAddress.setCountryKey( iCanada );
      lAddress.setStateKey( null );

      Organization lOrg = iOrgService.createOrg( OrgKey.ADMIN, "CS2", "CountryState2",
            RefOrgTypeKey.OPERATOR, lAddress, iEmptyContact, null );

      Assert.assertTrue( lOrg.getAddress().getCountryKey().equals( iCanada ) );
      Assert.assertNull( lOrg.getAddress().getStateKey() );
   }


   /**
    * Ensure that an organization created with no address returns a null address.
    *
    * @throws Exception
    */
   @Test
   public void testOrgCreateWithNullAddress() throws Exception {

      Organization lOrg =
            iOrgService.createOrg( OrgKey.ADMIN, "CS1", "CountryState1", RefOrgTypeKey.OPERATOR );
      Assert.assertNull( lOrg.getAddress() );
   }


   /**
    * Ensures that an organization created with address details but not country/state persists its
    * details.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testOrgCreateWithNullCountry() throws Exception {

      // Test that an organization created with no country/state retains its address/city
      // information.
      AddressTO lAddress = new AddressTO();
      lAddress.setAddress1( "75 Nicholas Street" );
      lAddress.setCity( "Ottawa" );

      Organization lOrg = iOrgService.createOrg( OrgKey.ADMIN, "CS2", "CountryState2",
            RefOrgTypeKey.OPERATOR, lAddress, iEmptyContact, null );

      Assert.assertTrue( lOrg.getAddress().getAddress1().equals( "75 Nicholas Street" ) );
      Assert.assertTrue( lOrg.getAddress().getCityName().equals( "Ottawa" ) );
      Assert.assertTrue( lOrg.getAddress().getCountryKey() == null );
      Assert.assertTrue( lOrg.getAddress().getStateKey() == null );
   }


   /**
    * // Test that an organization created with a country retains its information and has a null
    * state.
    *
    * @throws Exception
    */
   @Test
   public void testOrgCreateWithNullState() throws Exception {
      AddressTO lAddress = new AddressTO();
      lAddress.setAddress1( "75 Nicholas Street" );
      lAddress.setCity( "Ottawa" );
      lAddress.setCountryKey( iCanada );
      lAddress.setStateKey( null );

      Organization lOrg = iOrgService.createOrg( OrgKey.ADMIN, "CS2", "CountryState2",
            RefOrgTypeKey.OPERATOR, lAddress, iEmptyContact, null );

      Assert.assertTrue( lOrg.getAddress().getCountryKey().equals( iCanada ) );
      Assert.assertNull( lOrg.getAddress().getStateKey() );
   }


   /**
    * Tests update of country/state. Especially important is the test for the bug fixed in MX-6585
    * (country is nulled if state == null.)
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testOrgUpdateCountryAndState() throws Exception {

      Organization lOrg =
            iOrgService.createOrg( OrgKey.ADMIN, "CS2", "CountryState2", RefOrgTypeKey.OPERATOR );

      AddressTO lAddress = new AddressTO();
      lAddress.setAddress1( "75 Nicholas" );
      lAddress.setCity( "Ottawa" );

      iOrgService.setAddress( lOrg.getOrgKey(), lAddress );
      addressEquals( lOrg, lAddress );

      // Test update of country field.
      lAddress.setCountryKey( iCanada );
      iOrgService.setAddress( lOrg.getOrgKey(), lAddress );
      addressEquals( lOrg, lAddress );

      // Test update of state field.
      lAddress.setStateKey( iOntario );
      iOrgService.setAddress( lOrg.getOrgKey(), lAddress );
      addressEquals( lOrg, lAddress );

      // Test that country remains persisted even after state is set to null.
      lAddress.setStateKey( null );
      iOrgService.setAddress( lOrg.getOrgKey(), lAddress );
      addressEquals( lOrg, lAddress );

      // Test that state is nulled after country is nulled.
      lAddress.setStateKey( iOntario );
      iOrgService.setAddress( lOrg.getOrgKey(), lAddress );
      lAddress.setCountryKey( null );
      iOrgService.setAddress( lOrg.getOrgKey(), lAddress );
      lAddress.setStateKey( null );
      addressEquals( lOrg, lAddress );
   }


   /**
    * Compares an addressTO with an organization's persisted address data.
    *
    * @param aOrg
    *           organization with an address to compare with <code>lAddressTO</code>.
    * @param aAddressTO
    *           an address with information to compare with persisted data.
    *
    * @return whether the organization's address matches the transfer object's.
    */
   private boolean addressEquals( Organization aOrg, AddressTO aAddressTO ) {
      Address lAddress2 = new OrganizationDaoImpl().get( aOrg.getOrgKey() ).getAddress();

      if ( ( lAddress2 == null ) && ( aAddressTO == null ) ) {
         return true;
      }

      if ( ( lAddress2 == null ) || ( aAddressTO == null ) ) {
         return false;
      }

      if ( !StringUtils.areEqual( lAddress2.getAddress1(), aAddressTO.getAddress1() )
            || !StringUtils.areEqual( lAddress2.getAddress2(), aAddressTO.getAddress2() )
            || !StringUtils.areEqual( lAddress2.getCityName(), aAddressTO.getCity() )
            || !StringUtils.areEqual( lAddress2.getZipCode(), aAddressTO.getZipCode() ) ) {
         return false;
      }

      RefCountryKey lCountry1 = lAddress2.getCountryKey();
      RefCountryKey lCountry2 = aAddressTO.getCountryKey();
      if ( !ObjectUtils.areEqual( lCountry1, lCountry2 ) ) {
         return false;
      }

      StateKey lState1 = lAddress2.getStateKey();
      StateKey lState2 = aAddressTO.getStateKey();
      if ( !ObjectUtils.areEqual( lState1, lState2 ) ) {
         return false;
      }

      return true;
   }

}
