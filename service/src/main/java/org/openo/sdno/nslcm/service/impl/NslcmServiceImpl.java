/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.sdno.nslcm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.model.servicemodel.vpn.VpnVo;
import org.openo.sdno.nslcm.model.db.NsCreationInfo;
import org.openo.sdno.nslcm.model.db.NsInstantiationInfo;
import org.openo.sdno.nslcm.model.db.NsResponseInfo;
import org.openo.sdno.nslcm.model.nbi.NsInstanceQueryResponse;
import org.openo.sdno.nslcm.model.nbi.SdnoTemplateParameter;
import org.openo.sdno.nslcm.sbi.inf.CatalogSbiService;
import org.openo.sdno.nslcm.sbi.inf.OverlaySbiService;
import org.openo.sdno.nslcm.sbi.inf.UnderlaySbiService;
import org.openo.sdno.nslcm.service.inf.NslcmService;
import org.openo.sdno.nslcm.util.Const;
import org.openo.sdno.nslcm.util.db.DbOper;
import org.openo.sdno.nslcm.util.exception.ThrowException;
import org.openo.sdno.overlayvpn.model.servicemodel.SiteToDcNbi;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.springframework.stereotype.Service;

/**
 * Nslcm service implementation. <br/>
 * 
 * @author
 * @version SDNO 0.5 Jun 16, 2016
 */
@Service
public class NslcmServiceImpl implements NslcmService {

    @Resource
    private DbOper dbOper;

    @Resource
    private CatalogSbiService catalogSbiService;

    @Resource
    private OverlaySbiService overlaySbiService;

    @Resource
    private UnderlaySbiService underlaySbiService;

    public void setDbOper(DbOper dbOper) {
        this.dbOper = dbOper;
    }

    public void setCatalogSbiService(CatalogSbiService catalogSbiService) {
        this.catalogSbiService = catalogSbiService;
    }

    public void setOverlaySbiService(OverlaySbiService overlaySbiService) {
        this.overlaySbiService = overlaySbiService;
    }

    public void setUnderlaySbiService(UnderlaySbiService underlaySbiService) {
        this.underlaySbiService = underlaySbiService;
    }

    @Override
    public Map<String, String> queryServiceTemplate(String nsdId) throws ServiceException {
        return catalogSbiService.queryServiceTemplate(nsdId);
    }

    @Override
    public Map<String, String> createOverlay(SiteToDcNbi siteToDcNbiMo, String instanceId) throws ServiceException {
        Map<String, String> response = overlaySbiService.createOverlay(siteToDcNbiMo);

        List<NsResponseInfo> nsResponseInfoList = new ArrayList<NsResponseInfo>();
        NsResponseInfo nsResponseInfo = new NsResponseInfo();
        nsResponseInfo.setInstanceId(instanceId);
        nsResponseInfo.setExternalId(response.get("vpnId"));
        nsResponseInfo.allocateUuid();
        nsResponseInfoList.add(nsResponseInfo);
        dbOper.insert(nsResponseInfoList);
        return response;
    }

    @Override
    public Map<String, String> deleteOverlay(String instanceId) throws ServiceException {
        ResultRsp<List<NsResponseInfo>> nsResponseInfoRsp = queryNsResponseInfo(instanceId);

        String nsResponseInfoUuid = nsResponseInfoRsp.getData().get(0).getUuid();
        String site2DcUuid = nsResponseInfoRsp.getData().get(0).getExternalId();
        Map<String, String> response = overlaySbiService.deleteOverlay(site2DcUuid);
        dbOper.delete(NsResponseInfo.class, nsResponseInfoUuid);
        return response;
    }

    @Override
    public Map<String, String> createUnderlay(VpnVo vpnVo, String instanceId) throws ServiceException {
        Map<String, String> response = underlaySbiService.createUnderlay(vpnVo);

        List<NsResponseInfo> nsResponseInfoList = new ArrayList<NsResponseInfo>();
        NsResponseInfo nsResponseInfo = new NsResponseInfo();
        nsResponseInfo.setInstanceId(instanceId);
        nsResponseInfo.setExternalId(response.get("vpnId"));
        nsResponseInfo.allocateUuid();
        nsResponseInfoList.add(nsResponseInfo);
        dbOper.insert(nsResponseInfoList);
        return response;
    }

    @Override
    public Map<String, String> deleteUnderlay(String instanceId, List<NsInstantiationInfo> nsInstantiationInfoList)
            throws ServiceException {
        ResultRsp<List<NsResponseInfo>> nsResponseInfoRsp = queryNsResponseInfo(instanceId);

        String nsResponseInfoUuid = nsResponseInfoRsp.getData().get(0).getUuid();
        String vpnUuid = nsResponseInfoRsp.getData().get(0).getExternalId();

        String serviceType = null;
        for(NsInstantiationInfo nsInstantiationInfo : nsInstantiationInfoList) {
            if("serviceType".equals(nsInstantiationInfo.getName())) {
                serviceType = nsInstantiationInfo.getValue();
                break;
            }
        }

        Map<String, String> response = underlaySbiService.deleteUnderlay(vpnUuid, serviceType);
        dbOper.delete(NsResponseInfo.class, nsResponseInfoUuid);
        return response;
    }

    @Override
    public NsInstanceQueryResponse queryVpn(String instanceId) throws ServiceException {
        if(dbOper.checkRecordIsExisted(NsCreationInfo.class, Const.UUID, instanceId)) {
            ThrowException.throwDataIsExisted(instanceId);
        }
        ResultRsp<List<NsCreationInfo>> NsCreationInfoRsp = dbOper.query(NsCreationInfo.class, Const.UUID, instanceId);

        if(dbOper.checkRecordIsExisted(NsInstantiationInfo.class, Const.INSTANCE_ID, instanceId)) {
            ThrowException.throwDataIsExisted(instanceId);
        }
        ResultRsp<List<NsInstantiationInfo>> NsInstantiationInfoRsp =
                dbOper.query(NsInstantiationInfo.class, Const.INSTANCE_ID, instanceId);

        NsCreationInfo nsCreationInfo = NsCreationInfoRsp.getData().get(0);
        List<NsInstantiationInfo> nsInstantiationInfoList = NsInstantiationInfoRsp.getData();

        NsInstanceQueryResponse nsInstanceQueryResponse = new NsInstanceQueryResponse();
        nsInstanceQueryResponse.setId(nsCreationInfo.getUuid());
        nsInstanceQueryResponse.setName(nsCreationInfo.getNsName());
        nsInstanceQueryResponse.setNsdId(nsCreationInfo.getNsdId());
        nsInstanceQueryResponse.setDescription(nsCreationInfo.getDescription());

        return setSdnoTemplateParameterList(nsInstanceQueryResponse, nsInstantiationInfoList);
    }

    private NsInstanceQueryResponse setSdnoTemplateParameterList(NsInstanceQueryResponse nsInstanceQueryResponse,
            List<NsInstantiationInfo> nsInstantiationInfoList) {
        List<SdnoTemplateParameter> sdnoTemplateParameterList = new ArrayList<SdnoTemplateParameter>();
        for(NsInstantiationInfo nsInstantiationInfo : nsInstantiationInfoList) {
            SdnoTemplateParameter sdnoTemplateParameterMo = new SdnoTemplateParameter();
            sdnoTemplateParameterMo.setName(nsInstantiationInfo.getName());
            sdnoTemplateParameterMo.setValue(nsInstantiationInfo.getValue());
            sdnoTemplateParameterList.add(sdnoTemplateParameterMo);
        }
        nsInstanceQueryResponse.setAdditionalParams(sdnoTemplateParameterList);
        return nsInstanceQueryResponse;
    }

    private ResultRsp<List<NsResponseInfo>> queryNsResponseInfo(String instanceId) throws ServiceException {
        if(dbOper.checkRecordIsExisted(NsResponseInfo.class, Const.INSTANCE_ID, instanceId)) {
            ThrowException.throwDataIsExisted(instanceId);
        }
        ResultRsp<List<NsResponseInfo>> nsResponseInfoRsp =
                dbOper.query(NsResponseInfo.class, Const.INSTANCE_ID, instanceId);
        return nsResponseInfoRsp;
    }
}
