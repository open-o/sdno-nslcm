/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package java.org.openo.sdno.nslcm.mocoserver;

import org.openo.sdno.testframework.moco.MocoHttpServer;

public class SbiAdapterDeleteIpSecFailInDcServer extends MocoHttpServer {

    private static final String DELETE_IPSEC_SUCCESS_IN_AC_FILE =
            "src/integration-test/resources/acsbiadapter/deleteipsecsuccess.json";

    private static final String DELETE_IPSEC_FAIL_IN_DC_FILE =
            "src/integration-test/resources/ossbiadapter/deleteipsecfail.json";

    public SbiAdapterDeleteIpSecFailInDcServer() {

    }

    @Override
    public void addRequestResponsePairs() {
        this.addRequestResponsePair(DELETE_IPSEC_SUCCESS_IN_AC_FILE);

        this.addRequestResponsePair(DELETE_IPSEC_FAIL_IN_DC_FILE);
    }

}
