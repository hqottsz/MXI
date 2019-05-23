package com.mxi.mx.core.services.location;

import static org.junit.Assert.assertEquals;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.core.dao.location.InvLocZoneDao;
import com.mxi.mx.core.key.InvLocZoneKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.table.inv.InvLocZoneTable;


/**
 * Helper class for unit tests related to the export/import route order functionality
 *
 * @author sufelk
 *
 */
public class RouteOrderTestUtils {

   /**
    *
    * Builds an excel workbook in the same format as the buildExcelForRouteOrder method in the
    * ZoneService. This excel is used to validate the exceptions that occur in the Import Sub
    * Locations flow.
    *
    * @return an excel workbook with formatting similar to a workbook generated through
    *         ZoneService.buildExcelForRouteOrder()
    */
   public static HSSFWorkbook buildExportRouteOrderExcel() {
      HSSFWorkbook lWorkbook = new HSSFWorkbook();
      HSSFSheet lSheet = lWorkbook.createSheet( i18n.get( "core.lbl.ROUTE_ORDER" ) );

      // create the first row with the column headers
      Row lHeaderRow = lSheet.createRow( 0 );
      lHeaderRow.createCell( 0 ).setCellValue( i18n.get( "core.lbl.PARENT_LOCATION" ) );
      lHeaderRow.createCell( 1 ).setCellValue( i18n.get( "core.lbl.BIN_LOC_CODE" ) );
      lHeaderRow.createCell( 2 ).setCellValue( i18n.get( "core.lbl.ROUTE_ORDER" ) );

      return lWorkbook;
   }


   /**
    *
    * Adds a row of route order data to an excel workbook
    *
    * @param aWorkbook
    *           the excel workbook
    * @param aParentLocCode
    *           the parent location code
    * @param aBinLocCode
    *           the bin location code
    * @param aRouteOrder
    *           the route order
    * @return the workbook with the new data added
    */
   public static HSSFWorkbook addRouteOrderDataToExcel( HSSFWorkbook aWorkbook,
         String aParentLocCode, String aBinLocCode, int aRouteOrder ) {

      // insert a new row after the existing data rows
      HSSFSheet lSheet = aWorkbook.getSheetAt( 0 );
      int lRowNum = lSheet.getLastRowNum() + 1;
      Row lDataRow = lSheet.createRow( lRowNum );

      // create and enter data into the cells
      lDataRow.createCell( 0 ).setCellValue( aParentLocCode );
      lDataRow.createCell( 1 ).setCellValue( aBinLocCode );
      lDataRow.createCell( 2 ).setCellValue( aRouteOrder );

      return aWorkbook;
   }


   /**
    *
    * Adds a route order to a bin location
    *
    * @param aBinLocationKey
    *           the bin location key
    * @param aRouteOrder
    *           the route order
    */
   public static void addRouteOrder( LocationKey aBinLocationKey, Integer aRouteOrder ) {
      InvLocZoneDao lInvLocZoneDao = InjectorContainer.get().getInstance( InvLocZoneDao.class );
      InvLocZoneTable lInvLocZoneTable =
            lInvLocZoneDao.create( new InvLocZoneKey( aBinLocationKey ) );
      lInvLocZoneTable.setRouteOrder( aRouteOrder );
      lInvLocZoneDao.update( lInvLocZoneTable );
   }


   /**
    *
    * Asserts the route order value in the database
    *
    * @param aBinLocationKey
    *           the bin location key
    * @param aRouteOrder
    *           the route order
    */
   public static void assertRouteOrder( LocationKey aBinLocationKey, Integer aRouteOrder ) {
      assertEquals( "Route order", aRouteOrder,
            InvLocZoneTable.findByPrimaryKey( aBinLocationKey ).getRouteOrder(), 0 );

   }


   /**
    *
    * Asserts that the route order information has been cleared from the db (no data exists in the
    * inv_loc_zone table)
    *
    * @param aBinLocationKey
    *           the bin location key
    */
   public static void assertRouteOrderCleared( LocationKey aBinLocationKey ) {
      InvLocZoneDao lInvLocZoneDao = InjectorContainer.get().getInstance( InvLocZoneDao.class );
      InvLocZoneTable lInvLocZoneTable =
            lInvLocZoneDao.create( new InvLocZoneKey( aBinLocationKey ) );
      assertEquals( "Route order information cleared", false, lInvLocZoneTable.exists() );
   }

}
