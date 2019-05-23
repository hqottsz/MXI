package com.mxi.mx.core.services.bom.sensitivity.impl;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.services.bom.sensitivity.SystemSensitivityService;
import com.mxi.mx.core.services.bom.sensitivity.model.ConfigSlotSensitivityDetails;
import com.mxi.mx.core.services.sensitivity.model.SensitivitySearchCriteria;


/**
 * Test suite for the {@link SystemSensitivityServiceImpl#get} method where the search criteria
 * object can specify filtering rules around whether or not the assembly is enabled, disabled or
 * either.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class SystemSensitivityService_get_assembly_Test {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   // Object under test
   private SystemSensitivityService iService;


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            SystemSensitivityService_get_assembly_Test.class );
   }


   @Before
   public void setUp() {
      iService = new SystemSensitivityServiceImpl();
   }


   @Test
   public void get_enabledAssembly() throws Throwable {
      SensitivitySearchCriteria lCriteria =
            SensitivitySearchCriteria.builder().assignedToAssembly( true ).build();
      List<ConfigSlotSensitivityDetails> lResults = iService.get( lCriteria );
      assertEquals( 2, lResults.size() );
   }


   @Test
   public void get_disabledAssembly() throws Throwable {
      SensitivitySearchCriteria lCriteria =
            SensitivitySearchCriteria.builder().assignedToAssembly( false ).build();
      List<ConfigSlotSensitivityDetails> lResults = iService.get( lCriteria );
      assertEquals( 1, lResults.size() );
   }


   @Test
   public void get_noAssemblyStatusFiltering() throws Throwable {
      SensitivitySearchCriteria lCriteria =
            SensitivitySearchCriteria.builder().assignedToAssembly( null ).build();
      List<ConfigSlotSensitivityDetails> lResults = iService.get( lCriteria );
      assertEquals( 3, lResults.size() );
   }
}
