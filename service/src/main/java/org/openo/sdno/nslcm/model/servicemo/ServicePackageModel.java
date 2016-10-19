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

package org.openo.sdno.nslcm.model.servicemo;

/**
 * Mapping relation Class.<br>
 * <p>
 * Relation between service instance and service package.
 * </p>
 * 
 * @author
 * @version SDNO 0.5 Oct 15, 2016
 */
public class ServicePackageModel {

    /**
     * Service instance ID.
     */
    private String serviceId;

    /**
     * Service package ID.
     */
    private String serviceDefId;

    /**
     * Service definition template ID.
     */
    private String templateId;

    /**
     * Service definition template name.
     */
    private String templateName;

    /**
     * @return Returns the serviceId.
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * @param serviceId The serviceId to set.
     */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * @return Returns the serviceDefId.
     */
    public String getServiceDefId() {
        return serviceDefId;
    }

    /**
     * @param serviceDefId The serviceDefId to set.
     */
    public void setServiceDefId(String serviceDefId) {
        this.serviceDefId = serviceDefId;
    }

    /**
     * @return Returns the templateId.
     */
    public String getTemplateId() {
        return templateId;
    }

    /**
     * @param templateId The templateId to set.
     */
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    /**
     * @return Returns the templateName.
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * @param templateName The templateName to set.
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

}
