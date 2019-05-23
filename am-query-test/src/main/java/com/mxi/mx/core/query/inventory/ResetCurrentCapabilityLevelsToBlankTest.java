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
 * This class runs unit tests on ResetCurrentCapabilityLevelsToBlank query file
 *
 * @author ywang
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class ResetCurrentCapabilityLevelsToBlankTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            ResetCurrentCapabilityLevelsToBlankTest.class );
   }


   /**
    *
    * This tests attempts to remove the current capability levels from aircraft and asserts those
    * capability levels have been removed
    */
   @Test
   public void TestResetCurrentCapabilityLevelsToBlank() {

      DataSetArgument lArgs = new DataSetArgument();

      // Parameters required by the query.
      AssemblyKey lAssemblyKey = new AssemblyKey( 100, "A320" );
      lArgs.add( lAssemblyKey, new String[] { "aAssmblDbId", "aAssmblCd" } );

      CapabilityKey lCapabilityKey = new CapabilityKey( 100, "ETOPS" );
      CapabilityLevelKey lCapabilityLevelsToBeRemoved =
            new CapabilityLevelKey( 100, "ETOPS120", lCapabilityKey );
      lArgs.addWhereIn( "WHERE_CURRENT_LEVEL_IN",
            new String[] { "level_db_id", "level_cd", "cap_db_id", "cap_cd" },
            lCapabilityLevelsToBeRemoved );
      lArgs.addWhere( "WHERE_CONFIG_LEVEL_IN", "1=1" );

      // Execute query!
      int lResult = MxDataAccess.getInstance().executeUpdate(
            "com.mxi.mx.core.query.inventory.ResetCurrentCapabilityLevelsToBlank", lArgs );

      assertEquals( "Number of updated row", 1, lResult );
   }

}
