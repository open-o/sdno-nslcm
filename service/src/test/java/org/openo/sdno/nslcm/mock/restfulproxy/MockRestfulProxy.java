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

package org.openo.sdno.nslcm.mock.restfulproxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.exception.HttpCode;
import org.openo.sdno.framework.container.resthelper.RestfulProxy;
import org.openo.sdno.framework.container.util.UuidUtils;
import org.openo.sdno.nslcm.util.AdapterUrlConst;
import org.openo.sdno.overlayvpn.brs.model.NetworkElementMO;
import org.openo.sdno.overlayvpn.model.servicechain.ServiceChainPath;
import org.openo.sdno.overlayvpn.model.servicemodel.SubNet;
import org.openo.sdno.overlayvpn.model.servicemodel.Vpc;
import org.openo.sdno.overlayvpn.model.v2.cpe.NbiCloudCpeModel;
import org.openo.sdno.overlayvpn.model.v2.overlay.NbiVpn;
import org.openo.sdno.overlayvpn.model.v2.overlay.NbiVpnConnection;
import org.openo.sdno.overlayvpn.model.v2.overlay.NbiVpnGateway;
import org.openo.sdno.overlayvpn.model.v2.site.NbiSiteModel;
import org.openo.sdno.overlayvpn.model.v2.subnet.NbiSubnetModel;
import org.openo.sdno.overlayvpn.model.v2.vlan.NbiVlanModel;
import org.openo.sdno.testframework.util.file.JsonUtil;

import mockit.Mock;
import mockit.MockUp;

public class MockRestfulProxy extends MockUp<RestfulProxy> {

    private static RestfulResponse defaultRestfulResponse = new RestfulResponse();
    static {
        defaultRestfulResponse.setStatus(HttpCode.RESPOND_OK);
    }

    @Mock
    public static RestfulResponse get(String uri, RestfulParametes restParametes) throws ServiceException {
        if(uri.contains(AdapterUrlConst.CATALOG_ADAPTER_URL)) {
            return createCatalogGetResponse();
        } else if(uri.contains(AdapterUrlConst.VPN_ADAPTER_URL)) {
            return createVpnGetResponse();
        } else if(uri.contains(AdapterUrlConst.SERVICECHAIN_ADAPTER_URL)) {
            return createServiceChainGetResponse();
        } else if(uri.contains(AdapterUrlConst.SITE_ADAPTER_URL)) {
            return createSiteGetResponse();
        } else if(uri.contains(AdapterUrlConst.VPC_ADAPTER_URL)) {
            return createVpcGetResponse();
        } else if(uri.contains(AdapterUrlConst.VPCSUBNET_ADAPTER_URL)) {
            return createVpcSubnetGetResponse();
        } else if(uri.contains(AdapterUrlConst.CLOUDCPE_ADAPTER_URL)) {
            return createCloudCpeGetResponse();
        }
        return defaultRestfulResponse;
    }

    @Mock
    public static RestfulResponse post(String uri, RestfulParametes restParametes) throws ServiceException {
        if(uri.contains(AdapterUrlConst.VPN_ADAPTER_URL)) {
            return createVpnPostResponse();
        } else if(uri.contains(AdapterUrlConst.VPN_GATEWAY_ADAPTER_URL)) {
            return createVpnGatewayPostResponse();
        } else if(uri.contains(AdapterUrlConst.VPN_CONNECTION_ADAPTER_URL)) {
            return createVpnConnectionPostResponse();
        }
        return defaultRestfulResponse;
    }

    @Mock
    public static RestfulResponse delete(String uri, RestfulParametes restParametes) throws ServiceException {
        return defaultRestfulResponse;
    }

    private static RestfulResponse createCatalogGetResponse() {
        RestfulResponse restfulResponse = new RestfulResponse();
        restfulResponse.setStatus(HttpCode.RESPOND_OK);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("templateName", "enterprise2DC");
        resultMap.put("csarId", "test");
        restfulResponse.setResponseJson(JsonUtil.toJson(resultMap));
        return restfulResponse;
    }

    private static RestfulResponse createVpnPostResponse() {
        RestfulResponse restfulResponse = new RestfulResponse();
        restfulResponse.setStatus(HttpCode.RESPOND_OK);
        NbiVpn vpn = new NbiVpn();
        vpn.setUuid("nsInstance");
        restfulResponse.setResponseJson(JsonUtil.toJson(vpn));
        return restfulResponse;
    }

    private static RestfulResponse createVpnGetResponse() {
        RestfulResponse restfulResponse = new RestfulResponse();
        restfulResponse.setStatus(HttpCode.RESPOND_OK);
        NbiVpn vpn = new NbiVpn();
        vpn.setUuid("nsInstance");
        Set<String> siteIdList = new HashSet<String>();
        siteIdList.add("SiteId");
        vpn.setSiteList(siteIdList);
        Set<String> vpcIdList = new HashSet<String>();
        vpcIdList.add("VpcId");
        vpn.setVpcList(vpcIdList);

        NbiVpnGateway siteGateway = new NbiVpnGateway();
        siteGateway.setUuid(UuidUtils.createUuid());
        siteGateway.setSiteId("SiteId");
        siteGateway.setVpnId("nsInstance");

        NbiVpnGateway vpcGateway = new NbiVpnGateway();
        vpcGateway.setUuid(UuidUtils.createUuid());
        vpcGateway.setVpcId("VpcId");
        vpcGateway.setVpnId("nsInstance");

        vpn.setVpnGateways(new ArrayList<NbiVpnGateway>());
        vpn.getVpnGateways().add(siteGateway);
        vpn.getVpnGateways().add(vpcGateway);

        NbiVpnConnection connection = new NbiVpnConnection();
        connection.setUuid(UuidUtils.createUuid());
        connection.setVpnId("VpnId");
        connection.setaEndVpnGatewayId(siteGateway.getUuid());
        connection.setzEndVpnGatewayId(vpcGateway.getUuid());
        vpn.setVpnConnections(Arrays.asList(connection));

        restfulResponse.setResponseJson(JsonUtil.toJson(vpn));
        return restfulResponse;
    }

    private static RestfulResponse createServiceChainGetResponse() {
        RestfulResponse restfulResponse = new RestfulResponse();
        restfulResponse.setStatus(HttpCode.RESPOND_OK);
        ServiceChainPath sfp = new ServiceChainPath();
        sfp.setUuid("nsInstance");
        restfulResponse.setResponseJson(JsonUtil.toJson(sfp));
        return restfulResponse;
    }

    private static RestfulResponse createSiteGetResponse() {
        RestfulResponse restfulResponse = new RestfulResponse();
        restfulResponse.setStatus(HttpCode.RESPOND_OK);
        NbiSiteModel siteModel = new NbiSiteModel();
        siteModel.setUuid("SiteId");
        NetworkElementMO localCpeNe = new NetworkElementMO();
        localCpeNe.setId("LocalCpeNeId");
        NetworkElementMO cloudCpeNe = new NetworkElementMO();
        cloudCpeNe.setId("CloudCpeNeId");
        siteModel.setLocalCpes(Arrays.asList(localCpeNe));
        siteModel.setCloudCpes(Arrays.asList(cloudCpeNe));

        NbiVlanModel vlanModel = new NbiVlanModel();
        vlanModel.setUuid(UuidUtils.createUuid());
        vlanModel.setSiteId("SiteId");
        siteModel.setVlans(Arrays.asList(vlanModel));

        NbiSubnetModel subnetModel = new NbiSubnetModel();
        subnetModel.setUuid(UuidUtils.createUuid());
        subnetModel.setSiteId("SiteId");
        siteModel.setSubnets(Arrays.asList(subnetModel));

        restfulResponse.setResponseJson(JsonUtil.toJson(siteModel));
        return restfulResponse;
    }

    private static RestfulResponse createVpcGetResponse() {
        RestfulResponse restfulResponse = new RestfulResponse();
        restfulResponse.setStatus(HttpCode.RESPOND_OK);
        Vpc vpc = new Vpc();
        vpc.setUuid("VpcId");
        restfulResponse.setResponseJson(JsonUtil.toJson(vpc));
        return restfulResponse;
    }

    private static RestfulResponse createVpcSubnetGetResponse() {
        RestfulResponse restfulResponse = new RestfulResponse();
        restfulResponse.setStatus(HttpCode.RESPOND_OK);
        SubNet subnet = new SubNet();
        subnet.setUuid(UuidUtils.createUuid());
        subnet.setVpcId("VpcId");
        restfulResponse.setResponseJson(JsonUtil.toJson(Arrays.asList(subnet)));
        return restfulResponse;
    }

    private static RestfulResponse createCloudCpeGetResponse() {
        RestfulResponse restfulResponse = new RestfulResponse();
        restfulResponse.setStatus(HttpCode.RESPOND_OK);
        NbiCloudCpeModel cloudCpeModel = new NbiCloudCpeModel();
        cloudCpeModel.setUuid("CloudCpeNeId");
        cloudCpeModel.setSiteId("SiteId");
        restfulResponse.setResponseJson(JsonUtil.toJson(cloudCpeModel));
        return restfulResponse;
    }

    private static RestfulResponse createVpnGatewayPostResponse() {
        RestfulResponse restfulResponse = new RestfulResponse();
        restfulResponse.setStatus(HttpCode.RESPOND_OK);
        NbiVpnGateway vpnGateway = new NbiVpnGateway();
        vpnGateway.setUuid(UuidUtils.createUuid());
        vpnGateway.setVpnId("nsInstance");
        restfulResponse.setResponseJson(JsonUtil.toJson(vpnGateway));
        return restfulResponse;
    }

    private static RestfulResponse createVpnConnectionPostResponse() {
        RestfulResponse restfulResponse = new RestfulResponse();
        restfulResponse.setStatus(HttpCode.RESPOND_OK);
        NbiVpnConnection vpnConnection = new NbiVpnConnection();
        vpnConnection.setUuid(UuidUtils.createUuid());
        vpnConnection.setVpnId("nsInstance");
        restfulResponse.setResponseJson(JsonUtil.toJson(vpnConnection));
        return restfulResponse;
    }

}
