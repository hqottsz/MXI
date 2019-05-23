
package com.mxi.mx.core.dao.lrp.aircraft;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.mxi.am.db.connection.loader.SqlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.dao.ReferenceCache;
import com.mxi.mx.core.dao.lrp.DaoTestCase;
import com.mxi.mx.core.dao.lrp.LrpReferenceCache;
import com.mxi.mx.model.key.CdKey;
import com.mxi.mx.model.key.CdKeyImpl;
import com.mxi.mx.model.key.IdKey;
import com.mxi.mx.model.key.IdKeyImpl;
import com.mxi.mx.model.lrp.Aircraft;
import com.mxi.mx.model.lrp.Assembly;
import com.mxi.mx.model.lrp.ForecastModel;
import com.mxi.mx.model.lrp.key.AircraftKeyImpl;


/**
 * DOCUMENT_ME
 *
 * @author yvakulenko
 */
public class AircraftDaoTest extends DaoTestCase {

   private static final IdKey LRP_KEY = new IdKeyImpl( LRP_TEST_DB_ID, TEST_LRP_ID );
   private static final IdKey ACTUAL_AIRCRAFT_KEY = new IdKeyImpl( LRP_TEST_DB_ID, 1 );
   private static final CdKey ASSEMBLY_KEY = new CdKeyImpl( LRP_TEST_DB_ID, "BOEING" );
   private static final IdKey FORECAST_MODEL_KEY = new IdKeyImpl( LRP_TEST_DB_ID, 1 );
   private static final Date RETIREMENT_DT = new Date( 1000 );
   private static final Date MANUFACT_DT = new Date( 1000000 );
   private static final Date INDUCTION_DT = new Date( 200000000 );

   private AircraftDao iAircraftDao;


   @Override
   @Before
   public void loadData() throws Exception {
      super.loadData();

      iAircraftDao = iFactory.getAircraftDao( LRP_KEY );

      LrpReferenceCache lReferenceCache = new LrpReferenceCache();
      ReferenceCache<CdKey, Assembly> lAssemblyCache = lReferenceCache.getAssemblyCache();
      lAssemblyCache.addReferences( getTestAssemblies() );

      ReferenceCache<IdKey, ForecastModel> lForecastModelCache =
            lReferenceCache.getForecastModelsCache();
      lForecastModelCache.addReferences( getTestForecastModels() );

      iAircraftDao.setReferenceCache( lReferenceCache );
   }


   /**
    * Test Create operation.<br>
    * 1. Construct object<br>
    * 2. Write it into DB<br>
    * 3. Read data from DB and assert
    */
   @Test
   public void testCreate() {
      ForecastModel lForecastModel = new ForecastModel();
      lForecastModel.setActualRefKey( FORECAST_MODEL_KEY );

      Assembly lAssembly = new Assembly();
      lAssembly.setActualRefKey( ASSEMBLY_KEY );

      Aircraft lAircraft = new Aircraft();
      lAircraft.setActualRefKey( ACTUAL_AIRCRAFT_KEY );
      lAircraft.setAssembly( lAssembly );
      lAircraft.setForecastModel( lForecastModel );
      lAircraft.setRetirementDate( RETIREMENT_DT );
      lAircraft.setManufacturedDate( MANUFACT_DT );
      lAircraft.setReceivedDate( INDUCTION_DT );
      lAircraft.setReadOnly( true );
      lAircraft.setAdHocEvtCtrl( false );

      Collection<Aircraft> lAircrafts = new ArrayList<Aircraft>();
      lAircrafts.add( lAircraft );

      // Create test entity
      iAircraftDao.createAll( lAircrafts );

      // Fetch records
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aLrpDbId", LRP_TEST_DB_ID );
      lArgs.add( "aLrpId", TEST_LRP_ID );

      DataSet lDs = MxDataAccess.getInstance()
            .executeQuery( "com.mxi.mx.core.dao.lrp.aircraft.GetLrpAircraft", lArgs );
      lDs.beforeFirst();
      assertEquals( 1, lDs.getTotalRowCount() ); // assert number of record
      lDs.next(); // we expect only 1 record, so get it

      // assert data
      assertEquals( ACTUAL_AIRCRAFT_KEY,
            new IdKeyImpl( lDs.getInt( "INV_NO_DB_ID" ), lDs.getInt( "INV_NO_ID" ) ) );
      assertEquals( ASSEMBLY_KEY,
            new CdKeyImpl( lDs.getInt( "ASSMBL_DB_ID" ), lDs.getString( "ASSMBL_CD" ) ) );
      assertEquals( FORECAST_MODEL_KEY, new IdKeyImpl( lDs.getInt( "FORECAST_MODEL_DB_ID" ),
            lDs.getInt( "FORECAST_MODEL_ID" ) ) );

      assertEquals( RETIREMENT_DT, lDs.getDate( "RETIREMENT_DT" ) );
      assertEquals( MANUFACT_DT, lDs.getDate( "MANUFACT_DT" ) );
      assertEquals( INDUCTION_DT, lDs.getDate( "INDUCTION_DT" ) );
      assertTrue( "READ_ONLY_BOOL should be true.", lDs.getBoolean( "READ_ONLY_BOOL" ) );
      assertFalse( "AH_EVT_CTRL_BOOL should be false.", lDs.getBoolean( "AH_EVT_CTRL_BOOL" ) );
   }


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testDelete() throws Exception {
      SqlLoader.load( iDatabaseConnectionRule.getConnection(), getClass(),
            "CreateTestLrpAircrafts.sql" );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aLrpDbId", LRP_TEST_DB_ID );
      lArgs.add( "aLrpId", TEST_LRP_ID );

      DataSet lDs = MxDataAccess.getInstance()
            .executeQuery( "com.mxi.mx.core.dao.lrp.aircraft.GetLrpAircraft", lArgs );
      lDs.beforeFirst();
      assertEquals( 2, lDs.getTotalRowCount() ); // assert number of record before

      iAircraftDao.deleteAll();

      lDs = MxDataAccess.getInstance()
            .executeQuery( "com.mxi.mx.core.dao.lrp.aircraft.GetLrpAircraft", lArgs );
      lDs.beforeFirst();
      assertEquals( 0, lDs.getTotalRowCount() ); // assert number of record after deletion
   }


   /**
    * Reads aircrafts.<br>
    * Expects one actual and one future aircrafts.
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testRead() throws Exception {
      SqlLoader.load( iDatabaseConnectionRule.getConnection(), getClass(),
            "CreateTestLrpAircrafts.sql" );

      Collection<Aircraft> lAircrafts = iAircraftDao.readAll();
      assertEquals( 2, lAircrafts.size() ); // assert number of record

      boolean lFutureAircraftFound = false;
      boolean lAircraftFound = false;

      for ( Aircraft lAircraft : lAircrafts ) {

         if ( lAircraft.getFuture() ) {
            lFutureAircraftFound = true;
            assertTrue( lAircraft.getFuture() );
            assertEquals( new CdKeyImpl( 777, "AIRBUS" ),
                  lAircraft.getAssembly().getActualRefKey() );
            assertEquals( new IdKeyImpl( 777, 1 ), lAircraft.getForecastModel().getActualRefKey() );
            assertEquals( "LRP TEST AIRBUS ASSEMBLY-FUTURE", lAircraft.getName() );
            assertEquals( new AircraftKeyImpl( 777, 1, 2 ), lAircraft.getPrimaryKey() );
            assertFalse( lAircraft.isReadOnly() );
            assertFalse( lAircraft.getAdHocEvtCtrl() );
         } else {
            lAircraftFound = true;
            assertFalse( lAircraft.getFuture() );
            assertEquals( new CdKeyImpl( 777, "BOEING" ),
                  lAircraft.getAssembly().getActualRefKey() );
            assertEquals( new IdKeyImpl( 777, 1 ), lAircraft.getForecastModel().getActualRefKey() );
            assertEquals( "LRP TEST BOEING ASSEMBLY-ACTUAL", lAircraft.getName() );
            assertEquals( new AircraftKeyImpl( 777, 1, 1 ), lAircraft.getPrimaryKey() );
            assertTrue( lAircraft.isReadOnly() );
            assertTrue( lAircraft.getAdHocEvtCtrl() );
         }
      }

      assertTrue( lFutureAircraftFound );
      assertTrue( lAircraftFound );
   }


   /**
    * Aircraft DAO requires already loaded assemblies, to establish references to.
    *
    * @return Collection of assemblies.
    */
   private Collection<Assembly> getTestAssemblies() {
      Collection<Assembly> lAssemblies = new ArrayList<Assembly>();

      Assembly lAssembly = new Assembly();
      lAssembly.setActualRefKey( new CdKeyImpl( 777, "BOEING" ) );
      lAssemblies.add( lAssembly );

      lAssembly = new Assembly();
      lAssembly.setActualRefKey( new CdKeyImpl( 777, "AIRBUS" ) );
      lAssemblies.add( lAssembly );

      return lAssemblies;
   }


   /**
    * Aircraft DAO requires cached forecast models.
    *
    * @return Collection of forecast models.
    */
   private Collection<ForecastModel> getTestForecastModels() {
      Collection<ForecastModel> lModels = new ArrayList<ForecastModel>();

      ForecastModel lModel = new ForecastModel();
      lModel.setActualRefKey( new IdKeyImpl( 777, 1 ) );

      lModels.add( lModel );

      return lModels;
   }
}
