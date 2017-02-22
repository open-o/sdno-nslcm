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

package org.openo.sdno.nslcm.sbi.site;

import java.text.MessageFormat;

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
import org.openo.sdno.overlayvpn.model.v2.cpe.NbiCloudCpeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * SBI service of CloudCpe.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-25
 */
@Service
public class CloudCpeSbiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudCpeSbiService.class);

    /**
     * Query CloudCpe model.<br>
     * 
     * @param cloudCpeUuid CloudCpe Uuid
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public NbiCloudCpeModel queryCloudCpe(String cloudCpeUuid) throws ServiceException {
        if(StringUtils.isEmpty(cloudCpeUuid)) {
            LOGGER.error("cloudCpeUuid is invalid, need to check parameter");
            throw new ParameterServiceException("cloudCpeUuid is invalid");
        }

        String queryCpeUrl = MessageFormat.format(AdapterUrlConst.CLOUDCPE_ADAPTER_URL + "/{0}", cloudCpeUuid);
        RestfulParametes restfulParameters = RestfulParametersUtil.getRestfulParameters();
        RestfulResponse response = RestfulProxy.get(queryCpeUrl, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Query CloudCpe failed");
            throw new ServiceException(response.getStatus(), response.getResponseContent());
        }

        return JsonUtil.fromJson(response.getResponseContent(), NbiCloudCpeModel.class);
    }

    /**
     * Create CloudCpe.<br>
     * 
     * @param cloudCpeModel CloudCpeModel need to create
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    public void createCloudCpe(NbiCloudCpeModel cloudCpeModel) throws ServiceException {
        if(null == cloudCpeModel) {
            LOGGER.error("cloudCpeModel is invalid");
            throw new ParameterServiceException("cloudCpeModel is invalid");
        }

        RestfulParametes restfulParameters = RestfulParametersUtil.getRestfulParameters(JsonUtil.toJson(cloudCpeModel));
        RestfulResponse response = RestfulProxy.post(AdapterUrlConst.CLOUDCPE_ADAPTER_URL, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Create CloudCpe failed");
            throw new ServiceException(response.getStatus(), response.getResponseContent());
        }
    }

    /**
     * Delete CloudCpe.<br>
     * 
     * @param cloudCpeUuid CloudCpeModel Uuid
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    public void deleteCloudCpe(String cloudCpeUuid) throws ServiceException {
        if(StringUtils.isEmpty(cloudCpeUuid)) {
            LOGGER.error("cloudCpeUuid is invalid, need to check parameter");
            throw new ParameterServiceException("cloudCpeUuid is invalid");
        }

        String deleteCpeUrl = MessageFormat.format(AdapterUrlConst.CLOUDCPE_ADAPTER_URL + "/{0}", cloudCpeUuid);
        RestfulParametes restfulParameters = RestfulParametersUtil.getRestfulParameters();
        RestfulResponse response = RestfulProxy.delete(deleteCpeUrl, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Delete CloudCpe failed");
            throw new ServiceException(response.getStatus(), response.getResponseContent());
        }
    }

}
