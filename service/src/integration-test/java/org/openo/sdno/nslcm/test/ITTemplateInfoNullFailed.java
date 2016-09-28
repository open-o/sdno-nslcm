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

package org.openo.sdno.nslcm.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.exception.HttpCode;
import org.openo.sdno.nslcm.mocoserver.MocoTemplateInfoNull;
import org.openo.sdno.testframework.checker.IChecker;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.http.model.HttpResponse;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.testmanager.TestManager;

public class ITTemplateInfoNullFailed extends TestManager {

    private static MocoTemplateInfoNull sbiAdapterServer = new MocoTemplateInfoNull();

    private static final String CREATE_NSLCM_EXISTED_FAILED_TESTCASE =
            "src/integration-test/resources/testcase/createnslcmfailed.json";

    @BeforeClass
    public static void setup() throws ServiceException {
        sbiAdapterServer.start();
    }

    @AfterClass
    public static void tearDown() throws ServiceException {
        sbiAdapterServer.stop();
    }

    @Test
    public void testNsdIdExistedFailed() throws ServiceException {
        testCreateNslcm(CREATE_NSLCM_EXISTED_FAILED_TESTCASE);
    }

    private void testCreateNslcm(String createNslcmPath) throws ServiceException {
        HttpRquestResponse httpCreateObject = HttpModelUtils.praseHttpRquestResponseFromFile(createNslcmPath);
        HttpRequest createRequest = httpCreateObject.getRequest();
        execTestCase(createRequest, new FailedChecker());
    }

    private class FailedChecker implements IChecker {

        @Override
        public boolean check(HttpResponse response) {
            if(HttpCode.isSucess(response.getStatus())) {
                return false;
            }
            return true;
        }

    }

}