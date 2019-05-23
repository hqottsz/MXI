package com.mxi.mx.core.services.sensitivity.validator;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.RefSensitivityKey;
import com.mxi.mx.core.services.bom.sensitivity.exception.SensitivitiesNotInSyncException;
import com.mxi.mx.core.services.bom.sensitivity.validator.SensitivityValidator;


public class SensitivityValidatorTest {

   // Test Data
   private static final RefSensitivityKey RVSM = RefSensitivityKey.RVSM;
   private static final RefSensitivityKey ETOPS = RefSensitivityKey.ETOPS;
   private static final RefSensitivityKey CAT_III = RefSensitivityKey.CAT_III;
   private static final RefSensitivityKey RII = RefSensitivityKey.RII;
   private static final List<RefSensitivityKey> ASSIGNED_SENSITIVITIES =
         Arrays.asList( CAT_III, ETOPS, RII );

   // Object under test
   private SensitivityValidator iValidator;

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      iValidator = new SensitivityValidator();
   }


   @Test( expected = NullPointerException.class )
   public void validate_nullAssignedSensitivities() throws Exception {
      List<RefSensitivityKey> lSensitivitiesForUpdate = Arrays.asList( CAT_III, ETOPS );
      iValidator.validateSynchronization( null, lSensitivitiesForUpdate );
   }


   @Test( expected = SensitivitiesNotInSyncException.class )
   public void validate_EnableLessThanAssigned() throws Exception {
      List<RefSensitivityKey> lSensitivitiesForUpdate = Arrays.asList( CAT_III, ETOPS );
      iValidator.validateSynchronization( ASSIGNED_SENSITIVITIES, lSensitivitiesForUpdate );
   }


   @Test( expected = SensitivitiesNotInSyncException.class )
   public void validate_EnableMoreThanAssigned() throws Exception {
      List<RefSensitivityKey> lSensitivitiesForUpdate = Arrays.asList( CAT_III, ETOPS, RII, RVSM );

      iValidator.validateSynchronization( ASSIGNED_SENSITIVITIES, lSensitivitiesForUpdate );
   }


   @Test( expected = SensitivitiesNotInSyncException.class )
   public void validate_EnableThatAreNotAssigned() throws Exception {
      List<RefSensitivityKey> lSensitivitiesForUpdate = Arrays.asList( CAT_III, ETOPS, RVSM );

      iValidator.validateSynchronization( ASSIGNED_SENSITIVITIES, lSensitivitiesForUpdate );
   }


   @Test
   public void validate_validPartGroupScenario() throws Exception {
      List<RefSensitivityKey> lSensitivitiesForUpdate = Arrays.asList( CAT_III, ETOPS, RII );

      iValidator.validateSynchronization( ASSIGNED_SENSITIVITIES, lSensitivitiesForUpdate );
   }

}
