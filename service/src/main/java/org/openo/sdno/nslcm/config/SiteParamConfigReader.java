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

import org.springframework.stereotype.Component;

/**
 * Class of Site Parameter Reader.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-2-6
 */
@Component
public class SiteParamConfigReader extends ConfigReader {

    private static final String SITEPARAM_CONFIG_FILE = "etc/siteparam.properties";

    /**
     * Constructor.<br>
     * 
     * @since SDNO 0.5
     */
    public SiteParamConfigReader() {
        super(SITEPARAM_CONFIG_FILE);
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
     * Get site isEncrypt.<br>
     * 
     * @return isEncrypt parameter
     * @since SDNO 0.5
     */
    public String getSiteIsEncrypt() {
        return properties.getProperty("isEncrypt");
    }

    /**
     * Get cpe online waiting time.<br>
     * 
     * @return cpe online waiting time
     * @since SDNO 0.5
     */
    public int getCpeOneTime() {
        return Integer.parseInt(properties.getProperty("cpeOneTime"));
    }

}
