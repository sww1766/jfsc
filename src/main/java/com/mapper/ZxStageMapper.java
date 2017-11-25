package com.mapper;

import java.util.List;
import java.util.Map;

import com.model.ZxTable;

public interface ZxStageMapper {
	public List<Map<String,Object>> queryForList(ZxTable zxTable);
}
