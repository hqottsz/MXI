package com.mxi.mx.repository.workpackage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.utils.uuid.UuidConverter;
import com.mxi.mx.domain.maintenance.WorkPackage;


@RunWith( MockitoJUnitRunner.class )
public class JdbcWorkPackageRepositoryTest {

   private WorkPackageRepository iWorkPackageRepository;

   // Test data
   private TaskKey iTestWorkPackageKey = new TaskKey( "4650:2" );
   private TaskKey iInvalidTestWorkPackageKey = new TaskKey( "4650:100" );
   private UUID iWorkPackageTaskAltId = UUID.fromString( "12345678-90AB-CDEF-1234-567890ABCDEF" );
   private UUID iInvalidWorkPackageTaskAltId =
         UUID.fromString( "12345670-90AB-CDEF-1234-567890ABCDEF" );
   private UUID iAircraftUUID = UUID.fromString( "12345678-91AB-CDEF-1234-567890ABCDEF" );
   private UUID iUnfoundAircraftUUID = UUID.fromString( "11111111-11AA-AAAA-1111-111111AAAAAA" );
   private WorkPackage iTestWorkPackage =
         new WorkPackage( iTestWorkPackageKey, RefEventStatusKey.IN_WORK );
   private RefEventStatusKey iUnfoundRefEventStatusKey = RefEventStatusKey.ACARCHIVE;
   private UuidConverter iUuidConverter = new UuidConverter();
   @Mock
   private SchedStaskDao iMockSchedStaskDao;
   private SchedStaskTable iMockSchedStaskTable;

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( SchedStaskDao.class ).toInstance( iMockSchedStaskDao );
            }
         } );


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );

      iWorkPackageRepository = new JdbcWorkPackageRepository( iMockSchedStaskDao, iUuidConverter );
      iMockSchedStaskTable = new SchedStaskTable( iTestWorkPackageKey ) {
      };
      iMockSchedStaskTable.setAlternateKey( iWorkPackageTaskAltId );
   }


   @Test( expected = IllegalStateException.class )
   public void get_byWorkPackageKey_workPackageNotPresent() {
      iMockSchedStaskTable.setAlternateKey( iInvalidWorkPackageTaskAltId );
      when( iMockSchedStaskDao.create( Mockito.any( TaskKey.class ) ) )
            .thenReturn( iMockSchedStaskTable );
      iWorkPackageRepository.get( iInvalidTestWorkPackageKey );
   }


   @Test
   public void get_byWorkPackageKey_workPackagePresent() {
      when( iMockSchedStaskDao.create( iTestWorkPackageKey ) ).thenReturn( iMockSchedStaskTable );
      WorkPackage lResultingWorkPackage = iWorkPackageRepository.get( iTestWorkPackageKey );
      assertEquals( lResultingWorkPackage, iTestWorkPackage );
   }


   @Test( expected = UnsupportedOperationException.class )
   public void get_manyKeys() {
      Set<TaskKey> aKeys = new HashSet<TaskKey>();
      iWorkPackageRepository.get( aKeys );
   }


   @Test( expected = NullPointerException.class )
   public void find_byWorkPackageKey_nullAircraftId() {
      TaskKey lTaskKey = null;
      iWorkPackageRepository.get( lTaskKey );
   }


   @Test
   public void find_byWorkPackageKey_cannotFindAircraftId() {
      iMockSchedStaskTable.setAlternateKey( iInvalidWorkPackageTaskAltId );
      when( iMockSchedStaskDao.create( iInvalidTestWorkPackageKey ) )
            .thenReturn( iMockSchedStaskTable );

      Optional<WorkPackage> lResultingWorkPackage =
            iWorkPackageRepository.find( iInvalidTestWorkPackageKey );
      assertFalse( lResultingWorkPackage.isPresent() );
   }


   @Test
   public void find_byWorkPackageKey_valid() {
      when( iMockSchedStaskDao.create( iTestWorkPackageKey ) ).thenReturn( iMockSchedStaskTable );
      Optional<WorkPackage> lResultingWorkPackage =
            iWorkPackageRepository.find( iTestWorkPackageKey );
      assertTrue( lResultingWorkPackage.isPresent() );
      WorkPackage lGetWorkPackage = lResultingWorkPackage.get();
      assertEquals( lGetWorkPackage, iTestWorkPackage );
   }


   @Test( expected = NullPointerException.class )
   public void find_byKeyAndStatus_nullAircraftId() {
      iWorkPackageRepository.find( null, RefEventStatusKey.IN_WORK );
   }


   @Test( expected = NullPointerException.class )
   public void find_byKeyAndStatus_nullStatusKey() {
      iWorkPackageRepository.find( iAircraftUUID, null );
   }


   @Test
   public void find_byKeyAndStatus_cannotFindAircraftId() {
      when( iMockSchedStaskDao.create( iTestWorkPackageKey ) ).thenReturn( iMockSchedStaskTable );
      Optional<WorkPackage> lResultingWorkPackage =
            iWorkPackageRepository.find( iUnfoundAircraftUUID, RefEventStatusKey.IN_WORK );
      assertFalse( lResultingWorkPackage.isPresent() );
   }


   @Test
   public void find_byKeyAndStatus_cannotFindStatusKey() {
      when( iMockSchedStaskDao.create( iTestWorkPackageKey ) ).thenReturn( iMockSchedStaskTable );
      Optional<WorkPackage> lResultingWorkPackage =
            iWorkPackageRepository.find( iAircraftUUID, iUnfoundRefEventStatusKey );
      assertFalse( lResultingWorkPackage.isPresent() );
   }


   @Test
   public void find_byKeyAndStatus_valid() {
      when( iMockSchedStaskDao.create( iTestWorkPackageKey ) ).thenReturn( iMockSchedStaskTable );
      Optional<WorkPackage> lResultingWorkPackage =
            iWorkPackageRepository.find( iAircraftUUID, RefEventStatusKey.IN_WORK );
      assertTrue( lResultingWorkPackage.isPresent() );
      WorkPackage lGetWorkPackage = lResultingWorkPackage.get();
      assertEquals( lGetWorkPackage, iTestWorkPackage );
   }

}
