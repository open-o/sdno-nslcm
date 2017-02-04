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

package org.openo.sdno.nslcm.sbi.servicechain;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.exception.ParameterServiceException;
import org.openo.sdno.framework.container.resthelper.RestfulProxy;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.nslcm.util.AdapterUrlConst;
import org.openo.sdno.nslcm.util.RestfulParametersUtil;
import org.openo.sdno.overlayvpn.consts.HttpCode;
import org.openo.sdno.overlayvpn.model.servicechain.ServiceChainPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * SBI service of ServiceChain.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-25
 */
@Service
public class ServiceChainSbiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceChainSbiService.class);

    private static final String SERVICE_CHAIN_PATH_KEY = "serviceChainPath";

    /**
     * Create ServiceChain.<br>
     * 
     * @param serviceChainPath ServiceChainPath need to create
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    public void createServiceChain(ServiceChainPath serviceChainPath) throws ServiceException {
        if(null == serviceChainPath) {
            LOGGER.error("serviceChainPath is invalid");
            throw new ParameterServiceException("serviceChainPath is invalid");
        }

        Map<String, ServiceChainPath> serviceChainPathMap = new HashMap<String, ServiceChainPath>();
        serviceChainPathMap.put(SERVICE_CHAIN_PATH_KEY, serviceChainPath);

        RestfulParametes restfulParameters =
                RestfulParametersUtil.getRestfulParameters(JsonUtil.toJson(serviceChainPathMap));
        RestfulResponse response = RestfulProxy.post(AdapterUrlConst.SERVICECHAIN_ADAPTER_URL, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Create ServiceChainPath failed");
            throw new ServiceException(response.getStatus(), response.getResponseContent());
        }
    }

    /**
     * Delete ServiceChain.<br>
     * 
     * @param serviceChainUuid ServiceChainPath Uuid
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    public void deleteServiceChain(String serviceChainUuid) throws ServiceException {
        if(StringUtils.isEmpty(serviceChainUuid)) {
            LOGGER.error("serviceChainUuid is invalid");
            throw new ParameterServiceException("serviceChainUuid is invalid");
        }

        String deleteServiceChainUrl =
                MessageFormat.format(AdapterUrlConst.SERVICECHAIN_ADAPTER_URL + "/{0}", serviceChainUuid);
        RestfulParametes restfulParameters = RestfulParametersUtil.getRestfulParameters();
        RestfulResponse response = RestfulProxy.delete(deleteServiceChainUrl, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Delete ServiceChain failed");
            throw new ServiceException(response.getStatus(), response.getResponseContent());
        }
    }

}
