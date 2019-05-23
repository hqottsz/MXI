package com.mxi.mx.web.jsp.controller.bom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.domain.sensitivity.Sensitivity;
import com.mxi.mx.web.jsp.controller.bom.service.ConfigSlotService;


public class ConfigSlotService_getConfigurableSensitivities_Test {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private static final String CAT_III_CODE = "CAT_III";
   private static final String ETOPS_CODE = "ETOPS";
   private static final String RVSM_CODE = "RVSM";

   private static final String CAT_III_NAME = "CAT III";
   private static final String ETOPS_NAME = "ETOPS";
   private static final String RVSM_NAME = "RVSM";

   private static final int CAT_III_ORDER = 10;
   private static final int ETOPS_ORDER = 20;
   private static final int RVSM_ORDER = 40;

   private static final String SENSITIVITY_WARNING_MESSAGE =
         "This system is %s compliance sensitive - the aircraft may require recertification.";

   private static final AssemblyKey ASSEMBLY_KEY = new AssemblyKey( 4650, "TEST" );
   private static final int NUM_CONFIGURABLE_SYSTEMS = 3;

   // Object under test
   private ConfigSlotService iService;


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            ConfigSlotService_getConfigurableSensitivities_Test.class );
   }


   @Before
   public void setUp() {
      iService = new ConfigSlotService();
   }


   @Test
   public void getConfigurableSensitivities_activeAndAssignedToAssembly() throws Throwable {
      List<Sensitivity> lConfigurableSensitivities =
            iService.getConfigurableSensitivities( ASSEMBLY_KEY );
      assertEquals( NUM_CONFIGURABLE_SYSTEMS, lConfigurableSensitivities.size() );
      assertTrue( lConfigurableSensitivities
            .contains( buildActiveSensitivities( CAT_III_CODE, CAT_III_NAME, CAT_III_ORDER,
                  String.format( SENSITIVITY_WARNING_MESSAGE, CAT_III_NAME ) ) ) );
      assertTrue( lConfigurableSensitivities.contains( buildActiveSensitivities( ETOPS_CODE,
            ETOPS_NAME, ETOPS_ORDER, String.format( SENSITIVITY_WARNING_MESSAGE, ETOPS_NAME ) ) ) );
      assertTrue( lConfigurableSensitivities.contains( buildActiveSensitivities( RVSM_CODE,
            RVSM_NAME, RVSM_ORDER, String.format( SENSITIVITY_WARNING_MESSAGE, RVSM_NAME ) ) ) );
   }


   private Sensitivity buildActiveSensitivities( String aCode, String aName, int aOrder,
         String aMessage ) {
      return Sensitivity.builder().active( true ).code( aCode ).name( aName ).order( aOrder )
            .warning( aMessage ).build();
   }

}
