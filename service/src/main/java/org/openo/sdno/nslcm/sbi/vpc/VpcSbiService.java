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

package org.openo.sdno.nslcm.sbi.vpc;

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
import org.openo.sdno.overlayvpn.model.servicemodel.Vpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * SBI service of Vpc.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-25
 */
@Service
public class VpcSbiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VpcSbiService.class);

    /**
     * Query Vpc.<br>
     * 
     * @param vpcUuid Vpc Uuid
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public Vpc queryVpc(String vpcUuid) throws ServiceException {
        if(StringUtils.isEmpty(vpcUuid)) {
            LOGGER.error("vpcUuid is invalid");
            throw new ParameterServiceException("vpcUuid is invalid");
        }

        String queryVpcUrl = MessageFormat.format(AdapterUrlConst.VPC_ADAPTER_URL + "/{0}", vpcUuid);
        RestfulParametes restfulParameters = RestfulParametersUtil.getRestfulParameters();
        RestfulResponse response = RestfulProxy.get(queryVpcUrl, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Query vpc failed");
            throw new ServiceException(response.getStatus(), response.getResponseContent());
        }

        return JsonUtil.fromJson(response.getResponseContent(), Vpc.class);
    }

    /**
     * Create Vpc.<br>
     * 
     * @param vpcModel Vpc need to create
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    public void createVpc(Vpc vpcModel) throws ServiceException {
        if(null == vpcModel) {
            LOGGER.error("vpcModel is invalid");
            throw new ParameterServiceException("vpcModel is invalid");
        }

        RestfulParametes restfulParameters = RestfulParametersUtil.getRestfulParameters(JsonUtil.toJson(vpcModel));
        RestfulResponse response = RestfulProxy.post(AdapterUrlConst.VPC_ADAPTER_URL, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Create vpc failed");
            throw new ServiceException(response.getStatus(), response.getResponseContent());
        }
    }

    /**
     * Delete Vpc.<br>
     * 
     * @param vpcUuid Vpc Uuid
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    public void deleteVpc(String vpcUuid) throws ServiceException {
        if(StringUtils.isEmpty(vpcUuid)) {
            LOGGER.error("vpcUuid is invalid");
            throw new ParameterServiceException("vpcUuid is invalid");
        }

        String deleteVpcUrl = MessageFormat.format(AdapterUrlConst.VPC_ADAPTER_URL + "/{0}", vpcUuid);
        RestfulParametes restfulParameters = RestfulParametersUtil.getRestfulParameters();
        RestfulResponse response = RestfulProxy.delete(deleteVpcUrl, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Delete vpc failed");
            throw new ServiceException(response.getStatus(), response.getResponseContent());
        }
    }

}
