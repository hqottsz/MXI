
package com.mxi.mx.core.services.bsync.zipper.logic;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;

import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.bsync.zipper.model.BlockStrand;
import com.mxi.mx.core.services.bsync.zipper.model.RequirementStrand;
import com.mxi.mx.core.services.bsync.zipper.model.ZipBlock;
import com.mxi.mx.core.services.bsync.zipper.model.ZipReq;
import com.mxi.mx.core.services.bsync.zipper.model.ZipTask;
import com.mxi.mx.core.services.bsync.zipper.model.ZipTask.ZipTaskBuilder;


/**
 * Fakes a {@linkplain ZipQueueProcessor}
 *
 */
public class ZipQueueProcessorFake implements ZipQueueProcessor {

   private ZipQueueProcessor iProcessor;

   // In memory collection of tasks, used instead of queuing the zip tasks.
   private Collection<ZipTask> iTasks;


   /**
    * Creates a new {@linkplain ZipQueueProcessorFake} object.
    *
    */
   public ZipQueueProcessorFake() {
      iProcessor = new DefaultZipQueueProcessor();
      iTasks = Collections.<ZipTask>emptyList();
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void clearZipTableData( int aZipId ) {
      iProcessor.clearZipTableData( aZipId );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Map<String, BlockStrand> composeBlockStrands( Map<TaskKey, ZipBlock> aZipBlocks ) {
      return iProcessor.composeBlockStrands( aZipBlocks );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Map<String, RequirementStrand> composeReqStrands( Map<TaskKey, ZipReq> aZipReqs ) {
      return iProcessor.composeReqStrands( aZipReqs );
   }


   /**
    * {@inheritDoc}
    *
    * Create {@link ZipTask} for all the requirements that need to be zipped. Store the
    * {@link ZipTask} instead of queuing them.
    */
   @Override
   public void enqueueZipTasks( Collection<ZipReq> aZipTasks, HumanResourceKey aHr ) {
      iTasks = new LinkedList<ZipTask>();

      for ( ZipReq lRequirement : aZipTasks ) {
         TaskKey lOldParent = lRequirement.getCurrentParent();
         TaskKey lNewParent = lRequirement.getWorkPackage();

         ZipTaskBuilder lBuilder = new ZipTaskBuilder();

         // If the Requirement has a Block to be assigned to, use that as the parent instead
         if ( lRequirement.isAssigned() ) {
            ZipBlock lParentBlock = lRequirement.getAssignedBlock();
            lNewParent = lParentBlock.getTaskKey();
         }

         if ( ( lOldParent == null && lNewParent == null ) || ( lOldParent != null
               && lNewParent != null && lOldParent.equals( lNewParent ) ) ) {
            // No operation
            continue;
         }

         iTasks.add( lBuilder.task( lRequirement.getTaskKey() ).parent( lOldParent )
               .newParent( lNewParent ).authorizingHr( aHr ).build() );
      }
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Map<TaskKey, ZipBlock> generateZipBlocks( int aZipId ) {
      return iProcessor.generateZipBlocks( aZipId );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Map<TaskKey, ZipReq> generateZipRequirements( int aZipId ) {
      return iProcessor.generateZipRequirements( aZipId );
   }


   /**
    * Return the collection of {@link ZipTask} that are stored in memory as opposed to being queued.
    *
    * @return
    */
   public Collection<ZipTask> getEnqueuedZipTasks() {
      return iTasks;
   }

}
