
package com.mxi.am.domain.builder;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.EventKey;


/**
 * Assignment service for managing assigned tasks.
 *
 */
public class AssignmentService {

   /**
    *
    * Updates the highest event value for all the sub-events of the provided event.
    *
    * @param aRootEventKey
    * @param aHighestEventKey
    */
   public static void updateHighestEventOfSubEvents( EventKey aRootEventKey,
         EventKey aHighestEventKey ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aRootEventKey, "aRootEventDbId", "aRootEventId" );
      lArgs.add( aHighestEventKey, "aHighestEventDbId", "aHighestEventId" );
      MxDataAccess.getInstance()
            .executeUpdate( "com.mxi.am.domain.builder.UpdateHighestEventOfSubEvents", lArgs );

   }

}
