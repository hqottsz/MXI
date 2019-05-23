
package com.mxi.mx.core.dao.org;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.dao.address.AddressDao;
import com.mxi.mx.core.dao.address.AddressDaoImpl;
import com.mxi.mx.core.dao.contact.ContactDao;
import com.mxi.mx.core.dao.contact.ContactDaoImpl;
import com.mxi.mx.core.key.AddressKey;
import com.mxi.mx.core.key.ContactKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.RefCountryKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.key.StateKey;
import com.mxi.mx.core.model.organization.Address;
import com.mxi.mx.core.model.organization.Contact;
import com.mxi.mx.core.model.organization.Organization;
import com.mxi.mx.core.model.organization.OrganizationType;
import com.mxi.mx.core.table.org.OrgAddressTable;
import com.mxi.mx.core.table.org.OrgContactTable;


/**
 * DOCUMENT_ME
 *
 * @author asmolko
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class OrganizationDaoTest {

   AddressDao iAddressDao;
   ContactDao iContactDao;

   Organization iOrg1_1;
   OrganizationDao iOrgDao;

   Address iTestAddress;
   Contact iTestContact;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   @Before
   public void setUp() throws Exception {
      iOrgDao = new OrganizationDaoImpl();
      iAddressDao = new AddressDaoImpl();
      iContactDao = new ContactDaoImpl();

      iOrg1_1 = OrgTestDataFactory.genOperator( new OrgKey( 1, 1 ), new OrgKey( 0, 1 ) );

      iTestAddress = new Address( new AddressKey( OrgAddressTable.getDatabaseId(), 1 ), "Address1",
            "Address2", "CityName", new RefCountryKey( "1:1" ), new StateKey( "1:1:1" ),
            "ZipCode" );

      iTestContact = new Contact( new ContactKey( OrgContactTable.getDatabaseId(), 1 ),
            "ContactName", "Email", "FaxNumber", "JobTitle", "PhoneNumber" );
   }


   /**
    * Checks to see if created object is the same as expected
    */
   @Test
   public void testCreate() {
      iOrgDao.create( iOrg1_1 );

      Organization lActual = iOrgDao.get( new OrgKey( 1, 1 ) );
      Assert.assertEquals( iOrg1_1, lActual );
   }


   /**
    * DOCUMENT_ME
    */
   @Test
   public void testCreateWithAddress() {

      // create the organization (DAO Layer)
      iOrgDao.create( iOrg1_1 );

      // create the Address (DAO Layer)
      iAddressDao.create( iTestAddress );

      // get the organization and assert
      Organization lActual = iOrgDao.get( new OrgKey( 1, 1 ) );
      Assert.assertEquals( iOrg1_1, lActual );

      // get the address and assert
      Address lActualAddress =
            iAddressDao.get( new AddressKey( OrgAddressTable.getDatabaseId(), 1 ) );
      Assert.assertEquals( iTestAddress.toString(), lActualAddress.toString() );

      // add the address to the organization (DAO Layer)
      iOrgDao.addAddress( lActual.getOrgKey(), lActualAddress.getAddressKey() );

      // get the Org with added address info
      lActual = iOrgDao.get( lActual.getOrgKey() );

      // get the address key from Org
      AddressKey lActualAddressKey = lActual.getAddress().getAddressKey();

      // get the address and verify
      Address lAddress = iAddressDao.get( lActualAddressKey );
      Assert.assertEquals( iTestAddress.toString(), lAddress.toString() );
   }


   /**
    * DOCUMENT_ME
    */
   @Test
   public void testCreateWithContact() {

      // create the organization (DAO Layer)
      iOrgDao.create( iOrg1_1 );

      // create the Contact (DAO Layer)
      iContactDao.create( iTestContact );

      // get the organization and assert
      Organization lActual = iOrgDao.get( new OrgKey( 1, 1 ) );
      Assert.assertEquals( iOrg1_1, lActual );

      // get the Contact and assert
      Contact lActualContact =
            iContactDao.get( new ContactKey( OrgContactTable.getDatabaseId(), 1 ) );
      Assert.assertEquals( iTestContact.toString(), lActualContact.toString() );

      // add the Contact to the organization (DAO Layer)
      iOrgDao.addContact( lActual.getOrgKey(), lActualContact.getContactKey() );

      // get the Org with added Contact info
      lActual = iOrgDao.get( lActual.getOrgKey() );

      // get the Contact key from Org
      ContactKey lActualContactKey = lActual.getContact().getContactKey();

      // get the Contact and verify
      Contact lContact = iContactDao.get( lActualContactKey );
      Assert.assertEquals( iTestContact.toString(), lContact.toString() );
   }


   /**
    * Check to make sure that the details of an organization type could be retrieved
    */
   @Test
   public void testGetOrgType() {
      OrganizationType lOrgType = iOrgDao.getType( RefOrgTypeKey.ADMIN );
      assertTrue( lOrgType.isCompany() );
      assertEquals( RefOrgTypeKey.ADMIN, lOrgType.getType() );

      lOrgType = iOrgDao.getType( RefOrgTypeKey.DEPT );
      assertFalse( lOrgType.isCompany() );
      assertEquals( RefOrgTypeKey.DEPT, lOrgType.getType() );
   }

}
