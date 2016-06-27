package com.tekenable.tdsec2.controller;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import com.google.common.net.HttpHeaders;
import com.tekenable.tdsec2.dto.TrialDirectLoginRequest;
import com.tekenable.tdsec2.model.LoginForm;
import com.tekenable.tdsec2.model.TdLoginAttempt;
import com.tekenable.tdsec2.model.TdLoginAttemptStatus;
import com.tekenable.tdsec2.model.TdUser;
import com.tekenable.tdsec2.service.TdAuthService;
import com.tekenable.tdsec2.utils.Clock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

/**
 * Created by nbarrett on 20/06/2016.
 */

@Controller
public class LogonController {

    public static final String TD_AUTH_COOKIE = "TdAuth";

    public static final String SESSION_AUTH_COOKIE = "Session-";
    public static final String COOKIE_SECURED = "trialDirect.cookie.isSecured";


    @Autowired
    private TdAuthService tdAuthService;

    @Autowired
    private Clock clock;

    @Autowired
    @Qualifier("sharedConfigProperties")
    private Properties sharedConfigProperties;

//    @Autowired
//    private AuthenticationHolder authenticationHolder;


    @RequestMapping(value = { "/", "/signin**" }, method = RequestMethod.GET)
    public ModelAndView welcomePage() {

        ModelAndView model = new ModelAndView();
        model.addObject("title", "Spring Security Hello World");
        model.addObject("message", "This is welcome page!");
        model.addObject("command", new TrialDirectLoginRequest());
        model.setViewName("login");

        return model;

    }

    /**
     * create patient auth cookie, login user.
     *
     * @param tdLoginRequest - td login request json
     * @param request             - request
     * @param response            - response
     * @throws Exception - exception
     */

//    public void login(@RequestBody TrialDirectLoginRequest tdLoginRequest,
//                      HttpServletRequest request,
//                      HttpServletResponse response) throws Exception {
    @RequestMapping(value = "/global/logins", method = RequestMethod.POST)
    @Transactional(noRollbackFor = Exception.class)
    public String login(@ModelAttribute TrialDirectLoginRequest tdLoginRequest,
                      HttpServletRequest request,
                      HttpServletResponse response) throws Exception {

        TdLoginAttempt loginAttempt = createLoginAttempt(tdLoginRequest, request);

        try {
            doLogin(tdLoginRequest, loginAttempt, response);
        } catch (Exception exc) {
            StringWriter writer = new StringWriter();
            exc.printStackTrace(new PrintWriter(writer));
            loginAttempt.setAttemptStatus(TdLoginAttemptStatus.EXCEPTION);
            loginAttempt.setException(writer.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            throw exc;
        } finally {
            //TODO: implement in future to track logon attempts
            //patientLoginAttemptDao.save(loginAttempt);
        }

        return "index";
    }


    /**
     * creates login attempt.
     * @param tdLoginRequest   - patient login request
     * @param request               - request
     * @return  {@link TdLoginAttempt} object
     */
    @VisibleForTesting
    public TdLoginAttempt createLoginAttempt(TrialDirectLoginRequest tdLoginRequest,
                                                  HttpServletRequest request) {
        return TdLoginAttempt.Builder.newInstance()
                .setIpAddress(request.getRemoteAddr())
                .setDate(clock.now())
                .setIpAddress(request.getRemoteAddr())
                .setEmail(tdLoginRequest.getEmail())
                .setUserAgent(request.getHeader(HttpHeaders.USER_AGENT))
                .setAcceptLanguage(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE))
                .build();
    }

    /**
     *
     * @param plr
     * @param attempt
     * @param response
     */
    private void doLogin(TrialDirectLoginRequest plr, TdLoginAttempt attempt, HttpServletResponse response) {

        String uuid = tdAuthService.login(plr.getEmail(), plr.getPassword(), attempt);

        if (uuid == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Cookie tdUserAuthCookie = new Cookie(TD_AUTH_COOKIE, uuid);
        tdUserAuthCookie.setSecure(isCookieSecured());
        tdUserAuthCookie.setMaxAge(-1);
        tdUserAuthCookie.setPath("/");
        // TODO: servlet 3.0 - tomcat 7.0+ needed!
        // tdUserAuthCookie.setHttpOnly(true);

        response.addCookie(tdUserAuthCookie);

        TdUser tdUser = tdAuthService.getTdUserDao().retrieveByEmail(plr.getEmail());

        //Append the user type to the cookie name, the ui can use this to determine the user type.
        Cookie sessionAuthCookie = new Cookie(SESSION_AUTH_COOKIE + tdUser.getUserType(), uuid);
        sessionAuthCookie.setSecure(isCookieSecured());
        sessionAuthCookie.setMaxAge(0);
        sessionAuthCookie.setValue("");

        // TODO: servlet 3.0 - tomcat 7.0+ needed!
        // sessionAuthCookie.setHttpOnly(true);

        response.addCookie(sessionAuthCookie);

        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    /**
     * Logs currently logged in user.
     *
     * @param response  - response
     */
    //TODO: method = RequestMethod.DELETE for angular logout
    @RequestMapping(value = "/global/logins/current", method = RequestMethod.POST)
    @Transactional
    public ModelAndView logout(HttpServletResponse response) {

//        if( getAuthenticationHolder().getAuthentication() instanceof PatientAuthenticationToken ) {
//            PatientAuthenticationToken auth = (PatientAuthenticationToken) getAuthenticationHolder().getAuthentication();
//            tdAuthService.logout(auth.getCookieUuid());
//        }

        tdAuthService.logout(null); //Passing null for now as we dont know the cookie id

        Cookie patientAuthCookie = new Cookie(TD_AUTH_COOKIE, "");
        patientAuthCookie.setSecure(isCookieSecured());
        patientAuthCookie.setMaxAge(0);
        patientAuthCookie.setPath("/");
        // TODO: servlet 3.0 - tomcat 7.0+ needed!
        // patientAuthCookie.setHttpOnly(true);
        response.addCookie(patientAuthCookie);

        ModelAndView model = new ModelAndView();
        model.addObject("title", "Spring Security Hello World");
        model.addObject("message", "This is welcome page!");
        model.addObject("command", new TrialDirectLoginRequest());
        model.setViewName("login");

        return model;
    }

    /**
     *
     * @return
     */
    private boolean isCookieSecured() {
        String property = sharedConfigProperties.getProperty(COOKIE_SECURED);
        return Strings.isNullOrEmpty(property) || Boolean.parseBoolean(property);
    }

    @RequestMapping(value = "/loginForm", method = RequestMethod.POST)
    public String processForm(@ModelAttribute("SpringWeb") LoginForm loginForm, ModelMap modelMap) {

        String userName = "Admin";
        String password = "root";

        modelMap.addAttribute("userName", loginForm.getUserName());
        modelMap.addAttribute("password", loginForm.getPassword());


//        if (result.hasErrors()) {
//            return "loginform";
//
//        }
//        loginForm = (LoginForm) model.get("loginForm");
//        if (!loginForm.getUserName().equals(userName)
//                || !loginForm.getPassword().equals(password)) {
//            return "loginerror";
//        }
//
//        model.put("loginForm", loginForm);
        return "index";
    }

    @RequestMapping(value="logon/{name}", method = RequestMethod.GET)
    public String getMovie(@PathVariable String name, ModelMap model) {

        model.addAttribute("movie", name);
        return "list";

    }

    /**
     * Sample rest call: User: Joseph Vartuli
     * @param id
     * @return
     */
//    @RequestMapping("/json/{id}")
//    public @ResponseBody
//    JsonRestObject getJsonResponse(@PathVariable Integer id) {
//        id += 200;
//        return new JsonRestObject(id);
//    }


}
