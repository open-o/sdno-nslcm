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
 * Inventory service model.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 Oct 15, 2016
 */
public class InvServiceModel {

    /**
     * Service instance ID.
     */
    private String serviceId;

    /**
     * Service instance name.
     */
    private String name;

    /**
     * Service type.
     */
    private String serviceType;

    /**
     * Service instance description.
     */
    private String description;

    /**
     * Active status of service instance. For example active, deactive.
     */
    private String activeStatus;

    /**
     * Creating status of service instance. For example createdSucceed, createdFail.
     */
    private String status;

    /**
     * Who created service instance.
     */
    private String creator;

    /**
     * Time that create service instance.
     */
    private Long createAt;


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
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Returns the activeStatus.
     */
    public String getActiveStatus() {
        return activeStatus;
    }

    /**
     * @param activeStatus The activeStatus to set.
     */
    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    /**
     * @return Returns the status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return Returns the creator.
     */
    public String getCreator() {
        return creator;
    }

    /**
     * @param creator The creator to set.
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * @return Returns the createAt.
     */
    public Long getCreateAt() {
        return createAt;
    }

    /**
     * @param createAt The createAt to set.
     */
    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

    /**
     * @return Returns the serviceType.
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * @param serviceType The serviceType to set.
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
