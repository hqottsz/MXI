
package com.mxi.am.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.mxi.mx.core.key.AuthorityKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefOwnerTypeKey;
import com.mxi.mx.core.table.inv.InvInvTable;


public class SerializedInventory {

   private String iSerialNumber;
   private PartNoKey iPartNumber;
   private LocationKey iLocation;
   private RefInvCondKey iCondition;
   private OwnerKey iOwner;
   private boolean iComplete;
   private InventoryKey iParent;
   private RefOwnerTypeKey iOwnershipType;
   private PurchaseOrderLineKey iOrderLineKey;
   private String iInvNoSdesc;
   private String iBarcode;
   private boolean iLocked;

   private AuthorityKey iAuthorityKey;
   private Date iReceivedDate;
   private InvInvTable.FinanceStatusCd iFinanceStatusCd;

   private Map<DataTypeKey, BigDecimal> iUsage = new HashMap<>();


   public Map<DataTypeKey, BigDecimal> getUsage() {
      return iUsage;
   }


   public void addUsage( DataTypeKey aType, BigDecimal aUsage ) {
      iUsage.put( aType, aUsage );
   }


   public void setOrderLine( PurchaseOrderLineKey aOrderLineKey ) {
      iOrderLineKey = aOrderLineKey;
   }


   public PurchaseOrderLineKey getOrderLine() {
      return iOrderLineKey;
   }


   public void setOwner( OwnerKey aOwner ) {
      iOwner = aOwner;
   }


   public OwnerKey getOwner() {
      return iOwner;
   }


   public void setOwnershipType( RefOwnerTypeKey aOwnershipType ) {
      iOwnershipType = aOwnershipType;
   }


   public RefOwnerTypeKey getOwnershipType() {
      return iOwnershipType;
   }


   public void setCondition( RefInvCondKey aCondition ) {
      iCondition = aCondition;
   }


   public RefInvCondKey getCondition() {
      return iCondition;
   }


   public String getSerialNumber() {
      return iSerialNumber;
   }


   public void setSerialNumber( String aSerialNoOem ) {
      iSerialNumber = aSerialNoOem;
   }


   public void setPartNumber( PartNoKey aPartNumber ) {
      iPartNumber = aPartNumber;
   }


   public PartNoKey getPartNumber() {
      return iPartNumber;
   }


   public void setLocation( LocationKey aLocation ) {
      iLocation = aLocation;
   }


   public LocationKey getLocation() {
      return iLocation;
   }


   public void setComplete( boolean aInvComplete ) {
      iComplete = aInvComplete;
   }


   public boolean getComplete() {
      return iComplete;
   }


   /**
    * Returns the value of the iBarcode property.
    *
    * @return the value of the iBarcode property
    */
   public String getBarcode() {
      return iBarcode;
   }


   /**
    * Sets a new value for the iBarcode property.
    *
    * @param iBarcode
    *           the new value for the iBarcode property
    */
   public void setBarcode( String iBarcode ) {
      this.iBarcode = iBarcode;
   }


   public void setParent( InventoryKey aParent ) {
      iParent = aParent;
   }


   public InventoryKey getParent() {
      return iParent;
   }


   public String getInvNoSdesc() {
      return iInvNoSdesc;
   }


   public void setInvNoSdesc( String aInvNoSdesc ) {
      iInvNoSdesc = aInvNoSdesc;
   }


   public AuthorityKey getAuthorityKey() {
      return iAuthorityKey;
   }


   public void setLockedBoolean( boolean aLockedBoolean ) {
      iLocked = aLockedBoolean;
   }


   public boolean getLockedBoolean() {
      return iLocked;
   }


   public void setAuthorityKey( AuthorityKey iAuthorityKey ) {
      this.iAuthorityKey = iAuthorityKey;
   }


   public void setReceivedDate( Date aReceivedDate ) {
      iReceivedDate = aReceivedDate;
   }


   public void setFinanceStatusCd( InvInvTable.FinanceStatusCd aFinanceStatusCd ) {
      iFinanceStatusCd = aFinanceStatusCd;
   }


   public InvInvTable.FinanceStatusCd getFinanceStatusCd() {
      return iFinanceStatusCd;
   }


   public Date getReceivedDate() {
      return iReceivedDate;
   }
}
