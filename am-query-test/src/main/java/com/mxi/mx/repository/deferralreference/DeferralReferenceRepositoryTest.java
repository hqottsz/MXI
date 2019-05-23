package com.mxi.mx.repository.deferralreference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.FailDeferRefKey;
import com.mxi.mx.core.maintenance.plan.deferralreference.domain.DeferralReference;
import com.mxi.mx.core.maintenance.plan.deferralreference.domain.DeferralReferenceRepository;
import com.mxi.mx.core.maintenance.plan.deferralreference.infra.JdbcDeferralReferenceRepository;


public class DeferralReferenceRepositoryTest {

   public static final FailDeferRefKey DEFERRAL_REFERENCE_KEY = new FailDeferRefKey( 4650, 1 );
   public static final FailDeferRefKey DEFERRAL_REFERENCE_KEY_WITH_OPERATORS =
         new FailDeferRefKey( 4651, 1 );
   public static final FailDeferRefKey INVALID_DEFERRAL_REFERENCE_KEY =
         new FailDeferRefKey( 4650, 1234 );
   public static final CarrierKey CARRIER_KEY = new CarrierKey( 124, 1 );

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private DeferralReferenceRepository deferralReferenceRepository;


   @Before
   public void setUp() {
      deferralReferenceRepository = new JdbcDeferralReferenceRepository();
      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );
   }


   @Test( expected = NullPointerException.class )
   public void null_DeferralReferenceKey() {
      deferralReferenceRepository.get( null );
   }


   /**
    * Deferral references should always have at least one operator
    */
   @Test( expected = IllegalStateException.class )
   public void get_DeferralReference_WithoutOperator() {
      deferralReferenceRepository.get( DEFERRAL_REFERENCE_KEY );
   }


   @Test
   public void get_DeferralReference_WithOperators() {
      DeferralReference deferralReference =
            deferralReferenceRepository.get( DEFERRAL_REFERENCE_KEY_WITH_OPERATORS ).get();

      assertNotNull( deferralReference );
      assertEquals( DEFERRAL_REFERENCE_KEY_WITH_OPERATORS, deferralReference.getId() );
      assertFalse( deferralReference.getOperators().isEmpty() );
      assertEquals( deferralReference.getOperators().size(), 2 );
      assertTrue( deferralReference.getOperators().contains( CARRIER_KEY ) );
   }


   @Test
   public void get_DeferralReference_byInvalidKey() {
      Optional<DeferralReference> deferralReference =
            deferralReferenceRepository.get( INVALID_DEFERRAL_REFERENCE_KEY );

      assertFalse( deferralReference.isPresent() );
   }

}
