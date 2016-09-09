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

package org.openo.sdno.nslcm.model.translator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.util.UuidUtils;
import org.openo.sdno.model.servicemodel.tp.Tp;
import org.openo.sdno.model.servicemodel.tp.TpTypeSpec;
import org.openo.sdno.model.servicemodel.vpn.Vpn;
import org.openo.sdno.model.servicemodel.vpn.VpnBasicInfo;
import org.openo.sdno.model.servicemodel.vpn.VpnVo;
import org.openo.sdno.nslcm.model.db.NsInstantiationInfo;
import org.openo.sdno.overlayvpn.brs.invdao.NetworkElementInvDao;
import org.openo.sdno.overlayvpn.model.servicechain.ServicePathHop;
import org.openo.sdno.overlayvpn.model.servicemodel.SiteToDcNbi;

/**
 * Class of NsCreationInfo Model Data. <br>
 * <p>
 * It is used to recode the NsCreationInfo data that passed by caller.
 * </p>
 * 
 * @author
 * @version SDNO 0.5 Aug 30, 2016
 */
public class Translator {

    private Translator() {

    }

    public static SiteToDcNbi translateList2Overlay(List<NsInstantiationInfo> nsInstantiationInfoList)
            throws ServiceException {
        SiteToDcNbi siteToDcNbiMo = new SiteToDcNbi();
        Map<String, String> inputMap = translateList2Map(nsInstantiationInfoList);

        siteToDcNbiMo.setName(inputMap.get("name"));
        siteToDcNbiMo.setDescription(inputMap.get("description"));
        setSiteNbi(inputMap, siteToDcNbiMo);
        setVpcNbi(inputMap, siteToDcNbiMo);
        setSfpNbi(inputMap, siteToDcNbiMo);
        return siteToDcNbiMo;

    }

    public static VpnVo translateList2Underlay(List<NsInstantiationInfo> nsInstantiationInfoList)
            throws ServiceException {
        Vpn vpnMo = new Vpn();
        VpnBasicInfo vpnBasicInfo = new VpnBasicInfo();
        Tp srcTpMo = new Tp();
        Tp dstTpMo = new Tp();
        TpTypeSpec srcTpTypeSpec = new TpTypeSpec();
        TpTypeSpec dstTpTypeSpec = new TpTypeSpec();

        Map<String, String> inputMap = translateList2Map(nsInstantiationInfoList);

        setVpnBasicInfo(vpnBasicInfo, inputMap);

        setSrcTpMo(srcTpMo, srcTpTypeSpec, inputMap);

        setDstTpMo(dstTpMo, dstTpTypeSpec, inputMap);

        setVpnMo(vpnMo, vpnBasicInfo, srcTpMo, dstTpMo, inputMap);

        return new VpnVo(vpnMo);
    }

    private static Map<String, String> translateList2Map(List<NsInstantiationInfo> nsInstantiationInfoList) {
        Map<String, String> inputMap = new HashMap<String, String>();
        for(NsInstantiationInfo nsInstantiationInfo : nsInstantiationInfoList) {
            inputMap.put(nsInstantiationInfo.getName(), nsInstantiationInfo.getValue());
        }
        return inputMap;
    }

    private static void setVpnBasicInfo(VpnBasicInfo vpnBasicInfo, Map<String, String> inputMap) {
        vpnBasicInfo.setUuid(UuidUtils.createUuid());
        vpnBasicInfo.setTopology(inputMap.get("topology"));
        vpnBasicInfo.setServiceType(inputMap.get("serviceType"));
        vpnBasicInfo.setTechnology(inputMap.get("technology"));
        vpnBasicInfo.setAdminStatus("up");
    }

    private static void setSrcTpMo(Tp srcTpMo, TpTypeSpec srcTpTypeSpec, Map<String, String> inputMap)
            throws ServiceException {
        srcTpMo.setName(inputMap.get("ac1_port"));
        srcTpMo.setNeId(getMeUuid(inputMap.get("pe1_ip")));
        srcTpTypeSpec.getEthernetTpSpec().setQinqSvlanList(inputMap.get("ac1_svlan"));
        srcTpTypeSpec.getEthernetTpSpec().setAccessType("dot1q");
        srcTpTypeSpec.getIpTpSpec().setMasterIp(inputMap.get("ac1_ip"));
        srcTpTypeSpec.setLayerRate("LR_Ethernet");
        setCommonTpModel(srcTpMo);
        srcTpMo.getTypeSpecList().add(srcTpTypeSpec);
    }

    private static void setDstTpMo(Tp dstTpMo, TpTypeSpec dstTpTypeSpec, Map<String, String> inputMap)
            throws ServiceException {
        dstTpMo.setName(inputMap.get("ac2_port"));
        dstTpMo.setNeId(getMeUuid(inputMap.get("pe2_ip")));
        dstTpTypeSpec.getEthernetTpSpec().setQinqSvlanList(inputMap.get("ac2_svlan"));
        dstTpTypeSpec.getEthernetTpSpec().setAccessType("dot1q");
        dstTpTypeSpec.getIpTpSpec().setMasterIp(inputMap.get("ac2_ip"));
        dstTpTypeSpec.setLayerRate("LR_Ethernet");
        setCommonTpModel(dstTpMo);
        dstTpMo.getTypeSpecList().add(dstTpTypeSpec);
    }

    private static void setCommonTpModel(Tp tpMo) {
        tpMo.setId(UuidUtils.createUuid());
        tpMo.setAdminStatus("up");
        tpMo.setOperStatus("up");
        tpMo.setEdgePointRole("PE");
        tpMo.setHubSpoke("other");
        tpMo.setType("CTP");
        tpMo.setWorkingLayer("LR_Ethernet");
    }

    private static void setVpnMo(Vpn vpnMo, VpnBasicInfo vpnBasicInfo, Tp srcTpMo, Tp dstTpMo,
            Map<String, String> inputMap) {
        vpnMo.setId(UuidUtils.createUuid());
        vpnMo.setName(inputMap.get("name"));
        vpnMo.setDescription(inputMap.get("description"));
        vpnMo.setOperStatus("up");
        vpnMo.setSyncStatus("sync");
        vpnMo.getAccessPointList().add(srcTpMo);
        vpnMo.getAccessPointList().add(dstTpMo);
        vpnMo.setVpnBasicInfo(vpnBasicInfo);
    }

    private static void setSiteNbi(Map<String, String> inputMap, SiteToDcNbi siteToDcNbiMo) throws ServiceException {
        siteToDcNbiMo.getSite().setCidr(inputMap.get("siteCidr"));
        siteToDcNbiMo.getSite().setThinCpeId(getMeUuid(inputMap.get("siteThinCpeIP")));
        siteToDcNbiMo.getSite().setPortAndVlan(inputMap.get("siteAccessPortVlan"));
        siteToDcNbiMo.getSite().setVCPEId(getMeUuid(inputMap.get("vCPE_MgrIp")));
    }

    private static void setVpcNbi(Map<String, String> inputMap, SiteToDcNbi siteToDcNbiMo) {
        siteToDcNbiMo.getVpc().setName(inputMap.get("vpcName"));
        siteToDcNbiMo.getVpc().getSite().setName(inputMap.get("vpcSubnetName"));
        siteToDcNbiMo.getVpc().getSite().setCidr(inputMap.get("vpcSubnetCidr"));
        siteToDcNbiMo.getVpc().getSite().setVni(Integer.valueOf(inputMap.get("vpcVNI")));
    }

    private static void setSfpNbi(Map<String, String> inputMap, SiteToDcNbi siteToDcNbiMo) throws ServiceException {
        siteToDcNbiMo.getSfp().setScfNeId(getMeUuid(inputMap.get("dcGWIP")));
        setSfpPathHop(inputMap.get("dcFWIP"), siteToDcNbiMo, 1);
        setSfpPathHop(inputMap.get("dcLBIP"), siteToDcNbiMo, 2);
    }

    private static void setSfpPathHop(String ipAddress, SiteToDcNbi siteToDcNbiMo, Integer hopNumber)
            throws ServiceException {
        ServicePathHop servicePathHop = new ServicePathHop();
        servicePathHop.setHopNumber(hopNumber);
        servicePathHop.setSfiId(getMeUuid(ipAddress));
        siteToDcNbiMo.getSfp().getServicePathHop().add(servicePathHop);
    }

    private static String getMeUuid(String ipAddress) throws ServiceException {
        NetworkElementInvDao neInvDao = new NetworkElementInvDao();
        Map<String, String> condition = new HashMap<String, String>();
        condition.put("ipAddress", ipAddress);
        return neInvDao.query(condition).get(0).getId();
    }
}
