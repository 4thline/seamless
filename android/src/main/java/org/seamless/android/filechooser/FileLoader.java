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
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.os.FileObserver;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Loader that returns a list of Files in a given file path.
 *
 * @author paulburke (ipaulpro)
 * @author Christian Bauer
 */
public class FileLoader extends AsyncTaskLoader<List<File>> {

    public static final String HIDDEN_PREFIX = ".";

    protected static final int FILE_OBSERVER_MASK = FileObserver.CREATE
        | FileObserver.DELETE | FileObserver.DELETE_SELF
        | FileObserver.MOVED_FROM | FileObserver.MOVED_TO
        | FileObserver.MODIFY | FileObserver.MOVE_SELF;

    protected static FileFilter mDirFilter = new FileFilter() {
        public boolean accept(File file) {
            final String fileName = file.getName();
            // Return directories only and skip hidden directories
            return file.isDirectory() && !fileName.startsWith(HIDDEN_PREFIX);
        }
    };

    protected static FileFilter mFileFilter = new FileFilter() {
        public boolean accept(File file) {
            final String fileName = file.getName();
            // Return files only (not directories) and skip hidden files
            return file.isFile() && !fileName.startsWith(HIDDEN_PREFIX);
        }
    };

    protected static Comparator<File> mComparator = new Comparator<File>() {
        public int compare(File f1, File f2) {
            // Sort alphabetically by lower case, which is much cleaner
            return f1.getName().toLowerCase().compareTo(
                f2.getName().toLowerCase()
            );
        }
    };

    final protected String path;
    protected FileObserver fileObserver;
    protected List<File> data;

    public FileLoader(Context context, String path) {
        super(context);
        this.path = path;
    }

    @Override
    public List<File> loadInBackground() {
        ArrayList<File> list = new ArrayList<File>();

        // Current directory File instance
        final File pathDir = new File(path);

        // List file in this directory with the directory filter
        final File[] dirs = pathDir.listFiles(mDirFilter);
        if (dirs != null) {
            // Sort the folders alphabetically
            Arrays.sort(dirs, mComparator);
            // Add each folder to the File list for the list adapter
            Collections.addAll(list, dirs);
        }

        // List file in this directory with the file filter
        final File[] files = pathDir.listFiles(mFileFilter);
        if (files != null) {
            // Sort the files alphabetically
            Arrays.sort(files, mComparator);
            // Add each file to the File list for the list adapter
            Collections.addAll(list, files);
        }

        return list;
    }

    @Override
    public void deliverResult(List<File> data) {
        if (isReset()) {
            releaseResources();
            return;
        }

        List<File> oldData = this.data;
        this.data = data;

        if (isStarted())
            super.deliverResult(data);

        if (oldData != null && oldData != data)
            releaseResources();
    }

    @Override
    protected void onStartLoading() {
        if (data != null)
            deliverResult(data);

        if (fileObserver == null) {
            fileObserver = new FileObserver(path, FILE_OBSERVER_MASK) {
                @Override
                public void onEvent(int event, String path) {
                    onContentChanged();
                }
            };
        }
        fileObserver.startWatching();

        if (takeContentChanged() || data == null)
            forceLoad();
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if (data != null) {
            releaseResources();
            data = null;
        }
    }

    @Override
    public void onCanceled(List<File> data) {
        super.onCanceled(data);
        releaseResources();
    }

    protected void releaseResources() {
        if (fileObserver != null) {
            fileObserver.stopWatching();
            fileObserver = null;
        }
    }
}