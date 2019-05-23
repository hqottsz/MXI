
package com.mxi.mx.core.unittest.table.inv;

import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;

import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.utils.FormatUtils;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.AuthorityKey;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvLocTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class is used to check values in the <code>inv_inv</code> table.
 *
 * @author ysotozaki
 * @created April 28, 2002
 */

public class InvInv extends InvInvTable {

   /**
    * Initializes the class. Retrieves all columns in the database table for the given primary key.
    *
    * @param aKey
    *           primary key of the table.
    */
   public InvInv(InventoryKey aKey) {
      super( aKey );
   }


   /**
    * Returns the value of the inventory from serial no, part no, condition , issued boolean and loc
    * property.
    *
    * @param aPartNo
    *           The part no
    * @param aSerialNoOem
    *           The serial no
    * @param aCondition
    *           The condition
    * @param aIssuedBool
    *           The issued boolean
    * @param aLocation
    *           The location
    * @param aNhInventory
    *           The parent inventory
    *
    * @return the value of the inventory from serial no, part no, condition , issued boolean and loc
    *         property.
    */
   public static InventoryKey getInvBySerialPartConditionIssuedAndLoc( PartNoKey aPartNo,
         String aSerialNoOem, RefInvCondKey aCondition, boolean aIssuedBool, LocationKey aLocation,
         InventoryKey aNhInventory ) {
      LocationKey lSupplyLocation = InvLocTable.findByPrimaryKey( aLocation ).getSupplyLocation();

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lSupplyLocation, "aSupplyLocDbId", "aSupplyLocId" );
      lArgs.add( aPartNo, "aPartNoDbId", "aPartNoId" );
      lArgs.add( "aSerialNoOEM", aSerialNoOem );
      lArgs.add( aCondition, "aInvCondDbId", "aInvCondCd" );
      lArgs.add( "aExcludeKit", false );
      lArgs.add( "aExcludeLock", false );
      lArgs.add( "aExcludeIssued", false );
      lArgs.addWhereBoolean( "inv_inv.issued_bool", aIssuedBool );
      lArgs.addWhereEquals( new String[] { "inv_inv.nh_inv_no_db_id", "inv_inv.nh_inv_no_id" },
            aNhInventory );

      QuerySet lInventoryDs = QuerySetFactory.getInstance().executeQuery(
            "com.mxi.mx.core.query.inventory.findInvByPartLocSerialAndCondition", lArgs );

      if ( lInventoryDs.next() ) {
         return lInventoryDs.getKey( InventoryKey.class, "inv_no_db_id", "inv_no_id" );
      }

      return null;
   }


   /**
    * Returns the value of the inventory from serial no property.
    *
    * @param aSerialNo
    *           The serial number
    * @param aLocation
    *           The location key
    *
    * @return the value of the inventory from serial no property.
    */
   public static InventoryKey[] getInventoryFromSerialNo( String aSerialNo,
         LocationKey aLocation ) {

      // return array of InventoryKeys
      List<InventoryKey> lInventoryKeys = new ArrayList<InventoryKey>();

      // used to located sub components of main inventory.

      // get sub inventory of main inventory.
      DataSetArgument lArg = new DataSetArgument();
      lArg.add( "serial_no_oem", aSerialNo );
      lArg.add( aLocation, "loc_db_id", "loc_id" );

      String[] lColumns = new String[] { "inv_no_db_id", "inv_no_id" };

      DataSet lDataSet = MxDataAccess.getInstance().executeQuery( lColumns, "inv_inv", lArg );

      // for each sub inventory, add inventory pk to return array
      while ( lDataSet.next() ) {
         lInventoryKeys.add( lDataSet.getKey( InventoryKey.class, "inv_no_db_id", "inv_no_id" ) );
      }

      return lInventoryKeys.toArray( new InventoryKey[0] );
   }


   /**
    * Assert that the <code>alt_id</code> key and the argument are equal
    *
    * @param aAlternateKey
    *           the value to assert
    */
   public void assertAlternateKey( UUID aAlternateKey ) {
      MxAssert.assertEquals( aAlternateKey, getAlternateKey() );
   }


   /**
    * Test the applicability code field
    *
    * @param aExpected
    *           the expected applicability code
    */
   public void assertApplicabilityCode( String aExpected ) {
      MxAssert.assertEquals( aExpected, getApplEffCd() );
   }


   /**
    * Asserts that the <code>assmbl_inv_no_db_id:assmbl_inv_no_id</code> key and the argument are
    * equal.
    *
    * @param aExpected
    *           Assembly inventory to assert against actual data.
    */
   public void assertAssemblyInventory( InventoryKey aExpected ) {
      MxAssert.assertEquals( aExpected, getAssmblInvNo() );
   }


   /**
    * Asserts that the <code>authority_db_id:authority_id</code> key and the key argument are equal.
    *
    * @param aExpected
    *           Aurhority to assert against actual data.
    */
   public void assertAuthority( AuthorityKey aExpected ) {
      MxAssert.assertEquals( aExpected, getAuthority() );
   }


   /**
    * Asserts that the value in the <code>barcode_sdesc</code> column and the argument value are
    * equal.
    *
    * <ol>
    * <li>The call is delegated to {@link InvInv#assertStringField(String, String )
    * assertStringField()}.</li>
    * </ol>
    *
    * @param aExpected
    *           value to assert against actual value.
    */
   public void assertBarcode( String aExpected ) {
      MxAssert.assertEquals( aExpected, getBarcodeSdesc() );
   }


   /**
    * Asserts that the barcode has been set
    */
   public void assertBarcodeNotNull() {
      MxAssert.assertTrue( getBarcodeSdesc() != null );
   }


   /**
    * Asserts that the value in the <code>bin_qt</code> column and the argument value are equal.
    *
    * <ol>
    * <li>The call is delegated to {@link InvInv#assertDoubleField(String, Double )
    * assertDoubleField()}.</li>
    * </ol>
    *
    * @param aExpected
    *           value to assert against actual value.
    */
   public void assertBinQuantity( Double aExpected ) {
      Double lRoundOfValue = null;
      if ( aExpected != null ) {
         lRoundOfValue =
               new Double( FormatUtils.formatTrimDecimal( aExpected, getBinQtyDecimalPlaces() ) );
      }

      MxAssert.assertEquals( lRoundOfValue, getBinQt() );
   }


   /**
    * DOCUMENT_ME
    *
    * @param aExpected
    *           DOCUMENT_ME
    */
   public void assertBomItem( ConfigSlotKey aExpected ) {
      MxAssert.assertEquals( aExpected, getBOMItem() );
   }


   /**
    * Asserts that the <code>assmbl_db_id:assmbl_cd:assmbl_bom_id:assmbl_pos_id</code> key and the
    * argument are equal.
    *
    * @param aExpected
    *           BomItemPosition to assert against actual data.
    */
   public void assertBomItemPosition( ConfigSlotPositionKey aExpected ) {
      MxAssert.assertEquals( aExpected, getBOMItemPosition() );
   }


   /**
    * Asserts that the <code>bom_part_db_id:bom_part_id</code> key and the argument are equal.
    *
    * @param aExpected
    *           BomPart to assert against actual data.
    */
   public void assertBomPart( PartGroupKey aExpected ) {
      MxAssert.assertEquals( aExpected, getBomPart() );
   }


   /**
    * Asserts that the <code>carrier_db_id:carrier_id</code> key and the argument are equal.
    *
    * @param aExpected
    *           carrier to assert against the actual data.
    */
   public void assertCarrier( CarrierKey aExpected ) {
      MxAssert.assertEquals( aExpected, getCarrier() );
   }


   /**
    * Asserts that the <code>inv_class_cd</code> key and the argument are equal.
    *
    * @param aExpected
    *           Class code to assert against the actual data.
    */
   public void assertClassCd( String aExpected ) {
      MxAssert.assertEquals( aExpected, getInvClassCd() );
   }


   /**
    * Asserts that the value in the <code>complete_bool</code> column and the argument value are
    * equal.
    *
    * <ol>
    * <li>The call is delegated to {@link InvInv#assertBooleanField(String, boolean )
    * assertBooleanField()}.</li>
    * </ol>
    *
    * @param aExpected
    *           value to assert against actual value.
    */
   public void assertCompleteBoolean( boolean aExpected ) {
      MxAssert.assertEquals( aExpected, isCompleteBool() );
   }


   /**
    * Asserts that the <code>inv_cond_cd</code> key and the argument are equal.
    *
    * @param aExpected
    *           Condition code to assert against the actual data.
    */
   public void assertCondCd( String aExpected ) {
      MxAssert.assertEquals( aExpected, getInvCondCd() );
   }


   /**
    * Asserts that the inventory condition key and the argument are NOT equal.
    *
    * @param aNotExpected
    *           Condition code to assert against the actual data.
    */
   public void assertCondCdNotEquals( RefInvCondKey aNotExpected ) {
      assertNotEquals( aNotExpected, getInvCond() );
   }


   /**
    * Asserts that the value in the <code>config_pos_sdesc</code> column and the argument value are
    * equal.
    *
    * <ol>
    * <li>The call is delegated to {@link InvInv#assertStringField(String, String )
    * assertStringField()}.</li>
    * </ol>
    *
    * @param aExpected
    *           value to assert against actual value.
    */
   public void assertConfigPosSdesc( String aExpected ) {
      MxAssert.assertEquals( aExpected, getConfigPosSdesc() );
   }


   /**
    * Asserts that the table has a row corresponding to the current primary key.
    */
   public void assertExist() {

      // Check if there is at least one entry in the instance
      if ( !exists() ) {
         Assert.fail( "The table does not have the row for the specified primary key." );
      }
   }


   /**
    * Test the finance status code
    *
    * @param aExpected
    *           expected value
    */
   public void assertFinanceStatusCd( FinanceStatusCd aExpected ) {
      MxAssert.assertEquals( aExpected, getFinanceStatusCd() );
   }


   /**
    * Asserts that the <code>h_inv_no_db_id:h_inv_no_id</code> key and the argument are equal.
    *
    * @param aExpected
    *           Highest inventory to assert against actual data.
    */
   public void assertHInventory( InventoryKey aExpected ) {
      MxAssert.assertEquals( aExpected, getHInvNo() );
   }


   /**
    * Asserts that the value in the <code>icn_no_sdesc</code> column and the argument value are
    * equal.
    *
    * <ol>
    * <li>The call is delegated to {@link InvInv#assertStringField(String, String )
    * assertStringField()}.</li>
    * </ol>
    *
    * @param aExpected
    *           value to assert against actual value.
    */
   public void assertIcnNoSdesc( String aExpected ) {
      MxAssert.assertEquals( aExpected, getIcnNoSdesc() );
   }


   /**
    * Asserts that the value in the <code>install_dt</code>, <code>install_gdt</code> column and the
    * argument value are equal.
    *
    * <ol>
    * <li>The call is delegated to {@link InvInv#assertDateField(String,Date )
    * assertDateField()}.</li>
    * </ol>
    *
    * @param aExpected
    *           value to assert against actual value.
    */
   public void assertInstallDate( Date aExpected ) {
      MxAssert.assertEquals( aExpected, getInstallDt() );
   }


   /**
    * Asserts that the value in the <code>inv_no_sdesc</code> column and the argument value are
    * equal.
    *
    * <ol>
    * <li>The call is delegated to {@link InvInv#assertStringField(String, String )
    * assertStringField()}.</li>
    * </ol>
    *
    * @param aExpected
    *           value to assert against actual value.
    */
   public void assertInvNoSdesc( String aExpected ) {
      MxAssert.assertEquals( aExpected, getInvNoSdesc() );
   }


   /**
    * Asserts that the value in the <code>issued_bool</code> column and the argument value are
    * equal.
    *
    * @param aExpected
    *           the expected result
    */
   public void assertIssuedBoolean( boolean aExpected ) {
      MxAssert.assertEquals( aExpected, isIssuedBool() );
   }


   /**
    * Asserts that the <code>loc_db_id:loc_id</code> key and the argument are equal.
    *
    * @param aExpected
    *           Location to assert against actual data.
    */
   public void assertLocation( LocationKey aExpected ) {
      MxAssert.assertEquals( aExpected, getLocation() );
   }


   /**
    * Asserts that the value in the <code>locked_bool</code> column and the argument value are
    * equal.
    *
    * <ol>
    * <li>The call is delegated to {@link InvInv#assertBooleanField(String, boolean )
    * assertBooleanField()}.</li>
    * </ol>
    *
    * @param aExpected
    *           value to assert against actual value.
    */
   public void assertLockedBoolean( boolean aExpected ) {
      MxAssert.assertEquals( aExpected, isLocked() );
   }


   /**
    * Asserts that the value in the <code>manufact_dt</code> column and the argument value are
    * equal.
    *
    * <ol>
    * <li>The call is delegated to {@link InvInv#assertDateField(String,Date )
    * assertDateField()}.</li>
    * </ol>
    *
    * @param aExpected
    *           value to assert against actual value.
    */
   public void assertManufacturedDate( Date aExpected ) {
      MxAssert.assertEquals( aExpected, getManufactDt() );
   }


   /**
    * Asserts that the <code>mod_status_note</code> and the argument are equal.
    *
    * @param aExpected
    *           the modification status value to compare with the actual data
    */
   public void assertModificationStatus( String aExpected ) {
      MxAssert.assertEquals( aExpected, getModStatusNote() );
   }


   /**
    * Asserts that the <code>nh_inv_no_db_id:nh_inv_no_id</code> key and the argument are equal.
    *
    * @param aExpected
    *           Next highest inventory to assert against actual data.
    */
   public void assertNhInventory( InventoryKey aExpected ) {
      MxAssert.assertEquals( aExpected, getNhInvNo() );
   }


   /**
    * Asserts that the value in the <code>note</code> column and the argument value are equal.
    *
    * <ol>
    * <li>The call is delegated to {@link InvInv#assertStringField(String, String )
    * assertStringField()}.</li>
    * </ol>
    *
    * @param aExpected
    *           value to assert against actual value.
    */
   public void assertNote( String aExpected ) {
      MxAssert.assertEquals( aExpected, getNote() );
   }


   /**
    * Asserts that the table does not have a row corresponding to the current primary key.
    */
   public void assertNotExist() {

      // Check if there is at least one entry in the instance
      if ( exists() ) {
         Assert.fail( "The table has the row for the specified primary key." );
      }
   }


   /**
    * Asserts that the <code>not_found_bool</code> and the argument are equal.
    *
    * @param aExpected
    *           The expected value
    */
   public void assertNotFoundBool( boolean aExpected ) {
      MxAssert.assertEquals( aExpected, isNotFoundBool() );
   }


   /**
    * Asserts that the value in the <code>batch_no_oem</code> column and the argument value are
    * equal.
    *
    * <ol>
    * <li>The call is delegated to {@link InvInv#assertStringField(String, String )
    * assertStringField()}.</li>
    * </ol>
    *
    * @param aExpected
    *           value to assert against actual value.
    */
   public void assertOemBatchNumber( String aExpected ) {
      MxAssert.assertEquals( aExpected, getBatchNoOem() );
   }


   /**
    * Asserts that the value in the <code>lot_oem_tag</code> column and the argument value are
    * equal.
    *
    * <ol>
    * <li>The call is delegated to {@link InvInv#assertStringField(String, String )
    * assertStringField()}.</li>
    * </ol>
    *
    * @param aExpected
    *           value to assert against actual value.
    */
   public void assertOemLotNumber( String aExpected ) {
      MxAssert.assertEquals( aExpected, getLotOemTag() );
   }


   /**
    * Asserts that the value in the <code>serial_no_oem</code> column and the argument value are
    * equal.
    *
    * <ol>
    * <li>The call is delegated to {@link InvInv#assertStringField(String, String )
    * assertStringField()}.</li>
    * </ol>
    *
    * @param aExpected
    *           value to assert against actual value.
    */
   public void assertOemSerialNumber( String aExpected ) {
      MxAssert.assertEquals( aExpected, getSerialNoOem() );
   }


   /**
    * Asserts that the <code>orig_assmbl_db_id:orig_assmbl_cd</code> key and the argument are equal.
    *
    * @param aExpected
    *           Assembly to assert against actual data.
    */
   public void assertOriginalAssembly( AssemblyKey aExpected ) {
      MxAssert.assertEquals( aExpected, getOrigAssmbl() );
   }


   /**
    * Asserts that the <code>owner_db_id:owner_id</code> key and the argument are equal.
    *
    * @param aExpected
    *           Owner to assert against actual data.
    */
   public void assertOwner( OwnerKey aExpected ) {
      MxAssert.assertEquals( aExpected, getOwner() );
   }


   /**
    * Asserts that the <code>owner_type_cd</code> key and the argument are equal.
    *
    * @param aExpected
    *           OwnerType code to assert against the actual data.
    */
   public void assertOwnerTypeCd( String aExpected ) {
      MxAssert.assertEquals( aExpected, getOwnerType().getCd() );
   }


   /**
    * Asserts that the <code>part_no_db_id:part_no_id</code> key and the argument are equal.
    *
    * @param aExpected
    *           PartNo to assert against actual data.
    */
   public void assertPartNo( PartNoKey aExpected ) {
      MxAssert.assertEquals( aExpected, getPartNo() );
   }


   /**
    * Asserts that the <code>po_db_id:po_id:po_line_id</code> key and the argument are equal.
    *
    * @param aExpected
    *           Purchase order to assert against actual data.
    */
   public void assertPoLine( PurchaseOrderLineKey aExpected ) {
      MxAssert.assertEquals( aExpected, getPurchaseOrderLine() );
   }


   /**
    * Asserts that the value in the <code>prevent_synch_bool</code> column and the argument value
    * are equal.
    *
    * <ol>
    * <li>The call is delegated to {@link InvInv#assertBooleanField(String, boolean )
    * assertBooleanField()}.</li>
    * </ol>
    *
    * @param aExpected
    *           value to assert against actual value.
    */
   public void assertPreventSynchBoolean( boolean aExpected ) {
      MxAssert.assertEquals( aExpected, isPreventSynchBool() );
   }


   /**
    * Asserts that the value in the <code>po_ref_sdesc</code> column and the argument value are
    * equal.
    *
    * <ol>
    * <li>The call is delegated to {@link InvInv#assertStringField(String, String )
    * assertStringField()}.</li>
    * </ol>
    *
    * @param aExpected
    *           value to assert against actual value.
    */
   public void assertPurchaseOrder( String aExpected ) {
      MxAssert.assertEquals( aExpected, getPoRefSdesc() );
   }


   /**
    * Asserts that the <code>receive_cond_cd</code> key and the argument are equal.
    *
    * @param aExpected
    *           Condition code to assert against the actual data.
    */
   public void assertReceiveCondCd( String aExpected ) {
      MxAssert.assertEquals( aExpected, getReceiveCondCd() );
   }


   /**
    * Asserts that the value in the <code>received_dt</code> column and the argument value are
    * equal.
    *
    * <ol>
    * <li>The call is delegated to {@link InvInv#assertDateField(String,Date )
    * assertDateField()}.</li>
    * </ol>
    *
    * @param aExpected
    *           value to assert against actual value.
    */
   public void assertReceivedDate( Date aExpected ) {
      MxAssert.assertEquals( "RECEIVED_DT", aExpected, getReceivedDt() );
   }


   /**
    * Assert the Release Date is as expected
    *
    * @param aExpected
    *           The expected value
    */
   public void assertReleaseDate( Date aExpected ) {
      MxAssert.assertEquals( aExpected, getReleaseDate() );
   }


   /**
    * Assert the Release Number is as expected
    *
    * @param aExpected
    *           The expected value
    */
   public void assertReleaseNumber( String aExpected ) {
      MxAssert.assertEquals( aExpected, getReleaseNumber() );
   }


   /**
    * Assert the Release Remarks are as expected
    *
    * @param aExpected
    *           The expected value
    */
   public void assertReleaseRemarks( String aExpected ) {
      MxAssert.assertEquals( aExpected, getReleaseRemarks() );
   }


   /**
    * Asserts that the <code>reserved_bool</code> key and the argument are equal.
    *
    * @param aExpected
    *           value assigned to actual value
    */
   public void assertReservedBool( boolean aExpected ) {
      MxAssert.assertEquals( aExpected, isReservedBool() );
   }


   /**
    * Asserts that the value in the <code>reserve_qt</code> column and the argument value are equal.
    *
    * <ol>
    * <li>The call is delegated to {@link InvInv#assertDoubleField(String, Double )
    * assertDoubleField()}.</li>
    * </ol>
    *
    * @param aExpected
    *           value to assert against actual value.
    */
   public void assertReservedQuantity( Double aExpected ) {
      Assert.assertEquals( "Unexpected Reserved Quantity", aExpected, getReserveQt() );
   }


   /**
    * Asserts that the value in the <code>revision_dt</code> column and the argument value are
    * equal.
    *
    * <ol>
    * <li>The call is delegated to {@link InvInv#assertDateField(String,Date )
    * assertDateField()}.</li>
    * </ol>
    *
    * @param aExpected
    *           value to assert against actual value.
    */
   public void assertRevisionDate( Date aExpected ) {
      MxAssert.assertEquals( aExpected, getString( "revision_dt" ) );
   }


   /**
    * Asserts that the value in the <code>severity_cd</code> column and the argument value are
    * equal.
    *
    * <ol>
    * <li>The call is delegated to {@link InvInv#assertStringField(String, String )
    * assertStringField()}.</li>
    * </ol>
    *
    * @param aExpected
    *           value to assert against actual value.
    */
   public void assertSeverityCode( String aExpected ) {
      MxAssert.assertEquals( aExpected, getSeverityCd() );
   }


   /**
    * Asserts that the value in the <code>shelf_expiry_dt</code> column and the argument value are
    * equal.
    *
    * <ol>
    * <li>The call is delegated to {@link InvInv#assertDateField(String,Date )
    * assertDateField()}.</li>
    * </ol>
    *
    * @param aExpected
    *           value to assert against actual value.
    */
   public void assertShelfExpiryDate( Date aExpected ) {
      MxAssert.assertEquals( aExpected, getShelfExpiryDt() );
   }


   /**
    * Asserts that the value in the <code>used_bool</code> column and the argument value are equal.
    *
    * <ol>
    * <li>The call is delegated to {@link InvInv#assertBooleanField(String, boolean )
    * assertBooleanField()}.</li>
    * </ol>
    *
    * @param aExpected
    *           value to assert against actual value.
    */
   public void assertUsedBoolean( boolean aExpected ) {
      MxAssert.assertEquals( aExpected, isUsedBool() );
   }


   /**
    * Asserts that the <code>vendor_db_id:vendor_cd</code> key and the argument are equal.
    *
    * @param aExpected
    *           Vendor to assert against actual data.
    */
   public void assertVendor( VendorKey aExpected ) {
      MxAssert.assertEquals( aExpected, getVendor() );
   }


   public Date getRevisionDt() {
      return getDate( "revision_dt" );
   }
}
