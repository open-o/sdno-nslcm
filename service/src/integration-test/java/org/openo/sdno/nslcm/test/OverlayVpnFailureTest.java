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

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.nslcm.mocoserver.catalog.CatalogMocoServer;
import org.openo.sdno.nslcm.model.nbi.NsCreationResponse;
import org.openo.sdno.nslcm.model.nbi.NsInstantiationRequest;
import org.openo.sdno.nslcm.msbmanager.MsbRegisterManager;
import org.openo.sdno.nslcm.responsechecker.StatusChecker;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.http.model.HttpResponse;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.replace.PathReplace;
import org.openo.sdno.testframework.testmanager.TestManager;
import org.openo.sdno.testframework.util.file.JsonUtil;

public class OverlayVpnFailureTest extends TestManager {

    private static final String CREATE_OVERLAY_INSTANCE_TESTCASE =
            "src/integration-test/resources/testcase/createsite2dcnsinstance.json";

    private static final String DELETE_OVERLAY_INSTANCE_TESTCASE =
            "src/integration-test/resources/testcase/deleteoverlaynsinstance.json";

    private static final String OVERLAY_PARAM_ERROR_TESTCASE =
            "src/integration-test/resources/testcase/instantiateoverlayparamerror.json";

    private static final String TERMINATE_UNEXIST_NSINSTANCE_TESTCASE =
            "src/integration-test/resources/testcase/terminateunexistnsinstance.json";

    private CatalogMocoServer catalogMocoServer = new CatalogMocoServer();

    @Before
    public void setup() throws ServiceException {
        catalogMocoServer.start();
        MsbRegisterManager.registerOverlayMsb();
    }

    @After
    public void tearDown() throws ServiceException {
        MsbRegisterManager.unRegisterOverlayMsb();
        catalogMocoServer.stop();
    }

    @Test
    public void overlayParamErrorTest() throws ServiceException {

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
                HttpModelUtils.praseHttpRquestResponseFromFile(OVERLAY_PARAM_ERROR_TESTCASE);
        HttpRequest httpInstantiateNsInstanceRequest = httpInstantiateNsInstanceObject.getRequest();
        httpInstantiateNsInstanceRequest
                .setUri(PathReplace.replaceUuid("instanceId", httpInstantiateNsInstanceRequest.getUri(), nsInstanceId));
        NsInstantiationRequest nsInstantiationRequest =
                JsonUtil.fromJson(httpInstantiateNsInstanceRequest.getData(), NsInstantiationRequest.class);
        nsInstantiationRequest.setNsInstanceId(nsInstanceId);
        httpInstantiateNsInstanceRequest.setData(JsonUtil.toJson(nsInstantiationRequest));
        execTestCase(httpInstantiateNsInstanceRequest,
                new StatusChecker(httpInstantiateNsInstanceObject.getResponse()));

        // Delete NsInstance
        HttpRquestResponse httpDeleteNsInstanceObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_OVERLAY_INSTANCE_TESTCASE);
        HttpRequest httpDeleteNsInstanceRequest = httpDeleteNsInstanceObject.getRequest();
        httpDeleteNsInstanceRequest
                .setUri(PathReplace.replaceUuid("instanceId", httpDeleteNsInstanceRequest.getUri(), nsInstanceId));
        execTestCase(httpDeleteNsInstanceObject.getRequest(),
                new StatusChecker(httpDeleteNsInstanceObject.getResponse()));
    }
}
