package com.example.finalprojectapplication.data.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.finalprojectapplication.ui.widget.StackRemoteViewsFactory;

public class StackWidgetService extends RemoteViewsService {
    public StackWidgetService() {
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext());
    }
}
