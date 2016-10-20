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

package org.openo.sdno.nslcm.dao.multi;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.nslcm.util.Const;
import org.openo.sdno.nslcm.util.exception.ApplicationException;
import org.openo.sdno.overlayvpn.consts.HttpCode;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Class for routing data source.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 Oct 18, 2016
 */
public class MultiDataSource extends AbstractRoutingDataSource {

    private Map<Object, Object> targetDataSources = new HashMap<Object, Object>();

    private static final String CONFIG_PATH = "generalconfig/jdbc.json";

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceName = DataSourceHolder.getDataSource();
        if(dataSourceName == null) {
            dataSourceName = "inventory";
        }
        try {
            this.setDataSource(dataSourceName);
        } catch(ServiceException e) {
            dataSourceName = "inventory";
        }
        return dataSourceName;
    }

    private void setDataSource(String dataSourceName) throws ServiceException {
        Map<String, String> values = getJsonDataFromFile(CONFIG_PATH);
        String driverClassName = values.get("driverClassName");
        String url = values.get("jdbcUrl");
        String userName = values.get("jdbcUsername");
        String password = values.get("jdbcPassword");
        ComboPooledDataSource dataSource = this.createDataSource(driverClassName, url, userName, password);
        this.addTargetDataSource(dataSourceName, dataSource);
    }

    private ComboPooledDataSource createDataSource(String driverClassName, String url, String username,
            String password) {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(driverClassName);
            dataSource.setJdbcUrl(url);
            dataSource.setUser(username);
            dataSource.setPassword(password);
        } catch(PropertyVetoException e) {
            throw new ApplicationException(HttpCode.ERR_FAILED, Const.OPER_DB_FAIL);
        }
        return dataSource;
    }

    private void addTargetDataSource(String dataSourceName, ComboPooledDataSource dataSource) {
        this.targetDataSources.put(dataSourceName, dataSource);
        super.setTargetDataSources(this.targetDataSources);
        afterPropertiesSet();
    }

    private static Map<String, String> getJsonDataFromFile(String path) throws ServiceException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            byte[] bytes = Files.readAllBytes(Paths.get(path));
            return mapper.readValue(bytes, Map.class);
        } catch(IOException e) {
            throw new ApplicationException(HttpCode.ERR_FAILED, Const.OPER_DB_FAIL);
        }
    }
}
