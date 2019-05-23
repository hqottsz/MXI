
package com.mxi.mx.core.query.shipment;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.table.ship.ShipShipmentLineTable;
import com.mxi.mx.core.unittest.table.ship.ShipShipmentLine;


/**
 * Tests update query UpdateShipmentLineSerialNo.qrx
 *
 * @author vchan
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class UpdateShipmentLineSerialNoTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            UpdateShipmentLineSerialNoTest.class );
   }


   /** The query execution data set */
   private int iRowCount = -1;


   /**
    * Tests
    * <li>Query updates the 2 active shipment lines with SN01 and PartKey 4650:3000</li>
    * <li>Query does not update lines with matching part key but not SN</li>
    * <li>Query does not update lines with matching SN but not part key</li>
    */
   @Test
   public void testQuery() {
      final PartNoKey lPartNoKey = new PartNoKey( "4650:3000" );
      final String lOldSn = "SN01";
      final String lNewSn = "SN01NEW";

      execute( lOldSn, lNewSn, lPartNoKey );

      ShipShipmentLineTable lShipShipmentLine =
            ShipShipmentLine.findByPrimaryKey( new ShipmentLineKey( "4650:10500" ) );

      // check shipment line serial number has been updated
      assertEquals( lNewSn, lShipShipmentLine.getSerialNoOem() );

      lShipShipmentLine = ShipShipmentLine.findByPrimaryKey( new ShipmentLineKey( "4650:10700" ) );

      // check shipment line serial number has been updated
      assertEquals( lNewSn, lShipShipmentLine.getSerialNoOem() );

      // check only these 2 lines were updated.
      assertEquals( 2, iRowCount );
   }


   /**
    * Tests
    * <li>Update does not update shipment lines associated with historical shipments</li>
    */
   @Test
   public void testQueryHistorical() {
      final PartNoKey lPartNoKey = new PartNoKey( "4650:3005" );
      final String lOldSn = "SN04";
      final String lNewSn = "SN04NEW";

      execute( lOldSn, lNewSn, lPartNoKey );

      ShipShipmentLineTable lShipShipmentLine =
            ShipShipmentLine.findByPrimaryKey( new ShipmentLineKey( "4650:10600" ) );

      // check shipment line serial number has NOT been updated
      assertEquals( lOldSn, lShipShipmentLine.getSerialNoOem() );

      // check that no lines were updated
      assertEquals( 0, iRowCount );
   }


   /**
    * Execute the query.
    *
    * @param aOldSerialNo
    *           db id of task
    * @param aNewSerialNo
    *           id of task
    * @param aPartNoKey
    *           DOCUMENT_ME
    */
   private void execute( String aOldSerialNo, String aNewSerialNo, PartNoKey aPartNoKey ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aNewSerialNoOem", aNewSerialNo );
      lArgs.add( "aOldSerialNoOem", aOldSerialNo );
      lArgs.add( aPartNoKey, new String[] { "aPartNoDbId", "aPartNoId" } );

      iRowCount = QueryExecutor.executeUpdate( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
