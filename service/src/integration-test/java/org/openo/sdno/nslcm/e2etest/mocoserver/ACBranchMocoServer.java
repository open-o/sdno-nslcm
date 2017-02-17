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

package org.openo.sdno.nslcm.e2etest.mocoserver;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.nslcm.e2etest.mocoserver.model.AcBranchResponse;
import org.openo.sdno.nslcm.e2etest.mocoserver.model.NetVxLanDeviceModel;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.http.model.HttpResponse;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.moco.MocoHttpsServer;
import org.openo.sdno.testframework.moco.responsehandler.MocoResponseHandler;

public class ACBranchMocoServer extends MocoHttpsServer {

    private static final String CREATE_THINCPE_VXLAN =
            "src/integration-test/resources/AcBranchController/CreateThinCPEVxLan.json";

    private static final String CREATE_VCPE_IPSEC =
            "src/integration-test/resources/AcBranchController/CreateVCPEIPSec.json";

    private static final String CREATE_VCPE_VXLAN =
            "src/integration-test/resources/AcBranchController/CreatevCPEVxLan.json";

    private static final String DELETE_THINCPE_VXLAN =
            "src/integration-test/resources/AcBranchController/DeleteThinCPEVxLan.json";

    private static final String DELETE_VCPE_VXLAN =
            "src/integration-test/resources/AcBranchController/DeletevCPEVxLan.json";

    private static final String QUERY_THINCPE_WAN_SUBINF =
            "src/integration-test/resources/AcBranchController/QueryThinCPEWanSubInf.json";

    private static final String QUERY_VCPE_IPSEC_BY_ID =
            "src/integration-test/resources/AcBranchController/QueryVCPEIPSecById.json";

    private static final String QUERY_VCPE_IPSEC_BY_INFNAME =
            "src/integration-test/resources/AcBranchController/QueryVCPEIPSecByInfName.json";

    private static final String QUERY_VCPE_SUBINF =
            "src/integration-test/resources/AcBranchController/QueryVCPEWanSubInf.json";

    private static final String SSO_LOGIN1 = "src/integration-test/resources/AcBranchController/SSOLogin1.json";

    private static final String SSO_LOGIN2 = "src/integration-test/resources/AcBranchController/SSOLogin2.json";

    private static final String CREATE_IKEPOLICY =
            "src/integration-test/resources/OSControllerIpSec/CreateIkePolicy.json";

    private static final String CREATE_IPSEC_CONNECTION =
            "src/integration-test/resources/OSControllerIpSec/CreateIpsecConnection.json";

    private static final String CREATE_IPSECPOLICY =
            "src/integration-test/resources/OSControllerIpSec/CreateIpsecPolicy.json";

    private static final String CREATE_VPN_SERVICE =
            "src/integration-test/resources/OSControllerIpSec/CreateVpnService.json";

    private static final String DELETE_IKEPOLICY =
            "src/integration-test/resources/OSControllerIpSec/DeleteIkePolicy.json";

    private static final String DELETE_IPSEC_CONNECTION =
            "src/integration-test/resources/OSControllerIpSec/DeleteIpsecConnection.json";

    private static final String DELETE_IPSECPOLICY =
            "src/integration-test/resources/OSControllerIpSec/DeleteIpsecPolicy.json";

    private static final String DELETE_VPN_SERVICE =
            "src/integration-test/resources/OSControllerIpSec/DeleteVpnService.json";

    private static final String LOGIN_OPEN_STATCK_1 =
            "src/integration-test/resources/OSControllerIpSec/LoginOpenStack.json";

    private static final String ATTACH_SUBNET = "src/integration-test/resources/OSControllerVpc/AttachSubnet.json";

    private static final String CREATE_NETWORK = "src/integration-test/resources/OSControllerVpc/CreateNetwork.json";

    private static final String CREATE_PROJECT = "src/integration-test/resources/OSControllerVpc/CreateProject.json";

    private static final String CREATE_ROUTER = "src/integration-test/resources/OSControllerVpc/CreateRouter.json";

    private static final String CREATE_SUBNET = "src/integration-test/resources/OSControllerVpc/CreateSubnet.json";

    private static final String DELETE_NETWORK = "src/integration-test/resources/OSControllerVpc/DeleteNetwork.json";

    private static final String DELETE_PROJECT = "src/integration-test/resources/OSControllerVpc/DeleteProject.json";

    private static final String DELETE_ROUTER = "src/integration-test/resources/OSControllerVpc/DeleteRouter.json";

    private static final String DELETE_SUBNET = "src/integration-test/resources/OSControllerVpc/DeleteSubnet.json";

    private static final String DETACH_SUBNET = "src/integration-test/resources/OSControllerVpc/DetachSubnet.json";

    private static final String LOGIN_OPEN_STATCK =
            "src/integration-test/resources/OSControllerVpc/LoginOpenStack.json";

    private static final String QUERY_NETWORK = "src/integration-test/resources/OSControllerVpc/QueryNetwork.json";

    private static final String QUERY_PROJECT = "src/integration-test/resources/OSControllerVpc/QueryProject.json";

    private static final String QUERY_ROUTER = "src/integration-test/resources/OSControllerVpc/QueryRouter.json";

    private static final String QUERY_SUBNET = "src/integration-test/resources/OSControllerVpc/QuerySubnet.json";

    public ACBranchMocoServer(int port) {
        super(port);
    }

    @Override
    public void addRequestResponsePairs() {
        this.addRequestResponsePair(QUERY_THINCPE_WAN_SUBINF);
        this.addRequestResponsePair(QUERY_VCPE_SUBINF);
        this.addRequestResponsePair(SSO_LOGIN1);
        this.addRequestResponsePair(SSO_LOGIN2);
        this.addRequestResponsePair(CREATE_THINCPE_VXLAN);
        this.addRequestResponsePair(CREATE_VCPE_VXLAN, new CreateVxLanResponseHandler());
        this.addRequestResponsePair(DELETE_THINCPE_VXLAN);
        this.addRequestResponsePair(DELETE_VCPE_VXLAN);
        this.addRequestResponsePair(QUERY_VCPE_IPSEC_BY_ID);
        this.addRequestResponsePair(QUERY_VCPE_IPSEC_BY_INFNAME);
        this.addRequestResponsePair(CREATE_VCPE_IPSEC);
        this.addRequestResponsePair(LOGIN_OPEN_STATCK);
        this.addRequestResponsePair(QUERY_PROJECT);
        this.addRequestResponsePair(CREATE_PROJECT);
        this.addRequestResponsePair(QUERY_NETWORK);
        this.addRequestResponsePair(QUERY_ROUTER);
        this.addRequestResponsePair(CREATE_ROUTER);
        this.addRequestResponsePair(QUERY_SUBNET);
        this.addRequestResponsePair(CREATE_SUBNET);
        this.addRequestResponsePair(LOGIN_OPEN_STATCK_1);
        this.addRequestResponsePair(CREATE_IKEPOLICY);
        this.addRequestResponsePair(CREATE_IPSECPOLICY);
        this.addRequestResponsePair(CREATE_IPSEC_CONNECTION);
        this.addRequestResponsePair(CREATE_VPN_SERVICE);
        this.addRequestResponsePair(DELETE_IKEPOLICY);
        this.addRequestResponsePair(DELETE_IPSECPOLICY);
        this.addRequestResponsePair(DELETE_IPSEC_CONNECTION);
        this.addRequestResponsePair(DELETE_VPN_SERVICE);
        this.addRequestResponsePair(ATTACH_SUBNET);
        this.addRequestResponsePair(DELETE_PROJECT);
        this.addRequestResponsePair(DELETE_ROUTER);
        this.addRequestResponsePair(DELETE_SUBNET);
        this.addRequestResponsePair(DETACH_SUBNET);
        this.addRequestResponsePair(CREATE_NETWORK);
        this.addRequestResponsePair(DELETE_NETWORK);
    }

    private class CreateVxLanResponseHandler extends MocoResponseHandler {

        @Override
        public void processRequestandResponse(HttpRquestResponse httpObject) {

            HttpRequest request = httpObject.getRequest();
            Map<String, List<NetVxLanDeviceModel>> requestObject = JsonUtil.fromJson(request.getData(),
                    new TypeReference<Map<String, List<NetVxLanDeviceModel>>>() {});
            List<NetVxLanDeviceModel> reqModelList = requestObject.get("vxlanList");

            HttpResponse response = httpObject.getResponse();
            AcBranchResponse<List<NetVxLanDeviceModel>> responseObj = JsonUtil.fromJson(response.getData(),
                    new TypeReference<AcBranchResponse<List<NetVxLanDeviceModel>>>() {});

            List<NetVxLanDeviceModel> rspModelList = responseObj.getData();

            for(int modelIndex = 0; modelIndex < rspModelList.size(); modelIndex++) {
                rspModelList.get(modelIndex).setName(reqModelList.get(modelIndex).getName());
            }

            response.setData(JsonUtil.toJson(responseObj));
        }

    }
}
