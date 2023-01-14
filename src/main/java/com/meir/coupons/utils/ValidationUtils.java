package com.meir.coupons.utils;

import com.meir.coupons.enums.UserTypes;
import com.meir.coupons.exceptions.ApplicationException;
import com.meir.coupons.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.meir.coupons.enums.ErrorType.*;

@Component
public class ValidationUtils {

    private static IUserRepository userRepository;

    @Autowired
    public ValidationUtils(IUserRepository iUserRepository){
        userRepository = iUserRepository;
    }

    ///הפונקציה נקראית 11 פעמים
    public static void isTextLengthLegal(String text, int maxLength, int minLength) throws ApplicationException {
        isTextEmpty(text);
        if (text.length() > maxLength) {
            throw new ApplicationException(TEXT_TOO_LONG);
        }
        if (text.length() < minLength) {
            throw new ApplicationException(TEXT_TOO_SHORT);
        }
    }

    ///הפונקציה נקראית בפונקציה שמעל
    public static void isTextEmpty(String text) throws ApplicationException {
        if (text.isBlank()) {
            throw new ApplicationException(TEXT_IS_EMPTY);
        }
    }

    ///הפונקציה נקראית 7 פעמים
    public static void authorizationValidation(Integer applicantId, UserTypes userTypeAllowed) throws ApplicationException {
        if (!userRepository.existsById(applicantId)) {
            throw new ApplicationException(ID_DOES_NOT_EXIST);
        }
        if (userRepository.findById(applicantId).getUserType() != userTypeAllowed) {
            throw new ApplicationException(NO_AUTHORIZED);
        }
    }

    ///הפונקציה נקראית פעמיים
    public static void authorizationValidation(Integer applicantId, UserTypes userTypeAllowed1, UserTypes userTypeAllowed2) throws ApplicationException {
        if (!userRepository.existsById(applicantId)) {
            throw new ApplicationException(ID_DOES_NOT_EXIST);
        }
        UserTypes applicantUserType = userRepository.findById(applicantId).getUserType();
        if (applicantUserType != userTypeAllowed1 && applicantUserType != userTypeAllowed2) {
            throw new ApplicationException(NO_AUTHORIZED);
        }
    }
}
