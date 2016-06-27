package com.tekenable.tdsec2.controller;

import com.tekenable.tdsec2.dto.TdRegisterUserRequest;
import com.tekenable.tdsec2.infrastructure.dao.TdUserDao;
import com.tekenable.tdsec2.model.TdUser;
import com.tekenable.tdsec2.utils.Clock;
import com.tekenable.tdsec2.utils.TdRunException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by nbarrett on 27/06/2016.
 */
@Controller
public class RegisterUserController {


    private String validationMessage;

    @Autowired
    private TdUserDao tdUserDao;

    @Autowired
    private Clock clock;

    public RegisterUserController() {}


    /**
     * Register a new user on Trial Direct.
     *
     * @param tdRegisterUserRequest
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST)
    @Transactional(noRollbackFor = Exception.class)
    public void registerUser(@RequestBody TdRegisterUserRequest tdRegisterUserRequest,
                             HttpServletRequest request,
                             HttpServletResponse response) throws Exception {

        try {

            //1. Validate registration details
            if (!validateRegistrationDetails(tdRegisterUserRequest)) {

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                throw new TdRunException(TdRunException.ErrorCode.VALIDATION_ERROR_MANDATORY_FIELD_MISSING, this.validationMessage);
            }

            //2. Save registration details
            TdUser tdUser = new TdUser(tdRegisterUserRequest);
            this.tdUserDao.create(tdUser);

        } catch (Exception exc) {
            StringWriter writer = new StringWriter();
            exc.printStackTrace(new PrintWriter(writer));

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            throw exc;
        }
    }

    /**
     * Validate the registration details provided. Returns false if validation false, true otherwise.
     *
     * @param tdRegisterUserRequest
     * @return
     */
    private boolean validateRegistrationDetails(TdRegisterUserRequest tdRegisterUserRequest) {

        boolean result = true; //Assume true by default

        //1. Validate all mandatory fields were provided
        if (!checkForMandatoryFieldProvided(tdRegisterUserRequest.getTitle(), "Title"))
            return false;

        if (!checkForMandatoryFieldProvided(tdRegisterUserRequest.getFirstName(), "First Name"))
            return false;

        if (!checkForMandatoryFieldProvided(tdRegisterUserRequest.getLastName(), "Last Name"))
            return false;

        if (!checkForMandatoryFieldProvided(tdRegisterUserRequest.getPracticeName(), "Practice Name"))
            return false;

        if (!checkForMandatoryFieldProvided(tdRegisterUserRequest.getZipCode(), "Zip Code"))
            return false;

        if (!checkForMandatoryFieldProvided(tdRegisterUserRequest.getEmail(), "Email"))
            return false;

//        if (!checkForMandatoryFieldProvided(tdRegisterUserRequest.getConfirmEmail(), "Confirm Email"))
//            return false;

        if (!checkForMandatoryFieldProvided(tdRegisterUserRequest.getPassword(), "Password"))
            return false;

        if (!checkForMandatoryFieldProvided(tdRegisterUserRequest.getConfirmPassword(), "Confirm Password"))
            return false;

        //2. Validate that both email addresses are the same
//        if (!tdRegisterUserRequest.getEmail().equals(tdRegisterUserRequest.getConfirmEmail())) {
//            this.validationMessage = "The email addresses provided do not match!";
//            return false;
//        }

        if (!tdRegisterUserRequest.getPassword().equals(tdRegisterUserRequest.getConfirmPassword())) {
            this.validationMessage = "The passwords provided do not match!";
            return false;
        }


        //3. Validate the email address is unique and not already registered
        TdUser existingUserWithSameEmail = tdUserDao.retrieveByEmail(tdRegisterUserRequest.getEmail());

        if (existingUserWithSameEmail != null) {
            this.validationMessage = "A user already exists with the email address provided!";
            return false;
        }

        return result;
    }

    /**
     * Check mandatory fields for null or empty.
     * @param field
     * @param fieldName
     * @return
     */
    private boolean checkForMandatoryFieldProvided(String field, String fieldName) {

        boolean result = true;

        if (StringUtils.isEmpty(field)) {
            this.validationMessage = "Mandatory field:" + fieldName + " is empty, please provide a value!";
            result = false;
        }

        return result;
    }
}
