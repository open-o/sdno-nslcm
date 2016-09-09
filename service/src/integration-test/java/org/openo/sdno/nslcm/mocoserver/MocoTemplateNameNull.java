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

public class MocoTemplateNameNull extends MocoHttpServer {

    private static final String QUERY_TEMPLATE_NAME_NULL =
            "src/integration-test/resources/mocosuccess/querytemplatenull.json";

    public MocoTemplateNameNull() {
        super();
    }

    @Override
    public void addRequestResponsePairs() {

        this.addRequestResponsePair(QUERY_TEMPLATE_NAME_NULL);
    }
}
