package com.company.navurl.web;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.web.Connection;
import com.haulmont.cuba.web.DefaultApp;
import com.haulmont.cuba.web.WebWindowManager;
import com.vaadin.server.Page;
import org.apache.commons.lang.StringUtils;

public class CustomApp extends DefaultApp {
    @Override
    public void connectionStateChanged(Connection connection) throws LoginException {
        super.connectionStateChanged(connection);

        if (connection.isAuthenticated()) {
            // open window based on URL fragment after login
            Page page = Page.getCurrent();

            String fragment = page.getUriFragment();
            if (StringUtils.isNotEmpty(fragment)) {
                WindowInfo windowInfo = windowConfig.findWindowInfo(fragment);
                if (getConnection().isAuthenticated()) {
                    if (windowInfo != null) {
                        // check if we logged in
                        WebWindowManager wm = getWindowManager();
                        wm.openWindow(windowInfo, WindowManager.OpenType.THIS_TAB);
                    } else {
                        page.setUriFragment("");
                    }
                }
            }
        }
    }
}