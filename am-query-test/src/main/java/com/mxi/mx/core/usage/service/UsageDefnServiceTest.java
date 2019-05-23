package com.mxi.mx.core.usage.service;

import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.CalculatedUsageParameter;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.services.assembly.CalculatedParmTO;
import com.mxi.mx.core.services.assembly.DuplicateCalcParmException;
import com.mxi.mx.core.services.assembly.DuplicateMeasurementCodeException;
import com.mxi.mx.core.services.usagedefn.UsageDefnService;


/**
 * Integration tests for {@link UsageDefnService}
 *
 */
public class UsageDefnServiceTest {

   private static final String CALC_PARM_CODE = "CALC_PARM_CODE";
   private static final String CALC_PARM_FUNCTION = "CALC_PARM_FUNCTION";
   private static final String AircraftAssemblyCode1 = "B676";
   private static final String AircraftAssemblyCode2 = "A320";
   private static final String HOURS_USAGE_PARM = "HOURS";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public ExpectedException iExpectedException = ExpectedException.none();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Given an aircraft assembly having a calculated parameter with a code defined against the root
    * configuration slot
    *
    * And another aircraft assembly
    *
    * When attempting to create a calculated parameter with the same code as the existing calculated
    * parm against another aircraft assembly
    *
    * Then the creation of calculated parameter is stopped along with an appropriate error message
    *
    */
   @Test
   public void itPreventsDuplicationOfCalcParmAgainstDifferentAssemblies() throws Exception {

      Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

         @Override
         public void configure( AircraftAssembly aBuilder ) {
            aBuilder.setCode( AircraftAssemblyCode1 );
            aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

               @Override
               public void configure( ConfigurationSlot aBuilder ) {
                  // Track a parameter.
                  aBuilder.addCalculatedUsageParameter(
                        new DomainConfiguration<CalculatedUsageParameter>() {

                           @Override
                           public void configure( CalculatedUsageParameter aBuilder ) {
                              aBuilder.setCode( CALC_PARM_CODE );
                              aBuilder.setDatabaseCalculation( CALC_PARM_FUNCTION );
                              aBuilder.setPrecisionQt( 2 );
                           }

                        } );
               }
            } );
         }
      } );

      CalculatedParmTO lCalculatedParmTO = new CalculatedParmTO();
      lCalculatedParmTO.setCode( CALC_PARM_CODE );
      lCalculatedParmTO.setName( CALC_PARM_CODE );

      final AssemblyKey lAnotherAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.setCode( AircraftAssemblyCode2 );
               }
            } );

      iExpectedException.expect( DuplicateCalcParmException.class );
      iExpectedException.expectMessage( String.format(
            "[MXERR-31013] The code '%s' already exists for assembly '%s'.<br><br>Please enter a unique code.",
            CALC_PARM_CODE, AircraftAssemblyCode1 ) );
      UsageDefnService.createCalcParm( lAnotherAircraftAssembly, lCalculatedParmTO );

      fail( "Expected DuplicateCalcParmException" );
   }


   /**
    * Given an aircraft assembly having a calculated parameter with a code defined against the root
    * configuration slot
    *
    * When attempting to create a calculated parameter with the same code as the existing calculated
    * parm
    *
    * Then the creation of calculated parameter is stopped along with an appropriate error message
    *
    */
   @Test
   public void itPreventsDuplicationOfCalcParmAgainstSameAssembly() throws Exception {

      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.setCode( AircraftAssemblyCode1 );
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        // Track a parameter.
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setCode( CALC_PARM_CODE );
                                    aBuilder.setDatabaseCalculation( CALC_PARM_FUNCTION );
                                    aBuilder.setPrecisionQt( 2 );
                                 }

                              } );
                     }
                  } );
               }
            } );

      CalculatedParmTO lCalculatedParmTO = new CalculatedParmTO();
      lCalculatedParmTO.setCode( CALC_PARM_CODE );
      lCalculatedParmTO.setName( CALC_PARM_CODE );

      iExpectedException.expect( DuplicateCalcParmException.class );
      iExpectedException.expectMessage( String.format(
            "[MXERR-31013] The code '%s' already exists for assembly '%s'.<br><br>Please enter a unique code.",
            CALC_PARM_CODE, AircraftAssemblyCode1 ) );
      UsageDefnService.createCalcParm( lAircraftAssembly, lCalculatedParmTO );

      fail( "Expected DuplicateCalcParmException" );
   }


   /**
    * Given an aircraft assembly tracking a usage parameter
    *
    * When attempting to create a calculated parameter with the same code as the existing usage
    * parameter
    *
    * Then the creation of calculated parameter is stopped along with an appropriate error message
    *
    */
   @Test
   public void itPreventsCreationOfCalcParmWithSameCodeAsExistingUsageParm() throws Exception {

      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.setCode( AircraftAssemblyCode1 );
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        // Track a parameter.
                        aBuilder.addUsageParameter( DataTypeKey.HOURS );
                     }
                  } );
               }
            } );

      CalculatedParmTO lCalculatedParmTO = new CalculatedParmTO();
      lCalculatedParmTO.setCode( HOURS_USAGE_PARM );
      lCalculatedParmTO.setName( HOURS_USAGE_PARM );

      iExpectedException.expect( DuplicateMeasurementCodeException.class );
      iExpectedException.expectMessage( String.format(
            "[MXERR-30745] The code '%s' already exists.<br><br>Please enter a unique code.",
            HOURS_USAGE_PARM ) );
      UsageDefnService.createCalcParm( lAircraftAssembly, lCalculatedParmTO );

      fail( "Expected DuplicateMeasurementCodeException" );
   }
}
