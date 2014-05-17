package com.gunnarro.android.bandy.view.dashboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.gunnarro.android.bandy.R;

@RunWith(RobolectricTestRunner.class)
public class HomeActivityTest {

	HomeActivity homeActivity;

	@Before
	public void setup() {
		homeActivity = Robolectric.buildActivity(HomeActivity.class).create().get();
		Bundle bundle = new Bundle();
	}

	@Ignore
	@Test
	public void checkActivityNotNull() throws Exception {
		assertNotNull(homeActivity);
		// assertNotNull(
		// homeActivity.getSupportFragmentManager().findFragmentById( R.id. ) );
	}

	@Ignore
	@Test
	public void checkView() throws Exception {
		// String home = new
		// HomeActivity().getResources().getString(R.string.home);
		assertEquals("", homeActivity.getTitle());
	}

	@Ignore
	@Test
	public void buttonClickShouldStartNewActivity() throws Exception {
		Button button = (Button) homeActivity.findViewById(R.id.about_btn);
		button.performClick();
		Intent intent = Robolectric.shadowOf(homeActivity).peekNextStartedActivity();
		// assertEquals(SecondActivity.class.getCanonicalName(),
		// intent.getComponent().getClassName());
	}

	@Ignore
	@Test
	public void testButtonClick() throws Exception {
		Button view = (Button) homeActivity.findViewById(R.id.about_btn);
		assertNotNull(view);
		view.performClick();
	}
}
