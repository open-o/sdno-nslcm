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

package org.openo.sdno.nslcm.model;

import org.openo.sdno.overlayvpn.model.servicemodel.Vpc;

/**
 * Business model of VoLte OverlayVpn.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-2-7
 */
public class VoLteBusinessModel extends BusinessModel {

    private Vpc coreVpcModel;

    private Vpc edgeVpc1Model;

    private Vpc edgeVpc2Model;

    public Vpc getCoreVpcModel() {
        return coreVpcModel;
    }

    public void setCoreVpcModel(Vpc coreVpcModel) {
        this.coreVpcModel = coreVpcModel;
    }

    public Vpc getEdgeVpc1Model() {
        return edgeVpc1Model;
    }

    public void setEdgeVpc1Model(Vpc edgeVpc1Model) {
        this.edgeVpc1Model = edgeVpc1Model;
    }

    public Vpc getEdgeVpc2Model() {
        return edgeVpc2Model;
    }

    public void setEdgeVpc2Model(Vpc edgeVpc2Model) {
        this.edgeVpc2Model = edgeVpc2Model;
    }
}
