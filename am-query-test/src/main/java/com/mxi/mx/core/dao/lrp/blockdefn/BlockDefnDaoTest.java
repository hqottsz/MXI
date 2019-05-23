
package com.mxi.mx.core.dao.lrp.blockdefn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.mxi.am.db.connection.loader.SqlLoader;
import com.mxi.mx.core.dao.ReferenceCache;
import com.mxi.mx.core.dao.lrp.DaoTestCase;
import com.mxi.mx.core.dao.lrp.LrpReferenceCache;
import com.mxi.mx.model.key.CdKey;
import com.mxi.mx.model.key.CdKeyImpl;
import com.mxi.mx.model.key.IdKey;
import com.mxi.mx.model.key.IdKeyImpl;
import com.mxi.mx.model.lrp.Assembly;
import com.mxi.mx.model.lrp.BlockDefinition;
import com.mxi.mx.model.lrp.WorkType;


/**
 * BlockDefnDao test
 *
 * @author slevert
 */
public class BlockDefnDaoTest extends DaoTestCase {

   private static final WorkType HANGAR =
         new WorkType( new CdKeyImpl( 10, "HANGAR" ), "Aircraft hangar visit work", 1 );
   private static final WorkType ADMIN =
         new WorkType( new CdKeyImpl( 10, "ADMIN" ), "Administration work", 10 );
   private static final WorkType AIRCOND =
         new WorkType( new CdKeyImpl( 10, "AIRCOND" ), "Air-conditioning system work", 20 );

   private static final Map<IdKey, Collection<WorkType>> BLOCK_DEFN_WORK_TYPES =
         new HashMap<IdKey, Collection<WorkType>>();

   static {
      BLOCK_DEFN_WORK_TYPES.put( new IdKeyImpl( 4650, 238168 ), Arrays.asList( HANGAR, ADMIN ) );
      BLOCK_DEFN_WORK_TYPES.put( new IdKeyImpl( 4650, 238169 ), Arrays.asList( ADMIN, HANGAR ) );
      BLOCK_DEFN_WORK_TYPES.put( new IdKeyImpl( 4650, 238170 ), Arrays.asList( ADMIN, HANGAR ) );
      BLOCK_DEFN_WORK_TYPES.put( new IdKeyImpl( 4650, 238171 ), Arrays.asList( ADMIN, HANGAR ) );
      BLOCK_DEFN_WORK_TYPES.put( new IdKeyImpl( 4650, 238172 ), Arrays.asList( AIRCOND, HANGAR ) );
      BLOCK_DEFN_WORK_TYPES.put( new IdKeyImpl( 4650, 238183 ), Arrays.asList( ADMIN, HANGAR ) );
      BLOCK_DEFN_WORK_TYPES.put( new IdKeyImpl( 4650, 238173 ), Arrays.asList( AIRCOND, HANGAR ) );
      BLOCK_DEFN_WORK_TYPES.put( new IdKeyImpl( 4650, 238184 ), Arrays.asList( ADMIN, HANGAR ) );
      BLOCK_DEFN_WORK_TYPES.put( new IdKeyImpl( 4650, 238175 ), Arrays.asList( ADMIN, HANGAR ) );
      BLOCK_DEFN_WORK_TYPES.put( new IdKeyImpl( 4650, 238174 ), Arrays.asList( ADMIN, HANGAR ) );
      BLOCK_DEFN_WORK_TYPES.put( new IdKeyImpl( 4650, 238176 ), Arrays.asList( ADMIN, HANGAR ) );
      BLOCK_DEFN_WORK_TYPES.put( new IdKeyImpl( 4650, 238177 ), Arrays.asList( ADMIN, HANGAR ) );
      BLOCK_DEFN_WORK_TYPES.put( new IdKeyImpl( 4650, 238178 ), Arrays.asList( ADMIN, HANGAR ) );
   }

   private BlockDefnDao iDao;


   @Override
   @Before
   public void loadData() throws Exception {
      super.loadData();

      iDao = iFactory.getBlockDefinitionDao();

      LrpReferenceCache lReferenceCache = new LrpReferenceCache();
      ReferenceCache<CdKey, Assembly> lAssemblyCache = lReferenceCache.getAssemblyCache();
      lAssemblyCache.addReferences( getTestAssemblies() );
      iDao.setReferenceCache( lReferenceCache );
   }


   /**
    * We expect to retrieve the correct amount of definitions as mapped in the task_task_dep table.
    * In this scenario, we have 4 blocks and 3 reqs. The blocks form a chain and all of the blocks
    * should be returned. The reqs are recurring and not a dependent of any block and should not be
    * returned.
    *
    * @throws Exception
    */
   @Test
   public void testGetBlockActualDependencies() throws Exception {

      SqlLoader.load( iDatabaseConnectionRule.getConnection(), getClass(),
            "BlockDefnDependencies.sql" );

      List<IdKey> lKeyList = new ArrayList<IdKey>();

      // recurring block chain
      IdKey lBlock1 = new IdKeyImpl( 4650, 1000 );
      lKeyList.add( lBlock1 );

      Collection<BlockDefinition> lDefns = iDao.getBlockActuals( lKeyList );

      assertNotNull( lDefns );

      // We want 4 blocks returned
      assertEquals( 4, lDefns.size() );
   }


   /**
    * Reads aircrafts.<br>
    * Expects one actual and one future aircrafts.
    *
    * @throws Exception
    */
   @Test
   public void testGetBlockActuals() throws Exception {
      List<IdKey> lKeyList = new ArrayList<IdKey>();

      // recurring block chain
      IdKey lRBCKey = new IdKeyImpl( 4650, 238172 );
      lKeyList.add( lRBCKey );

      // non-recurring block chain
      IdKey lNRBCKey = new IdKeyImpl( 4650, 238169 );
      lKeyList.add( lNRBCKey );

      // recurring one-time block
      IdKey lROTBKey = new IdKeyImpl( 4650, 238184 );
      lKeyList.add( lROTBKey );

      // non-recurring one-time block
      IdKey lNROTBKey = new IdKeyImpl( 4650, 238168 );
      lKeyList.add( lNROTBKey );

      // recurring req
      IdKey lRREQKey = new IdKeyImpl( 4650, 238183 );
      lKeyList.add( lRREQKey );

      // non-recurring req
      IdKey lNRREQKey = new IdKeyImpl( 4650, 238175 );
      lKeyList.add( lNRREQKey );

      Collection<BlockDefinition> lDefns = iDao.getBlockActuals( lKeyList );

      assertNotNull( lDefns );
      assertEquals( 9, lDefns.size() );

      List<IdKey> lRetrievedKeyList = new ArrayList<IdKey>();

      for ( BlockDefinition lBlockDefinition : lDefns ) {
         assertEquals( 2, lBlockDefinition.getWorkTypes().size() );

         Collection<WorkType> lWorkTypes = lBlockDefinition.getWorkTypes();
         assertTrue( lWorkTypes
               .containsAll( BLOCK_DEFN_WORK_TYPES.get( lBlockDefinition.getActualRefKey() ) ) );

         assertNotNull( lBlockDefinition.getActualRefKey() );
         lRetrievedKeyList.add( lBlockDefinition.getActualRefKey() );
      }

      // make sure the list of defn keys is in the list of retrieved defns
      assertTrue( lRetrievedKeyList.containsAll( lKeyList ) );
   }


   /**
    * We expect to retrieve the correct amount of definitions as mapped in the task_task_dep table.
    * In this scenario, we want 6 requirements. 1 is a mod and 5 are reqs.
    *
    * @throws Exception
    */
   @Test
   public void testGetRequirementTree() throws Exception {
      SqlLoader.load( iDatabaseConnectionRule.getConnection(), getClass(), "ReqDefnTree.sql" );

      List<IdKey> lKeyList = new ArrayList<IdKey>();

      // recurring block chain
      IdKey lBlock1 = new IdKeyImpl( 4650, 1000 );
      lKeyList.add( lBlock1 );

      Collection<BlockDefinition> lDefns = iDao.getBlockActuals( lKeyList );

      assertNotNull( lDefns );
      assertEquals( 6, lDefns.size() );
   }


   /**
    * Reads block definitions.<br>
    * Tests that with multiple scheduling rule the correct number of block definition returned is
    * correct.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testRead() throws Exception {

      // execute script to insert data
      SqlLoader.load( iDatabaseConnectionRule.getConnection(), getClass(),
            "CreateTestLrpTaskDefn.sql" );

      // calls test method
      Collection<BlockDefinition> lBlockDefns = iDao.readAll( new IdKeyImpl( 555, 1 ) );

      // assert total block definitions returned
      assertEquals( 2, lBlockDefns.size() );

      // iterate collection of block definitions
      for ( BlockDefinition lBlockDefn : lBlockDefns ) {

         // assert size of scheduling rules for first task definition
         if ( lBlockDefn.getActualRefKey().equals( new IdKeyImpl( 777, 1 ) ) ) {
            assertEquals( 2, lBlockDefn.getScheduleRules().size() );
         }
         // assert size of scheduling rules for second task definition
         else if ( lBlockDefn.getActualRefKey().equals( new IdKeyImpl( 777, 2 ) ) ) {
            assertEquals( 3, lBlockDefn.getScheduleRules().size() );
         }

         // test for default value
         assertFalse( lBlockDefn.getReadOnly() );
      }
   }


   /**
    * We expect to retrieve the correct amount of definitions as mapped in the task_task_dep table.
    * In this scenario, we want 2 requirements. 1 is a mod and 1 is a recurring req.
    *
    * @throws Exception
    */
   @Test
   public void testRequirementDependencies() throws Exception {

      SqlLoader.load( iDatabaseConnectionRule.getConnection(), getClass(),
            "ReqDefnDependencies.sql" );

      List<IdKey> lKeyList = new ArrayList<IdKey>();

      // recurring block chain
      IdKey lBlock1 = new IdKeyImpl( 4650, 1000 );
      lKeyList.add( lBlock1 );

      Collection<BlockDefinition> lDefns = iDao.getBlockActuals( lKeyList );

      assertNotNull( lDefns );
      assertEquals( 2, lDefns.size() );
   }


   private Collection<Assembly> getTestAssemblies() {
      Collection<Assembly> lAssemblies = new ArrayList<Assembly>();

      Assembly lAssembly = new Assembly();
      lAssembly.setActualRefKey( new CdKeyImpl( 8000000, "B767-200" ) );
      lAssemblies.add( lAssembly );

      return lAssemblies;
   }
}
