
package com.mxi.am.domain.builder;

import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.EqpPartCompatTaskKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.table.eqp.EqpPartCompatTask;


/**
 * A builder for task incompatibilities. Table eqp_part_compat_task
 *
 * @author dbaxter
 */
public class TaskIncompatibilityBuilder implements DomainBuilder<EqpPartCompatTaskKey> {

   private PartGroupKey iBomPart;
   private PartNoKey iPart;
   private boolean iPreComplete = false;
   private TaskDefnKey iTask;


   /**
    * Builds the incompatibility. The key is a 3 part key comprised of BomPart, PartNo and TaskDefn.
    * You must call withTaskDefn(), withPartNo() and withBomPart() before calling this build
    * otherwise your key will not be complete.
    *
    * @return the incompatibility key
    */
   @Override
   public EqpPartCompatTaskKey build() {
      if ( ( iBomPart == null ) || ( iPart == null ) || ( iTask == null ) ) {
         throw new RuntimeException(
               "You must call withTaskDefn(), withPartNo() and withBomPart() before calling build()" );
      }

      EqpPartCompatTaskKey lCompatKey = new EqpPartCompatTaskKey( iBomPart, iPart, iTask );
      EqpPartCompatTask lIncompatibility = EqpPartCompatTask.create( lCompatKey );
      lIncompatibility.setPreCompleteBool( iPreComplete );
      lCompatKey = lIncompatibility.insert();

      return lCompatKey;
   }


   /**
    * Sets the pre_complete_bool column (aka incompatible with historic tasks)
    *
    * @param aPreComplete
    *           whether it is incompatible
    *
    * @return the builder
    */
   public TaskIncompatibilityBuilder isPreCompletion( boolean aPreComplete ) {
      iPreComplete = aPreComplete;

      return this;
   }


   /**
    * sets the bom part for the incompatibility key
    *
    * @param aBomPart
    *           the bom part key
    *
    * @return the builder
    */
   public TaskIncompatibilityBuilder withBomPart( PartGroupKey aBomPart ) {
      iBomPart = aBomPart;

      return this;
   }


   /**
    * sets the part no of the incompatibility key
    *
    * @param aPart
    *           the part no key
    *
    * @return the builder
    */
   public TaskIncompatibilityBuilder withPartNo( PartNoKey aPart ) {
      iPart = aPart;

      return this;
   }


   /**
    * sets the task definition of the incompatibility key
    *
    * @param aTask
    *           the task defn key
    *
    * @return the builder
    */
   public TaskIncompatibilityBuilder withTaskDefn( TaskDefnKey aTask ) {
      iTask = aTask;

      return this;
   }
}
