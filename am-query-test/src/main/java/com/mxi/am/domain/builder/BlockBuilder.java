package com.mxi.am.domain.builder;

import com.mxi.am.domain.Block;
import com.mxi.am.domain.Deadline;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskTable;


public class BlockBuilder {

   static EvtEventDao iEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
   static SchedStaskDao iSchedStaskDao = InjectorContainer.get().getInstance( SchedStaskDao.class );


   public static TaskKey build( Block aBlock ) {
      TaskBuilder lTaskBuilder = new TaskBuilder();
      lTaskBuilder.withTaskClass( BlockDefinitionBuilder.BLOCK );
      lTaskBuilder.withTaskRevision( aBlock.getDefinition() );
      lTaskBuilder.withPrevTask( aBlock.getPreviousBlock() );

      if ( aBlock.getStatus() == null ) {
         aBlock.setStatus( RefEventStatusKey.ACTV );
      }
      lTaskBuilder.withStatus( aBlock.getStatus() );

      if ( aBlock.getInventory() == null ) {
         aBlock.setInventory( Domain.createAircraft() );
      }
      lTaskBuilder.onInventory( aBlock.getInventory() );

      if ( aBlock.getDefinition() == null ) {
         aBlock.setDefinition( Domain.createBlockDefinition() );
      }
      lTaskBuilder.withTaskRevision( aBlock.getDefinition() );

      TaskKey lBlockKey = lTaskBuilder.build();

      for ( TaskKey lReqKey : aBlock.getRequirementKeys() ) {
         addRequirementToBlock( lBlockKey, lReqKey );
      }

      for ( DomainConfiguration<Deadline> lDeadlineConfig : aBlock.getDeadlines() ) {
         Deadline lDeadline = new Deadline();
         lDeadlineConfig.configure( lDeadline );
         lDeadline.setTask( lBlockKey );
         DeadlineBuilder.build( lDeadline );
      }

      return lBlockKey;
   }


   private static void addRequirementToBlock( TaskKey aBlockKey, TaskKey aReqKey ) {
      EvtEventTable lEvtEvent = iEvtEventDao.findByPrimaryKey( aReqKey );
      lEvtEvent.setNhEvent( aBlockKey.getEventKey() );
      lEvtEvent.setHEvent( aBlockKey.getEventKey() );
      iEvtEventDao.update( lEvtEvent );

      SchedStaskTable lSchedStask = iSchedStaskDao.findByPrimaryKey( aReqKey );
      lSchedStask.setHTaskKey( aBlockKey );
      iSchedStaskDao.update( lSchedStask );
   }

}
