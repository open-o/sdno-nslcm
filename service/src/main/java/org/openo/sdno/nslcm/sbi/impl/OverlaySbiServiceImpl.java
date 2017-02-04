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
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulOptions;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.framework.container.resthelper.RestfulProxy;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.nslcm.sbi.inf.OverlaySbiService;
import org.openo.sdno.nslcm.util.RestfulParametersUtil;
import org.openo.sdno.overlayvpn.consts.UrlAdapterConst;
import org.openo.sdno.overlayvpn.model.servicemodel.SiteToDcNbi;
import org.openo.sdno.rest.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DC gateway controller south branch interface implementation. <br>
 * 
 * @author
 * @version SDNO 0.5 June 22, 2016
 */
public class OverlaySbiServiceImpl implements OverlaySbiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OverlaySbiServiceImpl.class);

    @Override
    public Map<String, String> deleteOverlay(String site2DcUuid) throws ServiceException {

        String url =
                UrlAdapterConst.OVERLAY_BASE_URL + MessageFormat.format(UrlAdapterConst.DELETE_OVERLAY, site2DcUuid);

        LOGGER.info("deleteOverlay begin: " + url);
        RestfulOptions restOptions = new RestfulOptions();
        restOptions.setRestTimeout(500000);

        RestfulResponse response = RestfulProxy.delete(url, RestfulParametersUtil.getRestfulParameters(), restOptions);

        String rspContent = ResponseUtils.transferResponse(response);
        Map<String, String> restResult = JsonUtil.fromJson(rspContent, new TypeReference<Map<String, String>>() {});

        LOGGER.info("deleteOverlay end, result = " + restResult.toString());

        return restResult;
    }

    @Override
    public Map<String, String> createOverlay(SiteToDcNbi siteToDcNbiMo) throws ServiceException {
        String url = UrlAdapterConst.OVERLAY_BASE_URL + UrlAdapterConst.CREATE_OVERLAY;

        LOGGER.info("createOverlay begin: " + url + ", body: " + JsonUtil.toJson(siteToDcNbiMo));
        RestfulOptions restOptions = new RestfulOptions();
        restOptions.setRestTimeout(500000);

        RestfulResponse response = RestfulProxy.post(url,
                RestfulParametersUtil.getRestfulParameters(JsonUtil.toJson(siteToDcNbiMo)), restOptions);
        String rspContent = ResponseUtils.transferResponse(response);
        Map<String, String> restResult = JsonUtil.fromJson(rspContent, new TypeReference<Map<String, String>>() {});

        LOGGER.info("createOverlay end, result = " + restResult.toString());

        return restResult;
    }

}
