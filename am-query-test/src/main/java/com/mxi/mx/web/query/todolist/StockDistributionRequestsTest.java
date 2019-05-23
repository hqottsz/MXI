
package com.mxi.mx.web.query.todolist;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.Table;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgHrSupplyKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefStockDistReqStatusKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.table.org.OrgHrSupplyTable;
import com.mxi.mx.core.table.org.OrgHrTable;


/**
 * Tests the query com.mxi.mx.web.query.todolist.StockDistributionRequests
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class StockDistributionRequestsTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private HumanResourceKey iCurrentHR;
   private HumanResourceKey iDifferentHR;
   private LocationKey iAirport;
   private LocationKey iMainWarehouse;
   private LocationKey iLineStore;
   private StockNoKey iStockNo;
   private OwnerKey iOwner;
   private PartNoKey iPartNoKey;
   private static final RefQtyUnitKey QTY_UNIT = RefQtyUnitKey.EA;
   private static final String STOCK_CODE = "SKCD";
   private static final Float iNeededQty = new Float( 5.0 );


   @Before
   public void loadData() {
      // create airport
      iAirport = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.AIRPORT );
         location.setCode( "theairport" );
         location.setIsSupplyLocation( true );
      } );

      // create main warehouse
      iMainWarehouse = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setCode( "mainwarehouse" );
         location.setIsSupplyLocation( true );
         location.setParent( iAirport );
      } );

      // create line store and make main warehouse as it's supplier
      iLineStore = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setCode( "linestore" );
         location.setHubLocation( iMainWarehouse );
         location.setParent( iAirport );
         location.setSupplyLocation( iAirport );
      } );

      // create a user and link the user to the iUserSupplyLocation
      int lCurrentUserId = 991;
      iCurrentHR = buildUser( "current_hr", lCurrentUserId );
      linkHumanResourceToSupplyLocation( iAirport, lCurrentUserId, iCurrentHR );

      // create a second user that is not associated to the iUserSupplyLocation
      iDifferentHR = buildUser( "different_hr", lCurrentUserId + 1 );

      // create stock
      iStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH ).withQtyUnitKey( QTY_UNIT )
            .withStockCode( STOCK_CODE ).build();

      iPartNoKey = new PartNoBuilder().withInventoryClass( RefInvClassKey.BATCH )
            .withOemPartNo( "PartNo1" ).withStatus( RefPartStatusKey.ACTV ).withStock( iStockNo )
            .build();

      // create default owner
      iOwner = new OwnerDomainBuilder().isDefault().build();
   }


   /**
    * Build users for the system.
    *
    * @param aUsername
    *           Desired username
    * @param aUserid
    *           Desired userid.
    * @return A HR Key.
    */
   private HumanResourceKey buildUser( String aUsername, int aUserid ) {
      UserKey lUserKey = new UserKey( aUserid );

      DataSetArgument lUserArgs = new DataSetArgument();
      lUserArgs.add( lUserKey, "user_id" );
      lUserArgs.add( "username", aUsername );
      lUserArgs.add( "utl_id", Table.Util.getDatabaseId() );

      MxDataAccess.getInstance().executeInsert( "utl_user", lUserArgs );

      OrgHrTable lTable = OrgHrTable.create();
      lTable.setUserId( lUserKey );

      return lTable.insert();
   }


   /**
    * Add a record in the org_hr_supply table linking the HR to the supply location.
    *
    * @param aSupplyLocation
    * @param aUserId
    * @param aHumanResource
    * @return key to the org_hr_supply link
    */
   private OrgHrSupplyKey linkHumanResourceToSupplyLocation( LocationKey aSupplyLocation,
         int aUserId, HumanResourceKey aHumanResource ) {
      OrgHrSupplyKey lHrSupplyLink = new OrgHrSupplyKey( aSupplyLocation.getDbId(),
            aSupplyLocation.getId(), aUserId, aHumanResource.getDbId(), aHumanResource.getId() );
      OrgHrSupplyTable lOrgHrSupply = new OrgHrSupplyTable();
      return lOrgHrSupply.insert( lHrSupplyLink );
   }


   /**
    * Creates a stock distribution request for testing purposes.
    *
    * @param aStockDistReqStatusKey
    *           Key of the status you want to create the request in.
    */
   private void createStockDistributionRequest( RefStockDistReqStatusKey aStockDistReqStatusKey ) {
      Domain.createStockDistReq( pojo -> {
         pojo.setNeededLocation( iLineStore );
         pojo.setSupplierLocation( iMainWarehouse );
         pojo.setStockNo( iStockNo );
         pojo.setOwner( iOwner );
         pojo.setNeededQuantity( iNeededQty );
         pojo.setStatus( aStockDistReqStatusKey );
      } );
   }


   /**
    * Execute the query.
    *
    * @param aHrKey
    *           Hr Key
    *
    * @return the result
    */
   public DataSet execute( HumanResourceKey aHrKey ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aHrKey, "aHrDbId", "aHrId" );
      lArgs.addWhereEquals( "WHERE_CLAUSE_WHERE_NEEDED", "1", "1" );
      lArgs.addWhereEquals( "WHERE_CLAUSE_HAZMAT", "1", "1" );
      String[] lTheStatuses = { "INPROGRESS", "OPEN", "PICKED" };
      lArgs.addWhereIn( "WHERE_CLAUSE_STATUS", "stock_dist_req.status_cd", lTheStatuses );

      // Execute the query
      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Ensures that a list of Stock Distribution requests are returned if their state is one of OPEN,
    * PICKED, or INPROGRESS. Also ensures that valid values are returned for one of the rows
    * returned.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testValidHRCanViewStockRequests() throws Exception {

      // Create stock distribution requests with multiple different statuses
      createStockDistributionRequest( RefStockDistReqStatusKey.INPROGRESS );
      createStockDistributionRequest( RefStockDistReqStatusKey.OPEN );
      createStockDistributionRequest( RefStockDistReqStatusKey.PICKED );
      createStockDistributionRequest( RefStockDistReqStatusKey.CANCELED );
      createStockDistributionRequest( RefStockDistReqStatusKey.COMPLETED );

      // first, test that we receive 3 results back for the OPEN, INPROGRESS, and PICKED states
      DataSet lResult = execute( iCurrentHR );
      assertEquals( "Expected three rows of results", 3, lResult.getRowCount() );

      // second, verify the returned values for one of the rows contains valid values.
      lResult.first();
      assertEquals( null, lResult.getString( "assigned_to_key" ) );
      assertEquals( null, lResult.getString( "assigned_to_name" ) );
      assertEquals( iStockNo, lResult.getKey( StockNoKey.class, "stock_no_key" ) );
      assertEquals( STOCK_CODE, lResult.getString( "stock_no_cd" ) );
      assertEquals( iNeededQty.doubleValue(), lResult.getDouble( "needed_qty" ), 0 );
      assertEquals( iLineStore, lResult.getKey( LocationKey.class, "needed_loc_key" ) );
      assertEquals( "linestore", lResult.getString( "needed_location_cd_name" ) );
      assertEquals( iMainWarehouse, lResult.getKey( LocationKey.class, "supplier_loc_key" ) );
      assertEquals( "mainwarehouse", lResult.getString( "supplier_location_cd_name" ) );
      assertEquals( iOwner, lResult.getKey( OwnerKey.class, "owner_key" ) );
   }


   /**
    * Ensures that a list of Stock Distribution requests are not returned if the user requesting
    * them is not associated to the Supply location (i.e. the user does not belong to a department
    * that is associated to the location)
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testNonValidHRCannotViewStockRequests() throws Exception {

      // Create a stock distribution request
      createStockDistributionRequest( RefStockDistReqStatusKey.INPROGRESS );

      // test where the retrieving user is not associated to the supply location
      DataSet lResult = execute( iDifferentHR );
      assertEquals( "Expected no rows of results", 0, lResult.getRowCount() );
   }
}
