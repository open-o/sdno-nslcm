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

package org.openo.sdno.nslcm.model.nbi;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * SDN-O NS Package MAnagement Operation (Onboard/Delete) Response.<br>
 * 
 * @author
 * @version SDNO 0.5 Sep 8, 2016
 */
@ApiModel(description = "SDN-O NS Package MAnagement Operation (Onboard/Delete) Response")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-08-31T18:01:03.332+08:00")
public class PackageManagementResponse {

    private String status = null;

    private String errorCode = null;

    private String statusDescription = null;

    @ApiModelProperty(required = true, value = "the status of SDN-O NS package management request")
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @ApiModelProperty(value = "the code of error (if happened)")
    @JsonProperty("errorCode")
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @ApiModelProperty(value = "description of operation status")
    @JsonProperty("statusDescription")
    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }
}
