
package com.mxi.am.domain.builder;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.key.PrimaryKeyService;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.RefPoAuthFlowKey;
import com.mxi.mx.core.key.RefPoTypeKey;


/**
 * Builds a <code>ref_po_auth_flow</code> object
 */
public class OrderAuthorizationFlowBuilder implements DomainBuilder<RefPoAuthFlowKey> {

   private final String iCode;


   /**
    * Creates a new {@linkplain OrderAuthorizationFlowBuilder} object.
    *
    * @param aCode
    *           The currency code
    */
   public OrderAuthorizationFlowBuilder(String aCode) {
      iCode = aCode;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public RefPoAuthFlowKey build() {

      int lDbId = ( int ) PrimaryKeyService.getInstance().getDatabaseId();
      RefPoAuthFlowKey lPrimaryKey = new RefPoAuthFlowKey( lDbId, iCode );

      DataSetArgument lArgs = lPrimaryKey.getPKWhereArg();
      lArgs.add( RefPoTypeKey.PURCHASE, "PO_TYPE_DB_ID", "PO_TYPE_CD" );
      lArgs.add( "DEFAULT_BOOL", 1 );

      MxDataAccess.getInstance().executeInsert( "REF_PO_AUTH_FLOW", lArgs );

      return lPrimaryKey;
   }
}
