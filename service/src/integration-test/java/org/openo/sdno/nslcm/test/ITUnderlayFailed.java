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
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.nslcm.mocoserver.MocoUnderlaySuccessServer;
import org.openo.sdno.nslcm.model.nbi.NsCreationResponse;
import org.openo.sdno.nslcm.model.nbi.NsInstantiationRequest;
import org.openo.sdno.testframework.checker.IChecker;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.http.model.HttpResponse;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.replace.PathReplace;
import org.openo.sdno.testframework.testmanager.TestManager;
import org.openo.sdno.testframework.topology.Topology;

public class ITUnderlayFailed extends TestManager {

    private static MocoUnderlaySuccessServer sbiAdapterServer = new MocoUnderlaySuccessServer();

    private static final String CREATE_NSLCM_SUCCESS_TESTCASE =
            "src/integration-test/resources/testcase/createnslcmsuccess.json";

    private static final String CREATE_UNDERLAY_FAILED_TESTCASE =
            "src/integration-test/resources/testcase/createunderlayfailed.json";

    private static final String DELETE_UNDERLAY_FAILED_TESTCASE =
            "src/integration-test/resources/testcase/deleteunderlayfailed.json";

    private static final String DELETE_NSLCM_SUCCESS_TESTCASE =
            "src/integration-test/resources/testcase/deletenslcmsuccess.json";

    private static final String TOPODATA_PATH = "src/integration-test/resources/topodata";

    private static Topology topo = new Topology(TOPODATA_PATH);

    @BeforeClass
    public static void setup() throws ServiceException {
        topo.createInvTopology();
        sbiAdapterServer.start();
    }

    @AfterClass
    public static void tearDown() throws ServiceException {
        sbiAdapterServer.stop();
        topo.clearInvTopology();
    }

    @Test
    public void testCreateUnderlayFailed() throws ServiceException {
        String instanceId = testCreateNslcm(CREATE_NSLCM_SUCCESS_TESTCASE);

        HttpRquestResponse httpCreateUnderlayObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_UNDERLAY_FAILED_TESTCASE);
        HttpRequest createUnderlayRequest = httpCreateUnderlayObject.getRequest();
        createUnderlayRequest.setUri(PathReplace.replaceUuid("instanceid", createUnderlayRequest.getUri(), instanceId));

        NsInstantiationRequest nsInstantiationData =
                JsonUtil.fromJson(createUnderlayRequest.getData(), NsInstantiationRequest.class);
        nsInstantiationData.setNsInstanceId(instanceId);
        createUnderlayRequest.setData(JsonUtil.toJson(nsInstantiationData));

        execTestCase(createUnderlayRequest, new FailedChecker());

        HttpRquestResponse httpDeleteUnderlayObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_UNDERLAY_FAILED_TESTCASE);
        HttpRequest deleteUnderlayRequest = httpDeleteUnderlayObject.getRequest();
        deleteUnderlayRequest.setUri(PathReplace.replaceUuid("instanceid", deleteUnderlayRequest.getUri(), instanceId));

        execTestCase(deleteUnderlayRequest, new FailedChecker());

        testOperation(DELETE_NSLCM_SUCCESS_TESTCASE, instanceId);
    }

    private String testCreateNslcm(String createNslcmPath) throws ServiceException {
        HttpRquestResponse httpCreateObject = HttpModelUtils.praseHttpRquestResponseFromFile(createNslcmPath);
        HttpRequest createRequest = httpCreateObject.getRequest();
        HttpResponse createResponse = execTestCase(createRequest, new SuccessChecker());
        NsCreationResponse nsCreationResponse = JsonUtil.fromJson(createResponse.getData(), NsCreationResponse.class);
        return nsCreationResponse.getNsInstanceId();
    }

    private void testOperation(String queryPath, String instanceId) throws ServiceException {
        HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponseFromFile(queryPath);
        HttpRequest hrttpRequest = httpObject.getRequest();

        hrttpRequest.setUri(PathReplace.replaceUuid("instanceid", hrttpRequest.getUri(), instanceId));
        execTestCase(hrttpRequest, new SuccessChecker());
    }

    private class SuccessChecker implements IChecker {

        @Override
        public boolean check(HttpResponse response) {
            if(HttpCode.isSucess(response.getStatus())) {
                return true;
            }
            return false;
        }

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
