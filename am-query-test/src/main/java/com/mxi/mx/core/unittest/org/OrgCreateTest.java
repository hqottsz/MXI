
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
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.exception.StringTooLongException;
import com.mxi.mx.core.dao.MxDao;
import com.mxi.mx.core.dao.org.OrganizationDao;
import com.mxi.mx.core.facility.MxDaoFactory;
import com.mxi.mx.core.facility.MxFacilityAliases.DaoAlias;
import com.mxi.mx.core.facility.MxFacilityLocator;
import com.mxi.mx.core.key.InvalidKeyException;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.RefCountryKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.key.StateKey;
import com.mxi.mx.core.model.organization.Address;
import com.mxi.mx.core.model.organization.Contact;
import com.mxi.mx.core.model.organization.Operator;
import com.mxi.mx.core.services.org.AddressTO;
import com.mxi.mx.core.services.org.ContactTO;
import com.mxi.mx.core.services.org.DuplicateOrganizationCodeException;
import com.mxi.mx.core.services.org.OperatorTO;
import com.mxi.mx.core.unittest.MxTestUtils;
import com.mxi.mx.core.unittest.api.org.OrganizationServiceTestDelegate;
import com.mxi.mx.core.unittest.authority.AUTestData;


/**
 * DOCUMENT_ME
 *
 * @author mgharibjanian
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class OrgCreateTest {

   private OrgBLAssert iOrgBLAssert;
   private MxDao iOrgDao;
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
   public void testCreate() throws Exception {

      OrgKey lOrgKey = OrgKey.ADMIN;

      // Set the code and name to the maximum string length
      String lCode = MxTestUtils.generateString( 8 );
      String lName = MxTestUtils.generateString( 40 );

      // Create the new organization
      OrgKey lNewOrg =
            iOrgService.createOrg( lOrgKey, lCode, lName, RefOrgTypeKey.DEPT ).getOrgKey();

      iOrgBLAssert.assertCodesWithType( lNewOrg, lCode, lName, RefOrgTypeKey.DEPT );
   }


   /**
    * Tests if creation of new Organization with existing code is failed
    *
    * @throws StringTooLongException
    * @throws MandatoryArgumentException
    * @throws InvalidKeyException
    * @throws DuplicateOrganizationCodeException
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testCreateDuplicateOrganization()
         throws StringTooLongException, MandatoryArgumentException, InvalidKeyException,
         DuplicateOrganizationCodeException, Exception {
      String lCode = "CODE1";
      String lName = "test1";

      OrgKey lOrgKey = OrgKey.ADMIN;

      // Create the new Organization
      iOrgService.createOrg( lOrgKey, lCode, lName, RefOrgTypeKey.DEPT );

      try {
         iOrgService.createOrg( lOrgKey, lCode, lName, RefOrgTypeKey.DEPT );
         fail( "Duplicate path/code not reported." );
      } catch ( DuplicateOrganizationCodeException e ) {
         // Expected behaviour.
      }
   }


   /**
    * Tests to make sure an operator can be created properly
    *
    * @exception Exception
    *               if an error occurs.
    */
   @Test
   public void testCreateOperator() throws Exception {
      OrgKey lOrgKey = OrgKey.ADMIN;
      String lCode = MxTestUtils.generateString( 8 );
      String lName = MxTestUtils.generateString( 40 );

      String lIATACode = MxTestUtils.generateString( 3 );
      String lICAOCode = MxTestUtils.generateString( 4 );
      String lCallSign = MxTestUtils.generateString( 80 );

      // create the operator
      OperatorTO lOperatorTO = new OperatorTO();
      lOperatorTO.setIATACode( lIATACode );
      lOperatorTO.setICAOCode( lICAOCode );
      lOperatorTO.setCallSign( lCallSign );
      lOperatorTO.setAuthority( AUTestData.getAuthorityWithInventory() );
      lOperatorTO.setCode( lCode );

      // insert the operator
      // Create the new organization
      OrgKey lNewOrg = iOrgService
            .createOrg( lOrgKey, lCode, lName, RefOrgTypeKey.OPERATOR, null, null, lOperatorTO )
            .getOrgKey();

      // operator for verfication
      Operator lOperator = new Operator();
      lOperator.setIATACode( lIATACode );
      lOperator.setICAOCode( lICAOCode );
      lOperator.setCallSign( lCallSign );
      lOperator.setAuthorityKey( AUTestData.getAuthorityWithInventory() );
      lOperator.setOrgKey( lNewOrg );
      lOperator.setCode( lCode );

      iOrgBLAssert.assertOperator( lNewOrg, lOperator );
   }


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testCreateWithAddress() throws Exception {
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

      iOrgBLAssert.assertCodesWithType( lNewOrg, lCode, lName, RefOrgTypeKey.DEPT );
      iOrgBLAssert.assertAddress( lNewOrg, lAddress );
   }


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testCreateWithContact() throws Exception {
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

      iOrgBLAssert.assertCodesWithType( lNewOrg, lCode, lName, RefOrgTypeKey.DEPT );
      iOrgBLAssert.assertContact( lNewOrg, lContact );
   }


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testCreateWithLogo() throws Exception {
      OrgKey lOrgKey = OrgKey.ADMIN;

      // Set the code and name to the maximum string length
      String lCode = MxTestUtils.generateString( 8 );
      String lName = MxTestUtils.generateString( 40 );

      // Create the new organization
      OrgKey lNewOrg = iOrgService
            .createOrg( lOrgKey, lCode, lName, RefOrgTypeKey.DEPT, null, null, null ).getOrgKey();

      iOrgBLAssert.assertCodesWithType( lNewOrg, lCode, lName, RefOrgTypeKey.DEPT );
   }


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testCreationDefaultOrganizationForEachCompany() throws Exception {

      OrgKey lOrgKey = OrgKey.ADMIN;

      // Set the code and name to the maximum string length
      String lCode = MxTestUtils.generateString( 8 );
      String lName = MxTestUtils.generateString( 40 );

      // Create the new organization
      OrgKey lNewOrg =
            iOrgService.createOrg( lOrgKey, lCode, lName, RefOrgTypeKey.OPERATOR ).getOrgKey();

      iOrgBLAssert.assertDefaultOrganizationCreatedUnderCompany( lNewOrg );
   }


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testCreationOrganizationWithSameNameInDifferentLevels() throws Exception {
      OrgKey lFirstLevel = OrgKey.ADMIN;

      String lCode = "CODE";
      String lName = "test";

      // Create the sub Organization under ADMIN
      OrgKey lSecondLevel =
            iOrgService.createOrg( lFirstLevel, lCode, lName, RefOrgTypeKey.DEPT ).getOrgKey();

      iOrgBLAssert.assertCodesWithType( lSecondLevel, lCode, lName, RefOrgTypeKey.DEPT );

      // Trying to create sub organization with same code as parent
      OrgKey lThirdLevel =
            iOrgService.createOrg( lSecondLevel, lCode, lName, RefOrgTypeKey.DEPT ).getOrgKey();

      iOrgBLAssert.assertCodesWithType( lThirdLevel, lCode, lName, RefOrgTypeKey.DEPT );
   }


   /**
    * Tests the MandatoryArgumentException for organization keys.
    *
    * @exception Exception
    *               if an error occurs.
    */
   @Test
   public void testMandatoryOrgKey() throws Exception {

      // create an operator transfer object wihtout org key
      OperatorTO lOperatorTO = new OperatorTO();
      lOperatorTO.setIATACode( "AC" );
      lOperatorTO.setICAOCode( MxTestUtils.generateString( 3 ) );

      try {
         lOperatorTO.setOrgKey( null );
         fail( "Expected a MandatoryArgumentException." );
      } catch ( MandatoryArgumentException e ) {
         // continue since we are supposed to get here
      }
   }


   /**
    * Verifies that numeric values are allowed for operator IATA code
    *
    * @exception Exception
    *               if an error occurs.
    */
   @Test
   public void testNumericIATACodeForOperator() throws Exception {
      String lIATACode = "AC1";
      OrgKey lOrgKey = OrgKey.ADMIN;
      String lCode = MxTestUtils.generateString( 8 );
      String lName = MxTestUtils.generateString( 40 );
      String lICAOCode = MxTestUtils.generateString( 4 );
      String lCallSign = MxTestUtils.generateString( 80 );

      // create the operator
      OperatorTO lOperatorTO = new OperatorTO();
      lOperatorTO.setIATACode( lIATACode );
      lOperatorTO.setICAOCode( lICAOCode );
      lOperatorTO.setCallSign( lCallSign );
      lOperatorTO.setAuthority( AUTestData.getAuthorityWithInventory() );

      // insert the operator
      // Create the new organization
      OrgKey lNewOrg = iOrgService
            .createOrg( lOrgKey, lCode, lName, RefOrgTypeKey.OPERATOR, null, null, lOperatorTO )
            .getOrgKey();

      // operator for verfication
      Operator lOperator = new Operator();
      lOperator.setIATACode( lIATACode );
      lOperator.setICAOCode( lICAOCode );
      lOperator.setCallSign( lCallSign );
      lOperator.setAuthorityKey( AUTestData.getAuthorityWithInventory() );
      lOperator.setOrgKey( lNewOrg );

      iOrgBLAssert.assertOperator( lNewOrg, lOperator );
   }


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testPathCreation() throws Exception {
      OrgKey lFirstLevel = OrgKey.ADMIN;

      String lCodeADMIN = ( ( OrganizationDao ) iOrgDao ).get( lFirstLevel ).getCode();
      String lCodeSecond = "CODE2";
      String lNameSecond = "test2";

      String lCodeThird = "CODE3";
      String lNameThird = "test3";

      // Create the sub Organization under ADMIN
      OrgKey lSecondLevel = iOrgService
            .createOrg( lFirstLevel, lCodeSecond, lNameSecond, RefOrgTypeKey.DEPT ).getOrgKey();

      iOrgBLAssert.assertFullCode( lSecondLevel, lCodeADMIN + "/" );

      OrgKey lThirdLevel = iOrgService
            .createOrg( lSecondLevel, lCodeThird, lNameThird, RefOrgTypeKey.DEPT ).getOrgKey();

      iOrgBLAssert.assertFullCode( lThirdLevel, lCodeADMIN + "/" + lCodeSecond + "/" );
   }


   /**
    * Constructor for the test case.
    *
    * @exception Exception
    *               if an unexpected error occurs.
    */
   @Before
   public void setUp() throws Exception {
      // Create the instance of organization service and DAO
      MxDaoFactory lDaoFactory = MxFacilityLocator.getInstance().getDaoFactory();
      iOrgDao = lDaoFactory.getFacility( DaoAlias.ORGANIZATION );
      iOrgService = new OrganizationServiceTestDelegate();
      iOrgBLAssert = new OrgBLAssert();
   }

}
