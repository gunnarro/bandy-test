package com.gunnarro.android.bandy.domain.view.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import android.content.Intent;
import android.os.Bundle;

public class ItemTest {

	@Test
	public void constructor() {
		Item item = new Item(1, "item", false);
		assertFalse(item.isEnabled());
		assertEquals(1, item.getId().intValue());
		assertEquals("item", item.getValue());
	}

	@Test
	public void parcable() {
		Item item = new Item(1, "item", false);
//		Intent intent = new Intent("action");
//		Bundle bundle = new Bundle();
//		bundle.putParcelable("key_item", item);
//		intent.putExtras(bundle);
//		Item itemPar = (Item) intent.getParcelableExtra("key_item");
		assertFalse(item.isEnabled());
		assertEquals(1, item.getId().intValue());
		assertEquals("item", item.getValue());
//		Item.CREATOR.createFromParcel(item);
	}
}