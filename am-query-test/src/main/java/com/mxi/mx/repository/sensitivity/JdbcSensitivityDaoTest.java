package com.mxi.mx.repository.sensitivity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.dao.sensitivity.SensitivityDao;
import com.mxi.mx.core.dao.sensitivity.impl.JdbcSensitivityDao;
import com.mxi.mx.core.key.RefSensitivityKey;


/**
 * Test class for {@link JdbcSensitivityDao}.
 */
public class JdbcSensitivityDaoTest {

   private SensitivityDao iSensitivityDao;

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      iSensitivityDao = new JdbcSensitivityDao();
   }


   @Test
   public void getNonCapabilityLevelLinkedSensitivities() {
      List<RefSensitivityKey> lNonCapabilityLevelLinkedSensitivities =
            iSensitivityDao.getNonCapabilityLevelLinkedSensitivities();

      assertEquals( 2, lNonCapabilityLevelLinkedSensitivities.size() );

      List<String> lSensitivityCodes = Arrays.asList( "SENS_3", "SENS_4" );

      for ( RefSensitivityKey lRefSensitivityKey : lNonCapabilityLevelLinkedSensitivities ) {
         assertTrue( lSensitivityCodes.contains( lRefSensitivityKey.getKeyAsString() ) );
      }

   }
}
