package com.mxi.am.domain.builder;

import com.mxi.am.domain.PartAdvisory;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.ejb.EjbFactory;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.Table;
import com.mxi.mx.core.key.AdvisoryKey;
import com.mxi.mx.core.key.EqpPartVendorKey;
import com.mxi.mx.core.key.RefPartVendorTypeKey;
import com.mxi.mx.core.key.RefVendorStatusKey;
import com.mxi.mx.core.table.eqp.EqpPartVendorTable;


public class PartAdvisoryBuilder {

   public static AdvisoryKey build( PartAdvisory partAdvisory ) {

      int advisoryDbId = Table.Util.getDatabaseId();
      int nextAdvisoryId =
            EjbFactory.getInstance().createSequenceGenerator().nextValue( "EQP_ADVSRY_ID_SEQ" );

      AdvisoryKey advisoryKey = new AdvisoryKey( advisoryDbId, nextAdvisoryId );

      DataSetArgument args = new DataSetArgument();
      args.add( advisoryKey, "advsry_db_id", "advsry_id" );
      args.add( "advsry_name", partAdvisory.getName() );

      MxDataAccess.getInstance().executeInsert( "EQP_ADVSRY", args );

      if ( partAdvisory.getVendor() != null ) {

         EqpPartVendorKey partVendorKey =
               new EqpPartVendorKey( partAdvisory.getPartNo(), partAdvisory.getVendor() );

         EqpPartVendorTable lEqpPartVendorTable = EqpPartVendorTable.create();
         lEqpPartVendorTable.setVendorStatus( RefVendorStatusKey.APPROVED );
         lEqpPartVendorTable.setPartNo( partAdvisory.getPartNo() );
         lEqpPartVendorTable.setVendor( partAdvisory.getVendor() );
         lEqpPartVendorTable.insert( partVendorKey );

         args = new DataSetArgument();
         args.add( advisoryKey, "advsry_db_id", "advsry_id" );
         args.add( partAdvisory.getPartNo(), "part_no_db_id", "part_no_id" );
         args.add( "active_bool", true );
         args.add( partAdvisory.getVendor(), "vendor_db_id", "vendor_id" );
         args.add( RefPartVendorTypeKey.PURCHASE, "part_vendor_type_db_id", "part_vendor_type_cd" );

         MxDataAccess.getInstance().executeInsert( "EQP_PART_VENDOR_ADVSRY", args );

      } else {

         args = new DataSetArgument();
         args.add( advisoryKey, "advsry_db_id", "advsry_id" );
         args.add( partAdvisory.getPartNo(), "part_no_db_id", "part_no_id" );
         args.add( "active_bool", true );

         MxDataAccess.getInstance().executeInsert( "EQP_PART_ADVSRY", args );
      }

      return advisoryKey;
   }
}
