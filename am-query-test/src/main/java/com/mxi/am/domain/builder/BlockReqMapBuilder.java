
package com.mxi.am.domain.builder;

import com.mxi.mx.core.key.TaskBlockReqMapKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.task.TaskBlockReqMapTable;


/**
 * Builds a mapping between a block revision and a requirement definition.
 */
public class BlockReqMapBuilder implements DomainBuilder<TaskBlockReqMapKey> {

   private TaskTaskKey iBlockRev;
   private TaskDefnKey iReqDefn;


   /**
    * Builds the mapping.
    *
    * @return The key of the mapping.
    */
   @Override
   public TaskBlockReqMapKey build() {
      TaskBlockReqMapTable lBlockReqMap = TaskBlockReqMapTable.create( iBlockRev, iReqDefn );

      return lBlockReqMap.insert();
   }


   /**
    * Sets the block revision.
    *
    * @param aBlockRev
    *           The block revision
    *
    * @return The builder
    */
   public BlockReqMapBuilder from( TaskTaskKey aBlockRev ) {
      iBlockRev = aBlockRev;

      return this;
   }


   /**
    * Sets the requirement definition.
    *
    * @param aReqDefn
    *           The requirement definition.
    *
    * @return The builder.
    */
   public BlockReqMapBuilder to( TaskDefnKey aReqDefn ) {
      iReqDefn = aReqDefn;

      return this;
   }
}
