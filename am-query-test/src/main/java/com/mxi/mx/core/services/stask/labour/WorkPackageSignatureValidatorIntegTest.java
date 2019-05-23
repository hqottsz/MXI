package com.mxi.mx.core.services.stask.labour;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.validation.ValidationException;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.SchedWPSignReqKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.stask.CannotCompleteException;
import com.mxi.mx.core.services.stask.RootTaskOperationException;
import com.mxi.mx.core.services.stask.labour.exception.InvalidHrQualificationException;
import com.mxi.mx.core.services.stask.status.InvalidOperationForTaskStatusException;
import com.mxi.mx.core.services.stask.status.SubTasksNotHistoricException;


public class WorkPackageSignatureValidatorIntegTest {

   private static final SchedWPSignReqKey VALID_SCHED_WP_SIGN_REQ_KEY =
         new SchedWPSignReqKey( 4650, 3000 );
   private static final Date TODAY = new Date();
   private static final HumanResourceKey VALID_HR_KEY = new HumanResourceKey( 4650, 4100 );
   private static final HumanResourceKey INVALID_HR_KEY = new HumanResourceKey( 4650, 4000 );
   private static final int USER_ID = 4100;

   private static final TaskKey VALID_TASK_KEY = new TaskKey( 4650, 2000 );
   private static final TaskKey VALID_TASK_KEY_AOG_FAULT = new TaskKey( 4650, 2400 );
   private static final TaskKey NON_ROOT_TASK_KEY = new TaskKey( 4650, 2100 );
   private static final TaskKey NON_HISTORIC_TASK_KEY = new TaskKey( 4650, 2200 );
   private static final TaskKey INVALID_STATUS_TASK_KEY = new TaskKey( 4650, 2300 );

   private WorkPackageSignatureValidator iWorkPackageSignatureValidator;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();
   @Rule
   public OperateAsUserRule iOperateAsUserRule = new OperateAsUserRule( USER_ID, "testuser" );


   @Before
   public void setUp() throws Exception {

      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );

      iWorkPackageSignatureValidator = new WorkPackageSignatureValidator();

      GlobalParametersFake.getInstance( ParmTypeEnum.LOGIC )
            .setInteger( "END_DATE_COMPLETION_THRESHOLD", 1 );
      GlobalParametersFake.getInstance( ParmTypeEnum.LOGIC ).setBoolean( "ENFORCE_QUALIFICATION",
            true );
   }


   @Test
   public void validate_success() throws Exception {

      iWorkPackageSignatureValidator.validate( VALID_SCHED_WP_SIGN_REQ_KEY, VALID_TASK_KEY,
            VALID_HR_KEY, TODAY, true, "" );
   }


   @Test( expected = RootTaskOperationException.class )
   public void validate_rootTaskException() throws Exception {

      iWorkPackageSignatureValidator.validate( VALID_SCHED_WP_SIGN_REQ_KEY, NON_ROOT_TASK_KEY,
            VALID_HR_KEY, TODAY, true, "" );
   }


   @Test( expected = SubTasksNotHistoricException.class )
   public void validate_subTasksNotHistoricException() throws Exception {

      iWorkPackageSignatureValidator.validate( VALID_SCHED_WP_SIGN_REQ_KEY, NON_HISTORIC_TASK_KEY,
            VALID_HR_KEY, TODAY, true, "" );
   }


   @Test( expected = InvalidHrQualificationException.class )
   public void validate_invalidHrQualificationException() throws Exception {

      iWorkPackageSignatureValidator.validate( VALID_SCHED_WP_SIGN_REQ_KEY, VALID_TASK_KEY,
            INVALID_HR_KEY, TODAY, true, "" );
   }


   @Test( expected = InvalidOperationForTaskStatusException.class )
   public void validate_invalidOperationForTaskStatusException() throws Exception {

      iWorkPackageSignatureValidator.validate( VALID_SCHED_WP_SIGN_REQ_KEY, INVALID_STATUS_TASK_KEY,
            VALID_HR_KEY, TODAY, true, "" );
   }


   @Test( expected = CannotCompleteException.class )
   public void validate_cannotCompleteException() throws Exception {

      iWorkPackageSignatureValidator.validate( VALID_SCHED_WP_SIGN_REQ_KEY,
            VALID_TASK_KEY_AOG_FAULT, VALID_HR_KEY, TODAY, true, "" );
   }


   @Test
   public void validate_cannotCompleteExceptionNotThrow() throws Exception {

      iWorkPackageSignatureValidator.validate( VALID_SCHED_WP_SIGN_REQ_KEY,
            VALID_TASK_KEY_AOG_FAULT, VALID_HR_KEY, TODAY, false, "" );
   }


   @Test( expected = ValidationException.class )
   public void validate_endDateInFutureValidator() throws Exception {

      Calendar lCalendar = Calendar.getInstance();
      lCalendar.setTime( TODAY );
      lCalendar.add( Calendar.DAY_OF_MONTH, 7 );

      iWorkPackageSignatureValidator.validate( VALID_SCHED_WP_SIGN_REQ_KEY, VALID_TASK_KEY,
            VALID_HR_KEY, lCalendar.getTime(), false, "" );
   }

}
