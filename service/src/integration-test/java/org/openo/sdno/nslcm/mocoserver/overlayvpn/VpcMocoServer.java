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

public class VpcMocoServer extends MocoHttpServer {

    private static final String BASE_MOCOFILE_PATH = "src/integration-test/resources/moco/vpc";

    public VpcMocoServer() {
        super(8518);
    }

    @Override
    public void addRequestResponsePairs() {
        addVpcRequestResponsePairs();
        addVpcSubnetRequestResponsePairs();
    }

    private void addVpcRequestResponsePairs() {
        this.addRequestResponsePair(generateMocoFilePath("vpc/createvpc.json"));
        this.addRequestResponsePair(generateMocoFilePath("vpc/deletevpc.json"));
        this.addRequestResponsePair(generateMocoFilePath("vpc/queryvpc.json"));
    }

    private void addVpcSubnetRequestResponsePairs() {
        this.addRequestResponsePair(generateMocoFilePath("vpcsubnet/createvpcsubnet.json"));
        this.addRequestResponsePair(generateMocoFilePath("vpcsubnet/deletevpcsubnet.json"));
        this.addRequestResponsePair(generateMocoFilePath("vpcsubnet/queryvpcsubnet.json"));
    }

    private String generateMocoFilePath(String mocoFileName) {
        return BASE_MOCOFILE_PATH + "/" + mocoFileName;
    }

}
