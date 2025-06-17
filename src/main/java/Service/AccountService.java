package Service;

import DAO.AccountDAO; 
import Model.Account;

public class AccountService {
    AccountDAO accountDAO; 

    /**
     * Gets the singleton instance of an AccountDao.
     */
    public AccountService() {
        accountDAO = AccountDAO.instance();
    }

    /**
     * Registers the account within the database. 
     * 
     * @param account object containing details of new account.
     * @return the newly registered account.
     */
    public Account registerAccount(Account account) {
        if (isValid(account)) {
            return this.accountDAO.registerAccount(account);
        }
        
        return null;
    }

    /**
     * Login to an existing account within the database. 
     * 
     * @param account object containing details of new account.
     * @return the authenticated account if login is successful; otherwise, null
     */
    public Account loginToAccount(Account account) {
        if (isValid(account)) {
            return this.accountDAO.loginToAccount(account);
        }
        
        return null; 
    }

    // Helper Methods 

    /**
     * Checks whether an account is within the database. 
     * 
     * @param account_id
     * @return true if the account is in the data; otherwise, false.
     */
    public boolean accountExists(int account_id) {
        return this.accountDAO.accountExists(account_id);
    }

    /**
     * Checks whether an account's password and username are valid. 
     * 1. Username must not be blank.
     * 2. Password must be greater than 4 characters. 
     * 
     * @param account_id
     * @return true if the account is valid; otherwise, false.
     */
    private boolean isValid(Account account) {
        String username = account.getUsername();
        String password = account.getPassword(); 

        if (password.length() < 4 || username.length() == 0) {
            return false; 
        }
        
        return true;
    }
}