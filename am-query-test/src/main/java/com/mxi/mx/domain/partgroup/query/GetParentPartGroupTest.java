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
 * Tests GetParentPartGroup.qrx
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetParentPartGroupTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), new Object() {
         // use reflection to get class name
      }.getClass().getEnclosingClass() );
   }


   @Test
   public void getParentPartGroupOfATrackedPartGroup() {
      QuerySet lQuerySet = getParentPartGroup( "ACFT_CD1", "TRK_WITH_PARENT" );

      assertEquals( 1, lQuerySet.getRowCount() );
      assertNotNull( lQuerySet.next() );

      assertEquals( "0", lQuerySet.getString( "inv_class_db_id" ) );
      assertEquals( "TRK", lQuerySet.getString( "inv_class_cd" ) );
      assertEquals( "1", lQuerySet.getString( "purch_type_db_id" ) );
      assertEquals( "CMNHW", lQuerySet.getString( "purch_type_cd" ) );
      assertEquals( "PARENT", lQuerySet.getString( "bom_part_cd" ) );
      assertEquals( "Parent", lQuerySet.getString( "bom_part_name" ) );
      assertEquals( "1.0", lQuerySet.getString( "part_qt" ) );
      assertEquals( "001-500,2000-5000", lQuerySet.getString( "part_group_appl_eff_ldesc" ) );
      assertEquals( "0", lQuerySet.getString( "req_spec_part_bool" ) );
      assertEquals( "other conditions", lQuerySet.getString( "conditions_ldesc" ) );
      assertEquals( "20000000000000000000000000000007", lQuerySet.getString( "part_group_id" ) );
      assertEquals( "0", lQuerySet.getString( "lru_bool" ) );
      assertEquals( null, lQuerySet.getString( "ietm_db_id" ) );
      assertEquals( null, lQuerySet.getString( "ietm_id" ) );
      assertEquals( null, lQuerySet.getString( "ietm_topic_id" ) );
      assertEquals( "10000000000000000000000000000002",
            lQuerySet.getString( "configuration_slot_id" ) );
      assertEquals( "0", lQuerySet.getString( "standard_bool" ) );
      assertEquals( "1", lQuerySet.getString( "interchg_ord" ) );
      assertEquals( "CODE", lQuerySet.getString( "part_baseline_cd" ) );
      assertEquals( "1234", lQuerySet.getString( "part_appl_eff_ldesc" ) );
      assertEquals( "1", lQuerySet.getString( "approved_bool" ) );
      assertEquals( "1", lQuerySet.getString( "conditional_bool" ) );
      assertEquals( "00000000000000000000000000000001", lQuerySet.getString( "part_id" ) );
      assertEquals( "ABC", lQuerySet.getString( "manufacturer_code" ) );
      assertEquals( "A0001", lQuerySet.getString( "part_number" ) );
   }


   @Test
   public void getParentPartGroupOfASerializedPartGroup() {
      QuerySet lQuerySet = getParentPartGroup( "ACFT_CD1", "SER_WITH_PARENT" );

      assertEquals( 1, lQuerySet.getRowCount() );
      assertNotNull( lQuerySet.next() );

      assertEquals( "0", lQuerySet.getString( "inv_class_db_id" ) );
      assertEquals( "TRK", lQuerySet.getString( "inv_class_cd" ) );
      assertEquals( "1", lQuerySet.getString( "purch_type_db_id" ) );
      assertEquals( "CMNHW", lQuerySet.getString( "purch_type_cd" ) );
      assertEquals( "PARENT", lQuerySet.getString( "bom_part_cd" ) );
      assertEquals( "Parent", lQuerySet.getString( "bom_part_name" ) );
      assertEquals( "1.0", lQuerySet.getString( "part_qt" ) );
      assertEquals( "001-500,2000-5000", lQuerySet.getString( "part_group_appl_eff_ldesc" ) );
      assertEquals( "0", lQuerySet.getString( "req_spec_part_bool" ) );
      assertEquals( "other conditions", lQuerySet.getString( "conditions_ldesc" ) );
      assertEquals( "20000000000000000000000000000007", lQuerySet.getString( "part_group_id" ) );
      assertEquals( "0", lQuerySet.getString( "lru_bool" ) );
      assertEquals( null, lQuerySet.getString( "ietm_db_id" ) );
      assertEquals( null, lQuerySet.getString( "ietm_id" ) );
      assertEquals( null, lQuerySet.getString( "ietm_topic_id" ) );
      assertEquals( "10000000000000000000000000000002",
            lQuerySet.getString( "configuration_slot_id" ) );
      assertEquals( "0", lQuerySet.getString( "standard_bool" ) );
      assertEquals( "1", lQuerySet.getString( "interchg_ord" ) );
      assertEquals( "CODE", lQuerySet.getString( "part_baseline_cd" ) );
      assertEquals( "1234", lQuerySet.getString( "part_appl_eff_ldesc" ) );
      assertEquals( "1", lQuerySet.getString( "approved_bool" ) );
      assertEquals( "1", lQuerySet.getString( "conditional_bool" ) );
      assertEquals( "00000000000000000000000000000001", lQuerySet.getString( "part_id" ) );
      assertEquals( "ABC", lQuerySet.getString( "manufacturer_code" ) );
      assertEquals( "A0001", lQuerySet.getString( "part_number" ) );
   }


   @Test
   public void getParentPartGroupWhichDoesNotHaveParent_returnsNull() {
      QuerySet lQuerySet = getParentPartGroup( "ACFT_CD1", "BATCH_NO_PARENT" );

      assertEquals( 0, lQuerySet.getRowCount() );
   }


   private QuerySet getParentPartGroup( String aAssemblyCode, String aPartGroupCode ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aAssemblyCode", aAssemblyCode );
      lArgs.add( "aPartGroupCode", aPartGroupCode );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }

}
