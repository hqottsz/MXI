
package com.mxi.mx.common.services.work;

import java.util.Collection;
import java.util.Date;

import com.mxi.mx.common.ejb.sequence.SequenceGeneratorLocal;
import com.mxi.mx.common.key.DatabaseIdAccessor;


/**
 * Stub for {@link WorkItemGeneration} that does nothing
 */
public class WorkItemGeneratorStub implements WorkItemGeneration {

   /**
    * {@inheritDoc}
    */
   @Override
   public void generateWorkItem( WorkItem aWorkItem ) {
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void generateWorkItem( WorkItem aWorkItem, DuplicateCheckType aCheckForDuplicate ) {
   }

   @Override
   public void generateWorkItem(WorkItem aWorkItem, DuplicateCheckAttributes aDuplicateCheckAttributes, DuplicateCheckType aCheckForDuplicate) {

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void generateWorkItem( WorkItem aWorkItem, DuplicateCheckType aCheckForDuplicate,
         Date aScheduleDate ) {
   }

   @Override
   public void generateWorkItem(WorkItem aWorkItem, DuplicateCheckAttributes aDuplicateCheckAttributes, DuplicateCheckType aCheckForDuplicate, Date aScheduleDate) {

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void generateWorkItems( Collection<? extends WorkItem> aWorkItems ) {
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void generateWorkItems( Collection<? extends WorkItem> aWorkItems,
         DuplicateCheckType aCheckForDuplicates ) {
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void generateWorkItems( Collection<? extends WorkItem> aWorkItems,
         DuplicateCheckType aCheckForDuplicates, Date aScheduleDate ) {
   }

   @Override
   public void generateWorkItems(Collection<? extends WorkItem> aWorkItems, DuplicateCheckAttributes aDuplicateCheckAttributes, DuplicateCheckType aCheckForDuplicates, Date aScheduleDate) {

   }

   @Override
   public void generateWorkItems(Collection<? extends WorkItem> aWorkItems, DuplicateCheckAttributes aDuplicateCheckAttributes, DuplicateCheckType aCheckForDuplicates) {

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isWorkItemTypeEnabled( String aWorkItemType ) {
      return false;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void setDatabaseIdAccessor( DatabaseIdAccessor aDatabaseIdAccessor ) {
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void setSequenceGenerator( SequenceGeneratorLocal aSequenceGenerator ) {
   }
}
