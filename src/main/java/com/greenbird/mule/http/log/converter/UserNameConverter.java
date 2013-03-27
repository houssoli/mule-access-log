package com.greenbird.mule.http.log.converter;

import org.apache.log4j.helpers.FormattingInfo;
import org.mule.RequestContext;
import org.mule.api.MuleMessage;
import org.mule.api.security.SecurityContext;
import org.springframework.security.core.userdetails.User;

public class UserNameConverter extends AbstractAccessLogConverter {
    public final static char CONVERSION_CHARACTER = 'u'; 

    public UserNameConverter(FormattingInfo formattingInfo, String defaultValue) {
        super(formattingInfo, defaultValue);
    }

    @Override
    protected String doConvert(MuleMessage message) {
        String user = null;
        SecurityContext securityContext = RequestContext.getEvent().getSession().getSecurityContext();
        if (securityContext != null) {
            Object principal = securityContext.getAuthentication().getPrincipal();
            if (principal instanceof User) {
                user = ((User) principal).getUsername();
            }
        }
        return user;
    }
}
