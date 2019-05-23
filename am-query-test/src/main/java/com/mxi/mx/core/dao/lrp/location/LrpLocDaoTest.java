
package com.mxi.mx.core.dao.lrp.location;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.mxi.am.db.connection.loader.SqlLoader;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.dao.ReferenceCache;
import com.mxi.mx.core.dao.lrp.DaoTestCase;
import com.mxi.mx.core.dao.lrp.LrpReferenceCache;
import com.mxi.mx.model.key.CdKey;
import com.mxi.mx.model.key.CdKeyImpl;
import com.mxi.mx.model.key.IdKey;
import com.mxi.mx.model.key.IdKeyImpl;
import com.mxi.mx.model.lrp.Assembly;
import com.mxi.mx.model.lrp.Capability;
import com.mxi.mx.model.lrp.Location;
import com.mxi.mx.model.lrp.Priority;
import com.mxi.mx.model.lrp.WorkType;


/**
 * DOCUMENT_ME
 *
 * @author yvakulenko
 */
public class LrpLocDaoTest extends DaoTestCase {

   private static final IdKey ACTUAL_LOC_REF = new IdKeyImpl( LRP_TEST_DB_ID, 6 );

   LrpLocDao iLrpLocDao;


   @Override
   @Before
   public void loadData() throws Exception {
      iLrpLocDao = iFactory.getLrpLocDao( new IdKeyImpl( LRP_TEST_DB_ID, TEST_LRP_ID ) );

      LrpReferenceCache lReferenceCache = new LrpReferenceCache();
      ReferenceCache<CdKey, Assembly> lAssemblyCache = lReferenceCache.getAssemblyCache();
      lAssemblyCache.addReferences( getTestAssemblies() );
      iLrpLocDao.setReferenceCache( lReferenceCache );
   }


   /**
    * Test Create operation.<br>
    * 1. Construct object<br>
    * 2. Write it into DB<br>
    * 3. Read data from DB and assert
    */
   @Test
   public void testCreate() {
      Capability lCapability = new Capability();
      Assembly lAssembly = new Assembly();
      lAssembly.setActualRefKey( new CdKeyImpl( 777, "ASSMBL_1" ) );
      lCapability.setAssembly( lAssembly );

      Priority lPriority = new Priority();
      lPriority.setPrimaryKey( new CdKeyImpl( 777, "MED" ) );
      lCapability.setPriority( lPriority );

      WorkType lWorkType = new WorkType( new CdKeyImpl( 0, "TURN" ), "TURN", 0 );
      lCapability.setWorkType( lWorkType );

      List<Capability> lCapabilities = new ArrayList<Capability>();

      // Construct a Location model object
      Location lLocation = new Location();
      lLocation.setActualRefKey( ACTUAL_LOC_REF );
      lLocation.setLabourRate( 2.0f );
      lLocation.setDefaultCapacity( 1 );
      lLocation.setCapabilities( lCapabilities );
      lLocation.setAdHocEvtCtrl( true );

      Collection<Location> lLocations = new ArrayList<Location>();
      lLocations.add( lLocation );

      // Create a record using DAO
      iLrpLocDao.createAll( lLocations );

      // Fetch records
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aLrpDbId", LRP_TEST_DB_ID );
      lArgs.add( "aLrpId", TEST_LRP_ID );

      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.core.dao.lrp.location.GetLrpLoc", lArgs );
      lQs.beforeFirst();
      assertEquals( 1, lQs.getRowCount() ); // assert number of record
      lQs.next(); // we expect only 1 record, so get it

      // assert data
      assertEquals( ACTUAL_LOC_REF,
            new IdKeyImpl( lQs.getInt( "LOC_DB_ID" ), lQs.getInt( "LOC_ID" ) ) );
      assertEquals( 2.0f, lQs.getFloat( "LABOR_RATE" ), 0f );
      assertEquals( 1, lQs.getInt( "DEFAULT_CAPACITY" ) );

      assertTrue( lQs.getBoolean( "AH_EVT_CTRL_BOOL" ) );
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
            "CreateLrpLocDaoTest.sql" );

      iLrpLocDao.deleteAll( new IdKeyImpl( LRP_TEST_DB_ID, TEST_LRP_ID ) );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aLrpDbId", LRP_TEST_DB_ID );
      lArgs.add( "aLrpId", TEST_LRP_ID );

      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.core.dao.lrp.location.GetLrpLoc", lArgs );
      assertEquals( 0, lQs.getRowCount() ); // assert number of record
   }


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testRead() throws Exception {
      SqlLoader.load( iDatabaseConnectionRule.getConnection(), getClass(),
            "CreateLrpLocDaoTest.sql" );

      Object[] lLocations = iLrpLocDao.readAll().toArray();
      assertEquals( 1, lLocations.length ); // assert number of record

      Location lLocation = ( Location ) lLocations[0];

      // Test entity's data
      assertEquals( ACTUAL_LOC_REF, lLocation.getActualRefKey() );
      assertEquals( 2.0f, lLocation.getLabourRate(), 0f );
      assertEquals( 1, lLocation.getDefaultCapacity() );
      assertEquals( "CA/OTT/LN/VLINE/VTRK (VENDOR TRACK)", lLocation.getFullName() );
      assertEquals( "CA/OTT/LN/VLINE/VTRK", lLocation.getLocationCode() );
      assertEquals( "VENDOR TRACK", lLocation.getName() );
      assertEquals( "VENTRK (Vendor Track)", lLocation.getType() );
      assertTrue( lLocation.getAdHocEvtCtrl() );

      List<Capability> lCapabilities = lLocation.getCapabilities();
      assertEquals( 1, lCapabilities.size() );
   }


   /**
    * Aircraft DAO requires already loaded assemblies, to establish references to.
    *
    * @return Collection of assemblies.
    */
   private Collection<Assembly> getTestAssemblies() {
      Collection<Assembly> lAssemblies = new ArrayList<Assembly>();

      Assembly lAssembly = new Assembly();
      lAssembly.setActualRefKey( new CdKeyImpl( 777, "ASSMBL_1" ) );
      lAssemblies.add( lAssembly );

      return lAssemblies;
   }
}
