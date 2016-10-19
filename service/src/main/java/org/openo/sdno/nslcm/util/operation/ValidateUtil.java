/*
 * Copyright 2016 Huawei Technologies Co., Ltd.
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

package org.openo.sdno.nslcm.util.operation;

import org.openo.sdno.nslcm.util.exception.ApplicationException;
import org.openo.sdno.overlayvpn.consts.HttpCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Validate the object.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 Oct 18, 2016
 */
public class ValidateUtil {

    /**
     * Log server.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateUtil.class);

    /**
     * Constructor<br>
     * <p>
     * </p>
     * 
     * @since SDNO 0.5
     */
    private ValidateUtil() {

    }

    /**
     * Assert String parameter.<br>
     * 
     * @param param parameter data
     * @throws ApplicationException when parameter is null or empty.
     * @since SDNO 0.5
     */
    public static void assertStringNotNull(String param) throws ApplicationException {
        if(StringUtils.hasLength(param)) {
            return;
        }

        LOGGER.error("Parameter is null or empty.");
        throw new ApplicationException(HttpCode.BAD_REQUEST, "Invalid parameter.");
    }

    /**
     * Assert object is null.<br>
     * 
     * @param object data object
     * @throws ApplicationException when object is null.
     * @since SDNO 0.5
     */
    public static void assertObjectNotNull(Object object) throws ApplicationException {
        if(null == object) {
            LOGGER.error("Object is null.");
            throw new ApplicationException(HttpCode.BAD_REQUEST, "Object is null.");
        }

    }
}
