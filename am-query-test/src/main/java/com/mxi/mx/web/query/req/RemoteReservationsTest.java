
package com.mxi.mx.web.query.req;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.DataSetException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
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
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.req.ReqPartTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class RemoteReservationsTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final RefInvClassKey BATCH = RefInvClassKey.BATCH;
   private static final RefInvClassKey SERIALIZED = RefInvClassKey.SER;

   private static final double RESERVE_QT = 10.0;
   private static final double NEEDED_QT = 10.0;
   private static final Date NEEDED_BY_DATE = DateUtils.addDays( new Date(), 10 );
   private static final String TASK_CODE = "Task Barcode";
   private static final String TASK_NAME = "Task Name";
   private static final String TASK_DESC = TASK_NAME + " [" + TASK_CODE + "]";
   private static final String WP_CODE = "WP Barcode";
   private static final String WP_NAME = "WP Name";
   private static final String WP_DESC = WP_NAME + " [" + WP_CODE + "]";
   private static final String ACFT_DESC = "Aircraftiness";
   private static final String SERIAL_NO = "Serial No";

   private InventoryKey iBatchInventory;
   private InventoryKey iSerializedInventory;
   private LocationKey iDockLocationForYYZ;
   private LocationKey iLineLocationForYYZ;
   private LocationKey iSupplyLocationForYYZ;
   private OwnerKey iLocalOwner;
   private PartNoKey iBatchPartNo;
   private PartNoKey iSerializedPartNo;
   private PartRequestKey iBatchPartRequest;
   private PartRequestKey iSerializedPartRequest;


   /**
    *
    * Test that batch inventory is returned (happy path).
    *
    * @throws ParseException
    * @throws DataSetException
    *
    */
   @Test
   public void testBatchInventoryReturned() throws DataSetException, ParseException {

      DataSet lDs = execute( iBatchPartNo, iLineLocationForYYZ );

      // check that one inventory item was returned
      MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );
      // and let's confirm the details of the item
      lDs.next();
      MxAssert.assertEquals( "inventory_key", iBatchInventory, lDs.getString( "inventory_key" ) );
      MxAssert.assertEquals( "serial_no_oem", SERIAL_NO, lDs.getString( "serial_no_oem" ) );
      MxAssert.assertEquals( "reserve_qt", RESERVE_QT, lDs.getDouble( "reserve_qt" ) );
      MxAssert.assertEquals( "decimal_places_qt", 0, lDs.getDouble( "decimal_places_qt" ) );
      MxAssert.assertEquals( "qty_unit_cd", "EA", lDs.getString( "qty_unit_cd" ) );
      MxAssert.assertEquals( "req_part_pk", iBatchPartRequest, lDs.getString( "req_part_pk" ) );
      MxAssert.assertEquals( "req_priority_cd", "NORMAL", lDs.getString( "req_priority_cd" ) );
      MxAssert.assertEquals( "needed_qt", NEEDED_QT, lDs.getDouble( "needed_qt" ) );
      MxAssert.assertEquals( "needed_by_dt", NEEDED_BY_DATE, lDs.getDate( "needed_by_dt" ) );
   }


   /**
    *
    * Test that serialized inventory is returned (happy path).
    *
    * @throws ParseException
    * @throws DataSetException
    *
    */
   @Test
   public void testSerializedInventoryReturned() throws DataSetException, ParseException {

      DataSet lDs = execute( iSerializedPartNo, iLineLocationForYYZ );

      // check that one inventory item was returned
      MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );
      // and let's confirm the details of the item
      lDs.next();
      MxAssert.assertEquals( "inventory_key", iSerializedInventory,
            lDs.getString( "inventory_key" ) );
      MxAssert.assertEquals( "serial_no_oem", SERIAL_NO, lDs.getString( "serial_no_oem" ) );
      MxAssert.assertEquals( "reserve_qt", 0.0, lDs.getDouble( "reserve_qt" ) );
      MxAssert.assertEquals( "decimal_places_qt", 0, lDs.getDouble( "decimal_places_qt" ) );
      MxAssert.assertEquals( "qty_unit_cd", "EA", lDs.getString( "qty_unit_cd" ) );
      MxAssert.assertEquals( "req_part_pk", iSerializedPartRequest,
            lDs.getString( "req_part_pk" ) );
      MxAssert.assertEquals( "req_priority_cd", "AOG", lDs.getString( "req_priority_cd" ) );
      MxAssert.assertEquals( "needed_qt", 1.0, lDs.getDouble( "needed_qt" ) );
      MxAssert.assertEquals( "needed_by_dt", NEEDED_BY_DATE, lDs.getDate( "needed_by_dt" ) );
   }


   /**
    *
    * Test that inventory is returned when it is under the same supply location as the location
    * argument in the query.
    *
    */
   @Test
   public void testInventoryUnderSameSupplyLocation() {
      // execute the query for another location under the YYZ supply location
      DataSet lDs = execute( iBatchPartNo, iDockLocationForYYZ );
      assertInventoryReturned( lDs, iBatchInventory );
   }


   /**
    *
    * Test that inventory is not returned when it is under a different supply location as the
    * location argument in the query.
    *
    */
   @Test
   public void testInventoryUnderDifferentSupplyLocation() {
      // create a second supply location
      LocationKey iSupplyLocationForATL = new LocationDomainBuilder().withCode( "ATL" )
            .withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();

      LocationKey iLineLocationForATL = new LocationDomainBuilder().withCode( "ATL/LINE" )
            .withType( RefLocTypeKey.LINE ).withSupplyLocation( iSupplyLocationForATL ).build();

      DataSet lDs = execute( iBatchPartNo, iLineLocationForATL );
      assertInventoryNotReturned( lDs );
   }


   /**
    *
    * Test that batch inventory is not returned when it is not reserved (query only returns
    * reserved).
    *
    */
   @Test
   public void testBatchInventoryNotReserved() {
      // ensure the inventory has no reserved quantity
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iBatchInventory );
      lInvInv.setReservedQt( 0.0 );
      lInvInv.update();

      DataSet lDs = execute( iBatchPartNo, iLineLocationForYYZ );
      assertInventoryNotReturned( lDs );
   }


   /**
    *
    * Test that serialized inventory is not returned when it is not reserved (query only returns
    * reserved).
    *
    */
   @Test
   public void testSerializedInventoryNotReserved() {
      // ensure the inventory is not reserved
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iSerializedInventory );
      lInvInv.setReservedBool( false );
      lInvInv.update();

      DataSet lDs = execute( iSerializedPartNo, iLineLocationForYYZ );
      assertInventoryNotReturned( lDs );
   }


   /**
    *
    * Test that inventory is not returned when it is not RFI condition.
    *
    */
   @Test
   public void testInventoryNotRFI() {
      // set inventory condition to inspection required
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iBatchInventory );
      lInvInv.setInvCond( RefInvCondKey.INSPREQ );
      lInvInv.update();

      DataSet lDs = execute( iBatchPartNo, iLineLocationForYYZ );
      assertInventoryNotReturned( lDs );
   }


   /**
    *
    * Test that inventory is not returned when it is issued.
    *
    */
   @Test
   public void testInventoryIssued() {
      // set inventory condition to inspection required
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iBatchInventory );
      lInvInv.setIssuedBool( true );
      lInvInv.update();

      DataSet lDs = execute( iBatchPartNo, iLineLocationForYYZ );
      assertInventoryNotReturned( lDs );
   }


   /**
    *
    * Test that inventory is not returned when a part request does not have it reserved (query only
    * returns reserved).
    *
    */
   @Test
   public void testPartRequestHasNoReservedInventory() {
      // ensure the inventory is not reserved by the part request
      ReqPartTable lReqPart = ReqPartTable.findByPrimaryKey( iBatchPartRequest );
      lReqPart.setInventory( null );
      lReqPart.update();

      DataSet lDs = execute( iBatchPartNo, iLineLocationForYYZ );
      assertInventoryNotReturned( lDs );
   }


   /**
    *
    * Test that inventory is not returned when the part request which has it reserved is of type
    * STOCK.
    *
    */
   @Test
   public void testPartRequestIsStock() {
      // ensure the inventory is not reserved by the part request
      ReqPartTable lReqPart = ReqPartTable.findByPrimaryKey( iBatchPartRequest );
      lReqPart.setReqType( RefReqTypeKey.STOCK );
      lReqPart.update();

      DataSet lDs = execute( iBatchPartNo, iLineLocationForYYZ );
      assertInventoryNotReturned( lDs );
   }


   /**
    *
    * Test that inventory is returned when the part request has status of PRREMOTE.
    *
    */
   @Test
   public void testPartRequestStatusPRREMOTE() {
      // update status of request to be PRREMOTE
      EvtEventTable lEvtEvent = EvtEventTable.findByPrimaryKey( iBatchPartRequest );
      lEvtEvent.setStatus( RefEventStatusKey.PRREMOTE );
      lEvtEvent.update();

      DataSet lDs = execute( iBatchPartNo, iLineLocationForYYZ );
      assertInventoryReturned( lDs, iBatchInventory );
   }


   /**
    *
    * Test that inventory is not returned when the part request has a status other than PRAVAIL or
    * PRREMOTE.
    *
    */
   @Test
   public void testPartRequestStatusNotPRAVAILorPRREMOTE() {
      // update status of request to be something other than PRAVAIL or PRREMOTE
      EvtEventTable lEvtEvent = EvtEventTable.findByPrimaryKey( iBatchPartRequest );
      lEvtEvent.setStatus( RefEventStatusKey.PRCANCEL );
      lEvtEvent.update();

      DataSet lDs = execute( iBatchPartNo, iLineLocationForYYZ );
      assertInventoryNotReturned( lDs );
   }


   @Before
   public void setUp() {

      iLocalOwner = new OwnerDomainBuilder().build();

      iSupplyLocationForYYZ = new LocationDomainBuilder().withCode( "YYZ" )
            .withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();

      iLineLocationForYYZ = new LocationDomainBuilder().withCode( "YYZ/LINE" )
            .withType( RefLocTypeKey.LINE ).withSupplyLocation( iSupplyLocationForYYZ ).build();

      iDockLocationForYYZ = new LocationDomainBuilder().withCode( "YYZ/DOCK" )
            .withType( RefLocTypeKey.DOCK ).withSupplyLocation( iSupplyLocationForYYZ ).build();

      // create a batch part
      iBatchPartNo = new PartNoBuilder().withOemPartNo( "PART-BATCH" ).withInventoryClass( BATCH )
            .withUnitType( RefQtyUnitKey.EA ).withStatus( RefPartStatusKey.ACTV ).build();

      // create reserved inventory for the batch part
      iBatchInventory = new InventoryBuilder().withClass( BATCH ).withPartNo( iBatchPartNo )
            .withSerialNo( SERIAL_NO ).withCondition( RefInvCondKey.RFI ).withBinQt( RESERVE_QT )
            .withReserveQt( RESERVE_QT ).atLocation( iLineLocationForYYZ ).withOwner( iLocalOwner )
            .build();

      // Create part request that has the batch inventory reserved
      iBatchPartRequest = new PartRequestBuilder().forSpecifiedPart( iBatchPartNo )
            .withRequestedQuantity( NEEDED_QT ).withPriority( RefReqPriorityKey.NORMAL )
            .withType( RefReqTypeKey.TASK ).withStatus( RefEventStatusKey.PRAVAIL )
            .isNeededAt( iSupplyLocationForYYZ ).requiredBy( NEEDED_BY_DATE )
            .withReservedInventory( iBatchInventory ).build();

      // create a serialized part
      iSerializedPartNo =
            new PartNoBuilder().withOemPartNo( "PART-SER" ).withInventoryClass( SERIALIZED )
                  .withUnitType( RefQtyUnitKey.EA ).withStatus( RefPartStatusKey.ACTV ).build();

      // create reserved inventory for the serialized part
      iSerializedInventory =
            new InventoryBuilder().withClass( SERIALIZED ).withPartNo( iSerializedPartNo )
                  .withSerialNo( SERIAL_NO ).withCondition( RefInvCondKey.RFI ).isReserved()
                  .atLocation( iLineLocationForYYZ ).withOwner( iLocalOwner ).build();

      // Create part request that has the serialized inventory reserved
      iSerializedPartRequest = new PartRequestBuilder().forSpecifiedPart( iSerializedPartNo )
            .withRequestedQuantity( 1.0 ).withPriority( RefReqPriorityKey.AOG )
            .withType( RefReqTypeKey.TASK ).withStatus( RefEventStatusKey.PRAVAIL )
            .isNeededAt( iLineLocationForYYZ ).requiredBy( NEEDED_BY_DATE )
            .withReservedInventory( iSerializedInventory ).build();
   }


   /**
    * Assert inventory was not returned by the query.
    *
    * @param aDs
    */
   private void assertInventoryNotReturned( DataSet aDs ) {
      MxAssert.assertEquals( "Number of retrieved rows", 0, aDs.getRowCount() );
   }


   /**
    * Assert the given inventory was returned by the query.
    *
    * @param aDs
    * @param aInventory
    */
   private void assertInventoryReturned( DataSet aDs, InventoryKey aInventory ) {
      MxAssert.assertEquals( "Number of retrieved rows", 1, aDs.getRowCount() );
      aDs.next();
      MxAssert.assertEquals( "inventory_key", aInventory, aDs.getString( "inventory_key" ) );

   }


   /**
    * Assert the inventories in the given list are returned in the order given (note that the query
    * orders inventories by inventory id, so the first created should be the first returned).
    *
    * @param aDs
    * @param aInventories
    */
   private void assertMultipleInventoriesReturned( DataSet aDs,
         ArrayList<InventoryKey> aInventories ) {
      MxAssert.assertEquals( "Number of retrieved rows", aInventories.size(), aDs.getRowCount() );
      // rows are ordered by inventory key (so the first created should be first returned)
      for ( int i = 0; i < aInventories.size(); i++ ) {
         aDs.next();
         MxAssert.assertEquals( "inventory_key", aInventories.get( i ),
               aDs.getString( "inventory_key" ) );
      }

   }


   /**
    * Execute the RemoteReservations query.
    *
    * @param aPartNo
    * @param aLocation
    *
    * @return DataSet result.
    */
   private DataSet execute( PartNoKey aPartNo, LocationKey aLocation ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPartNo, new String[] { "aPartNoDbId", "aPartNoId" } );
      lArgs.add( aLocation, new String[] { "aLocDbId", "aLocId" } );

      // Sort the Dataset
      DataSet lDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      // Execute the query
      return lDs;
   }
}
