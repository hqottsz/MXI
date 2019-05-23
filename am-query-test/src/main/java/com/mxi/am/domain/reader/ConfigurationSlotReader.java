package com.mxi.am.domain.reader;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;


/**
 * Reader for retrieving Configuration Slot information.
 *
 */
public class ConfigurationSlotReader {

   /**
    *
    * Retrieve the identifier for the root configuration slot of the provided assembly.
    *
    * @param aAssembly
    *           identifier of the assembly
    * @return
    */
   public static ConfigSlotKey readRootConfigurationSlot( AssemblyKey aAssembly ) {
      // The root config slot always has a bom id of 0.
      return new ConfigSlotKey( aAssembly, 0 );
   }


   /**
    *
    * Retrieve the identifier for a sub-configuration slot beneath a parent configuration slot given
    * that sub-configuration slot's code (the code is unique amongst its siblings).
    *
    * @param aParentConfigurationSlot
    * @param aCode
    * @return
    */
   public static ConfigSlotKey readSubConfigurationSlot( ConfigSlotKey aParentConfigurationSlot,
         String aCode ) {

      // Note: it is assumed that the code (not the name) of sub-config slots must be unique on an
      // assembly (thus, unique under the parent config slot).

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aParentConfigurationSlot, "aAssmblDbId", "aAssmblCd", "aAssmblBomId" );
      lArgs.add( "aSubConfigSlotCode", aCode );
      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.am.domain.GetSubConfigSlotByCode", lArgs );

      if ( lQs.isEmpty() ) {
         return null;
      } else if ( lQs.getRowCount() > 1 ) {
         throw new RuntimeException( "Unexpected number (" + lQs.getRowCount()
               + ") of sub-configuration slots with code [" + aCode
               + "] under parent configuration slot key=" + aParentConfigurationSlot );
      }
      lQs.first();

      return lQs.getKey( ConfigSlotKey.class, "assmbl_db_id", "assmbl_cd", "assmbl_bom_id" );
   }


   /**
    *
    * Retrieve the position key for a configuration slot given that configuration slot's code and
    * position code.
    *
    * @param aParentConfigurationSlot
    * @param aCode
    * @return
    */
   public static ConfigSlotPositionKey
         readConfigurationSlotPosition( ConfigSlotKey aConfigurationSlot, String aPositionCode ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aConfigurationSlot, "aAssmblDbId", "aAssmblCd", "aAssmblBomId" );
      lArgs.add( "aPositionCode", aPositionCode );
      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.am.domain.GetConfigSlotPositionByCode", lArgs );

      if ( lQs.isEmpty() ) {
         return null;
      } else if ( lQs.getRowCount() > 1 ) {
         throw new RuntimeException( "Unexpected number (" + lQs.getRowCount()
               + ") of configuration slots with position code [" + aPositionCode
               + "] under configuration slot key=" + aConfigurationSlot );
      }
      lQs.first();

      return lQs.getKey( ConfigSlotPositionKey.class, "assmbl_db_id", "assmbl_cd", "assmbl_bom_id",
            "assmbl_pos_id" );
   }

}
