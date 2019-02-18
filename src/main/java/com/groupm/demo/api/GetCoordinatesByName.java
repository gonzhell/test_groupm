package com.groupm.demo.api;

import com.qaprosoft.carina.core.foundation.api.AbstractApiMethodV2;
import com.qaprosoft.carina.core.foundation.utils.Configuration;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Properties;

public class GetCoordinatesByName extends AbstractApiMethodV2 {
    public GetCoordinatesByName(String placeName) throws UnsupportedEncodingException {
        super(null, "api/users/_get/rs.json", new Properties());
        replaceUrlPlaceholder("base_url", Configuration.getEnvArg("api_url"));
        replaceUrlPlaceholder("place_name", URLEncoder.encode(placeName, "UTF-8") );
        replaceUrlPlaceholder("api_key", Configuration.getEnvArg("api_key"));
    }
}
