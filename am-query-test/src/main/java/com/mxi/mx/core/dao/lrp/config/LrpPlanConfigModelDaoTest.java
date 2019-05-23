
package com.mxi.mx.core.dao.lrp.config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.dao.lrp.LrpDaoFactoryImpl;
import com.mxi.mx.core.dao.lrp.TestData;
import com.mxi.mx.core.dao.lrp.plan.PlanDao;
import com.mxi.mx.core.dao.lrp.plan.PlanDaoImpl;
import com.mxi.mx.model.key.CdKeyImpl;
import com.mxi.mx.model.key.IdKey;
import com.mxi.mx.model.key.IdKeyImpl;
import com.mxi.mx.model.key.UserKeyImpl;
import com.mxi.mx.model.lrp.Aircraft;
import com.mxi.mx.model.lrp.Assembly;
import com.mxi.mx.model.lrp.ForecastModel;
import com.mxi.mx.model.lrp.GantConfiguration;
import com.mxi.mx.model.lrp.LRPPlan;
import com.mxi.mx.model.lrp.Severity;
import com.mxi.mx.model.user.User;


/**
 * DOCUMENT_ME
 *
 * @author adenysenko
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class LrpPlanConfigModelDaoTest {

   GantConfiguration iConfig;
   LrpPlanConfigDao iConfigDao;
   LRPPlan iPlan;
   PlanDao iPlanDao;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   @Before
   public void setUp() throws Exception {
      TestData lData = new TestData();

      iPlanDao = PlanDaoImpl.getInstance();

      iPlan = new LRPPlan();
      iPlan.setName( "ThePlan" );

      iConfig = lData.getGanttConfiguration();

      iPlan.setGantConfig( iConfig );

      User lUser = new User();
      lUser.setPrimaryKey( new UserKeyImpl( 0 ) );
      iPlan.setCreateUser( lUser );

      Aircraft lAircraft = new Aircraft();
      lAircraft.setReceivedDate( new Date() );

      // lAircraft.setPrimaryKey( new AircraftKeyImpl( 1, 2 ) );
      lAircraft.setName( "PIPPO" );

      lAircraft.setActualRefKey( new IdKeyImpl( 1, 1 ) );

      Assembly lA = new Assembly();
      lA.setActualRefKey( new CdKeyImpl( 1, "111" ) );
      lAircraft.setAssembly( lA );

      ForecastModel lFm = new ForecastModel();
      lFm.setActualRefKey( new IdKeyImpl( 1, 1 ) );
      lAircraft.setForecastModel( lFm );

      iPlan.addAircraft( lAircraft );

      IdKey lPlanKey = iPlanDao.insertPlan( iPlan );
      iPlan.setPrimaryKey( lPlanKey );

      iConfigDao = LrpDaoFactoryImpl.getInstance().getLrpPlanConfigDao();
   }


   /**
    * Attempt to read the configuration.
    *
    * @throws Exception
    *            if an exception occurs
    */
   @Test
   public void testRead() throws Exception {
      testWrite();

      GantConfiguration lConfig = iConfigDao.read( iPlan.getPrimaryKey() );
      assertNotNull( lConfig );
      assertTrue( lConfig.getSeverities().size() != 0 );
      for ( Severity lSeverity : lConfig.getSeverities().values() ) {
         assertNotNull( lSeverity );
      }
   }


   /**
    * Attempt to persist/write the configuration.
    */
   @Test
   public void testWrite() {
      GantConfiguration lConfig = iConfigDao.create( iPlan.getPrimaryKey(), iPlan.getGantConfig() );
      assertNotNull( lConfig );
      assertTrue( lConfig.getSeverities().size() != 0 );
      for ( Severity lSeverity : lConfig.getSeverities().values() ) {
         assertNotNull( lSeverity );
      }
   }


   @Before
   public void loadData() throws Exception {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }
}
