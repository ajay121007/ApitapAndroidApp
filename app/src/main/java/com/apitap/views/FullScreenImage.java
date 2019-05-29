package com.apitap.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.apitap.App;
import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.ProgressDialogLoading;
import com.apitap.model.Utils;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.fragments.FragmentItems;
import com.apitap.views.fragments.FragmentMessages;
import com.apitap.views.fragments.FragmentScanner;
import com.apitap.views.fragments.FragmentSpecial;
import com.apitap.views.fragments.Fragment_Ads;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.EventLogger;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.jsibbold.zoomage.ZoomageView;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Shami on 11/10/2017.
 */

public class FullScreenImage extends AppCompatActivity implements PlaybackPreparer, VideoRendererEventListener,  RequestListener<String, GlideDrawable>, View.OnClickListener {


    @SuppressLint("NewApi")

    ZoomageView imgDisplay;
    Button btnClose;
    LinearLayout collapse,collapse_white;
    RelativeLayout main_rel;
    String bmp, video="", merchant, adName, desc, id,merchantid;
    int adpos;
    long currentvidPosition=0;
    int height=0,width=0;
    CircularProgressView circleIndicator;
    private static  int state =0;

    private SimpleExoPlayer player;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private Handler mainHandler;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private String[] extensions;
    private EventLogger eventLogger;
    private PlayerView videoPlayerView;
    LinearLayout scan_tool;
    LinearLayout msg_tool;
    LinearLayout search_tool;
    RelativeLayout viewMain;
    FrameLayout frameLayout;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    getWindow().setFormat(PixelFormat.TRANSLUCENT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Bundle extras = getIntent().getExtras();
        bmp = (String) extras.getString("imagebitmap");
        video = (String) extras.getString("video");
        merchant = (String) extras.getString("merchant");
        adName = (String) extras.getString("adName");
        desc = (String) extras.getString("desc");
        id = (String) extras.getString("id");
        adpos = (Integer) extras.getInt("adpos");
        merchantid = (String) extras.getString("merchantid");
        currentvidPosition = (Long) extras.getLong("vidpos");
        setContentView(R.layout.layout_full);

        imgDisplay = (ZoomageView) findViewById(R.id.imgDisplay);
        circleIndicator = (CircularProgressView) findViewById(R.id.pocket);
        collapse = (LinearLayout) findViewById(R.id.collapse);
        collapse_white = (LinearLayout) findViewById(R.id.collapse_white);
        btnClose = (Button) findViewById(R.id.btnClose);
        videoPlayerView = (PlayerView) findViewById(R.id.video_view);
        viewMain = (RelativeLayout) findViewById(R.id.linear);
        frameLayout = (FrameLayout) findViewById(R.id.container_body);
        scan_tool = (LinearLayout) findViewById(R.id.ll_scan);
        msg_tool = (LinearLayout) findViewById(R.id.ll_message);
        search_tool = (LinearLayout) findViewById(R.id.ll_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Merchant Detail");
        msg_tool.setOnClickListener(this);
        search_tool.setOnClickListener(this);
        scan_tool.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainHandler = new Handler();
        mediaDataSourceFactory = buildDataSourceFactory(true);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

// 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

        DrmSessionManager<FrameworkMediaCrypto> drmSessionManager = null;

        @DefaultRenderersFactory.ExtensionRendererMode int extensionRendererMode =
                App.getInstance().useExtensionRenderers()
                        ? (true ? DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
                        : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
                        : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF;
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(this,
                drmSessionManager, extensionRendererMode);

// 3. Create the player
        //    player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
        player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);

        if (!video.equals("")) {
            //     setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            toolbar.setVisibility(View.GONE);
            imgDisplay.setVisibility(View.GONE);
            collapse.setVisibility(View.GONE);
            collapse_white.setVisibility(View.VISIBLE);
            videoPlayerView.setVisibility(View.VISIBLE);
            initializePlayer();

        }else{
            circleIndicator.setVisibility(View.VISIBLE);
            imgDisplay.setVisibility(View.VISIBLE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            Glide.with(this).load(bmp).listener(this).placeholder(R.drawable.loading).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imgDisplay);
        }

        collapse_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapse.performClick();
            }
        });


        collapse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!video.equals("")) {
                    player.release();

                    startActivity(new Intent(FullScreenImage.this, AdDetailActivity.class)
                            .putExtra("videoUrl", video)
                            .putExtra("image", bmp)
                            .putExtra("merchant", merchant)
                            .putExtra("adName", adName)
                            .putExtra("desc", desc)
                            .putExtra("id", id)
                            .putExtra("adpos",adpos)
                            .putExtra("vidpos", currentvidPosition)
                            );
                } else {
                    state=0;
                    finish();

                }
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
        circleIndicator.setVisibility(View.GONE);

        return false;
    }

    @Override
    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
        circleIndicator.setVisibility(View.GONE);

        ViewTreeObserver vto = imgDisplay.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                imgDisplay.getViewTreeObserver().removeOnPreDrawListener(this);
                height = imgDisplay.getMeasuredHeight();
                width = imgDisplay.getMeasuredWidth();
                Log.d("ImagesHeightWidth",height+" "+width);
//                if (state==0){
//                if (height<width){
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                }else{
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                }
//                state=1;
//                }
                return true;
            }
        });
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!video.equals("")) {
            currentvidPosition = Math.max(0, player.getContentPosition());
            player.release();

            startActivity(new Intent(FullScreenImage.this, AdDetailActivity.class)
                    .putExtra("videoUrl", video)
                    .putExtra("image", bmp)
                    .putExtra("merchant", merchant)
                    .putExtra("adName", adName)
                    .putExtra("desc", desc)
                    .putExtra("id", id)
                    .putExtra("adpos",adpos)
                    .putExtra("vidpos", currentvidPosition)
                    .putExtra("merchantid", merchantid)
            );
        } else {
            state=0;
            finish();

        }
    }




    @Override
    protected void onStop() {
        super.onStop();
        if (!video.isEmpty()) {
            player.release();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!video.isEmpty())
            player.release();
    }


    public void initializePlayer() {
        Uri videoUri = Uri.parse(ATPreferences.readString(this, Constants.KEY_VIDEO_URL) + video);
        TrackSelection.Factory adaptiveTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
        trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
        eventLogger = new EventLogger(trackSelector);
        extensions = new String[1];
        com.google.android.exoplayer2.source.MediaSource[] mediaSources = new com.google.android.exoplayer2.source.MediaSource[1];
        //   mediaSources[i] = buildMediaSource(uris[i], extensions[i], mainHandler, eventLogger);


        mediaSources[0] = buildMediaSource(videoUri, extensions[0], mainHandler, eventLogger);

        //   mediaSources[i] = buildMediaSource(uri, extensions[i], mainHandler, eventLogger);

        Log.d("lengths", extensions.length + "");
        com.google.android.exoplayer2.source.MediaSource mediaSource = mediaSources.length == 1 ? mediaSources[0]
                : new ConcatenatingMediaSource(mediaSources);


        videoPlayerView.setUseController(true);
        videoPlayerView.setPlaybackPreparer(this);
        videoPlayerView.setPlayer(player);
        player.addListener(new PlayerEventListener());
        player.addListener(eventLogger);
        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.setPlayWhenReady(true); //run file/link when ready to play.
        player.addVideoDebugListener(this);

        player.prepare(mediaSource, false, false);
        player.seekTo(currentvidPosition);
    }
    @Override
    public void preparePlayback() {
        initializePlayer();
    }

    @Override
    public void onVideoEnabled(DecoderCounters counters) {

    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

    }

    @Override
    public void onVideoInputFormatChanged(Format format) {

    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

    }

    @Override
    public void onRenderedFirstFrame(Surface surface) {

    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {

    }

    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return App.getInstance()
                .buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    private com.google.android.exoplayer2.source.MediaSource buildMediaSource(
            Uri uri,
            String overrideExtension,
            @Nullable Handler handler,
            @Nullable MediaSourceEventListener listener) {
        @C.ContentType int type = TextUtils.isEmpty(overrideExtension) ? Util.inferContentType(uri)
                : Util.inferContentType("." + overrideExtension);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory),
                        buildDataSourceFactory(false))
                        .createMediaSource(uri, handler, listener);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(
                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory),
                        buildDataSourceFactory(false))
                        .createMediaSource(uri, handler, listener);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(uri, handler, listener);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(uri, handler, listener);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ll_message:
                frameLayout.setVisibility(View.VISIBLE);
                viewMain.setVisibility(View.GONE);
                displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, new Bundle());
                break;

            case R.id.ll_scan:
                frameLayout.setVisibility(View.VISIBLE);
                viewMain.setVisibility(View.GONE);
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_REQUEST_CODE);
                } else {
                    //mTxtHeading.setText("Scan");
                    //mlogo.setVisibility(View.GONE);
                    //mTxtHeading.setVisibility(View.VISIBLE);
                    displayView(new FragmentScanner(), Constants.TAG_SCANNER, new Bundle());
                }

                break;
            case R.id.ll_search:
                Utils.showSearchDialog(this);
                break;
    }
    }

    private class PlayerEventListener extends Player.DefaultEventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == Player.STATE_ENDED) {

            }
            //updateButtonVisibilities();
        }

        @Override
        public void onPositionDiscontinuity(@Player.DiscontinuityReason int reason) {
        }

        @Override
        public void onPlayerError(ExoPlaybackException e) {
            String errorString = null;

        }

        @Override
        @SuppressWarnings("ReferenceEquality")
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {


        }

    }
    public void displayView(Fragment fragment, String tag, Bundle bundle) {
        //  if (fragment != null) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragB = fragmentManager.findFragmentByTag(tag);
        if (bundle != null)
            fragment.setArguments(bundle);
        //  if (fragB == null) {
        fragmentTransaction.replace(R.id.container_body, fragment);
        if (fragment instanceof Fragment_Ads || fragment instanceof FragmentSpecial || fragment instanceof FragmentItems) {

        } else
            fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
//            } else
//                fragmentTransaction.show(fragB);
        //  getSupportActionBar().setTitle(tag);
        // }
    }

}

