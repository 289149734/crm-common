/**
 * 
 */
package com.sjy.base.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sjy.base.domain.SimpleOper;

/**
 * @Title: SimpleOperRepository.java
 * @Package com.sjy.dao
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liyan
 * @email 289149734@qq.com
 * @date 2017年8月24日 下午8:32:29
 * @version V1.0
 */
@Repository
public interface SimpleOperRepository extends JpaRepository<SimpleOper, Long> {

}
