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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.util.UuidUtils;
import org.openo.sdno.model.servicemodel.tp.CeTp;
import org.openo.sdno.model.servicemodel.tp.EthernetTpSpec;
import org.openo.sdno.model.servicemodel.tp.IpTpSpec;
import org.openo.sdno.model.servicemodel.tp.Tp;
import org.openo.sdno.model.servicemodel.tp.TpTypeSpec;
import org.openo.sdno.model.servicemodel.vpn.Vpn;
import org.openo.sdno.model.servicemodel.vpn.VpnBasicInfo;
import org.openo.sdno.model.servicemodel.vpn.VpnVo;
import org.openo.sdno.nslcm.model.servicemo.ServiceParameter;
import org.openo.sdno.overlayvpn.brs.invdao.NetworkElementInvDao;
import org.openo.sdno.overlayvpn.model.servicechain.ServicePathHop;
import org.openo.sdno.overlayvpn.model.servicemodel.SfpNbi;
import org.openo.sdno.overlayvpn.model.servicemodel.SiteNbi;
import org.openo.sdno.overlayvpn.model.servicemodel.SiteToDcNbi;
import org.openo.sdno.overlayvpn.model.servicemodel.SubnetNbi;
import org.openo.sdno.overlayvpn.model.servicemodel.VpcNbi;

/**
 * Class of NsCreationInfo Model Data. <br>
 * <p>
 * It is used to recode the NsCreationInfo data that passed by caller.
 * </p>
 * 
 * @author
 * @version SDNO 0.5 August 30, 2016
 */
public class Translator {

    private Translator() {

    }

    /**
     * Translate NsInstantiation Info List to SiteToDcNbi model.<br>
     * 
     * @param nsInstantiationInfoList NsInstantiation Info List
     * @return The SiteToDcNbi object
     * @throws ServiceException When translate failed
     * @since SDNO 0.5
     */
    public static SiteToDcNbi translateList2Overlay(List<ServiceParameter> serviceParameterList)
            throws ServiceException {
        SiteToDcNbi siteToDcNbiMo = new SiteToDcNbi();
        Map<String, String> inputMap = translateList2Map(serviceParameterList);

        siteToDcNbiMo.setName(inputMap.get("name"));
        siteToDcNbiMo.setDescription(inputMap.get("description"));
        setSiteNbi(inputMap, siteToDcNbiMo);
        setVpcNbi(inputMap, siteToDcNbiMo);
        setSfpNbi(inputMap, siteToDcNbiMo);
        return siteToDcNbiMo;

    }

    /**
     * Translate NsInstantiation Info List to VpnVo model.<br>
     * 
     * @param nsInstantiationInfoList NsInstantiation Info List
     * @return The VpnVo object
     * @throws ServiceException When translate failed
     * @since SDNO 0.5
     */
    public static VpnVo translateList2Underlay(List<ServiceParameter> serviceParameterList) throws ServiceException {
        Vpn vpnMo = new Vpn();
        VpnBasicInfo vpnBasicInfo = new VpnBasicInfo();
        Tp srcTpMo = new Tp();
        Tp dstTpMo = new Tp();
        TpTypeSpec srcTpTypeSpec = new TpTypeSpec();
        TpTypeSpec dstTpTypeSpec = new TpTypeSpec();

        Map<String, String> inputMap = translateList2Map(serviceParameterList);

        setVpnBasicInfo(vpnBasicInfo, inputMap);

        setSrcTpMo(srcTpMo, srcTpTypeSpec, inputMap);

        setDstTpMo(dstTpMo, dstTpTypeSpec, inputMap);

        setVpnMo(vpnMo, vpnBasicInfo, srcTpMo, dstTpMo, inputMap);

        return new VpnVo(vpnMo);
    }

    private static Map<String, String> translateList2Map(List<ServiceParameter> serviceParameterList) {
        Map<String, String> inputMap = new HashMap<String, String>();
        for(ServiceParameter serviceParameter : serviceParameterList) {
            inputMap.put(serviceParameter.getInputKey(), serviceParameter.getInputValue());
        }
        return inputMap;
    }

    private static void setVpnBasicInfo(VpnBasicInfo vpnBasicInfo, Map<String, String> inputMap) {
        vpnBasicInfo.setUuid(UuidUtils.createUuid());
        vpnBasicInfo.setTopology(inputMap.get("topology"));
        vpnBasicInfo.setServiceType(inputMap.get("serviceType"));
        vpnBasicInfo.setTechnology(inputMap.get("technology"));
        vpnBasicInfo.setAdminStatus("inactive");
    }

    private static void setSrcTpMo(Tp srcTpMo, TpTypeSpec srcTpTypeSpec, Map<String, String> inputMap)
            throws ServiceException {
        srcTpMo.setName(inputMap.get("ac1_port"));
        srcTpMo.setNeId(getMeUuid(inputMap.get("pe1_ip")));

        EthernetTpSpec ethernetTpSpec = new EthernetTpSpec();
        ethernetTpSpec.setQinqSvlanList(inputMap.get("ac1_svlan"));
        ethernetTpSpec.setAccessType("dot1q");
        srcTpTypeSpec.setEthernetTpSpec(ethernetTpSpec);

        IpTpSpec ipTpSpec = new IpTpSpec();
        ipTpSpec.setMasterIp(inputMap.get("ac1_ip"));
        srcTpTypeSpec.setIpTpSpec(ipTpSpec);

        srcTpTypeSpec.setLayerRate("LR_Ethernet");
        setCommonTpModel(srcTpMo);

        List<TpTypeSpec> tpTypeSpecList = new ArrayList<TpTypeSpec>();
        tpTypeSpecList.add(srcTpTypeSpec);
        srcTpMo.setTypeSpecList(tpTypeSpecList);

        CeTp srcPeerCeTp = new CeTp();
        srcPeerCeTp.setUuid(UuidUtils.createUuid());
        srcPeerCeTp.setCeIfmasterIp(inputMap.get("ac1_peer_ip"));

        srcTpMo.setPeerCeTp(srcPeerCeTp);
    }

    private static void setDstTpMo(Tp dstTpMo, TpTypeSpec dstTpTypeSpec, Map<String, String> inputMap)
            throws ServiceException {

        dstTpMo.setName(inputMap.get("ac2_port"));
        dstTpMo.setNeId(getMeUuid(inputMap.get("pe2_ip")));

        EthernetTpSpec ethernetTpSpec = new EthernetTpSpec();
        ethernetTpSpec.setQinqSvlanList(inputMap.get("ac2_svlan"));
        ethernetTpSpec.setAccessType("dot1q");
        dstTpTypeSpec.setEthernetTpSpec(ethernetTpSpec);

        IpTpSpec ipTpSpec = new IpTpSpec();
        ipTpSpec.setMasterIp(inputMap.get("ac2_ip"));
        dstTpTypeSpec.setIpTpSpec(ipTpSpec);

        dstTpTypeSpec.setLayerRate("LR_Ethernet");
        setCommonTpModel(dstTpMo);

        List<TpTypeSpec> tpTypeSpecList = new ArrayList<TpTypeSpec>();
        tpTypeSpecList.add(dstTpTypeSpec);
        dstTpMo.setTypeSpecList(tpTypeSpecList);

        CeTp dstPeerCeTp = new CeTp();
        dstPeerCeTp.setUuid(UuidUtils.createUuid());
        dstPeerCeTp.setCeIfmasterIp(inputMap.get("ac2_peer_ip"));

        dstTpMo.setPeerCeTp(dstPeerCeTp);
    }

    private static void setCommonTpModel(Tp tpMo) {
        tpMo.setId(UuidUtils.createUuid());
        tpMo.setAdminStatus("inactive");
        tpMo.setOperStatus("up");
        tpMo.setType("CTP");
        tpMo.setWorkingLayer("LR_IP");
    }

    private static void setVpnMo(Vpn vpnMo, VpnBasicInfo vpnBasicInfo, Tp srcTpMo, Tp dstTpMo,
            Map<String, String> inputMap) {
        vpnMo.setId(UuidUtils.createUuid());
        vpnMo.setName(inputMap.get("name"));
        vpnMo.setDescription(inputMap.get("description"));
        vpnMo.setOperStatus("up");
        vpnMo.setSyncStatus("sync");

        List<Tp> accessPointList = new ArrayList<Tp>();
        accessPointList.add(srcTpMo);
        accessPointList.add(dstTpMo);
        vpnMo.setAccessPointList(accessPointList);

        vpnMo.setVpnBasicInfo(vpnBasicInfo);
    }

    private static void setSiteNbi(Map<String, String> inputMap, SiteToDcNbi siteToDcNbiMo) throws ServiceException {
        SiteNbi siteNbi = new SiteNbi();
        siteNbi.setCidr(inputMap.get("siteCidr"));
        siteNbi.setThinCpeId(getMeUuid(inputMap.get("siteThinCpeIP")));
        siteNbi.setPortAndVlan(inputMap.get("siteAccessPortVlan"));
        siteNbi.setVCPEId(getMeUuid(inputMap.get("vCPE_MgrIp")));
        siteToDcNbiMo.setSite(siteNbi);
    }

    private static void setVpcNbi(Map<String, String> inputMap, SiteToDcNbi siteToDcNbiMo) {
        VpcNbi vpcNbi = new VpcNbi();
        SubnetNbi subnetNbi = new SubnetNbi();
        subnetNbi.setName(inputMap.get("vpcSubnetName"));
        subnetNbi.setCidr(inputMap.get("vpcSubnetCidr"));
        subnetNbi.setVni(Integer.valueOf(inputMap.get("vpcVNI")));
        vpcNbi.setName(inputMap.get("vpcName"));
        vpcNbi.setSite(subnetNbi);
        siteToDcNbiMo.setVpc(vpcNbi);
    }

    private static void setSfpNbi(Map<String, String> inputMap, SiteToDcNbi siteToDcNbiMo) throws ServiceException {
        SfpNbi sfpNbi = new SfpNbi();
        sfpNbi.setScfNeId(getMeUuid(inputMap.get("dcGWIP")));
        setSfpPathHop(inputMap.get("dcFWIP"), sfpNbi, 1);
        setSfpPathHop(inputMap.get("dcLBIP"), sfpNbi, 2);
        siteToDcNbiMo.setSfp(sfpNbi);
    }

    private static void setSfpPathHop(String ipAddress, SfpNbi sfpNbi, Integer hopNumber) throws ServiceException {
        ServicePathHop servicePathHop = new ServicePathHop();
        servicePathHop.setHopNumber(hopNumber);
        servicePathHop.setSfiId(getMeUuid(ipAddress));
        servicePathHop.setSfgId("");
        sfpNbi.getServicePathHops().add(servicePathHop);
    }

    private static String getMeUuid(String ipAddress) throws ServiceException {
        NetworkElementInvDao neInvDao = new NetworkElementInvDao();
        Map<String, String> condition = new HashMap<String, String>();
        condition.put("ipAddress", ipAddress);
        return neInvDao.query(condition).get(0).getId();
    }
}
