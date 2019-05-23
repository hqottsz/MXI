package com.mxi.mx.core.table.eqp.partgroup;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartGroupSensitivityKey;
import com.mxi.mx.core.key.RefSensitivityKey;


@RunWith( BlockJUnit4ClassRunner.class )
public class JdbcPartGroupSensitivityDaoTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private static final PartGroupKey PART_GROUP_KEY = new PartGroupKey( "4650:1" );
   private static final RefSensitivityKey SENSITIVITY_KEY = new RefSensitivityKey( "TEST" );

   private PartGroupSensitivityDao iDao;


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      iDao = new JdbcPartGroupSensitivityDao();
   }


   @Test( expected = Exception.class )
   public void insert_duplicateRecord() {
      PartGroupSensitivityKey lKey = new PartGroupSensitivityKey( PART_GROUP_KEY, SENSITIVITY_KEY );
      PartGroupSensitivityTable lPartGroupSensitivity = new PartGroupSensitivityTable( lKey );

      iDao.insert( lPartGroupSensitivity );
      iDao.insert( lPartGroupSensitivity );
   }

}
