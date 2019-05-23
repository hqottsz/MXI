package com.mxi.mx.repository.capability;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.key.CapabilityKey;
import com.mxi.mx.core.key.CapabilityLevelKey;
import com.mxi.mx.core.key.RefSensitivityKey;
import com.mxi.mx.domain.capability.Capability;
import com.mxi.mx.domain.capability.CapabilityLevel;


@RunWith( BlockJUnit4ClassRunner.class )
public class JdbcCapabilityLevelRepositoryTest {

   // Test Data
   private static final Capability CAP_1 = Capability.builder()
         .key( new CapabilityKey( "0:CAP_1" ) ).name( "CAP 1" ).order( 1 ).build();

   private static final CapabilityLevel CAP_LEVEL_A = CapabilityLevel.builder().capability( CAP_1 )
         .name( "Level A" ).key( new CapabilityLevelKey( "0:LEVEL_A:0:CAP_1" ) ).order( 1 )
         .sensitivityKey( null ).build();

   private static final CapabilityLevel CAP_LEVEL_B = CapabilityLevel.builder().capability( CAP_1 )
         .name( "Level B" ).key( new CapabilityLevelKey( "0:LEVEL_B:0:CAP_1" ) ).order( 2 )
         .sensitivityKey( RefSensitivityKey.ETOPS ).build();

   private static final CapabilityLevelKey NON_EXISTING_CAP_LEVEL_KEY =
         new CapabilityLevelKey( "0:FAKE_1:0:FAKE" );

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   // Object under test
   private CapabilityLevelRepository iRepository;


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      iRepository = new JdbcCapabilityLevelRepository();
   }


   @Test( expected = NullPointerException.class )
   public void get_byKeys_null() {
      iRepository.get( ( Set<CapabilityLevelKey> ) null );
   }


   @Test( expected = RuntimeException.class )
   public void get_byKeys_missingRecord() {
      iRepository.get( new HashSet<CapabilityLevelKey>(
            Arrays.asList( NON_EXISTING_CAP_LEVEL_KEY, CAP_LEVEL_A.getKey() ) ) );
   }


   @Test
   public void get_byKeys_emptyList() {
      List<CapabilityLevel> lCapabilityLevels =
            iRepository.get( Collections.<CapabilityLevelKey>emptySet() );
      assertTrue( lCapabilityLevels.isEmpty() );
   }


   @Test
   public void get_byKeys() {
      List<CapabilityLevel> lCapabilityLevels = iRepository.get( new HashSet<CapabilityLevelKey>(
            Arrays.asList( CAP_LEVEL_A.getKey(), CAP_LEVEL_B.getKey() ) ) );
      assertEquals( 2, lCapabilityLevels.size() );
      assertEquals( CAP_LEVEL_A, lCapabilityLevels.get( 0 ) );
      assertEquals( CAP_LEVEL_B, lCapabilityLevels.get( 1 ) );
   }

}
