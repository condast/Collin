package org.collin.core.advice;

public interface IAdvice {

	enum AdviceTypes{
		FAIL,
		PROGRESS,
		SUCCESS,
	}

	String getMember();

	String getAdvice();

	int getRepeat();

	IAdvice.AdviceTypes getType();

}