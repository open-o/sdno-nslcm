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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.overlayvpn.brs.invdao.LogicalTernminationPointInvDao;
import org.openo.sdno.overlayvpn.brs.model.LogicalTernminationPointMO;

import mockit.Mock;
import mockit.MockUp;

public class MockLtpInvDao extends MockUp<LogicalTernminationPointInvDao> {

    @Mock
    public List<LogicalTernminationPointMO> query(List<String> ids) throws ServiceException {

        List<LogicalTernminationPointMO> ltpMOList = new ArrayList<LogicalTernminationPointMO>();
        for(String curId : ids) {
            LogicalTernminationPointMO ltpMO = new LogicalTernminationPointMO();
            ltpMO.setName("ltpName");
            ltpMO.setId(curId);
            ltpMOList.add(ltpMO);
        }

        return ltpMOList;
    }

    @Mock
    public List<LogicalTernminationPointMO> query(Map<String, String> condition) throws ServiceException {
        List<LogicalTernminationPointMO> ltpMOList = new ArrayList<LogicalTernminationPointMO>();
        LogicalTernminationPointMO ltpMO = new LogicalTernminationPointMO();
        ltpMO.setName("ltpName");
        ltpMO.setId("testId");
        ltpMOList.add(ltpMO);

        return ltpMOList;
    }

}
