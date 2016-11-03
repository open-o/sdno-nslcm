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
import org.openo.sdno.model.common.NVString;
import org.openo.sdno.model.servicemodel.businesstype.TunnelSchema;
import org.openo.sdno.model.servicemodel.routeprotocol.BgpProtocolItem;
import org.openo.sdno.model.servicemodel.routeprotocol.RouteProtocolSpec;
import org.openo.sdno.model.servicemodel.routeprotocol.StaticRouteTable;
import org.openo.sdno.model.servicemodel.tp.EthernetTpSpec;
import org.openo.sdno.model.servicemodel.tp.IpTpSpec;
import org.openo.sdno.model.servicemodel.tp.Tp;
import org.openo.sdno.model.servicemodel.tp.TpTypeSpec;
import org.openo.sdno.model.servicemodel.tunnel.MplsTESpec;
import org.openo.sdno.model.servicemodel.tunnel.PWSpec;
import org.openo.sdno.model.servicemodel.tunnel.TunnelPathConstraint;
import org.openo.sdno.model.servicemodel.vpn.Vpn;
import org.openo.sdno.model.servicemodel.vpn.VpnBasicInfo;
import org.openo.sdno.model.servicemodel.vpn.VpnVo;
import org.openo.sdno.nslcm.model.servicemo.ServiceParameter;
import org.openo.sdno.overlayvpn.brs.invdao.LogicalTernminationPointInvDao;
import org.openo.sdno.overlayvpn.brs.invdao.NetworkElementInvDao;
import org.openo.sdno.overlayvpn.model.servicechain.ServicePathHop;
import org.openo.sdno.overlayvpn.model.servicemodel.SfpNbi;
import org.openo.sdno.overlayvpn.model.servicemodel.SiteNbi;
import org.openo.sdno.overlayvpn.model.servicemodel.SiteToDcNbi;
import org.openo.sdno.overlayvpn.model.servicemodel.SubnetNbi;
import org.openo.sdno.overlayvpn.model.servicemodel.VpcNbi;
import org.springframework.util.StringUtils;

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
    public static SiteToDcNbi translateList2Overlay(List<ServiceParameter> serviceParameterList, String instanceId)
            throws ServiceException {
        SiteToDcNbi siteToDcNbiMo = new SiteToDcNbi();
        Map<String, String> inputMap = translateList2Map(serviceParameterList);

        siteToDcNbiMo.setName(inputMap.get("name"));
        siteToDcNbiMo.setDescription(inputMap.get("description"));
        siteToDcNbiMo.setUuid(instanceId);
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
    public static VpnVo translateList2Underlay(List<ServiceParameter> serviceParameterList, String instanceId)
            throws ServiceException {
        Map<String, String> inputMap = translateList2Map(serviceParameterList);

        String serviceType = inputMap.get("serviceType");

        TunnelSchema tunnelSchema = new TunnelSchema();
        setTunnelSchema(tunnelSchema, serviceType);

        VpnBasicInfo vpnBasicInfo = new VpnBasicInfo();
        setVpnBasicInfo(vpnBasicInfo, inputMap);

        Tp srcTpMo = new Tp();
        setSrcTpMo(srcTpMo, inputMap, serviceType);

        Tp dstTpMo = new Tp();
        setDstTpMo(dstTpMo, inputMap, serviceType);

        Vpn vpnMo = new Vpn();
        setVpnMo(vpnMo, vpnBasicInfo, srcTpMo, dstTpMo, inputMap, instanceId);

        VpnVo vpnVo = new VpnVo();
        vpnVo.setVpn(vpnMo);
        vpnVo.setTunnelSchema(tunnelSchema);
        return vpnVo;
    }

    private static Map<String, String> translateList2Map(List<ServiceParameter> serviceParameterList) {
        Map<String, String> inputMap = new HashMap<String, String>();
        for(ServiceParameter serviceParameter : serviceParameterList) {
            inputMap.put(serviceParameter.getInputKey(), serviceParameter.getInputValue());
        }
        return inputMap;
    }

    private static void setTunnelSchema(TunnelSchema tunnelSchema, String serviceType) {
        if("l3vpn".equals(serviceType)) {
            TunnelPathConstraint pathConstraint = new TunnelPathConstraint();
            pathConstraint.setSetupPriority(4);
            pathConstraint.setHoldupPriority(4);

            MplsTESpec tunnelCreatePolicy = new MplsTESpec();
            tunnelCreatePolicy.setBestEffort("false");
            tunnelCreatePolicy.setBfdEnable("true");
            tunnelCreatePolicy.setCoRoute("true");
            tunnelCreatePolicy.setShareMode("delegate");
            tunnelCreatePolicy.setPathConstraint(pathConstraint);

            tunnelSchema.setTunnelTech("RSVP-TE");
            tunnelSchema.setTunnelLatency(2000);
            tunnelSchema.setTunnelCreatePolicy(tunnelCreatePolicy);
        } else if("l2vpn".equals(serviceType)) {
            TunnelPathConstraint pathConstraint = new TunnelPathConstraint();
            pathConstraint.setSetupPriority(4);
            pathConstraint.setHoldupPriority(4);

            MplsTESpec tunnelCreatePolicy = new MplsTESpec();
            tunnelCreatePolicy.setBestEffort("true");
            tunnelCreatePolicy.setBfdEnable("true");
            tunnelCreatePolicy.setCoRoute("false");
            tunnelCreatePolicy.setShareMode("1:1");
            tunnelCreatePolicy.setPathConstraint(pathConstraint);

            tunnelSchema.setTunnelTech("RSVP-TE");
            tunnelSchema.setTunnelLatency(12);
            tunnelSchema.setTunnelCreatePolicy(tunnelCreatePolicy);

            PWSpec pwTech = new PWSpec();
            pwTech.setControlWord("ctrlWord");
            pwTech.setPwVlanAction("Raw");
            tunnelSchema.setPwTech(pwTech);
            tunnelSchema.setTunnelSelectMode("AutoCreate");
        }
    }

    private static void setVpnBasicInfo(VpnBasicInfo vpnBasicInfo, Map<String, String> inputMap) {
        vpnBasicInfo.setTopology(inputMap.get("topology"));
        vpnBasicInfo.setServiceType(inputMap.get("serviceType"));
        vpnBasicInfo.setTechnology(inputMap.get("technology"));
        vpnBasicInfo.setAdminStatus("active");
    }

    private static void setSrcTpMo(Tp srcTpMo, Map<String, String> inputMap, String serviceType)
            throws ServiceException {
        srcTpMo.setName(inputMap.get("ac1_port"));
        srcTpMo.setNeId(getMeUuid(inputMap.get("pe1_ip")));

        TpTypeSpec srcTpTypeSpec = new TpTypeSpec();

        EthernetTpSpec ethernetTpSpec = new EthernetTpSpec();
        ethernetTpSpec.setDot1qVlanList(inputMap.get("ac1_svlan"));
        ethernetTpSpec.setAccessType("dot1q");
        if("l2vpn".equals(serviceType)) {
            ethernetTpSpec.setActionValue("KEEP");
            ethernetTpSpec.setAccessType("untag");
        }
        srcTpTypeSpec.setEthernetTpSpec(ethernetTpSpec);

        if("l3vpn".equals(serviceType)) {
            IpTpSpec ipTpSpec = new IpTpSpec();
            ipTpSpec.setMasterIp(inputMap.get("ac1_ip"));
            srcTpTypeSpec.setIpTpSpec(ipTpSpec);
            srcTpTypeSpec.setLayerRate("LR_IP");
        }
        setCommonTpModel(srcTpMo, serviceType);

        List<TpTypeSpec> tpTypeSpecList = new ArrayList<TpTypeSpec>();
        tpTypeSpecList.add(srcTpTypeSpec);
        srcTpMo.setTypeSpecList(tpTypeSpecList);

        if("l3vpn".equals(serviceType)) {
            List<RouteProtocolSpec> routeProtocolSpecs = new ArrayList<RouteProtocolSpec>();

            String sRoute = inputMap.get("ac1_route");
            String[] sRouteArray = sRoute.split(";");

            for(String tempRoute : sRouteArray) {
                String[] staticRouteArray = tempRoute.split(",");
                if(staticRouteArray.length == 0) {
                    continue;
                }

                if(StringUtils.hasLength(staticRouteArray[0])) {
                    RouteProtocolSpec staticRouteProtocolSpecs = new RouteProtocolSpec();
                    StaticRouteTable staticRoute = new StaticRouteTable();
                    staticRoute.setDestinationCidr(staticRouteArray[0]);
                    staticRoute.setNextHopIp("");
                    if(staticRouteArray.length > 1) {
                        staticRoute.setNextHopIp(staticRouteArray[1]);
                    }

                    staticRouteProtocolSpecs.setType("staticRouting");
                    staticRouteProtocolSpecs.setStaticRoute(staticRoute);

                    routeProtocolSpecs.add(staticRouteProtocolSpecs);
                }
            }

            if(StringUtils.hasLength(inputMap.get("ac1_peer_ip"))) {
                RouteProtocolSpec srcRouteProtocolSpec = new RouteProtocolSpec();

                BgpProtocolItem bgpRoute = new BgpProtocolItem();
                setBgpRoute(bgpRoute);
                bgpRoute.setPeerIp(inputMap.get("ac1_peer_ip"));
                srcRouteProtocolSpec.setType("bgp");
                srcRouteProtocolSpec.setBgpRoute(bgpRoute);
                routeProtocolSpecs.add(srcRouteProtocolSpec);
            }

            srcTpMo.setRouteProtocolSpecs(routeProtocolSpecs);
        }

        if("l2vpn".equals(serviceType)) {
            NVString addtionalInfo = new NVString();
            addtionalInfo.setName("pwPeerIp");
            addtionalInfo.setValue("1.1.1.1");
            List<NVString> addtionalInfos = new ArrayList<NVString>();
            addtionalInfos.add(addtionalInfo);
            srcTpMo.setAddtionalInfo(addtionalInfos);
        }
        srcTpMo.setContainedMainTP(getPortNativeID(inputMap.get("ac1_port")));
    }

    private static void setDstTpMo(Tp dstTpMo, Map<String, String> inputMap, String serviceType)
            throws ServiceException {

        dstTpMo.setName(inputMap.get("ac2_port"));
        dstTpMo.setNeId(getMeUuid(inputMap.get("pe2_ip")));

        TpTypeSpec dstTpTypeSpec = new TpTypeSpec();

        EthernetTpSpec ethernetTpSpec = new EthernetTpSpec();
        ethernetTpSpec.setDot1qVlanList(inputMap.get("ac2_svlan"));
        ethernetTpSpec.setAccessType("dot1q");
        if("l2vpn".equals(serviceType)) {
            ethernetTpSpec.setActionValue("KEEP");
            ethernetTpSpec.setAccessType("untag");
        }
        dstTpTypeSpec.setEthernetTpSpec(ethernetTpSpec);

        if("l3vpn".equals(serviceType)) {
            IpTpSpec ipTpSpec = new IpTpSpec();
            ipTpSpec.setMasterIp(inputMap.get("ac2_ip"));
            dstTpTypeSpec.setIpTpSpec(ipTpSpec);
            dstTpTypeSpec.setLayerRate("LR_IP");
        }

        setCommonTpModel(dstTpMo, serviceType);

        List<TpTypeSpec> tpTypeSpecList = new ArrayList<TpTypeSpec>();
        tpTypeSpecList.add(dstTpTypeSpec);
        dstTpMo.setTypeSpecList(tpTypeSpecList);

        if("l3vpn".equals(serviceType)) {
            List<RouteProtocolSpec> routeProtocolSpecs = new ArrayList<RouteProtocolSpec>();

            String sRoute = inputMap.get("ac2_route");
            String[] sRouteArray = sRoute.split(";");

            for(String tempRoute : sRouteArray) {
                String[] staticRouteArray = tempRoute.split(",");
                if(staticRouteArray.length == 0) {
                    continue;
                }

                if(StringUtils.hasLength(staticRouteArray[0])) {
                    RouteProtocolSpec staticRouteProtocolSpecs = new RouteProtocolSpec();
                    StaticRouteTable staticRoute = new StaticRouteTable();
                    staticRoute.setDestinationCidr(staticRouteArray[0]);
                    staticRoute.setNextHopIp("");
                    if(staticRouteArray.length > 1) {
                        staticRoute.setNextHopIp(staticRouteArray[1]);
                    }

                    staticRouteProtocolSpecs.setType("staticRouting");
                    staticRouteProtocolSpecs.setStaticRoute(staticRoute);

                    routeProtocolSpecs.add(staticRouteProtocolSpecs);
                }
            }

            if(StringUtils.hasLength(inputMap.get("ac2_peer_ip"))) {
                BgpProtocolItem bgpRoute = new BgpProtocolItem();
                setBgpRoute(bgpRoute);
                bgpRoute.setPeerIp(inputMap.get("ac2_peer_ip"));

                RouteProtocolSpec dstRouteProtocolSpec = new RouteProtocolSpec();
                dstRouteProtocolSpec.setType("bgp");
                dstRouteProtocolSpec.setBgpRoute(bgpRoute);

                routeProtocolSpecs.add(dstRouteProtocolSpec);
            }
            dstTpMo.setRouteProtocolSpecs(routeProtocolSpecs);
        }

        if("l2vpn".equals(serviceType)) {
            NVString addtionalInfo = new NVString();
            addtionalInfo.setName("pwPeerIp");
            addtionalInfo.setValue("1.1.1.2");
            List<NVString> addtionalInfos = new ArrayList<NVString>();
            addtionalInfos.add(addtionalInfo);
            dstTpMo.setAddtionalInfo(addtionalInfos);
        }
        dstTpMo.setContainedMainTP(getPortNativeID(inputMap.get("ac2_port")));
    }

    private static void setBgpRoute(BgpProtocolItem bgpRoute) {
        bgpRoute.setIdx(0);
        bgpRoute.setPeerAsNumber(100);
        bgpRoute.setKeepAliveTime(0);
        bgpRoute.setHoldTime(0);
        bgpRoute.setPassword("200");
        bgpRoute.setBgpMaxPrefix(1000);
        bgpRoute.setBgpMaxPrefixAlarm(100);
    }

    private static void setCommonTpModel(Tp tpMo, String serviceType) {
        tpMo.setAdminStatus("active");
        tpMo.setOperStatus("up");
        if("l3vpn".equals(serviceType)) {
            tpMo.setType("CTP");
            tpMo.setWorkingLayer("LR_IP");
        }
    }

    private static void setVpnMo(Vpn vpnMo, VpnBasicInfo vpnBasicInfo, Tp srcTpMo, Tp dstTpMo,
            Map<String, String> inputMap, String instanceId) {
        vpnMo.setId(instanceId);
        vpnMo.setName(inputMap.get("name"));
        vpnMo.setDescription(inputMap.get("description"));
        vpnMo.setOperStatus("up");

        List<Tp> accessPointList = new ArrayList<Tp>();
        accessPointList.add(srcTpMo);
        accessPointList.add(dstTpMo);
        vpnMo.setAccessPointList(accessPointList);
        vpnMo.setVpnBasicInfo(vpnBasicInfo);
        List<NVString> addtionalInfoList = new ArrayList<NVString>();
        NVString addtionalInfo = new NVString();
        addtionalInfo.setName("encapsulation");
        addtionalInfo.setValue("eth");
        addtionalInfoList.add(addtionalInfo);
        vpnMo.setAddtionalInfo(addtionalInfoList);
    }

    private static void setSiteNbi(Map<String, String> inputMap, SiteToDcNbi siteToDcNbiMo) throws ServiceException {
        SiteNbi siteNbi = new SiteNbi();
        siteNbi.setCidr(inputMap.get("siteCidr"));
        siteNbi.setThinCpeId(getMeUuid(inputMap.get("siteThinCpeIP")));
        siteNbi.setPortAndVlan(inputMap.get("siteAccessPortVlan"));
        siteNbi.setvCPEId(getMeUuid(inputMap.get("vCPE_MgrIp")));
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

    private static String getPortNativeID(String portName) throws ServiceException {
        LogicalTernminationPointInvDao poryInvDao = new LogicalTernminationPointInvDao();
        Map<String, String> condition = new HashMap<String, String>();
        condition.put("name", portName);
        return poryInvDao.query(condition).get(0).getNativeID();
    }
}
