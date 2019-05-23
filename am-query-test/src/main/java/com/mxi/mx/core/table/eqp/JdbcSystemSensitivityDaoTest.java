package com.mxi.mx.core.table.eqp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.inject.Injector;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.SystemSensitivityKey;


public class JdbcSystemSensitivityDaoTest {

   private static final SystemSensitivityKey EXISTING_SYSTEM_SENSTIIVITY_KEY =
         new SystemSensitivityKey( "5000000:A320:0:TEST" );
   private static final SystemSensitivityKey NEW_SYSTEM_SENSTIIVITY_KEY =
         new SystemSensitivityKey( "4000000:A320:0:TEST" );

   private JdbcSystemSensitivityDao iJdbcSystemSensitivityDao;
   private Injector iInjector;

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      iJdbcSystemSensitivityDao = new JdbcSystemSensitivityDao();
   }


   @Test
   public void delete() {
      SystemSensitivityTable lSystemSensitivityTableRow =
            new SystemSensitivityTable( EXISTING_SYSTEM_SENSTIIVITY_KEY );
      boolean isDeleted = iJdbcSystemSensitivityDao.delete( lSystemSensitivityTableRow );

      assertTrue( isDeleted );
   }


   @Test
   public void create() {
      SystemSensitivityTable lSystemSensitivityTableRow = iJdbcSystemSensitivityDao.create();

      assertNotNull( lSystemSensitivityTableRow );
      assertNull( lSystemSensitivityTableRow.getPk() );
   }


   @Test
   public void create_withKey() {
      SystemSensitivityTable lSystemSensitivityTableRow =
            iJdbcSystemSensitivityDao.create( NEW_SYSTEM_SENSTIIVITY_KEY );

      assertNotNull( lSystemSensitivityTableRow );
      assertEquals( NEW_SYSTEM_SENSTIIVITY_KEY, lSystemSensitivityTableRow.getPk() );
   }


   @Test
   public void findByPrimaryKey() {
      SystemSensitivityTable lSystemSensitivityTableRow =
            iJdbcSystemSensitivityDao.findByPrimaryKey( EXISTING_SYSTEM_SENSTIIVITY_KEY );

      assertEquals( EXISTING_SYSTEM_SENSTIIVITY_KEY, lSystemSensitivityTableRow.getPk() );
   }


   @Test
   public void findByPrimaryKey_nullKey() {
      SystemSensitivityTable lSystemSensitivityTableRow =
            iJdbcSystemSensitivityDao.findByPrimaryKey( null );

      assertNull( lSystemSensitivityTableRow.getPk() );
   }


   @Test
   public void insert() {
      SystemSensitivityTable lSystemSensitivityTableRow =
            new SystemSensitivityTable( NEW_SYSTEM_SENSTIIVITY_KEY );
      SystemSensitivityKey lInsertedRowKey =
            iJdbcSystemSensitivityDao.insert( lSystemSensitivityTableRow );

      assertNotNull( lInsertedRowKey );
   }


   @Test( expected = NullPointerException.class )
   public void insert_nullRow() {
      SystemSensitivityTable lSystemSensitivityTableRow = new SystemSensitivityTable( null );

      iJdbcSystemSensitivityDao.insert( lSystemSensitivityTableRow );
   }
}
