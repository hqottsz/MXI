
package com.mxi.mx.web.query.todolist;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.EventInventoryBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.Table;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgHrSupplyKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefReqPriorityKey;
import com.mxi.mx.core.key.RefReqTypeKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.org.OrgHrSupplyTable;
import com.mxi.mx.core.table.org.OrgHrTable;
import com.mxi.mx.core.table.req.ReqPartTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the OpenPartRequests query
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class OpenPartRequestsTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final int DAY_COUNT = 60;
   private static final String TASK_BARCODE = "TASK_BARCODE";
   private static final String TASK_NAME = "TASK_NAME";
   private static final String ACFT_DESC = "ACFT_DESC";
   protected static final String WP_BARCODE = "WP_BARCODE";
   protected static final String WP_NAME = "WP_NAME";
   private static final String PART_GROUP = "PART_GROUP";
   private static final String PART = "PART";
   private static final String KIT_PART = "KIT_PART";
   private static final String AIR_CRAFT = "Boeing 757";
   private static final String INV_NO = "B001";
   private static final String AIR_PORT_LOC = "ATL";
   private static final String SRVSTORE = "ATL/SRVSTORE";
   private static final String OTHER_LOC_NAME = "YYZ";
   private static final String CURRENT_HR = "current_hr";
   private static final String DIFFERENT_HR = "different_hr";

   private HumanResourceKey iCurrentHR;
   private HumanResourceKey iDifferentHR;
   private OrgHrSupplyKey iOrgHrSupplyLink;
   private LocationKey iUserLocation;
   private LocationKey iUserSupplyLocation;
   private LocationKey iOtherLocation;
   private InventoryKey iBoeing757;
   private PartNoKey iAircraft;
   private PartNoKey iPart;
   private TaskKey iWorkPackage;
   private TaskKey iTask;
   private PartNoKey iKitPart;
   private PartGroupKey iPartGroupKit;

   private List<PartRequestKey> iPartRequests = new ArrayList<PartRequestKey>();


   /**
    * Tests the query for a user with all locations = false
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testUserCanSeeRequestsOnlyAtUserLocations() throws Exception {

      // data setup - create one part request at a location linked to the user and one that is not
      PartRequestKey lPartRequestAtUserLocation =
            buildOpenPartRequest( RefReqTypeKey.ADHOC, iUserLocation, false );
      PartRequestKey lPartRequestAtOtherLocation =
            buildOpenPartRequest( RefReqTypeKey.ADHOC, iOtherLocation, false );

      // store the part request keys so we can delete them afterwards
      iPartRequests = Arrays.asList( lPartRequestAtUserLocation, lPartRequestAtOtherLocation );
      // execute the query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aDayCount", DAY_COUNT );
      lArgs.add( "aHrDbId", iCurrentHR.getDbId() );
      lArgs.add( "aHrId", iCurrentHR.getId() );
      lArgs.add( "aShowAssignedToOthers", false );
      lArgs.add( "aAllLocations", false );

      DataSet lDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Only part requests with a location linked to the user through a department (link will be in
      // org_hr_supply) should be returned
      assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );
      lDs.next();
      testPartRequestDetailsRow( lDs, lPartRequestAtUserLocation );

   }


   /**
    * Tests the query for a user with all locations = true
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testUserCanSeeRequestsAtAllLocations() throws Exception {

      // data setup - create one part request at a location linked to the user and one that is not
      PartRequestKey lPartRequestAtUserLocation =
            buildOpenPartRequest( RefReqTypeKey.ADHOC, iUserLocation, false );
      PartRequestKey lPartRequestAtOtherLocation =
            buildOpenPartRequest( RefReqTypeKey.ADHOC, iOtherLocation, false );

      // store the part request keys so we can delete them afterwards
      iPartRequests = Arrays.asList( lPartRequestAtUserLocation, lPartRequestAtOtherLocation );

      // execute the query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aDayCount", DAY_COUNT );
      lArgs.add( "aHrDbId", iCurrentHR.getDbId() );
      lArgs.add( "aHrId", iCurrentHR.getId() );
      lArgs.add( "aShowAssignedToOthers", false );
      lArgs.add( "aAllLocations", true );

      DataSet lDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // assert that both part requests are returned
      assertEquals( "Number of retrieved rows", 2, lDs.getRowCount() );
   }


   /**
    * Tests the returned values from the query.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQueryDetails() throws Exception {

      PartRequestKey lPartRequestAtUserLocation =
            buildOpenPartRequest( RefReqTypeKey.ADHOC, iUserLocation, false );
      PartRequestKey lPartRequestAtOtherLocation =
            buildOpenPartRequest( RefReqTypeKey.ADHOC, iOtherLocation, false );
      PartRequestKey lPartRequestWithTask =
            buildOpenPartRequestWithTask( RefReqTypeKey.TASK, iOtherLocation );

      // create open adhoc part request for KIT
      PartRequestKey lPartRequestforKITWithAdHoc =
            buildOpenPartRequest( RefReqTypeKey.ADHOC, iUserLocation, true );

      // store the part request keys so we can delete them afterwards
      iPartRequests = Arrays.asList( lPartRequestAtUserLocation, lPartRequestAtOtherLocation,
            lPartRequestWithTask, lPartRequestforKITWithAdHoc );
      // execute the query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aDayCount", DAY_COUNT );
      lArgs.add( "aHrDbId", iCurrentHR.getDbId() );
      lArgs.add( "aHrId", iCurrentHR.getId() );
      lArgs.add( "aShowAssignedToOthers", false );
      lArgs.add( "aAllLocations", true );

      DataSet lDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      // assert all three part requests were returned and verify their details
      assertEquals( "Number of retrieved rows", 4, lDs.getRowCount() );

      while ( lDs.hasNext() ) {
         lDs.next();
         PartRequestKey lPartRequest = lDs.getKey( PartRequestKey.class, "req_part_key" );
         testPartRequestDetailsRow( lDs, lPartRequest );
         testAvailableInventoryRow( lDs, "0*0", "0*0", "0*0", "0*0" );

         if ( lPartRequestWithTask.equals( lPartRequest ) ) {
            // check the details of the task, work package and aircraft are correct
            testTaskInformationRow( lDs, iTask.getEventKey(), TASK_NAME, TASK_BARCODE );
            testCheckInformationRow( lDs, iWorkPackage.getEventKey(), WP_NAME, WP_BARCODE );
            testAircraftInventoryRow( lDs, iBoeing757, ACFT_DESC );
         } else {
            // no other part requests have any task, work package or aircraft info
            testTaskInformationRow( lDs, null, null, null );
            testCheckInformationRow( lDs, null, null, null );
            testAircraftInventoryRow( lDs, null, null );
         }
      }
   }


   /**
    * Tests open stock requests are returned.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testStockRequestIsReturned() throws Exception {

      // data setup - create an open stock request
      PartRequestKey lStockRequest =
            buildOpenPartRequest( RefReqTypeKey.STOCK, iUserLocation, false );

      // store the part request keys so we can delete them afterwards
      iPartRequests = Arrays.asList( lStockRequest );

      // execute the query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aDayCount", DAY_COUNT );
      lArgs.add( "aHrDbId", iCurrentHR.getDbId() );
      lArgs.add( "aHrId", iCurrentHR.getId() );
      lArgs.add( "aShowAssignedToOthers", false );
      lArgs.add( "aAllLocations", false );

      DataSet lDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // assert the stock request is returned
      assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );
      lDs.next();
      testPartRequestDetailsRow( lDs, lStockRequest );
   }


   /**
    * Tests only requests assigned to the current HR are returned if aShowAssignedToOthers is true.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testUserCanSeeRequestsAssignedToHer() throws Exception {

      // data setup - create one request with the current user assigned and one with a different
      // user assigned
      PartRequestKey lPartRequestWithCurrentHrAssigned =
            buildOpenPartRequestWithAssignedHR( RefReqTypeKey.ADHOC, iUserLocation, iCurrentHR );
      PartRequestKey lPartRequestWithDifferentHrAssigned =
            buildOpenPartRequestWithAssignedHR( RefReqTypeKey.ADHOC, iUserLocation, iDifferentHR );

      // store the part request keys so we can delete them afterwards
      iPartRequests =
            Arrays.asList( lPartRequestWithCurrentHrAssigned, lPartRequestWithDifferentHrAssigned );

      // execute the query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aDayCount", DAY_COUNT );
      lArgs.add( "aHrDbId", iCurrentHR.getDbId() );
      lArgs.add( "aHrId", iCurrentHR.getId() );
      lArgs.add( "aShowAssignedToOthers", false );
      lArgs.add( "aAllLocations", false );

      DataSet lDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // assert that only the request assigned to the current HR is returned
      assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );
      lDs.next();
      testPartRequestDetailsRow( lDs, lPartRequestWithCurrentHrAssigned );
   }


   /**
    * Tests requests assigned to any HR are returned if aShowAssignedToOthers is false.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testUserCanSeeRequestsAssignedToAnyHR() throws Exception {

      // data setup - create one request with the current user assigned and one with a different
      // user assigned
      PartRequestKey lPartRequestWithCurrentHrAssigned =
            buildOpenPartRequestWithAssignedHR( RefReqTypeKey.ADHOC, iUserLocation, iCurrentHR );
      PartRequestKey lPartRequestWithDifferentHrAssigned =
            buildOpenPartRequestWithAssignedHR( RefReqTypeKey.ADHOC, iUserLocation, iDifferentHR );

      // store the part request keys so we can delete them afterwards
      iPartRequests =
            Arrays.asList( lPartRequestWithCurrentHrAssigned, lPartRequestWithDifferentHrAssigned );

      // execute the query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aDayCount", DAY_COUNT );
      lArgs.add( "aHrDbId", iCurrentHR.getDbId() );
      lArgs.add( "aHrId", iCurrentHR.getId() );
      lArgs.add( "aShowAssignedToOthers", true );
      lArgs.add( "aAllLocations", false );

      DataSet lDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // assert both requests are returned
      assertEquals( "Number of retrieved rows", 2, lDs.getRowCount() );
   }


   @Before
   public void loadData() throws Exception {

      /**
       * Create the data with the tables instead of builders because we have to manually delete the
       * data afterwards. This is because the OpenPartRequests query depends on a materialized view,
       * and when the view is refreshed, the data is implicitly committed.
       */
      iUserSupplyLocation = buildLocation( RefLocTypeKey.AIRPORT, AIR_PORT_LOC, true, null );

      iUserLocation = buildLocation( RefLocTypeKey.SRVSTORE, SRVSTORE, false, iUserSupplyLocation );

      iOtherLocation = buildLocation( RefLocTypeKey.AIRPORT, OTHER_LOC_NAME, true, null );

      iPartGroupKit = buildPartGroup( RefInvClassKey.KIT );
      iPart = buildPart( PART, RefInvClassKey.BATCH, null );

      iKitPart = buildPart( KIT_PART, RefInvClassKey.KIT, iPartGroupKit );

      // create a user and link the user to the iUserSupplyLocation
      int lCurrentUserId = 991;
      iCurrentHR = buildUser( CURRENT_HR, lCurrentUserId );
      iOrgHrSupplyLink =
            linkHumanResourceToSupplyLocation( iUserSupplyLocation, lCurrentUserId, iCurrentHR );

      iDifferentHR = buildUser( DIFFERENT_HR, lCurrentUserId + 1 );
   }


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


   private LocationKey buildLocation( RefLocTypeKey aType, String aCode, boolean isSupply,
         LocationKey aSupply ) {

      return Domain.createLocation( aLocation -> {
         aLocation.setType( aType );
         aLocation.setCode( aCode );
         aLocation.setIsSupplyLocation( isSupply );
         aLocation.setSupplyLocation( aSupply );
      } );
   }


   private PartNoKey buildPart( String aOemPartNo, RefInvClassKey aInventoryClass,
         PartGroupKey aPartGroup ) {

      return Domain.createPart( aPart -> {
         aPart.setPartGroup( aPartGroup, true );
         aPart.setInventoryClass( aInventoryClass );
         aPart.setPartStatus( RefPartStatusKey.ACTV );
         aPart.setQtyUnitKey( RefQtyUnitKey.EA );
         aPart.setCode( aOemPartNo );
      } );

   }


   private PartGroupKey buildPartGroup( RefInvClassKey aInventoryClass ) {

      // create part group to assign for a kit part
      return Domain.createPartGroup( aPartGroup -> {
         aPartGroup.setInventoryClass( RefInvClassKey.KIT );

         aPartGroup.setCode( PART_GROUP );
      } );

   }


   private PartRequestKey buildPartRequest( RefReqTypeKey aType, LocationKey aNeededAt,
         HumanResourceKey aHumanResource, TaskKey aTask, Boolean aIsKit ) {

      return Domain.createPartRequest( aReqPart -> {
         aReqPart.status( RefEventStatusKey.PROPEN );
         aReqPart.requiredBy( new Date() );
         aReqPart.requestedQuantity( 10.0 );
         aReqPart.type( aType );
         aReqPart.neededAt( aNeededAt );
         aReqPart.purchasePart( iPart );
         if ( aIsKit ) {
            aReqPart.purchasePart( iKitPart );
            aReqPart.partGroup( iPartGroupKit );
         }
         aReqPart.priority( RefReqPriorityKey.NORMAL );
         aReqPart.lastAutoRsrvDate( new Date() );
         if ( aTask != null ) {
            aReqPart.task( aTask );
         }
         aReqPart.assignedTo( aHumanResource );
         aReqPart.setQuantityUnit( RefQtyUnitKey.EA );
      } );
   }


   private PartRequestKey buildOpenPartRequest( RefReqTypeKey aType, LocationKey aNeededAt,
         Boolean aIsKit ) {
      return buildPartRequest( aType, aNeededAt, null, null, aIsKit );
   }


   private PartRequestKey buildOpenPartRequestWithAssignedHR( RefReqTypeKey aType,
         LocationKey aNeededAt, HumanResourceKey aHumanResource ) {
      return buildPartRequest( aType, aNeededAt, aHumanResource, null, false );
   }


   private PartRequestKey buildOpenPartRequestWithTask( RefReqTypeKey aType,
         LocationKey aOtherLocation ) {

      iAircraft = buildPart( AIR_CRAFT, RefInvClassKey.ACFT, null );
      iBoeing757 =
            buildInventory( iAircraft, INV_NO, ACFT_DESC, RefInvClassKey.ACFT, iOtherLocation );
      iWorkPackage = buildTask( RefTaskClassKey.CHECK, null, iBoeing757, WP_BARCODE, WP_NAME );
      iTask = buildTask( RefTaskClassKey.ADHOC, iWorkPackage, null, TASK_BARCODE, TASK_NAME );

      return buildPartRequest( aType, aOtherLocation, null, iTask, false );
   }


   private TaskKey buildTask( RefTaskClassKey aTaskClass, TaskKey aParentTask,
         InventoryKey aMainInventory, String aTaskBarcode, String aTaskName ) {

      TaskKey lTaskKey = new TaskBuilder().withParentTask( aParentTask )
            .withDescription( aTaskName ).onInventory( aMainInventory ).withBarcode( aTaskBarcode )
            .withTaskClass( aTaskClass ).withRequestParts( true ).withStatus( null ).build();

      if ( aMainInventory != null ) {
         new EventInventoryBuilder().withEventKey( lTaskKey.getEventKey() )
               .withInventoryKey( aMainInventory ).isMainInv( true )
               .withHInventoryKey( InvInvTable.findByPrimaryKey( aMainInventory ).getHInvNo() )
               .build();
      }
      return lTaskKey;
   }


   private InventoryKey buildInventory( PartNoKey aPartNo, String aSerialNo, String aDesc,
         RefInvClassKey aInvClass, LocationKey aLocation ) {

      InventoryKey lInventory = InvInvTable.generatePk();
      InvInvTable lTable = InvInvTable.create( lInventory );
      lTable.setInvCond( RefInvCondKey.RFI );
      lTable.setInvClass( aInvClass );
      lTable.setInvNoSdesc( aDesc );
      lTable.setPartNo( aPartNo );
      lTable.setHInvNo( lInventory );
      lTable.setLocation( aLocation );
      lTable.setSerialNoOEM( aSerialNo );
      lTable.insert();
      return lInventory;
   }


   /**
    *
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
    * This method tests the inventory availability section
    *
    * @param aDs
    *           dataset object
    * @param aAvailLocal
    *           available local inventory
    * @param aAvailRemote
    *           available remote inventory
    * @param aAvailUS
    *           available unserviceable inventory
    * @param aAvailOnOrder
    *           available on order inventory
    */
   private void testAvailableInventoryRow( DataSet aDs, String aAvailLocal, String aAvailRemote,
         String aAvailUS, String aAvailOnOrder ) {
      MxAssert.assertEquals( "avail_local", aAvailLocal, aDs.getString( "avail_local" ) );
      MxAssert.assertEquals( "avail_remote", aAvailRemote, aDs.getString( "avail_remote" ) );
      MxAssert.assertEquals( "avail_us", aAvailUS, aDs.getString( "avail_us" ) );
      MxAssert.assertEquals( "avail_on_order", aAvailOnOrder, aDs.getString( "avail_on_order" ) );
   }


   /**
    * Tests the part request details
    *
    * @param aDs
    *           The dataset
    * @param aPartRequest
    *           The request key
    */
   private void testPartRequestDetailsRow( DataSet aDs, PartRequestKey aPartRequest ) {
      MxAssert.assertEquals( "req_part_key", aPartRequest,
            aDs.getKey( PartRequestKey.class, "req_part_key" ) );

      EvtEventTable lEvtEvent = EvtEventTable.findByPrimaryKey( aPartRequest.getEventKey() );
      MxAssert.assertEquals( "req_part_sdesc", lEvtEvent.getEventSdesc(),
            aDs.getString( "req_part_sdesc" ) );

      // part request details
      ReqPartTable lReqPart = ReqPartTable.findByPrimaryKey( aPartRequest );
      MxAssert.assertEquals( "req_part_purch_type_cd", lReqPart.getPurchType(),
            aDs.getString( "req_part_purch_type_cd" ) );
      MxAssert.assertEquals( "req_part_req_qt", new Double( lReqPart.getReqQt() ),
            aDs.getDoubleObj( "req_part_req_qt" ) );
      MxAssert.assertEquals( "req_part_priority_cd", lReqPart.getReqPriority().getCd(),
            aDs.getString( "req_part_priority_cd" ) );
      MxAssert.assertEquals( "req_part_req_by_dt", lReqPart.getReqByDt(),
            aDs.getDate( "req_part_req_by_dt" ) );
      MxAssert.assertEquals( "loc_key", lReqPart.getReqLoc(),
            aDs.getKey( LocationKey.class, "loc_key" ) );
      MxAssert.assertEquals( "req_part_no_key", lReqPart.getPoPartNo(),
            aDs.getKey( PartNoKey.class, "req_part_no_key" ) );
   }


   /**
    * This method tests the contents of aircraft and inventory row
    *
    * @param aDs
    *           dataset object
    * @param aInvKey
    *           inventory key
    * @param aInvSdesc
    *           inventory description
    */
   private void testAircraftInventoryRow( DataSet aDs, InventoryKey aInvKey, String aInvSdesc ) {
      MxAssert.assertEquals( "inventory_key", aInvKey,
            aDs.getKey( InventoryKey.class, "inventory_key" ) );
      MxAssert.assertEquals( "inv_no_sdesc", aInvSdesc, aDs.getString( "wp_inv_desc" ) );
   }


   /**
    * This tests the Work Package / Check information
    *
    * @param aDs
    *           dataset object
    * @param aCheckKey
    *           check key
    * @param aCheckSdesc
    *           check description
    * @param aCheckBarcode
    *           check barcode
    */
   private void testCheckInformationRow( DataSet aDs, EventKey aCheckKey, String aCheckSdesc,
         String aCheckBarcode ) {
      MxAssert.assertEquals( "wp_key", aCheckKey, aDs.getKey( EventKey.class, "wp_key" ) );
      MxAssert.assertEquals( "wp_desc", aCheckSdesc, aDs.getString( "wp_desc" ) );
      MxAssert.assertEquals( "wp_barcode", aCheckBarcode, aDs.getString( "wp_barcode" ) );
   }


   /**
    * This tests the Needed For Task section
    *
    * @param aDs
    *           dataset
    * @param aTaskKey
    *           task key
    * @param aTaskSdesc
    *           task description
    * @param aTaskBarcode
    *           task barcode
    */
   private void testTaskInformationRow( DataSet aDs, EventKey aTaskKey, String aTaskSdesc,
         String aTaskBarcode ) {
      MxAssert.assertEquals( "task_key", aTaskKey, aDs.getKey( EventKey.class, "task_key" ) );
      MxAssert.assertEquals( "task_sdesc", aTaskSdesc, aDs.getString( "task_desc" ) );
      MxAssert.assertEquals( "task_barcode", aTaskBarcode, aDs.getString( "task_barcode" ) );
   }
}
