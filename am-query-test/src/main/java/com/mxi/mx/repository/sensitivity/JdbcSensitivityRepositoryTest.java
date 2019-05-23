package com.mxi.mx.repository.sensitivity;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.key.RefSensitivityKey;
import com.mxi.mx.core.services.sensitivity.model.SensitivitySearchCriteria;
import com.mxi.mx.domain.sensitivity.Sensitivity;


public class JdbcSensitivityRepositoryTest {

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   // Test Data
   private static final RefSensitivityKey INVALID_KEY_1 = new RefSensitivityKey( "INVALID" );
   private static final RefSensitivityKey INVALID_KEY_2 = new RefSensitivityKey( "INVALID2" );
   private static final RefSensitivityKey VALID_KEY_1 = new RefSensitivityKey( "ETOPS" );
   private static final RefSensitivityKey VALID_KEY_2 = new RefSensitivityKey( "CAT_III" );

   private static final Sensitivity VALID_SENSITIVITY_1 =
         Sensitivity.builder().code( VALID_KEY_1.toString() ).name( "1" ).active( true )
               .warning( "warn 1" ).order( 10 ).description( "Sensitivity One" ).build();

   private static final Sensitivity VALID_SENSITIVITY_2 =
         Sensitivity.builder().code( VALID_KEY_2.toString() ).name( "2" ).active( false )
               .warning( "warn 2" ).order( 20 ).description( "Sensitivity Two" ).build();

   private static final Set<RefSensitivityKey> INVALID_KEYS =
         new HashSet<>( Arrays.asList( INVALID_KEY_1, INVALID_KEY_2 ) );

   private static final Set<RefSensitivityKey> VALID_KEYS =
         new HashSet<>( Arrays.asList( VALID_KEY_1, VALID_KEY_2 ) );

   private static final Set<RefSensitivityKey> VALID_AND_INVALID_KEYS =
         new HashSet<>( Arrays.asList( VALID_KEY_1, INVALID_KEY_1 ) );

   private static final List<Sensitivity> VALID_SENSITIVITIES =
         Arrays.asList( VALID_SENSITIVITY_1, VALID_SENSITIVITY_2 );

   // Object Under Test
   private SensitivityRepository iRepository;


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      iRepository = new JdbcSensitivityRepository();
   }


   /*
    * Get By Key Tests
    */

   @Test( expected = NullPointerException.class )
   public void getByKey_null() {
      iRepository.get( ( RefSensitivityKey ) null );
   }


   @Test( expected = RuntimeException.class )
   public void getByKey_invalidKey() {
      iRepository.get( INVALID_KEY_1 );
   }


   @Test
   public void getByKey_valid() {
      assertEquals( VALID_SENSITIVITY_1, iRepository.get( VALID_KEY_1 ) );
   }


   /*
    * Get By Keys Tests
    */

   @Test( expected = NullPointerException.class )
   public void getByKeys_null() {
      iRepository.get( ( Set<RefSensitivityKey> ) null );
   }


   @Test( expected = RuntimeException.class )
   public void getByKeys_invalidKeys() {
      iRepository.get( INVALID_KEYS );
   }


   @Test
   public void getByKeys_valid() {
      assertEquals( VALID_SENSITIVITIES, iRepository.get( VALID_KEYS ) );
   }


   @Test( expected = RuntimeException.class )
   public void getByKeys_invalidAndValidKeys() {
      iRepository.get( VALID_AND_INVALID_KEYS );
   }


   /*
    * Get By Search Criteria Tests
    */

   @Test( expected = NullPointerException.class )
   public void getBySearchCriteria_null() {
      iRepository.get( ( SensitivitySearchCriteria ) null );
   }


   @Test
   public void getBySearchCriteria_empty() {
      assertEquals( iRepository.get( SensitivitySearchCriteria.EMPTY ), VALID_SENSITIVITIES );
   }

}
