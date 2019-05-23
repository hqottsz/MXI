package com.mxi.mx.core.maintenance.eng.config.partgroup.dao;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

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
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.RefSensitivityKey;
import com.mxi.mx.core.maintenance.eng.config.partgroup.model.PartBaseline;
import com.mxi.mx.core.maintenance.eng.config.partgroup.model.PartGroupEntity;
import com.mxi.mx.core.maintenance.eng.config.partgroup.model.PartGroupLegacyKey;
import com.mxi.mx.core.maintenance.eng.config.partgroup.model.PartGroupNaturalKey;


@RunWith( BlockJUnit4ClassRunner.class )
public final class JdbcPartGroupDaoTest {

   private JdbcPartGroupDao iJdbcPartGroupDao;

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   private static final String PART_GROUP_ASSEMBLY = "ACFT_CD1";
   private static final String PART_GROUP_ASSEMBLY_POSITION = "POS_1";

   private static final RefSensitivityKey SENSITIVITY_1 = new RefSensitivityKey( "SENS_1" );
   private static final RefSensitivityKey SENSITIVITY_2 = new RefSensitivityKey( "SENS_2" );

   private static final PartGroupEntity PART_GROUP_WITH_SENS = new PartGroupEntity();
   {
      PART_GROUP_WITH_SENS.setName( "PART GROUP SENS" );
      PART_GROUP_WITH_SENS.setCondition( "Service Required" );
      PART_GROUP_WITH_SENS.setPartBaselines( new ArrayList<PartBaseline>() );
      PART_GROUP_WITH_SENS.setSensitivities( Arrays.asList( SENSITIVITY_1, SENSITIVITY_2 ) );
      PART_GROUP_WITH_SENS.setInventoryClassCode( "TRK" );
      PART_GROUP_WITH_SENS.setPositions( Arrays.asList( PART_GROUP_ASSEMBLY_POSITION ) );

      PartGroupLegacyKey lPartGroupLegacyKey = new PartGroupLegacyKey();
      lPartGroupLegacyKey.setPartGroupKey( "4650:1" );
      lPartGroupLegacyKey.setPartGroupPositionKey( "4650:ACFT_CD1:1:1" );
      PART_GROUP_WITH_SENS.setPartGroupLegacyKey( lPartGroupLegacyKey );

      PartGroupNaturalKey lPartGroupNaturalKey = new PartGroupNaturalKey();
      lPartGroupNaturalKey.setPartGroupCode( "PGSENS" );
      lPartGroupNaturalKey.setAssemblyCode( PART_GROUP_ASSEMBLY );
      PART_GROUP_WITH_SENS.setNaturalKey( lPartGroupNaturalKey );
   }

   private static final String PART_GROUP_WITH_SENS_ID = "10000000000000000000000000000002";

   private static final PartGroupEntity PART_GROUP_WITHOUT_SENS = new PartGroupEntity();
   {
      PART_GROUP_WITHOUT_SENS.setName( "PART GROUP NO SENS" );
      PART_GROUP_WITHOUT_SENS.setCondition( "Inspection Required" );
      PART_GROUP_WITHOUT_SENS.setPartBaselines( new ArrayList<PartBaseline>() );
      PART_GROUP_WITHOUT_SENS.setSensitivities( new ArrayList<RefSensitivityKey>() );
      PART_GROUP_WITHOUT_SENS.setInventoryClassCode( "TRK" );
      PART_GROUP_WITHOUT_SENS.setPositions( Arrays.asList( PART_GROUP_ASSEMBLY_POSITION ) );

      PartGroupLegacyKey lPartGroupLegacyKey = new PartGroupLegacyKey();
      lPartGroupLegacyKey.setPartGroupKey( "4650:2" );
      lPartGroupLegacyKey.setPartGroupPositionKey( "4650:ACFT_CD1:1:1" );
      PART_GROUP_WITHOUT_SENS.setPartGroupLegacyKey( lPartGroupLegacyKey );

      PartGroupNaturalKey lPartGroupNaturalKey = new PartGroupNaturalKey();
      lPartGroupNaturalKey.setPartGroupCode( "PGNOSENS" );
      lPartGroupNaturalKey.setAssemblyCode( PART_GROUP_ASSEMBLY );
      PART_GROUP_WITHOUT_SENS.setNaturalKey( lPartGroupNaturalKey );
   }

   private static final String PART_GROUP_WITHOUT_SENS_ID = "10000000000000000000000000000003";


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), JdbcPartGroupDaoTest.class );
   }


   @Before
   public void setUp() {
      iJdbcPartGroupDao = new JdbcPartGroupDao( QuerySetFactory.getInstance() );
   }


   @Test
   public void findByIds_with_sensitivities() {
      assertTrue( iJdbcPartGroupDao.findByIds( Arrays.asList( PART_GROUP_WITH_SENS_ID ) ).now()
            .contains( PART_GROUP_WITH_SENS ) );
   }


   @Test
   public void findByIds_without_sensitivities() {
      assertTrue( iJdbcPartGroupDao.findByIds( Arrays.asList( PART_GROUP_WITHOUT_SENS_ID ) ).now()
            .contains( PART_GROUP_WITHOUT_SENS ) );
   }
}
