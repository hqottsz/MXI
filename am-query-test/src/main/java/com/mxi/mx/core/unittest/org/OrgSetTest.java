
package com.mxi.mx.core.unittest.org;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.asrt.org.OrgBLAssert;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.RefCountryKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.key.StateKey;
import com.mxi.mx.core.model.organization.Address;
import com.mxi.mx.core.model.organization.Contact;
import com.mxi.mx.core.model.organization.Organization;
import com.mxi.mx.core.services.org.AddressTO;
import com.mxi.mx.core.services.org.ContactTO;
import com.mxi.mx.core.services.org.DuplicateOrganizationCodeException;
import com.mxi.mx.core.unittest.MxTestUtils;
import com.mxi.mx.core.unittest.api.org.OrganizationServiceTestDelegate;


/**
 * This class contains tests for OrganizationService.set() method
 *
 * @author asmolko
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class OrgSetTest {

   private OrgBLAssert iOrgBLAssert;

   private OrganizationServiceTestDelegate iOrgService;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testChangeAddress() throws Exception {
      OrgKey lOrgKey = OrgKey.ADMIN;

      // Set the code and name to the maximum string length
      String lCode = MxTestUtils.generateString( 8 );
      String lName = MxTestUtils.generateString( 40 );

      String lAddress1 = "Address1";
      String lAddress2 = "Address2";
      String lCity = "City";
      RefCountryKey lCountryKey = new RefCountryKey( "1:1" );
      StateKey lStateKey = new StateKey( "1:1:1" );
      String lZipCode = "ZipCode";

      AddressTO lAddressTO = new AddressTO();
      Address lAddress = new Address();

      lAddressTO.setAddress1( lAddress1 );
      lAddressTO.setAddress2( lAddress2 );
      lAddressTO.setCity( lCity );
      lAddressTO.setCountryKey( lCountryKey );
      lAddressTO.setStateKey( lStateKey );
      lAddressTO.setZipCode( lZipCode );

      lAddress.setAddress1( lAddress1 );
      lAddress.setAddress2( lAddress2 );
      lAddress.setCityName( lCity );
      lAddress.setCountryKey( lCountryKey );
      lAddress.setStateKey( lStateKey );
      lAddress.setZipCode( lZipCode );

      // Create the new organization
      OrgKey lNewOrg = iOrgService
            .createOrg( lOrgKey, lCode, lName, RefOrgTypeKey.DEPT, lAddressTO, null, null )
            .getOrgKey();

      iOrgBLAssert.assertAddress( lNewOrg, lAddress );

      lAddress1 = "nAddress1";
      lAddress2 = "nAddress2";
      lCity = "nCity";
      lCountryKey = new RefCountryKey( "2:2" );
      lStateKey = new StateKey( "2:2:2" );
      lZipCode = "nZipCode";

      lAddressTO.setAddress1( lAddress1 );
      lAddressTO.setAddress2( lAddress2 );
      lAddressTO.setCity( lCity );
      lAddressTO.setCountryKey( lCountryKey );
      lAddressTO.setStateKey( lStateKey );
      lAddressTO.setZipCode( lZipCode );

      lAddress.setAddress1( lAddress1 );
      lAddress.setAddress2( lAddress2 );
      lAddress.setCityName( lCity );
      lAddress.setCountryKey( lCountryKey );
      lAddress.setStateKey( lStateKey );
      lAddress.setZipCode( lZipCode );

      iOrgService.setAddress( lNewOrg, lAddressTO );

      iOrgBLAssert.assertAddress( lNewOrg, lAddress );
   }


   /**
    * Make sure that a code name can be changed and that all children get changed
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testChangeCompanyCodeName() throws Exception {

      // Set up test org tree
      Organization lOrg =
            iOrgService.createOrg( OrgKey.ADMIN, "ORG1", "Organization", RefOrgTypeKey.OPERATOR );
      Organization lDept1 =
            iOrgService.createOrg( lOrg.getOrgKey(), "DEPT1", "Organization", RefOrgTypeKey.DEPT );
      Organization lDept2 =
            iOrgService.createOrg( lOrg.getOrgKey(), "DEPT2", "Organization", RefOrgTypeKey.DEPT );
      Organization lDept3 = iOrgService.createOrg( lDept2.getOrgKey(), "DEPT3", "Organization",
            RefOrgTypeKey.DEPT );

      // Make sure the codes in the org tree look the way we want them
      iOrgBLAssert.assertCodeName( lOrg.getOrgKey(), "ORG1", "Organization" );
      iOrgBLAssert.assertFullCode( lDept1.getOrgKey(), "ORG1/" );
      iOrgBLAssert.assertFullCode( lDept2.getOrgKey(), "ORG1/" );
      iOrgBLAssert.assertFullCode( lDept3.getOrgKey(), "ORG1/DEPT2/" );

      // Change the code and name
      iOrgService.setCodeName( lOrg.getOrgKey(), "CHANGE1", "Change 1" );

      // Make sure that the code name was propagated down the tree
      iOrgBLAssert.assertCodeName( lOrg.getOrgKey(), "CHANGE1", "Change 1" );
      iOrgBLAssert.assertFullCode( lDept1.getOrgKey(), "CHANGE1/" );
      iOrgBLAssert.assertFullCode( lDept2.getOrgKey(), "CHANGE1/" );
      iOrgBLAssert.assertFullCode( lDept3.getOrgKey(), "CHANGE1/DEPT2/" );
   }


   /**
    * Make sure that only a name can be changed
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testChangeCompanyNameSameCode() throws Exception {

      // Set up test org tree
      OrgKey lDept1Key = iOrgService
            .createOrg( OrgKey.ADMIN, "DEPT1", "Organization", RefOrgTypeKey.DEPT ).getOrgKey();

      // Make sure the codes in the org tree look the way we want them
      iOrgBLAssert.assertCodeName( lDept1Key, "DEPT1", "Organization" );
      iOrgBLAssert.assertFullCode( lDept1Key, "MXI/" );

      // Change the code and name
      iOrgService.setName( lDept1Key, "Change 1" );

      // Make sure that the code name was propagated down the tree
      iOrgBLAssert.assertCodeName( lDept1Key, "DEPT1", "Change 1" );
      iOrgBLAssert.assertFullCode( lDept1Key, "MXI/" );
   }


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testChangeContact() throws Exception {
      OrgKey lOrgKey = OrgKey.ADMIN;

      // Set the code and name to the maximum string length
      String lCode = MxTestUtils.generateString( 8 );
      String lName = MxTestUtils.generateString( 40 );

      String lContactName = "Contact Name";
      String lEmail = "Email@Email.com";
      String lPhone = "123456";
      String lJob = "Job";
      String lFax = "Fax";

      ContactTO lContactTO = new ContactTO();
      Contact lContact = new Contact();

      lContactTO.setContactName( lContactName );
      lContactTO.setEmailAddress( lEmail );
      lContactTO.setFaxNumber( lFax );
      lContactTO.setPhoneNumber( lPhone );
      lContactTO.setJobTitle( lJob );

      lContact.setContactName( lContactName );
      lContact.setEmail( lEmail );
      lContact.setFaxNumber( lFax );
      lContact.setPhoneNumber( lPhone );
      lContact.setJobTitle( lJob );

      // Create the new organization
      OrgKey lNewOrg = iOrgService
            .createOrg( lOrgKey, lCode, lName, RefOrgTypeKey.DEPT, null, lContactTO, null )
            .getOrgKey();

      iOrgBLAssert.assertContact( lNewOrg, lContact );

      lContactName = "nContact Name";
      lEmail = "nEmail@Email.com";
      lPhone = "1234567";
      lJob = "nJob";
      lFax = "nFax";

      lContactTO.setContactName( lContactName );
      lContactTO.setEmailAddress( lEmail );
      lContactTO.setFaxNumber( lFax );
      lContactTO.setPhoneNumber( lPhone );
      lContactTO.setJobTitle( lJob );

      lContact.setContactName( lContactName );
      lContact.setEmail( lEmail );
      lContact.setFaxNumber( lFax );
      lContact.setPhoneNumber( lPhone );
      lContact.setJobTitle( lJob );

      iOrgService.setContact( lNewOrg, lContactTO );

      iOrgBLAssert.assertContact( lNewOrg, lContact );
   }


   /**
    * Make sure that a ADMIN/ORG1/DEPT1/DEPT1 changing to ADMIN/ORG1/DEPT2/DEPT1. Just making sure
    * that DEPT1 does not get changed to DEPT2 in all instances on the same path
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testChangeRepeatingCodeInFullPath() throws Exception {

      // set the test org tree
      OrgKey lOrgKey = iOrgService
            .createOrg( OrgKey.ADMIN, "ORG1", "Organization", RefOrgTypeKey.OPERATOR ).getOrgKey();
      OrgKey lDept1Key = iOrgService
            .createOrg( lOrgKey, "DEPT1", "Organization", RefOrgTypeKey.DEPT ).getOrgKey();
      OrgKey lDept2Key = iOrgService
            .createOrg( lDept1Key, "DEPT1", "Organization", RefOrgTypeKey.DEPT ).getOrgKey();

      // Make sure the tree is correct
      iOrgBLAssert.assertCodeName( lOrgKey, "ORG1", "Organization" );
      iOrgBLAssert.assertFullCode( lDept1Key, "ORG1/" );
      iOrgBLAssert.assertFullCode( lDept2Key, "ORG1/DEPT1/" );

      // Chenahe the code of a an org in the middle of the tree
      iOrgService.setCode( lDept1Key, "DEPT2" );

      // Make sure the change poperly done
      iOrgBLAssert.assertCodes( lDept1Key, "ORG1/", "DEPT2" );
      iOrgBLAssert.assertCodes( lDept2Key, "ORG1/DEPT2/", "DEPT1" );
   }


   /**
    * Check to make sure that companies with the same code are not allowed. For example,
    * ADMIN/ORG1/Org3 and ADMIN/ORG2/Org3 are not allowed, where each Org is a company.
    *
    * @throws Exception
    */
   @Test
   public void testCompaniesWithSameCode() throws Exception {

      // Setup test org tree
      OrgKey lOrg1Key = iOrgService
            .createOrg( OrgKey.ADMIN, "ORG1", "Org1", RefOrgTypeKey.OPERATOR ).getOrgKey();

      iOrgService.createOrg( OrgKey.ADMIN, "ORG2", "Org2", RefOrgTypeKey.OPERATOR );

      iOrgService.createOrg( lOrg1Key, "Org3", "Organization", RefOrgTypeKey.DEPT );

      OrgKey lOrg4Key = iOrgService
            .createOrg( lOrg1Key, "Org4", "Organization", RefOrgTypeKey.CREW ).getOrgKey();

      // Attempt to change name of the company to be the same as another one
      try {
         iOrgService.setCode( lOrg4Key, "Org3" );
         fail( "Expected exception" );
      } catch ( DuplicateOrganizationCodeException lExp ) {
         // Expected
      }
   }


   /**
    * Make sure that non-company organizations with the same code are not allowed if they belong to
    * the same company and on the same level in the tree (siblings)
    *
    * @throws Exception
    *            if something goes wrong
    */
   @Test
   public void testNonCompanyOrgWithSameCodeCompanyAndLevel() throws Exception {

      // Setup test org tree
      OrgKey lOrg1Key = iOrgService
            .createOrg( OrgKey.ADMIN, "ORG1", "Org1", RefOrgTypeKey.OPERATOR ).getOrgKey();

      iOrgService.createOrg( lOrg1Key, "DEPT1", "Dept1", RefOrgTypeKey.DEPT );

      OrgKey lDept2Key =
            iOrgService.createOrg( lOrg1Key, "DEPT2", "Dept1", RefOrgTypeKey.DEPT ).getOrgKey();

      // Attempt to change the code
      try {
         iOrgService.setCode( lDept2Key, "DEPT1" );
         fail( "Expected exception" );
      } catch ( DuplicateOrganizationCodeException lExp ) {
         // Expected
      }
   }


   /**
    * Check to make sure that orgs in the same company but at different leveles in the org tree are
    * allowed. For example, ADMIN/ORG1/DEPT1 and ADMIN/ORG1/DEPT1/DEPT2/DEPT1 are allowed.
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testOrgsWithSameCodeInTheSameCompanyDifLevels() throws Exception {

      // Setup test org tree
      OrgKey lOrg1Key = iOrgService
            .createOrg( OrgKey.ADMIN, "ORG1", "Org1", RefOrgTypeKey.OPERATOR ).getOrgKey();

      iOrgService.createOrg( lOrg1Key, "DEPT1", "Dept1", RefOrgTypeKey.DEPT );

      OrgKey lDept2Key =
            iOrgService.createOrg( lOrg1Key, "DEPT2", "Dept2", RefOrgTypeKey.DEPT ).getOrgKey();
      OrgKey lDept3Key =
            iOrgService.createOrg( lDept2Key, "DEPT3", "Dept1", RefOrgTypeKey.DEPT ).getOrgKey();

      // Change the code to the same value as one of the parent orgs in the company
      try {
         iOrgService.setCode( lDept3Key, "DEPT1" );
      } catch ( DuplicateOrganizationCodeException lEx ) {
         fail( "Exception was thrown but was not supposed to" );
      }
   }


   /**
    * Check to make sure that ADMIN/ORG1/DEPT1 and ADMIN/ORG2/DEPT1 where ORG1 and ORG2 two
    * different companies is allowed.
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testOrgsWithTheSameCodeInDifferentCompany() throws Exception {

      // Setup test org tree
      OrgKey lOrg1Key = iOrgService
            .createOrg( OrgKey.ADMIN, "ORG1", "Org1", RefOrgTypeKey.OPERATOR ).getOrgKey();
      iOrgService.createOrg( lOrg1Key, "DEPT1", "Dept1", RefOrgTypeKey.DEPT );

      OrgKey lOrg2Key = iOrgService
            .createOrg( OrgKey.ADMIN, "ORG2", "Org2", RefOrgTypeKey.OPERATOR ).getOrgKey();
      OrgKey lDept2Key =
            iOrgService.createOrg( lOrg2Key, "DEPT2", "Dept1", RefOrgTypeKey.DEPT ).getOrgKey();

      // Change the code of an org to be the same as code of an org in a different company
      try {
         iOrgService.setCode( lDept2Key, "DEPT1" );
      } catch ( DuplicateOrganizationCodeException lEx ) {
         fail( "Exception was thrown but was not supposed to" );
      }
   }


   @Before
   public void setUp() throws Exception {
      iOrgService = new OrganizationServiceTestDelegate();
      iOrgBLAssert = new OrgBLAssert();
   }

}
