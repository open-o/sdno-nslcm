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
import org.openo.sdno.overlayvpn.model.v2.site.NbiSiteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * SBI service of Site.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-25
 */
@Service
public class SiteSbiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteSbiService.class);

    /**
     * Query Site.<br>
     * 
     * @param siteUuid Site Uuid
     * @return SiteModel queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public NbiSiteModel querySite(String siteUuid) throws ServiceException {
        if(StringUtils.isEmpty(siteUuid)) {
            LOGGER.error("siteUuid is invalid");
            throw new ParameterServiceException("siteUuid is invalid");
        }

        String querySiteUrl = MessageFormat.format(AdapterUrlConst.SITE_ADAPTER_URL + "/{0}", siteUuid);
        RestfulParametes restfulParameters = RestfulParametersUtil.getRestfulParameters();
        RestfulResponse response = RestfulProxy.get(querySiteUrl, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Query site failed");
            throw new ServiceException(response.getStatus(), response.getResponseContent());
        }

        return JsonUtil.fromJson(response.getResponseContent(), NbiSiteModel.class);
    }

    /**
     * Create Site.<br>
     * 
     * @param siteModel SiteModel need to create
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    public void createSite(NbiSiteModel siteModel) throws ServiceException {
        if(null == siteModel) {
            LOGGER.error("siteModel is invalid");
            throw new ParameterServiceException("siteModel is invalid");
        }

        RestfulParametes restfulParameters = RestfulParametersUtil.getRestfulParameters(JsonUtil.toJson(siteModel));
        RestfulResponse response = RestfulProxy.post(AdapterUrlConst.SITE_ADAPTER_URL, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Create Site failed");
            throw new ServiceException(response.getStatus(), response.getResponseContent());
        }
    }

    /**
     * Delete Site.<br>
     * 
     * @param siteUuid Site Uuid
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    public void deleteSite(String siteUuid) throws ServiceException {
        if(StringUtils.isEmpty(siteUuid)) {
            LOGGER.error("siteUuid is invalid");
            throw new ParameterServiceException("siteUuid is invalid");
        }

        String deleteSiteUrl = MessageFormat.format(AdapterUrlConst.SITE_ADAPTER_URL + "/{0}", siteUuid);
        RestfulParametes restfulParameters = RestfulParametersUtil.getRestfulParameters();
        RestfulResponse response = RestfulProxy.delete(deleteSiteUrl, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Delete Site failed");
            throw new ServiceException(response.getStatus(), response.getResponseContent());
        }
    }
}
