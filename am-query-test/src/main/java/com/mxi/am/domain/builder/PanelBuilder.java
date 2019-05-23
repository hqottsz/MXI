
package com.mxi.am.domain.builder;

import java.util.concurrent.atomic.AtomicInteger;

import com.mxi.am.domain.Panel;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.Table;
import com.mxi.mx.core.key.PanelKey;
import com.mxi.mx.core.table.eqp.EqpTaskPanel;


/**
 * Builder for Panel domain object
 */
public class PanelBuilder {

   private static AtomicInteger iPanelId = new AtomicInteger( 1 );


   public static PanelKey build( Panel aPanel ) {

      if ( aPanel.getParentAssembly() == null )
         throw new RuntimeException( "Panel has to have a parent assembly" );

      PanelKey lPanelKey = new PanelKey( Table.Util.getDatabaseId(), iPanelId.getAndIncrement() );
      MxDataAccess.getInstance().executeInsert( "eqp_task_panel", lPanelKey.getPKWhereArg() );

      EqpTaskPanel lEqpTaskPanel = EqpTaskPanel.findByPrimaryKey( lPanelKey );
      lEqpTaskPanel.setDescSdesc( aPanel.getDescription() );
      lEqpTaskPanel.setPanelCd( aPanel.getCode() );
      lEqpTaskPanel.setAssembly( aPanel.getParentAssembly() );
      lEqpTaskPanel.update();
      return lPanelKey;

   }

}
