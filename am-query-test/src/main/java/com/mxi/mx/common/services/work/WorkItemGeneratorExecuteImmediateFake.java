package com.mxi.mx.common.services.work;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.ejb.EjbFactory;
import com.mxi.mx.common.ejb.sequence.SequenceGeneratorLocal;
import com.mxi.mx.common.key.DatabaseIdAccessor;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.plsql.StoredProcedureCall;
import com.mxi.mx.core.services.bsync.helpers.BaselineSyncHelper;
import com.mxi.mx.core.services.bsync.synchronization.logic.TaskSynchronizer;
import com.mxi.mx.core.services.bsync.synchronization.logic.cancel.TaskCanceller;
import com.mxi.mx.core.services.bsync.synchronization.logic.initialize.TaskInitializer;
import com.mxi.mx.core.services.bsync.synchronization.logic.update.TaskUpdater;
import com.mxi.mx.core.services.bsync.synchronization.model.Inventory;
import com.mxi.mx.core.services.bsync.synchronization.model.InventoryTask.InventoryTaskFromDataBuilder;
import com.mxi.mx.core.services.bsync.synchronization.model.cancel.CancelTask;
import com.mxi.mx.core.services.bsync.synchronization.model.cancel.CancelTask.CancelTaskFromDataBuilder;
import com.mxi.mx.core.services.bsync.synchronization.model.initialize.InitializeTask;
import com.mxi.mx.core.services.bsync.synchronization.model.initialize.InitializeTask.InitializeTaskFromDataBuilder;
import com.mxi.mx.core.services.bsync.synchronization.model.initialize.ValidateTaskActuals;
import com.mxi.mx.core.services.bsync.synchronization.model.update.UpdateTask;
import com.mxi.mx.core.services.bsync.zipper.model.ZipQueue;
import com.mxi.mx.core.services.bsync.zipper.model.ZipQueue.ZipQueueFromDataBuilder;
import com.mxi.mx.core.services.dataservices.BulkLoadDataWorkItem;
import com.mxi.mx.core.services.inventory.InventoryService;
import com.mxi.mx.core.services.inventory.UpdateIETMWorkItem;
import com.mxi.mx.core.services.stask.deadline.updatedeadline.model.AircraftDeadline;
import com.mxi.mx.core.services.stask.deadline.updatedeadline.model.TaskDeadline;
import com.mxi.mx.core.services.stocklevel.StockLevelCheckWorkItem;
import com.mxi.mx.core.services.stocklevel.WarehouseStockLevelCheckWorkItem;


/**
 *
 * Fake {@linkplain WorkItemGenerator}.
 *
 * The production {@linkplain WorkItemGenerator} generates rows in the utl_work_item table. Then a
 * work manager reads that table and spawns workers to perform the work.
 *
 * This class will fake that process and directly call the logic that the workers perform for each
 * work item type.
 *
 */
public class WorkItemGeneratorExecuteImmediateFake implements WorkItemGeneration {

   static final HumanResourceKey ADMIN_USER = new HumanResourceKey( 0, 3 );
   private HumanResourceKey iAuthorizingHr;


   /**
    * Creates a new {@linkplain WorkItemGeneratorExecuteImmediateFake} object using the provided HR
    * key as the authorizing HR for work items generated when synchronizing tasks. If no HR key is
    * provided then the admin user is used.
    *
    * @param aAuthorizingHr
    *           authorizing HR
    */
   public WorkItemGeneratorExecuteImmediateFake(HumanResourceKey aAuthorizingHr) {
      iAuthorizingHr = ( aAuthorizingHr != null ) ? aAuthorizingHr : ADMIN_USER;
   }


   /**
    * Creates a new {@linkplain WorkItemGeneratorExecuteImmediateFake} object using the default
    * admin user as the authorizing HR for work items generated when synchronizing tasks.
    *
    */
   public WorkItemGeneratorExecuteImmediateFake() {
      this( null );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void generateWorkItems( Collection<? extends WorkItem> aWorkItems ) {
      generateWorkItems( aWorkItems, null );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void generateWorkItems( Collection<? extends WorkItem> aWorkItems,
         DuplicateCheckType aCheckForDuplicates ) {
      generateWorkItems( aWorkItems, aCheckForDuplicates, null );
   }


   /**
    * {@inheritDoc}
    *
    * This fake method will process the work item directly.
    *
    * In production this method generates the work item rows in the utl_work_item table, after which
    * the application server manages workers that read and process those work items. This fake
    * allows for the processing without having an application server.
    */
   @Override
   public void generateWorkItems( Collection<? extends WorkItem> aWorkItems,
         DuplicateCheckAttributes aDuplicateCheckAttributes, DuplicateCheckType aCheckForDuplicates,
         Date aScheduleDate ) {

      for ( WorkItem lWorkItem : aWorkItems ) {

         try {
            // Check if the work item is a baseline sync work item.
            com.mxi.mx.core.services.bsync.synchronization.model.WorkType lBsyncWorkType =
                  com.mxi.mx.core.services.bsync.synchronization.model.WorkType
                        .valueOf( lWorkItem.getType() );

            switch ( lBsyncWorkType ) {
               case INVENTORY_SYNC:
                  Inventory lInventory = new InventoryTaskFromDataBuilder()
                        .data( lWorkItem.getData() ).key( lWorkItem.getKey() ).build();
                  new TaskSynchronizer( lInventory, iAuthorizingHr ).synchronizeTasks();
                  break;

               case INITIALIZE_TASK:
                  InitializeTask lInitializeTask = new InitializeTaskFromDataBuilder()
                        .data( lWorkItem.getData() ).key( lWorkItem.getKey() ).build();
                  try {
                     new TaskInitializer().processTask( lInitializeTask );
                  } catch ( Exception e ) {
                     throw new RuntimeException( e );
                  }
                  break;

               case UPDATE_TASK:
                  TaskUpdater lTaskUpdater = TaskUpdater.getInstance();
                  UpdateTask lTask =
                        lTaskUpdater.getTask( lWorkItem.getData(), lWorkItem.getKey() );
                  try {
                     lTaskUpdater.processTask( lTask );
                  } catch ( Exception e ) {
                     throw new RuntimeException( e );
                  }
                  break;

               case CANCEL_TASK:
                  CancelTask lCancelTask = new CancelTaskFromDataBuilder()
                        .data( lWorkItem.getData() ).key( lWorkItem.getKey() ).build();
                  try {
                     new TaskCanceller().processTask( lCancelTask );
                  } catch ( Exception e ) {
                     throw new RuntimeException( e );
                  }
                  break;

               case ZIP_QUEUE:
                  ZipQueueFromDataBuilder lBuilder = new ZipQueueFromDataBuilder();
                  ZipQueue lZipQueue =
                        lBuilder.data( lWorkItem.getData() ).key( lWorkItem.getKey() ).build();

                  try {
                     BaselineSyncHelper.getInstance().performZipping( lZipQueue.getId(),
                           lZipQueue.getHInventory(), lZipQueue.getHr() );
                  } catch ( Exception e ) {
                     throw new RuntimeException( e );
                  }
                  break;

               case VALIDATE_TASK_ACTUALS:
                  ValidateTaskActuals lValidateTaskActuals =
                        new ValidateTaskActuals.ValidateTaskActualsFromDataBuilder()
                              .data( lWorkItem.getData() ).key( lWorkItem.getKey() ).build();
                  try {
                     InitializeTask lNewTask = lValidateTaskActuals.getInitializeTask();

                     if ( lNewTask != null ) {
                        Collection<WorkItem> lWorkItemList = new ArrayList<WorkItem>( 1 );
                        lWorkItemList.add( lNewTask );
                        // Send item back into this method for immediate execution
                        generateWorkItems( lWorkItemList );
                     }
                  } catch ( Exception e ) {
                     throw new RuntimeException( e );
                  }
                  break;
               default:
                  // All other bsync work types are currently not supported. Please add as required.

            }
         } catch ( IllegalArgumentException lBsyncEx ) {
            // This exception means the work type is not a baseline sync work item.

            try {
               // Check if the work item is a deadline work item.
               com.mxi.mx.core.services.stask.deadline.updatedeadline.model.WorkTypes lDeadlineWorkType =
                     com.mxi.mx.core.services.stask.deadline.updatedeadline.model.WorkTypes
                           .valueOf( lWorkItem.getType() );

               switch ( lDeadlineWorkType ) {
                  case JOB_AIRCRAFT_DEADLINE_UPDATE:
                     AircraftDeadline lAircraftDeadline =
                           new AircraftDeadline.Builder( lWorkItem.getKey(), lWorkItem.getType(),
                                 lWorkItem.getData() ).build();
                     try {
                        StoredProcedureCall.getInstance()
                              .updateDeadlinesForAircraft( lAircraftDeadline.getAircraft() );
                     } catch ( Exception e ) {
                        throw new RuntimeException( e );
                     }
                     break;

                  case TASK_TREE_DEADLINE_UPDATE:
                     TaskDeadline lTaskDeadline = new TaskDeadline.Builder( lWorkItem.getKey(),
                           lWorkItem.getType(), lWorkItem.getData() ).build();
                     try {
                        StoredProcedureCall.getInstance()
                              .updateDeadlinesOnDependentTasksTree( lTaskDeadline.getTaskKey() );
                     } catch ( Exception e ) {
                        throw new RuntimeException( e );
                     }
                     break;

                  default:
                     // All other deadline work types are currently not supported. Please add as
                     // required.
               }

            } catch ( IllegalArgumentException lDeadEx ) {
               // This exception means the work type is not a deadline work type, either. Please add
               // as required.
               if ( UpdateIETMWorkItem.WORK_TYPE.equals( lWorkItem.getType() ) ) {
                  UpdateIETMWorkItem lUpdateIETMWorkItem =
                        new UpdateIETMWorkItem.Builder( lWorkItem.getKey(), lWorkItem.getData() )
                              .build();
                  try {
                     InventoryService.updateIETM( lUpdateIETMWorkItem.getInventory() );
                  } catch ( Exception e ) {
                     throw new RuntimeException( e );
                  }

               } else if ( WarehouseStockLevelCheckWorkItem.WORK_TYPE
                     .equals( lWorkItem.getType() ) ) {

                  try {

                     DataSetArgument lArgs = new DataSetArgument();
                     lArgs.add( "id", EjbFactory.getInstance().createSequenceGenerator()
                           .nextValue( "UTL_WORK_ITEM_ID" ) );
                     lArgs.add( "type", lWorkItem.getType() );
                     lArgs.add( "key", lWorkItem.getKey().toString() );
                     lArgs.add( "data", lWorkItem.getData() );

                     MxDataAccess.getInstance().executeInsert( "utl_work_item", lArgs );
                  } catch ( Exception e ) {
                     throw new RuntimeException( e );
                  }
               } else if ( com.mxi.mx.core.services.print.model.WorkType.PRINT_ITEM.name()
                     .equals( lWorkItem.getType() )
                     || com.mxi.mx.core.services.print.model.WorkType.PRINT_JOB.name()
                           .equals( lWorkItem.getType() ) ) {
                  // For Print jobs or items... just insert a work item row like the real
                  // implementation does...
                  try {

                     DataSetArgument lArgs = new DataSetArgument();
                     lArgs.add( "id", EjbFactory.getInstance().createSequenceGenerator()
                           .nextValue( "UTL_WORK_ITEM_ID" ) );
                     lArgs.add( "type", lWorkItem.getType() );
                     lArgs.add( "key", lWorkItem.getKey().toString() );
                     lArgs.add( "data", lWorkItem.getData() );

                     MxDataAccess.getInstance().executeInsert( "utl_work_item", lArgs );
                  } catch ( Exception e ) {
                     throw new RuntimeException( e );
                  }

               } else if ( BulkLoadDataWorkItem.WORK_TYPE.equals( lWorkItem.getType() ) ) {
                  try {

                     DataSetArgument lArgs = new DataSetArgument();
                     lArgs.add( "id", EjbFactory.getInstance().createSequenceGenerator()
                           .nextValue( "UTL_WORK_ITEM_ID" ) );
                     lArgs.add( "type", lWorkItem.getType() );
                     lArgs.add( "key", lWorkItem.getKey().toString() );
                     lArgs.add( "data", lWorkItem.getData() );

                     MxDataAccess.getInstance().executeInsert( "utl_work_item", lArgs );
                  } catch ( Exception e ) {
                     throw new RuntimeException( e );
                  }
               } else if ( StockLevelCheckWorkItem.WORK_TYPE.equals( lWorkItem.getType() ) ) {

                  try {

                     DataSetArgument lArgs = new DataSetArgument();
                     lArgs.add( "id", EjbFactory.getInstance().createSequenceGenerator()
                           .nextValue( "UTL_WORK_ITEM_ID" ) );
                     lArgs.add( "type", lWorkItem.getType() );
                     lArgs.add( "key", lWorkItem.getKey().toString() );
                     lArgs.add( "data", lWorkItem.getData() );

                     MxDataAccess.getInstance().executeInsert( "utl_work_item", lArgs );
                  } catch ( Exception e ) {
                     throw new RuntimeException( e );
                  }
               }
            }
         }

      }

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void generateWorkItems( Collection<? extends WorkItem> aWorkItems,
         DuplicateCheckType aCheckForDuplicates, Date aScheduleDate ) {
      generateWorkItems( aWorkItems, DuplicateCheckAttributes.KEY_DATA, aCheckForDuplicates,
            aScheduleDate );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void generateWorkItems( Collection<? extends WorkItem> aWorkItems,
         DuplicateCheckAttributes aDuplicateCheckAttributes,
         DuplicateCheckType aCheckForDuplicates ) {
      generateWorkItems( aWorkItems, aDuplicateCheckAttributes, aCheckForDuplicates, null );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void generateWorkItem( WorkItem aWorkItem ) {
      generateWorkItem( aWorkItem, DuplicateCheckType.ALLOW );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void generateWorkItem( WorkItem aWorkItem, DuplicateCheckType aCheckForDuplicate ) {
      generateWorkItem( aWorkItem, aCheckForDuplicate, null );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void generateWorkItem( WorkItem aWorkItem,
         DuplicateCheckAttributes aDuplicateCheckAttributes,
         DuplicateCheckType aCheckForDuplicate ) {
      generateWorkItem( aWorkItem, aDuplicateCheckAttributes, aCheckForDuplicate, null );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void generateWorkItem( WorkItem aWorkItem,
         DuplicateCheckAttributes aDuplicateCheckAttributes, DuplicateCheckType aCheckForDuplicate,
         Date aScheduleDate ) {
      Collection<WorkItem> lWorkItemList = new ArrayList<WorkItem>( 1 );
      lWorkItemList.add( aWorkItem );

      generateWorkItems( lWorkItemList, aDuplicateCheckAttributes, aCheckForDuplicate,
            aScheduleDate );
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
      // do nothing
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void setSequenceGenerator( SequenceGeneratorLocal aSequenceGenerator ) {
      // do nothing
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void generateWorkItem( WorkItem aWorkItem, DuplicateCheckType aCheckForDuplicate,
         Date aScheduleDate ) {
      generateWorkItem( aWorkItem, DuplicateCheckAttributes.KEY_DATA, aCheckForDuplicate,
            aScheduleDate );

   }

}
