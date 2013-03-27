package com.greenbird.mule.http.log.converter;

import org.junit.Test;
import org.mule.api.MuleSession;
import org.mule.module.spring.security.SpringAuthenticationAdapter;
import org.mule.security.DefaultSecurityContext;
import org.mule.session.DefaultMuleSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserNameConverterTest extends AccessLogConverterTestBase {
    private static final String TEST_USER = "testUser";

    @Test
    public void doConvert_springPrincipalAvailable_userNameFound() throws Exception {
        Collection<? extends GrantedAuthority> emptyList = Collections.emptyList();
        String logOutput = logWithSession(UserNameConverter.CONVERSION_CHARACTER,
                securedSession(new User(TEST_USER, "pw", emptyList)));
        assertThat(logOutput, is(TEST_USER));
    }

    @Test
    public void doConvert_unknownPrincipalType_userNameNotFoundAndDefaultValueUsed() throws Exception {
        String logOutput = logWithSession(UserNameConverter.CONVERSION_CHARACTER, securedSession(new Object()));
        assertThat(logOutput, is(DEFAULT_VALUE));
    }

    @Test
    public void doConvert_principalMissing_userNameNotFoundAndDefaultValueUsed() throws Exception {
        String logOutput = logWithSession(UserNameConverter.CONVERSION_CHARACTER, securedSession(null));
        assertThat(logOutput, is(DEFAULT_VALUE));
    }

    @Test
    public void doConvert_securityContextMissing_userNameNotFoundAndDefaultValueUsed() throws Exception {
        String logOutput = logWithSession(UserNameConverter.CONVERSION_CHARACTER, new DefaultMuleSession());
        assertThat(logOutput, is(DEFAULT_VALUE));
    }

    private MuleSession securedSession(Object principal) {
        MuleSession session = new DefaultMuleSession();
        session.setSecurityContext(new DefaultSecurityContext(
                new SpringAuthenticationAdapter(new UsernamePasswordAuthenticationToken(principal, null))));
        return session;
    }
}
