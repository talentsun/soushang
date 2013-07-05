package com.baidu.soushang.lbs;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.baidu.soushang.Intents;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class LBSService extends Service {
  private static final String LBS_SERVER = "118.244.225.222";
  private static final int LBS_SERVER_PORT = 9300;
  
  private ClientBootstrap mClientBootstrap;
  private Channel mChannel;
  private LBSClientRequestClientHandler mHandler;
  private PendingIntent mHeartbeat;
  private AlarmManager mAlarmManager;
  
  private boolean mStartup = false;

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onCreate() {
    if (!mStartup) {
      mClientBootstrap =
          new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(),
              Executors.newCachedThreadPool(), 1));
      mClientBootstrap.setPipelineFactory(new LBSClientPipelineFactory());
      ChannelFuture connectFuture =
          mClientBootstrap.connect(new InetSocketAddress(LBS_SERVER, LBS_SERVER_PORT));
      if (connectFuture.awaitUninterruptibly(1000)) {
        mChannel = connectFuture.getChannel();
        mHandler = mChannel.getPipeline().get(LBSClientRequestClientHandler.class);
        mStartup = true;
      }
    }
    
    AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    Intent heartbeatIntent = new Intent(this, LBSService.class);
    heartbeatIntent.setAction(Intents.ACTION_HEARTBEAT);
    mHeartbeat = PendingIntent.getService(this, 0, heartbeatIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    mAlarmManager.setRepeating(AlarmManager.RTC, new Date().getTime(), 60000, mHeartbeat);
    
    super.onCreate();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    if (intent != null) {
      String action = intent.getAction();
      if (Intents.ACTION_HEARTBEAT.equalsIgnoreCase(action)) {
        mHandler.sendHeartbeat();
      } else {
        
      }
    }
    return super.onStartCommand(intent, flags, startId);
  }

  @Override
  public void onDestroy() {
    if (mStartup) {
      mChannel.close().awaitUninterruptibly();
      mClientBootstrap.releaseExternalResources();
    }
    mAlarmManager.cancel(mHeartbeat);
    super.onDestroy();
  }

}
