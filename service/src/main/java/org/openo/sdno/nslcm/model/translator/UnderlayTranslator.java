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
import org.openo.sdno.nslcm.dao.inf.IBaseResourceDao;
import org.openo.sdno.nslcm.model.servicemo.ServiceParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
@Component
public class UnderlayTranslator {

    @Autowired
    private IBaseResourceDao baseResourceDao;

    private UnderlayTranslator() {

    }

    /**
     * Translate NsInstantiation Info List to VpnVo model.<br>
     * 
     * @param nsInstantiationInfoList NsInstantiation Info List
     * @return The VpnVo object
     * @throws ServiceException When translate failed
     * @since SDNO 0.5
     */
    public VpnVo translateList2Underlay(List<ServiceParameter> serviceParameterList, String instanceId)
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

    private Map<String, String> translateList2Map(List<ServiceParameter> serviceParameterList) {
        Map<String, String> inputMap = new HashMap<String, String>();
        for(ServiceParameter serviceParameter : serviceParameterList) {
            inputMap.put(serviceParameter.getInputKey(), serviceParameter.getInputValue());
        }
        return inputMap;
    }

    private void setTunnelSchema(TunnelSchema tunnelSchema, String serviceType) {
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
            pwTech.setControlWord(true);
            pwTech.setPwVlanAction("Raw");
            tunnelSchema.setPwTech(pwTech);
            tunnelSchema.setTunnelSelectMode("AutoCreate");
        }
    }

    private void setVpnBasicInfo(VpnBasicInfo vpnBasicInfo, Map<String, String> inputMap) {
        vpnBasicInfo.setTopology(inputMap.get("topology"));
        vpnBasicInfo.setServiceType(inputMap.get("serviceType"));
        vpnBasicInfo.setTechnology(inputMap.get("technology"));
        vpnBasicInfo.setAdminStatus("active");
    }

    private void setSrcTpMo(Tp srcTpMo, Map<String, String> inputMap, String serviceType) throws ServiceException {
        srcTpMo.setName(inputMap.get("ac1_port"));
        srcTpMo.setNeId(baseResourceDao.queryNeByIpAddress(inputMap.get("pe1_ip")).getId());
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
        srcTpMo.setContainedMainTP(baseResourceDao.queryPortNativeID(inputMap.get("ac1_port")));
    }

    private void setDstTpMo(Tp dstTpMo, Map<String, String> inputMap, String serviceType) throws ServiceException {

        dstTpMo.setName(inputMap.get("ac2_port"));
        dstTpMo.setNeId(baseResourceDao.queryNeByIpAddress(inputMap.get("pe2_ip")).getId());

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
        dstTpMo.setContainedMainTP(baseResourceDao.queryPortNativeID(inputMap.get("ac2_port")));
    }

    private void setBgpRoute(BgpProtocolItem bgpRoute) {
        bgpRoute.setIdx(0);
        bgpRoute.setPeerAsNumber(100);
        bgpRoute.setKeepAliveTime(0);
        bgpRoute.setHoldTime(0);
        bgpRoute.setPassword("200");
        bgpRoute.setBgpMaxPrefix(1000);
        bgpRoute.setBgpMaxPrefixAlarm(100);
    }

    private void setCommonTpModel(Tp tpMo, String serviceType) {
        tpMo.setAdminStatus("active");
        tpMo.setOperStatus("up");
        if("l3vpn".equals(serviceType)) {
            tpMo.setType("CTP");
            tpMo.setWorkingLayer("LR_IP");
        }
    }

    private void setVpnMo(Vpn vpnMo, VpnBasicInfo vpnBasicInfo, Tp srcTpMo, Tp dstTpMo, Map<String, String> inputMap,
            String instanceId) {
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
}
