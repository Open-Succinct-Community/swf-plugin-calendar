package com.venky.swf.plugins.calendar.extensions;

import com.venky.swf.db.extensions.BeforeModelValidateExtension;
import com.venky.swf.plugins.calendar.db.model.WorkCalendar;

public class BeforeValidateWorkCalendar extends BeforeModelValidateExtension<WorkCalendar> {
	static {
		registerExtension(new BeforeValidateWorkCalendar());
	}
	@Override
	public void beforeValidate(WorkCalendar model) {
		WorkCalendar parent = model.getParentWorkCalendar();
		if (parent != null){
			if (model.getId() == parent.getId()){
				throw new RuntimeException("Parent of a calendar cannot be itself");
			}
			if (model.getStartDate() == null){
				model.setStartDate(parent.getStartDate());
			}
			if (model.getEndDate() == null){
				model.setEndDate(parent.getEndDate());
			}
			
			if ( model.getStartDate().before(parent.getStartDate()) ||
					model.getEndDate().after(parent.getEndDate()) ){
				throw new RuntimeException("Child Calendar date range must be a subset of parent Calendar's date range");
			}
		}
	}

}
