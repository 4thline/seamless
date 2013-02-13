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
import java.util.List;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;
import org.seamless.android.R;

/**
 * Fragment that displays a list of Files in a given path.
 * 
 * @author paulburke (ipaulpro)
 * @author Christian Bauer
 * 
 */
public class FileListFragment
    extends ListFragment
    implements LoaderManager.LoaderCallbacks<List<File>> {

	private static final int LOADER_ID = 0;

	protected FileListAdapter adapter;
    protected String path;

	/**
	 * Create a new instance with the given file path.
	 * 
	 * @param path The absolute path of the file (directory) to display.
	 * @return A new Fragment with the given file path. 
	 */
	public static FileListFragment newInstance(String path) {
		FileListFragment fragment = new FileListFragment();
		Bundle args = new Bundle();
		args.putString(FileChooserActivity.STATE_PATH, path);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		adapter = new FileListAdapter(getActivity());
		path = getArguments() != null
            ? getArguments().getString(FileChooserActivity.STATE_PATH)
            : Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		setEmptyText(getString(R.string.empty_directory));
		setListAdapter(adapter);
		setListShown(false);
		getLoaderManager().initLoader(LOADER_ID, null, this);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		FileListAdapter adapter = (FileListAdapter) l.getAdapter();
		if (adapter != null) {
			File file = (File) adapter.getItem(position);
			path = file.getAbsolutePath();
			((FileChooserActivity) getActivity()).onFileSelected(file);
		}
	}

	public Loader<List<File>> onCreateLoader(int id, Bundle args) {
		return new FileLoader(getActivity(), path);
	}

	public void onLoadFinished(Loader<List<File>> loader, List<File> data) {
		adapter.setListItems(data);
		if (isResumed())
			setListShown(true);
		else
			setListShownNoAnimation(true);
	}

	public void onLoaderReset(Loader<List<File>> loader) {
		adapter.clear();
	}
}