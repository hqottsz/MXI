/*
 * Confidential, proprietary and/or trade secret information of Mxi Technologies, Ltd.
 *
 * Copyright 2000-2018 Mxi Technologies, Ltd. All Rights Reserved.
 *
 * Except as expressly provided by written license signed by a duly appointed officer of Mxi
 * Technologies, Ltd., any disclosure, distribution, reproduction, compilation, modification,
 * creation of derivative works and/or other use of the Mxi source code is strictly prohibited.
 * Inclusion of a copyright notice shall not be taken to indicate that the source code has been
 * published.
 */

/**
 * Query test for OutBoundShipments.qrx
 *
 */

package com.mxi.mx.web.query.shipment;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgHrSupplyKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefShipSegmentStatusKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.org.OrgHrSupplyTable;


@RunWith( BlockJUnit4ClassRunner.class )
public final class OutboundShipmentsTest {

   private static final String PART_NO = "PART_001";
   private static final String SERIAL_NO_OEM = "ABC";
   private LocationKey iDockLocation = null;
   private HumanResourceKey iHr;
   private int iUserId;

   @Rule
   public final DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setup() throws SQLException {

      // create a human resource
      iHr = Domain.createHumanResource();

      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iHr ) );

      iUserId = OrgHr.findByPrimaryKey( iHr ).getUserId();

      iDockLocation = Domain.createLocation( aLocationConfiguration -> {
         aLocationConfiguration.setType( RefLocTypeKey.DOCK );
         aLocationConfiguration.setIsSupplyLocation( true );
         aLocationConfiguration.setCode( "ATL/DOCK" );
      } );

      OrgHrSupplyKey lHrSupplyLink = new OrgHrSupplyKey( iDockLocation.getDbId(),
            iDockLocation.getId(), iUserId, iHr.getDbId(), iHr.getId() );
      OrgHrSupplyTable lOrgHrSupply = new OrgHrSupplyTable();
      lOrgHrSupply.insert( lHrSupplyLink );

   }


   @After
   public void tearDown() {
      SecurityIdentificationUtils.setInstance( null );
      UserParameters.setInstance( iUserId, "SECURED_RESOURCE", null );
   }


   /**
    *
    * GIVEN an out bound shipment is created from a unservicable location, WHEN queried for the
    * shipment, THEN should return ready for shipment as 0
    *
    */
   @Test
   public void testReadyForShippingWithInventoryNotAtDockLocation() {

      final LocationKey lNonDockLocation = Domain.createLocation( aLocationConfiguration -> {
         aLocationConfiguration.setType( RefLocTypeKey.USSTG );
         aLocationConfiguration.setCode( "JFK" );
      } );

      createInventoryAndShipment( lNonDockLocation );
      QuerySet lQs = getOutboundShipmentDetails();
      lQs.next();
      assertEquals( 0, lQs.getInt( "ready_for_shipping" ) );

   }


   /**
    *
    * GIVEN an out bound shipment is created from a DOCK type location, WHEN queried for the
    * shipment, THEN should return ready for shipment as 1
    *
    *
    */
   @Test
   public void testReadyForShippingWithInventoryAtDockLocation() {

      createInventoryAndShipment( iDockLocation );
      QuerySet lQs = getOutboundShipmentDetails();
      lQs.next();
      assertEquals( 1, lQs.getInt( "ready_for_shipping" ) );

   }


   private QuerySet getOutboundShipmentDetails() {
      // bind query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iHr, new String[] { "aHrDbId", "aHrId" } );

      // execute query and return results
      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQuery( QueryExecutor.getQueryName( getClass() ), lArgs );
      return lQs;
   }


   private void createInventoryAndShipment( LocationKey aLocationKey ) {
      // create partNo
      final PartNoKey lPartNo = new PartNoBuilder().withInventoryClass( RefInvClassKey.SER )
            .withUnitType( RefQtyUnitKey.EA ).withOemPartNo( PART_NO )
            .withStatus( RefPartStatusKey.ACTV ).manufacturedBy( Domain.createManufacturer() )
            .isTool().withDefaultPartGroup().build();

      // create Vendor location
      final LocationKey lVendorLocation = Domain.createLocation( aLocationConfiguration -> {
         aLocationConfiguration.setType( RefLocTypeKey.VENDOR );
         aLocationConfiguration.setCode( "AMS" );
      } );

      // create Inventory
      InventoryKey lInventory = Domain.createTrackedInventory( aInventoryConfiguration -> {
         aInventoryConfiguration.setPartNumber( lPartNo );
         aInventoryConfiguration.setSerialNumber( SERIAL_NO_OEM );
         aInventoryConfiguration.setCondition( RefInvCondKey.RFI );
         aInventoryConfiguration.setLocation( aLocationKey );
         aInventoryConfiguration.setOwner( Domain.createOwner() );
      } );

      Domain.createShipment( aShipmentConfiguration -> {
         aShipmentConfiguration.setType( RefShipmentTypeKey.REPAIR );
         aShipmentConfiguration.setStatus( RefEventStatusKey.IXPEND );
         aShipmentConfiguration.addShipmentSegment( aSegment -> {
            aSegment.setFromLocation( iDockLocation );
            aSegment.setToLocation( lVendorLocation );
            aSegment.setStatus( RefShipSegmentStatusKey.PENDING );
         } );
         aShipmentConfiguration.addShipmentLine( aLine -> {
            aLine.inventory( lInventory );
            aLine.part( lPartNo );
         } );
      } );

   }
}
