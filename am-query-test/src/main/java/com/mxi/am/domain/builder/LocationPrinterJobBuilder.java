package com.mxi.am.domain.builder;

import com.mxi.am.domain.LocationPrinterJob;
import com.mxi.mx.core.key.InvLocPrinterJobKey;
import com.mxi.mx.core.table.inv.InvLocPrinterJob;


public class LocationPrinterJobBuilder {

   public static InvLocPrinterJobKey build( LocationPrinterJob printerJob ) {

      InvLocPrinterJob printerJobRow = InvLocPrinterJob
            .create( new InvLocPrinterJobKey( printerJob.getPrinter().toValueString() + ":"
                  + printerJob.getJobType().toValueString() ) );
      printerJobRow.setDefaultBool( printerJob.isDefault() );

      return printerJobRow.insert();
   }

}
