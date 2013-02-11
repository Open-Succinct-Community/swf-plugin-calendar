package com.venky.swf.plugins.calendar.extensions;

import com.venky.swf.db.extensions.AfterModelValidateExtension;
import com.venky.swf.plugins.calendar.db.model.WorkCalendar;

public class AfterValidateWorkCalendar extends AfterModelValidateExtension<WorkCalendar>{
	static {
		registerExtension(new AfterValidateWorkCalendar());
	}
	@Override
	public void afterValidate(WorkCalendar model) {
		if (model.getStartDate().after(model.getEndDate())){
			throw new RuntimeException("Start date cannot be after End date!");
		}
	}

}
