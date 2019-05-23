package com.mxi.mx.domain.partgroup.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;


/**
 * Tests GetPartGroup.qrx
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetPartGroupTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetPartGroupTest.class );
   }


   @Test
   public void getPartGroupById() {
      String lPartGroupId = "00000000000000000000000000000001";

      QuerySet lQuerySet = getPartGroup( lPartGroupId );

      assertEquals( 1, lQuerySet.getRowCount() );
      assertNotNull( lQuerySet.next() );

      assertEquals( "BATCH", lQuerySet.getString( "inv_class_cd" ) );
      assertEquals( "CMNHW", lQuerySet.getString( "purch_type_cd" ) );
      assertEquals( "PG_CD", lQuerySet.getString( "bom_part_cd" ) );
      assertEquals( "PG_NAME", lQuerySet.getString( "bom_part_name" ) );
      assertEquals( "1.5", lQuerySet.getString( "part_qt" ) );
      assertEquals( "001-500,2000-5000", lQuerySet.getString( "part_group_appl_eff_ldesc" ) );
      assertEquals( "0", lQuerySet.getString( "req_spec_part_bool" ) );
      assertEquals( "other conditions", lQuerySet.getString( "conditions_ldesc" ) );
      assertEquals( "00000000000000000000000000000001", lQuerySet.getString( "part_group_id" ) );
      assertEquals( "0", lQuerySet.getString( "lru_bool" ) );
      assertEquals( "00000000000000000000000000000001",
            lQuerySet.getString( "configuration_slot_id" ) );
      assertEquals( "0", lQuerySet.getString( "standard_bool" ) );
      assertEquals( "1", lQuerySet.getString( "interchg_ord" ) );
      assertEquals( "CODE", lQuerySet.getString( "part_baseline_cd" ) );
      assertEquals( "1234", lQuerySet.getString( "part_appl_eff_ldesc" ) );
      assertEquals( "1", lQuerySet.getString( "approved_bool" ) );
      assertEquals( "1", lQuerySet.getString( "conditional_bool" ) );
   }


   private QuerySet getPartGroup( String aPartGroupId ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aPartGroupId", aPartGroupId );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }

}
