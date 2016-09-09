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

package org.openo.sdno.nslcm.model.db;

import org.openo.sdno.overlayvpn.inventory.sdk.model.annotation.MOResType;
import org.openo.sdno.overlayvpn.model.common.enums.ActionStatus;
import org.openo.sdno.overlayvpn.model.uuid.AbstUuidModel;

/**
 * Class of NsInstantiationInfo Model Data. <br>
 * <p>
 * It is used to recode the NsInstantiationInfo data that passed by caller.
 * </p>
 * 
 * @author
 * @version SDNO 0.5 Aug 30, 2016
 */
@MOResType(infoModelName = "nsinstantiationinfo")
public class NsInstantiationInfo extends AbstUuidModel {

    private String instanceId;

    private String name;

    private String value;

    private String actionState = ActionStatus.NORMAL.getName();

    public NsInstantiationInfo() {
        // construct a empty object
    }

    /**
     * Constructor<br>
     * >
     * 
     * @param instanceId The instance ID
     * @param name the name
     * @param value The value
     * @param actionState The action state
     * @since SDNO 0.5
     */
    public NsInstantiationInfo(String instanceId, String name, String value, String actionState) {
        this.instanceId = instanceId;
        this.name = name;
        this.value = value;
        this.actionState = actionState;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getActionState() {
        return actionState;
    }

    public void setActionState(String actionState) {
        this.actionState = actionState;
    }
}
