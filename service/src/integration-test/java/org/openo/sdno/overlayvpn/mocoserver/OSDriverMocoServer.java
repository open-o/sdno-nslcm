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

import org.openo.sdno.testframework.moco.MocoHttpsServer;

public class OSDriverMocoServer extends MocoHttpsServer {

    private static final String LOGIN_OPEN_STATCK =
            "src/integration-test/resources/OSControllerVpc/LoginOpenStack.json";

    private static final String QUERY_PROJECT = "src/integration-test/resources/OSControllerVpc/QueryProject.json";

    private static final String CREATE_PROJECT = "src/integration-test/resources/OSControllerVpc/CreateProject.json";

    private static final String QUERY_NETWORK = "src/integration-test/resources/OSControllerVpc/QueryNetwork.json";

    private static final String QUERY_ROUTER = "src/integration-test/resources/OSControllerVpc/QueryRouter.json";

    private static final String CREATE_ROUTER = "src/integration-test/resources/OSControllerVpc/CreateRouter.json";

    private static final String QUERY_SUBNET = "src/integration-test/resources/OSControllerVpc/QuerySubnet.json";

    private static final String CREATE_SUBNET = "src/integration-test/resources/OSControllerVpc/CreateSubnet.json";

    private static final String LOGIN_OPEN_STATCK_1 =
            "src/integration-test/resources/OSControllerIpSec/LoginOpenStack.json";

    private static final String CREATE_IKEPOLICY =
            "src/integration-test/resources/OSControllerIpSec/CreateIkePolicy.json";

    private static final String CREATE_IPSECPOLICY =
            "src/integration-test/resources/OSControllerIpSec/CreateIpsecPolicy.json";

    private static final String CREATE_IPSEC_CONNECTION =
            "src/integration-test/resources/OSControllerIpSec/CreateIpsecConnection.json";

    private static final String CREATE_VPN_SERVICE =
            "src/integration-test/resources/OSControllerIpSec/CreateVpnService.json";

    private static final String DELETE_IKEPOLICY =
            "src/integration-test/resources/OSControllerIpSec/DeleteIkePolicy.json";

    private static final String DELETE_IPSECPOLICY =
            "src/integration-test/resources/OSControllerIpSec/DeleteIpsecPolicy.json";

    private static final String DELETE_IPSEC_CONNECTION =
            "src/integration-test/resources/OSControllerIpSec/DeleteIpsecConnection.json";

    private static final String DELETE_VPN_SERVICE =
            "src/integration-test/resources/OSControllerIpSec/DeleteVpnService.json";

    public OSDriverMocoServer(int port) {
        super(port);
    }

    @Override
    public void addRequestResponsePairs() {
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
    }
}
