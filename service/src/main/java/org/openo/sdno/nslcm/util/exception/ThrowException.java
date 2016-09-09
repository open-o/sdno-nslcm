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

package org.openo.sdno.nslcm.util.exception;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.openo.sdno.overlayvpn.result.SvcExcptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Throw exception implementation. <br>
 * 
 * @author
 * @version SDNO 0.5 Jun 20, 2016
 */
public class ThrowException {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThrowException.class);

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     */
    private ThrowException() {
    }

    /**
     * It is used to throw exception when the data is existed. <br>
     * 
     * @param id The id
     * @throws ServiceException Throw 400 error
     * @since SDNO 0.5
     */
    public static void throwDataIsExisted(String id) throws ServiceException {
        LOGGER.error(String.format("id (%s) is existed", id));
        String message = "id (" + id + ") is existed";
        String advice = "id is existed, please modify data and try again";
        SvcExcptUtil.throwBadReqSvcExptionWithInfo(ErrorCode.OVERLAYVPN_PARAMETER_INVALID, message, message, message,
                advice);
    }

    /**
     * It is used to throw exception when parameter is invalid. <br>
     * 
     * @param description The description
     * @throws ServiceException Throw 400 error
     * @since SDNO 0.5
     */
    public static void throwParameterInvalid(String description) throws ServiceException {
        LOGGER.error(description);

        String message = description;
        String advice = description + ", please modify data and try again";
        SvcExcptUtil.throwBadReqSvcExptionWithInfo(ErrorCode.OVERLAYVPN_PARAMETER_INVALID, message, message, message,
                advice);
    }
}
