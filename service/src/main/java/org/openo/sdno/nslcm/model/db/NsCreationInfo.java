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
 * Class of NsCreationInfo Model Data.<br>
 * <p>
 * It is used to recode the NsCreationInfo data that passed by caller.
 * </p>
 * 
 * @author
 * @version SDNO 0.5 Sep 8, 2016
 */
@MOResType(infoModelName = "nscreationinfo")
public class NsCreationInfo extends AbstUuidModel {

    private String nsdId;

    private String nsName;

    private String templateName;

    private String description;

    private String parentId;

    private String actionState = ActionStatus.NORMAL.getName();

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     */
    public NsCreationInfo() {
        super();
    }

    /**
     * Constructor<br>
     * 
     * @param nsdId The NS creation ID
     * @param nsName The NS name
     * @param templateName The template name
     * @param description The description
     * @param actionState The action state
     * @since SDNO 0.5
     */
    public NsCreationInfo(String nsdId, String nsName, String templateName, String description, String actionState) {
        super();
        this.nsdId = nsdId;
        this.nsName = nsName;
        this.templateName = templateName;
        this.description = description;
        this.actionState = actionState;
    }

    public String getNsdId() {
        return nsdId;
    }

    public void setNsdId(String nsdId) {
        this.nsdId = nsdId;
    }

    public String getNsName() {
        return nsName;
    }

    public void setNsName(String nsName) {
        this.nsName = nsName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActionState() {
        return actionState;
    }

    public void setActionState(String actionState) {
        this.actionState = actionState;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
