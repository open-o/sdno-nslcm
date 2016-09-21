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

import org.openo.sdno.testframework.moco.MocoHttpsServer;

public class MocoUnderlayE2ESuccessHttpsServer extends MocoHttpsServer {

    private static final String AUTH_TOKEN_FILE = "src/integration-test/resources/acwancontroller/authentication.json";

    private static final String CREATE_L3VPN = "src/integration-test/resources/acwancontroller/createsuccess.json";

    private static final String DELETE_L3VPN = "src/integration-test/resources/acwancontroller/deletesuccess.json";

    private static final String QUERY_L3VPN = "src/integration-test/resources/acwancontroller/getsuccess.json";

    private static final String UPDATE_L3VPN = "src/integration-test/resources/acwancontroller/updatesuccess.json";

    public MocoUnderlayE2ESuccessHttpsServer() {
        super();
    }

    @Override
    public void addRequestResponsePairs() {
        this.addRequestResponsePair(AUTH_TOKEN_FILE);

        this.addRequestResponsePair(CREATE_L3VPN);

        this.addRequestResponsePair(DELETE_L3VPN);

        this.addRequestResponsePair(QUERY_L3VPN);

        this.addRequestResponsePair(UPDATE_L3VPN);
    }
}
