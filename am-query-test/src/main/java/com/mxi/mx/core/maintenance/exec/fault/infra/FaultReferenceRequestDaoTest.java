package com.mxi.mx.core.maintenance.exec.fault.infra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.google.inject.AbstractModule;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.FaultReferenceRequestKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.maintenance.exec.fault.infra.FaultReferenceRequestDao;
import com.mxi.mx.core.maintenance.exec.fault.infra.FaultReferenceRequestTableRow;
import com.mxi.mx.core.maintenance.exec.fault.infra.FaultReferenceRequestDao.ColumnName;


@RunWith( BlockJUnit4ClassRunner.class )
public class FaultReferenceRequestDaoTest {

   private static final FaultReferenceRequestKey NEW_REFERENCE_REQUEST_KEY =
         new FaultReferenceRequestKey( "1:4650" );
   private static final FaultReferenceRequestKey EXISTING_REFERENCE_REQUEST_KEY =
         new FaultReferenceRequestKey( "5:4650" );
   private static final HumanResourceKey EXISTING_HR_KEY = new HumanResourceKey( "4650:222" );
   private static final HumanResourceKey NEW_HR_KEY = new HumanResourceKey( "4650:333" );

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( FaultReferenceRequestDao.class ).to( JdbcFaultReferenceRequestDao.class );
            }
         } );

   private FaultReferenceRequestDao referenceRequestDao;


   @Before
   public void setup() {
      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );
      referenceRequestDao = InjectorContainer.get().getInstance( FaultReferenceRequestDao.class );
   }


   @Test
   public void create_default() {
      FaultReferenceRequestTableRow row = referenceRequestDao.create();

      assertNotNull( row );
      assertNull( row.getPk() );
   }


   @Test
   public void create_valid() {
      FaultReferenceRequestTableRow row = referenceRequestDao.create( NEW_REFERENCE_REQUEST_KEY );
      assertEquals( NEW_REFERENCE_REQUEST_KEY, row.getPk() );
   }


   @Test
   public void findByPrimaryKey_null() {
      FaultReferenceRequestTableRow row = referenceRequestDao.findByPrimaryKey( null );

      assertNull( row.getPk() );
   }


   @Test
   public void findByPrimaryKey_valid() {
      FaultReferenceRequestTableRow row =
            referenceRequestDao.findByPrimaryKey( EXISTING_REFERENCE_REQUEST_KEY );
      assertNotNull( row );
      assertEquals( EXISTING_HR_KEY, row.getHumanResource() );
   }


   @Test( expected = NullPointerException.class )
   public void insert_null() {
      referenceRequestDao.insert( null );
   }


   @Test
   public void insert_doesNotGenerateKey() {
      FaultReferenceRequestTableRow row =
            new FaultReferenceRequestTableRow( NEW_REFERENCE_REQUEST_KEY );

      FaultReferenceRequestKey repairReferenceRequestKey = referenceRequestDao.insert( row );

      assertEquals( NEW_REFERENCE_REQUEST_KEY, repairReferenceRequestKey );
      assertTrue( hasRecord( NEW_REFERENCE_REQUEST_KEY ) );
   }


   @Test
   public void insert_generatesKey() {
      FaultReferenceRequestTableRow row = new FaultReferenceRequestTableRow( null );

      FaultReferenceRequestKey repairReferenceRequestKey = referenceRequestDao.insert( row );

      assertTrue( hasRecord( repairReferenceRequestKey ) );
   }


   @Test( expected = NullPointerException.class )
   public void update_null() {
      referenceRequestDao.update( null );
   }


   @Test
   public void update_valid() {
      FaultReferenceRequestTableRow row =
            new FaultReferenceRequestTableRow( EXISTING_REFERENCE_REQUEST_KEY );
      row.setHumanResource( NEW_HR_KEY );

      referenceRequestDao.update( row );

      FaultReferenceRequestTableRow retrievedRequestTable =
            referenceRequestDao.findByPrimaryKey( EXISTING_REFERENCE_REQUEST_KEY );

      assertEquals( NEW_HR_KEY, retrievedRequestTable.getHumanResource() );
   }


   @Test( expected = NullPointerException.class )
   public void refresh_null() {
      referenceRequestDao.refresh( null );
   }


   @Test
   public void refresh_valid() {
      FaultReferenceRequestTableRow row =
            new FaultReferenceRequestTableRow( EXISTING_REFERENCE_REQUEST_KEY );

      DataSetArgument args = new DataSetArgument();
      args.add( NEW_HR_KEY, ColumnName.HR_DB_ID.name(), ColumnName.HR_ID.name() );

      DataSetArgument whereArgs = new DataSetArgument();
      whereArgs.add( row.getPk(), ColumnName.FAULT_REF_REQ_ID.name(),
            ColumnName.FAULT_REF_REQ_DB_ID.name() );

      MxDataAccess.getInstance().executeUpdate( FaultReferenceRequestTableRow.TABLE_NAME, args,
            whereArgs );

      referenceRequestDao.refresh( row );

      assertEquals( NEW_HR_KEY, row.getHumanResource() );
   }


   @Test( expected = NullPointerException.class )
   public void delete_null() {
      referenceRequestDao.delete( null );
   }


   @Test
   public void delete_valid() {
      FaultReferenceRequestTableRow row =
            new FaultReferenceRequestTableRow( EXISTING_REFERENCE_REQUEST_KEY );

      assertTrue( referenceRequestDao.delete( row ) );
      assertFalse( hasRecord( EXISTING_REFERENCE_REQUEST_KEY ) );
   }


   @Test
   public void delete_invalid() {
      FaultReferenceRequestTableRow row =
            new FaultReferenceRequestTableRow( NEW_REFERENCE_REQUEST_KEY );

      assertFalse( referenceRequestDao.delete( row ) );
   }


   @Test
   public void generatePrimaryKey_unique() {
      FaultReferenceRequestKey key1 = referenceRequestDao.generatePrimaryKey();
      FaultReferenceRequestKey key2 = referenceRequestDao.generatePrimaryKey();

      assertFalse( key1.getKeyId() == key2.getKeyId() );
   }


   private boolean hasRecord( FaultReferenceRequestKey aKey ) {

      DataSetArgument args = new DataSetArgument();
      args.add( aKey, "fault_ref_req_id", "fault_ref_req_db_id" );

      QuerySet qs = QuerySetFactory.getInstance()
            .executeQueryTable( FaultReferenceRequestTableRow.TABLE_NAME, args );

      if ( qs.next() ) {
         return true;
      }

      return false;
   }

}
