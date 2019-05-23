
package com.mxi.mx.core.usage.dao;

import static org.junit.Assert.assertNotNull;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.usage.model.UsageAdjustmentId;
import com.mxi.mx.core.usage.model.UsageType;
import com.mxi.mx.persistence.uuid.SequentialUuidGenerator;
import com.mxi.mx.persistence.uuid.UuidGenerator;


/**
 * This class tests the JdbcUsageRecordDao class.
 *
 * @author dsewell
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class JdbcUsageRecordDaoTest {

   /** The object under test. */
   private JdbcUsageRecordDao iDao = new JdbcUsageRecordDao();

   /** The UUID generator. */
   private UuidGenerator iUuidGenerator;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Test that a usage record can be found based on its natural key no matter what the milliseconds
    * are for a given date.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testFindByNaturalKey() throws Exception {
      InventoryKey lInventory = new InventoryKey( 4650, 1001 );

      Calendar lCal = Calendar.getInstance();

      // set the millisenconds to 999
      lCal.set( Calendar.MILLISECOND, 999 );

      UsageRecordEntity lUsageRecordEntity =
            new UsageRecordEntity( new UsageAdjustmentId( iUuidGenerator.newUuid() ) );
      lUsageRecordEntity.setInventoryKey( lInventory );
      lUsageRecordEntity.setNegatedBool( false );
      lUsageRecordEntity.setAppliedBool( true );
      lUsageRecordEntity.setUsageName( "name" );
      lUsageRecordEntity.setUsageType( UsageType.MXACCRUAL );
      lUsageRecordEntity.setRecordHrKey( null );
      lUsageRecordEntity.setUsageDate( lCal.getTime() );
      lUsageRecordEntity.setRecordDate( lCal.getTime() );
      lUsageRecordEntity.setUsageDesc( "description" );
      lUsageRecordEntity.setDocumentReference( "doc ref" );
      lUsageRecordEntity.setExtKeyDesc( "ext key" );

      iDao.persist( lUsageRecordEntity );

      assertNotNull( "finds usage record with exact same date",
            iDao.findByNaturalKey( lInventory, lCal.getTime() ) );

      // set the millisenconds to 1
      lCal.set( Calendar.MILLISECOND, 1 );

      assertNotNull( "finds usage record with date with different milliseconds",
            iDao.findByNaturalKey( lInventory, lCal.getTime() ) );
   }


   @Before
   public void setUp() throws Exception {
      iUuidGenerator = new SequentialUuidGenerator();
   }


   @Before
   public void loadData() throws Exception {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }
}
