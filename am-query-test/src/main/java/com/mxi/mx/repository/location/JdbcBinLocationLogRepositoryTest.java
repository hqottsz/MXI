package com.mxi.mx.repository.location;

import static com.mxi.mx.repository.location.JdbcBinLocationLogRepository.ColumnName.COUNT_ACTUAL_QT;
import static com.mxi.mx.repository.location.JdbcBinLocationLogRepository.ColumnName.COUNT_EXPECTED_QT;
import static com.mxi.mx.repository.location.JdbcBinLocationLogRepository.ColumnName.HR_DB_ID;
import static com.mxi.mx.repository.location.JdbcBinLocationLogRepository.ColumnName.HR_ID;
import static com.mxi.mx.repository.location.JdbcBinLocationLogRepository.ColumnName.LOC_DB_ID;
import static com.mxi.mx.repository.location.JdbcBinLocationLogRepository.ColumnName.LOC_ID;
import static com.mxi.mx.repository.location.JdbcBinLocationLogRepository.ColumnName.LOG_GDT;
import static com.mxi.mx.repository.location.JdbcBinLocationLogRepository.ColumnName.LOG_TYPE_CD;
import static com.mxi.mx.repository.location.JdbcBinLocationLogRepository.ColumnName.PART_NO_DB_ID;
import static com.mxi.mx.repository.location.JdbcBinLocationLogRepository.ColumnName.PART_NO_ID;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.materials.inventorycount.domain.BinLocationLog;
import com.mxi.mx.core.materials.inventorycount.domain.BinLocationLogType;


@RunWith( BlockJUnit4ClassRunner.class )
public class JdbcBinLocationLogRepositoryTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private BinLocationLogRepository binLocationRepository;


   @Before
   public void setUp() {
      binLocationRepository = new JdbcBinLocationLogRepository();
   }


   @Test
   public void itCreateBinLocationLog() {

      final LocationKey locationKey = Domain.createLocation();
      final PartNoKey partNoKey = Domain.createPart();
      final HumanResourceKey hrKey = Domain.createHumanResource();
      final Double expectedQt = 10d;
      final Double actualQt = 5d;
      final Date date = new Date();
      final BinLocationLogType logType = BinLocationLogType.INVENTORY_COUNT_RECORDED;

      BinLocationLog binLocationLog = new BinLocationLog( locationKey, partNoKey, hrKey, expectedQt,
            actualQt, BinLocationLogType.INVENTORY_COUNT_RECORDED, date );

      // ACT
      binLocationRepository.create( binLocationLog );

      // ASSERT
      final DataSetArgument args = new DataSetArgument();
      args.add( locationKey, LOC_DB_ID.name(), LOC_ID.name() );
      args.add( locationKey, PART_NO_DB_ID.name(), PART_NO_ID.name() );
      args.add( locationKey, HR_DB_ID.name(), HR_ID.name() );

      final List<String> columns = new ArrayList<>();
      Stream.of( JdbcBinLocationLogRepository.ColumnName.values() ).forEach( element -> {
         columns.add( element.name() );
      } );

      final QuerySet querySet = QuerySetFactory.getInstance().executeQuery(
            columns.toArray( new String[0] ), JdbcBinLocationLogRepository.TABLE_NAME, args );
      assertEquals( 1, querySet.getRowCount() );
      querySet.next();
      assertEquals( locationKey.getDbId(), querySet.getInt( LOC_DB_ID.name() ) );
      assertEquals( locationKey.getId(), querySet.getInt( LOC_ID.name() ) );
      assertEquals( partNoKey.getDbId(), querySet.getInt( PART_NO_DB_ID.name() ) );
      assertEquals( partNoKey.getId(), querySet.getInt( PART_NO_ID.name() ) );
      assertEquals( hrKey.getDbId(), querySet.getInt( HR_DB_ID.name() ) );
      assertEquals( hrKey.getId(), querySet.getInt( HR_ID.name() ) );
      assertEquals( expectedQt.doubleValue(), querySet.getDouble( COUNT_EXPECTED_QT.name() ), 0d );
      assertEquals( actualQt.doubleValue(), querySet.getDouble( COUNT_ACTUAL_QT.name() ), 0d );
      assertEquals( logType.getCode(), querySet.getString( LOG_TYPE_CD.name() ) );
      assertEquals( date.getTime() / 1000, querySet.getDate( LOG_GDT.name() ).getTime() / 1000 );
   }
}
