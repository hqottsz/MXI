
package com.mxi.mx.asrt.org;

import org.junit.Assert;

import com.mxi.mx.core.dao.address.AddressDao;
import com.mxi.mx.core.dao.address.AddressDaoImpl;
import com.mxi.mx.core.dao.contact.ContactDao;
import com.mxi.mx.core.dao.contact.ContactDaoImpl;
import com.mxi.mx.core.dao.org.OperatorDao;
import com.mxi.mx.core.dao.org.OperatorDaoImpl;
import com.mxi.mx.core.dao.org.OrganizationDao;
import com.mxi.mx.core.facility.MxFacilityAliases.DaoAlias;
import com.mxi.mx.core.facility.MxFacilityLocator;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.model.organization.Address;
import com.mxi.mx.core.model.organization.Contact;
import com.mxi.mx.core.model.organization.Operator;
import com.mxi.mx.core.model.organization.Organization;


/**
 * DOCUMENT_ME
 *
 * @author asmolko
 */
public class OrgBLAssert {

   AddressDao iAddressDao;
   ContactDao iContactDao;
   OperatorDao iOperatorDao;

   OrganizationDao iOrgDao;


   /**
    * Creates a new OrgBLAssert object.
    */
   public OrgBLAssert() {
      iOrgDao = ( OrganizationDao ) MxFacilityLocator.getInstance().getDaoFactory()
            .getFacility( DaoAlias.ORGANIZATION );
      iAddressDao = new AddressDaoImpl();
      iContactDao = new ContactDaoImpl();
      iOperatorDao = new OperatorDaoImpl();
   }


   /**
    * DOCUMENT_ME
    *
    * @param aOrgKey
    *           DOCUMENT_ME
    * @param aAddress
    *           DOCUMENT_ME
    */
   public void assertAddress( OrgKey aOrgKey, Address aAddress ) {
      Organization lOrg = iOrgDao.get( aOrgKey );
      Address lOrgAddress = iAddressDao.get( lOrg.getAddress().getAddressKey() );

      aAddress.setAddressKey( lOrg.getAddress().getAddressKey() );
      Assert.assertEquals( aAddress.toString(), lOrgAddress.toString() );
   }


   /**
    * DOCUMENT_ME
    *
    * @param aOrgKey
    *           DOCUMENT_ME
    * @param aCode
    *           DOCUMENT_ME
    * @param aName
    *           DOCUMENT_ME
    */
   public void assertCodeName( OrgKey aOrgKey, String aCode, String aName ) {
      Organization lOrg = iOrgDao.get( aOrgKey );
      Assert.assertEquals( aCode, lOrg.getCode() );
      Assert.assertEquals( aName, lOrg.getName() );
   }


   /**
    * DOCUMENT_ME
    *
    * @param aOrgKey
    *           DOCUMENT_ME
    * @param aFullCode
    *           DOCUMENT_ME
    * @param aCode
    *           DOCUMENT_ME
    */
   public void assertCodes( OrgKey aOrgKey, String aFullCode, String aCode ) {
      Organization lOrg = iOrgDao.get( aOrgKey );
      Assert.assertEquals( aFullCode, lOrg.getPath() );
      Assert.assertEquals( aCode, lOrg.getCode() );
   }


   /**
    * DOCUMENT_ME
    *
    * @param aOrgKey
    *           DOCUMENT_ME
    * @param aCode
    *           DOCUMENT_ME
    * @param aName
    *           DOCUMENT_ME
    * @param aType
    *           DOCUMENT_ME
    */
   public void assertCodesWithType( OrgKey aOrgKey, String aCode, String aName,
         RefOrgTypeKey aType ) {
      Organization lOrg = iOrgDao.get( aOrgKey );
      Assert.assertEquals( aCode, lOrg.getCode() );
      Assert.assertEquals( aName, lOrg.getName() );
      Assert.assertEquals( aType, lOrg.getTypeKey() );
   }


   /**
    * DOCUMENT_ME
    *
    * @param aOrgKey
    *           DOCUMENT_ME
    * @param aContact
    *           DOCUMENT_ME
    */
   public void assertContact( OrgKey aOrgKey, Contact aContact ) {
      Organization lOrg = iOrgDao.get( aOrgKey );
      Contact lOrgContact = iContactDao.get( lOrg.getContact().getContactKey() );

      aContact.setContactKey( lOrg.getContact().getContactKey() );
      Assert.assertEquals( aContact.toString(), lOrgContact.toString() );
   }


   /**
    * DOCUMENT_ME
    *
    * @param aOrgKey
    *           DOCUMENT_ME
    */
   public void assertDefaultOrganizationCreatedUnderCompany( OrgKey aOrgKey ) {
      Organization lOrg = iOrgDao.get( aOrgKey );
      Assert.assertTrue( lOrg.isCompany() );

      Organization defaultOrg = iOrgDao.getCompanyDefaultOrg( lOrg );
      Assert.assertNotNull( defaultOrg );
   }


   /**
    * DOCUMENT_ME
    *
    * @param aOrgKey
    *           DOCUMENT_ME
    * @param aFullCode
    *           DOCUMENT_ME
    */
   public void assertFullCode( OrgKey aOrgKey, String aFullCode ) {
      Organization lOrg = iOrgDao.get( aOrgKey );
      Assert.assertEquals( aFullCode, lOrg.getPath() );
   }


   /**
    * Assert the provided Operator object has the same values as the operator retrieved from the DB.
    *
    * @param aOrgKey
    *           The organization key used to retrieve the operator.
    * @param aOperator
    *           The operator to validate against.
    */
   public void assertOperator( OrgKey aOrgKey, Operator aOperator ) {
      Operator lOperator = iOperatorDao.get( aOrgKey );
      Assert.assertEquals( lOperator.getCallSign(), aOperator.getCallSign() );
      Assert.assertEquals( lOperator.getIATACode(), aOperator.getIATACode() );
      Assert.assertEquals( lOperator.getICAOCode(), aOperator.getICAOCode() );
      Assert.assertEquals( lOperator.getAuthorityKey(), aOperator.getAuthorityKey() );
      Assert.assertEquals( lOperator.getOrgKey(), aOperator.getOrgKey() );
      Assert.assertEquals( lOperator.getCode(), aOperator.getCode() );
   }
}
