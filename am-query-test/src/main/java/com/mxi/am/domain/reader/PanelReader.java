package com.mxi.am.domain.reader;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.PanelKey;


/**
 * Reader for retrieving panel for a given assembly and panel code
 *
 */
public class PanelReader {

   public static PanelKey readPanelByCode( AssemblyKey aAssembly, String aPanelCode ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereIn( new String[] { "assmbl_db_id", "assmbl_cd" }, aAssembly );
      lArgs.addWhere( "panel_cd", aPanelCode );

      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "eqp_task_panel", lArgs );
      lQs.next();

      if ( lQs.getString( "panel_db_id" ) == null ) {
         throw new RuntimeException( String.format(
               "No panel exists with the pane code : %s for the provided assembly: %s", aPanelCode,
               aAssembly ) );
      }

      return new PanelKey( lQs.getInt( "panel_db_id" ), lQs.getInt( "panel_id" ) );

   }

}
