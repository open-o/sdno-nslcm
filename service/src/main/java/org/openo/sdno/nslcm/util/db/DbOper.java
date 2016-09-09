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

package org.openo.sdno.nslcm.util.db;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.nslcm.util.Const;
import org.openo.sdno.overlayvpn.inventory.sdk.util.InventoryDaoUtil;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.springframework.util.StringUtils;

import net.sf.json.JSONObject;

/**
 * It is used to operate table.<br>
 * 
 * @param <T> DB Model Class
 * @author
 * @version SDNO 0.5 Sep 8, 2016
 */
public class DbOper<T> {

    private DbOper() {

    }

    /**
     * It is used to check the special record is existed or not. <br>
     * 
     * @param clazz object type
     * @param fieldName The name of which field want to check
     * @param fieldVaule The value of which field want to check
     * @return true if the record is existed
     * @throws ServiceException When check failed.
     * @since SDNO 0.5
     */
    public boolean checkRecordIsExisted(Class<?> clazz, String fieldName, String fieldVaule) throws ServiceException {
        ResultRsp<List<T>> queryDbRsp = queryByFilter(clazz, fieldName, fieldVaule, Const.UUID);
        if(CollectionUtils.isEmpty(queryDbRsp.getData())) {
            return false;
        }

        return true;
    }

    /**
     * It is used to insert data. <br>
     * 
     * @param data The list of data that want to be inserted
     * @throws ServiceException When insert failed.
     * @since SDNO 0.5
     */
    public void insert(List<T> data) throws ServiceException {
        new InventoryDaoUtil<T>().getInventoryDao().batchInsert(data);
    }

    /**
     * It is used to update data. <br>
     * 
     * @param clazz object type
     * @param data The list of data that want to be updated
     * @param updateFieldList The field that want to be updated
     * @throws ServiceException When update failed.
     * @since SDNO 0.5
     */
    public void update(Class<?> clazz, List<T> data, String updateFieldList) throws ServiceException {
        new InventoryDaoUtil<T>().getInventoryDao().update(clazz, data, updateFieldList);
    }

    /**
     * It is used to query data. <br>
     * 
     * @param clazz object type
     * @param fieldName The name of which field want to query
     * @param fieldValue The value of which field want to query
     * @return The object list of data
     * @throws ServiceException When query failed
     * @since SDNO 0.5
     */
    public ResultRsp<List<T>> query(Class<?> clazz, String fieldName, String fieldValue) throws ServiceException {
        return queryByFilter(clazz, fieldName, fieldValue, null);
    }

    /**
     * It is used to query data.<br>
     * 
     * @param clazz object type
     * @param uuid The uuid used to query
     * @return The object data
     * @throws ServiceException When query failed
     * @since SDNO 0.5
     */
    public ResultRsp<T> queryById(Class<?> clazz, String uuid) throws ServiceException {
        return new InventoryDaoUtil<T>().getInventoryDao().query(clazz, uuid, null);
    }

    /**
     * It is used to delete data. <br>
     * 
     * @param clazz object type
     * @param uuid The uuid of which data want to be deleted
     * @throws ServiceException When delete failed.
     * @since SDNO 0.5
     */
    public void delete(Class<?> clazz, String uuid) throws ServiceException {
        new InventoryDaoUtil<T>().getInventoryDao().delete(clazz, uuid);
    }

    /**
     * It is used to delete data.<br>
     * 
     * @param clazz object type
     * @param uuids The uuid list of which data want to be deleted
     * @throws ServiceException When delete failed.
     * @since SDNO 0.5
     */
    public void batchDelete(Class<?> clazz, List<String> uuids) throws ServiceException {
        new InventoryDaoUtil<T>().getInventoryDao().batchDelete(clazz, uuids);
    }

    private ResultRsp<List<T>> queryByFilter(Class<?> clazz, String fieldName, String fieldValue,
            String queryResultFields) throws ServiceException {
        Map<String, Object> filterMap = new HashMap<String, Object>();
        if(StringUtils.hasLength(fieldValue)) {
            filterMap.put(fieldName, Arrays.asList(fieldValue));
        }

        String filter = JSONObject.fromObject(filterMap).toString();

        return new InventoryDaoUtil<T>().getInventoryDao().queryByFilter(clazz, filter, queryResultFields);
    }
}
