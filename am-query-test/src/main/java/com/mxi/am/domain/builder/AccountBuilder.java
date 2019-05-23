
package com.mxi.am.domain.builder;

import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.FncTCodeKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.table.fnc.FncAccount;


/**
 * Builds a <code>fnc_account</code> object
 */
public class AccountBuilder implements DomainBuilder<FncAccountKey> {

   private RefAccountTypeKey iAccountType;
   private String iCode;
   private boolean iDefault = false;
   private FncTCodeKey iFncTCodeKey;
   private String iExternalReference;


   /**
    * Creates a new {@linkplain AccountBuilder} object.
    */
   public AccountBuilder() {
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public FncAccountKey build() {
      FncAccount lFncAccount = FncAccount.create();

      lFncAccount.setAccountType( iAccountType );
      lFncAccount.setDefault( iDefault );
      lFncAccount.setAccountCd( iCode );
      if ( iFncTCodeKey != null ) {
         lFncAccount.setTCode( iFncTCodeKey );
      }
      if ( iExternalReference != null ) {
         lFncAccount.setAccountExtRefSdesc( iExternalReference );
      }
      return lFncAccount.insert();
   }


   /**
    * Sets the account to the default.
    *
    * @return The builder
    */
   public AccountBuilder isDefault() {
      iDefault = true;

      return this;
   }


   /**
    * Sets the account code.
    *
    * @param aCode
    *           Account Code
    *
    * @return The builder
    */
   public AccountBuilder withCode( String aCode ) {
      iCode = aCode;

      return this;
   }


   /**
    * Sets the account type.
    *
    * @param aAccountType
    *           The type
    *
    * @return The builder
    */
   public AccountBuilder withType( RefAccountTypeKey aAccountType ) {
      iAccountType = aAccountType;

      return this;
   }


   /**
    * Sets the tcode
    *
    * @param aFncTCodeKey
    *           The tcode
    *
    * @return The builder
    */
   public AccountBuilder withTCode( FncTCodeKey aFncTCodeKey ) {
      iFncTCodeKey = aFncTCodeKey;

      return this;
   }


   /**
    * Sets the external reference
    *
    * @param aExternalKey
    *           The external reference
    *
    * @return The builder
    */
   public AccountBuilder withExternalKey( String aExternalReference ) {
      iExternalReference = aExternalReference;

      return this;
   }
}
