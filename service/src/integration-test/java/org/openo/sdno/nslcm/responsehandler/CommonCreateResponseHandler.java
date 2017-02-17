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

package org.openo.sdno.nslcm.responsehandler;

import org.openo.sdno.overlayvpn.consts.HttpCode;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.http.model.HttpResponse;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.moco.responsehandler.MocoResponseHandler;

public class CommonCreateResponseHandler extends MocoResponseHandler {

    @Override
    public void processRequestandResponse(HttpRquestResponse httpObject) {
        HttpRequest httpRequest = httpObject.getRequest();
        HttpResponse httpResponse = httpObject.getResponse();
        httpResponse.setStatus(HttpCode.CREATE_OK);
        httpResponse.setData(httpRequest.getData());
    }
}
