package com.mxi.mx.core.services.dataservices;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.domain.builder.StockLevelBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.StreamedDataSet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.common.ejb.dao.QueryDAOBean;
import com.mxi.mx.core.dao.location.InvLocZoneDao;
import com.mxi.mx.core.key.BulkLoadElementKey;
import com.mxi.mx.core.key.InvLocZoneKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.RefBulkLoadFileActionKey;
import com.mxi.mx.core.key.RefBulkLoadStatusKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefStockLowActionKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.key.UtlFileImportKey;
import com.mxi.mx.core.table.DataServices.BulkLoadElementDao;
import com.mxi.mx.core.table.DataServices.BulkLoadElementTableRow;
import com.mxi.mx.core.table.DataServices.JdbcBulkLoadElementDao;
import com.mxi.mx.core.table.inv.InvLocZoneTable;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;


/**
 *
 * Parameterized unit test for the {@link BulkLoadDataService.writeToCsv} method
 *
 * @author sufelk
 *
 */
@RunWith( Parameterized.class )
public class WriteToCsvTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   /**
    * This jUnit rule creates a temporary folder which uses the default temp file directory, and
    * gets deleted when a test method passes/fails.
    */
   @Rule
   public TemporaryFolder iFolder = new TemporaryFolder();

   private static final String WAREHOUSE_STOCK_LEVEL_QRX =
         "com.mxi.mx.web.query.stock.WarehouseStockLevels";
   private static final String SUPPLY_LOC_STOCK_LEVEL_QRX =
         "com.mxi.mx.web.query.stock.SupplyLocationStockLevels";
   private static final String ERROR_LOG_QRX =
         "com.mxi.mx.core.query.dataservices.GetBulkLoadElementComponents";
   private static final String BIN_ROUTE_ORDER_QRX =
         "com.mxi.mx.core.query.location.GetBinRouteOrderInformation";
   private static final String NO_STOCK_LEVELS = "No stock levels found";
   private static final String NO_BIN_LOCATIONS = "No bin locations found";
   private static final String WAREHOUSE_CD =
         RefBulkLoadFileActionKey.WAREHOUSE_STOCK_LEVEL.getCd();
   private static final String SUPPLY_LOC_CD =
         RefBulkLoadFileActionKey.SUPPLY_LOC_STOCK_LEVEL.getCd();
   private static final String BIN_ROUTES_CD = RefBulkLoadFileActionKey.BIN_ROUTE_ORDER.getCd();

   private static final String STOCK_CD = "STOCK_CD";
   private static final double STOCK_CD_WEIGHT = 50.0;
   private static final String AIRPORT = "BIA";
   private static final String SRVSTORE = "BIA/SRVSTORE";
   private static final String OWNER = "SOWRY";
   private static final RefStockLowActionKey STOCK_LOW_ACTION = RefStockLowActionKey.MANUAL;
   private static final double SAFETY_LEVEL = 5.0;
   private static final double RESTOCK_QT = 10.0;
   private static final double MAX_LEVEL = 100.0;
   private static final double NUM_INBOUND_FLIGHTS = 45.0;
   private static final double STAITION_WEIGHT_FACTOR = 5.0;
   private static final double ALLOCATION_PERCENTAGE = 12.0;
   private static final double REORDER_SHIPPING_QT = 15.0;
   private static final int ROUTE_ORDER = 3;

   private static StockNoKey iStockNo;
   private static LocationKey iSupplyLocation;
   private static LocationKey iNonSupplyLocation;
   private static OwnerKey iOwner;
   private static UtlFileImportKey iFileImportKey;
   private static LocationKey iBinLocation;

   private BulkLoadDataService iBulkLoadService;
   private BulkLoadElementDao iBulkLoadElementDao;
   private File iFile = null;
   private String iQrx;
   private String iEmptyCsvMsg;
   private String iFileActionCd;
   private int iExpectedRowCount;
   private int iExpectedColCount;
   private boolean iIsErrorLog;
   private DataSetArgument iDsArgs;


   /**
    *
    * Creates a new {@linkplain WriteToCsvTest} object.
    *
    * @param aQrx
    *           the query
    * @param aDsArgs
    *           the dataset arguments, if any
    * @param aEmptyCsvMsg
    *           the empty csv message
    * @param aFileActionCd
    *           the file action code
    * @param aIsErrorLog
    *           true if the csv should be an error log (with an additional error column at the end)
    * @param aExpectedRowCount
    *           the expected row count
    * @param aExpectedColCount
    *           the expected column count
    */
   public WriteToCsvTest(String aQrx, DataSetArgument aDsArgs, String aEmptyCsvMsg,
         String aFileActionCd, boolean aIsErrorLog, int aExpectedRowCount, int aExpectedColCount) {
      iQrx = aQrx;
      iDsArgs = aDsArgs;
      iEmptyCsvMsg = aEmptyCsvMsg;
      iFileActionCd = aFileActionCd;
      iIsErrorLog = aIsErrorLog;
      iExpectedRowCount = aExpectedRowCount;
      iExpectedColCount = aExpectedColCount;
   }


   @Before
   public void setup() throws IOException, SQLException {

      // initialization
      iBulkLoadService = new BulkLoadDataService();
      iBulkLoadElementDao = new JdbcBulkLoadElementDao();
      iFileImportKey = new UtlFileImportKey( 1234, 12 );

      // create a new temporary file which will be deleted on exit
      iFile = File.createTempFile( "temp", ".csv", iFolder.getRoot() );
      iFile.deleteOnExit();

      // create a stock number
      iStockNo =
            new StockBuilder().withStockCode( STOCK_CD ).withMaxMultQt( STOCK_CD_WEIGHT ).build();

      // create supply and non-supply locations
      iSupplyLocation = Domain.createLocation( location -> {
         location.setCode( AIRPORT );
         location.setType( RefLocTypeKey.AIRPORT );
         location.setIsSupplyLocation( true );
         location.setInboundFlightsQt( NUM_INBOUND_FLIGHTS );
      } );
      iNonSupplyLocation = Domain.createLocation( location -> {
         location.setCode( SRVSTORE );
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setIsSupplyLocation( false );
         location.setInboundFlightsQt( NUM_INBOUND_FLIGHTS );
      } );

      // create an owner
      iOwner = Domain.createOwner( owner -> {
         owner.setCode( OWNER );
      } );

      // create a new bin location
      iBinLocation = Domain.createLocation( location -> {
         LocationKey lLocationKey =
               new LocationKey( iNonSupplyLocation.getDbId(), iNonSupplyLocation.getId() );
         location.setParent( lLocationKey );
         location.setType( RefLocTypeKey.BIN );
      } );

      // set route order value for a bin location in inv_loc_zone table
      InvLocZoneDao lInvLocZoneDao = InjectorContainer.get().getInstance( InvLocZoneDao.class );
      InvLocZoneTable lInvLocZoneTable = lInvLocZoneDao.create( new InvLocZoneKey( iBinLocation ) );
      lInvLocZoneTable.setRouteOrder( ROUTE_ORDER );
      lInvLocZoneDao.update( lInvLocZoneTable );

      // create a new stock level at a supply location
      new StockLevelBuilder( iSupplyLocation, iStockNo, iOwner )
            .withWeightFactorQt( STAITION_WEIGHT_FACTOR )
            .withAllocationPercentage( ALLOCATION_PERCENTAGE ).withMinReorderLevel( SAFETY_LEVEL )
            .withReorderQt( RESTOCK_QT ).withMaxLevel( MAX_LEVEL )
            .withBatchSize( REORDER_SHIPPING_QT ).withStockLowAction( STOCK_LOW_ACTION ).build();

      // create a new stock level at a warehouse (srvstore), non-supply location
      new StockLevelBuilder( iNonSupplyLocation, iStockNo, iOwner )
            .withWeightFactorQt( STAITION_WEIGHT_FACTOR )
            .withAllocationPercentage( ALLOCATION_PERCENTAGE ).withMinReorderLevel( SAFETY_LEVEL )
            .withReorderQt( RESTOCK_QT ).withMaxLevel( MAX_LEVEL )
            .withBatchSize( REORDER_SHIPPING_QT ).withStockLowAction( STOCK_LOW_ACTION ).build();

      // create a row in the bulk_load_element table with the ERROR status
      BulkLoadElementKey lRowKey = new BulkLoadElementKey( iFileImportKey, 100 );
      BulkLoadElementTableRow lRow = iBulkLoadElementDao.create( lRowKey );
      lRow.setStatus( RefBulkLoadStatusKey.ERROR );
      lRow.setFileActionType( RefBulkLoadFileActionKey.WAREHOUSE_STOCK_LEVEL );
      iBulkLoadElementDao.insert( lRow );

   }


   /**
    * setup dataset arguments for the error log scenario
    *
    * @return DataSetArgument
    *
    */
   private static DataSetArgument getErrorLogDataSetArgs() {

      DataSetArgument lErrorLogDsArgs = new DataSetArgument();
      lErrorLogDsArgs.add( new UtlFileImportKey( 1234, 12 ), "aFileImportDbId", "aFileImportId" );
      lErrorLogDsArgs.addWhereEquals( new String[] { "status_db_id", "status_cd" },
            RefBulkLoadStatusKey.ERROR );
      return lErrorLogDsArgs;
   }


   /**
    *
    * Parameters for each round of testing are the query name, ds arguments, empty csv message,
    * action type code, error log mode, expected row count and expected column count.
    *
    * @return a collection of test parameters
    */
   @Parameterized.Parameters
   public static Collection CsvWriterActionTypes() {
      return Arrays.asList( new Object[][] {
            // warehouse stock level csv - happy path
            { WAREHOUSE_STOCK_LEVEL_QRX, null, NO_STOCK_LEVELS, WAREHOUSE_CD, false, 2, 7 },

            // supply location stock level csv - happy path
            { SUPPLY_LOC_STOCK_LEVEL_QRX, null, NO_STOCK_LEVELS, SUPPLY_LOC_CD, false, 2, 12 },

            // warehouse stock level csv - when the file contains an error (and a row remains in the
            // bulk_load_element table)
            { ERROR_LOG_QRX, getErrorLogDataSetArgs(), "", WAREHOUSE_CD, true, 2, 8 },

            // bin locations csv - happy path
            { BIN_ROUTE_ORDER_QRX, null, NO_BIN_LOCATIONS, BIN_ROUTES_CD, false, 2, 3 }

      } );

   }


   /**
    *
    * GIVEN a dataset and a file reader, WHEN the data is parsed and written to an OpenCSV CSVWriter
    * object using the {@link BulkLoadDataService.writeToCsv} method, THEN the data should be
    * written to a specified writer as required.
    *
    * @throws IOException
    *            the expected annotation is added to make sure the exception is not thrown
    * @throws MxException
    */
   @Test( expected = Test.None.class )
   public void testWriteToCsv() throws IOException, MxException {

      // get the StreamedDataSet

      QueryDAOBean lQueryDAOBean = ( ( QueryDAOBean ) QuerySetFactory.getInstance() );
      StreamedDataSet lDs =
            iDsArgs == null ? lQueryDAOBean.executeQueryStreamed( iQrx, new DataSetArgument() )
                  : lQueryDAOBean.executeQueryStreamed( iQrx, iDsArgs );

      // pass the parameters to the writeToCsv method and close the writer
      CSVWriter lWriter = iBulkLoadService.writeToCsv( lDs, new FileWriter( iFile ), iEmptyCsvMsg,
            iFileActionCd, iIsErrorLog );
      lWriter.close();

      // create a new reader for the temporary file to assert our data
      CSVReader lReader = new CSVReader( new FileReader( iFile ) );

      // if the export is for an error log - this is a specific scenario, not recommended to re-use.
      if ( iIsErrorLog ) {

         // create a new reader to avoid rows being lost
         CSVReader lErrorReader = new CSVReader( new FileReader( iFile ) );

         // read the header row and assert that the column length is as expected
         String[] lHeaderRow = lErrorReader.readNext();
         assertEquals( iExpectedColCount, lHeaderRow.length );

         // assert that the last column is the error log
         assertEquals( "Error log column", i18n.get( "web.lbl.ERROR_LOG" ),
               lHeaderRow[iExpectedColCount - 1] );
         lErrorReader.close();

      }

      // fetch StreamedDataSet again as it is traversed once in writeToCsv() method

      lDs = iDsArgs == null ? lQueryDAOBean.executeQueryStreamed( iQrx, new DataSetArgument() )
            : lQueryDAOBean.executeQueryStreamed( iQrx, iDsArgs );

      // if the dataset is not empty, iterate through the row and assert that each row matches
      // the expected column count, and that the expected number of rows is generated
      if ( lDs.next() ) {
         String[] lRow;
         int lRowCount = 0;
         while ( ( lRow = lReader.readNext() ) != null ) {
            assertEquals( iExpectedColCount, lRow.length );
            lRowCount += 1;
         }
         assertEquals( iExpectedRowCount, lRowCount );
      }

      // if the dataset is empty, ensure that the empty csv message has been set, and that the
      // expected number of rows is generated (the empty message may be either in the first row
      // or second based on whether or not the file has a header)
      else {
         List<String[]> lRows = lReader.readAll();
         assertTrue( lRows.contains( new String[] { iEmptyCsvMsg } ) );
         assertEquals( iExpectedRowCount, lRows.size() );
      }

      // close the file reader
      lReader.close();
      // close StreamedDataSet
      ( ( QueryDAOBean ) QuerySetFactory.getInstance() ).closeStream( lDs );
   }

}
