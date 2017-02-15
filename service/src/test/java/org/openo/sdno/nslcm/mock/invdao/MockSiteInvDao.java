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

package org.openo.sdno.nslcm.mock.invdao;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.overlayvpn.brs.invdao.SiteInvDao;
import org.openo.sdno.overlayvpn.brs.model.SiteMO;

import mockit.Mock;
import mockit.MockUp;

public class MockSiteInvDao extends MockUp<SiteInvDao> {

    private SiteMO siteMO = new SiteMO();

    public MockSiteInvDao() {
        siteMO.setId("SiteId");
        siteMO.setName("Site1");
        siteMO.setTenantID("TenantId");
    }

    @Mock
    public SiteMO query(String id) throws ServiceException {
        return siteMO;
    }

    @Mock
    public void deleteMO(String uuid) throws ServiceException {
        return;
    }

    @Mock
    public void updateMO(SiteMO curMO) throws ServiceException {
        return;
    }

    @Mock
    public List<SiteMO> query(Map<String, String> condition) throws ServiceException {
        return Arrays.asList(siteMO);
    }

}
