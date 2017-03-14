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

package org.openo.sdno.nslcm.rest;

import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.nslcm.mock.configreader.MockOsDriverParamConfigReader;
import org.openo.sdno.nslcm.mock.configreader.MockSiteParamConfigReader;
import org.openo.sdno.nslcm.mock.dao.MockServiceModelDaoImpl;
import org.openo.sdno.nslcm.mock.dao.MockServicePackageDaoImpl;
import org.openo.sdno.nslcm.mock.dao.MockServiceParametersDaoImpl;
import org.openo.sdno.nslcm.mock.invdao.MockInventoryDao;
import org.openo.sdno.nslcm.mock.invdao.MockLtpInvDao;
import org.openo.sdno.nslcm.mock.invdao.MockNetworkElementInvDao;
import org.openo.sdno.nslcm.mock.invdao.MockSiteInvDao;
import org.openo.sdno.nslcm.mock.invdao.MockVimInvDao;
import org.openo.sdno.nslcm.mock.restfulproxy.MockRestfulProxy;
import org.openo.sdno.nslcm.model.db.NsResponseInfo;
import org.openo.sdno.nslcm.model.nbi.JobQueryResponse;
import org.openo.sdno.nslcm.model.nbi.LongOperationResponse;
import org.openo.sdno.nslcm.model.nbi.NsCreationRequest;
import org.openo.sdno.nslcm.model.nbi.NsCreationResponse;
import org.openo.sdno.nslcm.model.nbi.NsInstanceQueryResponse;
import org.openo.sdno.nslcm.model.nbi.NsInstantiationRequest;
import org.openo.sdno.nslcm.model.nbi.NsTerminationRequest;
import org.openo.sdno.nslcm.model.nbi.PackageManagementResponse;
import org.openo.sdno.nslcm.model.nbi.PackageOnboardRequest;
import org.openo.sdno.nslcm.springtest.SpringTest;
import org.openo.sdno.overlayvpn.consts.HttpCode;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;

public class NslcmSvcRoaResourceTest extends SpringTest {

    @Autowired
    private NslcmSvcRoaResource nslcmSvcRoaResource;

    @Mocked
    private HttpServletRequest httpRequest;

    @Mocked
    private HttpServletResponse httpResponse;

    @Before
    public void setUp() {
        new MockServicePackageDaoImpl();
        new MockRestfulProxy();
        new MockServiceModelDaoImpl();
        new MockServiceParametersDaoImpl();
        new MockSiteInvDao();
        new MockSiteParamConfigReader();
        new MockOsDriverParamConfigReader();
        new MockNetworkElementInvDao();
        new MockVimInvDao();
        new MockInventoryDao<NsResponseInfo>();
        new MockLtpInvDao();
    }

    @Test
    public void nsCreationPostTest() throws ServiceException {

        NsCreationRequest nsCreationRequest = new NsCreationRequest();
        nsCreationRequest.setDescription("descriptiontest");
        nsCreationRequest.setNsdId("nsdIdtest");
        nsCreationRequest.setNsName("nsNametest");

        NsCreationResponse response = nslcmSvcRoaResource.nsCreationPost(httpRequest, httpResponse, nsCreationRequest);
        assertTrue(StringUtils.isNotEmpty(response.getNsInstanceId()));
    }

    @Test
    public void nsDeletionDeleteTest() throws ServiceException {
        nslcmSvcRoaResource.nsDeletionDelete(httpRequest, httpResponse, "nsInstance");
    }

    @Test
    public void jobQueryGetTest() throws ServiceException {
        JobQueryResponse queryResponse = nslcmSvcRoaResource.jobQueryGet(httpRequest, httpResponse, "jobId");
        assertTrue("jobId".equals(queryResponse.getJobId()));
    }

    @Test
    public void nsQueryGetTest() throws ServiceException {
        NsInstanceQueryResponse queryResponse = nslcmSvcRoaResource.nsQueryGet(httpRequest, httpResponse, "nsInstance");
        assertTrue("nsdIdtest".equals(queryResponse.getNsdId()));
    }

    @Test
    public void packageDeletionDeleteTest() throws ServiceException {
        PackageManagementResponse response =
                nslcmSvcRoaResource.packageDeletionDelete(httpRequest, httpResponse, "csarId");
        assertTrue(ErrorCode.OVERLAYVPN_SUCCESS.equals(response.getErrorCode()));
        assertTrue(String.valueOf(HttpCode.RESPOND_OK).equals(response.getStatus()));
    }

    @Test
    public void packageOnboardingPostTest() throws ServiceException {
        PackageOnboardRequest ssRequest = new PackageOnboardRequest();
        PackageManagementResponse response =
                nslcmSvcRoaResource.packageOnboardingPost(httpRequest, httpResponse, ssRequest);
        assertTrue(ErrorCode.OVERLAYVPN_SUCCESS.equals(response.getErrorCode()));
        assertTrue(String.valueOf(HttpCode.RESPOND_OK).equals(response.getStatus()));
    }

    @Test
    public void nsInstantiationPostTest() throws ServiceException {
        NsInstantiationRequest instantiationRequest =
                JsonUtil.fromJson(new File("src/test/resources/overlaytemplate.json"), NsInstantiationRequest.class);
        LongOperationResponse response =
                nslcmSvcRoaResource.nsInstantiationPost(httpRequest, httpResponse, "nsInstance", instantiationRequest);
        assertTrue("nsInstance".equals(response.getJobId()));
    }

    @Test
    public void nsTerminationPostTest() throws ServiceException {

        NsTerminationRequest nsTerminationRequest = new NsTerminationRequest();

        new MockUp<NsResponseInfo>() {

            @Mock
            public String getUuid() {
                return "nsInstance";
            }

            @Mock
            public String getExternalId() {
                return "nsInstance";
            }
        };

        LongOperationResponse response =
                nslcmSvcRoaResource.nsTerminationPost(httpRequest, httpResponse, "nsInstance", nsTerminationRequest);
        assertTrue("nsInstance".equals(response.getJobId()));
    }

}
