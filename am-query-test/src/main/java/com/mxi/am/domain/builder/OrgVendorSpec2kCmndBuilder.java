package com.mxi.am.domain.builder;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.RefSpec2kCmndKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.key.VendorSpec2kCmndKey;


/**
 * Builds a org_vendor_spec2k_cmnd object
 *
 */
public class OrgVendorSpec2kCmndBuilder implements DomainBuilder<VendorSpec2kCmndKey> {

   private final int iVendorCmndId;
   private final VendorKey iVendorKey;
   private final RefSpec2kCmndKey iRefSpec2kCmndKey;


   /**
    *
    * Creates a new {@linkplain OrgVendorSpec2kCmndBuilder} object.
    *
    * @param aVendorKey
    *           The Vendor key
    * @param aVendorCmndId
    *           The vendor command ID
    * @param aRefSpec2kCmndKey
    *           The spec2k command key
    */
   public OrgVendorSpec2kCmndBuilder(VendorKey aVendorKey, int aVendorCmndId,
         RefSpec2kCmndKey aRefSpec2kCmndKey) {
      iVendorKey = aVendorKey;
      iVendorCmndId = aVendorCmndId;
      iRefSpec2kCmndKey = aRefSpec2kCmndKey;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public VendorSpec2kCmndKey build() {
      VendorSpec2kCmndKey lPrimaryKey = new VendorSpec2kCmndKey( iVendorKey, iVendorCmndId );

      DataSetArgument lArgs = lPrimaryKey.getPKWhereArg();
      lArgs.add( iRefSpec2kCmndKey, "spec2k_cmnd_db_id", "spec2k_cmnd_cd" );

      MxDataAccess.getInstance().executeInsert( "org_vendor_spec2k_cmnd", lArgs );

      return lPrimaryKey;
   }

}
