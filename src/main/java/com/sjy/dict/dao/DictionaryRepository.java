/**
 * 
 */
package com.sjy.dict.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.sjy.dict.domain.Dictionary;

/**
 * @copyright(c) Copyright SJY Corporation 2017.
 * @since 2017年3月29日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@Component
public interface DictionaryRepository extends JpaRepository<Dictionary, String> {

	List<Dictionary> findByCategory(String category);

}
