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

package org.openo.sdno.nslcm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.model.servicemodel.vpn.VpnVo;
import org.openo.sdno.nslcm.dao.inf.IServiceModelDao;
import org.openo.sdno.nslcm.dao.inf.IServicePackageDao;
import org.openo.sdno.nslcm.dao.inf.IServiceParameterDao;
import org.openo.sdno.nslcm.model.db.NsResponseInfo;
import org.openo.sdno.nslcm.model.nbi.NsInstanceQueryResponse;
import org.openo.sdno.nslcm.model.nbi.SdnoTemplateParameter;
import org.openo.sdno.nslcm.model.servicemo.InvServiceModel;
import org.openo.sdno.nslcm.model.servicemo.ServicePackageModel;
import org.openo.sdno.nslcm.model.servicemo.ServiceParameter;
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
 * NSLCM service implementation. <br>
 * 
 * @author
 * @version SDNO 0.5 June 16, 2016
 */
@Service
public class NslcmServiceImpl implements NslcmService {

    @Resource
    private DbOper dbOper;

    private IServiceModelDao iServiceModelDao;

    private IServicePackageDao iServicePackageDao;

    private IServiceParameterDao iServiceParameterDao;

    @Resource
    private CatalogSbiService catalogSbiService;

    @Resource
    private OverlaySbiService overlaySbiService;

    @Resource
    private UnderlaySbiService underlaySbiService;

    public void setiServiceModelDao(IServiceModelDao iServiceModelDao) {
        this.iServiceModelDao = iServiceModelDao;
    }

    public void setiServicePackageDao(IServicePackageDao iServicePackageDao) {
        this.iServicePackageDao = iServicePackageDao;
    }

    public void setiServiceParameterDao(IServiceParameterDao iServiceParameterDao) {
        this.iServiceParameterDao = iServiceParameterDao;
    }

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
    public Map<String, Object> queryServiceTemplate(String nsdId) throws ServiceException {
        return catalogSbiService.queryServiceTemplate(nsdId);
    }

    @Override
    public Map<String, String> createOverlay(SiteToDcNbi siteToDcNbiMo, String instanceId) throws ServiceException {
        Map<String, String> response = overlaySbiService.createOverlay(siteToDcNbiMo);

        insertNsResponseInfo(instanceId, response);
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

        insertNsResponseInfo(instanceId, response);
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

        return setSdnoTemplateParameterList(nsInstanceQueryResponse, ServiceParameterList);
    }

    private NsInstanceQueryResponse setSdnoTemplateParameterList(NsInstanceQueryResponse nsInstanceQueryResponse,
            List<ServiceParameter> ServiceParameterList) {
        List<SdnoTemplateParameter> sdnoTemplateParameterList = new ArrayList<SdnoTemplateParameter>();
        for(ServiceParameter serviceParameter : ServiceParameterList) {
            SdnoTemplateParameter sdnoTemplateParameterMo = new SdnoTemplateParameter();
            sdnoTemplateParameterMo.setName(serviceParameter.getInputKey());
            sdnoTemplateParameterMo.setValue(serviceParameter.getInputValue());
            sdnoTemplateParameterList.add(sdnoTemplateParameterMo);
        }
        nsInstanceQueryResponse.setAdditionalParams(sdnoTemplateParameterList);
        return nsInstanceQueryResponse;
    }

    private ResultRsp<List<NsResponseInfo>> queryNsResponseInfo(String instanceId) throws ServiceException {
        if(!dbOper.checkRecordIsExisted(NsResponseInfo.class, Const.INSTANCE_ID, instanceId)) {
            ThrowException.throwDataIsExisted(instanceId);
        }
        return dbOper.query(NsResponseInfo.class, Const.INSTANCE_ID, instanceId);
    }

    private void insertNsResponseInfo(String instanceId, Map<String, String> response) throws ServiceException {
        List<NsResponseInfo> nsResponseInfoList = new ArrayList<NsResponseInfo>();
        NsResponseInfo nsResponseInfo = new NsResponseInfo();
        nsResponseInfo.setInstanceId(instanceId);
        nsResponseInfo.setExternalId(response.get("vpnId"));
        nsResponseInfo.allocateUuid();
        nsResponseInfoList.add(nsResponseInfo);
        dbOper.insert(nsResponseInfoList);
    }
}
