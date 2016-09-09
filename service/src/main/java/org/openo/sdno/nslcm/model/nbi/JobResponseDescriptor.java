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

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Details of a job status")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-08-31T18:01:03.332+08:00")

/**
 * Details of a job status. <br>
 * 
 * @author
 * @version SDNO 0.5 Aug 31, 2016
 */
public class JobResponseDescriptor {

    private String status = null;

    private String statusDescription = null;

    private String progress = null;

    private String errorCode = null;

    private String responseId = null;

    public JobResponseDescriptor status(String status) {
        this.status = status;
        return this;
    }

    @ApiModelProperty(required = true, value = "status of the job")
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public JobResponseDescriptor statusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
        return this;
    }

    @ApiModelProperty(value = "description of the job status")
    @JsonProperty("statusDescription")
    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public JobResponseDescriptor progress(String progress) {
        this.progress = progress;
        return this;
    }

    @ApiModelProperty(value = "progress of the job")
    @JsonProperty("progress")
    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public JobResponseDescriptor errorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    @ApiModelProperty(value = "status of the job")
    @JsonProperty("errorCode")
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public JobResponseDescriptor responseId(String responseId) {
        this.responseId = responseId;
        return this;
    }

    @ApiModelProperty(value = "Id of the response.")
    @JsonProperty("responseId")
    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        JobResponseDescriptor jobResponseDescriptor = (JobResponseDescriptor)o;
        return Objects.equals(status, jobResponseDescriptor.status)
                && Objects.equals(statusDescription, jobResponseDescriptor.statusDescription)
                && Objects.equals(progress, jobResponseDescriptor.progress)
                && Objects.equals(errorCode, jobResponseDescriptor.errorCode)
                && Objects.equals(responseId, jobResponseDescriptor.responseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, statusDescription, progress, errorCode, responseId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class JobResponseDescriptor {\n");

        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    statusDescription: ").append(toIndentedString(statusDescription)).append("\n");
        sb.append("    progress: ").append(toIndentedString(progress)).append("\n");
        sb.append("    errorCode: ").append(toIndentedString(errorCode)).append("\n");
        sb.append("    responseId: ").append(toIndentedString(responseId)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if(o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
