/*
 * Copyright 2016-2017 Huawei Technologies Co., Ltd.
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

import org.apache.commons.lang.StringUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.exception.ParameterServiceException;
import org.openo.sdno.framework.container.resthelper.RestfulProxy;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.nslcm.util.AdapterUrlConst;
import org.openo.sdno.nslcm.util.RestfulParametersUtil;
import org.openo.sdno.overlayvpn.consts.HttpCode;
import org.openo.sdno.overlayvpn.model.v2.overlay.NbiVpn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * SBI service of OverlayVpn.<br>
 * 
 * @author
 * @version SDNO 0.5 June 22, 2016
 */
@Service
public class VpnSbiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VpnSbiService.class);

    /**
     * Query OverlayVpn.<br>
     * 
     * @param vpnUuid vpn uuid
     * @return OverlayVpn queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public NbiVpn queryVpn(String vpnUuid) throws ServiceException {

        if(StringUtils.isEmpty(vpnUuid)) {
            LOGGER.error("vpnUuid is invalid, need to check parameter");
            throw new ParameterServiceException("vpnUuid is invalid");
        }

        String queryUrl = MessageFormat.format(AdapterUrlConst.VPN_ADAPTER_URL + "/{0}", vpnUuid);

        RestfulResponse response = RestfulProxy.get(queryUrl, RestfulParametersUtil.getRestfulParameters());
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Query vpn failed");
            throw new ServiceException(response.getStatus(), response.getResponseContent());
        }

        return JsonUtil.fromJson(response.getResponseContent(), NbiVpn.class);
    }

    /**
     * Create OverlayVpn.<br>
     * 
     * @param vpn NbiVpn Object
     * @return NbiVpn created
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    public NbiVpn createVpn(NbiVpn vpn) throws ServiceException {
        if(null == vpn) {
            LOGGER.error("vpn is invalid, need to check parameter");
            throw new ParameterServiceException("vpn is invalid");
        }

        RestfulResponse response = RestfulProxy.post(AdapterUrlConst.VPN_ADAPTER_URL,
                RestfulParametersUtil.getRestfulParameters(JsonUtil.toJson(vpn)));
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Create vpn failed");
            throw new ServiceException(response.getStatus(), response.getResponseContent());
        }

        return JsonUtil.fromJson(response.getResponseContent(), NbiVpn.class);
    }

    /**
     * Delete OverlayVpn.<br>
     * 
     * @param vpnUuid Uuid of Vpn need to delete
     * @return NbiVpn deleted
     * @throws ServiceException When delete failed
     * @since SDNO 0.5
     */
    public void deleteVpn(String vpnUuid) throws ServiceException {

        if(StringUtils.isEmpty(vpnUuid)) {
            LOGGER.error("vpnUuid is invalid");
            throw new ParameterServiceException("vpnUuid is invalid");
        }

        String deleteUrl = MessageFormat.format(AdapterUrlConst.VPN_ADAPTER_URL + "/{0}", vpnUuid);

        RestfulResponse response = RestfulProxy.delete(deleteUrl, RestfulParametersUtil.getRestfulParameters());
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Delete vpn failed");
            throw new ServiceException(response.getStatus(), response.getResponseContent());
        }
    }

}
