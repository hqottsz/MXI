
package com.mxi.mx.web.query.location.stationcapacity;

import java.text.ParseException;
import java.util.Date;

import com.mxi.mx.common.internationalization.StringBundles;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.EqpPartBaselineKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.ShiftKey;
import com.mxi.mx.core.key.ShiftPlanKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.testing.FakeStringBundles;


/**
 * Constants for the station capacity data
 *
 * @author jcimino
 */
public class StationCapacityData {

   /** DOCUMENT ME! */
   public static Date DAY_DT_1 = null;

   /** DOCUMENT ME! */
   public static Date DAY_DT_2 = null;

   /** DOCUMENT ME! */
   public static Date DAY_DT_3 = null;

   static {
      try {
         FakeStringBundles lStringBundlesStub = new FakeStringBundles();
         StringBundles.setSingleton( lStringBundlesStub );
         DAY_DT_1 = DateUtils.parseDefaultDateString( "01-JAN-2006" );
         DAY_DT_2 = DateUtils.parseDefaultDateString( "02-JAN-2006" );
         DAY_DT_3 = DateUtils.parseDefaultDateString( "03-JAN-2006" );
      } catch ( ParseException eException ) {
         ;
      }
   }


   /**
    * Assigns the task to the check
    *
    * @param aTask
    *           the task
    * @param aCheck
    *           the check
    * @param aParent
    *           the parent
    */
   public static void assignTaskToCheck( TaskKey aTask, TaskKey aCheck, TaskKey aParent ) {
      EvtEventTable lEvtEvent = EvtEventTable.findByPrimaryKey( aTask.getEventKey() );
      lEvtEvent.setHEvent( aCheck.getEventKey() );
      lEvtEvent.setNhEvent( aParent.getEventKey() );
      lEvtEvent.update();
   }


   /**
    * Unassigns the task from the check
    *
    * @param aTask
    *           the task
    */
   public static void unassignTaskFromCheck( TaskKey aTask ) {
      EvtEventTable lEvtEvent = EvtEventTable.findByPrimaryKey( aTask.getEventKey() );
      lEvtEvent.setHEvent( null );
      lEvtEvent.setNhEvent( null );
      lEvtEvent.update();
   }


   /**
    * Returns the value of the data file property.
    *
    * @return the value of the data file property.
    */
   public static String getDataFile() {
      return "/com/mxi/mx/web/query/location/stationcapacity/StationCapacityData.xml";
   }


   /**
    * DOCUMENT ME!
    *
    * @author jcimino
    */
   public static class Aircraft {

      /** DOCUMENT ME! */
      public static final AircraftKey AC_1 = new AircraftKey( 4650, 1 );

      /** DOCUMENT ME! */
      public static final String AC_1_DESCRIPTION = "aircraft 1";

      /** DOCUMENT ME! */
      public static final AircraftKey AC_2 = new AircraftKey( 4650, 2 );

      /** DOCUMENT ME! */
      public static final String AC_2_DESCRIPTION = "aircraft 2";

      /** DOCUMENT ME! */
      public static final AircraftKey AC_3 = new AircraftKey( 4650, 3 );

      /** DOCUMENT ME! */
      public static final String AC_3_DESCRIPTION = "aircraft 3";

      /** DOCUMENT ME! */
      public static final AircraftKey AC_4 = new AircraftKey( 4650, 4 );

      /** DOCUMENT ME! */
      public static final String AC_4_DESCRIPTION = "aircraft 4";
   }

   /**
    * DOCUMENT ME!
    *
    * @author jcimino
    */
   public static class Check {

      /** DOCUMENT ME! */
      public static final TaskKey CHECK_ON_AIRCRAFT_1_ON_DAY_1 = new TaskKey( 4650, 1 );

      /** DOCUMENT ME! */
      public static final TaskKey CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT = new TaskKey( 4650, 2 );

      /** DOCUMENT ME! */
      public static final String CHECK_ON_AIRCRAFT_1_ON_DAY_1_SDESC = "check on aircraft 1";

      /** DOCUMENT ME! */
      public static final String CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT_SDESC =
            "requirement on check 1";
   }

   /**
    * DOCUMENT ME!
    *
    * @author jcimino
    */
   public static class Location {

      /** DOCUMENT ME! */
      public static final LocationKey YOW = new LocationKey( 4650, 1 );

      /** DOCUMENT ME! */
      public static final LocationKey YOW_LINE = new LocationKey( 4650, 2 );

      /** DOCUMENT ME! */
      public static final String YOW_LINE_CODE = "YOW/LINE";

      /** DOCUMENT ME! */
      public static final String YOW_LINE_CODE_NAME = "YOW/LINE (Ottawa Line)";

      /** DOCUMENT ME! */
      public static final String YOW_CODE = "YOW";

      /** DOCUMENT ME! */
      public static final String YOW_CODE_NAME = "YOW (Ottawa)";

      /** DOCUMENT ME! */
      public static final ShiftKey YOW_SHIFT_1 = new ShiftKey( 4650, 1 );

      /** DOCUMENT ME! */
      public static final ShiftKey YOW_SHIFT_2 = new ShiftKey( 4650, 2 );

      /** DOCUMENT ME! */
      public static final double YOW_SHIFT_1_WORK_HOURS = 10;

      /** DOCUMENT ME! */
      public static final double YOW_SHIFT_2_WORK_HOURS = 10;

      /** DOCUMENT ME! */
      public static final LocationKey YYZ = new LocationKey( 4650, 3 );

      /** DOCUMENT ME! */
      public static final String YYZ_CODE = "YYZ";

      /** DOCUMENT ME! */
      public static final String YYZ_CODE_NAME = "YYZ (Toronto)";

      /** DOCUMENT ME! */
      public static final LocationKey YYZ_LINE = new LocationKey( 4650, 4 );

      /** DOCUMENT ME! */
      public static final String YYZ_LINE_CODE = "YYZ/LINE";

      /** DOCUMENT ME! */
      public static final String YYZ_LINE_CODE_NAME = "YYZ/LINE (Toronto Line)";
   }

   /**
    * DOCUMENT ME!
    *
    * @author jcimino
    */
   public static class Part {

      /** DOCUMENT ME! */
      public static final PartGroupKey PART_GROUP_1 = new PartGroupKey( 4650, 3 );

      /** DOCUMENT ME! */
      public static final String PART_GROUP_1_CODE = "part 1 code";

      /** DOCUMENT ME! */
      public static final String PART_GROUP_1_NAME = "part 1 name";

      /** DOCUMENT ME! */
      public static final PartNoKey PART_NO_1 = new PartNoKey( 4650, 3 );

      /** DOCUMENT ME! */
      public static final EqpPartBaselineKey PART_BASELINE_1 =
            new EqpPartBaselineKey( PART_GROUP_1, PART_NO_1 );

      /** DOCUMENT ME! */
      public static final PartGroupKey PART_GROUP_2 = new PartGroupKey( 4650, 4 );

      /** DOCUMENT ME! */
      public static final String PART_GROUP_2_CODE = "part 2 code";

      /** DOCUMENT ME! */
      public static final String PART_GROUP_2_NAME = "part 2 name";

      /** DOCUMENT ME! */
      public static final PartNoKey PART_NO_2 = new PartNoKey( 4650, 4 );

      /** DOCUMENT ME! */
      public static final EqpPartBaselineKey PART_BASELINE_2 =
            new EqpPartBaselineKey( PART_GROUP_2, PART_NO_2 );

      /** DOCUMENT ME! */
      public static final PartGroupKey PART_GROUP_3 = new PartGroupKey( 4650, 5 );

      /** DOCUMENT ME! */
      public static final String PART_GROUP_3_CODE = "part 3 code";

      /** DOCUMENT ME! */
      public static final String PART_GROUP_3_NAME = "part 3 name";

      /** DOCUMENT ME! */
      public static final PartNoKey PART_NO_3 = new PartNoKey( 4650, 5 );

      /** DOCUMENT ME! */
      public static final EqpPartBaselineKey PART_BASELINE_3 =
            new EqpPartBaselineKey( PART_GROUP_3, PART_NO_3 );
   }

   /**
    * DOCUMENT ME!
    *
    * @author jcimino
    */
   public static class Task {

      /**
       * DOCUMENT ME!
       *
       * @author jcimino
       */
      public static class Labour {

         /** DOCUMENT ME! */
         public static final TaskKey TASK_WITH_AVAILABLE_LABOUR_ON_DAY_1 = new TaskKey( 4650, 10 );

         /** DOCUMENT ME! */
         public static final RefLabourSkillKey LABOUR_SKILL_TASK_WITH_AVAILABLE_LABOUR_ON_DAY_1 =
               RefLabourSkillKey.LBR;

         /** DOCUMENT ME! */
         public static final TaskKey TASK_WITH_WARNING_LABOUR_ON_DAY_1 = new TaskKey( 4650, 11 );

         /** DOCUMENT ME! */
         public static final RefLabourSkillKey LABOUR_SKILL_TASK_WITH_WARNING_LABOUR_ON_DAY_1 =
               RefLabourSkillKey.ENG;

         /** DOCUMENT ME! */
         public static final TaskKey TASK_WITH_UNAVAILABLE_LABOUR_ON_DAY_1 =
               new TaskKey( 4650, 12 );

         /** DOCUMENT ME! */
         public static final RefLabourSkillKey LABOUR_SKILL_TASK_UNAVAILABLE_LABOUR_ON_DAY_1 =
               RefLabourSkillKey.ENG;
      }

      /**
       * DOCUMENT ME!
       *
       * @author jcimino
       */
      public static class Part {

         /** DOCUMENT ME! */
         public static final TaskKey TASK_WITH_AVAILABLE_PART_ON_DAY_1 = new TaskKey( 4650, 6 );

         /** DOCUMENT ME! */
         public static final TaskKey TASK_WITH_AVAILABLE_BUT_RESERVED_PART_ON_DAY_1 =
               new TaskKey( 4650, 7 );

         /** DOCUMENT ME! */
         public static final TaskKey TASK_WITH_AVAILABLE_BUT_ISSUED_PART_ON_DAY_1 =
               new TaskKey( 4650, 8 );
      }

      /**
       * DOCUMENT ME!
       *
       * @author jcimino
       */
      public static class Tool {

         /** DOCUMENT ME! */
         public static final TaskKey TASK_WITH_AVAILABLE_TOOL_ON_DAY_1 = new TaskKey( 4650, 3 );

         /** DOCUMENT ME! */
         public static final TaskKey TASK_WITH_UNAVAILABLE_TOOL_ON_DAY_1 = new TaskKey( 4650, 4 );
      }
   }

   /**
    * DOCUMENT ME!
    *
    * @author jcimino
    */
   public static class Tool {

      /** DOCUMENT ME! */
      public static final PartGroupKey TOOL_PART_GROUP_1 = new PartGroupKey( 4650, 1 );

      /** DOCUMENT ME! */
      public static final String TOOL_PART_GROUP_1_CODE = "tool 1 code";

      /** DOCUMENT ME! */
      public static final String TOOL_PART_GROUP_1_NAME = "tool 1 name";

      /** DOCUMENT ME! */
      public static final PartNoKey TOOL_PART_NO_1 = new PartNoKey( 4650, 1 );

      /** DOCUMENT ME! */
      public static final EqpPartBaselineKey TOOL_PART_BASELINE_1 =
            new EqpPartBaselineKey( TOOL_PART_GROUP_1, TOOL_PART_NO_1 );

      /** DOCUMENT ME! */
      public static final PartGroupKey TOOL_PART_GROUP_2 = new PartGroupKey( 4650, 2 );

      /** DOCUMENT ME! */
      public static final String TOOL_PART_GROUP_2_CODE = "tool 2 code";

      /** DOCUMENT ME! */
      public static final String TOOL_PART_GROUP_2_NAME = "tool 2 name";

      /** DOCUMENT ME! */
      public static final PartNoKey TOOL_PART_NO_2 = new PartNoKey( 4650, 2 );

      /** DOCUMENT ME! */
      public static final EqpPartBaselineKey TOOL_PART_BASELINE_2 =
            new EqpPartBaselineKey( TOOL_PART_GROUP_2, TOOL_PART_NO_2 );
   }

   /**
    * DOCUMENT ME!
    *
    * @author jcimino
    */
   public static class User {

      /** DOCUMENT ME! */
      public static final UserKey USER_1 = new UserKey( 1 );

      /** DOCUMENT ME! */
      public static final String USER_1_FIRST_NAME = "user 1 first name";

      /** DOCUMENT ME! */
      public static final String USER_1_LAST_NAME = "user 1 last name";

      /** DOCUMENT ME! */
      public static final HumanResourceKey HR_1 = new HumanResourceKey( 4650, 1 );

      /** DOCUMENT ME! */
      public static final RefLabourSkillKey HR_1_LABOUR_SKILL_1 = RefLabourSkillKey.PILOT;

      /** DOCUMENT ME! */
      public static final RefLabourSkillKey HR_1_LABOUR_SKILL_2 = RefLabourSkillKey.LBR;

      /** DOCUMENT ME! */
      public static final UserKey USER_2 = new UserKey( 2 );

      /** DOCUMENT ME! */
      public static final String USER_2_FIRST_NAME = "user 2 first name";

      /** DOCUMENT ME! */
      public static final String USER_2_LAST_NAME = "user 2 last name";

      /** DOCUMENT ME! */
      public static final HumanResourceKey HR_2 = new HumanResourceKey( 4650, 2 );

      /** DOCUMENT ME! */
      public static final RefLabourSkillKey HR_2_LABOUR_SKILL_1 = RefLabourSkillKey.ENG;

      /** DOCUMENT ME! */
      public static final RefLabourSkillKey HR_2_LABOUR_SKILL_2 = RefLabourSkillKey.LBR;

      /** DOCUMENT ME! */
      public static final UserKey USER_3 = new UserKey( 3 );

      /** DOCUMENT ME! */
      public static final String USER_3_FIRST_NAME = "user 3 first name";

      /** DOCUMENT ME! */
      public static final String USER_3_LAST_NAME = "user 3 last name";

      /** DOCUMENT ME! */
      public static final HumanResourceKey HR_3 = new HumanResourceKey( 4650, 3 );

      /** DOCUMENT ME! */
      public static final RefLabourSkillKey HR_3_LABOUR_SKILL_1 = RefLabourSkillKey.LBR;

      /** DOCUMENT ME! */
      public static final RefLabourSkillKey HR_3_LABOUR_SKILL_2 = RefLabourSkillKey.ENG;

      /** DOCUMENT ME! */
      public static final ShiftPlanKey HR_1_SHIFT_PLAN_1 = new ShiftPlanKey( HR_1, 1 );

      /** DOCUMENT ME! */
      public static final ShiftPlanKey HR_1_SHIFT_PLAN_2 = new ShiftPlanKey( HR_1, 2 );

      /** DOCUMENT ME! */
      public static final RefLabourSkillKey HR_1_SHIFT_PLAN_1_LABOUR_SKILL = RefLabourSkillKey.LBR;

      /** DOCUMENT ME! */
      public static final RefLabourSkillKey HR_1_SHIFT_PLAN_2_LABOUR_SKILL = RefLabourSkillKey.LBR;

      /** DOCUMENT ME! */
      public static final ShiftPlanKey HR_2_SHIFT_PLAN_1 = new ShiftPlanKey( HR_2, 1 );

      /** DOCUMENT ME! */
      public static final RefLabourSkillKey HR_2_SHIFT_PLAN_1_LABOUR_SKILL = RefLabourSkillKey.ENG;

      /** DOCUMENT ME! */
      public static final ShiftPlanKey HR_3_SHIFT_PLAN_1 = new ShiftPlanKey( HR_3, 1 );

      /** DOCUMENT ME! */
      public static final RefLabourSkillKey HR_3_SHIFT_PLAN_1_LABOUR_SKILL = RefLabourSkillKey.LBR;
   }
}
