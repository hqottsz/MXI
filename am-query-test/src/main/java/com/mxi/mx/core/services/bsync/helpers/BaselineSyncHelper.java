package com.mxi.mx.core.services.bsync.helpers;

import java.util.Collection;

import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.services.work.WorkItemGeneratorExecuteImmediateFake;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.services.bsync.BaselineSyncService;
import com.mxi.mx.core.services.bsync.DefaultZipper;
import com.mxi.mx.core.services.bsync.synchronization.logic.TaskSynchronizer;
import com.mxi.mx.core.services.bsync.synchronization.model.InventoryTask;
import com.mxi.mx.core.services.bsync.synchronization.model.InventoryTask.InventoryTaskBuilder;
import com.mxi.mx.core.services.bsync.zipper.logic.DefaultZipTaskProcessor;
import com.mxi.mx.core.services.bsync.zipper.logic.ZipQueueProcessorFake;
import com.mxi.mx.core.services.bsync.zipper.model.ZipQueue.ZipQueueBuilder;
import com.mxi.mx.core.services.bsync.zipper.model.ZipTask;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * Helper method for performing baseline synchronization for unit tests in a synchronous manner.
 *
 * Note; originally based on {@linkplain com.mxi.mx.core.helper.bsync.BaselineSyncHelper} in the
 * testingendejb project.
 *
 */
public class BaselineSyncHelper {

   private static BaselineSyncHelper sInstance;


   private BaselineSyncHelper() {
   }


   public static BaselineSyncHelper getInstance() {
      if ( sInstance == null ) {
         sInstance = new BaselineSyncHelper();
      }

      return sInstance;
   }


   public void synchronize( InventoryKey aInventory, HumanResourceKey lAuthHr ) throws Exception {
      synchronize( aInventory, lAuthHr, true );
   }


   public void synchronize( InventoryKey aInventory ) throws Exception {
      HumanResourceKey lAuthHr = new HumanResourceDomainBuilder().build();
      int lUserId = OrgHr.findByPrimaryKey( lAuthHr ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", new UserParametersFake( lUserId, "LOGIC" ) );

      synchronize( aInventory, lAuthHr, true );
   }


   public void synchronize( InventoryKey aInventory, HumanResourceKey aAuthHr,
         boolean aPerformZipping ) throws Exception {

      InvInvTable lInvInvTable = InvInvTable.findByPrimaryKey( aInventory );
      InventoryTask lInventoryTask = new InventoryTaskBuilder().inventory( aInventory )
            .hInventory( lInvInvTable.getHInvNo() ).build();

      TaskSynchronizer lSynchronizer = new TaskSynchronizer(
            new WorkItemGeneratorExecuteImmediateFake( aAuthHr ),
            GlobalParameters.getInstance( ParmTypeEnum.LOGIC ),
            GlobalParameters.getInstance( ParmTypeEnum.WORK_MANAGER ), lInventoryTask, aAuthHr );

      lSynchronizer.synchronizeTasks();

      // Request zip on full tree for inventory
      int lZipId = BaselineSyncService.getInstance().requestZipTree( aInventory );

      // perform zip
      if ( aPerformZipping ) {
         performZipping( lZipId, lInventoryTask.getHInventory(), aAuthHr );
      }
   }


   public void performZipping( int aZipId, HumanResourceKey aAuthHr ) throws Exception {
      InventoryKey lDummyInv = new InventoryKey( 0, 0 );
      performZipping( aZipId, lDummyInv, aAuthHr );
   }


   public void performZipping( int aZipId, InventoryKey aHighestInv, HumanResourceKey aAuthHr )
         throws Exception {

      ZipQueueBuilder lBuilder = new ZipQueueBuilder();
      lBuilder.id( aZipId ).hInventory( aHighestInv ).authorizingHr( aAuthHr );

      // perform zipping in a synchronous manner
      ZipQueueProcessorFake lZipQueueProcessor = new ZipQueueProcessorFake();
      DefaultZipper lZipper =
            new DefaultZipper( lZipQueueProcessor, new DefaultZipTaskProcessor() );
      lZipper.process( lBuilder.build() );

      Collection<ZipTask> lZipTasks = lZipQueueProcessor.getEnqueuedZipTasks();

      for ( ZipTask lZipTask : lZipTasks ) {
         lZipper.process( lZipTask );
      }
   }


   public static Integer getLatestZipId() {
      QuerySet lDs = QuerySetFactory.getInstance().executeQuery(
            new String[] { "MAX(zip_id) as zip_id" }, "ZIP_QUEUE", new DataSetArgument() );

      if ( lDs.next() ) {
         return lDs.getInt( "zip_id" );
      }

      return null;
   }

}
