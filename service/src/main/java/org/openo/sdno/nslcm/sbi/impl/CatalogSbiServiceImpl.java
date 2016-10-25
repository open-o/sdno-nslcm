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
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.framework.container.resthelper.RestfulProxy;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.nslcm.sbi.inf.CatalogSbiService;
import org.openo.sdno.nslcm.util.operation.RestfulParametesUtil;
import org.openo.sdno.overlayvpn.consts.UrlAdapterConst;
import org.openo.sdno.rest.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DC gateway controller south branch interface implementation. <br>
 * 
 * @author
 * @version SDNO 0.5 Jun 22, 2016
 */
public class CatalogSbiServiceImpl implements CatalogSbiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogSbiServiceImpl.class);

    @Override
    public Map<String, Object> queryServiceTemplate(String nsdId) throws ServiceException {
        String url = UrlAdapterConst.CATALOG_ADAPTER_BASE_URL
                + MessageFormat.format(UrlAdapterConst.QUERY_TEMPLATE_INFORMATION, nsdId);

        LOGGER.info("queryServiceTemplate begin: " + url);

        RestfulResponse response = RestfulProxy.get(url, RestfulParametesUtil.getRestfulParametes());
        String rspContent = ResponseUtils.transferResponse(response);
        Map<String, Object> restResult = JsonUtil.fromJson(rspContent, new TypeReference<Map<String, Object>>() {});

        LOGGER.info("queryServiceTemplate end, result = " + restResult.toString());

        return restResult;
    }
}
