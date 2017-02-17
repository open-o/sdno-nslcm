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

package org.openo.sdno.nslcm.mocoserver.overlayvpn;

import org.openo.sdno.nslcm.responsehandler.CommonCreateResponseHandler;
import org.openo.sdno.testframework.moco.MocoHttpServer;

public class OverlayVpnMocoServer extends MocoHttpServer {

    private static final String BASE_MOCOFILE_PATH = "src/integration-test/resources/moco/overlayvpn";

    public OverlayVpnMocoServer() {
        super(8503);
    }

    @Override
    public void addRequestResponsePairs() {
        addVpnRequestResponsePairs();
        addVpnConnectionRequestResponsePairs();
        addVpnGatewayRequestResponsePairs();
    }

    private void addVpnRequestResponsePairs() {
        this.addRequestResponsePair(generateMocoFilePath("vpn/createvpn.json"), new CommonCreateResponseHandler());
        this.addRequestResponsePair(generateMocoFilePath("vpn/deletevpn.json"));
        this.addRequestResponsePair(generateMocoFilePath("vpn/queryvpn.json"));
    }

    private void addVpnConnectionRequestResponsePairs() {
        this.addRequestResponsePair(generateMocoFilePath("vpnconnection/createvpnconnection.json"),
                new CommonCreateResponseHandler());
        this.addRequestResponsePair(generateMocoFilePath("vpnconnection/deletevpnconnection.json"));
    }

    private void addVpnGatewayRequestResponsePairs() {
        this.addRequestResponsePair(generateMocoFilePath("vpngateway/createvpngateway.json"),
                new CommonCreateResponseHandler());
        this.addRequestResponsePair(generateMocoFilePath("vpngateway/deletevpngateway.json"));
    }

    private String generateMocoFilePath(String mocoFileName) {
        return BASE_MOCOFILE_PATH + "/" + mocoFileName;
    }

}
