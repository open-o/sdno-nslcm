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

package org.openo.sdno.nslcm.msbmanager;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.exception.HttpCode;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.testframework.restclient.HttpRestClient;
import org.openo.sdno.testframework.util.file.FileUtils;

public class MsbRegisterManager {

    private static final String OVERLAY_MSB_REGISTER_FILE =
            "src/integration-test/resources/msb/registeroverlaymsb.json";

    private static final String OVERLAY_MSB_UNREGISTER_FILE =
            "src/integration-test/resources/msb/unregisteroverlaymsb.json";

    private static final String UNDERLAY_MSB_REGISTER_FILE =
            "src/integration-test/resources/msb/registerunderlaymsb.json";

    private static final String UNDERLAY_MSB_UNREGISTER_FILE =
            "src/integration-test/resources/msb/unregisterunderlaymsb.json";

    private static final String MSB_REGISTER_URL = "/openoapi/microservices/v1/services";

    private static final String MSB_UNREGISTER_URL =
            "/openoapi/microservices/v1/services/{0}/version/{1}/nodes/{2}/{3}";

    private static final HttpRestClient restClient = new HttpRestClient();

    public static void registerOverlayMsb() throws ServiceException {
        registerServiceMsb(OVERLAY_MSB_REGISTER_FILE);
    }

    public static void unRegisterOverlayMsb() throws ServiceException {
        unRegisterServiceMsb(OVERLAY_MSB_UNREGISTER_FILE);
    }

    public static void registerUnderlayMsb() throws ServiceException {
        registerServiceMsb(UNDERLAY_MSB_REGISTER_FILE);
    }

    public static void unRegisterUnderlayMsb() throws ServiceException {
        unRegisterServiceMsb(UNDERLAY_MSB_UNREGISTER_FILE);
    }

    private static void registerServiceMsb(String registerFile) throws ServiceException {

        String msbContent = FileUtils.readFromJson(new File(registerFile));
        List<Map<String, Object>> msbList =
                JsonUtil.fromJson(msbContent, new TypeReference<List<Map<String, Object>>>() {});

        RestfulParametes restfulParametes = new RestfulParametes();
        restfulParametes.putHttpContextHeader("Content-Type", "application/json");

        for(Map<String, Object> curMsb : msbList) {
            String curMsbBody = JsonUtil.toJson(curMsb);
            restfulParametes.setRawData(curMsbBody);
            RestfulResponse response = restClient.post(MSB_REGISTER_URL, restfulParametes);
            if(!HttpCode.isSucess(response.getStatus())) {
                throw new ServiceException("Register Msb failed");
            }
        }

    }

    private static void unRegisterServiceMsb(String unRegisterFile) throws ServiceException {

        String msbContent = FileUtils.readFromJson(new File(unRegisterFile));
        List<Map<String, String>> unRegisterMsbList =
                JsonUtil.fromJson(msbContent, new TypeReference<List<Map<String, String>>>() {});
        RestfulParametes restfulParametes = new RestfulParametes();
        restfulParametes.putHttpContextHeader("Content-Type", "application/json");

        for(Map<String, String> curMsb : unRegisterMsbList) {
            String serviceName = curMsb.get("serviceName");
            String version = curMsb.get("version");
            String ip = curMsb.get("ip");
            String port = curMsb.get("port");

            String unRegisterUrl = MessageFormat.format(MSB_UNREGISTER_URL, serviceName, version, ip, port);
            RestfulResponse response = restClient.delete(unRegisterUrl, restfulParametes);
            if(!HttpCode.isSucess(response.getStatus())) {
                throw new ServiceException("UnRegister Msb failed");
            }
        }

    }
}
