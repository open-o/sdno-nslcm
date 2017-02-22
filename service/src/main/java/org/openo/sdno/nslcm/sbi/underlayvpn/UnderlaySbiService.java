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

package org.openo.sdno.nslcm.sbi.underlayvpn;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulOptions;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.framework.container.resthelper.RestfulProxy;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.model.servicemodel.vpn.Vpn;
import org.openo.sdno.model.servicemodel.vpn.VpnVo;
import org.openo.sdno.nslcm.util.AdapterUrlConst;
import org.openo.sdno.nslcm.util.RestfulParametersUtil;
import org.openo.sdno.nslcm.util.exception.ThrowException;
import org.openo.sdno.rest.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * SBI service of UnderlayVpn.<br>
 * 
 * @author
 * @version SDNO 0.5 June 22, 2016
 */
@Service
public class UnderlaySbiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnderlaySbiService.class);

    /**
     * Create UnderlayVpn.<br>
     * 
     * @param vpnVo The vpnVo object
     * @return The create result
     * @throws ServiceException When create failed
     * @since SDNO 0.5
     */
    public Map<String, String> createUnderlay(VpnVo vpnVo) throws ServiceException {
        String url = null;
        if("l3vpn".equals(vpnVo.getVpn().getVpnBasicInfo().getServiceType())) {
            url = AdapterUrlConst.L3VPN_ADAPTER_URL;
        } else if("l2vpn".equals(vpnVo.getVpn().getVpnBasicInfo().getServiceType())) {
            url = AdapterUrlConst.L2VPN_ADAPTER_URL;
        } else {
            ThrowException.throwParameterInvalid("ServiceType is invalid");
        }

        LOGGER.info("createUnderlay begin: " + url + ", body: " + JsonUtil.toJson(vpnVo));
        RestfulResponse response =
                RestfulProxy.post(url, RestfulParametersUtil.getRestfulParameters(JsonUtil.toJson(vpnVo)));
        String rspContent = ResponseUtils.transferResponse(response);
        Vpn restVpn = JsonUtil.fromJson(rspContent, Vpn.class);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("vpnId", restVpn.getId());
        LOGGER.info("createUnderlay end, result = " + resultMap.toString());

        return resultMap;
    }

    /**
     * Delete UnderlayVpn.<br>
     * 
     * @param vpnUuid The Vpn uuid
     * @param serviceType The service type
     * @return The delete result
     * @throws ServiceException When delete failed
     * @since SDNO 0.5
     */
    public Map<String, String> deleteUnderlay(String vpnUuid, String serviceType) throws ServiceException {
        String url = null;
        if("l3vpn".equals(serviceType)) {
            url = MessageFormat.format(AdapterUrlConst.L3VPN_ADAPTER_URL + "/{0}", vpnUuid);
        } else if("l2vpn".equals(serviceType)) {
            url = MessageFormat.format(AdapterUrlConst.L2VPN_ADAPTER_URL + "/{0}", vpnUuid);
        } else {
            ThrowException.throwParameterInvalid("ServiceType is invalid");
        }

        LOGGER.info("deleteUnderlay begin: " + url);
        RestfulOptions restOptions = new RestfulOptions();
        restOptions.setRestTimeout(500000);

        RestfulResponse response = RestfulProxy.delete(url, RestfulParametersUtil.getRestfulParameters(), restOptions);

        String rspContent = ResponseUtils.transferResponse(response);
        Vpn restVpn = JsonUtil.fromJson(rspContent, Vpn.class);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("errorCode", restVpn.getId());
        LOGGER.info("deleteUnderlay end, result = " + resultMap.toString());

        return resultMap;
    }

}
