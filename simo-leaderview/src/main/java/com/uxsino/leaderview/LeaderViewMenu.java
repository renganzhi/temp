package com.uxsino.leaderview;

import org.springframework.stereotype.Component;

import com.uxsino.commons.baseclass.AbstractMenu;

@Component
public class LeaderViewMenu extends AbstractMenu {

	public static final String MENU_FILE = "vierer_menu.json";

	@Override
	protected String fileClsPath() {
		return MENU_FILE;
	}

}
