package com.michael.tinygame.touch.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.michael.tinygame.touch.R;
import com.michael.tinygame.touch.setting.SettingManager;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TouchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TouchFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class TouchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView mTouchView;
    private TextView mTouchCountTV;
    private TextView mHistoryTV;
    private TextView mTimeTV;
    private int mRoundTouchCount;

    private DecimalFormat mDF = new DecimalFormat("0.00");
    private long mStartTime;
    private long mCurTime;
    private boolean mFinishRound;
    private boolean mDialogShow;
    private static final long TIME_ROUNG = 10 * 1000;

    private AlertDialog mFinishDialog;

    private OnFragmentInteractionListener mListener;

    private static final int UPDATE_TIME = 10000;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TIME:
                    long deta = System.currentTimeMillis() - mStartTime;
                    long leftTime = TIME_ROUNG - deta;
                    if (leftTime <= 0) {
                        //show finish
                        mTimeTV.setText(String.format(getString(R.string.time_tv), "0.00"));
                        if (mRoundTouchCount > 0) {
                            if (mRoundTouchCount > SettingManager.getInstance().getTouchBestCount()) {
                                SettingManager.getInstance().setTouchBestCount(mRoundTouchCount);
                                mHistoryTV.setText(String.format(getString(R.string.history_count_tv),
                                                                    SettingManager.getInstance().getTouchBestCount()));
                            }
                        }

                        showFinishDialog();
                        mFinishRound = true;
                    } else {
                        mTimeTV.setText(String.format(getString(R.string.time_tv),
                                                         mDF.format(Double.valueOf(leftTime * 1.0 / 1000))));
                        mHandler.sendEmptyMessage(UPDATE_TIME);
                    }
                    break;
            }
        }
    };

    public static TouchFragment newInstance() {
        TouchFragment fragment = new TouchFragment();
//        Bundle args = new Bundle();
//        args.putSerializable(ARG_SERIES, series);
//        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TouchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TouchFragment newInstance(String param1, String param2) {
        TouchFragment fragment = new TouchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public TouchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.touch_fragment, null);
        initView(root);

        mTouchCountTV.setText(String.format(getString(R.string.touch_count_tv), mRoundTouchCount));
        mHistoryTV.setText(String.format(getString(R.string.history_count_tv)
                                            , SettingManager.getInstance().getTouchBestCount()));
        mTimeTV.setText(String.format(getString(R.string.time_tv), 10.00));

        initDialog();

        return root;
    }

    private void initView(View rootView) {
        mTouchView = (ImageView) rootView.findViewById(R.id.touch);
        mTouchCountTV = (TextView) rootView.findViewById(R.id.count);
        mHistoryTV = (TextView) rootView.findViewById(R.id.history);
        mTimeTV = (TextView) rootView.findViewById(R.id.time);

        mTouchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP
                    || event.getAction() == MotionEvent.ACTION_POINTER_UP){
                    if (mFinishRound) {
                        showFinishDialog();
                        return false;
                    }

                    if (mRoundTouchCount == 0) {
                        mStartTime = System.currentTimeMillis();
                        mHandler.sendEmptyMessage(UPDATE_TIME);
                    }

                    mRoundTouchCount++;
                    mTouchCountTV.setText(String.format(getString(R.string.touch_count_tv), mRoundTouchCount));
                }

                return false;
            }
        });

        mTouchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mFinishRound) {
//                    showFinishDialog();
//                    return;
//                }
//
//                if (mRoundTouchCount == 0) {
//                    mStartTime = System.currentTimeMillis();
//                    mHandler.sendEmptyMessage(UPDATE_TIME);
//                }
//
//                mRoundTouchCount++;
//                mTouchCountTV.setText(String.format(getString(R.string.touch_count_tv), mRoundTouchCount));
            }
        });
    }

    private void initDialog() {
        mFinishDialog = new AlertDialog.Builder(getActivity())
                            .setTitle("没时间啦")
                            .setMessage("什么情况？手机坏了还是手指坏了？路还很远，加油。")
                            .setNegativeButton("不玩了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setPositiveButton("在玩一把", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    resetView();
                                }
                            })
                            .create();
        mFinishDialog.setCanceledOnTouchOutside(false);
    }

    private void showFinishDialog() {
        if (mFinishDialog.isShowing()) {
            mFinishDialog.cancel();
        }
        mFinishDialog.show();
    }

    public void resetView() {
        mFinishRound = false;
        mRoundTouchCount = 0;
        mTouchCountTV.setText(String.format(getString(R.string.touch_count_tv), 0));
        mHistoryTV.setText(String.format(getString(R.string.history_count_tv), SettingManager.getInstance().getTouchBestCount()));
        mTimeTV.setText(String.format(getString(R.string.time_tv), 10.00));
        mHandler.removeCallbacksAndMessages(null);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
