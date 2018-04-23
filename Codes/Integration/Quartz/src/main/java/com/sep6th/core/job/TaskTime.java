package com.sep6th.core.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * The Apache License 2.0
 * Copyright (c) 2018 sep6th
 */

public class TaskTime {

	private static Logger log = LoggerFactory.getLogger(TaskTime.class);
	
	public void first_Job(){
		log.info("[first_Job]-----begin-----");
		first();
		log.info("[first_Job]-----end-----");
	}
	
	public void second_Job(){
		log.info("[second_Job]-----begin-----");
		second();
		log.info("[second_Job]-----end-----");
	}
	
	private void first(){
		log.info("[first_Job]-----doing-----");
	}
	
	private void second(){
		log.info("[second_Job]-----doing-----");
	}
	
	
}
