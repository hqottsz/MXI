
package com.mxi.am.domain.builder;

import com.mxi.mx.core.key.SchedLabourInstPartKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.TaskInstPartKey;
import com.mxi.mx.core.table.sched.SchedLabourInstPartTable;


/**
 * This builder allows developers to build labour for tasks.
 */
public class ScheduledInstallLabourBuilder implements DomainBuilder<SchedLabourInstPartKey> {

   private SchedLabourKey iLabour;
   private TaskInstPartKey iPartRequirement;


   /**
    * {@inheritDoc}
    */
   @Override
   public SchedLabourInstPartKey build() {

      SchedLabourInstPartKey lKey = new SchedLabourInstPartKey( iLabour, iPartRequirement );

      SchedLabourInstPartTable lSchedLabourInstPartTable = SchedLabourInstPartTable.create( lKey );

      return lSchedLabourInstPartTable.insert();
   }


   /**
    * Sets the labour key
    *
    * @param aLabour
    *           sched_labour key
    *
    * @return builder
    */
   public ScheduledInstallLabourBuilder forLabour( SchedLabourKey aLabour ) {
      iLabour = aLabour;

      return this;
   }


   /**
    * Sets the part requirement
    *
    * @param aPartRequirement
    *           part requirement key
    *
    * @return builder
    */
   public ScheduledInstallLabourBuilder forPartRequirement( TaskInstPartKey aPartRequirement ) {
      iPartRequirement = aPartRequirement;

      return this;
   }
}
