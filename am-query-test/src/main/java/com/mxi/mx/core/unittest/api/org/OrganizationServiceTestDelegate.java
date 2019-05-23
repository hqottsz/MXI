
package com.mxi.mx.core.unittest.api.org;

import java.util.ArrayList;
import java.util.List;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.exception.StringTooLongException;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.dao.org.OrganizationDaoImpl;
import com.mxi.mx.core.dao.org.OrganizationDaoTestStub;
import com.mxi.mx.core.facility.MxDaoFactory;
import com.mxi.mx.core.facility.MxFacilityAliases.DaoAlias;
import com.mxi.mx.core.facility.MxFacilityLocator;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.model.organization.Address;
import com.mxi.mx.core.model.organization.Organization;
import com.mxi.mx.core.services.org.AddressTO;
import com.mxi.mx.core.services.org.AssignedUsersException;
import com.mxi.mx.core.services.org.ContactTO;
import com.mxi.mx.core.services.org.DependentSubOrganizationsException;
import com.mxi.mx.core.services.org.DuplicateOrganizationCodeException;
import com.mxi.mx.core.services.org.IllegalOrgDeleteException;
import com.mxi.mx.core.services.org.OperatorTO;
import com.mxi.mx.core.services.org.OrganizationService;
import com.mxi.mx.core.services.org.OrganizationServiceImpl;
import com.mxi.mx.core.services.org.OrganizationTO;
import com.mxi.mx.core.services.org.RestrictedUsersException;


/**
 * Tests all the organization service methods
 *
 * @author asmolko
 */
public class OrganizationServiceTestDelegate {

   private MxDaoFactory iDaoFactory = MxFacilityLocator.getInstance().getDaoFactory();

   private OrganizationService iOrgService;


   /**
    * Creates a new OrganizationServiceTestDelegate object.
    */
   public OrganizationServiceTestDelegate() {
      iDaoFactory.setFacility( DaoAlias.ORGANIZATION, new OrganizationDaoTestStub() );

      iOrgService = new OrganizationServiceImpl();
   }


   /**
    * Creates an organization with specified properties
    *
    * @param aParentOrgKey
    *           parent org
    * @param aCode
    *           code of the new org
    * @param aName
    *           name of the new org
    * @param aType
    *           type of the new org
    *
    * @return the key of the new organization
    *
    * @throws Exception
    *            if something goes wrong
    */
   public Organization createOrg( OrgKey aParentOrgKey, String aCode, String aName,
         RefOrgTypeKey aType ) throws Exception {
      OrganizationTO lOrgTo = new OrganizationTO();
      lOrgTo.setCode( aCode );
      lOrgTo.setName( aName );
      lOrgTo.setType( aType );

      Organization lNewOrg = iOrgService.create( aParentOrgKey, lOrgTo );

      return lNewOrg;
   }


   /**
    * Creates an organization with specified properties
    *
    * @param aParentOrgKey
    *           parent org
    * @param aCode
    *           code of the new org
    * @param aName
    *           name of the new org
    * @param aType
    *           type of the new org
    * @param aAddressTO
    *           address of the new org
    * @param aContactTO
    *           contact info of the new org
    * @param aOperatorTO
    *           DOCUMENT_ME
    *
    * @return the key of the new organization
    *
    * @throws Exception
    *            if something goes wrong
    */
   public Organization createOrg( OrgKey aParentOrgKey, String aCode, String aName,
         RefOrgTypeKey aType, AddressTO aAddressTO, ContactTO aContactTO, OperatorTO aOperatorTO )
         throws Exception {
      OrganizationTO lOrgTo = new OrganizationTO();
      lOrgTo.setCode( aCode );
      lOrgTo.setName( aName );
      lOrgTo.setType( aType );
      lOrgTo.setAddressTO( aAddressTO );
      lOrgTo.setContactTO( aContactTO );
      lOrgTo.setOperator( aOperatorTO );

      Organization lNewOrg = iOrgService.create( aParentOrgKey, lOrgTo );

      // Re-fetch the organization data because the OrganizationService doesn't check that
      // address/contact info is correctly persisted.
      return new OrganizationDaoImpl().get( lNewOrg.getOrgKey() );
   }


   /**
    * Deletes an organization
    *
    * @param aOrgKey
    *           the organization key
    *
    * @throws MandatoryArgumentException
    * @throws AssignedUsersException
    *            if the organization has users assigned to it
    * @throws DependentSubOrganizationsException
    *            if the organization has sub-organizations
    * @throws IllegalOrgDeleteException
    *            if the organization has the admin or default type
    * @throws RestrictedUsersException
    *            if the organization has restricted users
    * @throws MxException
    *            if any other error condition occurs
    */
   public void delete( OrgKey aOrgKey ) throws MandatoryArgumentException, AssignedUsersException,
         DependentSubOrganizationsException, IllegalOrgDeleteException, RestrictedUsersException,
         MxException {
      iOrgService.delete( aOrgKey );
   }


   public List<HumanResourceKey> getUnassinedUsersList( OrgKey aOrgKey ) {

      // Create the arguments
      DataSetArgument lArguments = new DataSetArgument();
      lArguments.add( aOrgKey, new String[] { "aCompanyOrgDbId", "aCompanyOrgId" } );

      // Retrieve the list of unassigned users
      QuerySet lDataSet = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.web.query.org.UnassignedOrgUsers", lArguments );
      List<HumanResourceKey> lList = new ArrayList<HumanResourceKey>();

      while ( lDataSet.next() ) {

         HumanResourceKey lHumanResourceKey = null;
         String lHRDbId = lDataSet.getString( "hr_key" );
         if ( lHRDbId != null ) {
            lHumanResourceKey = new HumanResourceKey( lHRDbId );
            lList.add( lHumanResourceKey );
         }
      }

      return lList;
   }


   public Address setAddress( OrgKey aOrgKey, AddressTO aAddressTO )
         throws DuplicateOrganizationCodeException, StringTooLongException,
         MandatoryArgumentException {
      Organization lOrg = new OrganizationDaoImpl().get( aOrgKey );
      OrganizationTO lOrgTo = new OrganizationTO();
      lOrgTo.setCode( lOrg.getCode() );
      lOrgTo.setName( lOrg.getName() );
      lOrgTo.setType( lOrg.getTypeKey() );

      lOrgTo.setAddressTO( aAddressTO );
      iOrgService.set( aOrgKey, lOrgTo );

      return new OrganizationDaoImpl().get( aOrgKey ).getAddress();
   }


   /**
    * Creates a new OrganizationServiceTestDelegate object.
    *
    * @param aOrgKey
    *           the OrgKey of the organization
    * @param aCode
    *           the code of the organization
    *
    * @throws DuplicateOrganizationCodeException
    *            if the code alreday exists
    * @throws MandatoryArgumentException
    * @throws StringTooLongException
    *            if the organization's name is too long
    */
   public void setCode( OrgKey aOrgKey, String aCode ) throws DuplicateOrganizationCodeException,
         MandatoryArgumentException, StringTooLongException {
      Organization lOrg = new OrganizationDaoImpl().get( aOrgKey );
      OrganizationTO lOrgTo = new OrganizationTO();
      lOrgTo.setCode( aCode );
      lOrgTo.setName( lOrg.getName() );
      lOrgTo.setType( lOrg.getTypeKey() );

      iOrgService.set( aOrgKey, lOrgTo );
   }


   /**
    * Creates a new OrganizationServiceTestDelegate object.
    *
    * @param aOrgKey
    *           the OrgKey of the organization
    * @param aCode
    *           the code of the organization
    * @param aName
    *           the name of the organization
    *
    * @throws DuplicateOrganizationCodeException
    *            if the code alreday exists
    * @throws MandatoryArgumentException
    * @throws StringTooLongException
    *            if the organization's name is too long
    */
   public void setCodeName( OrgKey aOrgKey, String aCode, String aName )
         throws DuplicateOrganizationCodeException, MandatoryArgumentException,
         StringTooLongException {
      Organization lOrg = new OrganizationDaoImpl().get( aOrgKey );
      OrganizationTO lOrgTo = new OrganizationTO();
      lOrgTo.setCode( aCode );
      lOrgTo.setName( aName );
      lOrgTo.setType( lOrg.getTypeKey() );

      iOrgService.set( aOrgKey, lOrgTo );
   }


   public void setContact( OrgKey aOrgKey, ContactTO aContactTO )
         throws DuplicateOrganizationCodeException, StringTooLongException,
         MandatoryArgumentException {
      Organization lOrg = new OrganizationDaoImpl().get( aOrgKey );
      OrganizationTO lOrgTo = new OrganizationTO();
      lOrgTo.setCode( lOrg.getCode() );
      lOrgTo.setName( lOrg.getName() );
      lOrgTo.setType( lOrg.getTypeKey() );

      lOrgTo.setContactTO( aContactTO );
      iOrgService.set( aOrgKey, lOrgTo );
   }


   public void setName( OrgKey aOrgKey, String aName ) throws DuplicateOrganizationCodeException,
         MandatoryArgumentException, StringTooLongException {
      Organization lOrg = new OrganizationDaoImpl().get( aOrgKey );
      OrganizationTO lOrgTo = new OrganizationTO();
      lOrgTo.setCode( lOrg.getCode() );
      lOrgTo.setName( aName );
      lOrgTo.setType( lOrg.getTypeKey() );

      iOrgService.set( aOrgKey, lOrgTo );
   }
}
