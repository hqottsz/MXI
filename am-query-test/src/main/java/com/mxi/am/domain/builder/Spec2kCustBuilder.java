package com.mxi.am.domain.builder;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.key.PrimaryKeyService;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.RefSpec2kCustKey;


/**
 * Builds a ref_spec2k_cust object
 *
 */
public class Spec2kCustBuilder implements DomainBuilder<RefSpec2kCustKey> {

   private final String iCode;
   private boolean iDefault = false;


   /**
    *
    * Creates a new {@linkplain Spec2kCustBuilder} object.
    *
    * @param aCode
    *           The spec2k customer code
    */
   public Spec2kCustBuilder(String aCode) {
      iCode = aCode;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public RefSpec2kCustKey build() {
      int lDbId = ( int ) PrimaryKeyService.getInstance().getDatabaseId();
      RefSpec2kCustKey lPrimaryKey = new RefSpec2kCustKey( lDbId, iCode );

      DataSetArgument lArgs = lPrimaryKey.getPKWhereArg();
      lArgs.add( "default_bool", iDefault );

      MxDataAccess.getInstance().executeInsert( "ref_spec2k_cust", lArgs );

      return lPrimaryKey;
   }


   /**
    *
    * Sets iDefault variable to true
    *
    * @return This object
    */
   public Spec2kCustBuilder isDefault() {
      iDefault = true;

      return this;
   }

}
