
package com.mxi.am.ee;

import org.junit.rules.ExternalResource;

import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.UserKey;


/**
 * A rule that sets up the environment to be 'operating as' the userid specified
 */
public final class OperateAsUserRule extends ExternalResource {

   private final int iUserId;
   private final String iUsername;


   public OperateAsUserRule(int aUserId, String aUsername) {
      iUserId = aUserId;
      iUsername = aUsername;
   }


   @Override
   protected void before() {
      // Override user parameter types
      SecurityIdentificationUtils
            .setInstance( new SecurityIdentificationStub( new UserKey( iUserId ), iUsername ) );

      for ( ParmTypeEnum lParmType : ParmTypeEnum.values() ) {
         UserParameters.setInstance( iUserId, lParmType.name(),
               new UserParametersFake( iUserId, lParmType.name() ) );
      }
   }


   @Override
   protected void after() {
      SecurityIdentificationUtils.setInstance( null );
      for ( ParmTypeEnum lParmType : ParmTypeEnum.values() ) {
         UserParameters.setInstance( iUserId, lParmType.name(), null );
      }
   }
}
