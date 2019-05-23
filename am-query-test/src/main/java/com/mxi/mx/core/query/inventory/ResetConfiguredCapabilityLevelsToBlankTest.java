package com.mxi.mx.core.query.inventory;

import static org.junit.Assert.assertEquals;

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
import com.mxi.mx.core.key.CapabilityLevelKey;


/**
 * This class runs unit tests on ResetConfiguredCapabilityLevelsToBlank query file
 *
 * @author ywang
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class ResetConfiguredCapabilityLevelsToBlankTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            ResetConfiguredCapabilityLevelsToBlankTest.class );
   }


   /**
    *
    * This tests attempts to remove the configured capability levels from aircraft and asserts those
    * capability levels have been removed
    */
   @Test
   public void TestResetConfiguredCapabilityLevelsToBlank() {

      DataSetArgument lArgs = new DataSetArgument();

      // Parameters required by the query.
      AssemblyKey lAssemblyKey = new AssemblyKey( 10, "A320" );
      lArgs.add( lAssemblyKey, new String[] { "aAssmblDbId", "aAssmblCd" } );

      CapabilityKey lCapabilityKey = new CapabilityKey( 10, "ETOPS" );
      CapabilityLevelKey lCapabilityLevelsToBeRemoved =
            new CapabilityLevelKey( 10, "ETOPS120", lCapabilityKey );
      lArgs.addWhereIn(
            new String[] { "config_level_db_id", "config_level_cd", "cap_db_id", "cap_cd" },
            lCapabilityLevelsToBeRemoved );
      // Execute query!
      int lResult = MxDataAccess.getInstance().executeUpdate(
            "com.mxi.mx.core.query.inventory.ResetConfiguredCapabilityLevelsToBlank", lArgs );

      assertEquals( "Number of updated row", 1, lResult );
   }
}
