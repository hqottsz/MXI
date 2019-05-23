package com.mxi.am.domain.builder;

import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

import com.mxi.am.domain.Manufacturer;
import com.mxi.mx.common.table.Table;
import com.mxi.mx.core.key.ManufacturerKey;
import com.mxi.mx.core.table.eqp.EqpManufactTable;


/**
 * Builder class for manufacturer domain object
 *
 */
public class ManufacturerBuilder {

   private static int iManufacturerCode = 0;


   public ManufacturerKey build( Manufacturer aManufacturer ) {

      ManufacturerKey lManufacturerKey = new ManufacturerKey( Table.Util.getDatabaseId(),
            ( String ) defaultIfNull( aManufacturer.getCode(), generateUniqueManufacturerCode() ) );
      EqpManufactTable lEqpManufactTable = EqpManufactTable.create( lManufacturerKey );

      return lEqpManufactTable.insert();
   }


   private static String generateUniqueManufacturerCode() {
      return "MANUFACTERER" + iManufacturerCode++;
   }

}
