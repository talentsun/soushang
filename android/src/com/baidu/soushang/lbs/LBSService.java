package com.baidu.soushang.lbs;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.baidu.soushang.Config;
import com.baidu.soushang.Intents;
import com.baidu.soushang.SouShangApplication;
import com.baidu.soushang.lbs.LBSClientRequestClientHandler.ClientListener;
import com.baidu.soushang.lbs.Models.User;
import com.baidu.soushang.utils.NetworkUtils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class LBSService extends Service {
  private static final String LBS_SERVER = "118.244.225.222";
  private static final String TAG = "lbs";

  private static final int LBS_SERVER_PORT = 9300;
  private static final int STARTUP = 1;
  private static final int HEARTBEAT = 2;
  private static final int LBS_INFO = 3;
  private static final int UPDATE_PEERS = 4;
  private static final int SHUTDOWN = 5;
  
  private AlarmManager mAlarmManager;
  private SouShangApplication mApplication;
  
  private Channel mChannel;
  private ClientBootstrap mClientBootstrap;
  private LBSClientRequestClientHandler mHandler;
  private PendingIntent mHeartbeat;
  private boolean mStartup = false;
  private HandlerThread mWorker;
  private LBSClient mClient;
  
  private LocationClient mLocationClient;
  private BDLocationListener mLocationListener = new MyLocationListener();
  private float mLatitude;
  private float mLongitude;
  
  public class LBSClient extends Handler {
    public LBSClient(Looper looper) {
      super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
      switch(msg.what) {
        case STARTUP:
          startup();
          break;
        case HEARTBEAT:
          mHandler.sendHeartbeat();
          break;
        case LBS_INFO:
          mHandler.sendLBSInfo(mLongitude, mLatitude);
          break;
        case UPDATE_PEERS:
          mHandler.sendFetchPeerListReq();
          break;
        case SHUTDOWN:
          shutdown();
          break;
      }
      super.handleMessage(msg);
    }
    
  }

  public class MyLocationListener implements BDLocationListener {
    @Override
    public void onReceiveLocation(BDLocation location) {
      if (location != null) {
        mLatitude = (float) location.getLatitude();
        mLongitude = (float) location.getLongitude();
        Message.obtain(mClient, LBS_INFO).sendToTarget();
      }
    }

    public void onReceivePoi(BDLocation poiLocation) {}
  }

  @Override
  public void onCreate() {
    mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    mApplication = (SouShangApplication) getApplication();
    
    mWorker = new HandlerThread("lbs-service");
    mWorker.start();
    mClient = new LBSClient(mWorker.getLooper());

    Message.obtain(mClient, STARTUP).sendToTarget();
    
    super.onCreate();
  }
  
  @Override
  public void onDestroy() {
    Message.obtain(mClient, SHUTDOWN).sendToTarget();
    super.onDestroy();
  }
  
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    if (intent != null) {
      String action = intent.getAction();
      if (Intents.ACTION_HEARTBEAT.equalsIgnoreCase(action)) {
        Message.obtain(mClient, HEARTBEAT).sendToTarget();
      } else if (Intents.ACTION_UPDATE_PEERS.equalsIgnoreCase(action)) {
        Message.obtain(mClient, UPDATE_PEERS).sendToTarget();
      }
    }
    
    return super.onStartCommand(intent, flags, startId);
  }

  private void shutdown() {
    if (mStartup) {
      mStartup = false;
      
      mChannel.close().awaitUninterruptibly();
      mClientBootstrap.releaseExternalResources();
      
      stopHeartbeat();
      stopLocationClient();
    }
  }
  
  private void broadcastStartupInfo(boolean success) {
    Intent intent = new Intent();
    intent.setAction(Intents.ACTION_LBS_STARTUP);
    intent.putExtra(Intents.EXTRA_STARTUP_SUCCESS, success);
    sendBroadcast(intent);
  }
  
  private void startLocationClient() {
    mLocationClient = new LocationClient(getApplicationContext());
    LocationClientOption option = new LocationClientOption();
    option.setOpenGps(true);
    option.setAddrType("all");
    option.setCoorType("bd09ll");
    option.disableCache(true);
    mLocationClient.setLocOption(option);
    mLocationClient.registerLocationListener(mLocationListener);
    mLocationClient.start();
  }
  
  private void stopLocationClient() {
    if (mLocationClient != null && mLocationClient.isStarted()) {
      mLocationClient.stop();
      mLocationClient.unRegisterLocationListener(mLocationListener);
    }
  }

  private void startup() {
    if (!mStartup) {
      System.setProperty("org.jboss.netty.selectTimeout", "60000");
      System.setProperty("org.jboss.netty.epollBugWorkaround", "true");
      System.setProperty("java.net.preferIPV4Stack", "true");
      System.setProperty("java.net.preferIPv6Addresses", "false");
      
      mClientBootstrap =
          new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(),
              Executors.newCachedThreadPool(), 1));
      mClientBootstrap.setPipelineFactory(new LBSClientPipelineFactory());
      mClientBootstrap.setOption("tcpNoDelay", true);
      mClientBootstrap.setOption("keepAlive", true);
      ChannelFuture connectFuture =
          mClientBootstrap.connect(new InetSocketAddress(LBS_SERVER, LBS_SERVER_PORT));
      if (connectFuture.awaitUninterruptibly(1000) && connectFuture.isDone()
          && connectFuture.isSuccess()) {
        mChannel = connectFuture.getChannel();
        mHandler = mChannel.getPipeline().get(LBSClientRequestClientHandler.class);
        mHandler.setClientListener(new ClientListener() {
          @Override
          public void onClosed() {
            if (mStartup) {
              LBSService.this.stopSelf();
            }
          }

          @Override
          public void onPeersUpdated(List<User> peers) {
            mApplication.setPeers(peers);
            
            Intent intent = new Intent();
            intent.setAction(Intents.ACTION_PEERS_UPDATED);
            sendBroadcast(intent);
          }
        });
        
        mStartup = true;

        startHeartbeat();

        if (!TextUtils.isEmpty(Config.getUserName(LBSService.this))) {
          Log.d(TAG, Config.getAvatar(LBSService.this));
          mHandler.sendClientInfo(Config.getUserId(LBSService.this),
              Config.getUserName(LBSService.this), Config.getAvatar(LBSService.this),
              NetworkUtils.getNetworkType(LBSService.this));
        } else {
          Log.e(TAG, "no client info");
        }

        startLocationClient();

        Log.d(TAG, "connect success!");
      } else {
        Log.d(TAG, "connect failed!");
      }
    }
  }

  private void startHeartbeat() {
    Intent heartbeatIntent = new Intent(this, LBSService.class);
    heartbeatIntent.setAction(Intents.ACTION_HEARTBEAT);
    mHeartbeat = PendingIntent.getService(this, 0, heartbeatIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    mAlarmManager.setRepeating(AlarmManager.RTC, new Date().getTime(), 60000, mHeartbeat);
  }
  
  private void stopHeartbeat() {
    if (mHeartbeat != null) {
      mAlarmManager.cancel(mHeartbeat);
    }
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

}