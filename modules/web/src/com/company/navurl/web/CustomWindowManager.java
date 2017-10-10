package com.company.navurl.web;

import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.web.WebWindowManager;
import com.vaadin.server.Page;

import java.util.Map;

// Update URL fragment on opening of new window
public class CustomWindowManager extends WebWindowManager {
    // this works only for windows opened from Menu, override openEditor / openLookup if needed
    @Override
    public Window openWindow(WindowInfo windowInfo, OpenType openType, Map<String, Object> params) {
        Window window = super.openWindow(windowInfo, openType, params);

        if (window != null && openType.getOpenMode() != OpenMode.DIALOG) {
            Page page = Page.getCurrent();
            page.setUriFragment(windowInfo.getId());
        }

        return window;
    }

    // reset uri fragment if workarea is empty
    @Override
    public void close(Window window) {
        super.close(window);

        boolean hasTabbedWindows = getOpenWindows().stream()
                .anyMatch(w -> windowOpenMode.get(w).getOpenMode() != OpenMode.DIALOG);
        if (!hasTabbedWindows) {
            Page page = Page.getCurrent();
            page.setUriFragment("");
        }
    }
}