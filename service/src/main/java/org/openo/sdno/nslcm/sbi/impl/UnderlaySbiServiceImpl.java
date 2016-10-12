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

package org.openo.sdno.nslcm.sbi.impl;

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
import org.openo.sdno.nslcm.sbi.inf.UnderlaySbiService;
import org.openo.sdno.nslcm.util.exception.ThrowException;
import org.openo.sdno.nslcm.util.operation.RestfulParametesUtil;
import org.openo.sdno.overlayvpn.consts.UrlAdapterConst;
import org.openo.sdno.rest.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DC gateway controller south branch interface implementation. <br>
 * 
 * @author
 * @version SDNO 0.5 June 22, 2016
 */
public class UnderlaySbiServiceImpl implements UnderlaySbiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnderlaySbiServiceImpl.class);

    @Override
    public Map<String, String> deleteUnderlay(String vpnUuid, String serviceType) throws ServiceException {
        String url = null;
        if("l3vpn".equals(serviceType)) {
            url = UrlAdapterConst.L3VPN_BASE_URL + MessageFormat.format(UrlAdapterConst.DELETE_L3VPN, vpnUuid);
        } else if("l2vpn".equals(serviceType)) {
            url = UrlAdapterConst.L2VPN_BASE_URL + MessageFormat.format(UrlAdapterConst.DELETE_L2VPN, vpnUuid);
        } else {
            ThrowException.throwParameterInvalid("ServiceType is invalid");
        }

        LOGGER.info("deleteUnderlay begin: " + url);
        RestfulOptions restOptions = new RestfulOptions();
        restOptions.setRestTimeout(500000);

        RestfulResponse response = RestfulProxy.delete(url, RestfulParametesUtil.getRestfulParametes(), restOptions);

        String rspContent = ResponseUtils.transferResponse(response);
        Vpn restVpn = JsonUtil.fromJson(rspContent, Vpn.class);
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("errorCode", restVpn.getId());
        LOGGER.info("deleteUnderlay end, result = " + resultMap.toString());

        return resultMap;
    }

    @Override
    public Map<String, String> createUnderlay(VpnVo vpnVo) throws ServiceException {
        String url = null;
        if("l3vpn".equals(vpnVo.getVpn().getVpnBasicInfo().getServiceType())) {
            url = UrlAdapterConst.L3VPN_BASE_URL + UrlAdapterConst.CREATE_L3VPN;
        } else if("l2vpn".equals(vpnVo.getVpn().getVpnBasicInfo().getServiceType())) {
            url = UrlAdapterConst.L2VPN_BASE_URL + UrlAdapterConst.CREATE_L2VPN;
        } else {
            ThrowException.throwParameterInvalid("ServiceType is invalid");
        }

        LOGGER.info("createUnderlay begin: " + url);
        RestfulOptions restOptions = new RestfulOptions();
        restOptions.setRestTimeout(500000);
        RestfulResponse response = RestfulProxy.post(url,
                RestfulParametesUtil.getRestfulParametesWithBody(JsonUtil.toJson(vpnVo)), restOptions);
        String rspContent = ResponseUtils.transferResponse(response);
        Vpn restVpn = JsonUtil.fromJson(rspContent, Vpn.class);
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("vpnId", restVpn.getId());
        LOGGER.info("createUnderlay end, result = " + resultMap.toString());

        return resultMap;
    }

}
