package com.mxi.mx.web.query.stockdistribution;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.InventoryTransferBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.ManufacturerKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.StockDistReqKey;
import com.mxi.mx.core.key.TransferKey;
import com.mxi.mx.core.table.ref.RefEventStatus;


/**
 * This class performs unit testing on the query file with the same package and name.
 *
 * @author Libin Cai
 * @created October 3, 2018
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class StockDistRequestPickedItemTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String XFER_NO = "XFERID";
   private static final BigDecimal XFER_QTY = BigDecimal.TEN;
   private static final RefEventStatusKey LXPEND = RefEventStatusKey.LXPEND;
   private static final String SN_BN = "SN_BN";
   private static final String PART_NAME = "PART_NAME";
   private static final String OEM_PART_NO = "OEM_PART_NO";
   private static final String MFC_CD = "MFC_CD";
   private static final String OWNER_CD = "OWNER_CD";

   private TransferKey iXfer;
   private PartNoKey iPartNo;
   private InventoryKey iInventory;
   private ManufacturerKey iManufact;


   @Before
   public void loadData() throws Exception {

      iManufact = Domain.createManufacturer( mfc -> {
         mfc.setCode( MFC_CD );
         mfc.setName( "MFC_NAME" );
      } );

      iPartNo = Domain.createPart( part -> {
         part.setCode( OEM_PART_NO );
         part.setShortDescription( PART_NAME );
         part.setManufacturer( iManufact );
         part.setStorageDescription( "fragile" );
      } );

      OwnerKey lOwner = Domain.createOwner( owner -> owner.setCode( OWNER_CD ) );

      iInventory = Domain.createBatchInventory( inv -> {
         inv.setBatchNumber( SN_BN );
         inv.setOwner( lOwner );
         inv.setPartNumber( iPartNo );
      } );

      iXfer = createXfer( LXPEND );
   }


   /**
    * Test to make sure the query returns correct data.
    */
   @Test
   public void testQuery() {

      // don't need to create real data for stock dist req because it is not needed in the query but
      // the key only
      StockDistReqKey lStockDistReq = new StockDistReqKey( 1, 1 );

      insertDataToTable( lStockDistReq, iXfer );

      // add a cancelled transfer to the stock dist request picked item table. Don't worry about two
      // transfers on the same transfer. It breaks the rule but has nothing wrong for this test.
      insertDataToTable( lStockDistReq, createXfer( RefEventStatusKey.LXCANCEL ) );

      // execute query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lStockDistReq, "aStockDistReqId", "aStockDistReqDbId" );
      QuerySet lQs = QueryExecutor.executeQuery( getClass(), lArgs );

      // the cancelled transfer is not returned
      assertEquals( 1, lQs.getRowCount() );

      // assert data for the returned record
      lQs.next();

      RefEventStatus lStatus = RefEventStatus.findByPrimaryKey( LXPEND );

      assertEquals( 1, lQs.getInt( "line_no" ) );
      assertEquals( iXfer, lQs.getKey( TransferKey.class, "xfer_key" ) );
      assertEquals( XFER_QTY.doubleValue(), lQs.getDouble( "xfer_qt" ), 0 );
      assertEquals( XFER_NO, lQs.getString( "xfer_no" ) );
      assertEquals( lStatus.getUserStatusCd() + " (" + lStatus.getSDesc() + ")",
            lQs.getString( "xfer_status" ) );
      assertEquals( iPartNo, lQs.getKey( PartNoKey.class, "part_no_key" ) );
      assertEquals( PART_NAME, lQs.getString( "part_no_sdesc" ) );
      assertEquals( OEM_PART_NO, lQs.getString( "part_no_oem" ) );
      assertEquals( iManufact, lQs.getKey( ManufacturerKey.class, "manufacturer_key" ) );
      assertEquals( MFC_CD, lQs.getString( "manufact_cd" ) );
      assertEquals( iInventory, lQs.getKey( InventoryKey.class, "inv_key" ) );
      assertEquals( SN_BN, lQs.getString( "serial_no_oem" ) );
      assertEquals( OWNER_CD, lQs.getString( "owner_cd" ) );
      assertEquals( true, lQs.getBoolean( "special_handling_bool" ) );
   }


   private TransferKey createXfer( RefEventStatusKey aStatus ) {

      return new InventoryTransferBuilder().withTransferId( XFER_NO ).withStatus( aStatus )
            .withInventory( iInventory ).withQuantity( XFER_QTY ).build();
   }


   private void insertDataToTable( StockDistReqKey aStockDistReq, TransferKey aXfer ) {

      DataSetArgument lArgs = aStockDistReq.getPKWhereArg();
      lArgs.add( aXfer.getPKWhereArg() );

      MxDataAccess.getInstance().executeInsert( "stock_dist_req_picked_item", lArgs );
   }

}
