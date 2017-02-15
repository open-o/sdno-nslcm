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

package org.openo.sdno.nslcm.sbi.overlayvpn;

import java.text.MessageFormat;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.exception.ParameterServiceException;
import org.openo.sdno.framework.container.resthelper.RestfulProxy;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.nslcm.util.AdapterUrlConst;
import org.openo.sdno.nslcm.util.RestfulParametersUtil;
import org.openo.sdno.overlayvpn.consts.HttpCode;
import org.openo.sdno.overlayvpn.model.v2.overlay.NbiVpnConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * SBI service of Vpn Connection.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-25
 */
@Service
public class VpnConnectionSbiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VpnConnectionSbiService.class);

    /**
     * Create VpnConnection.<br>
     * 
     * @param vpn NbiVpnConnection Object
     * @return NbiVpnConnection created
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    public NbiVpnConnection createVpnConnection(NbiVpnConnection vpnConnection) throws ServiceException {
        if(null == vpnConnection) {
            LOGGER.error("vpnConnection is invalid");
            throw new ParameterServiceException("vpnConnection is invalid");
        }

        RestfulResponse response = RestfulProxy.post(AdapterUrlConst.VPN_CONNECTION_ADAPTER_URL,
                RestfulParametersUtil.getRestfulParameters(JsonUtil.toJson(vpnConnection)));
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Create vpnConnection failed");
            throw new ServiceException(response.getStatus(), response.getResponseContent());
        }

        return JsonUtil.fromJson(response.getResponseContent(), NbiVpnConnection.class);
    }

    /**
     * Delete VpnConnection.<br>
     * 
     * @param vpnUuid Uuid of VpnConnection need to delete
     * @return NbiVpnConnection deleted
     * @throws ServiceException When delete failed
     * @since SDNO 0.5
     */
    public void deleteVpnConnection(String vpnConnectionUuid) throws ServiceException {
        String deleteUrl = MessageFormat.format(AdapterUrlConst.VPN_CONNECTION_ADAPTER_URL + "/{0}", vpnConnectionUuid);

        RestfulResponse response = RestfulProxy.delete(deleteUrl, RestfulParametersUtil.getRestfulParameters());
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Delete vpn connection failed");
            throw new ServiceException(response.getStatus(), response.getResponseContent());
        }
    }

}
