/*
 * Copyright (C) 2012 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seamless.android.filechooser;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.seamless.android.R;

/**
 * Main Activity that handles the FileListFragments
 *
 * @author paulburke (ipaulpro)
 * @author Christian Bauer
 */
public class FileChooserActivity
    extends FragmentActivity
    implements OnBackStackChangedListener {

    public static final String EXTRA_SELECT_DIRECTORY = FileChooserActivity.class.getName() + "_EXTRA_SELECT_DIRECTORY";

    protected static final String STATE_PATH = "path";
    protected static final String STATE_SELECT_DIRECTORY = "selectDirectory";

    public static final String EXTERNAL_BASE_PATH =
        Environment.getExternalStorageDirectory().getAbsolutePath();

    protected FragmentManager fragmentManager;

    protected BroadcastReceiver storageListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, R.string.storage_removed, Toast.LENGTH_LONG).show();
            finishWithResult(null);
        }
    };

    protected String path;
    protected boolean selectDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chooser);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);

        if (savedInstanceState == null) {
            path = EXTERNAL_BASE_PATH;
            selectDirectory = getIntent().getExtras() != null && getIntent().getExtras().getBoolean(EXTRA_SELECT_DIRECTORY);
            addFragment(path);
        } else {
            path = savedInstanceState.getString(STATE_PATH);
            selectDirectory = savedInstanceState.getBoolean(STATE_SELECT_DIRECTORY);
        }

        setTitle(path);

        Button selectDirectoryButton = (Button) findViewById(R.id.select_directory_button);
        selectDirectoryButton.setVisibility(selectDirectory ? View.VISIBLE : View.GONE);
        selectDirectoryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCurrentDirectorySelected();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterStorageListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerStorageListener();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_PATH, path);
        outState.putBoolean(STATE_SELECT_DIRECTORY, selectDirectory);
    }

    public void onBackStackChanged() {
        path = EXTERNAL_BASE_PATH;
        int count = fragmentManager.getBackStackEntryCount();
        if (count > 0) {
            BackStackEntry fragment = fragmentManager.getBackStackEntryAt(count - 1);
            path = fragment.getName();
        }
        setTitle(path);
    }

    /**
     * Add the initial Fragment with given path.
     *
     * @param path The absolute path of the file (directory) to display.
     */
    private void addFragment(String path) {
        FileListFragment explorerFragment = FileListFragment.newInstance(this.path);
        fragmentManager.beginTransaction()
            .add(R.id.explorer_fragment, explorerFragment).commit();
    }

    /**
     * "Replace" the existing Fragment with a new one using given path.
     * We're really adding a Fragment to the back stack.
     *
     * @param path The absolute path of the file (directory) to display.
     */
    private void replaceFragment(String path) {
        FileListFragment explorerFragment = FileListFragment.newInstance(path);
        fragmentManager.beginTransaction()
            .replace(R.id.explorer_fragment, explorerFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(path).commit();
    }

    /**
     * Finish this Activity with a result code and URI of the selected file.
     *
     * @param file The file selected.
     */
    private void finishWithResult(File file) {
        if (file != null) {
            Uri uri = Uri.fromFile(file);
            setResult(RESULT_OK, new Intent().setData(uri));
            finish();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    /**
     * Called when the user selects a File
     *
     * @param file The file that was selected
     */
    protected void onFileSelected(File file) {
        if (file != null) {
            path = file.getAbsolutePath();

            if (file.isDirectory()) {
                replaceFragment(path);
            } else if (!selectDirectory) {
                finishWithResult(file);
            }
        } else {
            Toast.makeText(FileChooserActivity.this, R.string.error_selecting_file, Toast.LENGTH_SHORT).show();
        }
    }

    protected void onCurrentDirectorySelected() {
        File file = new File(path);
        if (!file.isDirectory())
            file = file.getParentFile();
        finishWithResult(file);
    }

    /**
     * Register the external storage BroadcastReceiver.
     */
    private void registerStorageListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        registerReceiver(storageListener, filter);
    }

    /**
     * Unregister the external storage BroadcastReceiver.
     */
    private void unregisterStorageListener() {
        unregisterReceiver(storageListener);
    }
}
