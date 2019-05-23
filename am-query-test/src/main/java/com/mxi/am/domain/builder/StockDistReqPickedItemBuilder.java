package com.mxi.am.domain.builder;

import com.mxi.am.domain.StockDistReqPickedItem;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.core.key.StockDistReqPickedItemKey;
import com.mxi.mx.core.materials.stockdistribution.infra.StockDistReqPickedItemDao;
import com.mxi.mx.core.materials.stockdistribution.infra.StockDistReqPickedItemTableRow;


/**
 * The builder to create StockDistReqPickedItem.
 *
 * @author Libin Cai
 * @since September 14, 2018
 */
public class StockDistReqPickedItemBuilder {

   public static StockDistReqPickedItemKey build( StockDistReqPickedItem entity ) {

      StockDistReqPickedItemDao dao =
            InjectorContainer.get().getInstance( StockDistReqPickedItemDao.class );

      StockDistReqPickedItemKey pk =
            new StockDistReqPickedItemKey( entity.getStockDistReq(), entity.getTransferKey() );

      StockDistReqPickedItemTableRow tableRow = dao.create( pk );

      dao.insert( tableRow );

      return pk;
   }

}
