package com.mxi.am.domain.builder;

import com.mxi.am.domain.DamageRecord;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.core.key.InventoryDamageKey;
import com.mxi.mx.core.table.inv.InvDamageDao;
import com.mxi.mx.core.table.inv.InvDamageTable;


public class DamageRecordBuilder {

   public static InventoryDamageKey build( DamageRecord damageRecord ) {
      InvDamageDao invDamageDao = InjectorContainer.get().getInstance( InvDamageDao.class );
      InventoryDamageKey invDamageKey = invDamageDao.generatePrimaryKey();
      InvDamageTable invDamageRow = invDamageDao.create( invDamageKey );
      invDamageRow.setInventoryKey( damageRecord.getInventoryKey() );
      invDamageRow.setFaultKey( damageRecord.getFaultKey() );
      invDamageRow.setLocationDescription( damageRecord.getLocation() );
      if ( damageRecord.getAltId() != null ) {
         invDamageRow.setAlternateKey( damageRecord.getAltId() );
      }
      invDamageDao.insert( invDamageRow );
      return invDamageKey;
   }
}
