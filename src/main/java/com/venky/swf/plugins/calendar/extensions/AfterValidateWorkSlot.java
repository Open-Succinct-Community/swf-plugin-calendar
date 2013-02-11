package com.venky.swf.plugins.calendar.extensions;

import com.venky.core.date.DateUtils;
import com.venky.swf.db.extensions.AfterModelValidateExtension;
import com.venky.swf.plugins.calendar.db.model.WorkSlot;

public class AfterValidateWorkSlot extends AfterModelValidateExtension<WorkSlot>{
	static {
		registerExtension(new AfterValidateWorkSlot());
	}
	@Override
	public void afterValidate(WorkSlot model) {
		if (DateUtils.getTime(model.getStartTime()).after(DateUtils.getTime(model.getEndTime()))){
			throw new RuntimeException("Start time cannot be after End time!");
		}
	}

}
