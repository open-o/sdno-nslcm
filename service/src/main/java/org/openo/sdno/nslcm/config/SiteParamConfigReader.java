/*
 * Copyright 2017 Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.sdno.nslcm.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Class of Site Parameter Reader.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-2-6
 */
@Component
public class SiteParamConfigReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteParamConfigReader.class);

    private static final String SITEPARAM_CONFIG_FILE = "etc/siteparam.properties";

    private Properties properties = new Properties();

    /**
     * Constructor.<br>
     * 
     * @since SDNO 0.5
     */
    public SiteParamConfigReader() {
        loadProperties();
    }

    /**
     * Get site reliability.<br>
     * 
     * @return site reliability parameter
     * @since SDNO 0.5
     */
    public String getSiteReliability() {
        return properties.getProperty("reliability");
    }

    /**
     * Get site descriptor.<br>
     * 
     * @return site descriptor parameter
     * @since SDNO 0.5
     */
    public String getSiteDescriptor() {
        return properties.getProperty("siteDescriptor");
    }

    /**
     * Get site isEncrypt.<br>
     * 
     * @return isEncrypt parameter
     * @since SDNO 0.5
     */
    public String getSiteIsEncrypt() {
        return properties.getProperty("isEncrypt");
    }

    private void loadProperties() {
        try (FileInputStream finStream = new FileInputStream(SITEPARAM_CONFIG_FILE)) {
            properties.load(finStream);
        } catch(IOException e) {
            LOGGER.error("Read site parameter configuration file failed!", e);
        }
    }

}
