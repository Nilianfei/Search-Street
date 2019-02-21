package com.graduation.ss.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.graduation.ss.entity.Help;

public interface HelpDao {
	/**
	 * 通过help id查询帮助
	 * 
	 * @param helpId
	 * @return
	 */
	Help queryByHelpId(long helpId);

	/**
	 * 分页查询帮助，可输入的条件有：求助ID，帮助者用户ID,帮助状态
	 * 
	 * @param help
	 * @param rowIndex 从第几行开始取数据
	 * @param pageSize 返回的条数
	 * @return
	 */
	List<Help> queryHelpListFY(@Param("helpCondition") Help help, @Param("rowIndex") int rowIndex,
			@Param("pageSize") int pageSize);

	/**
	 * 查询帮助，可输入条件有：求助ID，帮助者用户Id，帮助状态
	 * 
	 * @param help
	 * @return
	 */
	List<Help> queryHelpList(@Param("helpCondition") Help help);

	/**
	 * 返回queryHelpList总数
	 * 
	 * @param helpCondition
	 * @return
	 */
	int queryHelpCount(@Param("helpCondition") Help helpCondition);

	/**
	 * 添加帮助
	 * 
	 * @param help
	 * @return
	 */
	int insertHelp(Help help);

	/**
	 * 更新帮助信息（主要是更新帮助状态以及求助者的评价）
	 * 
	 * @param help
	 * @return
	 */
	int updateHelp(Help help);
}
