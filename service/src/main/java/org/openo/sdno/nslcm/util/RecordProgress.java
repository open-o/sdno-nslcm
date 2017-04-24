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

package org.openo.sdno.nslcm.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * It is used to record progress so that report to caller.<br>
 * 
 * @author
 * @version SDNO 0.5 April 20, 2017
 */
public class RecordProgress {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecordProgress.class);

    private static volatile Map<String, Progress> progressList = new HashMap<>();

    public static Progress getJobProgress(String jobId) {
        return progressList.get(jobId);
    }

    public static void clearJobProgress(String jobId) {
        Progress progress = progressList.get(jobId);
        if(null == progress) {
            return;
        }

        progress.setTotal(1);
        progress.setCurrent(0);
        progress.setProgress("0");
        progress.setStatus("processing");
        progress.setStatusDescription("processing");
    }

    public static void addJobProgress(String jobId) {
        Progress progress = new Progress();
        progressList.put(jobId, progress);
        clearJobProgress(jobId);

        LOGGER.info("addJobProgress jobId: " + jobId);
    }

    public static void rmvJobProgress(String jobId) {
        Progress progress = progressList.get(jobId);
        if(null == progress) {
            return;
        }

        progress = null;
        progressList.remove(jobId);

        LOGGER.info("rmvJobProgress jobId: " + jobId);
    }

    public static void setTotalSteps(String jobId, int total) {
        Progress progress = progressList.get(jobId);
        if(null != progress) {
            progress.setTotal(total);
            LOGGER.info("setTotalSteps jobId: " + jobId + ", total: " + progress.getTotal());

            return;
        }

        Progress newProgress = new Progress();
        progressList.put(jobId, newProgress);
        clearJobProgress(jobId);
        newProgress.setTotal(total);

        LOGGER.info("setTotalSteps jobId: " + jobId + ", total: " + newProgress.getTotal());
    }

    public static void increaseCurrentStep(String jobId) {
        Progress progress = progressList.get(jobId);
        if(null == progress) {
            return;
        }

        int current = progress.getCurrent();
        current++;
        if(current > progress.getTotal()) {
            current = progress.getTotal();
        }

        progress.setCurrent(current);
        progress.setProgress(String.valueOf(Math.round((current * 1.0) / progress.getTotal() * 100)));
        if("100".equals(progress.getProgress())) {
            progress.setStatus("finished");
            progress.setStatusDescription("finished");
        }

        LOGGER.info("increaseCurrentStep jobId: " + jobId + ", progress: " + progress.getProgress() + ", status: "
                + progress.getStatus() + ", statusDescription: " + progress.getStatusDescription() + ", current: "
                + progress.getCurrent() + ", total: " + progress.getTotal());
    }

    public static void setStatus(String jobId, String status) {
        Progress progress = progressList.get(jobId);
        if(null == progress) {
            return;
        }

        progress.setStatus(status);

        LOGGER.info("setStatus jobId: " + jobId + ", status: " + progress.getStatus());
    }

    public static void setStatusDescription(String jobId, String statusDescription) {
        Progress progress = progressList.get(jobId);
        if(null == progress) {
            return;
        }

        progress.setStatusDescription(statusDescription);

        LOGGER.info("setStatus jobId: " + jobId + ", statusDescription: " + progress.getStatusDescription());
    }

    public static void setJobProgressFinish(String jobId) {
        Progress progress = progressList.get(jobId);
        if(null == progress) {
            return;
        }

        progress.setProgress("100");
        progress.setStatus("finished");
        progress.setStatusDescription("finished");

        LOGGER.info("setJobProgressFinish jobId: " + jobId + ", progress: " + progress.getProgress() + ", status: "
                + progress.getStatus() + ", statusDescription: " + progress.getStatusDescription());
    }
}
