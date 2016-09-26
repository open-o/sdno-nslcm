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

package org.openo.sdno.overlayvpn;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.exception.HttpCode;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.nslcm.model.nbi.NsCreationResponse;
import org.openo.sdno.nslcm.model.nbi.NsInstantiationRequest;
import org.openo.sdno.overlayvpn.mocoserver.ACBranchMocoServer;
import org.openo.sdno.overlayvpn.mocoserver.MocoOverlayDriverE2ESuccessServer;
import org.openo.sdno.testframework.checker.IChecker;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.http.model.HttpResponse;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.replace.PathReplace;
import org.openo.sdno.testframework.testmanager.TestManager;
import org.openo.sdno.testframework.topology.Topology;

public class ITOverlayVpnDriver extends TestManager {

    private static final String CREATE_NSLCM_SUCCESS_TESTCASE =
            "src/integration-test/resources/testcase/createnslcmsuccess.json";

    private static final String CREATE_OVERLAY_SUCCESS_TESTCASE =
            "src/integration-test/resources/testcase/createoverlaysuccess.json";

    private static final String QUERY_NSLCM_SUCCESS_TESTCASE =
            "src/integration-test/resources/testcase/querynslcmsuccess.json";

    private static final String DELETE_OVERLAY_SUCCESS_TESTCASE =
            "src/integration-test/resources/testcase/deleteoverlaysuccess.json";

    private static final String DELETE_NSLCM_SUCCESS_TESTCASE =
            "src/integration-test/resources/testcase/deletenslcmsuccess.json";

    private static final String TOPODATA_PATH = "src/integration-test/resources/AcBranchInventory";

    private MocoOverlayDriverE2ESuccessServer sbiAdapterServer = new MocoOverlayDriverE2ESuccessServer();

    private ACBranchMocoServer acBranchServer = new ACBranchMocoServer(12307);

    private Topology topo = new Topology(TOPODATA_PATH);

    @Before
    public void setup() throws ServiceException {
        topo.createInvTopology();
        sbiAdapterServer.start();
        acBranchServer.start();
    }

    @After
    public void tearDown() throws ServiceException {
        sbiAdapterServer.stop();
        acBranchServer.stop();
        topo.clearInvTopology();
    }

    @Test
    public void testOverlaySuccess() throws ServiceException, InterruptedException {
        String instanceId = testCreateNslcm(CREATE_NSLCM_SUCCESS_TESTCASE);

        testCreateOverlay(CREATE_OVERLAY_SUCCESS_TESTCASE, instanceId);

        testOperation(QUERY_NSLCM_SUCCESS_TESTCASE, instanceId);

        testOperation(DELETE_OVERLAY_SUCCESS_TESTCASE, instanceId);

        testOperation(DELETE_NSLCM_SUCCESS_TESTCASE, instanceId);
    }

    private String testCreateNslcm(String createNslcmPath) throws ServiceException {
        HttpRquestResponse httpCreateObject = HttpModelUtils.praseHttpRquestResponseFromFile(createNslcmPath);
        HttpRequest createRequest = httpCreateObject.getRequest();
        HttpResponse createResponse = execTestCase(createRequest, new SuccessChecker());
        NsCreationResponse nsCreationResponse = JsonUtil.fromJson(createResponse.getData(), NsCreationResponse.class);
        return nsCreationResponse.getNsInstanceId();
    }

    private void testCreateOverlay(String createUnderlayPath, String instanceId) throws ServiceException {
        HttpRquestResponse httpCreateOverlayObject = HttpModelUtils.praseHttpRquestResponseFromFile(createUnderlayPath);
        HttpRequest createOverlayRequest = httpCreateOverlayObject.getRequest();
        createOverlayRequest.setUri(PathReplace.replaceUuid("instanceid", createOverlayRequest.getUri(), instanceId));

        NsInstantiationRequest nsInstantiationData =
                JsonUtil.fromJson(createOverlayRequest.getData(), NsInstantiationRequest.class);
        nsInstantiationData.setNsInstanceId(instanceId);
        createOverlayRequest.setData(JsonUtil.toJson(nsInstantiationData));

        execTestCase(createOverlayRequest, new SuccessChecker());
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

}
