package com.mxi.mx.repository.assembly;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.CapabilityLevelKey;
import com.mxi.mx.core.key.RefSensitivityKey;
import com.mxi.mx.domain.assembly.Assembly;


@RunWith( MockitoJUnitRunner.class )
public class JdbcAssemblyRepositoryTest {

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   // Test Data
   private static final AssemblyKey ASSEMBLY_KEY = new AssemblyKey( "4650:ASSY" );
   private static final RefSensitivityKey CAT_III_ACTIVE = new RefSensitivityKey( "CAT_III" );
   private static final RefSensitivityKey ETOPS_ACTIVE = new RefSensitivityKey( "ETOPS" );
   private static final RefSensitivityKey RII_ACTIVE = new RefSensitivityKey( "RII" );
   private static final RefSensitivityKey RVSM_INACTIVE = new RefSensitivityKey( "RVSM" );
   private static final Assembly ASSEMBLY = Assembly.builder().key( ASSEMBLY_KEY )
         .sensitivities( new ArrayList<RefSensitivityKey>( Arrays.asList( CAT_III_ACTIVE ) ) )
         .capabilityLevels( Collections.<CapabilityLevelKey>emptyList() ).build();

   // Object under test
   private AssemblyRepository iRepository;


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      iRepository = new JdbcAssemblyRepository();
   }


   @Test( expected = NullPointerException.class )
   public void get_byKey_null() {
      iRepository.get( ( AssemblyKey ) null );
   }


   @Test
   public void get_byKey() {
      Assembly lAssembly = iRepository.get( ASSEMBLY_KEY );
      assertEquals( ASSEMBLY, lAssembly );
   }


   @Test( expected = UnsupportedOperationException.class )
   public void get_byKeys() {
      iRepository.get( Collections.<AssemblyKey>emptySet() );
   }


   @Test( expected = NullPointerException.class )
   public void update_null() {
      iRepository.update( null );
   }


   @Test
   public void update_addsAndRemovesActiveSensitivitiesAndPreservesInactiveSensitivities() {

      Assembly lModifiedAssembly = Assembly.builder( ASSEMBLY )
            .sensitivities( Arrays.asList( ETOPS_ACTIVE, RII_ACTIVE ) ).build();

      iRepository.update( lModifiedAssembly );

      assertTrue( isSensitivityAssigned( ASSEMBLY_KEY, ETOPS_ACTIVE ) );
      assertTrue( isSensitivityAssigned( ASSEMBLY_KEY, RII_ACTIVE ) );
      assertTrue( isSensitivityAssigned( ASSEMBLY_KEY, RVSM_INACTIVE ) );
      assertFalse( isSensitivityAssigned( ASSEMBLY_KEY, CAT_III_ACTIVE ) );
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
