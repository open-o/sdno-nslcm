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
import org.openo.sdno.overlayvpn.model.v2.vlan.NbiVlanModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * SBI service of Vlan.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-25
 */
@Service
public class VlanSbiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VlanSbiService.class);

    /**
     * Create Vlan.<br>
     * 
     * @param vlanModel VlanModel need to create
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    public void createVlan(NbiVlanModel vlanModel) throws ServiceException {
        if(null == vlanModel) {
            LOGGER.error("vlanModel is invalid");
            throw new ParameterServiceException("vlanModel is invalid");
        }

        RestfulParametes restfulParameters = RestfulParametersUtil.getRestfulParameters(JsonUtil.toJson(vlanModel));
        RestfulResponse response = RestfulProxy.post(AdapterUrlConst.VLAN_ADAPTER_URL, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Create Vlan failed");
            throw new ServiceException(response.getStatus(), response.getResponseContent());
        }
    }

    /**
     * Delete Vlan.<br>
     * 
     * @param vlanUuid Site Uuid
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    public void deleteVlan(String vlanUuid) throws ServiceException {
        if(StringUtils.isEmpty(vlanUuid)) {
            LOGGER.error("vlanUuid is invalid");
            throw new ParameterServiceException("vlanUuid is invalid");
        }

        String deleteVlanUrl = MessageFormat.format(AdapterUrlConst.VLAN_ADAPTER_URL + "/{0}", vlanUuid);
        RestfulParametes restfulParameters = RestfulParametersUtil.getRestfulParameters();
        RestfulResponse response = RestfulProxy.delete(deleteVlanUrl, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Delete Vlan failed");
            throw new ServiceException(response.getStatus(), response.getResponseContent());
        }
    }

}
