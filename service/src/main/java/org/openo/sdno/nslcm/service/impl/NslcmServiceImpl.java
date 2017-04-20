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

package org.openo.sdno.nslcm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.model.servicemodel.vpn.VpnVo;
import org.openo.sdno.nslcm.dao.inf.IServiceModelDao;
import org.openo.sdno.nslcm.dao.inf.IServicePackageDao;
import org.openo.sdno.nslcm.dao.inf.IServiceParameterDao;
import org.openo.sdno.nslcm.model.BusinessModel;
import org.openo.sdno.nslcm.model.db.NsResponseInfo;
import org.openo.sdno.nslcm.model.nbi.NsInstanceQueryResponse;
import org.openo.sdno.nslcm.model.servicemo.InvServiceModel;
import org.openo.sdno.nslcm.model.servicemo.ServicePackageModel;
import org.openo.sdno.nslcm.model.servicemo.ServiceParameter;
import org.openo.sdno.nslcm.sbi.catalog.CatalogSbiService;
import org.openo.sdno.nslcm.sbi.underlayvpn.UnderlaySbiService;
import org.openo.sdno.nslcm.service.inf.NslcmService;
import org.openo.sdno.nslcm.util.Const;
import org.openo.sdno.nslcm.util.RecordProgress;
import org.openo.sdno.nslcm.util.db.DbOper;
import org.openo.sdno.nslcm.util.exception.ThrowException;
import org.openo.sdno.nslcm.vpnbusinessexecutor.VpnBusinessExecutor;
import org.openo.sdno.overlayvpn.model.v2.overlay.NbiVpn;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * NSLCM service implementation. <br>
 * 
 * @author
 * @version SDNO 0.5 June 16, 2016
 */
@Service
public class NslcmServiceImpl implements NslcmService {

    @Autowired
    private DbOper<NsResponseInfo> dbOper;

    @Autowired
    private IServiceModelDao iServiceModelDao;

    @Autowired
    private IServicePackageDao iServicePackageDao;

    @Autowired
    private IServiceParameterDao iServiceParameterDao;

    @Autowired
    private CatalogSbiService catalogSbiService;

    @Autowired
    private HashMap<String, VpnBusinessExecutor> vpnBusinessExceutorMap;

    @Autowired
    private UnderlaySbiService underlaySbiService;

    @Override
    public Map<String, Object> queryServiceTemplate(String nsdId) throws ServiceException {
        return catalogSbiService.queryServiceTemplate(nsdId);
    }

    @Override
    public Map<String, String> createOverlayVpn(BusinessModel businessModel, String instanceId, String templateName)
            throws ServiceException {
        NbiVpn vpn = vpnBusinessExceutorMap.get(templateName).executeDeploy(businessModel);
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("vpnId", vpn.getId());
        insertNsResponseInfo(instanceId, resultMap);
        RecordProgress.increaseCurrentStep(instanceId);

        return resultMap;
    }

    @Override
    public Map<String, String> deleteOverlay(String instanceId, String templateName) throws ServiceException {
        ResultRsp<List<NsResponseInfo>> nsResponseInfoRsp = queryNsResponseInfo(instanceId);
        String nsResponseInfoUuid = nsResponseInfoRsp.getData().get(0).getUuid();
        String vpnUuid = nsResponseInfoRsp.getData().get(0).getExternalId();

        BusinessModel businessModel = vpnBusinessExceutorMap.get(templateName).executeQuery(vpnUuid);
        Map<String, String> response = vpnBusinessExceutorMap.get(templateName).executeUnDeploy(businessModel);

        dbOper.delete(NsResponseInfo.class, nsResponseInfoUuid);
        RecordProgress.increaseCurrentStep(instanceId);

        return response;
    }

    @Override
    public Map<String, String> createUnderlay(VpnVo vpnVo, String instanceId) throws ServiceException {
        RecordProgress.increaseCurrentStep(instanceId);

        Map<String, String> response = underlaySbiService.createUnderlay(vpnVo);
        RecordProgress.increaseCurrentStep(instanceId);

        insertNsResponseInfo(instanceId, response);
        RecordProgress.increaseCurrentStep(instanceId);

        return response;
    }

    @Override
    public Map<String, String> deleteUnderlay(String instanceId, List<ServiceParameter> serviceParameterList)
            throws ServiceException {
        ResultRsp<List<NsResponseInfo>> nsResponseInfoRsp = queryNsResponseInfo(instanceId);

        String nsResponseInfoUuid = nsResponseInfoRsp.getData().get(0).getUuid();
        String vpnUuid = nsResponseInfoRsp.getData().get(0).getExternalId();

        String serviceType = null;
        for(ServiceParameter serviceParameter : serviceParameterList) {
            if("serviceType".equals(serviceParameter.getInputKey())) {
                serviceType = serviceParameter.getInputValue();
                break;
            }
        }

        Map<String, String> response = underlaySbiService.deleteUnderlay(vpnUuid, serviceType);
        dbOper.delete(NsResponseInfo.class, nsResponseInfoUuid);
        return response;
    }

    @Override
    public NsInstanceQueryResponse queryVpn(String instanceId) throws ServiceException {
        InvServiceModel invServiceModel = iServiceModelDao.queryServiceById(instanceId);

        ServicePackageModel servicePackageModel = iServicePackageDao.queryServiceById(instanceId);
        List<ServiceParameter> ServiceParameterList = iServiceParameterDao.queryServiceById(instanceId);

        NsInstanceQueryResponse nsInstanceQueryResponse = new NsInstanceQueryResponse();
        nsInstanceQueryResponse.setId(invServiceModel.getServiceId());
        nsInstanceQueryResponse.setName(invServiceModel.getServiceName());
        nsInstanceQueryResponse.setDescription(invServiceModel.getDescription());
        nsInstanceQueryResponse.setNsdId(servicePackageModel.getTemplateId());

        return setAdditionalParam(nsInstanceQueryResponse, ServiceParameterList);
    }

    private NsInstanceQueryResponse setAdditionalParam(NsInstanceQueryResponse nsInstanceQueryResponse,
            List<ServiceParameter> ServiceParameterList) {
        Map<String, Object> additionalParamMap = new HashMap<String, Object>();
        for(ServiceParameter serviceParameter : ServiceParameterList) {
            additionalParamMap.put(serviceParameter.getInputKey(), serviceParameter.getInputValue());
        }
        nsInstanceQueryResponse.setAdditionalParams(additionalParamMap);
        return nsInstanceQueryResponse;
    }

    private ResultRsp<List<NsResponseInfo>> queryNsResponseInfo(String instanceId) throws ServiceException {
        if(!dbOper.checkRecordIsExisted(NsResponseInfo.class, Const.INSTANCE_ID, instanceId)) {
            ThrowException.throwDataIsExisted(instanceId);
        }
        return dbOper.query(NsResponseInfo.class, Const.INSTANCE_ID, instanceId);
    }

    private void insertNsResponseInfo(String instanceId, Map<String, String> response) throws ServiceException {
        List<NsResponseInfo> nsResponseInfoList = new ArrayList<>();
        NsResponseInfo nsResponseInfo = new NsResponseInfo();
        nsResponseInfo.setInstanceId(instanceId);
        nsResponseInfo.setExternalId(response.get("vpnId"));
        nsResponseInfo.allocateUuid();
        nsResponseInfoList.add(nsResponseInfo);
        dbOper.insert(nsResponseInfoList);
    }
}
