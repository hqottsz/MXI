package com.mxi.am.domain.builder;

import com.mxi.am.domain.StockDistReq;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.core.key.StockDistReqKey;
import com.mxi.mx.core.materials.stockdistribution.infra.StockDistReqDao;
import com.mxi.mx.core.materials.stockdistribution.infra.StockDistReqTableRow;


/**
 * The builder to create StockDistReq.
 *
 * @author Libin Cai
 * @since September 12, 2018
 */
public class StockDistReqBuilder {

   public static StockDistReqKey build( StockDistReq stockDistReqEntity ) {

      StockDistReqDao stockDistReqDao =
            InjectorContainer.get().getInstance( StockDistReqDao.class );

      StockDistReqKey pk = generatePrimaryKey();

      StockDistReqTableRow tableRow = stockDistReqDao.create( pk );

      tableRow.setRequestId( stockDistReqEntity.getRequestId() != null
            ? stockDistReqEntity.getRequestId() : pk.toString() );
      tableRow.setAssignedHr( stockDistReqEntity.getAssignedHr() );
      tableRow.setNeededLocation( stockDistReqEntity.getNeededLocation() );
      tableRow.setNeededQty( stockDistReqEntity.getNeededQuantity() );
      tableRow.setStatus( stockDistReqEntity.getStatus() );
      tableRow.setStockNo( stockDistReqEntity.getStockNo() );
      tableRow.setOwner( stockDistReqEntity.getOwner() );
      tableRow.setSupplierLocation( stockDistReqEntity.getSupplierLocation() );
      tableRow.setQtyUnit( stockDistReqEntity.getQtyUnit() );

      stockDistReqDao.insert( tableRow );

      return pk;
   }


   private static StockDistReqKey generatePrimaryKey() {

      StockDistReqDao dao = InjectorContainer.get().getInstance( StockDistReqDao.class );
      return dao.generatePrimaryKey();
   }

}
