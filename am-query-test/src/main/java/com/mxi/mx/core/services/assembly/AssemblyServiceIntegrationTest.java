package com.mxi.mx.core.services.assembly;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.RefSensitivityKey;
import com.mxi.mx.core.services.bom.sensitivity.exception.SensitivitiesNotInSyncException;
import com.mxi.mx.core.services.sensitivity.model.SensitivityConfigurationTO;


@RunWith( BlockJUnit4ClassRunner.class )
public class AssemblyServiceIntegrationTest {

   @Rule
   public final FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule =
         new FakeJavaEeDependenciesRule();

   @Rule
   public final DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   /* Test data */
   private static final AssemblyKey ASSEMBLY_KEY = new AssemblyKey( "4650:ASSY" );
   private static final RefSensitivityKey CAT_III_ACTIVE = new RefSensitivityKey( "CAT_III" );
   private static final RefSensitivityKey ETOPS_ACTIVE = new RefSensitivityKey( "ETOPS" );
   private static final RefSensitivityKey RVSM_INACTIVE = new RefSensitivityKey( "RVSM" );

   /* Object under test */
   private AssemblyService iService;


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      iService = new AssemblyService();
   }


   @Test( expected = SensitivitiesNotInSyncException.class )
   public void assignSensitivites_outOfSync_sameCountWrongSensitivities() throws Throwable {
      // Assignable sensitivities: CAT_III, ETOPS
      // Configured sensitivities: CAT_III, RVSM

      iService.assignSensitivities( ASSEMBLY_KEY,
            Arrays.asList( new SensitivityConfigurationTO( CAT_III_ACTIVE, true ),
                  new SensitivityConfigurationTO( RVSM_INACTIVE, false ) ) );
   }


   @Test( expected = SensitivitiesNotInSyncException.class )
   public void assignSensitivites_outOfSync_wrongCount() throws Throwable {
      // Assignable sensitivities: CAT_III, ETOPS
      // Configured sensitivities: CAT_III, ETOPS, RVSM

      iService.assignSensitivities( ASSEMBLY_KEY,
            Arrays.asList( new SensitivityConfigurationTO( CAT_III_ACTIVE, true ),
                  new SensitivityConfigurationTO( ETOPS_ACTIVE, false ),
                  new SensitivityConfigurationTO( RVSM_INACTIVE, false ) ) );
   }


   @Test
   public void assignSensitivities_preservesInactiveSensitivityAssignments() throws Throwable {
      // RVSM is assigned to the assembly but is globally inactive
      // CAT_III is assigned to the assembly but is globally active
      // ETOPS is not assigned to the assembly but is globally active

      iService.assignSensitivities( ASSEMBLY_KEY,
            Arrays.asList( new SensitivityConfigurationTO( CAT_III_ACTIVE, false ),
                  new SensitivityConfigurationTO( ETOPS_ACTIVE, true ) ) );

      // Configured assignments should be saved
      assertFalse( isSensitivityAssigned( ASSEMBLY_KEY, CAT_III_ACTIVE ) );
      assertTrue( isSensitivityAssigned( ASSEMBLY_KEY, ETOPS_ACTIVE ) );

      // Inactive sensitivity assignment should remain in tact
      assertTrue( isSensitivityAssigned( ASSEMBLY_KEY, RVSM_INACTIVE ) );
   }


   private boolean isSensitivityAssigned( AssemblyKey aAssemblyKey,
         RefSensitivityKey aSensitivityKey ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aAssemblyKey, "assmbl_db_id", "assmbl_cd" );
      lArgs.add( aSensitivityKey, "sensitivity_cd" );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "eqp_assmbl_sens", lArgs );
      return lQs.first();
   }
}
