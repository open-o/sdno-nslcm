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

import org.openo.sdno.testframework.moco.MocoHttpServer;

public class LocalSiteMocoServer extends MocoHttpServer {

    private static final String BASE_MOCOFILE_PATH = "src/integration-test/resources/moco/localsite";

    public LocalSiteMocoServer() {
        super(8548);
    }

    @Override
    public void addRequestResponsePairs() {
        addCloudCpeRequestResponsePairs();
        addLocalCpeRequestResponsePairs();
        addSiteRequestResponsePairs();
        addVlanRequestResponsePairs();
        addSubnetRequestResponsePairs();
    }

    private void addCloudCpeRequestResponsePairs() {
        this.addRequestResponsePair(generateMocoFilePath("cloudcpe/createcloudcpe.json"));
        this.addRequestResponsePair(generateMocoFilePath("cloudcpe/deletecloudcpe.json"));
        this.addRequestResponsePair(generateMocoFilePath("cloudcpe/querycloudcpe.json"));
    }

    private void addLocalCpeRequestResponsePairs() {
        this.addRequestResponsePair(generateMocoFilePath("localcpe/createlocalcpe.json"));
        this.addRequestResponsePair(generateMocoFilePath("localcpe/deletelocalcpe.json"));
    }

    private void addSiteRequestResponsePairs() {
        this.addRequestResponsePair(generateMocoFilePath("site/createsite.json"));
        this.addRequestResponsePair(generateMocoFilePath("site/deletesite.json"));
        this.addRequestResponsePair(generateMocoFilePath("site/querysite.json"));
    }

    private void addVlanRequestResponsePairs() {
        this.addRequestResponsePair(generateMocoFilePath("vlan/createvlan.json"));
        this.addRequestResponsePair(generateMocoFilePath("vlan/deletevlan.json"));
    }

    private void addSubnetRequestResponsePairs() {
        this.addRequestResponsePair(generateMocoFilePath("subnet/createsubnet.json"));
        this.addRequestResponsePair(generateMocoFilePath("subnet/deletesubnet.json"));
    }

    private String generateMocoFilePath(String mocoFileName) {
        return BASE_MOCOFILE_PATH + "/" + mocoFileName;
    }

}
