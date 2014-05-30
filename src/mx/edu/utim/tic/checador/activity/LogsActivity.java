package mx.edu.utim.tic.checador.activity;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

import mx.edu.utim.tic.checador.R;
import mx.edu.utim.tic.checador.fragment.LogsDiaFragment;
import mx.edu.utim.tic.checador.fragment.LogsRangoFragment;
import mx.edu.utim.tic.checador.fragment.LogsSemanaFragment;
import mx.edu.utim.tic.checador.fragment.OpcionesFragment;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;

public class LogsActivity extends FragmentActivity {

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	LoadDataTask loadTask = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logs);
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		
		// Set up the action bar.
//		final ActionBar actionBar = getActionBar();
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//actionBar.setDisplayShowHomeEnabled(false);
	    //actionBar.setDisplayShowTitleEnabled(false);
	    
	    final PagerTabStrip strip = PagerTabStrip.class.cast(findViewById(R.id.pager_title_strip));
	    strip.setDrawFullUnderline(false);
//	    strip.setTabIndicatorColor(Color.DKGRAY);
//	    strip.setBackgroundColor(Color.GRAY);
//	    strip.setNonPrimaryAlpha(0.5f);
//	    strip.setTextSpacing(25);
	    //strip.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
	    
	    
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
//		mViewPager
//		.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//			@Override
//			public void onPageSelected(int position) {
//				actionBar.setSelectedNavigationItem(position);
//			}
//		});
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.logs, menu);
//		return true;
//	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.action_logout:
			finish();
			break;
		}
		return true;
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			mViewPager.setVisibility(View.VISIBLE);
			mViewPager.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mViewPager.setVisibility(show ? View.VISIBLE : View.GONE);
				}
			});

		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mViewPager.setVisibility(show ? View.VISIBLE : View.GONE);
		}
	}
	
	public class LoadDataTask extends AsyncTask<Integer, Void,Fragment>{

		@Override
		protected Fragment doInBackground(Integer... arg0) {
			switch (arg0[0]) {
			case 0:
				return new LogsDiaFragment();
			case 1:
				return new LogsSemanaFragment();
			case 2:
				return new LogsRangoFragment();
			case 3:
				return new OpcionesFragment();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Fragment result) {
			loadTask = null;
			//showProgress(false);
		}
	}
	
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			//showProgress(true);
			loadTask = new LoadDataTask();
			try {
				return loadTask.execute(position).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}

		@Override
		public int getCount() {
			// Show 4 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			case 3:
				return getString(R.string.title_section4).toUpperCase(l);
			}
			return null;
		}
	}
}
