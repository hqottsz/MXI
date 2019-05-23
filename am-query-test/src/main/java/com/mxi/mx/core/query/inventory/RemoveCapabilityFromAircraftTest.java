package com.mxi.mx.core.query.inventory;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.CapabilityKey;


/**
 * This class runs unit tests on RemoveCapabilityFromAircraft query file
 *
 * @author ywang
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class RemoveCapabilityFromAircraftTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            RemoveCapabilityFromAircraftTest.class );
   }


   /**
    * This tests attempts to remove capabilities from aircraft and asserts those capabilities have
    * been removed
    */
   @Test
   public void testRemoveCapabilityFromAircraft() {

      DataSetArgument lArgs = new DataSetArgument();

      // Parameters required by the query.
      AssemblyKey lAssemblyKey = new AssemblyKey( 10, "A320" );
      lArgs.add( lAssemblyKey, new String[] { "aAssmblDbId", "aAssmblCd" } );
      List<CapabilityKey> lCapabilitiesToBeRemoved = new ArrayList<CapabilityKey>();
      CapabilityKey lCapabilityKey = new CapabilityKey( 100, "WIFI" );
      lCapabilitiesToBeRemoved.add( lCapabilityKey );
      lArgs.addWhereIn( new String[] { "cap_db_id", "cap_cd" }, lCapabilitiesToBeRemoved );
      // Execute query!
      int lResult = MxDataAccess.getInstance()
            .executeUpdate( "com.mxi.mx.core.query.inventory.RemoveCapabilityFromAircraft", lArgs );

      assertEquals( "Number of deleted rows", 1, lResult );
   }
}
