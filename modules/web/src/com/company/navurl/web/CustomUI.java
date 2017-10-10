package com.company.navurl.web;

import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.WebWindowManager;
import com.vaadin.server.VaadinRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import javax.inject.Inject;

// Here we listen to URL changes, open window on URL fragment change
public class CustomUI extends AppUI {
    @Inject
    private Logger log;
    @Inject
    private WindowConfig windowConfig;

    @Override
    protected void init(VaadinRequest request) {
        super.init(request);

        getPage().addUriFragmentChangedListener(source ->
                enter(source.getUriFragment())
        );

        // Read the initial URI fragment
        enter(getPage().getUriFragment());
    }

    private void enter(String fragment) {
        // open window here
        log.info("Enter URL state '{}'", fragment);

        // check if we logged in
        if (getApp().getConnection().isAuthenticated()) {
            WebWindowManager wm = getApp().getWindowManager();

            if (StringUtils.isNotEmpty(fragment)) {
                WindowInfo windowInfo = windowConfig.findWindowInfo(fragment);
                if (windowInfo != null) {
                    // check if already opened

                    boolean hasWindow = wm.getOpenWindows().stream()
                            .anyMatch(w -> windowInfo.getId().equals(w.getId()));
                    if (!hasWindow) {
                        wm.openWindow(windowInfo, OpenType.NEW_TAB);
                    }
                } else {
                    getPage().setUriFragment("");
                }
            } else {
                wm.closeAllTabbedWindows();
            }
        }
    }
}