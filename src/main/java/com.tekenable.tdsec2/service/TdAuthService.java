package com.tekenable.tdsec2.service;

import com.tekenable.tdsec2.auth.TdAuthenticationToken;
import com.tekenable.tdsec2.infrastructure.dao.TdLoginDao;
import com.tekenable.tdsec2.infrastructure.dao.TdUserDao;
import com.tekenable.tdsec2.model.*;
import com.tekenable.tdsec2.utils.Clock;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.apache.commons.lang.time.DateUtils.addMinutes;

/**
 * Created by nbarrett on 22/06/2016.
 */
@Service
public class TdAuthService {

    Log LOGGER = LogFactoryImpl.getLog(TdAuthService.class);

    @Autowired
    private TdLoginDao tdLoginDao;

    @Autowired
    private TdUserDao tdUserDao;

    @Autowired
    private Clock clock;

    public void setTdLoginDao(TdLoginDao tdLoginDao) {
        this.tdLoginDao = tdLoginDao;
    }

    public TdUserDao getTdUserDao() {
        return tdUserDao;
    }

    public void setTdUserDao(TdUserDao tdUserDao) {
        this.tdUserDao = tdUserDao;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    /**
     *
     * @param email
     * @param password
     * @param attempt
     * @return
     */
    public String login(String email, String password, TdLoginAttempt attempt) {

        TdUser tdUser = tdUserDao.retrieveByEmail(email);

        //Find attached instance
        if (tdUser != null)
            tdUser = tdUserDao.find(tdUser.getPk());

        if (tdUser == null) {
            attempt.setAttemptStatus(TdLoginAttemptStatus.BAD_USERNAME);
            return null;
        }

        //if(tdUser.getPassword() == null || !tdUser.getPassword().validatePassword(password)) {
        if(tdUser.getTdPassword() == null || !tdUser.getTdPassword().equals(password)) {
            attempt.setAttemptStatus(TdLoginAttemptStatus.BAD_PASSWORD);
            return null;
        }

        TdLogin login = new TdLogin();
        login.setId(UUID.randomUUID().toString());
        login.setExpirationDate(addMinutes(clock.now(), 20));
        login.setStatus(TdLoginStatus.ACTIVE);
        login.setAttempt(attempt);
        login.setTdUser(tdUser);

//        List<TdLogin> loginList = new ArrayList<TdLogin>(1);
//        loginList.add(login);
//        tdUser.setLogins(loginList);

        login.getAttempt().setAttemptStatus(TdLoginAttemptStatus.SUCCESS);
        tdLoginDao.save(login);

        return login.getId();
    }
    //CSON: ParameterNumber

    /**
     * checks whether login is valid.
     *
     * @param cookieUuid - cookie uuid
     * @param realmId    - realm id
     * @return true if login session is still valid
     */
    public TdLogin verify(String cookieUuid, String realmId) {

        TdLogin tdLogin = tdLoginDao.retrieveByCookieUuid(cookieUuid);

        if (tdLogin == null) {
            LOGGER.warn("Cookie not found: {}" + cookieUuid);
            return null;
        }
        if (tdLogin.getExpirationDate().before(clock.now())) {
            LOGGER.info("Expired: {}"+ cookieUuid);
            return null;
        }
        if (tdLogin.getStatus() != TdLoginStatus.ACTIVE) {
            LOGGER.info("Cookie {} in bad status {}"+ tdLogin.getStatus() + ":" + cookieUuid);
            return null;
        }


        // successful
        tdLogin.setExpirationDate(addMinutes(clock.now(), 20));
        return tdLogin;
    }

    /**
     * sets status of cookie to logged out.
     * @param cookieUuid - cookie uuid
     */
    public void logout(String cookieUuid) {

        if (cookieUuid == null) {
            //Lookup the db and find any active cookies for the user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication instanceof TdAuthenticationToken) {
                TdAuthenticationToken authenticationToken = (TdAuthenticationToken) authentication;
                System.out.println("cookie id:" +authenticationToken.getCookieUuid());
                cookieUuid = authenticationToken.getCookieUuid();
            }

            User user = (User) authentication.getPrincipal();
            String name = user.getUsername(); //get logged in username
            System.out.println("name:" + name);

            //Get the users cookie from the db. (this is a hack until we have a proper way to store the TdAuthenticationToken
            TdUser tdUser = this.tdUserDao.findByEmail(name);
            TdLogin tdLogin = tdLoginDao.findMostRecentForUserId(tdUser.getPk());
            cookieUuid = tdLogin.getId();

        }

        TdLogin tdLogin = tdLoginDao.retrieveByCookieUuid(cookieUuid);
        tdLogin.setStatus(TdLoginStatus.LOGGED_OUT);
        tdLogin.setExpirationDate(clock.now());
    }

    /**
     * Returns a TdLoginValidationResponse.
     * @param cookieUuid The id value from the cookie.
     * @param realmId The realm the login is within.
     * @return The login info.
     */
    public TdLoginValidationResponse verifyLogin(final String cookieUuid, final String realmId) {

        TdLogin tdLogin = verify(cookieUuid, realmId);
        TdLoginValidationResponse loginInfo = null;

        if (tdLogin != null) {
            String email = tdLogin.getTdUser().getSecuredData().getEmail();
            String name = tdLogin.getTdUser().getSecuredData().getFirstName();
            String userType = tdLogin.getTdUser().getUserType();

            loginInfo = new TdLoginValidationResponse(email, name, userType);
        }
        return loginInfo;
    }
}
