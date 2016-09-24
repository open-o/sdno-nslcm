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

import org.codehaus.jackson.type.TypeReference;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.framework.container.util.UuidUtils;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.openo.sdno.overlayvpn.model.netmodel.ipsec.DcGwIpSecConnection;
import org.openo.sdno.overlayvpn.model.netmodel.ipsec.NeIpSecConnection;
import org.openo.sdno.overlayvpn.model.netmodel.vpc.Vpc;
import org.openo.sdno.overlayvpn.model.netmodel.vxlan.NeVxlanInstance;
import org.openo.sdno.overlayvpn.model.servicechain.ServiceChainPath;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.http.model.HttpResponse;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.moco.MocoHttpServer;
import org.openo.sdno.testframework.moco.responsehandler.MocoResponseHandler;

public class MocoOverlayE2ESuccessServer extends MocoHttpServer {

    private static final String QUERY_TEMPLATE_OVERLAY =
            "src/integration-test/resources/AcBranchController/querytemplateoverlay.json";

    private static final String GET_ACBRANCH_CONTROLLER =
            "src/integration-test/resources/AcBranchController/getcontroller.json";

    private static final String GET_OSDRIVER_CONTROLLER =
            "src/integration-test/resources/OSControllerIpSec/getcontroller.json";

    private static final String AC_CREATE_IPSEC =
            "src/integration-test/resources/mocoservice/accreateipsecsuccess.json";

    private static final String AC_QUERY_WANSUB_INTERFACE =
            "src/integration-test/resources/mocoservice/acquerywansubinterfacesuccess.json";

    private static final String CREATE_IPSEC = "src/integration-test/resources/mocoservice/createipsecsuccess.json";

    private static final String CREATE_SERVICECHAIN =
            "src/integration-test/resources/mocoservice/createservicechainsuccess.json";

    private static final String CREATE_SUBNET = "src/integration-test/resources/mocoservice/createsubnetsuccess.json";

    private static final String CREATE_VPC = "src/integration-test/resources/mocoservice/createvpcsuccess.json";

    private static final String CREATE_VXLAN = "src/integration-test/resources/mocoservice/createvxlan.json";

    private static final String QUERY_VTEP = "src/integration-test/resources/mocoservice/queryvtep.json";

    private static final String QUERY_WANSUB_INTERFACE =
            "src/integration-test/resources/mocoservice/querywansubinterface.json";

    private static final String AC_DELETE_IPSEC =
            "src/integration-test/resources/mocoservice/acdeleteipsecsuccess.json";

    private static final String DELETE_IPSEC = "src/integration-test/resources/mocoservice/deleteipsecsuccess.json";

    private static final String DELETE_SERVICECHAIN =
            "src/integration-test/resources/mocoservice/deleteservicechainsuccess.json";

    private static final String DELETE_SUBNET = "src/integration-test/resources/mocoservice/deletesubnetsuccess.json";

    private static final String DELETE_VPC = "src/integration-test/resources/mocoservice/deletevpcsuccess.json";

    private static final String DELETE_VXLAN = "src/integration-test/resources/mocoservice/deletevxlan.json";

    public MocoOverlayE2ESuccessServer() {
        super();
    }

    @Override
    public void addRequestResponsePairs() {

        this.addRequestResponsePair(QUERY_TEMPLATE_OVERLAY);

        this.addRequestResponsePair(GET_ACBRANCH_CONTROLLER);

        this.addRequestResponsePair(GET_OSDRIVER_CONTROLLER);

        this.addRequestResponsePair(AC_CREATE_IPSEC, new CreateIpSecSuccessInAcResponseHandler());

        this.addRequestResponsePair(AC_QUERY_WANSUB_INTERFACE);

        this.addRequestResponsePair(CREATE_IPSEC, new CreateIpSecSuccessInOsResponseHandler());

        this.addRequestResponsePair(CREATE_SERVICECHAIN, new CreateServiceChainSuccessSbiResponseHandler());

        this.addRequestResponsePair(CREATE_SUBNET, new CreateSubnetResponseHandler());

        this.addRequestResponsePair(CREATE_VPC, new CreateVpcResponseHandler());

        this.addRequestResponsePair(CREATE_VXLAN, new CreateVxlanResponseHandler());

        this.addRequestResponsePair(QUERY_VTEP);

        this.addRequestResponsePair(QUERY_WANSUB_INTERFACE);

        this.addRequestResponsePair(AC_DELETE_IPSEC);

        this.addRequestResponsePair(DELETE_IPSEC);

        this.addRequestResponsePair(DELETE_SERVICECHAIN);

        this.addRequestResponsePair(DELETE_SUBNET);

        this.addRequestResponsePair(DELETE_VPC);

        this.addRequestResponsePair(DELETE_VXLAN);

    }

    private class CreateVpcResponseHandler extends MocoResponseHandler {

        @Override
        public void processRequestandResponse(HttpRquestResponse httpObject) {

            HttpRequest request = httpObject.getRequest();
            Vpc vpcMo = JsonUtil.fromJson(request.getData(), Vpc.class);

            Vpc.UnderlayResources attributes = new Vpc.UnderlayResources();
            attributes.setRouterId("test_router_id");
            attributes.setUuid(UuidUtils.createUuid());
            vpcMo.setAttributes(attributes);

            HttpResponse httpResponse = httpObject.getResponse();
            httpResponse.setStatus(200);
            httpResponse.setData(JsonUtil.toJson(vpcMo));
        }
    }

    private class CreateSubnetResponseHandler extends MocoResponseHandler {

        @Override
        public void processRequestandResponse(HttpRquestResponse httpObject) {

            HttpRequest request = httpObject.getRequest();
            HttpResponse httpResponse = httpObject.getResponse();
            httpResponse.setStatus(200);
            httpResponse.setData(request.getData());
        }
    }

    private class CreateIpSecSuccessInAcResponseHandler extends MocoResponseHandler {

        @Override
        public void processRequestandResponse(HttpRquestResponse httpObject) {

            HttpRequest httpRequest = httpObject.getRequest();
            HttpResponse httpResponse = httpObject.getResponse();
            List<NeIpSecConnection> inputInstanceList =
                    JsonUtil.fromJson(httpRequest.getData(), new TypeReference<List<NeIpSecConnection>>() {});

            ResultRsp<List<NeIpSecConnection>> newResult =
                    new ResultRsp<List<NeIpSecConnection>>(ErrorCode.OVERLAYVPN_SUCCESS);

            newResult.setData(inputInstanceList);
            httpResponse.setData(JsonUtil.toJson(newResult));
        }
    }

    private class CreateIpSecSuccessInOsResponseHandler extends MocoResponseHandler {

        @Override
        public void processRequestandResponse(HttpRquestResponse httpObject) {

            HttpRequest httpRequest = httpObject.getRequest();
            HttpResponse httpResponse = httpObject.getResponse();
            List<DcGwIpSecConnection> inputInstanceList =
                    JsonUtil.fromJson(httpRequest.getData(), new TypeReference<List<DcGwIpSecConnection>>() {});

            ResultRsp<List<DcGwIpSecConnection>> newResult =
                    new ResultRsp<List<DcGwIpSecConnection>>(ErrorCode.OVERLAYVPN_SUCCESS);

            newResult.setData(inputInstanceList);
            httpResponse.setData(JsonUtil.toJson(newResult));
        }
    }

    private class CreateVxlanResponseHandler extends MocoResponseHandler {

        @Override
        public void processRequestandResponse(HttpRquestResponse httpObject) {

            HttpRequest httpRequest = httpObject.getRequest();
            HttpResponse httpResponse = httpObject.getResponse();
            List<NeVxlanInstance> inputInstanceList =
                    JsonUtil.fromJson(httpRequest.getData(), new TypeReference<List<NeVxlanInstance>>() {});

            ResultRsp<List<NeVxlanInstance>> newResult =
                    new ResultRsp<List<NeVxlanInstance>>(ErrorCode.OVERLAYVPN_SUCCESS);

            newResult.setData(inputInstanceList);
            httpResponse.setData(JsonUtil.toJson(newResult));
        }
    }

    private class CreateServiceChainSuccessSbiResponseHandler extends MocoResponseHandler {

        @Override
        public void processRequestandResponse(HttpRquestResponse httpObject) {

            HttpRequest httpRequest = httpObject.getRequest();
            HttpResponse httpResponse = httpObject.getResponse();
            ServiceChainPath sfp = JsonUtil.fromJson(httpRequest.getData(), new TypeReference<ServiceChainPath>() {});

            ResultRsp<ServiceChainPath> newResult = new ResultRsp<ServiceChainPath>(ErrorCode.OVERLAYVPN_SUCCESS);

            newResult.setData(sfp);
            httpResponse.setData(JsonUtil.toJson(newResult));
        }
    }
}
