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

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;

/**
 * Session handler.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 Oct 18, 2016
 */
public class DatabaseSessionHandler extends SqlSessionDaoSupport {

    /**
     * Database name.
     */
    private String dataBaseName;

    /**
     * @return Returns the dataBaseName.
     */
    public String getDataBaseName() {
        return dataBaseName;
    }

    /**
     * @param dataBaseName The dataBaseName to set.
     */
    public void setDataBaseName(String dataBaseName) {
        this.dataBaseName = dataBaseName;
    }

    /**
     * It's used to switch data source dynamically when get SQL session.<br>
     * 
     * @return The object of SqlSession.
     * @since SDNO 0.5
     */
    @Override
    public SqlSession getSqlSession() {
        DataSourceHolder.setDataSource(dataBaseName);
        return super.getSqlSession();
    }
}
