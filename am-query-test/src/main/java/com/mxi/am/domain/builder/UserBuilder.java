package com.mxi.am.domain.builder;

import com.mxi.am.domain.User;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.ejb.EjbFactory;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.Table;
import com.mxi.mx.core.key.UserKey;


/**
 * Domain builder for the user entity
 *
 */
public class UserBuilder {

   private static final String UTL_USER_TABLE = "utl_user";
   private static final String FIRST_NAME_COLUMN = "first_name";
   private static final String LAST_NAME_COLUMN = "last_name";
   private static final String USERNAME_COLUMN = "username";
   private static final String UTL_ID_COLUMN = "utl_id";
   private static final String USER_ID_COLUMN = "user_id";
   private static final String ALT_ID_COLUMN = "alt_id";
   private static final String MIDDLE_NAME_COLUMN = "middle_name";
   private static final String EMAIL_ID_COLUMN = "email_addr";


   public static UserKey build( User aUser ) {

      // Create the user in utl_user
      int lUtlId = Table.Util.getDatabaseId();
      int lUserId = ( aUser.getUserId() != null ) ? aUser.getUserId()
            : EjbFactory.getInstance().createSequenceGenerator().nextValue( "UTL_USER_ID" );
      UserKey lUserKey = new UserKey( lUserId );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lUserKey, USER_ID_COLUMN );
      lArgs.add( FIRST_NAME_COLUMN, aUser.getFirstName() );
      lArgs.add( LAST_NAME_COLUMN, aUser.getLastName() );
      lArgs.add( USERNAME_COLUMN, aUser.getUsername() );
      lArgs.add( UTL_ID_COLUMN, lUtlId );
      lArgs.add( MIDDLE_NAME_COLUMN, aUser.getMiddleName() );
      lArgs.add( EMAIL_ID_COLUMN, aUser.getEmailId() );

      if ( aUser.getAltId() != null ) {
         lArgs.add( ALT_ID_COLUMN, aUser.getAltId() );
      }

      if ( MxDataAccess.getInstance().executeInsert( UTL_USER_TABLE, lArgs ) == 1 ) {
         return lUserKey;
      }
      throw new RuntimeException( "Error creating user" );

   }

}
