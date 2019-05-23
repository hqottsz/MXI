package com.mxi.am.domain.builder;

import com.mxi.am.domain.LocationPrinter;
import com.mxi.mx.core.key.LocationPrinterKey;
import com.mxi.mx.core.table.inv.InvLocPrinter;


public class LocationPrinterBuilder {

   public static LocationPrinterKey build( LocationPrinter printer ) {

      InvLocPrinter printerRow = InvLocPrinter.create( printer.getLocation() );

      printerRow.setPrinterParm( printer.getPrinterParm() );
      printerRow.setPrinterSdesc( printer.getPrinterSdesc() );
      printerRow.setPrinterType( printer.getPrinterType() );

      return printerRow.insert();
   }

}
