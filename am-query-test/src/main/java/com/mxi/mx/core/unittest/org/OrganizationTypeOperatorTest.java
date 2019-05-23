
package com.mxi.mx.core.unittest.org;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.CarrierBuilder;
import com.mxi.am.domain.builder.OrganizationDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParametersStub;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.services.org.OperatorTO;
import com.mxi.mx.core.services.org.OrganizationServiceImpl;
import com.mxi.mx.core.services.org.OrganizationTO;
import com.mxi.mx.core.table.org.OrgCarrierTable;
import com.mxi.mx.core.table.org.OrgOrgTable;


/**
 * This class test the update of the details of an organization type Operator, mostly related to
 * carrier code
 *
 * @author gpichetto
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class OrganizationTypeOperatorTest {

   private static final String CARRIER_CODE = "OT-OTA";
   private static final String IATA_CODE = "OT";
   private static final String ICAO_CODE = "OTA";
   private static final String ORG_CODE = "ORG_1";
   private static final String ORG_DESCRIPTION = "Organization for Test";
   private static final RefOrgTypeKey TYPE = RefOrgTypeKey.OPERATOR;
   private static final String ORG_CODE_2 = "ORG_2";

   private GlobalParametersStub iLogicParameters;
   private OrganizationServiceImpl iOrgService = new OrganizationServiceImpl();
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * This test verifies that when an organization type Operator updates the code, the operator
    * carrier code [org_carrier.carrier_cd] is also updated with the same value.
    *
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testUpdateOrganizationChangesInCarrierCode() throws Exception {

      // Build new Organization
      OrgKey lOrgKey = new OrganizationDomainBuilder().withCode( ORG_CODE )
            .withDescription( ORG_DESCRIPTION ).build();

      // Build new Carrier (Operator)
      CarrierKey lCarrierKey = new CarrierBuilder().withCode( CARRIER_CODE )
            .withIATACode( IATA_CODE ).withICAOCode( ICAO_CODE ).withOrgKey( lOrgKey ).build();

      // Assert carrier code prior to update the organization
      OrgCarrierTable lTable = OrgCarrierTable.findByPrimaryKey( lCarrierKey );
      assertEquals( lTable.getCode(), CARRIER_CODE );

      // create the operator transfer object
      // change the IATA code
      OperatorTO lOperatorTO = new OperatorTO();
      String lIATACode = "YY";
      String lCarrierCode = lIATACode + "-" + ICAO_CODE;
      lOperatorTO.setIATACode( lIATACode );
      lOperatorTO.setICAOCode( ICAO_CODE );
      lOperatorTO.setCode( lCarrierCode );
      lOperatorTO.setOperatorKey( lCarrierKey );

      // crate organization transfer object
      OrganizationTO lOrganizationTO = new OrganizationTO();
      lOrganizationTO.setCode( ORG_CODE );
      lOrganizationTO.setType( TYPE );
      lOrganizationTO.setName( ORG_DESCRIPTION );
      lOrganizationTO.setOperator( lOperatorTO );

      // update organization details
      iOrgService.set( lOrgKey, lOrganizationTO );

      // assert that organization carrier code changes
      lTable = OrgCarrierTable.findByPrimaryKey( lCarrierKey );
      assertEquals( lTable.getCode(), lCarrierCode );
   }


   /**
    * This test verifies when an organization type Operator changes the IATA code then operator
    * carrier code [org_carrier.carrier_cd] does not change
    *
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testUpdateOrganizationNoChangesInCarrierCode() throws Exception {

      // Build new Organization
      OrgKey lOrgKey = new OrganizationDomainBuilder().withCode( ORG_CODE )
            .withDescription( ORG_DESCRIPTION ).build();

      // Build new Carrier (Operator)
      CarrierKey lCarrierKey = new CarrierBuilder().withCode( CARRIER_CODE )
            .withIATACode( IATA_CODE ).withICAOCode( ICAO_CODE ).withOrgKey( lOrgKey ).build();

      // Assert carrier code prior to update the organization
      OrgCarrierTable lTable = OrgCarrierTable.findByPrimaryKey( lCarrierKey );
      assertEquals( lTable.getCode(), CARRIER_CODE );

      // create the operator transfer object
      // no changes to the existing one built in previous step
      OperatorTO lOperatorTO = new OperatorTO();
      lOperatorTO.setIATACode( IATA_CODE );
      lOperatorTO.setICAOCode( ICAO_CODE );
      lOperatorTO.setCode( CARRIER_CODE );
      lOperatorTO.setOperatorKey( lCarrierKey );

      // crate organization transfer object
      OrganizationTO lOrganizationTO = new OrganizationTO();
      lOrganizationTO.setOperator( lOperatorTO );

      // only change organization code
      lOrganizationTO.setCode( ORG_CODE_2 );

      // update organization details
      iOrgService.set( lOrgKey, lOrganizationTO );

      // assert that carrier code remais the same after updaing the organization code
      lTable = OrgCarrierTable.findByPrimaryKey( lCarrierKey );
      assertEquals( lTable.getCode(), CARRIER_CODE );

      // assert that organization code is updated
      OrgOrgTable lOrgOrgTable = OrgOrgTable.findByPrimaryKey( lOrgKey );
      assertEquals( lOrgOrgTable.getOrgCode(), ORG_CODE_2 );
   }

}
