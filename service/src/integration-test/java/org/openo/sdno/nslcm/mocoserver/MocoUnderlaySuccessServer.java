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

public class MocoUnderlaySuccessServer extends MocoHttpServer {

    private static final String QUERY_TEMPLATE_UNDERLAY =
            "src/integration-test/resources/mocosuccess/querytemplateunderlay.json";

    private static final String CREATE_L2VPN = "src/integration-test/resources/mocosuccess/createl2vpn.json";

    private static final String CREATE_L3VPN = "src/integration-test/resources/mocosuccess/createl3vpn.json";

    private static final String DELETE_L2VPN = "src/integration-test/resources/mocosuccess/deletel2vpn.json";

    private static final String DELETE_L3VPN = "src/integration-test/resources/mocosuccess/deletel3vpn.json";

    public MocoUnderlaySuccessServer() {
        super();
    }

    @Override
    public void addRequestResponsePairs() {

        this.addRequestResponsePair(QUERY_TEMPLATE_UNDERLAY);

        this.addRequestResponsePair(CREATE_L2VPN);

        this.addRequestResponsePair(CREATE_L3VPN);

        this.addRequestResponsePair(DELETE_L2VPN);

        this.addRequestResponsePair(DELETE_L3VPN);
    }
}
