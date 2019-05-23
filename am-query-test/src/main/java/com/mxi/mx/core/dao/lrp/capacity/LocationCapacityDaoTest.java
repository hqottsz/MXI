
package com.mxi.mx.core.dao.lrp.capacity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.mxi.am.db.connection.loader.SqlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.dao.lrp.DaoTestCase;
import com.mxi.mx.core.dao.lrp.locationcapacity.LocationCapacityDao;
import com.mxi.mx.model.key.IdKey;
import com.mxi.mx.model.key.IdKeyImpl;
import com.mxi.mx.model.lrp.CapacityPattern;
import com.mxi.mx.model.lrp.LrpLocCapacity;
import com.mxi.mx.model.lrp.LrpLocCapacityExcept;
import com.mxi.mx.model.lrp.LrpLocCapacityStd;
import com.mxi.mx.model.lrp.key.LrpLocCapacityExceptKey;
import com.mxi.mx.model.lrp.key.LrpLocCapacityExceptKeyImp;
import com.mxi.mx.model.lrp.key.LrpLocCapacityKey;
import com.mxi.mx.model.lrp.key.LrpLocCapacityKeyImp;
import com.mxi.mx.model.lrp.key.LrpLocCapacityStdKey;
import com.mxi.mx.model.lrp.key.LrpLocCapacityStdKeyImp;
import com.mxi.mx.model.lrp.key.LrpLocKey;
import com.mxi.mx.model.lrp.key.LrpLocKeyImpl;


/**
 * Location Capacity Model Dao Tests to ensure we can retrieve data using the various API methods.
 *
 * @author sdeshmukh
 */
public class LocationCapacityDaoTest extends DaoTestCase {

   private LocationCapacityDao iDao;


   @Override
   @Before
   public void loadData() throws Exception {
      iDao = iFactory.getLocationCapacityDao(
            new LrpLocKeyImpl( new IdKeyImpl( 777, 1 ), new IdKeyImpl( 4650, 100013 ) ) );
   }


   /**
    * Test to create location capacity.<br>
    * Expects one in use location capacity.
    *
    * @throws Exception
    *            if any exception occurs.
    */
   @Test
   public void testcreateLocationCapacityAll() throws Exception {
      Collection<LrpLocCapacity> lLrpLocCapacities = new ArrayList<LrpLocCapacity>();
      IdKey lPlanKey = new IdKeyImpl( 777, 1 );
      IdKey lLocKey = new IdKeyImpl( 4650, 100013 );
      IdKey lCapacityKey = new IdKeyImpl( 4650, 1 );

      LrpLocCapacityKey lLrpLocCapacityKey =
            new LrpLocCapacityKeyImp( lPlanKey, lLocKey, lCapacityKey );

      LrpLocCapacity lLrpLocCapacity = new LrpLocCapacity();
      lLrpLocCapacity.createPrimaryKey( lLrpLocCapacityKey );
      lLrpLocCapacity.setCode( "CP001" );
      lLrpLocCapacity.setDescription( "Winter Pattern1" );

      lLrpLocCapacities.add( lLrpLocCapacity );

      iDao.createLocationCapacityAll( lLrpLocCapacities );

      // Fetch records
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aLrpDbId", LRP_TEST_DB_ID );
      lArgs.add( "aLrpId", TEST_LRP_ID );
      lArgs.add( "aLocDbId", LOC_DB_ID );
      lArgs.add( "aLocId", LOC_ID );
      lArgs.add( "aCapacityDbIdId", CAPACITY_PATTERN_DB_ID );
      lArgs.add( "aCapacityId", CAPACITY_PATTERN_ID );

      DataSet lDs = MxDataAccess.getInstance()
            .executeQuery( "com.mxi.mx.core.dao.lrp.capacity.GetLocationCapacities", lArgs );
      lDs.beforeFirst();
      assertEquals( 1, lDs.getTotalRowCount() ); // assert number of record
      lDs.next(); // we expect only 1 record, so get it

      // assert data
      assertEquals( lCapacityKey, new IdKeyImpl( lDs.getInt( "CAPACITY_PATTERN_DB_ID" ),
            lDs.getInt( "CAPACITY_PATTERN_ID" ) ) );
   }


   /**
    * Test to create exception location capacity.<br>
    *
    * @throws Exception
    *            if any exception occurs.
    */
   @Test
   public void testcreateLocationCapacityExceptAll() throws Exception {
      Collection<LrpLocCapacityExcept> lLrpLocCapacityExcepts =
            new ArrayList<LrpLocCapacityExcept>();
      IdKey lPlanKey = new IdKeyImpl( 777, 1 );
      IdKey lLocKey = new IdKeyImpl( 4650, 100013 );
      IdKey lCapacityKey = new IdKeyImpl( 4650, 1 );
      int lCapacityExcept = 1;

      LrpLocCapacityExceptKey lLrpLocCapacityExceptKey =
            new LrpLocCapacityExceptKeyImp( lPlanKey, lLocKey, lCapacityKey, lCapacityExcept );

      LrpLocCapacityExcept lLrpLocCapacityExcept = new LrpLocCapacityExcept();

      lLrpLocCapacityExcept.createPrimaryKey( lLrpLocCapacityExceptKey );

      lLrpLocCapacityExcepts.add( lLrpLocCapacityExcept );

      iDao.createLocationCapacityExceptAll( lLrpLocCapacityExcepts );

      // Fetch records
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aLrpDbId", LRP_TEST_DB_ID );
      lArgs.add( "aLrpId", TEST_LRP_ID );
      lArgs.add( "aLocDbId", LOC_DB_ID );
      lArgs.add( "aLocId", LOC_ID );
      lArgs.add( "aCapacityDbIdId", CAPACITY_PATTERN_DB_ID );
      lArgs.add( "aCapacityId", CAPACITY_PATTERN_ID );
      lArgs.add( "aCapacityExceptId", CAPACITY_EXCEPT_ID );

      DataSet lDs = MxDataAccess.getInstance()
            .executeQuery( "com.mxi.mx.core.dao.lrp.capacity.GetExceptLocationCapacities", lArgs );
      lDs.beforeFirst();
      assertEquals( 1, lDs.getTotalRowCount() ); // assert number of record
      lDs.next(); // we expect only 1 record, so get it

      // assert data
      assertEquals( lCapacityKey, new IdKeyImpl( lDs.getInt( "CAPACITY_PATTERN_DB_ID" ),
            lDs.getInt( "CAPACITY_PATTERN_ID" ) ) );
   }


   /**
    * Test to create standard location capacity.<br>
    *
    * @throws Exception
    *            if any exception occurs.
    */
   @Test
   public void testcreateLocationCapacityStdAll() throws Exception {
      Collection<LrpLocCapacityStd> lLrpLocCapacityStds = new ArrayList<LrpLocCapacityStd>();
      IdKey lPlanKey = new IdKeyImpl( 777, 1 );
      IdKey lLocKey = new IdKeyImpl( 4650, 100013 );
      IdKey lCapacityKey = new IdKeyImpl( 4650, 1 );
      int lCapacityStd = 1;

      LrpLocCapacityStdKey lLrpLocCapacityStdKey =
            new LrpLocCapacityStdKeyImp( lPlanKey, lLocKey, lCapacityKey, lCapacityStd );

      LrpLocCapacityStd lLrpLocCapacityStd = new LrpLocCapacityStd();

      lLrpLocCapacityStd.createPrimaryKey( lLrpLocCapacityStdKey );

      lLrpLocCapacityStds.add( lLrpLocCapacityStd );

      iDao.createLocationCapacityStdAll( lLrpLocCapacityStds );

      // Fetch records
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aLrpDbId", LRP_TEST_DB_ID );
      lArgs.add( "aLrpId", TEST_LRP_ID );
      lArgs.add( "aLocDbId", LOC_DB_ID );
      lArgs.add( "aLocId", LOC_ID );
      lArgs.add( "aCapacityDbIdId", CAPACITY_PATTERN_DB_ID );
      lArgs.add( "aCapacityId", CAPACITY_PATTERN_ID );
      lArgs.add( "aCapacityStdId", CAPACITY_STD_ID );

      DataSet lDs = MxDataAccess.getInstance()
            .executeQuery( "com.mxi.mx.core.dao.lrp.capacity.GetStdLocationCapacities", lArgs );
      lDs.beforeFirst();
      assertEquals( 1, lDs.getTotalRowCount() ); // assert number of record
      lDs.next(); // we expect only 1 record, so get it

      // assert data
      assertEquals( lCapacityKey, new IdKeyImpl( lDs.getInt( "CAPACITY_PATTERN_DB_ID" ),
            lDs.getInt( "CAPACITY_PATTERN_ID" ) ) );
   }


   /**
    * Reads standard location capacities.<br>
    * Expects two standard location capacity.
    *
    * @throws Exception
    *            if any exception occurs.
    */
   @Test
   public void testGeStandardLocationCapacities() throws Exception {
      SqlLoader.load( iDatabaseConnectionRule.getConnection(), getClass(),
            "CreateStandardLocationCapacities.sql" );

      IdKey lPlanKey = new IdKeyImpl( 4650, 1 );
      IdKey lLocKey = new IdKeyImpl( 4650, 100013 );

      LrpLocKey lLrpLocKey = new LrpLocKeyImpl( lPlanKey, lLocKey );

      Collection<LrpLocCapacityStd> lLrpLocCapacityStds =
            iDao.getStandardLocationCapacities( lLrpLocKey );

      for ( LrpLocCapacityStd lLrpLocCapacityStd : lLrpLocCapacityStds ) {

         assertNotNull( "Standard Location Capacity is null", lLrpLocCapacityStd );
      }

      assertEquals( 2, lLrpLocCapacityStds.size() );
   }


   /**
    * Reads exception location capacities.<br>
    * Expects two exception location capacity.
    *
    * @throws Exception
    *            if any exception occurs.
    */
   @Test
   public void testGetExceptionLocationCapacities() throws Exception {
      SqlLoader.load( iDatabaseConnectionRule.getConnection(), getClass(),
            "CreateExceptLocationCapacities.sql" );

      IdKey lPlanKey = new IdKeyImpl( 4650, 1 );
      IdKey lLocKey = new IdKeyImpl( 4650, 100013 );

      LrpLocKey lLrpLocKey = new LrpLocKeyImpl( lPlanKey, lLocKey );

      Collection<LrpLocCapacityExcept> lLrpLocCapacityExcepts =
            iDao.getExceptionLocationCapacities( lLrpLocKey );

      for ( LrpLocCapacityExcept lLrpLocCapacityExcept : lLrpLocCapacityExcepts ) {

         assertNotNull( "Except Location Capacity is null", lLrpLocCapacityExcept );
      }

      assertEquals( 2, lLrpLocCapacityExcepts.size() );
   }


   /**
    * Reads in use location capacities.<br>
    * Expects one in use location capacity.
    *
    * @throws Exception
    *            if any exception occurs.
    */
   @Test
   public void testGetInUseLocationCapacities() throws Exception {
      SqlLoader.load( iDatabaseConnectionRule.getConnection(), getClass(),
            "CreateLocationCapacities.sql" );

      IdKey lPlanKey = new IdKeyImpl( 4650, 1 );
      IdKey lLocKey = new IdKeyImpl( 4650, 100013 );

      LrpLocKey lLrpLocKey = new LrpLocKeyImpl( lPlanKey, lLocKey );

      Collection<LrpLocCapacity> lLrpLocCapacities = iDao.getInUseLocationCapacities( lLrpLocKey );

      for ( LrpLocCapacity lLrpLocCapacity : lLrpLocCapacities ) {

         assertNotNull( "LrpLocCapacity is null", lLrpLocCapacity );
         assertEquals( "CP001", lLrpLocCapacity.getCode() );
      }

      assertEquals( 1, lLrpLocCapacities.size() );
   }


   /**
    * Reads not in use location capacities.<br>
    * Expects one not in use location capacity.
    *
    * @throws Exception
    *            if any exception occurs.
    */
   @Test
   public void testGetNotInUseLocationCapacities() throws Exception {
      SqlLoader.load( iDatabaseConnectionRule.getConnection(), getClass(),
            "CreateLocationCapacities.sql" );

      IdKey lPlanKey = new IdKeyImpl( 4650, 1 );
      IdKey lLocKey = new IdKeyImpl( 4650, 100013 );

      LrpLocKey lLrpLocKey = new LrpLocKeyImpl( lPlanKey, lLocKey );

      Collection<CapacityPattern> lCapacityPatterns = iDao.getNotInUseCapacities( lLrpLocKey );

      for ( CapacityPattern lCapacityPattern : lCapacityPatterns ) {

         assertNotNull( "Capacity is null", lCapacityPattern );
         assertEquals( "CP002", lCapacityPattern.getCode() );
      }

      assertEquals( 1, lCapacityPatterns.size() );
   }
}
