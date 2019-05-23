package com.mxi.am.domain.builder;

import static com.mxi.mx.core.key.RefInvClassKey.ACFT;
import static com.mxi.mx.core.key.RefInvClassKey.ASSY;

import java.util.Date;
import java.util.Set;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.services.event.inventory.EventInventoryService;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.inv.InvInvTable;


/**
 * Attachment service for managing attached inventory.
 *
 */
public class AttachmentService {

   /**
    *
    * Updates the highest inventory value for all the sub-inventory of the provided root inventory.
    *
    * @param aRootInvKey
    * @param aHighestInvKey
    */
   public static void updateHighestInventoryOfSubInventory( InventoryKey aRootInvKey,
         InventoryKey aHighestInvKey ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aRootInvKey, "aRootInvNoDbId", "aRootInvNoId" );
      lArgs.add( aHighestInvKey, "aHighestInvNoDbId", "aHighestInvNoId" );
      MxDataAccess.getInstance().executeUpdate(
            "com.mxi.am.domain.builder.UpdateHighestInventoryOfSubInventory", lArgs );

   }


   /**
    *
    * Updates the highest inventory value and assembly inventory value for all the sub-inventory of
    * the provided root inventory.
    *
    * @param aRootInvKey
    * @param aHighestInvKey
    * @param aAssemblyInvKey
    */
   public static void updateHighestAndAssemblyInventoryOfSubInventory( InventoryKey aRootInvKey,
         InventoryKey aHighestInvKey, InventoryKey aAssemblyInvKey ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aRootInvKey, "aRootInvNoDbId", "aRootInvNoId" );
      lArgs.add( aHighestInvKey, "aHighestInvNoDbId", "aHighestInvNoId" );
      lArgs.add( aAssemblyInvKey, "aAssyInvNoDbId", "aAssyInvNoId" );
      MxDataAccess.getInstance().executeUpdate(
            "com.mxi.am.domain.builder.UpdateHighestAndAssemblyInventoryOfSubInventory", lArgs );

   }


   public static EventKey attachInventoryToAircraft( InventoryKey aAircraftInvKey,
         InventoryKey aInvKey, Date aAttachDate, Set<UsageSnapshot> aUsages ) {
      return attachInventoryToAircraft( aAircraftInvKey, aInvKey, null, aAttachDate, aUsages );
   }


   public static EventKey attachInventoryToAircraft( InventoryKey aAircraftInvKey,
         InventoryKey aInvKey, EventKey aParentEvent, Date aAttachDate, Set<UsageSnapshot> aUsages ) {

      EventKey lEventKey =
            new EventBuilder().onInventory( aInvKey ).withType( RefEventTypeKey.FG )
                  .withParentEvent( aParentEvent ).withActualEndDate( aAttachDate )
                  .withStatus( RefEventStatusKey.FGINST ).build();

      EvtInvTable lEvtInstallEngine = EvtInvTable.findMainByEventKey( lEventKey );
      lEvtInstallEngine.setNHInventoryKey( aAircraftInvKey );
      lEvtInstallEngine.setHInventoryKey( aAircraftInvKey );
      lEvtInstallEngine.setAssmblInventoryKey( aAircraftInvKey );
      lEvtInstallEngine.update();

      InvInvTable lInvInvTable = InvInvTable.findByPrimaryKey( aInvKey );

      // if a trk is attached to aircraft, it and its sub-inv shall all set highest and assembly to
      // the aircraft.
      // if a sub-assembly is attached to aircraft, it shall set highest and assembly to the
      // aircraft. But the sub-inv of sub-assembly still set assembly to the attached sub-assembly,
      // only highest shall be set to aircraft
      boolean lIsSubAssembly = false;
      if ( RefInvClassKey.ASSY.equals( lInvInvTable.getInvClass() ) ) {
         lIsSubAssembly = true;
      }
      lInvInvTable.setNhInvNo( aAircraftInvKey );
      lInvInvTable.setAssmblInvNo( aAircraftInvKey );
      lInvInvTable.setHInvNo( aAircraftInvKey );
      lInvInvTable.update();

      if ( lIsSubAssembly ) {
         updateHighestInventoryOfSubInventory( aInvKey, aAircraftInvKey );
      } else {
         updateHighestAndAssemblyInventoryOfSubInventory( aInvKey, aAircraftInvKey, aAircraftInvKey );
      }

      if ( aUsages != null && !aUsages.isEmpty() ) {
         EventInventoryService lEventInventoryService = new EventInventoryService( lEventKey );

         for ( UsageSnapshot lUsage : aUsages ) {
            lEventInventoryService.addUsageSnapshot( lUsage, null );
         }

      }

      // create inv_install record
      DataSetArgument lArg = new DataSetArgument();
      lArg.add( lEventKey, "aEventDbId", "aEventId" );
      lArg.add( aInvKey, "aInvNoDbId", "aInvNoId" );
      lArg.add( "aEventDate", aAttachDate );
      MxDataAccess.getInstance().executeUpdate(
            "com.mxi.mx.core.query.event.CreateInventoryInstallRecords", lArg );

      return lEventKey;
   }


   public static EventKey detachInventoryFromAircraft( InventoryKey aAircraftInvKey,
         InventoryKey aInvKey, Date aAttachDate, Set<UsageSnapshot> aUsages ) {
      return detachInventoryFromAircraft( aAircraftInvKey, aInvKey, null, aAttachDate, aUsages );
   }


   public static EventKey detachInventoryFromAircraft( InventoryKey aAircraftInvKey,
         InventoryKey aInvKey, EventKey aParentEvent, Date aAttachDate, Set<UsageSnapshot> aUsages ) {

      EventKey lEventKey =
            new EventBuilder().onInventory( aInvKey ).withType( RefEventTypeKey.FG )
                  .withParentEvent( aParentEvent ).withActualEndDate( aAttachDate )
                  .withStatus( RefEventStatusKey.FGRMVL ).build();

      EvtInvTable lEvtInstallEngine = EvtInvTable.findMainByEventKey( lEventKey );
      lEvtInstallEngine.setNHInventoryKey( aAircraftInvKey );
      lEvtInstallEngine.setHInventoryKey( aAircraftInvKey );
      lEvtInstallEngine.setAssmblInventoryKey( aAircraftInvKey );
      lEvtInstallEngine.update();

      // create inv_remove record
      DataSetArgument lArg = new DataSetArgument();
      lArg.add( lEventKey, "aEventDbId", "aEventId" );
      lArg.add( aInvKey, "aInvNoDbId", "aInvNoId" );
      lArg.add( "aEventDate", aAttachDate );
      MxDataAccess.getInstance().executeUpdate(
            "com.mxi.mx.core.query.event.CreateInventoryRemoveRecords", lArg );

      if ( aUsages != null && !aUsages.isEmpty() ) {
         EventInventoryService lEventInventoryService = new EventInventoryService( lEventKey );

         for ( UsageSnapshot lUsage : aUsages ) {
            lEventInventoryService.addUsageSnapshot( lUsage, null );
         }

      }

      InvInvTable lInvInvTable = InvInvTable.findByPrimaryKey( aInvKey );

      // if a trk is detached from aircraft, it and its sub-inv shall all set highest to trk and
      // assembly to null.
      // if a sub-assembly is detached from aircraft, it shall set highest and assembly to the
      // itself. All the sub-inv of sub-assembly still set assembly to the attached sub-assembly,
      // only highest shall be set to aircraft.
      boolean lIsSubAssembly = false;
      if ( RefInvClassKey.ASSY.equals( lInvInvTable.getInvClass() ) ) {
         lIsSubAssembly = true;
      }
      lInvInvTable.setNhInvNo( null );
      if ( lIsSubAssembly ) {
         lInvInvTable.setAssmblInvNo( aInvKey );
      } else {
         lInvInvTable.setAssmblInvNo( null );
      }
      lInvInvTable.setHInvNo( aInvKey );
      lInvInvTable.update();

      if ( lIsSubAssembly ) {
         updateHighestInventoryOfSubInventory( aInvKey, aAircraftInvKey );
      } else {
         updateHighestAndAssemblyInventoryOfSubInventory( aInvKey, aInvKey, null );
      }

      return lEventKey;
   }


   /**
    *
    * Helper method to set up the following inv_inv table denormalized columns:
    *
    * inv_inv.nh_inv_<key> , inv_inv.h_inv_<key> , inv_inv.assmbl_inv_<key>
    *
    * These columns help to track the inventory configuration information regarding the
    * next-highest, highest, and assembly-inventory of a component (and all its sub-components) when
    * it is attached to another component. These values vary depending on the classes of the
    * components.
    *
    *
    * @param aChildKey
    *           - child component
    * @param aParentKey
    *           - parent component to which the child is attached
    */
   public static void attach( InventoryKey aChildKey, InventoryKey aParentKey ) {

      InvInvTable lChild = InvInvTable.findByPrimaryKey( aChildKey );
      RefInvClassKey lChildClass = lChild.getInvClass();

      InvInvTable lParent = InvInvTable.findByPrimaryKey( aParentKey );
      RefInvClassKey lParentClass = lParent.getInvClass();

      verifyNoExistingParent( lChild );

      InventoryKey lHighestInvKey;
      InventoryKey lAssemblyInvKey;

      // Note: the order of this if statement is important.
      if ( ACFT.equals( lParentClass ) ) {
         lHighestInvKey = aParentKey;
         lAssemblyInvKey = aParentKey;
      } else if ( ASSY.equals( lParentClass ) ) {
         lHighestInvKey = lParent.getHInvNo();
         lAssemblyInvKey = aParentKey;
      } else {
         lHighestInvKey = lParent.getHInvNo();
         lAssemblyInvKey = lParent.getAssmblInvNo();
      }

      lChild.setNhInvNo( aParentKey );
      lChild.setHInvNo( lHighestInvKey );
      lChild.setAssmblInvNo( lAssemblyInvKey );
      lChild.update();

      updateHighestInventoryOfSubInventory( aChildKey, lHighestInvKey );

      if ( !ASSY.equals( lChildClass ) ) {
         updateAssemblyInventoryOfSubInventory( aChildKey, lAssemblyInvKey );
      }

   }


   /**
    * Verifies that the child inventory does not already have a parent, which is not itself. Throws
    * a runtime exception if the verification fails.
    *
    * @param aChild
    *           - child inventory to verify
    */
   private static void verifyNoExistingParent( InvInvTable aChild ) {
      InventoryKey lExistingParent = aChild.getNhInvNo();
      if ( lExistingParent != null && !lExistingParent.equals( aChild.getPk() ) ) {
         throw new RuntimeException( "Cannot add tracked inventory [" + aChild.getPk()
               + "] as it already has a parent [" + lExistingParent + "]." );
      }

   }


   /**
    *
    * Updates the assembly inventory value for all the sub-inventory of the provided root inventory.
    *
    * @param aRootInvKey
    * @param aAssemblyInvKey
    */
   private static void updateAssemblyInventoryOfSubInventory( InventoryKey aRootInvKey,
         InventoryKey aAssemblyInvKey ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aRootInvKey, "aRootInvNoDbId", "aRootInvNoId" );
      lArgs.add( aAssemblyInvKey, "aAssemblyInvNoDbId", "aAssemblyInvNoId" );
      MxDataAccess.getInstance().executeUpdate(
            "com.mxi.am.domain.builder.UpdateAssemblyInventoryOfSubInventory", lArgs );
   }

}
