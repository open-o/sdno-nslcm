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

package org.openo.sdno.nslcm.mocoserver;

import org.openo.sdno.testframework.moco.MocoHttpServer;

public class MocoUnderlayE2ESuccessServer extends MocoHttpServer {

    private static final String QUERY_TEMPLATE_UNDERLAY =
            "src/integration-test/resources/acwancontroller/querytemplateunderlay.json";

    private static final String QUERY_CONTROLLER = "src/integration-test/resources/acwancontroller/getcontroller.json";

    private static final String ADD_TP = "src/integration-test/resources/acwancontroller/addtps.json";

    private static final String DELETE_TP = "src/integration-test/resources/acwancontroller/deletetps.json";

    private static final String CREATE_L2VPN = "src/integration-test/resources/acwancontroller/createl2vpn.json";

    private static final String DELETE_L2VPN = "src/integration-test/resources/acwancontroller/deletel2vpn.json";

    private static final String QUERY_L2VPN = "src/integration-test/resources/acwancontroller/queryl2vpndetail.json";

    private static final String UPDATE_L2VPN = "src/integration-test/resources/acwancontroller/updatel2vpn.json";

    public MocoUnderlayE2ESuccessServer() {
        super();
    }

    @Override
    public void addRequestResponsePairs() {

        this.addRequestResponsePair(QUERY_TEMPLATE_UNDERLAY);

        this.addRequestResponsePair(QUERY_CONTROLLER);

        this.addRequestResponsePair(ADD_TP);

        this.addRequestResponsePair(DELETE_TP);

        this.addRequestResponsePair(CREATE_L2VPN);

        this.addRequestResponsePair(DELETE_L2VPN);

        this.addRequestResponsePair(QUERY_L2VPN);

        this.addRequestResponsePair(UPDATE_L2VPN);
    }
}
