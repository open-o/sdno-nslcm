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
import org.openo.sdno.overlayvpn.model.v2.subnet.NbiSubnetModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * SBI service of Subnet.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-25
 */
@Service
public class SubnetSbiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubnetSbiService.class);

    /**
     * Create Subnet.<br>
     * 
     * @param subnetModel SubnetModel need to create
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    public void createSubnet(NbiSubnetModel subnetModel) throws ServiceException {
        if(null == subnetModel) {
            LOGGER.error("subnetModel is invalid");
            throw new ParameterServiceException("subnetModel is invalid");
        }

        RestfulParametes restfulParameters = RestfulParametersUtil.getRestfulParameters(JsonUtil.toJson(subnetModel));
        RestfulResponse response = RestfulProxy.post(AdapterUrlConst.SUBNET_ADAPTER_URL, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Create Subnet failed");
            throw new ServiceException(response.getStatus(), response.getResponseContent());
        }
    }

    /**
     * Delete Subnet.<br>
     * 
     * @param subnetUuid Subnet Uuid
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    public void deleteSubnet(String subnetUuid) throws ServiceException {
        if(StringUtils.isEmpty(subnetUuid)) {
            LOGGER.error("subnetUuid is invalid");
            throw new ParameterServiceException("subnetUuid is invalid");
        }

        String deleteSubnetUrl = MessageFormat.format(AdapterUrlConst.SUBNET_ADAPTER_URL + "/{0}", subnetUuid);
        RestfulParametes restfulParameters = RestfulParametersUtil.getRestfulParameters();
        RestfulResponse response = RestfulProxy.delete(deleteSubnetUrl, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Delete Subnet failed");
            throw new ServiceException(response.getStatus(), response.getResponseContent());
        }
    }

}
