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

package org.openo.sdno.nslcm.test;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.nslcm.mocoserver.catalog.CatalogMocoServer;
import org.openo.sdno.nslcm.mocoserver.overlayvpn.LocalSiteMocoServer;
import org.openo.sdno.nslcm.mocoserver.overlayvpn.OverlayVpnMocoServer;
import org.openo.sdno.nslcm.mocoserver.overlayvpn.ServiceChainMocoServer;
import org.openo.sdno.nslcm.mocoserver.overlayvpn.VpcMocoServer;
import org.openo.sdno.nslcm.model.nbi.LongOperationResponse;
import org.openo.sdno.nslcm.model.nbi.NsCreationResponse;
import org.openo.sdno.nslcm.model.nbi.NsInstanceQueryResponse;
import org.openo.sdno.nslcm.model.nbi.NsInstantiationRequest;
import org.openo.sdno.nslcm.msbmanager.MsbRegisterManager;
import org.openo.sdno.nslcm.responsechecker.StatusChecker;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.http.model.HttpResponse;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.replace.PathReplace;
import org.openo.sdno.testframework.testmanager.TestManager;
import org.openo.sdno.testframework.topology.Topology;
import org.openo.sdno.testframework.util.file.JsonUtil;

public class VoLTEOverlayVpnSuccessTest extends TestManager {

    private static final String CREATE_OVERLAY_INSTANCE_TESTCASE =
            "src/integration-test/resources/testcase/createvoltensinstance.json";

    private static final String DELETE_OVERLAY_INSTANCE_TESTCASE =
            "src/integration-test/resources/testcase/deleteoverlaynsinstance.json";

    private static final String INIT_VOLTE_NS_TESTCASE = "src/integration-test/resources/testcase/initvoltens.json";

    private static final String TERMINATE_OVERLAY_INSTANCE_TESTCASE =
            "src/integration-test/resources/testcase/terminateoverlaynsinstance.json";

    private static final String QUERY_INSTANCE_TESTCASE =
            "src/integration-test/resources/testcase/querynsinstance.json";

    private static final String CREATE_OSCONTROLLER_TESTCASE =
            "src/integration-test/resources/testcase/createoscontroller.json";

    private static final String DELETE_OSCONTROLLER_TESTCASE =
            "src/integration-test/resources/testcase/deleteoscontroller.json";

    private Topology topoManager = new Topology("src/integration-test/resources/topodata/overlay");

    private CatalogMocoServer catalogMocoServer = new CatalogMocoServer();

    private OverlayVpnMocoServer overlayVpnMocoServer = new OverlayVpnMocoServer();

    private ServiceChainMocoServer serviceChainMocoServer = new ServiceChainMocoServer();

    private VpcMocoServer vpcMocoServer = new VpcMocoServer();

    private LocalSiteMocoServer localsiteMocoServer = new LocalSiteMocoServer();

    @Before
    public void setup() throws ServiceException {
        catalogMocoServer.start();
        overlayVpnMocoServer.start();
        serviceChainMocoServer.start();
        vpcMocoServer.start();
        localsiteMocoServer.start();
        MsbRegisterManager.registerOverlayMsb();
        topoManager.createInvTopology();
    }

    @After
    public void tearDown() throws ServiceException {
        MsbRegisterManager.unRegisterOverlayMsb();
        catalogMocoServer.stop();
        overlayVpnMocoServer.stop();
        serviceChainMocoServer.stop();
        vpcMocoServer.stop();
        localsiteMocoServer.stop();
        topoManager.clearInvTopology();
    }

    @Test
    public void overlayVpnSuccessTest() throws ServiceException {

        // Create OsController
        HttpRquestResponse httpCreateOsControllerObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_OSCONTROLLER_TESTCASE);
        HttpResponse osControllerResopnse = execTestCase(httpCreateOsControllerObject.getRequest(),
                new StatusChecker(httpCreateOsControllerObject.getResponse()));
        Map<String, String> controllerResultMap =
                JsonUtil.fromJson(osControllerResopnse.getData(), new TypeReference<Map<String, String>>() {});
        assertTrue(controllerResultMap.containsKey("vimId"));

        // Create NsInstance
        HttpRquestResponse httpCreateNsInstanceObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_OVERLAY_INSTANCE_TESTCASE);
        HttpResponse createInstanceResponse = execTestCase(httpCreateNsInstanceObject.getRequest(),
                new StatusChecker(httpCreateNsInstanceObject.getResponse()));
        NsCreationResponse nsCreationResponse =
                JsonUtil.fromJson(createInstanceResponse.getData(), NsCreationResponse.class);
        String nsInstanceId = nsCreationResponse.getNsInstanceId();
        assertTrue(StringUtils.isNotEmpty(nsInstanceId));

        // Instantiate NsInstance
        HttpRquestResponse httpInstantiateNsInstanceObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(INIT_VOLTE_NS_TESTCASE);
        HttpRequest httpInstantiateNsInstanceRequest = httpInstantiateNsInstanceObject.getRequest();
        httpInstantiateNsInstanceRequest
                .setUri(PathReplace.replaceUuid("instanceId", httpInstantiateNsInstanceRequest.getUri(), nsInstanceId));
        NsInstantiationRequest nsInstantiationRequest =
                JsonUtil.fromJson(httpInstantiateNsInstanceRequest.getData(), NsInstantiationRequest.class);
        nsInstantiationRequest.setNsInstanceId(nsInstanceId);
        httpInstantiateNsInstanceRequest.setData(JsonUtil.toJson(nsInstantiationRequest));
        HttpResponse instantiateNsInstanceResponse = execTestCase(httpInstantiateNsInstanceRequest,
                new StatusChecker(httpInstantiateNsInstanceObject.getResponse()));
        LongOperationResponse instantiateResponse =
                JsonUtil.fromJson(instantiateNsInstanceResponse.getData(), LongOperationResponse.class);
        assertTrue(StringUtils.isNotEmpty(instantiateResponse.getJobId()));
        assertTrue(nsInstanceId.equals(instantiateResponse.getJobId()));

        // Query NsInstance
        HttpRquestResponse httpQueryNsInstanceObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(QUERY_INSTANCE_TESTCASE);
        HttpRequest httpQueryNsInstanceRequest = httpQueryNsInstanceObject.getRequest();
        httpQueryNsInstanceRequest
                .setUri(PathReplace.replaceUuid("instanceId", httpQueryNsInstanceRequest.getUri(), nsInstanceId));
        HttpResponse queryNsInstanceResponse =
                execTestCase(httpQueryNsInstanceRequest, new StatusChecker(httpQueryNsInstanceObject.getResponse()));
        NsInstanceQueryResponse queryResponse =
                JsonUtil.fromJson(queryNsInstanceResponse.getData(), NsInstanceQueryResponse.class);
        assertTrue(nsInstanceId.equals(queryResponse.getId()));

        // Terminate NsInstance
        HttpRquestResponse httpTerminateNsInstanceObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(TERMINATE_OVERLAY_INSTANCE_TESTCASE);
        HttpRequest httpTerminateNsInstanceRequest = httpTerminateNsInstanceObject.getRequest();
        httpTerminateNsInstanceRequest
                .setUri(PathReplace.replaceUuid("instanceId", httpTerminateNsInstanceRequest.getUri(), nsInstanceId));
        HttpResponse terminateNsInstanceResponse = execTestCase(httpTerminateNsInstanceRequest,
                new StatusChecker(httpTerminateNsInstanceObject.getResponse()));
        LongOperationResponse terminateResponse =
                JsonUtil.fromJson(terminateNsInstanceResponse.getData(), LongOperationResponse.class);
        StringUtils.isNotEmpty(terminateResponse.getJobId());

        // Delete NsInstance
        HttpRquestResponse httpDeleteNsInstanceObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_OVERLAY_INSTANCE_TESTCASE);
        HttpRequest httpDeleteNsInstanceRequest = httpDeleteNsInstanceObject.getRequest();
        httpDeleteNsInstanceRequest
                .setUri(PathReplace.replaceUuid("instanceId", httpDeleteNsInstanceRequest.getUri(), nsInstanceId));
        execTestCase(httpDeleteNsInstanceObject.getRequest(),
                new StatusChecker(httpDeleteNsInstanceObject.getResponse()));

        // Delete OsController
        HttpRquestResponse httpDeleteOsControllerObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_OSCONTROLLER_TESTCASE);
        HttpRequest httpDeleteOsControllerRequest = httpDeleteOsControllerObject.getRequest();
        httpDeleteOsControllerRequest.setUri(PathReplace.replaceUuid("objectId", httpDeleteOsControllerRequest.getUri(),
                controllerResultMap.get("vimId")));
        execTestCase(httpDeleteOsControllerRequest, new StatusChecker(httpDeleteOsControllerObject.getResponse()));
    }

}
