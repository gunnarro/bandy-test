package com.gunnarro.android.bandy.repository.view;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gunnarro.android.bandy.custom.CustomLog;

public class MatchResultViewTest {

	@Test
	public void testQuery() {
		assertNotNull(MatchResultView.createMatchResultViewQuery());
		CustomLog.d(this.getClass(), MatchResultView.createMatchResultViewQuery());
	}
}
