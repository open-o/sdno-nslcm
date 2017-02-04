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
import org.openo.sdno.overlayvpn.model.servicemodel.SubNet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * SBI service of Vpc Subnet.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-25
 */
@Service
public class VpcSubnetSbiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VpcSubnetSbiService.class);

    /**
     * Create Vpc Subnet.<br>
     * 
     * @param subNetModel Subnet need to create
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    public void createVpcSubnet(SubNet subNetModel) throws ServiceException {
        if(null == subNetModel) {
            LOGGER.error("subNetModel is invalid");
            throw new ParameterServiceException("subNetModel is invalid");
        }

        RestfulParametes restfulParameters = RestfulParametersUtil.getRestfulParameters(JsonUtil.toJson(subNetModel));
        RestfulResponse response = RestfulProxy.post(AdapterUrlConst.VPCSUBNET_ADAPTER_URL, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Create SubNet failed");
            throw new ServiceException(response.getStatus(), response.getResponseContent());
        }
    }

    /**
     * Delete Vpc Subnet.<br>
     * 
     * @param vpcSubnetUuid Subnet Uuid
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    public void deleteVpcSubnet(String vpcSubnetUuid) throws ServiceException {
        if(StringUtils.isEmpty(vpcSubnetUuid)) {
            LOGGER.error("vpcSubnetUuid is invalid");
            throw new ParameterServiceException("vpcSubnetUuid is invalid");
        }

        String deleteServiceChainUrl =
                MessageFormat.format(AdapterUrlConst.VPCSUBNET_ADAPTER_URL + "/{0}", vpcSubnetUuid);
        RestfulParametes restfulParameters = RestfulParametersUtil.getRestfulParameters();
        RestfulResponse response = RestfulProxy.delete(deleteServiceChainUrl, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Delete Vpc Subnet failed");
            throw new ServiceException(response.getStatus(), response.getResponseContent());
        }
    }

}
