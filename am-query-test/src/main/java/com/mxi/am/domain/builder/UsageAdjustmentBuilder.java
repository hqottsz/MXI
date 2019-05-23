package com.mxi.am.domain.builder;

import java.math.BigDecimal;

import com.mxi.am.domain.UsageAdjustment;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.usage.dao.JdbcUsageDataDao;
import com.mxi.mx.core.usage.dao.JdbcUsageRecordDao;
import com.mxi.mx.core.usage.dao.UsageDataEntity;
import com.mxi.mx.core.usage.dao.UsageRecordEntity;
import com.mxi.mx.core.usage.model.UsageAdjustmentId;
import com.mxi.mx.core.usage.model.UsageDataId;
import com.mxi.mx.persistence.uuid.SequentialUuidGenerator;


public class UsageAdjustmentBuilder {

   private static final JdbcUsageRecordDao JDBC_USAGE_RECORD_DAO = new JdbcUsageRecordDao();
   private static final JdbcUsageDataDao JDBC_USAGE_DATA_DAO = new JdbcUsageDataDao();
   private static final SequentialUuidGenerator SEQUENTIAL_UUID_GENERATOR =
         new SequentialUuidGenerator();


   public static UsageAdjustmentId build( UsageAdjustment aUsageRecord ) {

      InventoryKey lMainInventoryKey = aUsageRecord.getMainInventory();

      // Create a record in usg_usage_record.
      UsageAdjustmentId lUsageRecordId =
            new UsageAdjustmentId( SEQUENTIAL_UUID_GENERATOR.newUuid() );

      UsageRecordEntity lUsageRecordEntity = new UsageRecordEntity( lUsageRecordId );
      lUsageRecordEntity.setInventoryKey( lMainInventoryKey );
      lUsageRecordEntity.setUsageDate( aUsageRecord.getUsageDate() );
      lUsageRecordEntity.setNegatedBool( aUsageRecord.isNegated() );
      lUsageRecordEntity.setAppliedBool( aUsageRecord.hasBeenApplied() );
      lUsageRecordEntity.setUsageType( aUsageRecord.getUsageType() );

      JDBC_USAGE_RECORD_DAO.persist( lUsageRecordEntity );

      // Create a record in usg_usage_data for each usage parameter for each inventory.
      for ( InventoryKey lInventory : aUsageRecord.getAllInventory() ) {

         ConfigSlotPositionKey lConfigSlotPositionKey =
               InvInvTable.findByPrimaryKey( lInventory ).getBOMItemPosition();

         for ( DataTypeKey lDataType : aUsageRecord.getDataTypes( lInventory ) ) {

            BigDecimal lValue = aUsageRecord.getUsageValue( lInventory, lDataType );
            BigDecimal lDelta = aUsageRecord.getUsageDelta( lInventory, lDataType );

            UsageDataEntity lUsageDataEntity =
                  new UsageDataEntity( new UsageDataId( SEQUENTIAL_UUID_GENERATOR.newUuid() ) );
            lUsageDataEntity.setUsageRecord( lUsageRecordId );
            lUsageDataEntity.setInventoryKey( lInventory );
            lUsageDataEntity.setConfigSlotPosition( lConfigSlotPositionKey );
            lUsageDataEntity.setDataTypeKey( lDataType );
            lUsageDataEntity.setTsn( lValue );
            lUsageDataEntity.setTsi( lValue );
            lUsageDataEntity.setTso( lValue );
            lUsageDataEntity.setTsnDelta( lDelta );
            lUsageDataEntity.setTsiDelta( lDelta );
            lUsageDataEntity.setTsoDelta( lDelta );

            JDBC_USAGE_DATA_DAO.persist( lUsageDataEntity );
         }
      }

      return lUsageRecordId;
   }
}
