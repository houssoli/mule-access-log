package com.greenbird.mule.http.log.converter;

import org.apache.log4j.helpers.FormattingInfo;
import org.mule.api.MuleMessage;
import org.mule.api.config.MuleProperties;

public class RemoteHostConverter extends AbstractAccessLogConverter {
    public final static char CONVERSION_CHARACTER = 'h'; 
    
    public RemoteHostConverter(FormattingInfo formattingInfo, String option) {
        super(formattingInfo, option);
    }

    @Override
    protected String doConvert(MuleMessage message) {
        String address = message.getInboundProperty(MuleProperties.MULE_REMOTE_CLIENT_ADDRESS);
        return address != null ? address.replace("/", "").replaceAll(":.*", "") : null;
    }
}
