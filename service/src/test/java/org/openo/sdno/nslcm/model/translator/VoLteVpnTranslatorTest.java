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

package org.openo.sdno.nslcm.model.translator;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.nslcm.model.VoLteBusinessModel;
import org.openo.sdno.overlayvpn.esr.invdao.VimInvDao;
import org.openo.sdno.overlayvpn.esr.model.Vim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import mockit.Mock;
import mockit.MockUp;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring/applicationContext.xml",
                "classpath*:META-INF/spring/service.xml", "classpath*:spring/service.xml"})
public class VoLteVpnTranslatorTest {

    @Autowired
    VoLteVpnTranslator voLteVpnTranslator;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testCreateSuccess() throws ServiceException {
        new MockUp<VimInvDao>() {

            @Mock
            public Vim queryVimByName(String vimName) throws ServiceException {
                Vim vim = new Vim();
                vim.setVimId("vimId");

                return vim;
            }

        };

        Map<String, Object> templateParameter = new HashMap<>();
        templateParameter.put("vpnName", "testOverlay");
        templateParameter.put("vpnDescription", "testDescription");
        templateParameter.put("coreOpenStackName", "Sdno_Vim");
        templateParameter.put("coreVpcName", "default/project1");
        templateParameter.put("coreSubnets",
                "subnet1_1,10.40.0.0/24,23|subnet1_2,10.50.0.0/24,24|subnet1_3,10.60.0.0/24,25");
        templateParameter.put("edge1OpenStackName", "Sdno_Vim");
        templateParameter.put("edge1VpcName", "default/project2");
        templateParameter.put("edge1Subnets", "subnet2_1,10.20.0.0/24,26|subnet2_2,10.30.0.0/24,27");
        templateParameter.put("edge2OpenStackName", "Sdno_Vim");
        templateParameter.put("edge2VpcName", "default/project3");
        templateParameter.put("edge2Subnets", "subnet3_1,10.10.0.0/24,28");
        VoLteBusinessModel voLteBusinessModel =
                (VoLteBusinessModel)voLteVpnTranslator.translateVpnModel(templateParameter, "instanceId");

        assertTrue(3 == voLteBusinessModel.getCoreVpcModel().getSubNetList().size());
        assertTrue(2 == voLteBusinessModel.getEdgeVpc1Model().getSubNetList().size());
        assertTrue(1 == voLteBusinessModel.getEdgeVpc2Model().getSubNetList().size());

        assertTrue(6 == voLteBusinessModel.getVpnModel().getVpnGateways().size());
        assertTrue(11 == voLteBusinessModel.getVpnModel().getVpnConnections().size());
    }
}
