package com.mxi.mx.web.jsp.controller.bom;

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
import com.mxi.mx.web.jsp.controller.bom.service.PartGroupUIService;


/**
 *
 * Tests {@link PartGroupUIService #getConfigurableSensitivities(AssemblyKey)} method that is
 * responsible for retrieving the configurable sensitivities for a part group.
 *
 * A configurable sensitivity is one that is globally active and assigned to an Assembly
 *
 */
public class PartGroupUIService_getConfigurableSensitivities_Test {

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
   private static final int NUM_CONFIGURABLE_SYSTEMS = 2;

   // Object under test
   private PartGroupUIService iService;


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            PartGroupUIService_getConfigurableSensitivities_Test.class );
   }


   @Before
   public void setUp() {
      iService = new PartGroupUIService();
   }


   @Test
   public void getConfigurableSensitivities_activeAndAssignedToAssembly() throws Throwable {
      // will implement in oper-18451
   }


   private Sensitivity buildSensitivity( String aCode, String aName, int aOrder, String aMessage ) {
      return Sensitivity.builder().active( true ).code( aCode ).name( aName ).order( aOrder )
            .warning( aMessage ).build();
   }
}
