
package com.mxi.mx.core.dao.lrp;

import com.mxi.mx.model.key.CdKeyImpl;
import com.mxi.mx.model.lrp.GantConfiguration;
import com.mxi.mx.model.lrp.GantConfiguration.ConfigType;
import com.mxi.mx.model.lrp.Severity;


/**
 * Generic Test Data for LRP DAO tests.
 *
 * @author slevert
 */
public class TestData {

   public GantConfiguration getGanttConfiguration() {
      Severity lSeverity = new Severity( "description", "name", "red" );
      lSeverity.setPrimaryKey( new CdKeyImpl( 10, "CRITICAL" ) ); // ??

      GantConfiguration lConfig = new GantConfiguration();
      lConfig.addSeverity( ConfigType.ADHOC, lSeverity );
      lConfig.addSeverity( ConfigType.DURGRTMAX, lSeverity );
      lConfig.addSeverity( ConfigType.DURLESSMIN, lSeverity );
      lConfig.addSeverity( ConfigType.EXTDEAD, lSeverity );
      lConfig.addSeverity( ConfigType.OVERDUE, lSeverity );
      lConfig.addSeverity( ConfigType.OVERFLOW, lSeverity );
      lConfig.addSeverity( ConfigType.OVERLAP, lSeverity );
      lConfig.addSeverity( ConfigType.PLANAFTMAXYD, lSeverity );
      lConfig.addSeverity( ConfigType.PLANBFOREMINYD, lSeverity );
      lConfig.addSeverity( ConfigType.OUT_OF_SEQUENCE, lSeverity );
      lConfig.addSeverity( ConfigType.UPDATE_TO_ACTUALS, lSeverity );
      lConfig.addSeverity( ConfigType.EXTR_LIMIT_EXCEEDED, lSeverity );
      lConfig.addSeverity( ConfigType.PRIOR_EFFECTIVE_DATE, lSeverity );
      lConfig.addSeverity( ConfigType.READ_ONLY, lSeverity );
      lConfig.addSeverity( ConfigType.OVERRUNBKTS, lSeverity );

      return lConfig;
   }
}
