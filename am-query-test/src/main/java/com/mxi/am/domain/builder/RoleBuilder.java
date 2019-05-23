package com.mxi.am.domain.builder;

import java.util.Date;

import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.ejb.EjbFactory;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.tuple.Pair;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.RoleKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.key.UtlRoleParmKey;
import com.mxi.mx.core.key.UtlUserRoleKey;
import com.mxi.mx.core.table.utl.UtlRoleParm;
import com.mxi.mx.core.table.utl.UtlUserRole;
import com.mxi.mx.core.table.utl.UtlUserTempRole;


/**
 * Builds a <code>utl_role</code> or <code>utl_user_temp_role</code> object
 */
public class RoleBuilder {

   private boolean iTemporary = false;

   private UserKey iUser;

   private Pair<String, Boolean> iConfigParm;

   private Date iTempRoleStartDt;

   private Date iTempRoleEndDt;


   /**
    * {@inheritDoc}
    */
   public RoleKey build() {

      RoleKey lRoleKey = new RoleKey(
            EjbFactory.getInstance().createSequenceGenerator().nextValue( "UTL_ROLE_ID" ) );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lRoleKey, "role_id" );
      MxDataAccess.getInstance().executeInsert( "utl_role", lArgs );

      if ( iTemporary ) {
         UtlUserTempRole lUtlUserTempRole = UtlUserTempRole.create();
         lUtlUserTempRole.setUser( iUser.getId() );
         lUtlUserTempRole.setRole( lRoleKey.getRoleId() );
         lUtlUserTempRole
               .setStartDt( ( iTempRoleStartDt == null ) ? new Date() : iTempRoleStartDt );
         lUtlUserTempRole.setEndDt( ( iTempRoleEndDt == null )
               ? ( DateUtils.addDays( new Date(), 1 ) ) : iTempRoleEndDt );
         lUtlUserTempRole.insert();
      }

      if ( iUser != null ) {
         UtlUserRole lUtlUserRole =
               UtlUserRole.create( new UtlUserRoleKey( iUser.getId(), lRoleKey.getRoleId() ) );

         lUtlUserRole.setTemporary( iTemporary );
         lUtlUserRole.insert();
      }

      if ( iConfigParm != null ) {
         UtlRoleParmKey lUtlRoleParmKey = new UtlRoleParmKey( lRoleKey.getRoleId(),
               iConfigParm.getComponent1(), ParmTypeEnum.LOGIC );

         UtlRoleParm lNewUtlRoleParm = UtlRoleParm.create( lUtlRoleParmKey );
         lNewUtlRoleParm.setParmValue( iConfigParm.getComponent2() );
         lNewUtlRoleParm.insert();
      }

      return lRoleKey;
   }


   public RoleBuilder withUser( UserKey aUser ) {

      iUser = aUser;

      return this;
   }


   public RoleBuilder withConfigParm( String aConfigParm, boolean aValue ) {

      iConfigParm = new Pair<String, Boolean>( aConfigParm, aValue );

      return this;
   }


   public RoleBuilder isTemporary() {

      iTemporary = true;

      return this;
   }


   public RoleBuilder withTempRoleStartDate( Date aDate ) {

      iTempRoleStartDt = aDate;

      return this;
   }


   public RoleBuilder withTempRoleEndDate( Date aDate ) {

      iTempRoleEndDt = aDate;

      return this;
   }

}
