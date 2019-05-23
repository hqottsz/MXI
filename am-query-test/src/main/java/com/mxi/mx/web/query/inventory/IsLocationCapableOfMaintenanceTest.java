package com.mxi.mx.web.query.inventory;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.SqlLoader;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.LocationKey;


public class IsLocationCapableOfMaintenanceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final LocationKey LOCATION_KEY = new LocationKey( "4650:1" );
   private static final AssemblyKey ASSEMBLY_KEY = new AssemblyKey( "4650:ASSMBL-1" );
   private static final String[] WORK_TYPES = { "ADMIN", "LINE", "HANGAR" };
   private static final String QUERY_NAME =
         "com.mxi.mx.web.query.inventory.IsLocationCapableOfMaintenance";


   @Test
   public void execute_locationIsCapableOfMaintenance() {

      QuerySet lQuerySet = executeQuery( LOCATION_KEY, ASSEMBLY_KEY, WORK_TYPES );

      assertEquals( 1, lQuerySet.getRowCount() );
   }


   @Test
   public void execute_locationIsNotCapableOfMaintenance() {

      QuerySet lQuerySet = executeQuery( LOCATION_KEY, ASSEMBLY_KEY, null );

      assertEquals( 0, lQuerySet.getRowCount() );
   }


   private QuerySet executeQuery( LocationKey aSupplyLocation, AssemblyKey aAssembly,
         String[] aWorkTypes ) {

      DataSetArgument lArgs = new DataSetArgument();
      {
         lArgs.add( aSupplyLocation, "aSupplyLocationDbId", "aSupplyLocationId" );
         lArgs.add( aAssembly, "aAssemblyDbId", "aAssemblyCode" );

         if ( aWorkTypes != null ) {
            lArgs.addWhereIn( "inv_loc_capability.work_type_cd", aWorkTypes );
            lArgs.add( "aWorkTypeCount", aWorkTypes.length );
         }
      }

      return QuerySetFactory.getInstance().executeQuery( QUERY_NAME, lArgs );

   }


   @Before
   public void loadData() {
      SqlLoader.load( iDatabaseConnectionRule.getConnection(), getClass(),
            "IsLocationCapableOfMaintenanceTest.sql" );
   }

}
