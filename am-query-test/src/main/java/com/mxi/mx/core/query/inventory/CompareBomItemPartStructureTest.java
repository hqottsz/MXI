package com.mxi.mx.core.query.inventory;

import static org.junit.Assert.assertFalse;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.AssemblyBuilder;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefConfigSlotStatusKey;


/**
 * This class tests the compareBomItemPartStructure query
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class CompareBomItemPartStructureTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @ClassRule
   public static FakeJavaEeDependenciesRule sFakeJavaEeDependenciesRule =
         new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * <p>
    * Data Setup:
    * </p>
    *
    * <ul>
    * <li>Creates two partial assemblies with a very basic structure: ... -> TRK -> TRK</li>
    * <li>One of the assemblies has an obsolete TRK slot below the other TRK slot</li>
    * <li>The other assembly has an active TRK slot as the leaf</li>
    * </ul>
    *
    * <p>
    * Test Case: Ensure that the part structure for the assembly with the active slot is not
    * compatible with the assembly with the obsolete slot.
    * </p>
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testThatCheckFailsWithObsoleteSlotDifference() throws Exception {

      ConfigSlotKey lTrkConfigSlot1 =
            new ConfigSlotBuilder( "TRK1" ).withClass( RefBOMClassKey.TRK )
                  .withRootAssembly( new AssemblyBuilder( "TEST1" ) ).build();
      PartNoKey lTrkPartNo1 = new PartNoBuilder().build();
      new PartGroupDomainBuilder( "TRK1-PG" ).withConfigSlot( lTrkConfigSlot1 )
            .withPartNo( lTrkPartNo1 ).build();

      ConfigSlotKey lTrkConfigSlot11 = new ConfigSlotBuilder( "TRK1.1" )
            .withClass( RefBOMClassKey.TRK ).withParent( lTrkConfigSlot1 )
            .withStatus( RefConfigSlotStatusKey.ACTIVE ).build();
      PartNoKey lTrkPartNo11 = new PartNoBuilder().build();
      new PartGroupDomainBuilder( "TRK1.1-PG" ).withConfigSlot( lTrkConfigSlot11 )
            .withPartNo( lTrkPartNo11 ).build();

      ConfigSlotKey lTrkConfigSlot2 =
            new ConfigSlotBuilder( "TRK2" ).withClass( RefBOMClassKey.TRK )
                  .withRootAssembly( new AssemblyBuilder( "TEST2" ) ).build();
      new PartGroupDomainBuilder( "TRK2-PG" ).withConfigSlot( lTrkConfigSlot2 )
            .withPartNo( lTrkPartNo1 ).build();

      ConfigSlotKey lTrkConfigSlot21 = new ConfigSlotBuilder( "TRK2.1" )
            .withClass( RefBOMClassKey.TRK ).withParent( lTrkConfigSlot2 )
            .withStatus( RefConfigSlotStatusKey.OBSOLETE ).build();
      new PartGroupDomainBuilder( "TRK2.1-PG" ).withConfigSlot( lTrkConfigSlot21 )
            .withPartNo( lTrkPartNo11 ).build();

      DataSet lResults = executeQuery( lTrkConfigSlot1, lTrkConfigSlot2 );

      assertFalse( "Results should contain a row indicating no match", lResults.isEmpty() );
   }


   /**
    * Executes the query being tested and returns the results.
    *
    * @param aOrigConfigSlot
    *           The original config slot
    * @param aNewConfigSlot
    *           The new config slot
    *
    * @return The query results
    */
   private DataSet executeQuery( ConfigSlotKey aOrigConfigSlot, ConfigSlotKey aNewConfigSlot ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aOrigConfigSlot, "aOrigAssmblDbId", "aOrigAssmblCd", "aOrigAssmblBomId" );
      lArgs.add( aNewConfigSlot, "aNewAssmblDbId", "aNewAssmblCd", "aNewAssmblBomId" );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.inventory.compareBomItemPartStructure", lArgs );
   }
}
