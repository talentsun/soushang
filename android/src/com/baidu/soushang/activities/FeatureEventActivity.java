package com.baidu.soushang.activities;

import java.util.ArrayList;
import java.util.List;

import com.baidu.soushang.Intents;
import com.baidu.soushang.R;
import com.baidu.soushang.SouShangApplication;
import com.baidu.soushang.cloudapis.FeatureEvent;
import com.baidu.soushang.utils.JsonTool;
import com.baidu.soushang.views.LoadingView;
import com.baidu.soushang.widgets.FeatureDialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

public class FeatureEventActivity extends BaseActivity {

  private SouShangApplication mApplication;
  private TextView mNoEvent;
  private LoadingView mLoading;
  private Typeface mTypeface;
  private EventAdapter mAdapter;
  private static final String FEATURE_EVENT_URL =
      "http://sou.baidu.com/Devent/getRooms.html?access_token=%s";
  private FeatureDialog mFeatureDialog;
  private GridView mGridView;

  @Override
  protected void onCreate(Bundle arg0) {
    setContentView(R.layout.feature_event);
    mGridView = (GridView) findViewById(R.id.feature_grid);
    mTypeface = Typeface.createFromAsset(getAssets(),
        SouShangApplication.FONT);
    mGridView.setEmptyView(findViewById(android.R.id.empty));
    mLoading = (LoadingView) findViewById(R.id.loading);
    mNoEvent = (TextView) findViewById(R.id.no_event);
    mNoEvent.setTypeface(mTypeface);
    mFeatureDialog = new FeatureDialog(this);
    mApplication = (SouShangApplication) getApplication();
    mFeatureDialog.setOnClickListener(new FeatureDialog.OnClickListener() {
      @Override
      public void onResume() {}

      @Override
      public void onHome() {
        finish();
      }
    });
    showLoading();
    mAdapter = new EventAdapter(this);
    mGridView.setAdapter(mAdapter);
    GetFeatureEvents getFeatureEvents = new GetFeatureEvents();
    getFeatureEvents.execute("");
    mGridView.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view,
          int position, long id) {
        FeatureEvent featureEvent = (FeatureEvent) mAdapter.getItem(position);
        if (featureEvent != null) {
          if (featureEvent.isRunning() && !featureEvent.isFinished()) {
            mApplication.setFeatureEvent(featureEvent);
            Intent intent = new Intent(FeatureEventActivity.this, QuestionActivity.class);
            intent.putExtra(Intents.EXTRA_EVENT_TYPE,
                Intents.EVENT_TYPE_FEATURE);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
          } else if (featureEvent.isRunning() && featureEvent.isFinished()) {
            mFeatureDialog.setFeatureEvent(featureEvent);
            mFeatureDialog.show();
          }
        }
      }
    });

    super.onCreate(arg0);
  }

  class GetFeatureEvents extends
      AsyncTask<String, String, List<FeatureEvent>> {
    public GetFeatureEvents() {}

    @Override
    protected List<FeatureEvent> doInBackground(String... params) {
      return JsonTool.getFeatureData(FEATURE_EVENT_URL,
          FeatureEventActivity.this);
    }

    @Override
    protected void onPostExecute(List<FeatureEvent> result) {
      if (result != null) {
        mAdapter.setData(result);
      } else {
        showNoEvent();
      }
      super.onPostExecute(result);
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  @Override
  protected void onStart() {
    super.onStart();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onStop() {
    super.onStop();
  }

  private void showLoading() {
    mNoEvent.setVisibility(View.GONE);
    mLoading.show();
  }

  private void showNoEvent() {
    mNoEvent.setVisibility(View.VISIBLE);
    mLoading.hide();
  }

  public class EventAdapter extends BaseAdapter {
    private List<FeatureEvent> mData;
    private LayoutInflater mInflater;

    public List<FeatureEvent> getData() {
      return mData;
    }

    public void setData(List<FeatureEvent> data) {
      mData.clear();
      if (data != null) {
        mData.addAll(data);
        FeatureEvent featureEvent = new FeatureEvent();
        featureEvent.setIsStartPoint(true);
        mData.add(0, featureEvent);
      }
      notifyDataSetChanged();
    }

    public void clearData() {
      mData.clear();
      notifyDataSetChanged();
    }

    public EventAdapter(Context context) {
      super();
      mData = new ArrayList<FeatureEvent>();
      mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
      int count = 0;
      int length = mData.size();
      if (length >= 1 && length <= 3) {
        count = 1;
      } else if (length == 4) {
        count = 2;
      } else if (length == 5) {
        count = 3;
      } else if (length >= 6 && length <= 9) {
        count = 4;
      } else if (length == 10) {
        count = 5;
      } else if (length >= 11 && length <= 13) {
        count = 6;
      } else if (length == 14) {
        count = 7;
      } else if (length >= 15 && length <= 16) {
        count = 8;
      } else if (length == 17) {
        count = 9;
      } else if (length >= 18 && length <= 20) {
        count = 10;
      } else if (length >= 21 && length <= 22) {
        count = 11;
      } else if (length >= 22 && length <= 23) {
        count = 12;
      }
      return count * 5;
    }

    @Override
    public Object getItem(int position) {
      FeatureEvent featureEvent = null;
      if (mData != null && mData.size() >= 0) {
        switch (position) {
          case 1:
            if (mData.size() > 2) {
              featureEvent = mData.get(2);
              if (featureEvent.getCat().equalsIgnoreCase(mData.get(1).getCat())) {
                featureEvent.setIsPractice(false);
              } else {
                featureEvent.setIsPractice(true);
              }
            }
            return featureEvent;
          case 2:
            if (mData.size() > 1) {
              featureEvent = mData.get(1);
              featureEvent.setIsPractice(true);
            }
            return featureEvent;
          case 3:
            if (mData.size() > 0) {
              featureEvent = mData.get(0);
              featureEvent.setIsStartPoint(true);
            }
            return featureEvent;
          case 6:
            if (mData.size() > 3) {
              featureEvent = mData.get(3);
              if (featureEvent.getCat().equalsIgnoreCase(mData.get(2).getCat())) {
                featureEvent.setIsPractice(false);
              } else {
                featureEvent.setIsPractice(true);
              }
            }
            return featureEvent;
          case 11:
            if (mData.size() > 4) {
              featureEvent = mData.get(4);
              if (featureEvent.getCat().equalsIgnoreCase(mData.get(3).getCat())) {
                featureEvent.setIsPractice(false);
              } else {
                featureEvent.setIsPractice(true);
              }
            }
            return featureEvent;
          case 16:
            if (mData.size() > 5) {
              featureEvent = mData.get(5);
              if (featureEvent.getCat().equalsIgnoreCase(mData.get(4).getCat())) {
                featureEvent.setIsPractice(false);
              } else {
                featureEvent.setIsPractice(true);
              }
            }
            return featureEvent;
          case 17:
            if (mData.size() > 6) {
              featureEvent = mData.get(6);
              if (featureEvent.getCat().equalsIgnoreCase(mData.get(5).getCat())) {
                featureEvent.setIsPractice(false);
              } else {
                featureEvent.setIsPractice(true);
              }
            }
            return featureEvent;
          case 18:
            if (mData.size() > 7) {
              featureEvent = mData.get(7);
              if (featureEvent.getCat().equalsIgnoreCase(mData.get(6).getCat())) {
                featureEvent.setIsPractice(false);
              } else {
                featureEvent.setIsPractice(true);
              }
            }
            return featureEvent;
          case 19:
            if (mData.size() > 8) {
              featureEvent = mData.get(8);
              if (featureEvent.getCat().equalsIgnoreCase(mData.get(7).getCat())) {
                featureEvent.setIsPractice(false);
              } else {
                featureEvent.setIsPractice(true);
              }
            }
            return featureEvent;
          case 24:
            if (mData.size() > 9) {
              featureEvent = mData.get(9);
              if (featureEvent.getCat().equalsIgnoreCase(mData.get(8).getCat())) {
                featureEvent.setIsPractice(false);
              } else {
                featureEvent.setIsPractice(true);
              }
            }
            return featureEvent;
          case 27:
            if (mData.size() > 12) {
              featureEvent = mData.get(12);
              if (featureEvent.getCat().equalsIgnoreCase(mData.get(11).getCat())) {
                featureEvent.setIsPractice(false);
              } else {
                featureEvent.setIsPractice(true);
              }
            }
            return featureEvent;
          case 28:
            if (mData.size() > 11) {
              featureEvent = mData.get(11);
              if (featureEvent.getCat().equalsIgnoreCase(mData.get(10).getCat())) {
                featureEvent.setIsPractice(false);
              } else {
                featureEvent.setIsPractice(true);
              }
            }
            return featureEvent;
          case 29:
            if (mData.size() > 10) {
              featureEvent = mData.get(10);
              if (featureEvent.getCat().equalsIgnoreCase(mData.get(9).getCat())) {
                featureEvent.setIsPractice(false);
              } else {
                featureEvent.setIsPractice(true);
              }
            }
            return featureEvent;
          case 32:
            if (mData.size() > 13) {
              featureEvent = mData.get(13);
              if (featureEvent.getCat().equalsIgnoreCase(mData.get(12).getCat())) {
                featureEvent.setIsPractice(false);
              } else {
                featureEvent.setIsPractice(true);
              }
            }
            return featureEvent;
          case 36:
            if (mData.size() > 15) {
              featureEvent = mData.get(15);
              if (featureEvent.getCat().equalsIgnoreCase(mData.get(14).getCat())) {
                featureEvent.setIsPractice(false);
              } else {
                featureEvent.setIsPractice(true);
              }
            }
            return featureEvent;
          case 37:
            if (mData.size() > 14) {
              featureEvent = mData.get(14);
              if (featureEvent.getCat().equalsIgnoreCase(mData.get(13).getCat())) {
                featureEvent.setIsPractice(false);
              } else {
                featureEvent.setIsPractice(true);
              }
            }
            return featureEvent;
          case 41:
            if (mData.size() > 16) {
              featureEvent = mData.get(16);
              if (featureEvent.getCat().equalsIgnoreCase(mData.get(15).getCat())) {
                featureEvent.setIsPractice(false);
              } else {
                featureEvent.setIsPractice(true);
              }
            }
            return featureEvent;
          case 46:
            if (mData.size() > 17) {
              featureEvent = mData.get(17);
              if (featureEvent.getCat().equalsIgnoreCase(mData.get(16).getCat())) {
                featureEvent.setIsPractice(false);
              } else {
                featureEvent.setIsPractice(true);
              }
            }
            return featureEvent;
          case 47:
            if (mData.size() > 18) {
              featureEvent = mData.get(18);
              if (featureEvent.getCat().equalsIgnoreCase(mData.get(17).getCat())) {
                featureEvent.setIsPractice(false);
              } else {
                featureEvent.setIsPractice(true);
              }
            }
            return featureEvent;
          case 48:
            if (mData.size() > 19) {
              featureEvent = mData.get(19);
              if (featureEvent.getCat().equalsIgnoreCase(mData.get(18).getCat())) {
                featureEvent.setIsPractice(false);
              } else {
                featureEvent.setIsPractice(true);
              }
            }
            return featureEvent;
          case 52:
            if (mData.size() > 21) {
              featureEvent = mData.get(21);
              if (featureEvent.getCat().equalsIgnoreCase(mData.get(20).getCat())) {
                featureEvent.setIsPractice(false);
              } else {
                featureEvent.setIsPractice(true);
              }
            }
            return featureEvent;
          case 53:
            if (mData.size() > 20) {
              featureEvent = mData.get(20);
              if (featureEvent.getCat().equalsIgnoreCase(mData.get(19).getCat())) {
                featureEvent.setIsPractice(false);
              } else {
                featureEvent.setIsPractice(true);
              }
            }
            return featureEvent;
          case 57:
            if (mData.size() > 22) {
              featureEvent = mData.get(22);
              if (featureEvent.getCat().equalsIgnoreCase(mData.get(21).getCat())) {
                featureEvent.setIsPractice(false);
              } else {
                featureEvent.setIsPractice(true);
              }
            }
            return featureEvent;
        }
      }

      return null;
    }

    @Override
    public long getItemId(int position) {
      Object item = getItem(position);
      if (item != null) {
        return ((FeatureEvent) item).getId();
      } else {
        return -1;
      }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder viewHolder = null;
      if (convertView == null || convertView.getTag() == null) {
        viewHolder = new ViewHolder();
        convertView = mInflater.inflate(R.layout.feature_event_item,
            parent, false);
        viewHolder.Mark = (TextView) convertView.findViewById(R.id.feature_event_item_mark);
        viewHolder.Back =
            (FrameLayout) convertView.findViewById(R.id.feature_event_item_back);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }

      Object item = getItem(position);
      if (item != null) {
        FeatureEvent event = (FeatureEvent) item;
        if (event.isRunning()) {
          setBack(event, viewHolder.Back, viewHolder.Mark);
        } else {
          viewHolder.Back.setVisibility(View.VISIBLE);
          if (event.isIsStartPoint()) {
            viewHolder.Back.setBackgroundResource(R.drawable.event_start);
            viewHolder.Mark.setVisibility(View.GONE);
          } else {
            viewHolder.Back.setBackgroundResource(R.drawable.event_lock);
            viewHolder.Mark.setVisibility(View.GONE);
          }
        }
      } else {
        viewHolder.Back.setVisibility(View.GONE);
        viewHolder.Mark.setVisibility(View.GONE);
      }

      return convertView;
    }



    private void setBack(FeatureEvent featureEvent, FrameLayout featureBack, TextView featureMark) {
      featureBack.setVisibility(View.VISIBLE);
      featureMark.setVisibility(View.VISIBLE);
      if ("lbs".equalsIgnoreCase(featureEvent.getCat())) {
        if (featureEvent.isIsPractice()) {
          if (!featureEvent.isFinished()) {
            featureBack.setBackgroundResource(R.drawable.lbs_ex_unlock);
            featureMark.setVisibility(View.GONE);
          } else {
            featureBack.setBackgroundResource(R.drawable.lbs_ex_complete);
            featureMark.setVisibility(View.VISIBLE);
            featureMark.setText(featureEvent.getScore() + "");
          }
        } else {
          if (!featureEvent.isFinished()) {
            featureBack.setBackgroundResource(R.drawable.lbs_game_unlock);
            featureMark.setVisibility(View.GONE);
          } else {
            featureBack.setBackgroundResource(R.drawable.lbs_game_complete);
            featureMark.setVisibility(View.VISIBLE);
            featureMark.setText(featureEvent.getScore() + "");
          }
        }
      }

      else if ("music".equalsIgnoreCase(featureEvent.getCat())) {
        if (featureEvent.isIsPractice()) {
          if (!featureEvent.isFinished()) {
            featureBack.setBackgroundResource(R.drawable.music_ex_unlock);
            featureMark.setVisibility(View.GONE);
          } else {
            featureBack.setBackgroundResource(R.drawable.music_ex_complete);
            featureMark.setVisibility(View.VISIBLE);
            featureMark.setText(featureEvent.getScore() + "");
          }
        } else {
          if (!featureEvent.isFinished()) {
            featureBack.setBackgroundResource(R.drawable.music_game_unlock);
            featureMark.setVisibility(View.GONE);
          } else {
            featureBack.setBackgroundResource(R.drawable.music_game_complete);
            featureMark.setVisibility(View.VISIBLE);
            featureMark.setText(featureEvent.getScore() + "");
          }
        }
      }

      else if ("med".equalsIgnoreCase(featureEvent.getCat())) {
        if (featureEvent.isIsPractice()) {
          if (!featureEvent.isFinished()) {
            featureBack.setBackgroundResource(R.drawable.pill_ex_unlock);
            featureMark.setVisibility(View.GONE);
          } else {
            featureBack.setBackgroundResource(R.drawable.pill_ex_complete);
            featureMark.setVisibility(View.VISIBLE);
            featureMark.setText(featureEvent.getScore() + "");
          }
        } else {
          if (!featureEvent.isFinished()) {
            featureBack.setBackgroundResource(R.drawable.pill_game_unlock);
            featureMark.setVisibility(View.GONE);
          } else {
            featureBack.setBackgroundResource(R.drawable.pill_game_complete);
            featureMark.setVisibility(View.VISIBLE);
            featureMark.setText(featureEvent.getScore() + "");
          }
        }
      }

      else if ("zongyi".equalsIgnoreCase(featureEvent.getCat())) {
        if (featureEvent.isIsPractice()) {
          if (!featureEvent.isFinished()) {
            featureBack.setBackgroundResource(R.drawable.show_ex_unlock);
            featureMark.setVisibility(View.GONE);
          } else {
            featureBack.setBackgroundResource(R.drawable.show_ex_complete);
            featureMark.setVisibility(View.VISIBLE);
            featureMark.setText(featureEvent.getScore() + "");
          }
        } else {
          if (!featureEvent.isFinished()) {
            featureBack.setBackgroundResource(R.drawable.show_game_unlock);
            featureMark.setVisibility(View.GONE);
          } else {
            featureBack.setBackgroundResource(R.drawable.show_game_complete);
            featureMark.setVisibility(View.VISIBLE);
            featureMark.setText(featureEvent.getScore() + "");
          }
        }
      }

      else if ("other".equalsIgnoreCase(featureEvent.getCat())) {
        if (featureEvent.isIsPractice()) {
          if (!featureEvent.isFinished()) {
            featureBack.setBackgroundResource(R.drawable.others_ex_unlock);
            featureMark.setVisibility(View.GONE);
          } else {
            featureBack.setBackgroundResource(R.drawable.others_ex_complete);
            featureMark.setVisibility(View.VISIBLE);
            featureMark.setText(featureEvent.getScore() + "");
          }
        } else {
          if (!featureEvent.isFinished()) {
            featureBack.setBackgroundResource(R.drawable.others_game_unlock);
            featureMark.setVisibility(View.GONE);
          } else {
            featureBack.setBackgroundResource(R.drawable.others_game_complete);
            featureMark.setVisibility(View.VISIBLE);
            featureMark.setText(featureEvent.getScore() + "");
          }
        }
      }

      else if ("cartoon".equalsIgnoreCase(featureEvent.getCat())) {
        if (featureEvent.isIsPractice()) {
          if (!featureEvent.isFinished()) {
            featureBack.setBackgroundResource(R.drawable.cartoon_ex_unlock);
            featureMark.setVisibility(View.GONE);
          } else {
            featureBack.setBackgroundResource(R.drawable.cartoon_ex_complete);
            featureMark.setVisibility(View.VISIBLE);
            featureMark.setText(featureEvent.getScore() + "");
          }
        } else {
          if (!featureEvent.isFinished()) {
            featureBack.setBackgroundResource(R.drawable.cartoon_game_unlock);
            featureMark.setVisibility(View.GONE);
          } else {
            featureBack.setBackgroundResource(R.drawable.cartoon_game_complete);
            featureMark.setVisibility(View.VISIBLE);
            featureMark.setText(featureEvent.getScore() + "");
          }
        }
      }

      else if ("movie".equalsIgnoreCase(featureEvent.getCat())) {
        if (featureEvent.isIsPractice()) {
          if (!featureEvent.isFinished()) {
            featureBack.setBackgroundResource(R.drawable.movie_ex_unlock);
            featureMark.setVisibility(View.GONE);
          } else {
            featureBack.setBackgroundResource(R.drawable.movie_ex_complete);
            featureMark.setVisibility(View.VISIBLE);
            featureMark.setText(featureEvent.getScore() + "");
          }
        } else {
          if (!featureEvent.isFinished()) {
            featureBack.setBackgroundResource(R.drawable.movie_game_unlock);
            featureMark.setVisibility(View.GONE);
          } else {
            featureBack.setBackgroundResource(R.drawable.movie_game_complete);
            featureMark.setVisibility(View.VISIBLE);
            featureMark.setText(featureEvent.getScore() + "");
          }
        }
      }

      else if ("video".equalsIgnoreCase(featureEvent.getCat())) {
        if (featureEvent.isIsPractice()) {
          if (!featureEvent.isFinished()) {
            featureBack.setBackgroundResource(R.drawable.tv_ex_unlock);
            featureMark.setVisibility(View.GONE);
          } else {
            featureBack.setBackgroundResource(R.drawable.tv_ex_complete);
            featureMark.setVisibility(View.VISIBLE);
            featureMark.setText(featureEvent.getScore() + "");
          }
        } else {
          if (!featureEvent.isFinished()) {
            featureBack.setBackgroundResource(R.drawable.tv_game_unlock);
            featureMark.setVisibility(View.GONE);
          } else {
            featureBack.setBackgroundResource(R.drawable.tv_game_complete);
            featureMark.setVisibility(View.VISIBLE);
            featureMark.setText(featureEvent.getScore() + "");
          }
        }
      }

      else if ("digital".equalsIgnoreCase(featureEvent.getCat())) {
        if (featureEvent.isIsPractice()) {
          if (!featureEvent.isFinished()) {
            featureBack.setBackgroundResource(R.drawable.digital_ex_unlock);
            featureMark.setVisibility(View.GONE);
          } else {
            featureBack.setBackgroundResource(R.drawable.digital_ex_complete);
            featureMark.setVisibility(View.VISIBLE);
            featureMark.setText(featureEvent.getScore() + "");
          }
        } else {
          if (!featureEvent.isFinished()) {
            featureBack.setBackgroundResource(R.drawable.digital_game_unlock);
            featureMark.setVisibility(View.GONE);
          } else {
            featureBack.setBackgroundResource(R.drawable.digital_game_complete);
            featureMark.setVisibility(View.VISIBLE);
            featureMark.setText(featureEvent.getScore() + "");
          }
        }
      }

      else if ("star".equalsIgnoreCase(featureEvent.getCat())) {
        if (featureEvent.isIsPractice()) {
          if (!featureEvent.isFinished()) {
            featureBack.setBackgroundResource(R.drawable.peo_ex_unlock);
            featureMark.setVisibility(View.GONE);
          } else {
            featureBack.setBackgroundResource(R.drawable.peo_ex_complete);
            featureMark.setVisibility(View.VISIBLE);
            featureMark.setText(featureEvent.getScore() + "");
          }
        } else {
          if (!featureEvent.isFinished()) {
            featureBack.setBackgroundResource(R.drawable.peo_game_unlock);
            featureMark.setVisibility(View.GONE);
          } else {
            featureBack.setBackgroundResource(R.drawable.peo_game_complete);
            featureMark.setVisibility(View.VISIBLE);
            featureMark.setText(featureEvent.getScore() + "");
          }
        }
      }

      else if ("auto".equalsIgnoreCase(featureEvent.getCat())) {
        if (featureEvent.isIsPractice()) {
          if (!featureEvent.isFinished()) {
            featureBack.setBackgroundResource(R.drawable.car_ex_unlock);
            featureMark.setVisibility(View.GONE);
          } else {
            featureBack.setBackgroundResource(R.drawable.car_ex_complete);
            featureMark.setVisibility(View.VISIBLE);
            featureMark.setText(featureEvent.getScore() + "");
          }
        } else {
          if (!featureEvent.isFinished()) {
            featureBack.setBackgroundResource(R.drawable.car_game_unlock);
            featureMark.setVisibility(View.GONE);
          } else {
            featureBack.setBackgroundResource(R.drawable.car_game_complete);
            featureMark.setVisibility(View.VISIBLE);
            featureMark.setText(featureEvent.getScore() + "");
          }
        }
      }
    }

    class ViewHolder {
      TextView Mark;
      FrameLayout Back;
    }
  }

}
