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

package org.openo.sdno.overlayvpn.mocoserver;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.overlayvpn.mocoserver.model.AcBranchResponse;
import org.openo.sdno.overlayvpn.mocoserver.model.NetVxLanDeviceModel;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.http.model.HttpResponse;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.moco.MocoHttpsServer;
import org.openo.sdno.testframework.moco.responsehandler.MocoResponseHandler;

public class ACBranchMocoServer extends MocoHttpsServer {

    private static final String QUERY_THINCPE_WAN_SUBINF =
            "src/integration-test/resources/AcBranchController/QueryThinCPEWanSubInf.json";

    private static final String QUERY_VCPE_SUBINF =
            "src/integration-test/resources/AcBranchController/QueryVCPEWanSubInf.json";

    private static final String SSO_LOGIN1 = "src/integration-test/resources/AcBranchController/SSOLogin1.json";

    private static final String SSO_LOGIN2 = "src/integration-test/resources/AcBranchController/SSOLogin2.json";

    private static final String CREATE_THINCPE_VXLAN =
            "src/integration-test/resources/AcBranchController/CreateThinCPEVxLan.json";

    private static final String CREATE_VCPE_VXLAN =
            "src/integration-test/resources/AcBranchController/CreatevCPEVxLan.json";

    private static final String DELETE_THINCPE_VXLAN =
            "src/integration-test/resources/AcBranchController/DeleteThinCPEVxLan.json";

    private static final String DELETE_VCPE_VXLAN =
            "src/integration-test/resources/AcBranchController/DeletevCPEVxLan.json";

    private static final String QUERY_VCPE_IPSEC_BY_ID =
            "src/integration-test/resources/AcBranchController/QueryVCPEIPSecById.json";

    private static final String QUERY_VCPE_IPSEC_BY_INFNAME =
            "src/integration-test/resources/AcBranchController/QueryVCPEIPSecByInfName.json";

    private static final String CREATE_VCPE_IPSEC =
            "src/integration-test/resources/AcBranchController/CreateVCPEIPSec.json";

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
